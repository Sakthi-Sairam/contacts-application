package com.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import com.Dao.userDao;
import com.managers.LoggerManager;
import com.managers.SessionManager;
import com.models.Session;
import com.models.User;

@WebFilter("*")
public class SessionFilter implements Filter {
    private static final ThreadLocal<User> threadLocalSession = new ThreadLocal<>();
    private static final int TIMEOUT_MINUTES = 5; 

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	
    	final Logger accessLogger = LoggerManager.getAccessLogger();
    	
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
//        System.out.println(SessionManager.sessionMap);
        String requestURI = httpRequest.getRequestURI();
        

        // Allow unauthenticated access to login and registration pages
        if (requestURI.endsWith("login.jsp") || requestURI.endsWith("register.jsp") || requestURI.endsWith("login") || requestURI.endsWith("register")) {
            String sessionId = getSessionIdFromCookies(httpRequest.getCookies());
            if (sessionId != null && SessionManager.getSession(sessionId) != null) {
                httpResponse.sendRedirect("dashboard.jsp");
                return;
            }
        	System.out.println("new request.............");
            chain.doFilter(request, response);
            return;
        }

        // Extract session ID from cookies
        String sessionId = getSessionIdFromCookies(httpRequest.getCookies());
        if (sessionId == null) {
            System.out.println("Session ID not found");
            httpResponse.sendRedirect("login.jsp");
            return;
        }
        // Log access details
        accessLogger.info("Accessed URI: " + requestURI + " | Session ID: " + sessionId + " | Time: " + System.currentTimeMillis());

        // Retrieve session from the SessionManager
        Session storedSession = SessionManager.getSession(sessionId);

        if (storedSession == null) { //session is not present in the map and db
            System.out.println("No session found for ID: " + sessionId);
            httpResponse.sendRedirect("login.jsp");
            return;
        }

        long lastAccessedTime = storedSession.getLastAccessedTime();
        long now = System.currentTimeMillis();


        if (lastAccessedTime + TIMEOUT_MINUTES * 60000 < now) {
            // Remove from session map if timed out
            SessionManager.removeSession(sessionId);
            httpResponse.sendRedirect("login.jsp");
            return;
        } else {
        	
        	int userId = storedSession.getUserId();
            SessionManager.updateSession(sessionId, storedSession.getUserId());
//            int userId = SessionManager.sessionMap.get(sessionId).getUserId();
            if(!SessionManager.userMap.containsKey(userId)) {
            	SessionManager.userMap.put(userId,userDao.getUserById(userId));
            }
        }


        threadLocalSession.set(SessionManager.getUser(storedSession.getUserId()));

        try {
        	request.setAttribute("sessionId",sessionId);
            chain.doFilter(request, response);
        }catch(Exception e) {
        	accessLogger.severe("Error processing request: " + e.getMessage());
        } finally {
            threadLocalSession.remove();
        }
    }

    public static String getSessionIdFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("s-id".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null; // Session ID not found
    }

    public void destroy() {
        threadLocalSession.remove();
    }

    public static User getCurrentUser() {
//    	System.out.println("in filter : "+threadLocalSession.get());
    	User currentUser = threadLocalSession.get();
//    	if(currentUser == null) {
//            sid = getSessionIdFromCookies();
//    		SessionManager.userMap.put(SessionManager.sessionMap.get(), currentUser);}
        return currentUser;
    }
}
