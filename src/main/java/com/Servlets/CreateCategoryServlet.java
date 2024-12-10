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
 * Servlet implementation class CreateCategoryServlet
 */
@WebServlet("/createCategory")
public class CreateCategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String categoryName = request.getParameter("categoryName");
            int userId = Integer.parseInt(request.getParameter("userId"));
//            System.out.println(request.getParameter("categoryName")+" : "+request.getParameter("userId"));

            try {
				boolean success = CategoriesDao.createCategory(userId, categoryName);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            response.sendRedirect("categories.jsp");

	}

}
