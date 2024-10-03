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
 * Servlet implementation class DeleteCategoryServlet
 */

@WebServlet("/deleteCategory")
public class DeleteCategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int categoryId = Integer.parseInt(request.getParameter("categoryId"));
		try {
			CategoriesDao.deleteCategory(categoryId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		response.sendRedirect("categories.jsp");
	}

}
