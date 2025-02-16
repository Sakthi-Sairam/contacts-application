package com.server;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.dao.SessionDao;
import com.exceptions.DaoException;
import com.managers.SessionManager;
import com.models.Session;
import com.utils.ExceptionHandlerUtil;

@WebServlet("/server-cache-sync")
public class ServerCacheSyncController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().write(SessionManager.sessionMap.toString());
		response.getWriter().write(ServerCacheManager.serverCache.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action").toLowerCase();
		switch (action) {
		case "usersync":
			handleUserSync(request, response);
			break;
		case "serversync":
			handleServerSync(request,response);
		default:
			ExceptionHandlerUtil.logAndForwardClientException(request, response, "invalid action query param", null, "/error.jsp", getClass());
		}
	}

	private void handleServerSync(HttpServletRequest request, HttpServletResponse response) {
		ServerCacheManager.serverCache.clear();
		
	}

	private void handleUserSync(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("\n \n coming to the invalidatecache endpoint \n \n");
        String sessionIdParam = request.getParameter("sessionId");

        if (sessionIdParam == null || sessionIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing or invalid sessionId parameter.");
            return;
        }

        String sessionId = (String)sessionIdParam;
        
        Session removedSession = SessionManager.sessionMap.remove(sessionId);

        if (removedSession != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("User cache invalidated successfully.");
            try {
				SessionDao.deleteSessionById(sessionId);
			} catch (DaoException e) {
				ExceptionHandlerUtil.logAndForwardServerException(request, response, e, getClass());
			}
            System.out.print(SessionManager.userMap.get(removedSession.getUserId()));
            SessionManager.userMap.remove(removedSession.getUserId());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("User not found in cache.");
        }
    }

}
