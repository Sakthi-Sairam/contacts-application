package com.models;

public class Email {
	public Email() {}
	public Email(int id, String email, int isPrimary) {
		this.id = id;
		this.email = email;
		this.isPrimary = isPrimary;
	}
	private int id;
	private String email;
	int isPrimary;
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

}
