package com.winjune.ips.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.winjune.ips.db.PositionTable;
import com.winjune.ips.model.algorithm.NfcLocating;
import com.winjune.ips.model.algorithm.Tracing;
import com.winjune.ips.model.types.Location;
import com.winjune.ips.model.types.NfcFingerPrint;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

/**
 * Servlet implementation class LocateServlet
 */
public class NfcLocateServlet extends HttpServlet {
	public static final String TAG = "nfcLocateServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NfcLocateServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		BufferedReader reader = request.getReader();

		Gson gson = new Gson();

		NfcFingerPrint nfcFingerPrint = gson.fromJson(reader,
				NfcFingerPrint.class);

		if ((nfcFingerPrint != null) && DEBUG) {
			LogUtil.getInstance().log(TAG + "*****************Incoming Message nfcLocate Begin******************");
			LogUtil.getInstance().log(TAG + ", Receive nfcLocate message with NfcFingerPrint.");
			LogUtil.getInstance().log(TAG + ", NFC tag id is " + nfcFingerPrint.getTagId());
		} else {
			LogUtil.getInstance().log(TAG + ", Receive nfcLocate message with no NfcFignerPrint.");
			return;
		}

		Location location = null;
		
		// locate
		NfcLocating nfcLocatingInstance = NfcLocating.getInstance();
		if (nfcLocatingInstance == null) {
			LogUtil.getInstance().log(TAG + ", " + "NfcLocating instance is null.");
			return;
		}

		int positionId = nfcLocatingInstance.locate(nfcFingerPrint);
		if (positionId < 0) {
			if (DEBUG)
				LogUtil.getInstance().log(TAG + ", can not find position. <<< positionID = " + positionId);
			location = new Location(-1, -1, -1, -1);
		} else {
			location = PositionTable.getPositionById(positionId);
			if (location == null) {
				LogUtil.getInstance().log(TAG + ", *** SWERR *** Failed when NEW a location, it should not happen!");
				return;
			}
		}

		String locationString = gson.toJson(location);

		JSONObject jsonObject = new JSONObject(locationString);

		jsonObject.write(response.getWriter());

		LogUtil.getInstance().log(TAG + ", Response sent: " + jsonObject.toString());
		
		// user trace
		Tracing tracingInstance = Tracing.getInstance();
		if (tracingInstance == null) {
			LogUtil.getInstance().log(TAG + ", " + "Tracing instance is null.");
			return;
		}

		tracingInstance.saveOrUpdateUser(nfcFingerPrint.getMyWifiMac(), nfcFingerPrint.getDeviceName(), nfcFingerPrint.getAccountName());
		tracingInstance.saveTrace(nfcFingerPrint.getMyWifiMac(), positionId, "Fine location from nfc or 2D Code");

		LogUtil.getInstance().log(TAG + "*****************Incoming Message nfcLocate End******************");
	}

}
