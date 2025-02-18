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
    private static final Logger LOGGER = Logger.getLogger(OAuthManager.class.getName());
    
    public static final Runnable oauthScheduledTask = () -> {
        LOGGER.info("OAuthManager scheduler execution started");
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        
        try {
            List<OAuthToken> tokens = OAuthDao.getAllOAuthTokens();
            long currentTime = System.currentTimeMillis();

            for (OAuthToken token : tokens) {
                if (token.getSyncInterval() > 0 && 
                    (currentTime - token.getLastSync()) >= token.getSyncInterval() * 60000) {
                    
                    executorService.execute(() -> {
                        processToken(token);
                        try {
                            OAuthDao.updateLastSync(token.getId());
                        } catch (DaoException e) {
                            LOGGER.log(Level.SEVERE, "Error updating sync for token: " + token.getId(), e);
                        }
                    });
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in OAuth sync process", e);
        } finally {
            executorService.shutdown();
            try {
                executorService.awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
            LOGGER.info("OAuthManager scheduler completed");
        }
    };

    private static void processToken(OAuthToken token) {
        try {
            JSONObject res = OAuthService.getAccessTokenWithRefreshToken(token.getRefreshToken());
            String accessToken = res.optString("access_token", null);
            if (accessToken != null) {
                OAuthService.syncContacts(accessToken, token.getUserId());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing token for user: " + token.getUserId(), e);
        }
    }
}