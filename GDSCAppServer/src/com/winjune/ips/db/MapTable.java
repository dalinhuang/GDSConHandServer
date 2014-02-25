package com.winjune.ips.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

public class MapTable {
	public static final String TAG = "MapTable";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	public static boolean verifyMapVersion(int mapId, int versionCode) {
		int version = getCurrentMapVersion(mapId);
		
		if ((versionCode == -1) || (versionCode != version)) {
			return false;
		}
		
		return true;
	}	
	
	public static int getCurrentMapVersion(int mapId) {
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
		
		String sql = "SELECT version_code"
				+ " FROM map WHERE map_id=?";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, mapId);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return -1;
	}
	
}
