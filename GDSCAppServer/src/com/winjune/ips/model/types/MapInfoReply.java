package com.winjune.ips.model.types;

import java.util.ArrayList;

public class MapInfoReply {
	private int id;
	private int versionCode;
	private ArrayList<FieldInfoReply> fields;

	public ArrayList<FieldInfoReply> getFields() {
		return fields;
	}

	public void setFields(ArrayList<FieldInfoReply> fields) {
		this.fields = fields;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
