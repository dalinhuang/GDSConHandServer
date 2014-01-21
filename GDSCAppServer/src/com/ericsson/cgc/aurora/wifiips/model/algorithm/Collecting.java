package com.ericsson.cgc.aurora.wifiips.model.algorithm;

import com.ericsson.cgc.aurora.wifiips.db.MysqlManager;
import com.ericsson.cgc.aurora.wifiips.db.PositionTable;
import com.ericsson.cgc.aurora.wifiips.model.types.Location;
import com.ericsson.cgc.aurora.wifiips.model.types.WifiFingerPrint;
import com.ericsson.cgc.aurora.wifiips.model.types.MyWifiInfo;
import com.ericsson.cgc.aurora.wifiips.model.types.WifiFingerPrintSample;
import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiips.utils.LogUtil;

public class Collecting {
	public static final String TAG = "Collecting";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static Collecting mSingletonInstance = null;

	private Collecting() {

	}

	public static Collecting getInstance() {
		if (mSingletonInstance == null) {
			mSingletonInstance = new Collecting();
			return mSingletonInstance;
		}

		return mSingletonInstance;
	}

	public static void collect_this_location(String inputTag, Location location, WifiFingerPrint wifiFingerPrint) {
		if (location == null) {
			LogUtil.getInstance().log(inputTag + ", Invalid Location data inside collectInfo.");
			return;
		} else if (DEBUG) {
			LogUtil.getInstance().log(inputTag + ", " + location.toString());
		}

		if (wifiFingerPrint == null) {
			LogUtil.getInstance().log(inputTag + ", Invalid WifiFingerPrint data inside collectInfo.");
			return;
		} else if (DEBUG) {
			wifiFingerPrint.logDebugData(inputTag);
		}
		
		int positionId = PositionTable.getPositionId(location);
		if (positionId < 0) {
			LogUtil.getInstance().log(TAG + ", *** SWERR *** invalid position ID query result for " + location);
			return;
		}

		String orig = "INSERT INTO fingerprint\n"
					+ "(Collecting_Time, PositionID, MacAddress, Power_Orig)\n"
					+ "VALUES\n";
		for (WifiFingerPrintSample this_sample : wifiFingerPrint.getSamples()) {
			for (MyWifiInfo wifi : this_sample.getNeighboringAPs()) {
				orig += "(NOW(), " + positionId + ", '" + wifi.getMac() + "', " + wifi.getDbm() +"),\n";
			}
		}

		// trim the last ",\n"
		orig = orig.substring(0, orig.length()-2);
		int rows = MysqlManager.executeInsertStatement(orig);
		if (rows>0) {
			MysqlManager.executeStoredProcedure("{call update_avg_mse_by_positionid(" + positionId + ")}");
		}
		LogUtil.getInstance().log(TAG + ": " + rows + " new fingerprint rows inserted.");
	}
}
