package com.ericsson.cgc.aurora.wifiips.model.types;

import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;

public class Tuner {
	
	private String name;
	private String DBM_LINEAR_AVERAGE;
	private String LOGGER_FILE;
	private String Database_Server;
	private float pilot_power_threshold;
	private float mse_threshold;
	private float mse_ref_threshold;

	public Tuner(String name, String dBM_LINEAR_AVERAGE, String lOGGER_FILE,
			String database_Server, float pilot_power_threshold,
			float mse_threshold, float mse_ref_threshold) {
		this.name = name;
		this.DBM_LINEAR_AVERAGE = dBM_LINEAR_AVERAGE;
		this.LOGGER_FILE = lOGGER_FILE;
		this.Database_Server = database_Server;
		this.pilot_power_threshold = pilot_power_threshold;
		this.mse_threshold = mse_threshold;
		this.mse_ref_threshold = mse_ref_threshold;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDBM_LINEAR_AVERAGE() {
		return DBM_LINEAR_AVERAGE;
	}

	public void setDBM_LINEAR_AVERAGE(String dBM_LINEAR_AVERAGE) {
		DBM_LINEAR_AVERAGE = dBM_LINEAR_AVERAGE;
	}

	public String getLOGGER_FILE() {
		return LOGGER_FILE;
	}

	public void setLOGGER_FILE(String lOGGER_FILE) {
		LOGGER_FILE = lOGGER_FILE;
	}

	public String getDatabase_Server() {
		return Database_Server;
	}

	public void setDatabase_Server(String database_Server) {
		Database_Server = database_Server;
	}

	public float getMse_ref_threshold() {
		return mse_ref_threshold;
	}

	public void setMse_ref_threshold(float mse_ref_threshold) {
		this.mse_ref_threshold = mse_ref_threshold;
	}

	public float getMse_threshold() {
		return mse_threshold;
	}

	public void setMse_threshold(float mse_threshold) {
		this.mse_threshold = mse_threshold;
	}

	public float getPilot_power_threshold() {
		return pilot_power_threshold;
	}

	public void setPilot_power_threshold(float pilot_power_threshold) {
		this.pilot_power_threshold = pilot_power_threshold;
	}
	
	public static boolean syncToTuner(Tuner tuner) {
		try {
			WifiIpsSettings.DBM_LINEAR_AVERAGE = Boolean.parseBoolean(tuner.getDBM_LINEAR_AVERAGE());
			WifiIpsSettings.LOGGER_FILE = tuner.getLOGGER_FILE();
			WifiIpsSettings.Database_Server = tuner.getDatabase_Server();
			WifiIpsSettings.pilot_power_threshold = tuner.getPilot_power_threshold();
			WifiIpsSettings.mse_threshold = tuner.getMse_threshold();
			WifiIpsSettings.mse_ref_threshold = tuner.getMse_ref_threshold();
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}

}
