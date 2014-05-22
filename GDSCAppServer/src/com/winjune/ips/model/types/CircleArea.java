package com.winjune.ips.model.types;

//2014/05/22: Initialized by Derek

public class CircleArea {
	private int radius;
	private Location center;
	
	public CircleArea(int radius, Location center) {
		super();
		this.radius = radius;
		this.center = center;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	public Location getCenter() {
		return center;
	}
	
	public void setCenter(Location center) {
		this.center = center;
	}
}
