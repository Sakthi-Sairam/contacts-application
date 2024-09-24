package com.Servlets;

import com.Dao.userDao;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

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
        if(!userDao.mailCheck(email)) {
        	registrationSuccessful = userDao.registerUser(password, firstName, lastName, age, address, phone, email);
        }


        if (registrationSuccessful) {
            response.sendRedirect("login");
        } else {
            request.setAttribute("result", "Registration failed. Email might already be in use.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
