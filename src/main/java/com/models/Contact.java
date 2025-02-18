package com.models;

import java.util.List;

public class Contact implements BaseModel{
	@Column(name = "MyContactsData.MyContactsID")
	private int contactId;
	@Column(name = "MyContactsData.alias_fnd_name")
	private String alias_name;
	@Column(name = "MyContactsData.address")
	private String address;
	@Column(name = "MyContactsData.isArchived")
	private int isArchived;
	@Column(name = "MyContactsData.isFavorite")
	private int isFavorite;
	
	@Column(name = "MyContactsData.resourceName")
	private String resourceName;
	
	@Column(name ="MyContactsData.createdAt")
    private long createdAt;
	
	@Column(name ="MyContactsData.modifiedAt")
    private long modifiedAt;
	
    private List<ContactPhoneNumber> phoneNumbers;
    private List<ContactEmail> emails;
	
	public Contact() {}
	
	public int getMyContactsID() {
		return contactId;
	}
	public void setMyContactsID(int MyContactsID) {
		this.contactId = MyContactsID;
	}
	public String getAlias_name() {
		return alias_name;
	}
	public void setAlias_name(String alias_name) {
		this.alias_name = alias_name;
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
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
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

    public List<ContactPhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<ContactPhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public List<ContactEmail> getEmails() {
        return emails;
    }

    public void setEmails(List<ContactEmail> emails) {
        this.emails = emails;
    }
	
	@Override
	public String toString() {
		return "Contact [MyContactsID=" + contactId + ", alias_name=" + alias_name + ", friend_email=" + ", address=" + address + ", isArchived=" + isArchived + ", isFavorite="
				+ isFavorite + ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt + "]";
	}
	@Override
	public Object getPrimaryKeyValue() {
		return contactId;
	}
	
	
}
