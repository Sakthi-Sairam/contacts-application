package com.Servlets;

import java.io.IOException;
import java.sql.SQLException;

import com.Dao.userDao;
import com.models.User;
import com.utils.AuthUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet to handle user login requests.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Handles GET requests and forwards to the login page.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher("login.jsp").forward(req, res);
    }

    /**
     * Handles POST requests for user login.
     *
     * @param req  request object
     * @param res  response pbject
     * @throws ServletException If a servlet error occurs.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = null;
        try {
            // Authenticate user using the provided email and password
            user = userDao.loginUser(email, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (user != null) {
            try {
                // Log in the user using the AuthUtil class
                AuthUtil.loginUser(user, res);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            res.sendRedirect("dashboard.jsp");
        } else {
            req.setAttribute("errorMessage", "Invalid email or password.");
            req.getRequestDispatcher("login.jsp").forward(req, res);
        }
    }
}
