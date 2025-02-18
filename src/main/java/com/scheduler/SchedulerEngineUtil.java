package com.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import com.managers.OAuthManager;
import com.managers.SessionManager;

public class SchedulerEngineUtil {
    private static final Logger LOGGER = Logger.getLogger(SchedulerEngineUtil.class.getName());
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    
    public static void startScheduledTasks() {
        scheduler.scheduleWithFixedDelay(OAuthManager.oauthScheduledTask, 1, 1, TimeUnit.MINUTES);
        scheduler.scheduleWithFixedDelay(SessionManager.sessionScheduledTask, 1, 5, TimeUnit.MINUTES);
        LOGGER.info("Scheduled tasks started");
    }

    public static void stopScheduledTasks() {
        try {
            scheduler.shutdown();
            scheduler.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}