package com.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dao.UserDao;
import com.exceptions.DaoException;
import com.managers.LoggerManager;
import com.managers.SessionManager;
import com.models.Session;
import com.models.User;

@WebFilter(urlPatterns = {"/*"}, filterName = "SessionFilter")
public class SessionFilter implements Filter {
    private static final ThreadLocal<User> threadLocalSession = new ThreadLocal<>();
    private static final Logger ACCESS_LOGGER = LoggerManager.getAccessLogger();
    private static final Logger LOGGER = Logger.getLogger(SessionFilter.class.getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String queryString = httpRequest.getQueryString();
        String requestURL = httpRequest.getRequestURL().toString() + (queryString != null ? "?" + queryString : "");
        
        LOGGER.fine("Processing request: " + requestURI);

        // Path to exclude
        if (requestURI.endsWith("invalidatecache")) {
            LOGGER.info("Skipping session validation for: " + requestURI);
            chain.doFilter(request, response);
            return;
        }

        if (isLoginOrRegistrationPage(requestURI)) {
            handleLoginPage(httpRequest, httpResponse, chain);
            return;
        }

        // Extract and validate session
        String sessionId = getSessionIdFromCookies(httpRequest.getCookies());
        if (sessionId == null) {
            LOGGER.warning("No session ID found, redirecting to login");
            httpResponse.sendRedirect("/login");
            return;
        }

        // Log access details
//        logAccessDetails(requestURI, sessionId);
        logAccessDetails(requestURL, sessionId);


        // Validate session
        if (!validateAndProcessSession(httpRequest, httpResponse, sessionId)) {
            return;
        }

        try {
            request.setAttribute("sessionId", sessionId);
            chain.doFilter(request, response);
        } catch (Exception e) {
            ACCESS_LOGGER.log(Level.SEVERE, "Error processing request", e);
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Request processing error");
        } finally {
            threadLocalSession.remove();
        }
    }

    // Clear all session-related cookies
    private void clearSessionAndCookies(HttpServletResponse response) {
        Cookie sessionCookie = new Cookie("s-id", "");
        sessionCookie.setMaxAge(0);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);
    }

    private boolean isLoginOrRegistrationPage(String requestURI) {
        return requestURI.endsWith("login.jsp") || 
               requestURI.endsWith("register.jsp") || 
               requestURI.endsWith("login") || 
               requestURI.endsWith("register");
    }

    private void handleLoginPage(HttpServletRequest httpRequest, HttpServletResponse httpResponse, 
                                  FilterChain chain) 
            throws IOException, ServletException {
        String sessionId = getSessionIdFromCookies(httpRequest.getCookies());
        
        // Check if session exists and is valid
        if (sessionId != null) {
            Session session = SessionManager.getSession(sessionId);
            
            // Validate session time
            if (session != null) {
                long now = System.currentTimeMillis();
                if (session.getLastAccessedTime() + SessionManager.TIMEOUT_MINUTES * 60000L >= now) {
                    LOGGER.info("Active session found, redirecting to dashboard");
                    httpResponse.sendRedirect("/contacts");
                    return;
                }
            }
        }
        
        chain.doFilter(httpRequest, httpResponse);
    }

    private boolean validateAndProcessSession(HttpServletRequest httpRequest, HttpServletResponse httpResponse, 
                                              String sessionId) 
            throws IOException {
        // Retrieve session from the SessionManager
        Session storedSession = SessionManager.getSession(sessionId);

        if (storedSession == null) {
            LOGGER.warning("No session found for ID: " + sessionId);
            httpResponse.sendRedirect("/login");
            return false;
        }

        long lastAccessedTime = storedSession.getLastAccessedTime();
        long now = System.currentTimeMillis();
        long timeoutMillis = SessionManager.TIMEOUT_MINUTES * 60000L;

        // Check session timeout
        if (lastAccessedTime + timeoutMillis < now) {
            LOGGER.info("Session expired for ID: " + sessionId);
            
            // Remove session from manager and clear cookie
            SessionManager.removeSession(sessionId);
//            clearSessionAndCookies(httpResponse);
            
            httpResponse.sendRedirect("/login");
            return false;
        }

        // Update session and user cache
        int userId = storedSession.getUserId();
        SessionManager.updateSession(sessionId, userId);
        
        if (!SessionManager.userMap.containsKey(userId)) {
            User user = null;
			try {
				user = UserDao.getUserById(userId);
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            SessionManager.userMap.put(userId, user);
        }

        threadLocalSession.set(SessionManager.getUser(userId));
        return true;
    }

    private void logAccessDetails(String requestURI, String sessionId) {
        ACCESS_LOGGER.info(String.format(
            "Accessed URI: %s | Session ID: %s | Time: %d", 
            requestURI, sessionId, System.currentTimeMillis()
        ));
    }

    public static String getSessionIdFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("s-id".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void destroy() {
        threadLocalSession.remove();
    }

    public static User getCurrentUser() {
        return threadLocalSession.get();
    }
    public static void setCurrentUser(User user) {
    	System.out.println(user.getEmails());
    	threadLocalSession.remove();
    	threadLocalSession.set(user);
    }
}