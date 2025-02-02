package com.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.dao.UserDao;
import com.exceptions.DaoException;
import com.filters.SessionFilter;
import com.managers.SessionManager;
import com.models.User;
import com.utils.ErrorHandlerUtil;

/**
 * Servlet implementation class ProfileController
 */
@WebServlet("/profile/*")
public class UserProfileController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        String action = request.getParameter("action");
        if(action == null) {
			User user = (User) SessionFilter.getCurrentUser();
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
	}

	private void handleRefreshUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
			User user = (User) SessionFilter.getCurrentUser();
	        User updatedUser = UserDao.getUserById(user.getUserId());
	        SessionManager.setUser(updatedUser);
	        SessionFilter.setCurrentUser(updatedUser);
	        
	        response.sendRedirect("/profile");
        }catch (DaoException e) {
        	e.printStackTrace();
        }
	}

}
