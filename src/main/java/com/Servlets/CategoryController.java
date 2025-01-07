package com.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.Dao.CategoriesDao;
import com.Dao.ContactDao;
import com.exceptions.DaoException;
import com.filters.SessionFilter;
import com.models.CategoryDetails;
import com.models.Contact;
import com.models.User;
import com.utils.ErrorHandlerUtil;
import com.utils.PathParamUtil;

/**
 * Servlet implementation class CategoryController
 */
@WebServlet("/categories/*")
public class CategoryController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String categoryParam = PathParamUtil.getSingleParam(request.getPathInfo());
            if (categoryParam == null) {
                returnAllCategories(request, response);
            } else {
                int categoryId = Integer.parseInt(categoryParam);
                returnSpecificCategory(request, response, categoryId);
            }
        } catch (NumberFormatException e) {
            ErrorHandlerUtil.handleClientError(response, "Invalid category ID format.");
        } catch (Exception e) {
            ErrorHandlerUtil.handleServerError(response, "An error occurred while processing the request.", e);
        }
	}

	private void returnSpecificCategory(HttpServletRequest request, HttpServletResponse response,
			int categoryId) throws IOException {
        try {
			User user = (User) SessionFilter.getCurrentUser();
			CategoryDetails category = CategoriesDao.getCategoriesByCategoryId(categoryId, user.getUserId());
			if (category != null) {
			    response.getWriter().println(category);
			} else {
			    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			    response.getWriter().println("category not found.");
			}
		} catch (DaoException e) {
            ErrorHandlerUtil.handleServerError(response, "Failed to retrieve category.", e);
			e.printStackTrace();
		}
	}

	private void returnAllCategories(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
			User user = (User)SessionFilter.getCurrentUser();
			List<CategoryDetails> categories = CategoriesDao.getCategoriesByUserId(user.getUserId());
			List<Contact> contacts = ContactDao.getContactsByUserId(user.getUserId());
			
			request.setAttribute("user", user);
			request.setAttribute("contacts", contacts);
			request.setAttribute("categories", categories);
			
			request.getRequestDispatcher("./categories.jsp").forward(request, response);
		} catch (DaoException e) {
            ErrorHandlerUtil.handleRequestError(request, response, "Could not retrieve data.", e, "/error.jsp");
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch(action) {
        	case "create":
        		handleCreateCategory(request, response);
        		break;
        	case "delete":
        		handleDeleteCategory(request,response);
        		break;
        	case "assignContact":
        		handleAssignContactToCategory(request,response);
        		break;
        	default:
            	ErrorHandlerUtil.handleClientError(response, "Invalid action.");
        }

	}

	private void handleCreateCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
			String categoryName = request.getParameter("categoryName");
			int userId = SessionFilter.getCurrentUser().getUserId();
			boolean isSuccess = CategoriesDao.createCategory(userId, categoryName);
			
			if(isSuccess) {
				response.sendRedirect("/categories");
			} else {
				ErrorHandlerUtil.handleRequestError(request, response, "Failed to create category", null, "/categories");
			}
		} catch (DaoException e) {
        	ErrorHandlerUtil.handleServerError(response, "Failed to create category.", e);
			e.printStackTrace();
		}

	}

	private void handleDeleteCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
			int categoryId = Integer.parseInt(PathParamUtil.getSingleParam(request.getPathInfo()));
			boolean isSuccess = CategoriesDao.deleteCategory(categoryId);
			if (isSuccess) {
			    response.sendRedirect("/categories");
			} else {
				ErrorHandlerUtil.handleClientError(response, "Failed to delete category.");
			}
		} catch (DaoException e) {
        	ErrorHandlerUtil.handleServerError(response, "Failed to delete category.", e);
			e.printStackTrace();
		}

	}

	private void handleAssignContactToCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
	     try {
			int categoryId = Integer.parseInt(request.getParameter("categoryId"));
			   int contactId = Integer.parseInt(request.getParameter("contactId"));

			   if(!CategoriesDao.checkContactPresentInCategory(categoryId, contactId) && CategoriesDao.addContactToCategory(categoryId, contactId)) {
				    response.sendRedirect("/categories");
			   } else {
					ErrorHandlerUtil.handleRequestError(request, response, "Failed to Assign category to the contact", null, "/categories");
			   }
		} catch (DaoException e) {
        	ErrorHandlerUtil.handleServerError(response, "Failed to Assign category to the category.", e);
			e.printStackTrace();
		}
	}

}
