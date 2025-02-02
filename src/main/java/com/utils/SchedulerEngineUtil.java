package com.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulerEngineUtil {
	private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

	public static void schedule(Runnable task, long initialDelay, long period, TimeUnit unit) {
		executor.scheduleAtFixedRate(task, initialDelay, period, unit);
	}
    public static void shutdown() {
        executor.shutdown();
    }
}
