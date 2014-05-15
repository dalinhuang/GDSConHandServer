package com.winjune.ips.model.types;

public class IndoorMapReply {
	private int id;
	private String normalMapUrl;
	private String largeMapUrl;
	private String name;
	private String label;	
	private int cellPixel;
	private int longitude;
	private int latitude;
	private int versionCode;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNormalMapUrl() {
		return normalMapUrl;
	}
	public void setNormalMapUrl(String normalMapUrl) {
		this.normalMapUrl = normalMapUrl;
	}
	public String getLargeMapUrl() {
		return largeMapUrl;
	}
	public void setLargeMapUrl(String largeMapUrl) {
		this.largeMapUrl = largeMapUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getCellPixel() {
		return cellPixel;
	}
	public void setCellPixel(int cellPixel) {
		this.cellPixel = cellPixel;
	}
	public int getLongitude() {
		return longitude;
	}
	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}
	public int getLatitude() {
		return latitude;
	}
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
}
