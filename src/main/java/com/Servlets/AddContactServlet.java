package com.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import com.Dao.ContactDao;

@WebServlet("/addcontact")
public class AddContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String friend_email = request.getParameter("friend_email");
		String alias_fnd_name = request.getParameter("alias_fnd_name");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		int user_id = Integer.parseInt(request.getParameter("user_id"));
	    int isArchived = Integer.parseInt(request.getParameter("isArchived"));
	    int isFavorite = Integer.parseInt(request.getParameter("isFavorite"));
	    
	    try {
			ContactDao.addContact(friend_email, alias_fnd_name, phone, address,user_id, isArchived, isFavorite);
			response.sendRedirect("dashboard.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    
	        
	    
	}

}
