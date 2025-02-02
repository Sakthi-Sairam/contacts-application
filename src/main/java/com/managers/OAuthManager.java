package com.managers;

import java.util.List;
import java.util.concurrent.*;

import org.json.JSONObject;

import com.models.OAuthToken;
import com.oauth.OAuthDao;
import com.oauth.OAuthService;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OAuthManager {

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final ExecutorService workerPool = Executors.newFixedThreadPool(5); // Limit concurrent tasks
    private static final Logger LOGGER = Logger.getLogger(OAuthManager.class.getName());
    private static final OAuthService oAuthService = new OAuthService();

    public static void startScheduler() {
        LOGGER.info("Starting OAuth token refresh scheduler");

        scheduler.scheduleAtFixedRate(() -> {
            LOGGER.info("OAuthManager scheduler execution started");

            try {
                List<OAuthToken> authTokens = OAuthDao.getAllOAuthTokens();
                for (OAuthToken token : authTokens) {
                    workerPool.submit(() -> processToken(token));
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error during scheduled OAuth token processing", e);
            }

            LOGGER.info("OAuthManager scheduler execution completed");
        }, 1, 1, TimeUnit.MINUTES);
    }

    private static void processToken(OAuthToken token) {
        try {
            JSONObject res = oAuthService.getAccessTokenWithRefreshToken(token.getRefreshToken());
            String accessToken = res.optString("access_token", null);
            if (accessToken == null) return;
            oAuthService.syncContacts(accessToken, token.getUserId());
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
