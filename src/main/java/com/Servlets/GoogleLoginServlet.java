package com.Servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.oauth.OAuthConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/googleLogin")
public class GoogleLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {        
        // Store state in session for verification
//        request.getSession().setAttribute("oauth_state", state);
        
        String authUrl = String.format("%s?client_id=%s&redirect_uri=%s&response_type=code" +
                "&scope=%s&access_type=offline", //&prompt=consent
                OAuthConfig.AUTH_ENDPOINT,
                OAuthConfig.CLIENT_ID,
                URLEncoder.encode(OAuthConfig.REDIRECT_URI, StandardCharsets.UTF_8.toString()),
                URLEncoder.encode(OAuthConfig.SCOPE, StandardCharsets.UTF_8.toString()));
//        System.out.println(authUrl);
        
        response.sendRedirect(authUrl);
    }
}
