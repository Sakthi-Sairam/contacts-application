package com.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import com.dao.UserDao;
import com.exceptions.DaoException;
import com.utils.ErrorHandlerUtil;
import com.utils.PathParamUtil;

@WebServlet("/email/*")
public class EmailController extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "delete":
                    handleDeleteEmail(request, response);
                    break;
                case "changePrimary":
                    handlechangePrimaryEmail(request, response);
                    break;
                case "edit":
                    handleEditEmail(request, response);
                    break;
                case "add":
                    handleAddEmail(request, response);
                    break;
                default:
                	ErrorHandlerUtil.handleClientError(response, "Invalid action.");
            }
        } catch (NumberFormatException e) {
        	ErrorHandlerUtil.handleClientError(response, "Invalid input format.");
        } catch (Exception e) {
        	ErrorHandlerUtil.handleServerError(response, "An error occurred while processing the request.", e);
        }
	}


	private void handlechangePrimaryEmail(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			String[] pathParams = PathParamUtil.getMultipleParams(request.getPathInfo());
			int userId = Integer.parseInt(pathParams[0]);
			int primaryEmailId = Integer.parseInt(pathParams[1]);
			int emailId = Integer.parseInt(pathParams[2]);
			
			boolean isSuccess = UserDao.changePrimaryEmail(userId, emailId, primaryEmailId);
			if (isSuccess) {
			    response.sendRedirect("/profile?action=refresh");
			} else {
				ErrorHandlerUtil.handleRequestError(request, response, "Failed to change primary email.", null, "/profile");
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (DaoException e) {
        	ErrorHandlerUtil.handleServerError(response, "Failed to change primary email.", e);
        	e.printStackTrace();
		}
	}


	private void handleAddEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String email = request.getParameter("email");
			int userId = Integer.parseInt(PathParamUtil.getSingleParam(request.getPathInfo()));
			boolean isAdded = false;
			if(!UserDao.mailCheck(email)) {
				isAdded = UserDao.addEmailByUserId(userId, email);
			}
			if(isAdded) {
				response.sendRedirect("/profile?action=refresh");
			}
		} catch (NumberFormatException e) {
            ErrorHandlerUtil.handleClientError(response, "Invalid email ID format.");
		} catch (DaoException e) {
            ErrorHandlerUtil.handleServerError(response, "An error occurred while processing the request.", e);
		}
	}


	private void handleEditEmail(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleDeleteEmail(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

}
