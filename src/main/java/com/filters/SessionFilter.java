package com.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.models.User;

public class SessionFilter implements Filter {
    private static final ThreadLocal<User> threadLocalSession = new ThreadLocal<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String requestURI = httpRequest.getRequestURI();

        if (requestURI.endsWith("login.jsp") || requestURI.endsWith("register.jsp") || requestURI.endsWith("login") || requestURI.endsWith("register")) {
            chain.doFilter(request, response);
            return;
        }

        if (session == null || (User) session.getAttribute("user")==null) {
        	System.out.println("session not working");

            httpResponse.sendRedirect("login.jsp");
            return;
        } else {

            threadLocalSession.set((User)session.getAttribute("user"));
        }

        try {     
        	chain.doFilter(request, response);
        } finally {
        	threadLocalSession.remove();
        }
    }

    
    public void destroy() {
        threadLocalSession.remove();
    }

    public static Object getCurrentSession() {
        return threadLocalSession.get();
    }
}
