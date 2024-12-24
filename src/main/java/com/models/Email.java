package com.models;

public class Email {
	@Column(name = "MailMapper.id")
	private int id;
	@Column(name="MailMapper.email")
	private String email;
	@Column(name="MailMapper.isPrimary")
	int isPrimary;
	
	@Column(name ="MailMapper.createdAt")
    private long createdAt;
	
	@Column(name ="MailMapper.modifiedAt")
    private long modifiedAt;
	
	public Email() {}
	public Email(int id, String email, int isPrimary) {

		this.id = id;
		this.email = email;
		
		this.isPrimary = isPrimary;
	}
	
	public Email(int id, String email, int isPrimary, long createdAt, long modifiedAt) {
		this.id = id;
		this.email = email;
		this.isPrimary = isPrimary;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(int isPrimary) {
		this.isPrimary = isPrimary;
	}
	public long getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}
	public long getModifiedAt() {
		return modifiedAt;
	}
	public void setModifiedAt(long modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
	

}
