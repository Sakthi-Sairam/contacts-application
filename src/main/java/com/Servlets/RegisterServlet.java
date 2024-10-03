package com.Servlets;

import com.Dao.userDao;
import com.models.User;

import java.io.IOException;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
        
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        
        if(!userDao.mailCheck(email)) {
        	registrationSuccessful = userDao.registerUser(hashedPassword, firstName, lastName, age, address, phone, email);
        }


        if (registrationSuccessful) {
        	
            User user=null;
    		try {
    			user = userDao.loginUser(email, password);
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}

    		if (user != null) {
    		    HttpSession session = request.getSession();
    		    session.setAttribute("user",user);

//    		    response.sendRedirect("dashboard.jsp");
    		    response.sendRedirect("viewcontacts");
    		}
        	
        } else {
            request.setAttribute("result", "Registration failed. Email might already be in use.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
