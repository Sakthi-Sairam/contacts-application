package com.utils;

import java.util.Arrays;

public class PathParamUtil {
	public static String getSingleParam(String pathInfo) {
		if(pathInfo != null && pathInfo.startsWith("/") && pathInfo.length()>1) {
			return pathInfo.replaceAll("^/|/$", "");
		}
		return null;
	}

	public static String[] getMultipleParams(String pathInfo) {
		String[] parts = pathInfo.split("/");
		return Arrays.copyOfRange(parts, 1, parts.length);
	}
}
