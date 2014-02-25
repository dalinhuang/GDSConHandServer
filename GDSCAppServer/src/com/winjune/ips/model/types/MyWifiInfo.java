/**
 * @(#)WifiAp.java
 * Jun 4, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.ips.model.types;

import com.winjune.ips.utils.LogUtil;

/**
 * @author ezhipin, haleyshi
 * 
 */
public class MyWifiInfo {
	private String mac;
	private int dbm;

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
	
	public int getDbm() {
		return dbm;
	}

	public void setDbm(int dbm) {
		this.dbm = dbm;
	}
	
	public void logDebugData(String TAG) {
		LogUtil.getInstance().log(TAG + "\t" + mac + ", " + dbm);
	}
}
