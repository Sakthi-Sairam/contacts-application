package com.Dao;

import com.models.Session;
import com.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
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

    public static void deleteExpiredSessions(LocalDateTime expirationTime) throws SQLException {
        String deleteSQL = "DELETE FROM sessions WHERE lastAccessedTime < ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(deleteSQL)) {
            ps.setObject(1, expirationTime);
            ps.executeUpdate();
        }
    }
    public static void deleteSessionById(String id) throws SQLException {
        String deleteSQL = "DELETE FROM sessions WHERE sessionId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(deleteSQL)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

	public static void createSession(String sessionId, int userId, LocalDateTime lastAccessedTime,
			LocalDateTime createdAt) throws SQLException {
        String deleteSQL = "insert into sessions values(?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(deleteSQL)) {
            ps.setString(1, sessionId);
            ps.setInt(2,userId);
            ps.setObject(3, lastAccessedTime);
            ps.setObject(4,createdAt);
            ps.executeUpdate();
        }
		
	}

	public static Session getSession(String sessionId) {
		String checkEmailSQL = "SELECT * FROM sessions WHERE sessionId = ?";
        ResultSet rs = null;
        Session session = null;
	    try (Connection connection = DBConnection.getConnection(); 
		         PreparedStatement ps = connection.prepareStatement(checkEmailSQL)) {
            ps.setString(1, sessionId);
            rs = ps.executeQuery();

            if (rs.next()) {
            	session = new Session(
            				rs.getString("sessionId"),
            				rs.getInt("user_id"),
            				rs.getTimestamp("lastAccessedTime").toLocalDateTime(),
            				rs.getTimestamp("createdAt").toLocalDateTime()
            			);
            }
	    	
	    } catch (SQLException e) {
			e.printStackTrace();
		}
	    return session;
		
	}

}
