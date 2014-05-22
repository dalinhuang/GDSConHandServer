package com.winjune.ips.model.types;

//2014/05/22: Initialized by Derek

public class TriangleArea {
	private Location A; // Vertex A
	private Location B; // Vertex B
	private Location C; // Vertex C
	
	public TriangleArea(Location a, Location b, Location c) {
		super();
		A = a;
		B = b;
		C = c;
	}

	public Location getA() {
		return A;
	}

	public void setA(Location a) {
		A = a;
	}

	public Location getB() {
		return B;
	}

	public void setB(Location b) {
		B = b;
	}

	public Location getC() {
		return C;
	}

	public void setC(Location c) {
		C = c;
	}
}
