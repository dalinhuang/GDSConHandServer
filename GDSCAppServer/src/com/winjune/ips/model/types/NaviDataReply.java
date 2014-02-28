package com.winjune.ips.model.types;

public class NaviDataReply {
	private int from;
	private int to;
	private int distance;
	private String forwardInfo;
	private String backwardInfo;
	
	public int getFrom() {
		return from;
	}
	
	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getForwardInfo() {
		return forwardInfo;
	}

	public void setForwardInfo(String info) {
		this.forwardInfo = info;
	}

	public String getBackwardInfo() {
		return backwardInfo;
	}

	public void setBackwardInfo(String info) {
		this.backwardInfo = info;
	}	
}
