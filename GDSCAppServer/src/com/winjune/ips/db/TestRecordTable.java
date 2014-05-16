package com.winjune.ips.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.winjune.ips.model.types.Location;
import com.winjune.ips.model.types.TestRecord;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

public class TestRecordTable {
	public static final String TAG = "TestRecordTable";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;
	
	public static boolean insertRecord(String deviceMacAddress, Location location1, Location location2, String from) {
		MysqlManager instance = MysqlManager.getInstance();
		if (instance == null) {
			LogUtil.getInstance().log(TAG + ", MysqlManager instance is null.");
			return false;
		}

		Connection connection = instance.getConnection();
		if (connection == null) {
			LogUtil.getInstance().log(TAG + ", connection is null.");
			return false;
		}
		
		String sql = "insert into test_record (deviceMac, time, fromMap, fromX, fromY, locateMap, locateX, locateY, fromSignal) values (?, now(), ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, deviceMacAddress);
			statement.setInt(2, location1.getMapId());
			statement.setInt(3, location1.getColId());
			statement.setInt(4, location1.getRowId());
			statement.setInt(5, location2.getMapId());
			statement.setInt(6, location2.getColId());
			statement.setInt(7, location2.getRowId());
			statement.setString(8, from);
			
			statement.executeUpdate();

			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.getInstance().log(TAG + ", SQLException: " + e.getMessage());
		}
		
		return false;
	}
	
	public static ArrayList<TestRecord> queryTestRecord() {
		MysqlManager instance = MysqlManager.getInstance();
		if (instance == null) {
			LogUtil.getInstance().log(TAG + ", MysqlManager instance is null.");
			return null;
		}

		Connection connection = instance.getConnection();
		if (connection == null) {
			LogUtil.getInstance().log(TAG + ", connection is null.");
			return null;
		}
		
		
		ArrayList<TestRecord> recordList = new ArrayList<TestRecord>();
		
		String sql = "SELECT ID, deviceMac, time, fromMap, fromX, fromY, locateMap, locateX, locateY, fromSignal"
		             + " FROM test_record ORDER BY time DESC";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1),
				    mapId1 = rs.getInt(4),
				    rowId1 = rs.getInt(5),
				    colId1 = rs.getInt(6),
				    mapId2 = rs.getInt(7),
				    rowId2 = rs.getInt(8),
				    colId2 = rs.getInt(9);
				String from = rs.getString(10),
				       mac = rs.getString(2),
				       timestamp = rs.getString(3);
				
				TestRecord record = new TestRecord(id, mac, timestamp,
						new Location(mapId1, rowId1, colId1),
						new Location(mapId2, rowId2, colId2), from);	
				recordList.add(record);
			}
			
			return recordList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.getInstance().log(TAG + ", SQLException: " + e.getMessage());
		}

		return null;
	}
}
