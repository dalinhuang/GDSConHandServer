package com.ericsson.cgc.aurora.wifiips.model.types;

import java.util.ArrayList;

public class MapManagerReply {
	private int versionCode;
	private ArrayList<MapManagerItemReply> mapItems;
	
	public int getVersionCode() {
		return versionCode;
	}
	
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public ArrayList<MapManagerItemReply> getMapItems() {
		return mapItems;
	}

	public void setMapItems(ArrayList<MapManagerItemReply> mapItems) {
		this.mapItems = mapItems;
	}
}
