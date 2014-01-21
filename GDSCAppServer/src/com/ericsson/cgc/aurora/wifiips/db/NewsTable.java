package com.ericsson.cgc.aurora.wifiips.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiips.utils.LogUtil;

public class NewsTable {
	public static final String TAG = "NewsTable";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;
	
	public static ArrayList<String> getMessagesByPositionId(int positionId) {
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

		ArrayList<String> messages = new ArrayList<String>();
		
		String sql = "SELECT message"
				+ " FROM news WHERE position_id=?";
		String sql2 = "SELECT title"
				+ " FROM position WHERE id=?";
		
		try {
			PreparedStatement statement = connection.prepareStatement(sql);		
			statement.setInt(1, positionId);
			ResultSet rs = statement.executeQuery();
						
			PreparedStatement statement2 = connection.prepareStatement(sql2);
			statement2.setInt(1, positionId);
			ResultSet rs2 = statement2.executeQuery();
			
			while (rs.next()) {
				String message = rs.getString(1);
				messages.add(message);
				LogUtil.getInstance().log(
						TAG + ", " + positionId + ", " + message);
			}
			
			if (!messages.isEmpty()) {
				while (rs2.next()) {
					String positionTitle = rs2.getString(1);
					
					if (positionTitle == null) {
						positionTitle = ""+positionId;
					} else {
						if (positionTitle.equalsIgnoreCase("null")) {
							positionTitle = ""+positionId;
						}
					}
					
					messages.add("Published By: " + positionTitle);
					LogUtil.getInstance().log(
							TAG + ", " + positionId + ", " + positionTitle);
				}
			}
			
			return messages;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
