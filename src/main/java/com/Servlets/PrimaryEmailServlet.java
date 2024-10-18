package com.Servlets;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

import com.Dao.userDao;


@WebServlet("/primaryemail/*")
public class PrimaryEmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		System.out.println("path info of primary email" + pathInfo);
	    String[] parts = pathInfo.split("/");
	    int user_id = Integer.parseInt(parts[1]);
	    int emailId = Integer.parseInt(parts[3]);
	    int primaryEmailId = Integer.parseInt(parts[2]);
	    
		try {
			userDao.changePrimaryEmail(user_id, emailId, primaryEmailId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		response.sendRedirect(request.getContextPath() + "/viewcontacts");
	}

}
