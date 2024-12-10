package com.Dao;

import com.QueryLayer.QueryBuilderOld;
import com.models.Session;
import com.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SessionDao {

    public static void batchUpdateSessions(List<Session> sessionsToUpdate) throws SQLException {
        String updateSQL = "UPDATE sessions SET lastAccessedTime = ? WHERE sessionId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(updateSQL)) {

            for (Session session : sessionsToUpdate) {
                ps.setObject(1, session.getLastAccessedTime());
                ps.setString(2, session.getSessionId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
    public static void deleteExpiredSessions(long expirationTime) throws SQLException {
        new QueryBuilderOld()
            .delete("sessions")
            .where("lastAccessedTime", "<",  expirationTime, true)
            .execute();
    }

    public static void deleteSessionById(String id) throws SQLException {
        new QueryBuilderOld()
            .delete("sessions")
            .where("sessionId", "=", id, true)
            .execute();
    }

    public static void createSession(String sessionId, int userId, long lastAccessedTime, long createdAt) throws SQLException {
        new QueryBuilderOld()
            .insert("sessions")
            .columns("sessionId", "user_id", "lastAccessedTime", "createdAt")
            .values(sessionId, userId, lastAccessedTime, createdAt)
            .execute();
    }

    public static Session getSession(String sessionId) {
        Session session = null;
        List<Object> results;
		try {
			results = new QueryBuilderOld()
			    .select("sessionId","user_id","lastAccessedTime","createdAt")
			    .from("sessions")
			    .where("sessionId", "=", sessionId, true)
			    .execute();
			
	        if (!results.isEmpty()) {
	            session = new Session(
	                (String) results.get(0),
	                (Integer) results.get(1),
	                ((long) results.get(2)),
	                ((long) results.get(3))
	            );
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


        return session;
    }
}
