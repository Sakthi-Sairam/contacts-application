package com.models;

public class ContactEmail implements BaseModel {
    @Column(name = "ContactsEmail.contact_email_id")
    private int contactEmailId;

    @Column(name = "ContactsEmail.MyContactsID")
    private int contactId;

    @Column(name = "ContactsEmail.email")
    private String email;

    @Column(name = "ContactsEmail.label")
    private String label;

    @Column(name = "ContactsEmail.created_at")
    private long createdAt;

    @Column(name = "ContactsEmail.updated_at")
    private long updatedAt;

    public ContactEmail() {}

    public ContactEmail(int contactId, String email, String label, long createdAt, long updatedAt) {
        this.contactId = contactId;
        this.email = email;
        this.label = label;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getContactEmailId() {
        return contactEmailId;
    }

    public void setContactEmailId(int contactEmailId) {
        this.contactEmailId = contactEmailId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
        return contactEmailId;
    }

    @Override
    public String toString() {
        return "ContactEmail [contactEmailId=" + contactEmailId + ", contactId=" + contactId + ", email=" + email
                + ", label=" + label + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
}
