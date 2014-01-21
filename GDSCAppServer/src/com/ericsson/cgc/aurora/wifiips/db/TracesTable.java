package com.ericsson.cgc.aurora.wifiips.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ericsson.cgc.aurora.wifiips.model.types.TraceRecord;
import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiips.utils.LogUtil;

public class TracesTable {
	public static final String TAG = "TracesTable";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;
	
	public static boolean insertRecord(String deviceMacAddress, int positionId, String fromSignal) {
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
		
		String sql = "insert into traces (device_mac, position_id, time, from_signal) values (?, ?, now(), ?)";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, deviceMacAddress);
			statement.setInt(2, positionId);
			statement.setString(3, fromSignal);
			
			statement.executeUpdate();

			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.getInstance().log(TAG + ", SQLException: " + e.getMessage());
		}
		
		return false;
	}
	
	public static ArrayList<TraceRecord> queryTraces(String deviceMacAddress) {
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
		
		ArrayList<TraceRecord> recordList = new ArrayList<TraceRecord>();
		
		String sql = "SELECT traces.id, traces.position_id, position.description, traces.time, traces.from_signal, position.map_id, position.row_id, position.col_id"
				+ " FROM traces, position WHERE traces.device_mac=? AND traces.position_id=position.id ORDER BY traces.time DESC";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, deviceMacAddress);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				int positionId = rs.getInt(2);
				String positionDescription = rs.getString(3);
				String timestamp = rs.getString(4);
				String fromSignal = rs.getString(5);
				int mapId = rs.getInt(6);
				int rowId = rs.getInt(7);
				int colId = rs.getInt(8);
				
				TraceRecord record = new TraceRecord(id, positionId, positionDescription, timestamp, fromSignal, mapId, rowId, colId);
				
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
