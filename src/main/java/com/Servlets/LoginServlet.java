package com.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DemoContacts";
    private static final String DB_USER = "root"; 
    private static final String DB_PASS = null; 
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	RequestDispatcher rd=req.getRequestDispatcher("login.jsp");
    	rd.forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Establish the connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            // Prepare SQL query to check user credentials
            String sql = "SELECT a.first_name FROM userdata a JOIN MailMapper b ON a.user_id = b.user_id WHERE b.email = ? AND a.password = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);

            // Execute the query
            rs = ps.executeQuery();

            if (rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("user", email); // Set a meaningful session attribute
                session.setAttribute("firstName", rs.getString("first_name"));

                out.println("<html><body>");
                out.println("<h2>Login Successful</h2>");
                out.println("<p>Welcome, " + rs.getString("first_name") + "!</p>");
                out.println("</body></html>");
            } else {
                // Login failed
                out.println("<html><body>");
                out.println("<h2>Login Failed</h2>");
                out.println("<p>Invalid email or password. Please try again.</p>");
                out.println("<a href='login.jsp'>Back to login</a>");
                out.println("</body></html>");
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
    }
}
