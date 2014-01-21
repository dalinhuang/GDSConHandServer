package com.ericsson.cgc.aurora.wifiips.model.types;

public class User {
	private int id;
	private String deviceMacAddress;
	private String deviceName;
	private String loginName;

	public User(int id, String deviceMacAddress, String deviceName, String loginName) {
		this.id = id;
		this.deviceMacAddress = deviceMacAddress;
		this.deviceName = deviceName;
		this.loginName = loginName;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDeviceMacAddress() {
		return deviceMacAddress;
	}

	public void setDeviceMacAddress(String deviceMacAddress) {
		this.deviceMacAddress = deviceMacAddress;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}
