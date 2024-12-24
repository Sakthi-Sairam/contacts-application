package com.models;

import java.util.*;
import com.models.Column;

public class CategoryDetails {
    @Column(name = "CategoryDetails.categoryId")
    private int categoryId;

    @Column(name = "CategoryDetails.categoryName")
    private String categoryName;
    
	@Column(name ="CategoryDetails.createdAt")
    private long createdAt;
	
	@Column(name ="CategoryDetails.modifiedAt")
    private long modifiedAt;


	private List<Contact> categoryContacts;

    public CategoryDetails() {
        this.categoryContacts = new ArrayList<>();
    }

    public CategoryDetails(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryContacts = new ArrayList<>();
    }
    
    public CategoryDetails(int categoryId, String categoryName, long createdAt, long modifiedAt) {
        this.categoryContacts = new ArrayList<>();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
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