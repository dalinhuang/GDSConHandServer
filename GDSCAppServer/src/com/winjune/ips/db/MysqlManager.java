package com.winjune.ips.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

public class MysqlManager {
	public static final String TAG = "MysqlManager";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private final String mDriverName = "com.mysql.jdbc.Driver";

	private String mUrl;
	private String mUser;
	private String mPassword;

	private static MysqlManager mSingletonInstance = null;
	private static Statement st;
	private static CallableStatement proc;
	private static Connection mConnection = null;

	private MysqlManager() {
		if (WifiIpsSettings.RUNNING_IN_LINUX) {
			mUrl = WifiIpsSettings.LINUX_SERVER_DB_URL;
			mUser = WifiIpsSettings.LINUX_SERVER_DB_USER_NAME;
			mPassword = WifiIpsSettings.LINUX_SERVER_DB_USER_PASSWORD;
		} else {
			mUrl = WifiIpsSettings.DB_URL;
			mUser = WifiIpsSettings.DB_USER_NAME;
			mPassword = WifiIpsSettings.DB_USER_PASSWORD;
		}
	}

	public static MysqlManager getInstance() {
		if (mSingletonInstance == null) {
			mSingletonInstance = new MysqlManager();
		}

		return mSingletonInstance;
	}
	
	public static int executeInsertStatement (String this_sql) {
		try {
			return st.executeUpdate(this_sql);
		} catch (SQLException e) {
			e.printStackTrace();
			LogUtil.getInstance().log(TAG + ", SQLException: " + e.getMessage());
			return 0;
		}
	}
	
	public static void executeStoredProcedure (String this_sql) {
		try {
			proc = mConnection.prepareCall(this_sql);
			proc.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.getInstance().log(TAG + ", SQLException: " + e.getMessage());
		}
	}

	private boolean createConnection() {
		if (mConnection != null) {
			try {
				if (!mConnection.isClosed())
					mConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LogUtil.getInstance().log(TAG + ", SQLException: " + e.getMessage());
				return false;
			}
		}

		try {
			Class.forName(mDriverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			LogUtil.getInstance().log(TAG + ", SQLException: " + e.getMessage());
			return false;
		}

		try {
			//mConnection = DriverManager.getConnection(mUrl, mUser, mPassword);
			Properties info = new Properties();
			info.setProperty("user", mUser);
			info.setProperty("password", mPassword);
			//info.setProperty("useUnicode", "true");
			info.setProperty("characterEncoding", "utf-8");
			mConnection = DriverManager.getConnection(mUrl, info);
			
			st = mConnection.createStatement();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.getInstance().log(TAG + ", SQLException: " + e.getMessage());
		}

		return false;
	}

	public Connection getConnection() {
		try {
			if ((mConnection != null) && !mConnection.isClosed())
				return mConnection;
			else {
				boolean ret = createConnection();
				if (ret)
					return mConnection;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.getInstance().log(TAG + ", SQLException: " + e.getMessage());
		}

		return null;
	}

	public void closeConnection() {
		if (mConnection != null) {
			try {
				if (!mConnection.isClosed()) {
					mConnection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LogUtil.getInstance().log(TAG + ", SQLException: " + e.getMessage());
			}
		}
	}
}
