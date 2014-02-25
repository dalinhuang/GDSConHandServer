package com.winjune.ips.model.algorithm;

import com.winjune.ips.db.FineLocationTable;
import com.winjune.ips.model.types.Location;
import com.winjune.ips.model.types.NfcLocation;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

public class NfcCollecting {
	public static final String TAG = "NfcCollecting";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static NfcCollecting mSingletonInstance = null;

	private NfcCollecting() {

	}

	public static NfcCollecting getInstance() {
		if (mSingletonInstance == null) {
			mSingletonInstance = new NfcCollecting();
			return mSingletonInstance;
		}

		return mSingletonInstance;
	}
	
	public void collect(NfcLocation nfcLocation) {
		if (nfcLocation == null) {
			LogUtil.getInstance().log(TAG + ", nfcLocation is null.");
			return;
		}
		
		String tagId = nfcLocation.getTagId().trim();
		Location location = nfcLocation.getLocation();
		
		if (FineLocationTable.getPositionId(tagId) < 0) {
			// means new record, insert
			LogUtil.getInstance().log(TAG + ", " + "can not find position for RFID tag " + tagId + ", INSERT!");
			FineLocationTable.insertRecord(tagId, location);
		} else {
			// means old record, update
			LogUtil.getInstance().log(TAG + ", " + "find out position for RFID tag " + tagId + ", UPDATE!");
			FineLocationTable.updateRecord(tagId, location);
		}
	}
}
