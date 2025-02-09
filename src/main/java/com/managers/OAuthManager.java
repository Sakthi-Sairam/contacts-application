package com.managers;

import java.util.List;
import java.util.concurrent.*;

import org.json.JSONObject;

import com.exceptions.DaoException;
import com.models.OAuthToken;
import com.oauth.OAuthDao;
import com.oauth.OAuthService;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OAuthManager {

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final ExecutorService workerPool = Executors.newFixedThreadPool(5);
    private static final Logger LOGGER = Logger.getLogger(OAuthManager.class.getName());

    public static void startScheduler() {
        LOGGER.info("Starting OAuth token refresh scheduler");

        scheduler.scheduleAtFixedRate(() -> {
//            LOGGER.info("OAuthManager scheduler execution started");

            try {
                List<OAuthToken> tokens = OAuthDao.getAllOAuthTokens();
                long currentTime = System.currentTimeMillis();
                
                for (OAuthToken token : tokens) {
                	if(token.getSyncInterval()==0) {
                		continue;
                	}
                    long timeSinceLastSync = currentTime - token.getLastSync();
                    long requiredInterval = token.getSyncInterval() * 60000;
                    
                    if (timeSinceLastSync >= requiredInterval) {
                        workerPool.execute(() -> {
                            processToken(token);
                            try {
								OAuthDao.updateLastSync(token.getId());
							} catch (DaoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        });
                    }
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error in sync scheduler", e);
            }

//            LOGGER.info("OAuthManager scheduler execution completed");
        }, 1, 5, TimeUnit.MINUTES);
    }

    private static void processToken(OAuthToken token) {
        try {
            JSONObject res = OAuthService.getAccessTokenWithRefreshToken(token.getRefreshToken());
            String accessToken = res.optString("access_token", null);
            if (accessToken == null) return;
            OAuthService.syncContacts(accessToken, token.getUserId());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing OAuth token for user: " + token.getUserId(), e);
        }
    }

    public static void stopScheduler() {
        try {
            LOGGER.info("Shutting down OAuthManager scheduler...");
            scheduler.shutdown();
            workerPool.shutdown();
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
            if (!workerPool.awaitTermination(5, TimeUnit.SECONDS)) {
                workerPool.shutdownNow();
            }
            LOGGER.info("OAuthManager scheduler shut down successfully");
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Error shutting down OAuthManager scheduler", e);
            Thread.currentThread().interrupt();
        }
    }
}
