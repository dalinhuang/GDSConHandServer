package com.winjune.ips.model.types;

import java.util.ArrayList;

// 2014/05/22: Initialized by Derek

public class UnreachableArea {
	private int mapId;
	private ArrayList<CircleArea> circles;
	private ArrayList<TriangleArea> triangles;
	private ArrayList<QuadrangleArea> quadrangles;
	
	public UnreachableArea(int mapId) {
		this.mapId = mapId;
		this.circles = new ArrayList<CircleArea>();
		this.triangles = new ArrayList<TriangleArea>();
	}
	
	public UnreachableArea(int mapId, ArrayList<CircleArea> circles,
			ArrayList<TriangleArea> triangle) {
		super();
		this.mapId = mapId;
		this.circles = circles;
		this.triangles = triangle;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public ArrayList<CircleArea> getCircles() {
		return circles;
	}

	public void setCircles(ArrayList<CircleArea> circles) {
		this.circles = circles;
	}

	public ArrayList<TriangleArea> getTriangles() {
		return triangles;
	}

	public void setTriangles(ArrayList<TriangleArea> triangle) {
		this.triangles = triangle;
	}

	public ArrayList<QuadrangleArea> getQuadrangles() {
		return quadrangles;
	}

	public void setQuadrangles(ArrayList<QuadrangleArea> quadrangles) {
		this.quadrangles = quadrangles;
	}
	
	
	
}
