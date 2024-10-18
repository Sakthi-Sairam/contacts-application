package com.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Session {
    private String sessionId;
    private int userId;
    private LocalDateTime lastAccessedTime;
    private LocalDateTime createdAt;
//    private User user;

    public Session(int userId) {
        this.sessionId = UUID.randomUUID().toString(); // Generate random UUID
        this.userId = userId;
//        this.user = userDao.getUserById(userId);
        this.lastAccessedTime = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    public Session(String sessionId, int userId, LocalDateTime lastAccessedTime, LocalDateTime createdAt) {
		this.sessionId = sessionId;
		this.userId = userId;
		this.lastAccessedTime = lastAccessedTime;
		this.createdAt = createdAt;
	}

	public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public LocalDateTime getLastAccessedTime() { return lastAccessedTime; }
    public void setLastAccessedTime(LocalDateTime lastAccessedTime) { this.lastAccessedTime = lastAccessedTime; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}
