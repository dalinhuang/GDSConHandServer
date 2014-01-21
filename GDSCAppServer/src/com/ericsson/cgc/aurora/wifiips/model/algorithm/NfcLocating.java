package com.ericsson.cgc.aurora.wifiips.model.algorithm;

import com.ericsson.cgc.aurora.wifiips.db.FineLocationTable;
import com.ericsson.cgc.aurora.wifiips.model.types.NfcFingerPrint;
import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;

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
