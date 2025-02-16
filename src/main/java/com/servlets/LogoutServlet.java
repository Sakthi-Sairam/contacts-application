package com.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.dao.SessionDao;
import com.exceptions.DaoException;
import com.managers.SessionManager;
import com.server.CacheInvalidator;
import com.utils.ExceptionHandlerUtil;

//@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		String sessionId = (String) request.getAttribute("sessionId");
		System.out.print(sessionId);
		SessionManager.removeSession(sessionId);
		try {
			SessionDao.deleteSessionById(sessionId);
			CacheInvalidator.broadcastUserCacheInvalidation(sessionId);
			response.sendRedirect("login");
		} catch (DaoException e) {
			ExceptionHandlerUtil.logAndForwardServerException(request, response, e, getClass());
		} catch (Exception e) {
			ExceptionHandlerUtil.logAndForwardServerException(request, response, e, getClass());
		}
	}

}
