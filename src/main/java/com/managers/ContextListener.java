package com.managers;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.LogManager;

import com.exceptions.DaoException;
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

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Web application is starting. Initializing SessionManager...");
        
//        SessionManager.initialize();
        SessionManager.startScheduler();
        OAuthManager.startScheduler();
        try {
        	int port = getServerPort();
			ServerRegistryDao.registerServer(InetAddress.getLocalHost().getHostAddress(),port);
		} catch (UnknownHostException | DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        CacheInvalidator.broadcastServerCacheInvalidation();
        
        try (InputStream is =  ContextListener.class.getClassLoader().getResourceAsStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(is);
            System.out.println("loaded the logging.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    	SessionManager.updateSessionsInDB();
        System.out.println("Web application is shutting down. Cleaning up SessionManager...");
        try {
			ServerRegistryDao.deregisterServer(InetAddress.getLocalHost().getHostAddress(),8088);
		} catch (UnknownHostException | DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        SessionManager.shutdown();
    }
    
    private int getServerPort() {
        try {
            return (Integer) new InitialContext().lookup("java:comp/env/server.port");
        } catch (NamingException e) {
            e.printStackTrace();
            return 8088; // Default port
        }
    }
}
