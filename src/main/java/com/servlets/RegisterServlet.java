package com.servlets;

import com.dao.UserDao;
import com.exceptions.DaoException;
import com.models.User;
import com.utils.AuthUtil;
import com.utils.ExceptionHandlerUtil;

import java.io.IOException;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
			request.getRequestDispatcher("register.jsp").forward(request, response);
		} catch (ServletException | IOException e) {
			ExceptionHandlerUtil.logAndForwardServerException(request, response, e, getClass());
		}
    }

    /**
     * Handles POST requests for user registration.
     * 
     * @param request  object
     * @param response object
     * @throws ServletException If a servlet error occurs.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String age = request.getParameter("age");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        boolean registrationSuccessful = false;

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try {
			if (!UserDao.mailCheck(email)) {
				
			    registrationSuccessful = UserDao.registerUser(hashedPassword, firstName, lastName, age, address, phone, email);
			}

			if (registrationSuccessful) {

				User user = null;

			    	user = UserDao.loginUser(email, password);
			        if (user != null) {

			        	AuthUtil.loginUser(user, response);
			            response.sendRedirect("viewcontacts");
			        }

			} else {
			    request.setAttribute("result", "Registration failed. Email might already be in use.");
			    request.getRequestDispatcher("register.jsp").forward(request, response);
			}
		} catch (DaoException e) {
        	ExceptionHandlerUtil.logAndForwardServerException(request, response, e, getClass());
		} catch (Exception e) {
        	ExceptionHandlerUtil.logAndForwardServerException(request, response, e, getClass());
		}
    }
}
