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
import com.utils.ErrorHandlerUtil;
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
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().println("Contact not found.");
			}
		} catch (DaoException e) {
			ErrorHandlerUtil.handleServerError(response, "Failed to retrieve contact.", e);
		}
	}

	public static void returnAllContacts(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			User user = (User) AuthFilter.getCurrentUser();
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

	public static void handleAddContact(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
				ErrorHandlerUtil.handleRequestError(request, response, "Failed to edit contact.", null, "/contacts");
			}
		} catch (DaoException e) {
			ErrorHandlerUtil.handleServerError(response, "Failed to edit contact.", e);
		}
	}

	public static void handleDeleteContact(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
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
