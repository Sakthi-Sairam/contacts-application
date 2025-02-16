package com.servlets;

import java.io.IOException;

import com.handlers.CategoryHandler;
import com.utils.ExceptionHandlerUtil;
import com.utils.PathParamUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/categories/*")
public class CategoryController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try {
			String categoryParam = PathParamUtil.getSingleParam(request.getPathInfo());

			if (categoryParam == null) {
				CategoryHandler.returnAllCategories(request, response);
			} else {
				int categoryId = Integer.parseInt(categoryParam);
				CategoryHandler.returnSpecificCategory(request, response, categoryId);
			}
		} catch (NumberFormatException e) {
			ExceptionHandlerUtil.logAndForwardClientException(request, response, e.getMessage(), e, "/error.jsp", getClass());
		} catch (Exception e) {
			ExceptionHandlerUtil.logAndForwardServerException(request, response,e, getClass());
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		String action = request.getParameter("action");
		if (action == null || action.isEmpty()) {
			ExceptionHandlerUtil.logAndForwardClientException(request, response, "Action parameter is missing.", null,
					"/error.jsp", getClass());
			return;
		}
		try {

			switch (action) {
			case "create":
				CategoryHandler.handleCreateCategory(request, response);
				break;
			case "delete":
				CategoryHandler.handleDeleteCategory(request, response);
				break;
			case "assignContact":
				CategoryHandler.handleAssignContactToCategory(request, response);
				break;
			default:
				ExceptionHandlerUtil.logAndForwardClientException(request, response, "invalid action", null, "/error.jsp", getClass());
			}
		} catch (NumberFormatException e) {
			ExceptionHandlerUtil.logAndForwardClientException(request, response, e.getMessage(), e, "/error.jsp", getClass());
		} catch (Exception e) {
			ExceptionHandlerUtil.logAndForwardServerException(request, response, e, getClass());
		}
	}
}
