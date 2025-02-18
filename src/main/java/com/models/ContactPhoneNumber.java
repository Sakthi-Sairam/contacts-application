package com.models;

public class ContactPhoneNumber implements BaseModel {
    @Column(name = "ContactsPhoneNumber.contact_number_id")
    private int contactNumberId;

    @Column(name = "ContactsPhoneNumber.MyContactsID")
    private int contactId;

    @Column(name = "ContactsPhoneNumber.phone_number")
    private String phoneNumber;

    @Column(name = "ContactsPhoneNumber.label")
    private String label;

    @Column(name = "ContactsPhoneNumber.created_at")
    private long createdAt;

    @Column(name = "ContactsPhoneNumber.updated_at")
    private long updatedAt;

    public ContactPhoneNumber() {}

    public ContactPhoneNumber(int contactId, String phoneNumber, String label, long createdAt, long updatedAt) {
        this.contactId = contactId;
        this.phoneNumber = phoneNumber;
        this.label = label;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getContactNumberId() {
        return contactNumberId;
    }

    public void setContactNumberId(int contactNumberId) {
        this.contactNumberId = contactNumberId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
        return contactNumberId;
    }

    @Override
    public String toString() {
        return "ContactPhoneNumber [contactNumberId=" + contactNumberId + ", contactId=" + contactId + ", phoneNumber="
                + phoneNumber + ", label=" + label + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
}
