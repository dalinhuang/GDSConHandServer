package com.ericsson.cgc.aurora.wifiips.model.types;

public class NfcLocation {
	private String tagId;
	private Location location;

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
}
