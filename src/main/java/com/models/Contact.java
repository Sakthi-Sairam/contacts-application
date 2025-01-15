package com.models;

public class Contact implements BaseModel{
	@Column(name = "MyContactsData.MyContactsID")
	private int contactId;
	@Column(name = "MyContactsData.alias_fnd_name")
	private String alias_name;
	@Column(name = "MyContactsData.friend_email")
	private String friend_email;
	@Column(name = "MyContactsData.phone")
	private String phone;
	@Column(name = "MyContactsData.address")
	private String address;
	@Column(name = "MyContactsData.isArchived")
	private int isArchived;
	@Column(name = "MyContactsData.isFavorite")
	private int isFavorite;
	
	@Column(name ="MyContactsData.createdAt")
    private long createdAt;
	
	@Column(name ="MyContactsData.modifiedAt")
    private long modifiedAt;
	
	public Contact() {}
	public Contact(int contactsID, String alias_name, String friend_email, String phone, String address, int isArchived, int isFavorite) {
		this.contactId = contactsID;
		this.alias_name = alias_name;
		this.friend_email = friend_email;
		this.phone = phone;
		this.address = address;
		this.isArchived = isArchived;
		this.isFavorite = isFavorite;
	}
	public Contact(int myContactsID, String alias_name, String friend_email, String phone, String address, int isArchived, int isFavorite, long createdAt, long modifiedAt) {
		contactId = myContactsID;
		this.alias_name = alias_name;
		this.friend_email = friend_email;
		this.phone = phone;
		this.address = address;
		this.isArchived = isArchived;
		this.isFavorite = isFavorite;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}
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
		return "Contact [MyContactsID=" + contactId + ", alias_name=" + alias_name + ", friend_email=" + friend_email
				+ ", phone=" + phone + ", address=" + address + ", isArchived=" + isArchived + ", isFavorite="
				+ isFavorite + ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt + "]";
	}
	@Override
	public Object getPrimaryKeyValue() {
		return contactId;
	}
	
	
}
