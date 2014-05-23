package com.winjune.ips.model.utils;

import com.winjune.ips.model.types.Location;

// 2014/05/20: Initialized by Derek

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
		
		//Return whether the dot is in the rectangle defined by the four vertexes (a and c, b and d are the diagonal)
		public static boolean isDotInQuadrangle(Location p, Location a, Location b, Location c, Location d) {
			
			if (isDotInTriangle(p, a, b, c) || isDotInTriangle(p, a, c, d)) {
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
			result.setMapId(a.getMapId());
			
			return result;
		}
		
		//Find the nearest point x in triangle from point p given that p is in the triangle
		public static Location findNearestPointInTriangle(Location p, Location a, Location b, Location c) {
			
			double pToab = pointToLineDistance(p, a, b);
			double pToac = pointToLineDistance(p, a, c);
			double pTobc = pointToLineDistance(p, b, c);
			
			double tmp = Math.min(pToab, pToac);
			
			if (pTobc < tmp) {
				//pTobc is the shortest one
				return findNearestPointInLine(p, b, c);
			}
			else {
				if (pToab < pToac) {
					//pToab is the shortest one
					return findNearestPointInLine(p, a, b);
				}
				else {
					//pToac is the shortest one
					return findNearestPointInLine(p, a, c);
				}
			}
			
		}
		
		//Find the nearest point x in quadrangle from point p given that p is in the quadrangle
		//given that p is in the quadrangle
		public static Location findNearestPointInQuadrangle(Location p, Location a, Location b, Location c, Location d) {
			
			double pToab = pointToLineDistance(p, a, b);
			double pTobc = pointToLineDistance(p, b, c);
			double pTocd = pointToLineDistance(p, c, d);
			double pToad = pointToLineDistance(p, a, d);
			
			double tmp1 = Math.min(pToab, pTobc);
			double tmp2 = Math.min(pTocd, pToad);
			
			if (tmp1 < tmp2) {
				if (pToab < pTobc) {
					//pToab is the shortest one
					return findNearestPointInLine(p, a, b);
				}
				else {
					//pTobc is the shortest one
					return findNearestPointInLine(p, b, c);
				}
			}
			else {
				if (pTocd < pToad) {
					//pTocd is the shortest one
					return findNearestPointInLine(p, c, d);
				}
				else {
					//pToad is the shortest one
					return findNearestPointInLine(p, a, d);
				}
			}
		}
		
		//Find the nearest point x in circle from which to point p is the shortest distance
		//given that p is in the circle
		public static Location findNearestPointInCircle(Location p, Location center, double radius) {
			
			Location result = new Location();
			result.setMapId(center.getMapId());
			
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
