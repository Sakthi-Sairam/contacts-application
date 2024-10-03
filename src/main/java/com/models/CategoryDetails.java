package com.models;

import java.util.*;

public class CategoryDetails{
	private int categoryId;
	private String categoryName;
	private List<Contact> categoryContacts;
	public CategoryDetails(int categoryId, String categoryName) {
		this.categoryId = categoryId;
		this.categoryName=categoryName;
		this.categoryContacts = new ArrayList<>();
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public List<Contact> getCategoryContacts() {
		return categoryContacts;
	}
	public void setCategoryContacts(List<Contact> categoryContacts) {
		this.categoryContacts = categoryContacts;
	}
	
	
}