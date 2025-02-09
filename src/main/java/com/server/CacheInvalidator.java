package com.server;

import com.exceptions.DaoException;
import com.models.ServerRegistry;
import com.utils.HttpClientUtil;

import java.util.List;

public class CacheInvalidator {
    private static final HttpClientUtil httpClientUtil = new HttpClientUtil();

    public static void broadcastCacheInvalidation(String sessionId) {
        try {
			List<ServerRegistry> servers = ServerRegistryDao.getAllServers();
			String payload = "sessionId=" + sessionId;

			for (ServerRegistry server : servers) {
			    String url = "http://" + server.getIpAddress() + ":" + server.getPortNumber() + "/invalidatecache";
			    System.out.println(url);
			    httpClientUtil.sendPostRequestAsync(url, payload);
			}
		} catch (DaoException e) {
			e.printStackTrace();
		}
    }
}
