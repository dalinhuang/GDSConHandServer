package com.ericsson.cgc.aurora.wifiips.model.types;

public class TestRecord {
	private int id;
	private String mac;
	private String timestamp;
	private Location fromLocation;
	private Location locateLocation;
	private String from;

	public TestRecord(int id, String mac, String timestamp, Location fromLocation, Location locateLocation, String from) {
		setId(id);
		setMac(mac);
		setTimestamp(timestamp);
		setFromLocation(fromLocation);
		setLocateLocation(locateLocation);
		setFrom(from);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public Location getFromLocation() {
		return fromLocation;
	}

	public void setFromLocation(Location fromLocation) {
		this.fromLocation = fromLocation;
	}

	public Location getLocateLocation() {
		return locateLocation;
	}

	public void setLocateLocation(Location locateLocation) {
		this.locateLocation = locateLocation;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

}
