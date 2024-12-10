package com.managers;

import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;
import java.io.IOException;

public class LoggerManager {
    private static Logger accessLogger = Logger.getLogger("AccessLog");
    private static Logger appLogger = Logger.getLogger("AppLog");

    static {
        try {
            // File Handler for Access Log
//            FileHandler accessFileHandler = new FileHandler("/home/sakthi-pt7694/Desktop/jeeProjects1/contactsLogs/"+System.currentTimeMillis()+"_access.log", true);
        	FileHandler accessFileHandler = new FileHandler("/home/sakthi-pt7694/Desktop/jeeProjects1/contactsLogs/access.log", true);

        	accessFileHandler.setFormatter(new SimpleFormatter());
            accessLogger.addHandler(accessFileHandler);

            // File Handler for Application Log
//            FileHandler appFileHandler = new FileHandler("/home/sakthi-pt7694/Desktop/jeeProjects1/contactsLogs/"+System.currentTimeMillis()+"_app.log", true);
            FileHandler appFileHandler = new FileHandler("/home/sakthi-pt7694/Desktop/jeeProjects1/contactsLogs/app.log", true);

            appFileHandler.setFormatter(new SimpleFormatter());
            appLogger.addHandler(appFileHandler);

            // Console Handler for Application Log
            ConsoleHandler consoleHandler = new ConsoleHandler();
            appLogger.addHandler(consoleHandler);

        } catch (IOException e) {
            appLogger.severe("Failed to initialize log handlers: " + e.getMessage());
        }
    }

    public static Logger getAccessLogger() {
        return accessLogger;
    }

    public static Logger getAppLogger() {
        return appLogger;
    }
}
