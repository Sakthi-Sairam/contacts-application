package com.servlets;

import com.oauth.OAuthService;
import com.utils.ExceptionHandlerUtil;
import com.models.User;
import com.filters.AuthFilter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/oauth2callback")
public class OAuth2CallbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			String code = request.getParameter("code");
			if (code == null) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				ExceptionHandlerUtil.logAndForwardClientException(request, response, "code is not present", null, "/error.jsp", getClass());
				return;
			}

			User currentUser = (User) AuthFilter.getCurrentUser();
			OAuthService.processOAuthCallback(code, currentUser);
			response.sendRedirect("/contacts");

		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
