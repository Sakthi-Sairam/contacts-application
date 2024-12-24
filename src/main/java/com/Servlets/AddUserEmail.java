package com.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import com.Dao.userDao;

@WebServlet("/addemail/*")
public class AddUserEmail extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Handles post request of submission of the addemail form
     * @param request
     * @param response
     * @throws SevletException
     * @throws IOException
     *
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String pathInfo = request.getPathInfo();
        
        String[] parts = pathInfo.split("/");
        System.out.println("two parts are " + parts[0]+" : "+parts[1]);
        int user_id = Integer.parseInt(parts[1]);
        
        boolean isAdded = false;
        if (!userDao.mailCheck(email)) {
            try {
                isAdded = userDao.addEmailByUserId(user_id, email);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        if (isAdded) {
            response.sendRedirect(request.getContextPath() + "/viewcontacts");
        } else {
//            request.setAttribute("result", "Email might already be in use.");
//            request.getRequestDispatcher("/profile.jsp").forward(request, response);
            
            response.sendRedirect(request.getContextPath() + "/profile.jsp");
        }
    }
}
