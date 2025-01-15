package com.models;

import java.util.*;

public class User implements BaseModel{
	@Column(name = "userdata.user_id")
	private int userId;
	@Column(name = "userdata.first_name")
	private String firstName;
	@Column(name = "userdata.last_name")
	private String lastName;
	@Column(name = "userdata.age")
	private int age;
	@Column(name = "userdata.address")
	private String address;
	@Column(name = "userdata.phone")
	private String phone;
	
	@Column(name = "userdata.password")
	private String password;
	
	@Column(name ="userdata.createdAt")
    private long createdAt;
	
	@Column(name ="userdata.modifiedAt")
    private long modifiedAt;


	private Integer primaryEmailId = null;
	
	List<Email> emails;
	
	public User() {
		this.emails = new ArrayList<>();
	}
	public User(int userId, String firstName, String lastName, int age, String address, String phone) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.address = address;
		this.phone = phone;
		this.emails = new ArrayList<>();
		
	}

	public User(int userId, String firstName, String lastName, int age, String address, String phone, long createdAt, long modifiedAt) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.address = address;
		this.phone = phone;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
//    public List<Contact> getMyContacts() {
//        return myContacts;
//    }
//
//    public void setMyContacts(List<Contact> myContacts) {
//    	this.myContacts = new ArrayList<>(myContacts);
//    }
//
//	public List<Contact> getFavourites() {
//		return favourites;
//	}
//
//	public void setFavourites(List<Contact> favourites) {
//		this.favourites = favourites;
//	}
//	public List<Contact> getArchieves() {
//		return archieves;
//	}
//
//	public void setArchieves(List<Contact> archieves) {
//		this.archieves = archieves;
//	}

	public List<Email> getEmails() {
		return emails;
	}

	public void setEmails(List<Email> emails) {
		this.emails = emails;
	}
	public int findPrimaryEmail() {
		int email_id=0;
		for(Email i: emails) {
			if(i.isPrimary==1) {
				email_id = i.getId();
				break;
			}
		}
		return email_id;
	}

	public int getPrimaryEmailId() {
		if(this.primaryEmailId == null) {
			this.primaryEmailId = findPrimaryEmail();
		}
		return primaryEmailId;
	}

	public void setPrimaryEmailId(int primaryEmailId) {
		this.primaryEmailId = primaryEmailId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	@Override
	public String toString() {
		return "User [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", age=" + age
				+ ", address=" + address + ", phone=" + phone + ", password=" + password + ", primaryEmailId="
				+ primaryEmailId + ", emails=" + emails + "]";
	}
	@Override
	public Object getPrimaryKeyValue() {
		return userId;
	}
	
	

	
}
