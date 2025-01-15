package com.models;

/**
 * 
 * mysql> desc CategoryList;
+--------------+--------+------+-----+---------+----------------+
| Field        | Type   | Null | Key | Default | Extra          |
+--------------+--------+------+-----+---------+----------------+
| id           | int    | NO   | PRI | NULL    | auto_increment |
| categoryId   | int    | NO   | MUL | NULL    |                |
| MyContactsID | int    | NO   | MUL | NULL    |                |
| createdAt    | bigint | NO   |     | NULL    |                |
| modifiedAt   | bigint | NO   |     | NULL    |                |
+--------------+--------+------+-----+---------+----------------+

 */

public class CategoryList implements BaseModel{
	@Column(name = "CategoryList.id")
	private int id;
	@Column(name = "CategoryList.categoryId")
	private int categoryId;
	@Column(name = "CategoryList.MyContactsID")
	private int contactsId;
	@Column(name = "CategoryList.createdAt")
	private long createdAt;
	@Column(name = "CategoryList.modifiedAt")
	private long modifitedAt;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}


	public int getContactsId() {
		return contactsId;
	}

	public void setContactsId(int contactsId) {
		this.contactsId = contactsId;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public long getModifitedAt() {
		return modifitedAt;
	}

	public void setModifitedAt(long modifitedAt) {
		this.modifitedAt = modifitedAt;
	}

	@Override
	public Object getPrimaryKeyValue() {
		return this.id;
	}
	@Override
	public String toString() {
		return "CategoryList [id=" + id + ", categoryId=" + categoryId + ", contactsId=" + contactsId + ", createdAt="
				+ createdAt + ", modifitedAt=" + modifitedAt + "]";
	}

}
