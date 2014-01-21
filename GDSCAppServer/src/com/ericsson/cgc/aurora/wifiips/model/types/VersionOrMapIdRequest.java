package com.ericsson.cgc.aurora.wifiips.model.types;

public class VersionOrMapIdRequest {
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public String toString() {
		return String.valueOf(code);
	}
}
