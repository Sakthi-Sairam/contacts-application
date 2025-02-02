package com.models;

public class OAuthToken implements BaseModel {
	@Column(name = "oauth_tokens.id")
	private int id;
	@Column(name = "oauth_tokens.user_id")
	private int userId;
	@Column(name = "oauth_tokens.refresh_token")
	private String refreshToken;
	@Column(name = "oauth_tokens.email")
	private String email;
	@Column(name = "oauth_tokens.provider")
	private String provider;
	@Column(name = "oauth_tokens.created_at")
	private long createdAt;
	@Column(name = "oauth_tokens.updated_at")
	private long updatedAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public Object getPrimaryKeyValue() {
		return this.id;
	}
}