package com.server;

import com.exceptions.DaoException;
import com.models.ServerRegistry;
import com.utils.HttpClientUtil;

import java.util.List;

public class CacheInvalidator {
	private static final HttpClientUtil httpClientUtil = new HttpClientUtil();

	public static void broadcastUserCacheInvalidation(String sessionId) {
		try {
			if (ServerCacheManager.serverCache == null || ServerCacheManager.serverCache.size() == 0) {
				List<ServerRegistry> servers = ServerRegistryDao.getAllServers();
				ServerCacheManager.populateCache(servers);
			}
			String payload = "sessionId=" + sessionId;

			for (ServerRegistry server : ServerCacheManager.serverCache.getValues()) {
				String url = "http://" + server.getIpAddress() + ":" + server.getPortNumber()
						+ "/server-cache-sync?action=userSync";
				System.out.println(url);
				httpClientUtil.sendPostRequestAsync(url, payload);
			}
		} catch (DaoException e) {
			e.printStackTrace();
		}
	}

	public static void broadcastServerCacheInvalidation() {
		try {
			if (ServerCacheManager.serverCache == null || ServerCacheManager.serverCache.size() == 0) {
				List<ServerRegistry> servers = ServerRegistryDao.getAllServers();
				ServerCacheManager.populateCache(servers);
			}

			for (ServerRegistry server : ServerCacheManager.serverCache.getValues()) {
				String url = "http://" + server.getIpAddress() + ":" + server.getPortNumber()
						+ "/server-cache-sync?action=serverSync";
				System.out.println(url);
				httpClientUtil.sendPostRequestAsync(url, null);
			}
		} catch (DaoException e) {
			e.printStackTrace();
		}
	}
}
