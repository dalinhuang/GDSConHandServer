package com.winjune.ips.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.winjune.ips.model.types.User;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

public class ApkUsersTable {
	public static final String TAG = "ApkUsersTable";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;
	
	public static User getRecord(String deviceMacAddress, String loginName) {
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
		
		String sql = "SELECT ID, deviceName"
				+ " FROM apk_users WHERE loginName=? AND deviceMac=?";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, loginName);
			statement.setString(2, deviceMacAddress);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				int id = rs.getInt(1);
				String deviceName = rs.getString(2);
				
				User user = new User(id, deviceMacAddress, deviceName, loginName);
				
				return user;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public static boolean insertRecord(String deviceMacAddress, String deviceName, String loginName) {
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
		
		String sql = "insert into apk_users (loginName, deviceMac, deviceName) values (?, ?, ?)";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, loginName);
			statement.setString(2, deviceMacAddress);
			statement.setString(3, deviceName);
			
			statement.executeUpdate();
			LogUtil.getInstance().log(TAG + ", Insert user successful.");
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.getInstance().log(TAG + ", SQLException: " + e.getMessage());
		}
		
		return false;
	}
	
	public static boolean updateRecord(String deviceName, int id) {
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
		
		String sql = "update apk_users set deviceName=? where ID=?";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, deviceName);
			statement.setInt(2, id);
			
			statement.executeUpdate();
			LogUtil.getInstance().log(TAG + ", Update user successful.");
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.getInstance().log(TAG + ", SQLException: " + e.getMessage());
		}

		return false;
	}
	
	public static User getRecordByLoginName(String loginName) {
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
		
		String sql = "SELECT ID, deviceMac, deviceName"
				+ " FROM apk_users WHERE loginName=?";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, loginName);
			ResultSet rs = statement.executeQuery();
			
			//TODO: but here just get the first one
			if (rs.next()) {
				int id = rs.getInt(1);
				String deviceMacAddress = rs.getString(2);
				String deviceName = rs.getString(3);
				
				User user = new User(id, deviceMacAddress, deviceName, loginName);
				
				return user;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public static ArrayList<User> queryUsers() {
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
		
		
		ArrayList<User> userList = new ArrayList<User>();
		
		String sql = "SELECT ID, loginName, deviceMac, deviceName FROM apk_users";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String loginName = rs.getString(2);
				String deviceMac = rs.getString(3);
				String deviceName = rs.getString(4);
				
				User user = new User(id, deviceMac, deviceName, loginName);
				
				userList.add(user);
			}
			
			return userList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
