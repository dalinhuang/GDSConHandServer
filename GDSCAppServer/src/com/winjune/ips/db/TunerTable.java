package com.winjune.ips.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.winjune.ips.model.types.Tuner;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

public class TunerTable {
	public static final String TAG = "TunerTable";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;
	
	public static Tuner getRecord(String name) {
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
		
		String sql = "SELECT DBM_LINEAR_AVERAGE, LOGGER_FILE, Database_Server, pilot_power_threshold, mse_threshold, mse_ref_threshold"
				+ " FROM tuner WHERE Name=?";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				String DBM_LINEAR_AVERAGE = rs.getString(1);
				String LOGGER_FILE = rs.getString(2);
				String Database_Server = rs.getString(3);
				float pilot_power_threshold = rs.getFloat(4);
				float mse_threshold = rs.getFloat(5);
				float mse_ref_threshold = rs.getFloat(6);
				
				Tuner tuner = new Tuner(name, DBM_LINEAR_AVERAGE, LOGGER_FILE, Database_Server, pilot_power_threshold, mse_threshold, mse_ref_threshold);
				
				return tuner;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public static boolean insertRecord(String name, String dBM_LINEAR_AVERAGE, String lOGGER_FILE,
			String database_Server, float pilot_power_threshold,
			float mse_threshold, float mse_ref_threshold) {
		
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
		
		String sql = "insert into tuner (Name, DBM_LINEAR_AVERAGE, LOGGER_FILE, Database_Server, pilot_power_threshold, mse_threshold, mse_ref_threshold) values (?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setString(2, dBM_LINEAR_AVERAGE);
			statement.setString(3, lOGGER_FILE);
			statement.setString(4, database_Server);
			statement.setFloat(5, pilot_power_threshold);
			statement.setFloat(6, mse_threshold);
			statement.setFloat(7, mse_ref_threshold);
			
			statement.executeUpdate();

			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean deleteRecord(String name) {
		
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
		
		String sql = "DELETE FROM tuner WHERE Name=?";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	public static boolean saveTunerWithHistory(Tuner tuner) {
		Tuner old = getRecord("CURRENT");
		
		if (old != null) {
			if (!insertRecord("HISTORY"+System.currentTimeMillis(), old.getDBM_LINEAR_AVERAGE(), old.getLOGGER_FILE(), old.getDatabase_Server(), old.getPilot_power_threshold(), old.getMse_threshold(), old.getMse_ref_threshold())) {
				return false;
			}
			if (!deleteRecord("CURRENT")){
				return false;
			}
		}
		
		if (!insertRecord("CURRENT", tuner.getDBM_LINEAR_AVERAGE(), tuner.getLOGGER_FILE(), tuner.getDatabase_Server(), tuner.getPilot_power_threshold(), tuner.getMse_threshold(), tuner.getMse_ref_threshold())) {
			return false;
		}
		
		return Tuner.syncToTuner(tuner);
	}
}
