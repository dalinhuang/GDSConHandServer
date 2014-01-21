package com.ericsson.cgc.aurora.wifiips.model.types;

public class NfcFingerPrint {
	private String accountName;
	private String myWifiMac;
	private String deviceName;
	private String tagId;
	
	public NfcFingerPrint(String tagId) {
		setTagId(tagId);
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	
	public String getMyWifiMac() {
		return myWifiMac;
	}

	public void setMyWifiMac(String myWifiMac) {
		this.myWifiMac = myWifiMac;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
}
