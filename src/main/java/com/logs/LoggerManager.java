//package com.logs;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.logging.*;
//
//public class LoggerManager {
//    private static final String ACCESS_LOGGER_NAME = "access.logger";
//    private static final Logger ACCESS_LOGGER = Logger.getLogger(ACCESS_LOGGER_NAME);
//
//    static {
//        try (InputStream configStream = LoggerManager.class.getClassLoader()
//                .getResourceAsStream("logging.properties")) {
//            if (configStream == null) {
//                System.err.println("logging.properties file not found in classpath.");
//            } else {
//                LogManager.getLogManager().readConfiguration(configStream);
//                System.out.println("Logging configuration loaded from logging.properties");
//                
//                // Set up separate handler for access logger
//                FileHandler accessHandler = new FileHandler("access.log.%u.%g.txt", 5000000, 5, true);
//                accessHandler.setFormatter(new SimpleFormatter());
//                ACCESS_LOGGER.addHandler(accessHandler);
//                ACCESS_LOGGER.setUseParentHandlers(false);  // Don't use parent handlers
//            }
//        } catch (IOException e) {
//            System.err.println("Error loading logging configuration: " + e);
//        }
//    }
//
//    public static Logger getAccessLogger() {
//        return ACCESS_LOGGER;
//    }
//
//    public static Logger getLogger(String name) {
//        return Logger.getLogger(name);
//    }
//
//    public static void logAccess(String message) {
//        ACCESS_LOGGER.info(message);
//    }
//
//    public static void logInfo(String loggerName, String message) {
//        Logger.getLogger(loggerName).log(Level.INFO, message);
//    }
//
//    public static void logWarning(String loggerName, String message) {
//        Logger.getLogger(loggerName).log(Level.WARNING, message);
//    }
//
//    public static void logError(String loggerName, String message, Throwable thrown) {
//        Logger.getLogger(loggerName).log(Level.SEVERE, message, thrown);
//    }
//}