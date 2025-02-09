package com.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.dao.UserDao;
import com.exceptions.DaoException;
import com.filters.AuthFilter;
import com.managers.SessionManager;
import com.models.User;
import com.oauth.OAuthDao;
import com.models.OAuthToken;
import com.utils.ErrorHandlerUtil;

/**
 * Servlet implementation class ProfileController
 */
@WebServlet("/profile/*")
public class UserProfileController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        try {
			String action = request.getParameter("action");
			if(action == null) {
				User user = (User) AuthFilter.getCurrentUser();
				List<OAuthToken> oAuthTokens = OAuthDao.getOAuthTokensByUserId(user.getUserId());
				request.setAttribute("oAuthTokens", oAuthTokens);
				request.setAttribute("user", user);
				request.getRequestDispatcher("/profile.jsp").forward(request, response);
				return;
			}
			
			switch (action) {
			    case "refresh":
			        handleRefreshUser(request, response);
			        break;
			    default:
			    	ErrorHandlerUtil.handleClientError(response, "Invalid action.");
			}
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleRefreshUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
			User user = (User) AuthFilter.getCurrentUser();
	        User updatedUser = UserDao.getUserById(user.getUserId());
	        SessionManager.setUser(updatedUser);
	        AuthFilter.setCurrentUser(updatedUser);
	        
	        response.sendRedirect("/profile");
        }catch (DaoException e) {
        	e.printStackTrace();
        }
	}
	
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "updateSyncInterval":
                    handleUpdateSyncInterval(request, response);
                    break;
                default:
                	ErrorHandlerUtil.handleClientError(response, "Invalid action.");
            }
        } catch (Exception e) {
        	ErrorHandlerUtil.handleClientError(response, "Invalid input format.");
        }
    }

	private void handleUpdateSyncInterval(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String tokenId = request.getParameter("tokenId");
	    String syncInterval = request.getParameter("syncInterval");
	    
	    try {
	        int tokenIdInt = Integer.parseInt(tokenId);
	        long syncIntervalLong = Long.parseLong(syncInterval);
	        
	        OAuthDao.updateSyncInterval(tokenIdInt, syncIntervalLong);
	        response.sendRedirect("/profile");
	        
	    } catch (NumberFormatException | DaoException e) {
	        ErrorHandlerUtil.handleServerError(response, "Failed to update sync interval",e);
	    }
	}

}
