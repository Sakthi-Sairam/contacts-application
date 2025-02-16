package com.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

import com.filters.AuthFilter;
import com.handlers.UserProfileHandler;
import com.models.User;
import com.oauth.OAuthDao;
import com.models.OAuthToken;
import com.utils.ExceptionHandlerUtil;

@WebServlet("/profile/*")
public class UserProfileController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            String action = request.getParameter("action");
            if (action == null) {
                User user = (User) AuthFilter.getCurrentUser();
                List<OAuthToken> oAuthTokens = OAuthDao.getOAuthTokensByUserId(user.getUserId());
                request.setAttribute("oAuthTokens", oAuthTokens);
                request.setAttribute("user", user);
                request.getRequestDispatcher("/profile.jsp").forward(request, response);
                return;
            }

            switch (action) {
                case "refresh":
                    UserProfileHandler.handleRefreshUser(request, response);
                    break;
                default:
                    ExceptionHandlerUtil.logAndForwardClientException(request, response, "Invalid action.", null, "/error.jsp", UserProfileController.class);
            }
        } catch (Exception e) {
            ExceptionHandlerUtil.logAndForwardServerException(request, response, e, UserProfileController.class);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        try {
            String action = request.getParameter("action");
            if (action == null || action.isEmpty()) {
                ExceptionHandlerUtil.logAndForwardClientException(request, response, "Action parameter is missing.", null, "/error.jsp", UserProfileController.class);
                return;
            }

            switch (action) {
                case "updateSyncInterval":
                    UserProfileHandler.handleUpdateSyncInterval(request, response);
                    break;
                default:
                    ExceptionHandlerUtil.logAndForwardClientException(request, response, "Invalid action.", null, "/error.jsp", UserProfileController.class);
            }
        } catch (Exception e) {
            ExceptionHandlerUtil.logAndForwardServerException(request, response, e, UserProfileController.class);
        }
    }
}