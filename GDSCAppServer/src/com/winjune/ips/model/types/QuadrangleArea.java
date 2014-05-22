package com.winjune.ips.model.types;

public class QuadrangleArea {
	private Location A; // Vertex A
	private Location B; // Vertex B
	private Location C; // Vertex C
	private Location D; // Vertex D
	
	// A and C, B and D are diagonal
	
	public QuadrangleArea(Location a, Location b, Location c, Location d) {
		super();
		A = a;
		B = b;
		C = c;
		D = d;
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

	public Location getD() {
		return D;
	}

	public void setD(Location d) {
		D = d;
	}
	
	
}
