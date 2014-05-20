package com.winjune.ips.model.utils;

import com.winjune.ips.model.types.Location;

public class MathUtil {

	// Calculate the area of a triangle which is defined by the 3 vertexes a, b and c
		public static double calculateTriangleArea(Location a, Location b, Location c) {
			float xa = a.getColId();
			float ya = a.getRowId();
			float xb = b.getColId();
			float yb = b.getRowId();
			float xc = c.getColId();
			float yc = c.getRowId();
			
			return Math.abs(0.5*(xa*yb + xb*yc + xc*ya - xa*yc - xb*ya - xc*yb));
			
		}
		
		// Return whether the dot is in the triangle defined by 3 vertexes a, b and c
		public static boolean isDotInTriangle(Location dot, Location a, Location b, Location c) {
			double s1 = calculateTriangleArea(dot, a, b);
			double s2 = calculateTriangleArea(dot, a, c);
			double s3 = calculateTriangleArea(dot, b, c);
			
			double S = calculateTriangleArea(a, b, c);
			
			if ((s1 + s2 + s3 - S) < 1) {
				return true;
			}
			
			return false;

		}
		
		//Return whether the dot is in the circle defined by the center and radius
		public static boolean isDotInCircle(Location p, Location center, double radius) {
			
			if (pointToPointDistance(p, center) <= radius) {
				return true;
			}
			
			return false;
		}
		
		//Calculate the distance between two points
		public static double pointToPointDistance(Location a, Location b) {
			float xa = a.getColId();
			float ya = a.getRowId();
			float xb = b.getColId();
			float yb = b.getRowId();
			
			double A = xb - xa;
			double B = yb - ya;
			
			return Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2));
		}
		
		// Calculate the distance from point p to the line, of which the vertex are a and b
		public static double pointToLineDistance(Location p, Location a, Location b) {
			float xa = a.getColId();
			float ya = a.getRowId();
			float xb = b.getColId();
			float yb = b.getRowId();
			float xp = p.getColId();
			float yp = p.getRowId();
			
			double A = yb - ya;
			double B = xa - xb;
			double C = ya*xb - xa*yb;
			
			double tmp = Math.abs(B*yp + A*xp + C);
			double sqrt = Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2));
			
			return tmp/sqrt;
		}
		
		// Find the point x in line ab so that line px and line ab is perpendicular 
		public static Location findNearestPointInLine(Location p, Location a, Location b) {
			float xa = a.getColId();
			float ya = a.getRowId();
			float xb = b.getColId();
			float yb = b.getRowId();
			float xp = p.getColId();
			float yp = p.getRowId();
			
			double A = yb - ya;
			double B = xa - xb;
			double C = ya*xb - xa*yb;
			double C2 = xp*B - yp*A;
			
			double resultX = (B*C2 - A*C)/(Math.pow(A, 2) + Math.pow(B, 2));
			double resultY = (-B*C - A*C2)/(Math.pow(A, 2) + Math.pow(B, 2));
			
			Location result = new Location();
			
			result.setColId((int) resultX);
			result.setRowId((int) resultY);
			
			return result;
		}
		
		//Find the nearest point x in circle from which to point p is the shortest distance
		public static Location findNearestPointInCircle(Location p, Location center, double radius) {
			
			Location result = new Location();
			
			float x1 = center.getColId();
			float y1 = center.getRowId();
			float xp = p.getColId();
			float yp = p.getRowId();
			
			double K = (yp - y1)/(xp - x1);
			
			double resultX1 = x1 + radius/Math.sqrt((1+Math.pow(K, 2)));
			double resultX2 = x1 - radius/Math.sqrt((1+Math.pow(K, 2)));
			double resultY1 = y1 + radius*K/Math.sqrt((1+Math.pow(K, 2)));
			double resultY2 = y1 - radius*K/Math.sqrt((1+Math.pow(K, 2)));
			
			if (xp >= x1) {
				result.setColId((int) resultX1);
			}
			else {
				result.setColId((int) resultX2);
			}
			
			if (yp >= y1) {
				if (K>0) {
					result.setRowId((int) resultY1);
				}
				else {
					result.setRowId((int) resultY2);
				}
				
			}
			else {
				if (K>0) {
					result.setRowId((int) resultY2);
				}
				else{
					result.setRowId((int) resultY1);
				}
			}
			
			return result;
		}
}
