package com.winjune.ips.model.types;

import java.util.ArrayList;

public class LocationSet {
	private ArrayList<Location> locations;

	public ArrayList<Location> getLocations() {
		return locations;
	}

	public void setLocations(ArrayList<Location> locations) {
		this.locations = locations;
	}
	
	public void addLocation(Location loc) {
		this.locations.add(loc);
	}
}
