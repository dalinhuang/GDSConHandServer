/**
 * @(#)Cell.java
 * Jun 4, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.ericsson.cgc.aurora.wifiips.model.types;

/**
 * @author ezhipin
 * 
 */
public class MobileCell {
	private String mode;
	private int lac;
	private int cid;
	private int psc;
	private int systemId;
	private int networkId;
	private int baseStationId;
	private int baseStationLatitude;
	private int baseStationLongitude;
	private int networkType;
	private String imsi;
	private String msisdn;
	private String operator;
	private String operatorName;
	private String device;
	private String deviceSoftwareVersion;

	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * @return the lac
	 */
	public int getLac() {
		return lac;
	}

	/**
	 * @param lac
	 *            the lac to set
	 */
	public void setLac(int lac) {
		this.lac = lac;
	}

	/**
	 * @return the cid
	 */
	public int getCid() {
		return cid;
	}

	/**
	 * @param cid
	 *            the cid to set
	 */
	public void setCid(int cid) {
		this.cid = cid;
	}

	/**
	 * @return the psc
	 */
	public int getPsc() {
		return psc;
	}

	/**
	 * @param psc
	 *            the psc to set
	 */
	public void setPsc(int psc) {
		this.psc = psc;
	}

	/**
	 * @return the systemId
	 */
	public int getSystemId() {
		return systemId;
	}

	/**
	 * @param systemId
	 *            the systemId to set
	 */
	public void setSystemId(int systemId) {
		this.systemId = systemId;
	}

	/**
	 * @return the networkId
	 */
	public int getNetworkId() {
		return networkId;
	}

	/**
	 * @param networkId
	 *            the networkId to set
	 */
	public void setNetworkId(int networkId) {
		this.networkId = networkId;
	}

	/**
	 * @return the baseStationId
	 */
	public int getBaseStationId() {
		return baseStationId;
	}

	/**
	 * @param baseStationId
	 *            the baseStationId to set
	 */
	public void setBaseStationId(int baseStationId) {
		this.baseStationId = baseStationId;
	}

	/**
	 * @return the baseStationLatitude
	 */
	public int getBaseStationLatitude() {
		return baseStationLatitude;
	}

	/**
	 * @param baseStationLatitude
	 *            the baseStationLatitude to set
	 */
	public void setBaseStationLatitude(int baseStationLatitude) {
		this.baseStationLatitude = baseStationLatitude;
	}

	/**
	 * @return the baseStationLongitude
	 */
	public int getBaseStationLongitude() {
		return baseStationLongitude;
	}

	/**
	 * @param baseStationLongitude
	 *            the baseStationLongitude to set
	 */
	public void setBaseStationLongitude(int baseStationLongitude) {
		this.baseStationLongitude = baseStationLongitude;
	}

	/**
	 * @return the networkType
	 */
	public int getNetworkType() {
		return networkType;
	}

	/**
	 * @param networkType
	 *            the networkType to set
	 */
	public void setNetworkType(int networkType) {
		this.networkType = networkType;
	}

	/**
	 * @return the imsi
	 */
	public String getImsi() {
		return imsi;
	}

	/**
	 * @param imsi
	 *            the imsi to set
	 */
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	/**
	 * @return the msisdn
	 */
	public String getMsisdn() {
		return msisdn;
	}

	/**
	 * @param msisdn
	 *            the msisdn to set
	 */
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @return the operatorName
	 */
	public String getOperatorName() {
		return operatorName;
	}

	/**
	 * @param operatorName
	 *            the operatorName to set
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	/**
	 * @return the device
	 */
	public String getDevice() {
		return device;
	}

	/**
	 * @param device
	 *            the device to set
	 */
	public void setDevice(String device) {
		this.device = device;
	}

	/**
	 * @return the deviceSoftwareVersion
	 */
	public String getDeviceSoftwareVersion() {
		return deviceSoftwareVersion;
	}

	/**
	 * @param deviceSoftwareVersion
	 *            the deviceSoftwareVersion to set
	 */
	public void setDeviceSoftwareVersion(String deviceSoftwareVersion) {
		this.deviceSoftwareVersion = deviceSoftwareVersion;
	}
}
