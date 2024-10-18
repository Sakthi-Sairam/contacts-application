package com.managers;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class SessionContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Web application is starting. Initializing SessionManager...");
        
//        SessionManager.initialize();
        SessionManager.startScheduler();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Web application is shutting down. Cleaning up SessionManager...");
        
        SessionManager.shutdown();
    }
}
