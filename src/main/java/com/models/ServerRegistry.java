package com.models;

public class ServerRegistry implements BaseModel{
	
    @Column(name = "server_registry.id")
    private int id;

    @Column(name = "server_registry.ip_address")
    private String ipAddress;

    @Column(name = "server_registry.port_number")
    private int portNumber;

    @Column(name = "server_registry.created_at")
    private long createdAt;


    public ServerRegistry() {}



	public ServerRegistry(int id, String ipAddress, int portNumber, long createdAt) {
		this.id = id;
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;
		this.createdAt = createdAt;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getIpAddress() {
		return ipAddress;
	}



	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}



	public int getPortNumber() {
		return portNumber;
	}



	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}



	public long getCreatedAt() {
		return createdAt;
	}



	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}



	@Override
	public Object getPrimaryKeyValue() {
		return this.id;
	}
}
