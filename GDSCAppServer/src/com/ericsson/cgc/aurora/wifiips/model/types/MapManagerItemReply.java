package com.ericsson.cgc.aurora.wifiips.model.types;

public class MapManagerItemReply {
	private int mapId;   
	private String title;  
	private String version; 
	private int rows;      
	private int columns; 

	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public int getRows(){
		return rows;
	}
	
	public void setRows(int rows){
		this.rows = rows;
	}
	
	public int getColumns(){
		return columns;
	}
	
	public void setColumns(int columns){
		this.columns = columns;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
}
