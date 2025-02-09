package com.servlets;

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
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/group/*")
public class ContactsGroupController extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String groupType = PathParamUtil.getSingleParam(req.getPathInfo());
			User user = (User) AuthFilter.getCurrentUser();
			List<Contact> contacts = ContactDao.getContactsByUserId(user.getUserId());

			List<Contact> favourites = new ArrayList<>();
			List<Contact> archives = new ArrayList<>();
			ContactsUtil.getContacts(contacts, favourites, archives);

			switch (groupType) {
				case "favourites":
					req.setAttribute("groupedContacts", favourites);
					req.setAttribute("title", "Favourites");
					break;
				case "archived":
					req.setAttribute("groupedContacts", archives);
					req.setAttribute("title", "Archived");
					break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + groupType);
			}
			req.getRequestDispatcher("/groupedContacts.jsp").forward(req,resp);
		} catch (DaoException | ServletException | IOException e) {
            ErrorHandlerUtil.handleRequestError(req, resp, "Could not retrieve data.", e, "/error.jsp");
		}

	}
}
