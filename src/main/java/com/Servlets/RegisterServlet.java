package com.Servlets;

import com.Dao.userDao;
import com.exceptions.DaoException;
import com.models.User;
import com.utils.AuthUtil;
import com.utils.ErrorHandlerUtil;

import java.io.IOException;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet to handle user registration requests.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Handles GET requests and forwards to the registration page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			if (!userDao.mailCheck(email)) {
				
			    registrationSuccessful = userDao.registerUser(hashedPassword, firstName, lastName, age, address, phone, email);
			}

			if (registrationSuccessful) {

				User user = null;

			    	user = userDao.loginUser(email, password);
			        if (user != null) {

			        	AuthUtil.loginUser(user, response);
			            response.sendRedirect("viewcontacts");
			        }

			} else {
			    request.setAttribute("result", "Registration failed. Email might already be in use.");
			    request.getRequestDispatcher("register.jsp").forward(request, response);
			}
		} catch (DaoException e) {
        	ErrorHandlerUtil.handleServerError(response, "Failed to register the user.", e);
			e.printStackTrace();
		}
    }
}
