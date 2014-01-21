package com.ericsson.cgc.aurora.wifiips.model.algorithm;

import java.util.ArrayList;

import com.ericsson.cgc.aurora.wifiips.db.TestRecordTable;
import com.ericsson.cgc.aurora.wifiips.db.TracesTable;
import com.ericsson.cgc.aurora.wifiips.db.ApkUsersTable;
import com.ericsson.cgc.aurora.wifiips.model.types.Location;
import com.ericsson.cgc.aurora.wifiips.model.types.TraceRecord;
import com.ericsson.cgc.aurora.wifiips.model.types.User;
import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;

public class Tracing {
	public static final String TAG = "Tracing";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static Tracing mSingletonInstance = null;

	private Tracing() {

	}

	public static Tracing getInstance() {
		if (mSingletonInstance == null) {
			mSingletonInstance = new Tracing();
			return mSingletonInstance;
		}

		return mSingletonInstance;
	}

	public ArrayList<TraceRecord> tracePerLoginName(String loginName) {
		// TODO: get user's deviceMacAddress based on loginName, but here just get the first one 
		User user = ApkUsersTable.getRecordByLoginName(loginName);
		if (user != null) {
			String deviceMacAddress = user.getDeviceMacAddress();
			
			ArrayList<TraceRecord> recordList = TracesTable.queryTraces(deviceMacAddress);
			
			return recordList;
		}
		
		return null;
	}
	
	public ArrayList<TraceRecord> tracePerMac(String deviceMacAddress) {
		if (deviceMacAddress == null) {
			return null;
		}
			
		return TracesTable.queryTraces(deviceMacAddress);
	}
	
	public boolean saveOrUpdateUser(String deviceMacAddress, String deviceName, String loginName) {
		User user = ApkUsersTable.getRecord(deviceMacAddress, loginName);
		if (user != null) {
			if (!deviceName.equals(user.getDeviceName()))
				return ApkUsersTable.updateRecord(deviceName, user.getId());
		} else
			return ApkUsersTable.insertRecord(deviceMacAddress, deviceName, loginName);
		
		return false;
	}
	
	public boolean saveTrace(String deviceMacAddress, int positionId, String fromSignal) {
		return TracesTable.insertRecord(deviceMacAddress, positionId, fromSignal);
	}
	
	public boolean saveTestRecord(String deviceMacAddress, Location location1, Location location2, String from) {
		return TestRecordTable.insertRecord(deviceMacAddress, location1, location2, from);
	}
	
}
