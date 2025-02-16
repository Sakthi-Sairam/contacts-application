package com.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.dao.ContactDao;
import com.exceptions.DaoException;
import com.filters.AuthFilter;
import com.models.Contact;
import com.models.User;
import com.utils.ContactsUtil;
import com.utils.ExceptionHandlerUtil;
import com.utils.PathParamUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ContactsHandler {
    public static void returnSpecificContact(HttpServletRequest request, HttpServletResponse response, int contactId)
            throws IOException, ServletException {
        try {
            User user = (User) AuthFilter.getCurrentUser();
            Contact contact = ContactDao.getContactByContactId(contactId, user.getUserId());
            if (contact != null) {
                request.setAttribute("contact", contact);
                request.getRequestDispatcher("/contactView.jsp").forward(request, response);
            } else {
                ExceptionHandlerUtil.logAndForwardClientException(request, response, "Contact not found.", null, "/error.jsp", ContactsHandler.class);
            }
        } catch (DaoException e) {
            ExceptionHandlerUtil.logAndForwardServerException(request, response, e, ContactsHandler.class);
        }
    }

    public static void returnAllContacts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = (User) AuthFilter.getCurrentUser();
            String pageNumberString = request.getParameter("page");
            int pageNumber = 0;
            if(pageNumberString != null) pageNumber = Integer.parseInt(pageNumberString);
            if(pageNumber < 0) {
            	ExceptionHandlerUtil.logAndForwardClientException(request, response, "pageNumber cannot be negative", null, "/error.jsp", ContactsHandler.class);
            	return;
            }
            int limit = 11;
            int offset = pageNumber*10;
            List<Contact> contacts = ContactDao.getContactsByUserId(user.getUserId(),limit, offset);
            boolean isLastPage = isLastPage(contacts, limit);
            List<Contact> contactsToDisplay = contacts;
            if(!isLastPage) {
            	contactsToDisplay = contacts.subList(0, limit-1);
            }
            request.setAttribute("user", user);
            request.setAttribute("contacts", contactsToDisplay);
            request.setAttribute("isLastPage", isLastPage);
            request.setAttribute("pageNumber", pageNumber);

            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
        } catch (DaoException e) {
            ExceptionHandlerUtil.logAndForwardServerException(request, response, e, ContactsHandler.class);
        }
    }

    private static boolean isLastPage(List<Contact> contacts, int limit) {
		if(contacts.size() != limit) {
			return true;
		}
		return false;
	}

	public static void handleAddContact(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String friendEmail = request.getParameter("friend_email");
            String aliasName = request.getParameter("alias_fnd_name");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            int userId = Integer.parseInt(request.getParameter("user_id"));
            int isArchived = Integer.parseInt(request.getParameter("isArchived"));
            int isFavorite = Integer.parseInt(request.getParameter("isFavorite"));

            boolean isSuccess = ContactDao.addContact(friendEmail, aliasName, phone, address, userId, isArchived, isFavorite);
            if (isSuccess) {
                response.sendRedirect("/contacts");
            } else {
                ExceptionHandlerUtil.logAndForwardClientException(request, response, "Failed to add contact.", null, "/contacts", ContactsHandler.class);
            }
        }catch (DaoException e) {
            ExceptionHandlerUtil.logAndForwardServerException(request, response, e, ContactsHandler.class);
        }
    }

    public static void handleEditContact(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
                ExceptionHandlerUtil.logAndForwardClientException(request, response, "Failed to edit contact.", null, "/contacts", ContactsHandler.class);
            }
        }catch (DaoException e) {
            ExceptionHandlerUtil.logAndForwardServerException(request, response, e, ContactsHandler.class);
        }
    }

    public static void handleDeleteContact(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int contactId = Integer.parseInt(PathParamUtil.getSingleParam(request.getPathInfo()));
            boolean isSuccess = ContactDao.deleteContact(contactId);

            if (isSuccess) {
                response.sendRedirect("/contacts");
            } else {
                ExceptionHandlerUtil.logAndForwardClientException(request, response, "Failed to delete contact.", null, "/contacts", ContactsHandler.class);
            }
        } catch (NumberFormatException e) {
            ExceptionHandlerUtil.logAndForwardClientException(request, response, "Invalid contact ID format.", e, "/error.jsp", ContactsHandler.class);
        } catch (DaoException e) {
            ExceptionHandlerUtil.logAndForwardServerException(request, response, e, ContactsHandler.class);
        }
    }
}