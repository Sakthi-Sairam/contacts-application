package com.models;

import java.util.*;
import com.models.Column;

public class CategoryDetails {
    @Column(name = "categoryId")
    private int categoryId;

    @Column(name = "categoryName")
    private String categoryName;

    private List<Contact> categoryContacts;

    // Default constructor
    public CategoryDetails() {
        this.categoryContacts = new ArrayList<>();
    }

    // Parameterized constructor
    public CategoryDetails(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
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