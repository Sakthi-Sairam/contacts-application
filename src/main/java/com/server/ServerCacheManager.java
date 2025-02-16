package com.server;

import java.util.List;

import com.cache.CacheManager;
import com.models.ServerRegistry;

public class ServerCacheManager {
	public static final CacheManager<Integer,ServerRegistry> serverCache = new CacheManager<>(10);

	public static void populateCache(List<ServerRegistry> servers) {
		for(ServerRegistry server:servers) {
			serverCache.put(server.getId(), server);
		}
	}
}
