package com.Servlets;

import com.Dao.ContactDao;
import com.exceptions.DaoException;
import com.filters.SessionFilter;
import com.models.Contact;
import com.models.User;
import com.utils.ContactsUtil;
import com.utils.ErrorHandlerUtil;
import com.utils.PathParamUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/contacts/*")
public class ContactsController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String contactParam = PathParamUtil.getSingleParam(request.getPathInfo());
            if (contactParam == null) {
                returnAllContacts(request, response);
            } else {
                int contactId = Integer.parseInt(contactParam);
                returnSpecificContact(request, response, contactId);
            }
        } catch (NumberFormatException e) {
            ErrorHandlerUtil.handleClientError(response, "Invalid contact ID format.");
        } catch (Exception e) {
            ErrorHandlerUtil.handleServerError(response, "An error occurred while processing the request.", e);
        }
    }

    private void returnSpecificContact(HttpServletRequest request, HttpServletResponse response, int contactId) throws IOException, ServletException {
        try {
            User user = (User) SessionFilter.getCurrentUser();
            Contact contact = ContactDao.getContactByContactId(contactId, user.getUserId());
            if (contact != null) {
//                response.getWriter().println(contact);
            	request.setAttribute("contact", contact);
                request.getRequestDispatcher("/contactView.jsp").forward(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println("Contact not found.");
            }
        } catch (DaoException e) {
            ErrorHandlerUtil.handleServerError(response, "Failed to retrieve contact.", e);
        }
    }

    private void returnAllContacts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = (User) SessionFilter.getCurrentUser();
            List<Contact> contacts = ContactDao.getContactsByUserId(user.getUserId());

            List<Contact> favourites = new ArrayList<>();
            List<Contact> archives = new ArrayList<>();
            ContactsUtil.getContacts(contacts, favourites, archives);

            request.setAttribute("user", user);
            request.setAttribute("contacts", contacts);
            request.setAttribute("favourites", favourites);
            request.setAttribute("archives", archives);

            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
        } catch (DaoException e) {
            ErrorHandlerUtil.handleRequestError(request, response, "Could not retrieve data.", e, "/error.jsp");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "delete":
                    handleDeleteContact(request, response);
                    break;
                case "edit":
                    handleEditContact(request, response);
                    break;
                case "add":
                    handleAddContact(request, response);
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

    private void handleAddContact(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String friendEmail = request.getParameter("friend_email");
            String aliasName = request.getParameter("alias_fnd_name");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            int userId = Integer.parseInt(request.getParameter("user_id"));
            int isArchived = Integer.parseInt(request.getParameter("isArchived"));
            int isFavorite = Integer.parseInt(request.getParameter("isFavorite"));

            ContactDao.addContact(friendEmail, aliasName, phone, address, userId, isArchived, isFavorite);
            response.sendRedirect("/contacts");
        } catch (DaoException e) {
        	ErrorHandlerUtil.handleServerError(response, "Failed to add contact.", e);
        }
    }

    private void handleEditContact(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int contactId = Integer.parseInt(PathParamUtil.getSingleParam(request.getPathInfo()));
            String friendEmail = request.getParameter("friend_email");
            String aliasName = request.getParameter("alias_fnd_name");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            int isArchived = Integer.parseInt(request.getParameter("isArchived"));
            int isFavorite = Integer.parseInt(request.getParameter("isFavorite"));

            Contact contact = new Contact(contactId, aliasName, friendEmail, phone, address, isArchived, isFavorite);
            boolean isSuccess = ContactDao.updateContact(contact);

            if (isSuccess) {
                response.sendRedirect("/contacts");
            } else {
            	ErrorHandlerUtil.handleRequestError(request, response, "Failed to edit contact.", null, "/contacts");
            }
        } catch (DaoException e) {
        	ErrorHandlerUtil.handleServerError(response, "Failed to edit contact.", e);
        }
    }

    private void handleDeleteContact(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            int contactId = Integer.parseInt(PathParamUtil.getSingleParam(request.getPathInfo()));
            boolean success = ContactDao.deleteContact(contactId);

            if (success) {
                response.sendRedirect("/contacts");
            } else {
            	ErrorHandlerUtil.handleClientError(response, "Failed to delete contact.");
            }
        } catch (DaoException e) {
        	ErrorHandlerUtil.handleServerError(response, "Failed to delete contact.", e);
        }
    }

}
