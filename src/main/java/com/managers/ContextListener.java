package com.managers;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.scheduler.SchedulerEngineUtil;
import com.server.CacheInvalidator;
import com.server.ServerRegistryDao;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@WebListener
public class ContextListener implements ServletContextListener {

    private Logger logger;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        loadLoggingConfig();
        logger = Logger.getLogger(ContextListener.class.getName());

        logger.info("Web application is starting. Initializing services...");

        try {
            // starting services
//            SessionManager.startScheduler();
//            OAuthManager.startScheduler();
        	SchedulerEngineUtil.startScheduledTasks();

            String serverIp = getServerIp();
            int serverPort = getServerPort();
            ServerRegistryDao.registerServer(serverIp, serverPort);
            logger.info("Registered server: " + serverIp + ":" + serverPort);

            CacheInvalidator.broadcastServerCacheInvalidation();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during context initialization", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Web application is shutting down. Cleaning up resources...");

        try {
            SessionManager.updateSessionsInDB();

            // deregistering
            String serverIp = getServerIp();
            int serverPort = getServerPort();
            ServerRegistryDao.deregisterServer(serverIp, serverPort);
            logger.info("Deregistered server: " + serverIp + ":" + serverPort);

            SessionManager.shutdown();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during context destruction", e);
        }
    }

    private String getServerIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    private int getServerPort() {
        try {
            Context ctx = new InitialContext();
            return (Integer) ctx.lookup("java:comp/env/server.port");
        } catch (NamingException e) {
            logger.warning("Failed to retrieve server port from JNDI. Using default port 8088.");
            return 8088;
        }
    }

    private void loadLoggingConfig() {
        try (InputStream is = ContextListener.class.getClassLoader().getResourceAsStream("logging.properties")) {
            if (is != null) {
                LogManager.getLogManager().readConfiguration(is);
            } else {
                System.err.println("logging.properties not found.");
            }
        } catch (IOException e) {
            System.err.println("Failed to load logging configuration: " + e.getMessage());
        }
    }
}
