package com.ericsson.cgc.aurora.wifiips.settings;

public class WifiIpsSettings {
	public static boolean DEBUG = true;
	public static boolean DBM_LINEAR_AVERAGE = true;
	public static String LOGGER_FILE = "C:\\Users\\eshouwu\\Projects\\Aurora Project\\PoC\\IPSWifiIPSServer.log"; //"\\\\ev2c4138b15c2b\\A_Paddy\\newdev\\wifiipsserver.log";
	public static String Database_Server = "localhost";
	public static float pilot_power_threshold = 15.0f;
	public static float mse_threshold = 1.0f; // 0.01 is too small???
	public static float mse_ref_threshold = 30.0f;
	public static final boolean RUNNING_IN_LINUX = false; 
	public static String LINUX_LOGGER_FILE = "/home/plm/wifiipsserver.log";
	public static final String LINUX_SERVER_DB_USER_NAME = "root";
	public static final String LINUX_SERVER_DB_USER_PASSWORD = "root";
	public static String LINUX_SERVER_DB_URL = "jdbc:mysql://localhost:3306/wips";
	public static final String DB_USER_NAME = "root";
	public static final String DB_USER_PASSWORD = "root";
	public static String DB_URL = "jdbc:mysql://" + WifiIpsSettings.Database_Server + "/wips";
	
}
