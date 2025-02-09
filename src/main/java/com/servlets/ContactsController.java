package com.servlets;

import com.handlers.ContactsHandler;
import com.utils.ErrorHandlerUtil;
import com.utils.PathParamUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/contacts/*")
public class ContactsController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String contactParam = PathParamUtil.getSingleParam(request.getPathInfo());
            if (contactParam == null) {
                ContactsHandler.returnAllContacts(request, response);
            } else {
                int contactId = Integer.parseInt(contactParam);
                ContactsHandler.returnSpecificContact(request, response, contactId);
            }
        } catch (NumberFormatException e) {
            ErrorHandlerUtil.handleClientError(response, "Invalid contact ID format.");
        } catch (Exception e) {
            ErrorHandlerUtil.handleServerError(response, "An error occurred while processing the request.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "delete":
                	ContactsHandler.handleDeleteContact(request, response);
                    break;
                case "edit":
                	ContactsHandler.handleEditContact(request, response);
                    break;
                case "add":
                	ContactsHandler.handleAddContact(request, response);
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







}
