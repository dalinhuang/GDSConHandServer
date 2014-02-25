package com.winjune.ips.model.types;

import java.util.ArrayList;

import com.winjune.ips.utils.LogUtil;

public class WifiFingerPrint {
	private String accountName;
	private String myWifiMac;
	private String deviceName;
	private ArrayList<WifiFingerPrintSample> samples;

	public String getMyWifiMac() {
		return myWifiMac;
	}

	public void setMyWifiMac(String myWifiMac) {
		this.myWifiMac = myWifiMac;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public ArrayList<WifiFingerPrintSample> getSamples() {
		return samples;
	}

	public void setSamples(ArrayList<WifiFingerPrintSample> samples) {
		this.samples = samples;
	}
	
	public void logDebugData(String TAG) {
		if (samples.size() > 0) {
			for (WifiFingerPrintSample this_sample : samples) {
				this_sample.logDebugData(TAG);
			}
		} else {
			LogUtil.getInstance().log(TAG + ", " + "No AP sample is found.");
		}
	}
}