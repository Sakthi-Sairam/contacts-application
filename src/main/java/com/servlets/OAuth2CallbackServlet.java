package com.servlets;
import com.oauth.OAuthService;
import com.models.User;
import com.filters.AuthFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/oauth2callback")
public class OAuth2CallbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing 'code' parameter");
            return;
        }

        try {
            User currentUser = (User) AuthFilter.getCurrentUser();
            OAuthService.processOAuthCallback(code, currentUser);
            response.sendRedirect("/contacts");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
        }
    }
}
