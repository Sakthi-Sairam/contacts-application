package com.server;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

import com.dao.SessionDao;
import com.exceptions.DaoException;
import com.managers.SessionManager;
import com.models.Session;
/**
 * Servlet implementation class InvalidateCacheServlet
 */
@WebServlet("/invalidatecache")
public class InvalidateCacheServlet extends HttpServlet {
	@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			resp.getWriter().write(SessionManager.sessionMap.toString());

		}

    private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("\n \n coming to the invalidatecache endpoint \n \n");
        String sessionIdParam = request.getParameter("sessionId");

        if (sessionIdParam == null || sessionIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing or invalid sessionId parameter.");
            return;
        }

        String sessionId = (String)sessionIdParam;



        Session removedSession = SessionManager.sessionMap.remove(sessionId);
//        SessionManager.removeSession(sessionId);

        if (removedSession != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("User cache invalidated successfully.");
            try {
				SessionDao.deleteSessionById(sessionId);
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.print(SessionManager.userMap.get(removedSession.getUserId()));
            SessionManager.userMap.remove(removedSession.getUserId());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("User not found in cache.");
        }
    }

}
