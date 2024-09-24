package com.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/dbtest")
public class DbTest extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DemoContacts"; 
    private static final String DB_USER = "root"; // Replace with your database username
    private static final String DB_PASS = null; // Replace with your database password

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Connection connection = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            // If the connection is successful
            out.println(connection.toString());
            out.println("<html><body>");
            out.println("<h2>Database Connection Test</h2>");
            out.println("<p>Database connection successful!</p>");
            out.println("</body></html>");
            String sql = "SELECT * FROM userdata";
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            
            out.print(rs.toString());
            // Generate HTML response
            out.println("<html><body>");
            out.println("<h2>User Data:</h2>");
            out.println("<table border='1'>");
            out.println("<tr><th>User ID</th><th>Email</th><th>Password</th><th>First Name</th><th>Last Name</th><th>Age</th><th>Address</th><th>Phone</th></tr>");

            // Process the ResultSet
            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int age = rs.getInt("age");
                String address = rs.getString("address");
                String phone = rs.getString("phone");

                out.println("<tr>");
                out.println("<td>" + userId + "</td>");
                out.println("<td>" + email + "</td>");
                out.println("<td>" + password + "</td>");
                out.println("<td>" + firstName + "</td>");
                out.println("<td>" + lastName + "</td>");
                out.println("<td>" + age + "</td>");
                out.println("<td>" + address + "</td>");
                out.println("<td>" + phone + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body></html>");


        } catch (ClassNotFoundException e) {
            out.println("<html><body>");
            out.println("<h2>Database Connection Test</h2>");
            out.println("<p>MySQL JDBC Driver not found. Include it in your library path.</p>");
            e.printStackTrace(out);
            out.println("</body></html>");
        } catch (SQLException e) {
            out.println("<html><body>");
            out.println("<h2>Database Connection Test</h2>");
            out.println("<p>Connection failed. Check output console for details.</p>");
            e.printStackTrace(out);
            out.println("</body></html>");
        } finally {
            // Clean up and close the connection
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(out);
            }
        }
    }
}
