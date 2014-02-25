package com.winjune.ips.model.types;

import java.util.ArrayList;


public class BuildingManagerReply {
	private int versionCode;
	private ArrayList<BuildingReply> buildings;
	
	public int getVersionCode() {
		return versionCode;
	}
	
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public ArrayList<BuildingReply> getBuildings() {
		return buildings;
	}

	public void setBuildings(ArrayList<BuildingReply> buildings) {
		this.buildings = buildings;
	}
}
