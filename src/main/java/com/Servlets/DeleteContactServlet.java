package com.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import com.Dao.ContactDao;

@WebServlet("/deleteContact")
public class DeleteContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int MyContactId = Integer.parseInt(request.getParameter("contactId"));
		boolean success = false;
		try {
			success = ContactDao.deleteContact(MyContactId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        if (success) {
            response.sendRedirect("viewcontacts");
        } else {
            request.setAttribute("errorMessage", "Failed to delete contact.");
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        }
	}

}
