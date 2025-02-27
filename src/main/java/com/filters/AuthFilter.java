package com.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.dao.UserDao;
import com.exceptions.DaoException;
import com.managers.SessionManager;
import com.models.Session;
import com.models.User;

public class AuthFilter implements Filter {
    private static final ThreadLocal<User> THREAD_LOCAL_USER = new ThreadLocal<>();
    private static final Logger LOGGER = Logger.getLogger(AuthFilter.class.getName());
    private static final Logger ACCESS_LOGGER = Logger.getLogger("AccessLog");
    private static final String SESSION_COOKIE_NAME = "s-id";
    private static final String LOGIN_PATH = "/login";
    private static final long MILLIS_PER_MINUTE = 60000L;

    static {
        try {
            // Configure Access Log
            FileHandler accessFileHandler = new FileHandler(
                    "/home/sakthi-pt7694/Desktop/jeeProjects1/contactsLogs/access.log", true);
            accessFileHandler.setFormatter(new SimpleFormatter());
            ACCESS_LOGGER.addHandler(accessFileHandler);
            ACCESS_LOGGER.setUseParentHandlers(false);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to configure access logger", e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        LOGGER.fine("Processing request: " + requestURI);

        try {
            // Check for excluded paths
            if (isExcludedPath(requestURI)) {
                chain.doFilter(request, response);
                return;
            }

            // Handle login/registration pages
            if (isLoginOrRegistrationPage(requestURI)) {
                handleLoginPage(httpRequest, httpResponse, chain);
                return;
            }

            // Process session
            String sessionId = getSessionIdFromCookies(httpRequest.getCookies());
            if (sessionId == null) {
                LOGGER.warning("No session ID found, redirecting to login");
                httpResponse.sendRedirect(LOGIN_PATH);
                return;
            }

            // Log access
            logAccessDetails(httpRequest, sessionId);

            // Validate session
            if (!validateAndProcessSession(httpResponse, sessionId)) {
                return;
            }

            // Continue with request processing
            request.setAttribute("sessionId", sessionId);
            chain.doFilter(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing request", e);
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Request processing error");
        } finally {
            THREAD_LOCAL_USER.remove();
        }
    }

    private boolean isExcludedPath(String requestURI) {
        return requestURI.endsWith("invalidatecache") || requestURI.endsWith("server-cache-sync") || requestURI.endsWith("error.jsp");
    }

    private boolean isLoginOrRegistrationPage(String requestURI) {
        return requestURI.endsWith("login.jsp") || requestURI.endsWith("register.jsp") 
               || requestURI.endsWith("login") || requestURI.endsWith("register");
    }

    private void handleLoginPage(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain chain)
            throws IOException, ServletException {
        String sessionId = getSessionIdFromCookies(httpRequest.getCookies());

        // Check if user has a valid session
        if (sessionId != null && isSessionValid(sessionId)) {
            LOGGER.info("Active session found, redirecting to dashboard");
            httpResponse.sendRedirect("/contacts");
            return;
        }

        chain.doFilter(httpRequest, httpResponse);
    }

    private boolean isSessionValid(String sessionId) {
        Session session = SessionManager.getSession(sessionId);
        if (session == null) {
            return false;
        }
        
        long now = System.currentTimeMillis();
        return session.getLastAccessedTime() + SessionManager.TIMEOUT_MINUTES * MILLIS_PER_MINUTE >= now;
    }

    private boolean validateAndProcessSession(HttpServletResponse httpResponse, String sessionId) throws IOException {
        Session storedSession = SessionManager.getSession(sessionId);

        if (storedSession == null) {
            LOGGER.warning("No session found for ID: " + sessionId);
            redirectToLogin(httpResponse);
            return false;
        }

        long lastAccessedTime = storedSession.getLastAccessedTime();
        long now = System.currentTimeMillis();
        long timeoutMillis = SessionManager.TIMEOUT_MINUTES * MILLIS_PER_MINUTE;

        // Check session timeout
        if (lastAccessedTime + timeoutMillis < now) {
            LOGGER.info("Session expired for ID: " + sessionId);
            SessionManager.removeSession(sessionId);
            redirectToLogin(httpResponse);
            return false;
        }

        // Update session and ensure user is cached
        int userId = storedSession.getUserId();
        SessionManager.updateSession(sessionId, userId);
        ensureUserCached(userId);
        
        THREAD_LOCAL_USER.set(SessionManager.getUser(userId));
        return true;
    }

    private void ensureUserCached(int userId) {
        if (!SessionManager.userMap.containsKey(userId)) {
            try {
                User user = UserDao.getUserById(userId);
                if (user != null) {
                    SessionManager.userMap.put(userId, user);
                }
            } catch (DaoException e) {
                LOGGER.log(Level.SEVERE, "Error retrieving user data", e);
            }
        }
    }

    private void redirectToLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect(LOGIN_PATH);
    }

//    private void clearSessionCookie(HttpServletResponse response) {
//        Cookie sessionCookie = new Cookie(SESSION_COOKIE_NAME, "");
//        sessionCookie.setMaxAge(0);
//        sessionCookie.setPath("/");
//        response.addCookie(sessionCookie);
//    }

    private void logAccessDetails(HttpServletRequest request, String sessionId) {
        StringBuilder requestURI = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();
        if (queryString != null) {
            requestURI.append("?").append(queryString);
        }
        
        String clientIP = request.getRemoteAddr();
        String method = request.getMethod();

        ACCESS_LOGGER.info(String.format("Method: %s | URI: %s | IP: %s | Session ID: %s | Time: %d", 
                method, requestURI.toString(), clientIP, sessionId, System.currentTimeMillis()));
    }

    public static String getSessionIdFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        
        for (Cookie cookie : cookies) {
            if (SESSION_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    public void destroy() {
        THREAD_LOCAL_USER.remove();
    }

    public static User getCurrentUser() {
        return THREAD_LOCAL_USER.get();
    }

    public static void setCurrentUser(User user) {
        THREAD_LOCAL_USER.remove();
        THREAD_LOCAL_USER.set(user);
    }
}