package com.models;

public class ServerRegistry implements BaseModel{

    public ServerRegistry() {}

    public ServerRegistry(int id, String ipAddress, int portNumber, String registeredAt, String lastHeartbeat) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.registeredAt = registeredAt;
        this.lastHeartbeat = lastHeartbeat;
    }

    @Column(name = "server_registry.id")
    private int id;

    @Column(name = "server_registry.ip_address")
    private String ipAddress;

    @Column(name = "server_registry.port_number")
    private int portNumber;

    @Column(name = "server_registry.registered_at")
    private String registeredAt;

    @Column(name = "server_registry.last_heartbeat")
    private String lastHeartbeat;

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

    public String getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(String registeredAt) {
        this.registeredAt = registeredAt;
    }

    public String getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(String lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

	@Override
	public Object getPrimaryKeyValue() {
		return this.id;
	}
}
