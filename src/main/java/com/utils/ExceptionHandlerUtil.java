package com.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ExceptionHandlerUtil {

	public static void logAndForwardServerException(HttpServletRequest request, HttpServletResponse response,
			Exception e, Class<?> clazz) {
		LogRecord logRecord = new LogRecord(Level.SEVERE, e.getMessage());
		logRecord.setSourceClassName(clazz.getName());
		logRecord.setThrown(e);

		Logger logger = Logger.getLogger(clazz.getName());
		logger.log(logRecord);
		e.printStackTrace();
		forwardToView(request, response, "An unexpected error occurred", "/error.jsp");
	}

	public static void logAndForwardClientException(HttpServletRequest request, HttpServletResponse response,
			String message, Exception e, String view, Class<?> clazz) {
		LogRecord logRecord = new LogRecord(Level.WARNING, message);
		logRecord.setSourceClassName(clazz.getName());
		if (e != null) {
			logRecord.setThrown(e);
		}

		Logger logger = Logger.getLogger(clazz.getName());
		logger.log(logRecord);
		if (e != null) {
			e.printStackTrace();
		}
		forwardToView(request, response, message, view);
	}

	private static void forwardToView(HttpServletRequest request, HttpServletResponse response, String message, String view) {
		request.setAttribute("errorMessage", message);
		try {
			request.getRequestDispatcher(view).forward(request, response);
		} catch (Exception ex) {
			Logger localLogger = Logger.getLogger(ExceptionHandlerUtil.class.getName());
			localLogger.log(Level.SEVERE, "Failed to forward request to " + view, ex);
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
			} catch (IOException e) {
				localLogger.log(Level.SEVERE, "Failed to send error response", e);
			}
		}
	}
}