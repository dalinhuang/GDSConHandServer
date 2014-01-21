package com.ericsson.cgc.aurora.wifiips.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiips.utils.LogUtil;

public class PositionAdTable {
	public static final String TAG = PositionAdTable.class.toString();
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	public static List<Integer> getAdByPosition(int positionId) {
		MysqlManager instance = MysqlManager.getInstance();
		if (instance == null) {
			LogUtil.getInstance().log(
					TAG + ", " + "MysqlManager instance is null.");
			return null;
		}

		Connection connection = instance.getConnection();
		if (connection == null) {
			LogUtil.getInstance().log(TAG + ", " + "connection is null.");
			return null;
		}

		List<Integer> ads = new ArrayList<Integer>();

		String this_sql = "SELECT ad_id FROM position_ad\n"
				+ "WHERE position_id = " + positionId;
		try {
			PreparedStatement statement = connection.prepareStatement(this_sql);
			//statement.setInt(1, positionId);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				int adId = rs.getInt(1);
				ads.add(adId);
			}

			if (ads.size() > 0)
				return ads;
			else
				return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.getInstance()
					.log(TAG + ", SQLException: " + e.getMessage());
		}

		return null;
	}
}
