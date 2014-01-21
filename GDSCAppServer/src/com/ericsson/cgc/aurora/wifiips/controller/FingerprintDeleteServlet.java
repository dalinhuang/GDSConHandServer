package com.ericsson.cgc.aurora.wifiips.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ericsson.cgc.aurora.wifiips.db.MapTable;
import com.ericsson.cgc.aurora.wifiips.model.algorithm.FingerprintDeleting;
import com.ericsson.cgc.aurora.wifiips.model.types.Location;
import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiips.utils.LogUtil;
import com.google.gson.Gson;

/**
 * Servlet implementation class NfcCollectServlet
 */
public class FingerprintDeleteServlet extends HttpServlet {
	public static final String TAG = "FingerprintDeleteServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FingerprintDeleteServlet() {
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

		Location location = gson.fromJson(reader, Location.class);

		if (location != null) {
			if (DEBUG) {
				LogUtil.getInstance()
						.log(TAG
								+ "*****************Incoming Message fingerprintDelete Begin******************");
				LogUtil.getInstance()
						.log(TAG
								+ ", "
								+ "Receive fingerprintDelete message with Location: ");
			
				LogUtil.getInstance().log(TAG + ", " + location.toString());
			}
		} else {
			LogUtil.getInstance().log(TAG + ", location is null!");
		}
		
		// Verify valid map version code
		int mapVersionCode = location.getMapVersion();
		int mapId = location.getMapId();
		
		if (!MapTable.verifyMapVersion(mapId, mapVersionCode)) {
			LogUtil.getInstance().log(TAG + ", Inconsistent map version!");
			return;
		}

		// delete
		FingerprintDeleting fingerprintDeletingInstance = FingerprintDeleting.getInstance();		
		if (fingerprintDeletingInstance == null) {
			LogUtil.getInstance().log(
					TAG + ", " + "fingerprintDeleting instance is null.");
			return;
		}

		fingerprintDeletingInstance.delete(location);
		
		LogUtil.getInstance()
		.log(TAG
				+ "*****************Incoming Message fingerprintDelete End******************");
	}

}
