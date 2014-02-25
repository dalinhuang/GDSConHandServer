package com.winjune.ips.model.algorithm;

import com.winjune.ips.db.FineLocationTable;
import com.winjune.ips.model.types.NfcFingerPrint;
import com.winjune.ips.settings.WifiIpsSettings;

public class NfcLocating {
	public static final String TAG = "NfcLocating";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static NfcLocating mSingletonInstance = null;

	private NfcLocating() {

	}

	public static NfcLocating getInstance() {
		if (mSingletonInstance == null) {
			mSingletonInstance = new NfcLocating();
			return mSingletonInstance;
		}

		return mSingletonInstance;
	}
	
	public int locate(NfcFingerPrint nfcFingerPrint) {
		String tagId = nfcFingerPrint.getTagId().trim();
		
		return FineLocationTable.getPositionId(tagId);
	}
}
