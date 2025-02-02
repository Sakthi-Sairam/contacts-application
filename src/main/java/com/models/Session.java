package com.models;

import java.util.UUID;

public class Session implements BaseModel{
	@Column(name = "sessions.sessionId")
    private String sessionId;
	@Column(name = "sessions.user_id")
    private int userId;
	@Column(name = "sessions.lastAccessedTime")
    private long lastAccessedTime;
	@Column(name = "sessions.createdAt")
    private long createdAt;
//    private User user;
    public Session() {}

    public Session(int userId) {
        this.sessionId = UUID.randomUUID().toString(); // Generate random UUID
        this.userId = userId;
//        this.user = UserDao.getUserById(userId);
        this.lastAccessedTime = System.currentTimeMillis();
        this.createdAt = System.currentTimeMillis();
    }

    public Session(String sessionId, int userId, long lastAccessedTime, long createdAt) {
		this.sessionId = sessionId;
		this.userId = userId;
		this.lastAccessedTime = lastAccessedTime;
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Session [sessionId=" + sessionId + ", userId=" + userId + ", lastAccessedTime=" + lastAccessedTime
				+ ", createdAt=" + createdAt + "]";
	}

	public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public long getLastAccessedTime() { return lastAccessedTime; }
    public void setLastAccessedTime(long lastAccessedTime) { this.lastAccessedTime = lastAccessedTime; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

	@Override
	public Object getPrimaryKeyValue() {
		return this.sessionId;
	}

}
