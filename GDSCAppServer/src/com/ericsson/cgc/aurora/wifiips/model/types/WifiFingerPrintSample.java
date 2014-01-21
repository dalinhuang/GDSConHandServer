package com.ericsson.cgc.aurora.wifiips.model.types;

import java.util.ArrayList;

import com.ericsson.cgc.aurora.wifiips.utils.LogUtil;

public class WifiFingerPrintSample {
	private ArrayList<MyWifiInfo> neighboringAPs;
	private long birthday;

	public ArrayList<MyWifiInfo> getNeighboringAPs() {
		return neighboringAPs;
	}
	
	public void setNeighboringAPs(ArrayList<MyWifiInfo> neighboringAPs) {
		this.neighboringAPs = neighboringAPs;
	}

	public long getBirthday() {
		return birthday;
	}

	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}
	
	public void logDebugData(String TAG) {
		LogUtil.getInstance().log(TAG + ", Collected sample@" + birthday);
		for (MyWifiInfo wifi : neighboringAPs) {
			wifi.logDebugData(TAG);
		}
	}
}
