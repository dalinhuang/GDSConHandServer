package com.winjune.ips.model.types;

public class TraceRecord {
	private int id;
	private int positionId;
	private String positionDescription;
	private String timestamp;
	private String fromSignal;
	private int mapId;
	private int rowId;
	private int colId;

	public TraceRecord(int id, int positionId, String positionDescription,
			String timestamp, String fromSignal, int mapId, int rowId, int colId) {
		this.id = id;
		this.positionId = positionId;
		this.positionDescription = positionDescription;
		this.timestamp = timestamp;
		this.fromSignal = fromSignal;
		this.mapId = mapId;
		this.rowId = rowId;
		this.colId = colId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPositionId() {
		return positionId;
	}

	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}

	public String getPositionDescription() {
		return positionDescription;
	}

	public void setPositionDescription(String positionDescription) {
		this.positionDescription = positionDescription;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getFromSignal() {
		return fromSignal;
	}

	public void setFromSignal(String fromSignal) {
		this.fromSignal = fromSignal;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public int getColId() {
		return colId;
	}

	public void setColId(int colId) {
		this.colId = colId;
	}

}
