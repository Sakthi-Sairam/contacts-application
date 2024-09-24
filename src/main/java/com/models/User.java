package com.models;

import java.util.*;

public class User {
	private int userId;
	private String firstName;
	private String lastName;
	private int age;
	private String address;
	private String phone;
	
	List<Contact> myContacts;
	List<Contact> favourites;
	List<Contact> archieves;
	List<Email> emails;
	
	public User(int userId, String firstName, String lastName, int age, String address, String phone) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.address = address;
		this.phone = phone;
		this.myContacts = new ArrayList<>();
		this.favourites = new ArrayList<>();
		this.archieves = new ArrayList<>();
		this.emails = new ArrayList<>();
		
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
    public List<Contact> getMyContacts() {
        return myContacts;
    }

    public void setMyContacts(List<Contact> myContacts) {
    	this.myContacts = new ArrayList<>(myContacts);
    }

	public List<Contact> getFavourites() {
		return favourites;
	}

	public void setFavourites(List<Contact> favourites) {
		this.favourites = favourites;
	}
	public List<Contact> getArchieves() {
		return archieves;
	}

	public void setArchieves(List<Contact> archieves) {
		this.archieves = archieves;
	}

	public List<Email> getEmails() {
		return emails;
	}

	public void setEmails(List<Email> emails) {
		this.emails = emails;
	}

	
}
