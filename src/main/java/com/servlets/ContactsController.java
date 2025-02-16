package com.servlets;

import com.handlers.ContactsHandler;
import com.utils.ExceptionHandlerUtil;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            String contactParam = PathParamUtil.getSingleParam(request.getPathInfo());
            if (contactParam == null) {
                ContactsHandler.returnAllContacts(request, response);
            } else {
                int contactId = Integer.parseInt(contactParam);
                ContactsHandler.returnSpecificContact(request, response, contactId);
            }
        } catch (NumberFormatException e) {
            ExceptionHandlerUtil.logAndForwardClientException(request, response, "Invalid contact ID format.", e, "/error.jsp", ContactsController.class);
        } catch (Exception e) {
            ExceptionHandlerUtil.logAndForwardServerException(request, response, e, ContactsController.class);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
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
                    ExceptionHandlerUtil.logAndForwardClientException(request, response, "Invalid action.", null, "/error.jsp", ContactsController.class);
            }
        } catch (NumberFormatException e) {
            ExceptionHandlerUtil.logAndForwardClientException(request, response, "Invalid input format.", e, "/error.jsp", ContactsController.class);
        } catch (Exception e) {
            ExceptionHandlerUtil.logAndForwardServerException(request, response, e, ContactsController.class);
        }
    }
}
