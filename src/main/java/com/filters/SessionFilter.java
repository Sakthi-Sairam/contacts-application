package com.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.models.User;

public class SessionFilter implements Filter {
    private static final ThreadLocal<HttpSession> threadLocalSession = new ThreadLocal<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String requestURI = httpRequest.getRequestURI();

        // Allow access to login and register pages
        if (requestURI.endsWith("login.jsp") || requestURI.endsWith("register.jsp") || requestURI.endsWith("login") || requestURI.endsWith("register")) {
            chain.doFilter(request, response); // Allow access
            return;
        }

        if (session == null || (User) session.getAttribute("user")==null) {
        	System.out.println("session not working");
            // If session is not present, redirect to login
            httpResponse.sendRedirect("login.jsp");
            return;
        } else {
            // If session is present, store it in ThreadLocal
            threadLocalSession.set(session);
        }

        try {
            // Proceed with the next filter or target resource
            chain.doFilter(request, response);
        } finally {
            // Remove the session from ThreadLocal after processing the request
            threadLocalSession.remove();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Any initialization code can go here
    }

    @Override
    public void destroy() {
        // Any cleanup code can go here
        threadLocalSession.remove();
    }

    public static Object getCurrentSession() {
        return threadLocalSession.get().getAttribute("user");
    }
}
