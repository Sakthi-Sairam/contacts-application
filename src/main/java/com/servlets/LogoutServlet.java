package com.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

import com.dao.SessionDao;
import com.exceptions.DaoException;
import com.filters.AuthFilter;
import com.managers.SessionManager;
import com.server.CacheInvalidator;
import com.utils.ErrorHandlerUtil;

//@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionId = (String)request.getAttribute("sessionId");
		System.out.print(sessionId);
		SessionManager.removeSession(sessionId);
		try {
			SessionDao.deleteSessionById(sessionId);
			CacheInvalidator.broadcastCacheInvalidation(sessionId);
		} catch (DaoException e) {
        	ErrorHandlerUtil.handleServerError(response, "Failed to logout", e);
		}
		response.sendRedirect("login");
	}


}
