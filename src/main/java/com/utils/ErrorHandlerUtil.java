package com.utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorHandlerUtil {

    public static void handleClientError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println(message);
    }

    public static void handleServerError(HttpServletResponse response, String message, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().println(message);
        if (e != null) {
        	Throwable cause = e.getCause();
        	while(cause!=null) {
        		System.out.println(cause +" : "+ cause.getMessage());
        		cause = cause.getCause();
        	}
        	e.printStackTrace();
        }
    }

    public static void handleRequestError(HttpServletRequest request, HttpServletResponse response, String message, Exception e, String view) throws IOException{
        if (e != null) e.printStackTrace();
        response.sendRedirect(view+"?errorMessage="+message);
    }
}
