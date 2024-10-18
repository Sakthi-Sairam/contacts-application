package com.Servlets;

import com.Dao.userDao;
import com.models.User;
import com.utils.AuthUtil;

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

        // Hash the password using BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Check if email is already registered
        if (!userDao.mailCheck(email)) {
            // Register the user
            registrationSuccessful = userDao.registerUser(hashedPassword, firstName, lastName, age, address, phone, email);
        }

        if (registrationSuccessful) {
            // Auto-login the user after successful registration
            User user = null;
            try {
                // Authenticate the user
                user = userDao.loginUser(email, password);
                if (user != null) {
                    // Log in the user using the AuthUtil class
                    AuthUtil.loginUser(user, response);
                    response.sendRedirect("viewcontacts");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Handle registration failure
            request.setAttribute("result", "Registration failed. Email might already be in use.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
