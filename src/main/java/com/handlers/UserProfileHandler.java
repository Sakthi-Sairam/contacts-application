package com.handlers;

import java.io.IOException;

import com.dao.UserDao;
import com.exceptions.DaoException;
import com.filters.AuthFilter;
import com.managers.SessionManager;
import com.models.User;
import com.oauth.OAuthDao;
import com.utils.ExceptionHandlerUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserProfileHandler {
    public static void handleRefreshUser(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        try {
            User user = (User) AuthFilter.getCurrentUser();
            User updatedUser = UserDao.getUserById(user.getUserId());
            SessionManager.setUser(updatedUser);
            AuthFilter.setCurrentUser(updatedUser);

            response.sendRedirect("/profile");
        } catch (DaoException e) {
            ExceptionHandlerUtil.logAndForwardServerException(request, response, e, UserProfileHandler.class);
        }
    }

    public static void handleUpdateSyncInterval(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        try {
            String tokenId = request.getParameter("tokenId");
            String syncInterval = request.getParameter("syncInterval");

            int tokenIdInt = Integer.parseInt(tokenId);
            long syncIntervalLong = Long.parseLong(syncInterval);

            OAuthDao.updateSyncInterval(tokenIdInt, syncIntervalLong);
            response.sendRedirect("/profile");
        } catch (DaoException e) {
            ExceptionHandlerUtil.logAndForwardServerException(request, response, e, UserProfileHandler.class);
        }
    }
}