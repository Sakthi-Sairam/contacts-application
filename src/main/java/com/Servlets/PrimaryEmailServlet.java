package com.Servlets;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

import com.Dao.userDao;


@WebServlet("/primaryemail")
public class PrimaryEmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int user_id = Integer.parseInt(request.getParameter("user_id"));
        
		int emailId = Integer.parseInt(request.getParameter("emailId"));
		
		int primaryEmailId = Integer.parseInt(request.getParameter("primaryEmailId"));
		try {
			userDao.changePrimaryEmail(user_id, emailId, primaryEmailId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.sendRedirect("viewcontacts");
	}

}
