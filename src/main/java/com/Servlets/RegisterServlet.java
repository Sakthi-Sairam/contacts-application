package com.Servlets;

import com.utils.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        int userId = 0;

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean registrationSuccessful = false;

        try {
            // Establish the connection
            connection = DBConnection.getConnection();
            // Check if the email already exists
            String checkEmailSQL = "SELECT email FROM MailMapper WHERE email = ?";
            ps = connection.prepareStatement(checkEmailSQL);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                registrationSuccessful = false; 
            } else {
                // Insert new user
                String insertSQL = "INSERT INTO userdata (password, first_name, last_name, age, address, phone) VALUES (?, ?, ?, ?, ?, ?)";
                ps = connection.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, password);
                ps.setString(2, firstName);
                ps.setString(3, lastName);
                ps.setInt(4, (age != null && !age.isEmpty()) ? Integer.parseInt(age) : 0);
                ps.setString(5, address);
                ps.setString(6, phone);

                int rowsAffected = ps.executeUpdate();
                ResultSet generatedKeys = ps.getGeneratedKeys();
                userId = 0;
                if (generatedKeys.next()) {
                    userId = generatedKeys.getInt(1);
                }

                // Insert email into MailMapper
                String mailInsertSQL = "INSERT INTO MailMapper (user_id, email, isPrimary) VALUES (?, ?, true)";
                ps = connection.prepareStatement(mailInsertSQL);
                ps.setInt(1, userId);
                ps.setString(2, email);
                rowsAffected = ps.executeUpdate();

                registrationSuccessful = (rowsAffected > 0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Clean up and close the connection
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (registrationSuccessful) {
            HttpSession session = request.getSession();
            session.setAttribute("userId", userId);
            session.setAttribute("email", email);
            session.setAttribute("firstName", firstName);
            response.sendRedirect("welcome.jsp");
        } else {
            request.setAttribute("result", "Registration failed. Email might already be in use.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
