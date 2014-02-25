package com.winjune.ips.model.types;

import java.util.ArrayList;

public class QueryInfo {
	private ArrayList<LocationQueryInfo> mInfos;
	
	public QueryInfo() {
		mInfos = new ArrayList<LocationQueryInfo>();
	}
	
	public void add(LocationQueryInfo info) {
		mInfos.add(info);
	}
	
	public int size() {
		return mInfos.size();
	}

	public ArrayList<LocationQueryInfo> getInfos() {
		return mInfos;
	}

	public void setInfos(ArrayList<LocationQueryInfo> infos) {
		mInfos = infos;
	}
}
