package com.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.handlers.EmailHandler;
import com.utils.ExceptionHandlerUtil;

@WebServlet("/email/*")
public class EmailController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		String action = request.getParameter("action");
		if (action == null || action.isEmpty()) {
			ExceptionHandlerUtil.logAndForwardClientException(request, response, "Action parameter is missing.", null,"/profile", getClass());
			return;
		}

		try {
			switch (action) {
			case "delete":
				EmailHandler.handleDeleteEmail(request, response);
				break;
			case "changePrimary":
				EmailHandler.handleChangePrimaryEmail(request, response);
				break;
			case "edit":
				EmailHandler.handleEditEmail(request, response);
				break;
			case "add":
				EmailHandler.handleAddEmail(request, response);
				break;
			default:
				ExceptionHandlerUtil.logAndForwardClientException(request, response, "Invalid action.", null, "/error.jsp", getClass());
			}
		} catch (Exception e) {
			ExceptionHandlerUtil.logAndForwardServerException(request, response, e, getClass());
		}
	}
}