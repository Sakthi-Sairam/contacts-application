package com.models;

public class Contact {
	@Column(name = "MyContactsID")
	private int MyContactsID;
	@Column(name = "alias_fnd_name")
	private String alias_name;
	@Column(name = "friend_email")
	private String friend_email;
	@Column(name = "phone")
	private String phone;
	@Column(name = "address")
	private String address;
	@Column(name = "isArchived")
	private int isArchived;
	@Column(name = "isFavorite")
	private int isFavorite;
	public Contact() {}
	public Contact(int MyContactsID, String alias_name, String friend_email, String phone, String address, int isArchived, int isFavorite) {
		this.MyContactsID = MyContactsID;
		this.alias_name = alias_name;
		this.friend_email = friend_email;
		this.phone = phone;
		this.address = address;
		this.isArchived = isArchived;
		this.isFavorite = isFavorite;
	}
	public int getMyContactsID() {
		return MyContactsID;
	}
	public void setMyContactsID(int MyContactsID) {
		this.MyContactsID = MyContactsID;
	}
	public String getAlias_name() {
		return alias_name;
	}
	public void setAlias_name(String alias_name) {
		this.alias_name = alias_name;
	}
	public String getFriend_email() {
		return friend_email;
	}
	public void setFriend_email(String friend_email) {
		this.friend_email = friend_email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getIsArchived() {
		return isArchived;
	}
	public void setIsArchived(int isArchived) {
		this.isArchived = isArchived;
	}
	public int getIsFavorite() {
		return isFavorite;
	}
	public void setIsFavorite(int isFavorite) {
		this.isFavorite = isFavorite;
	}
	
	
}
