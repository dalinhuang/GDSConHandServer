package com.ericsson.cgc.aurora.wifiips.model.types;

public class IndoorMapReply {
	private String name;
	private String version;
	private int versionCode;
	private String editTime;
	private int id;
	private String pictureName;	
	private InitialMapReply initialMap;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getEditTime() {
		return editTime;
	}

	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPictureName() {
		return pictureName;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}

	public InitialMapReply getInitialMap() {
		return initialMap;
	}

	public void setInitialMap(InitialMapReply initialMap) {
		this.initialMap = initialMap;
	}
}
