package com.ericsson.cgc.aurora.wifiips.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ericsson.cgc.aurora.wifiips.model.types.Location;
import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiips.utils.LogUtil;

public class FineLocationTable {
	public static final String TAG = "FineLocationTable";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;
	
	public static int getPositionId(String tagId) {
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
		
		String sql = "SELECT PositionID"
				+ " FROM finelocation WHERE RFID=?";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, tagId);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				int positionId = rs.getInt(1);
				return positionId;
			}
			
			return -1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return -1;
	}
	
	public static boolean insertRecord(String tagId, Location location) {
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
		
		if (location == null) {
			LogUtil.getInstance().log(
					TAG + ", " + "location is null.");
			return false;
		}
		
		int positionId = PositionTable.getPositionId(location);
		
		//LogUtil.getInstance().log(TAG + ", " + "positionId: " + positionId);
		
		String sql = "insert into finelocation (RFID, PositionID) values (?, ?)";
		
		//LogUtil.getInstance().log(TAG + ", " + "SQL: insert into finelocation (RFID, PositionID) values (" + tagId + "," + positionId + ")");
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, tagId);
			statement.setInt(2, positionId);
			
			statement.executeUpdate();

			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
	
	public static boolean updateRecord(String tagId, Location location) {
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
		
		if (location == null) {
			LogUtil.getInstance().log(
					TAG + ", " + "location is null.");
			return false;
		}
		
		int positionId = PositionTable.getPositionId(location);
		
		String sql = "update finelocation set PositionID=? where RFID=?";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, positionId);
			statement.setString(2, tagId);
			
			statement.executeUpdate();

			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
}
