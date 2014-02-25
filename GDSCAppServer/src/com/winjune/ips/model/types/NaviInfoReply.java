package com.winjune.ips.model.types;

import java.util.ArrayList;

public class NaviInfoReply {
	private int id;
	private int versionCode; 
	private ArrayList<NaviNodeReply> nodes;
	private ArrayList<NaviDataReply> paths;

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

	public ArrayList<NaviNodeReply> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<NaviNodeReply> nodes) {
		this.nodes = nodes;
	}
	
	public ArrayList<NaviDataReply> getPaths() {
		return paths;
	}

	public void setPaths(ArrayList<NaviDataReply> paths) {
		this.paths = paths;
	}
}
