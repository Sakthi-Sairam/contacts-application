package com.utils;

import java.sql.SQLException;

import com.Dao.SessionDao;
import com.managers.SessionManager;
import com.models.Session;
import com.models.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Utility class to handle user authentication and session management.
 */
public class AuthUtil {

    /**
     * Logs in a user by creating a new session and adding the session ID as a cookie.
     *
     * @param user The user object containing the user's details.
     * @param response The HttpServletResponse object used to send the session cookie.
     * @throws SQLException If any SQL errors occur during session creation.
     */
    public static void loginUser(User user, HttpServletResponse response) throws SQLException {
    	
        Session session = new Session(user.getUserId());

        // Store session in SessionManager
        SessionManager.addSession(session);

        SessionDao.createSession(session.getSessionId(), user.getUserId(), session.getLastAccessedTime(), session.getCreatedAt());

        // Add session ID as cookie "s-id"
        Cookie sessionCookie = new Cookie("s-id", session.getSessionId());
        sessionCookie.setMaxAge(30 * 60); // 30 minutes expiration
        
        response.addCookie(sessionCookie);
    }
}
