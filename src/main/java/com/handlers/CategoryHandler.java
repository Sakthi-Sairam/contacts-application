package com.handlers;

import java.io.IOException;
import java.util.List;

import com.dao.AuthorizationDao;
import com.dao.CategoriesDao;
import com.dao.ContactDao;
import com.exceptions.DaoException;
import com.filters.AuthFilter;
import com.models.CategoryDetails;
import com.models.Contact;
import com.models.User;
import com.utils.ExceptionHandlerUtil;
import com.utils.PathParamUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CategoryHandler {
	public static void returnSpecificCategory(HttpServletRequest request, HttpServletResponse response, int categoryId) throws ServletException, IOException{
		try {
			int userId = AuthFilter.getCurrentUser().getUserId();
			CategoryDetails category = CategoriesDao.getCategoriesByCategoryId(categoryId, userId);
			List<Contact> contacts = ContactDao.getContactsByUserId(userId);
			List<Contact> categoryContacts = CategoriesDao.getContactsByCategoryId(category.getCategoryId());

			request.setAttribute("contacts", contacts);
			request.setAttribute("categoryContacts",categoryContacts);
			request.setAttribute("category", category);
			request.getRequestDispatcher("/categoriesView.jsp").forward(request, response);
		} catch (DaoException e) {
			ExceptionHandlerUtil.logAndForwardServerException(request, response, e, CategoryHandler.class);
		}
	}

	public static void returnAllCategories(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try {
			User user = (User) AuthFilter.getCurrentUser();
			int userId = user.getUserId();
			List<CategoryDetails> categories = CategoriesDao.getCategoriesByUserId(userId);
			List<Contact> contacts = ContactDao.getContactsByUserId(userId);

			request.setAttribute("user", user);
			request.setAttribute("contacts", contacts);
			request.setAttribute("categories", categories);

			request.getRequestDispatcher("./categories.jsp").forward(request, response);
		} catch (DaoException e) {
			ExceptionHandlerUtil.logAndForwardServerException(request, response, e, CategoryHandler.class);
		}
	}

	public static void handleCreateCategory(HttpServletRequest request, HttpServletResponse response) throws IOException{
		try {
			String categoryName = request.getParameter("categoryName");
			int userId = AuthFilter.getCurrentUser().getUserId();
			boolean isSuccess = CategoriesDao.createCategory(userId, categoryName);

			if (isSuccess) {
				response.sendRedirect("/categories");
			} else {
				ExceptionHandlerUtil.logAndForwardClientException(request, response, "Failed to create category", null,
						"/categories", CategoryHandler.class);
			}
		} catch (DaoException e) {
			ExceptionHandlerUtil.logAndForwardServerException(request, response, e, CategoryHandler.class);
		}
	}

	public static void handleDeleteCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			int categoryId = Integer.parseInt(PathParamUtil.getSingleParam(request.getPathInfo()));
			int userId = AuthFilter.getCurrentUser().getUserId();

			boolean isAuthorized = AuthorizationDao.isUserAuthorizedForCategory(userId, categoryId);
			if(!isAuthorized) {
			    ExceptionHandlerUtil.logAndForwardClientException(request, response, "You cannot do that", null, "/error.jsp", ContactsHandler.class);
			    return;
			}
			
			boolean isSuccess = CategoriesDao.deleteCategory(categoryId);
			if (isSuccess) {
				response.sendRedirect("/categories");
			} else {
				ExceptionHandlerUtil.logAndForwardClientException(request, response, "Failed to delete category.", null,
						"/categories", CategoryHandler.class);
			}
		} catch (DaoException e) {
			ExceptionHandlerUtil.logAndForwardServerException(request, response, e, CategoryHandler.class);
		}
	}

	public static void handleAssignContactToCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			int categoryId = Integer.parseInt(request.getParameter("categoryId"));
			int contactId = Integer.parseInt(request.getParameter("contactId"));
			int userId = AuthFilter.getCurrentUser().getUserId();

			boolean isAuthorized = AuthorizationDao.isUserAuthorizedForCategory(userId, categoryId);
			if(!isAuthorized) {
			    ExceptionHandlerUtil.logAndForwardClientException(request, response, "You cannot do that", null, "/error.jsp", ContactsHandler.class);
			    return;
			}
			
			if (!CategoriesDao.checkContactPresentInCategory(categoryId, contactId)
					&& CategoriesDao.addContactToCategory(categoryId, contactId)) {
				response.sendRedirect("/categories/" + categoryId);
			} else {
				ExceptionHandlerUtil.logAndForwardClientException(request, response, "cannot add the contact to group", null, "/categories/" + categoryId, CategoryHandler.class);
			}
		} catch (DaoException e) {
			ExceptionHandlerUtil.logAndForwardServerException(request, response, e, CategoryHandler.class);
		}
	}
}
