/**
 * @(#)Test.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.ericsson.cgc.aurora.wifiips.model.types;

/**
 * @author ezhipin, haleyshi
 * 
 */
public class Location {
	private int mapId;
	private int mapVersion;
	private int x;
	private int y;
	
	public Location(int mapId, int rowId, int colId, int v) {
		this.mapId = mapId;
		this.x = colId;  // x is column ID
		this.y = rowId;  // y is row ID
		this.mapVersion = v;
	}
	
	public Location() {
		
	}
	
	/**
	 * @return the mapId
	 */
	public int getMapId() {
		return mapId;
	}
	/**
	 * @param mapId the mapId to set
	 */
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	/**
	 * @return the x
	 */
	public int getColId() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setColId(int colId) {
		this.x = colId;
	}
	/**
	 * @return the y
	 */
	public int getRowId() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setRowId(int rowId) {
		this.y = rowId;
	}

	public int getMapVersion() {
		return mapVersion;
	}

	public void setMapVersion(int mapVersion) {
		this.mapVersion = mapVersion;
	}
	
	@Override
	public String toString() {
		return "Location [MapId=" + mapId + ", row=" + y + ", col=" + x + ", ver=" + mapVersion + "]";
	}
	
}
