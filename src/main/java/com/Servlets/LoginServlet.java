package com.Servlets;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.Dao.*;
import com.models.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	req.getRequestDispatcher("login.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user=null;
		try {
			user = userDao.loginUser(email, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (user != null) {
		    HttpSession session = request.getSession();
		    session.setAttribute("user",user);

//		    response.sendRedirect("dashboard.jsp");
		    response.sendRedirect("viewcontacts");
		} else {
		    request.setAttribute("errorMessage", "Invalid email or password. Please try again.");
		    request.getRequestDispatcher("login.jsp").forward(request, response);
		}
    }

}
