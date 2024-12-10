package com.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import com.Dao.CategoriesDao;

/**
 * Servlet implementation class AssignContactToCategoryServlet
 */
@WebServlet("/assignContactToCategory")
public class AssignContactToCategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        int contactId = Integer.parseInt(request.getParameter("contactId"));
        
        try {
			if (!CategoriesDao.checkContactPresentInCategory(categoryId, contactId) && CategoriesDao.addContactToCategory(categoryId, contactId)) {
			    response.sendRedirect("categories.jsp");
			} else {
			    response.getWriter().write("Error assigning contact to category");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}