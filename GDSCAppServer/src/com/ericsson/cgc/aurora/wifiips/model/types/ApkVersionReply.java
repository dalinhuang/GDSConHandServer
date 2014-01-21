package com.ericsson.cgc.aurora.wifiips.model.types;

public class ApkVersionReply {
	private int versionCode;
	private String versionName;
	private String apkUrl;
	
	public ApkVersionReply(int versionCode, String versionName, String apkUrl) {
		setVersionCode(versionCode);
		setVersionName(versionName);
		setApkUrl(apkUrl);
	}

	public int getVersionCode() {
		return versionCode;
	}
	
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	
	public String getVersionName() {
		return versionName;
	}
	
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
}
