package com.winjune.ips.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.winjune.ips.model.types.CircleArea;
import com.winjune.ips.model.types.Location;
import com.winjune.ips.model.types.QuadrangleArea;
import com.winjune.ips.model.types.TriangleArea;
import com.winjune.ips.model.types.UnreachableArea;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

//2014/05/22: Initialized by Derek

public class UnreachableAreaTable {

	public static final String TAG = "UnreachableTable";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;
	
	public static UnreachableArea getUnreachableArea(int mapId) {
		
		UnreachableArea ura = new UnreachableArea(mapId);
		
		MysqlManager instance = MysqlManager.getInstance();
		if (instance == null) {
			LogUtil.getInstance().log(
					TAG + ", " + "MysqlManager instance is null.");
			return null;
		}

		Connection connection = instance.getConnection();
		if (connection == null) {
			LogUtil.getInstance().log(
					TAG + ", " + "connection is null.");
			return null;
		}
		
		String sql1 = "SELECT AreaID, AreaType, CircleIndex, TriangleIndex, QuadrangleIndex"
				+ "FROM unreachable_area WHERE MapID=?";
		
		try {
			PreparedStatement statement1 = connection.prepareStatement(sql1);
			statement1.setInt(1, mapId);
			ResultSet rs1 = statement1.executeQuery();
		
			if (rs1.next()) {
				do {
					String areaType = rs1.getString("AreaType");
					int index;
					if  (areaType == "circle") {
							index = rs1.getInt("CircleIndex");
							
							String sql2 = "SELECT Center_ColID, Center_RowID, Radius"
									+ "FROM circle_area WHERE ID=?";
							PreparedStatement statement2 = connection.prepareStatement(sql2);
							statement2.setInt(1, index);
							ResultSet rs2 = statement2.executeQuery();
							
							if (rs2.next()) {
								Location center = new Location(mapId, rs2.getInt("Center_RowId"), rs2.getInt("Center_ColID"));
								CircleArea ca = new CircleArea(rs2.getInt("Radius"), center);
								
								ura.getCircles().add(ca);
							}
							
					}
					else if (areaType == "triangle") {
							index = rs1.getInt("TriangleIndex");
							
							String sql3 = "SELECT A_ColID, A_RowID, B_ColID, B_RowID, C_ColID, C_RowID"
									+ "FROM triangle_area WHERE ID=?";
							PreparedStatement statement3 = connection.prepareStatement(sql3);
							statement3.setInt(1, index);
							ResultSet rs3 = statement3.executeQuery();
							
							if (rs3.next()) {
								Location A = new Location(mapId, rs3.getInt("A_RowID"), rs3.getInt("A_ColID"));
								Location B = new Location(mapId, rs3.getInt("B_RowID"), rs3.getInt("B_ColID"));
								Location C = new Location(mapId, rs3.getInt("C_RowID"), rs3.getInt("C_ColID"));
								TriangleArea ta = new TriangleArea(A, B, C);
								
								ura.getTriangles().add(ta);
							}
					}
					else if (areaType == "quadrangle") {
							index = rs1.getInt("QuadrangleIndex");
							
							String sql4 = "SELECT A_ColID, A_RowID, B_ColID, B_RowID, C_ColID, C_RowID, D_ColID, D_RowID"
									+ "FROM triangle_area WHERE ID=?";
							PreparedStatement statement4 = connection.prepareStatement(sql4);
							statement4.setInt(1, index);
							ResultSet rs4 = statement4.executeQuery();
							
							if (rs4.next()) {
								Location A = new Location(mapId, rs4.getInt("A_RowID"), rs4.getInt("A_ColID"));
								Location B = new Location(mapId, rs4.getInt("B_RowID"), rs4.getInt("B_ColID"));
								Location C = new Location(mapId, rs4.getInt("C_RowID"), rs4.getInt("C_ColID"));
								Location D = new Location(mapId, rs4.getInt("D_RowID"), rs4.getInt("D_ColID"));
								QuadrangleArea qa = new QuadrangleArea(A, B, C, D);
								
								ura.getQuadrangles().add(qa);

							}
					}
					
				} while (rs1.next());
			}
			else {
				// no unreachable area for this map
				LogUtil.getInstance().log(
						TAG + ", " + "No unreachable area for map" + Integer.toString(mapId));
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ura;
	}
}
