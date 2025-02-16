package com.handlers;

import java.io.IOException;

import com.dao.UserDao;
import com.exceptions.DaoException;
import com.utils.ExceptionHandlerUtil;
import com.utils.PathParamUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmailHandler {
	public static void handleChangePrimaryEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String[] pathParams = PathParamUtil.getMultipleParams(request.getPathInfo());
			int userId = Integer.parseInt(pathParams[0]);
			int primaryEmailId = Integer.parseInt(pathParams[1]);
			int emailId = Integer.parseInt(pathParams[2]);

			boolean isSuccess = UserDao.changePrimaryEmail(userId, emailId, primaryEmailId);
			if (isSuccess) {
				response.sendRedirect("/profile?action=refresh");
			} else {
				ExceptionHandlerUtil.logAndForwardClientException(request, response, "Failed to change primary email.", null, "/profile", EmailHandler.class);
			}
		} catch (NumberFormatException e) {
			ExceptionHandlerUtil.logAndForwardClientException(request, response, "Invalid email ID format.", e, "/error.jsp", EmailHandler.class);
		} catch (DaoException e) {
			ExceptionHandlerUtil.logAndForwardServerException(request, response, e, EmailHandler.class);
		} catch (ArrayIndexOutOfBoundsException e) {
			ExceptionHandlerUtil.logAndForwardClientException(request, response, "Missing parameters in path.", e, "/error.jsp", EmailHandler.class);
		}
	}

	public static void handleAddEmail(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			String email = request.getParameter("email");
			int userId = Integer.parseInt(PathParamUtil.getSingleParam(request.getPathInfo()));

			if (UserDao.mailCheck(email)) {
				ExceptionHandlerUtil.logAndForwardClientException(request, response, "Email already exists.", null,
						"/profile", EmailHandler.class);
				return;
			}

			boolean isAdded = UserDao.addEmailByUserId(userId, email);
			if (isAdded) {
				response.sendRedirect("/profile?action=refresh");
			} else {
				ExceptionHandlerUtil.logAndForwardClientException(request, response, "Failed to add email.", null, "/profile", EmailHandler.class);
			}
		} catch (NumberFormatException e) {
			ExceptionHandlerUtil.logAndForwardClientException(request, response, "Invalid user ID format.", e, "/error.jsp", EmailHandler.class);
		} catch (DaoException e) {
			ExceptionHandlerUtil.logAndForwardServerException(request, response, e, EmailHandler.class);
		}
	}

	public static void handleDeleteEmail(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	public static void handleEditEmail(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
}
