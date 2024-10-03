package com.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import com.Dao.ContactDao;
import com.models.Contact;

@WebServlet("/editContact")
public class EditContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int contactId = Integer.parseInt(request.getParameter("contactId"));
		String friend_email = request.getParameter("friend_email");
		String alias_fnd_name = request.getParameter("alias_fnd_name");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
	    int isArchived = Integer.parseInt(request.getParameter("isArchived"));
	    int isFavorite = Integer.parseInt(request.getParameter("isFavorite"));
	    
	    Contact contact = new Contact(contactId, alias_fnd_name, friend_email, phone, address, isArchived, isFavorite);
	    try {
			ContactDao.updateContact(contact);
			response.sendRedirect("viewcontacts");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
