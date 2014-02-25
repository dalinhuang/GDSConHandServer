package com.winjune.ips.db;

import java.sql.CallableStatement;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.winjune.ips.model.types.Location;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

public class PositionTable {
	public static final String TAG = "PositionTable";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	public static int getPositionId(Location loc) {
		return getPositionId(loc.getMapId(), loc.getRowId(), loc.getColId());
	}
	
	public static int getPositionId(int mapId, int row, int column) {
		MysqlManager instance = MysqlManager.getInstance();
		if (instance == null) {
			LogUtil.getInstance().log(
					TAG + ", " + "MysqlManager instance is null.");
			return -1;
		}

		Connection connection = instance.getConnection();
		if (connection == null) {
			LogUtil.getInstance().log(
					TAG + ", " + "connection is null.");
			return -1;
		}

		String this_sql = "{call get_positionid(?, ?, ?, ?)}";
		try {
			CallableStatement proc = connection.prepareCall(this_sql);
			proc.setInt("MapID_IN", mapId);
			proc.setInt("RowID_IN", row);
			proc.setInt("ColID_IN", column);
			proc.execute();
			return proc.getInt("PositionID");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.getInstance().log(TAG + ", SQLException: " + e.getMessage());
		}

		return -1;
	}

	public static boolean this_position_has_data(Location loc) {
		return this_position_has_data(loc.getMapId(), loc.getRowId(), loc.getColId());
	}
	
	public static boolean this_position_has_data(int mapId, int row, int column) {
		MysqlManager instance = MysqlManager.getInstance();
		if (instance == null) {
			LogUtil.getInstance().log(
					TAG + ", " + "MysqlManager instance is null.");
			return false;
		}

		Connection connection = instance.getConnection();
		if (connection == null) {
			LogUtil.getInstance().log(
					TAG + ", " + "connection is null.");
			return false;
		}
		int positionId = getPositionId(mapId, row, column);
		String this_sql = "SELECT ID FROM fingerprint_linear\n"
		        		+ "WHERE PositionID = " + positionId
		        		+ " LIMIT 1";
		try {
			Statement st;
			st = connection.createStatement();
			ResultSet rs = st.executeQuery(this_sql);
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			LogUtil.getInstance().log(TAG + ", SQLException: " + e.getMessage());
		}

		return false;
	}

	public static Location getPositionById(int positionId) {
		MysqlManager instance = MysqlManager.getInstance();
		if (instance == null) {
			LogUtil.getInstance().log(
					TAG + ", " + "MysqlManager instance is null.");
			return null;
		}

		Connection connection = instance.getConnection();
		if (connection == null) {
			LogUtil.getInstance().log(
					TAG + ", " +  "connection is null.");
			return null;
		}

		String sql = "SELECT A.map_id, A.row_id, A.col_id, B.version_code\n"
				   + "FROM position A INNER JOIN map B ON A.map_id = B.map_id\n"
				   + "WHERE A.id=?";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, positionId);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				int mapId = rs.getInt("map_id"),
					rowId = rs.getInt("row_id"),
					colId = rs.getInt("col_id"),
					verId = rs.getInt("version_code");

				Location location = new Location(mapId, rowId, colId, verId);
				return location;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.getInstance().log(TAG + ", SQLException: " + e.getMessage());
		}

		return null;
	}
}
