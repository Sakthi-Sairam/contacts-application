package com.servlets;

import java.io.IOException;
import java.sql.SQLException;

import com.dao.UserDao;
import com.exceptions.DaoException;
import com.models.User;
import com.utils.AuthUtil;
import com.utils.ExceptionHandlerUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet to handle user login requests.
 */
//@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) {
		try {
			req.getRequestDispatcher("login.jsp").forward(req, res);
		} catch (ServletException | IOException e) {
			ExceptionHandlerUtil.logAndForwardServerException(req, res, e, getClass());
		}
	}

	/**
	 * Handles POST requests for user login.
	 *
	 * @param req request object
	 * @param res response pbject
	 * @throws ServletException If a servlet error occurs.
	 * @throws IOException      If an I/O error occurs.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) {
		String email = req.getParameter("email");
		String password = req.getParameter("password");

		User user = null;
		try {
			user = UserDao.loginUser(email, password);
			if (user != null) {
				AuthUtil.loginUser(user, res);
				res.sendRedirect("/contacts");
			} else {
				req.setAttribute("errorMessage", "Invalid email or password.");
				req.getRequestDispatcher("login.jsp").forward(req, res);
			}

		} catch (DaoException e) {
			ExceptionHandlerUtil.logAndForwardServerException(req, res, e, getClass());
		} catch (Exception e) {
			ExceptionHandlerUtil.logAndForwardServerException(req, res, e, getClass());
		}
	}
}
