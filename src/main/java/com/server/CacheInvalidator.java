package com.server;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class CacheInvalidator {

    public static void broadcastCacheInvalidation(int sessionId) {
//        List<String> servers = ServerRegistry.getActiveServers();
    	List<String> servers = new ArrayList<>();

        for (String server : servers) {
            try {
                URL url = new URI("http://" + server + "/invalidateUserCache").toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                String payload = "sessionId=" + sessionId;
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.getOutputStream().write(payload.getBytes());

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    System.out.println("Cache invalidation successful on server: " + server);
                } else {
                    System.err.println("Failed to notify server: " + server + ". Response code: " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
