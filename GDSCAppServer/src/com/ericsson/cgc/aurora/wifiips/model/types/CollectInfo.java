package com.ericsson.cgc.aurora.wifiips.model.types;

public class CollectInfo {
	private Location location;
	private WifiFingerPrint wifiFingerPrint;
	
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public WifiFingerPrint getWifiFingerPrint() {
		return wifiFingerPrint;
	}
	public void setWifiFingerPrint(WifiFingerPrint wifiFingerPrint) {
		this.wifiFingerPrint = wifiFingerPrint;
	}
}
