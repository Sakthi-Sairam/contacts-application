package com.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import com.Dao.userDao;

@WebServlet("/addemail")
public class AddUserEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		boolean isAdded =false;
        if(!userDao.mailCheck(email)) {
        	try {
				isAdded = userDao.addEmailByUserId(user_id, email);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
        if (isAdded) {
			response.sendRedirect("dashboard.jsp");
        } else {
            request.setAttribute("result", "Email might already be in use.");
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        }
//        return isAdded;
	      
	}

}
