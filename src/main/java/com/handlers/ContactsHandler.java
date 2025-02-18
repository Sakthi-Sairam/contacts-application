package com.handlers;

import java.io.IOException;
import java.util.List;

import com.dao.AuthorizationDao;
import com.dao.ContactDao;
import com.exceptions.DaoException;
import com.filters.AuthFilter;
import com.models.Contact;
import com.models.ContactEmail;
import com.models.User;
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
            int userId = user.getUserId();
            Contact contact = ContactDao.getContactByContactId(contactId, userId);
            if(contact == null) {
            	ExceptionHandlerUtil.logAndForwardClientException(request, response, "Contact not found.", null, "/error.jsp", ContactsHandler.class);
            	return;
            }

            List<ContactEmail> contactEmails = ContactDao.getContactEmailsByContactId(contactId, userId);
            contact.setEmails(contactEmails);

            request.setAttribute("contact", contact);
            request.getRequestDispatcher("/contactView.jsp").forward(request, response);
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
            System.out.println(contacts);
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
            int isArchived = Integer.parseInt(request.getParameter("isArchived"));
            int isFavorite = Integer.parseInt(request.getParameter("isFavorite"));
            int userId = AuthFilter.getCurrentUser().getUserId();

            boolean isSuccess = ContactDao.addContact(friendEmail, aliasName, phone, address, userId, null, isArchived, isFavorite);
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
            String aliasName = request.getParameter("alias_fnd_name");
            String address = request.getParameter("address");
            int isArchived = Integer.parseInt(request.getParameter("isArchived"));
            int isFavorite = Integer.parseInt(request.getParameter("isFavorite"));
            int userId = AuthFilter.getCurrentUser().getUserId();

			boolean isAuthorized = AuthorizationDao.isUserAuthorizedForCrudOnContacts(userId, contactId);
			if(!isAuthorized) {
			    ExceptionHandlerUtil.logAndForwardClientException(request, response, "You cannot do that", null, "/error.jsp", ContactsHandler.class);
			    return;
			}

            boolean isSuccess = ContactDao.updateContact(contactId, aliasName, address, isArchived, isFavorite);

            if (isSuccess) {
                response.sendRedirect("/contacts/"+contactId);
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
            int userId = AuthFilter.getCurrentUser().getUserId();

			boolean isAuthorized = AuthorizationDao.isUserAuthorizedForCrudOnContacts(userId, contactId);
			if(!isAuthorized) {
			    ExceptionHandlerUtil.logAndForwardClientException(request, response, "You cannot do that", null, "/error.jsp", ContactsHandler.class);
			    return;
			}
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

	public static void handleAddContactPhone(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int contactId = Integer.parseInt(PathParamUtil.getSingleParam(request.getPathInfo()));
            String label = request.getParameter("label");
            String phoneNumber = request.getParameter("phoneNumber");
            int userId = AuthFilter.getCurrentUser().getUserId();

			boolean isAuthorized = AuthorizationDao.isUserAuthorizedForCrudOnContacts(userId, contactId);
			if(!isAuthorized) {
			    ExceptionHandlerUtil.logAndForwardClientException(request, response, "You cannot do that", null, "/error.jsp", ContactsHandler.class);
			    return;
			}

            boolean isSuccess = ContactDao.addContactPhoneNumber(contactId, label, phoneNumber);
            if (isSuccess) {
                response.sendRedirect("/contacts/"+contactId);
            } else {
                ExceptionHandlerUtil.logAndForwardClientException(request, response, "Failed to add contact.", null, "/contacts", ContactsHandler.class);
            }
        }catch (DaoException e) {
            ExceptionHandlerUtil.logAndForwardServerException(request, response, e, ContactsHandler.class);
        }
	}
	
	public static void handleAddContactEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int contactId = Integer.parseInt(PathParamUtil.getSingleParam(request.getPathInfo()));
            String label = request.getParameter("label");
            String email = request.getParameter("email");
            int userId = AuthFilter.getCurrentUser().getUserId();

			boolean isAuthorized = AuthorizationDao.isUserAuthorizedForCrudOnContacts(userId, contactId);
			if(!isAuthorized) {
			    ExceptionHandlerUtil.logAndForwardClientException(request, response, "You cannot do that", null, "/error.jsp", ContactsHandler.class);
			    return;
			}

            boolean isSuccess = ContactDao.addContactEmail(contactId, label, email);
            if (isSuccess) {
                response.sendRedirect("/contacts/"+contactId);
            } else {
                ExceptionHandlerUtil.logAndForwardClientException(request, response, "Failed to add contact.", null, "/contacts", ContactsHandler.class);
            }
        }catch (DaoException e) {
            ExceptionHandlerUtil.logAndForwardServerException(request, response, e, ContactsHandler.class);
        }
	}

	public static void handleDeleteContactEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
			String[] pathParams = PathParamUtil.getMultipleParams(request.getPathInfo());
			int contactId = Integer.parseInt(pathParams[0]);
			int contactEmailId = Integer.parseInt(pathParams[1]);
			int userId = AuthFilter.getCurrentUser().getUserId();

			boolean isAuthorized = AuthorizationDao.isUserAuthorizedToDeleteContactEmail(userId, contactEmailId);
			if(!isAuthorized) {
			    ExceptionHandlerUtil.logAndForwardClientException(request, response, "You cannot do that", null, "/error.jsp", ContactsHandler.class);
			    return;
			}
			
			boolean isSuccess = ContactDao.deleteContactEmail(contactEmailId);
			if (isSuccess) {
			    response.sendRedirect("/contacts/"+contactId);
			} else {
			    ExceptionHandlerUtil.logAndForwardClientException(request, response, "Failed to delete contact email", null, "/contacts", ContactsHandler.class);
			}
		} catch (NumberFormatException e) {
            ExceptionHandlerUtil.logAndForwardClientException(request, response, "Invalid contact email ID format.", e, "/error.jsp", ContactsHandler.class);
		} catch (DaoException e) {
            ExceptionHandlerUtil.logAndForwardServerException(request, response, e, ContactsHandler.class);
		}
		
	}

	public static void handleDeleteContactPhone(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String[] pathParams = PathParamUtil.getMultipleParams(request.getPathInfo());
			int contactId = Integer.parseInt(pathParams[0]);
			int contactPhoneNumberId = Integer.parseInt(pathParams[1]);
			int userId = AuthFilter.getCurrentUser().getUserId();
			
			boolean isAuthorized = AuthorizationDao.isUserAuthorizedToDeleteContactNumber(userId, contactPhoneNumberId);
			if(!isAuthorized) {
			    ExceptionHandlerUtil.logAndForwardClientException(request, response, "You cannot do that", null, "/error.jsp", ContactsHandler.class);
			    return;
			}
			
			boolean isSuccess = ContactDao.deleteContactPhone(contactPhoneNumberId);
			if (isSuccess) {
			    response.sendRedirect("/contacts/"+contactId);
			} else {
			    ExceptionHandlerUtil.logAndForwardClientException(request, response, "Failed to delete contact phone", null, "/contacts", ContactsHandler.class);
			}
		} catch (NumberFormatException e) {
            ExceptionHandlerUtil.logAndForwardClientException(request, response, "Invalid contact phone ID format.", e, "/error.jsp", ContactsHandler.class);
		} catch (DaoException e) {
            ExceptionHandlerUtil.logAndForwardServerException(request, response, e, ContactsHandler.class);
		}
		
	}
}