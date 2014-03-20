package com.winjune.ips.model.types;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.winjune.ips.db.MysqlManager;
import com.winjune.ips.utils.LogUtil;

public class CollectStatusReply {
	
	public static final String TAG = "CollectStatusReply";
	
	private int mapId;
	private int versionCode;
	private ArrayList<CellStatus> cells;
	
	public int getMapId() {
		return mapId;
	}
	
	public int getVersionCode() {
		return versionCode;
	}
	
	public ArrayList<CellStatus> getCells() {
		return cells;
	}
	
	public void queryCollectStatus(int mapid) {
		
		if (mapid != 0) {
			
			mapId = mapid;
								
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
			
			// get the version code of the map
			String sql1 = "SELECT version_code"
					+ " FROM map WHERE map_id=?";
			
			try {
				PreparedStatement statement1 = connection.prepareStatement(sql1);
				statement1.setInt(1, mapId);
				ResultSet rs1 = statement1.executeQuery();
			
				if (rs1.next()) {
					versionCode = rs1.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// get the cell status
			cells = new ArrayList<CellStatus>();
			
			String sql = "SELECT A.PositionID, B.col_id, B.row_id, COUNT(DISTINCT A.Collecting_Time) AS Collected_Times\n"
						  + "FROM fingerprint A, position B\n"
						  + "WHERE (A.PositionID = B.id AND B.map_id=?)\n"
						  + "GROUP BY A.PositionID";
			try {
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, mapId);
				ResultSet rs = statement.executeQuery();
				while (rs.next()) {
					int serial = rs.getInt(1); // PositionID
					int x = rs.getInt(2); // col_id
					int y = rs.getInt(3); // row_id
					
					// Catch invalid Ids (some for Map but not for cell)
					if ((x<0) || (y<0)) {
						LogUtil.getInstance().log(TAG
								+ "[" +mapId+","+x+","+y+"] position has dirty data in tables");
						continue;
					}
					
					CellStatus cell = new CellStatus();
					cell.setSerial(serial);
					cell.setX(x); 
					cell.setY(y); 
					cell.setCount(rs.getInt(4)); //The count of collection
					
					cells.add(cell);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // if (map!=null)
	} // queryCollectStatus
	
	public class CellStatus {
		private int serial;
		private int x;
		private int y;
		private int count;

		public int getSerial() {
			return serial ;
		}
		
		public void setSerial(int serial ){
			this.serial  = serial;
		}
		
		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

	}

}
