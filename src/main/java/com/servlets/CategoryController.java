package com.servlets;

import java.io.IOException;

import com.handlers.CategoryHandler;
import com.utils.ErrorHandlerUtil;
import com.utils.PathParamUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CategoryController
 */
@WebServlet("/categories/*")
public class CategoryController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String categoryParam = PathParamUtil.getSingleParam(request.getPathInfo());
			if (categoryParam == null) {
				CategoryHandler.returnAllCategories(request, response);
			} else {
				int categoryId = Integer.parseInt(categoryParam);
				CategoryHandler.returnSpecificCategory(request, response, categoryId);
			}
		} catch (NumberFormatException e) {
			ErrorHandlerUtil.handleClientError(response, "Invalid category ID format.");
		} catch (Exception e) {
			ErrorHandlerUtil.handleServerError(response, "An error occurred while processing the request.", e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
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
			ErrorHandlerUtil.handleClientError(response, "Invalid action.");
		}

	}

}
