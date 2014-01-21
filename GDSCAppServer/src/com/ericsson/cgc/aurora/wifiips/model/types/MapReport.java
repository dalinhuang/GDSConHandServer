package com.ericsson.cgc.aurora.wifiips.model.types;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ericsson.cgc.aurora.wifiips.db.MysqlManager;
import com.ericsson.cgc.aurora.wifiips.utils.LogUtil;

public class MapReport {
	public static final String TAG = "MapReport";
	
	private int mapId;
	private String mapName;
	private int rows;
	private int columns;
	private ArrayList<MapCell> cells;
	
	public int getMapId() {
		return mapId;
	}
	
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	
	public String getMapName() {
		return mapName;
	}
	
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	
	public int getRows() {
		return rows;
	}
	
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public void setColumns(int columns) {
		this.columns = columns;
	}
	
	public ArrayList<MapCell> getCells() {
		return cells;
	}
	
	public void queryMapInfo(String map) {
		if (map != null) {
			try{
				mapId = Integer.parseInt(map);
				
				MysqlManager instance = MysqlManager.getInstance();
				if (instance == null) {
					LogUtil.getInstance().log(
							TAG + ", " + "MysqlManager instance is null.");
					return;
				}
	
				Connection connection = instance.getConnection();
				if (connection == null) {
					LogUtil.getInstance().log(
							TAG + ", " + "connection is null.");
					return;
				}
				
				String sql = "SELECT name, rows, columns"
						+ " FROM map WHERE map_id=?";
				try {
					PreparedStatement statement = connection.prepareStatement(sql);
					statement.setInt(1, mapId);
					ResultSet rs = statement.executeQuery();
					while (rs.next()) {
						mapName = rs.getString(1);
						rows = rs.getInt(2);
						columns = rs.getInt(3);
					}

				} catch (SQLException e) {
					e.printStackTrace();
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void queryMapData() {
		cells = new ArrayList<MapCell>();
		
		if (rows != 0) {
			MysqlManager instance = MysqlManager.getInstance();
			if (instance == null) {
				LogUtil.getInstance().log(
						TAG + ", " + "MysqlManager instance is null.");
				return;
			}

			Connection connection = instance.getConnection();
			if (connection == null) {
				LogUtil.getInstance().log(
						TAG + ", " + "connection is null.");
				return;
			}
			
			String sql = "SELECT A.id, A.col_id, A.row_id, MAX(B.Data_Count)\n"
					   + "FROM position A LEFT JOIN fingerprint_linear B ON A.id = B.positionid\n"
					   + "WHERE map_id=?\n"
					   + "GROUP BY A.id";
			try {
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, mapId);
				ResultSet rs = statement.executeQuery();
				while (rs.next()) {
					int x = rs.getInt(2);
					int y = rs.getInt(3);
					
					// Catch invalid Ids (some for Map but not for cell)
					if ((x<0) || (y<0) || (x>=columns) || (y>=rows)) {
						LogUtil.getInstance().log(TAG
								+ "[" +mapId+","+x+","+y+"] position has dirty data in tables");
						continue;
					}
					
					MapCell cell = new MapCell();
					cell.setPositionId(rs.getInt(1));
					cell.setX(x); 
					cell.setY(y); 
					cell.setCount(rs.getInt(4));
					
					cells.add(cell);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
	}
}
