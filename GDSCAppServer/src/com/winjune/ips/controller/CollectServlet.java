package com.winjune.ips.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.winjune.ips.db.MapTable;
import com.winjune.ips.model.algorithm.Collecting;
import com.winjune.ips.model.types.CollectInfo;
import com.winjune.ips.model.types.Location;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

/**
 * Servlet implementation class CollectServlet
 */
public class CollectServlet extends HttpServlet {
	public static final String TAG = "CollectServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CollectServlet() {
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

		CollectInfo collectInfo = gson.fromJson(reader, CollectInfo.class);

		if (collectInfo != null) {
			if (DEBUG) {
				LogUtil.getInstance().log(TAG + "*****************Incoming Message Collect Begin******************");
				LogUtil.getInstance().log(TAG + ", Receive collect message with Location and WifiFingerPrint: ");
			}
			
			Location location = collectInfo.getLocation();
			
			// Verify valid map version code
			int mapVersionCode = location.getMapVersion();
			int mapId = location.getMapId();
			
			if (!MapTable.verifyMapVersion(mapId, mapVersionCode)) {
				LogUtil.getInstance().log(TAG + ", Inconsistent map version!");
				return;
			}

			Collecting.collect_this_location(TAG, location, collectInfo.getWifiFingerPrint());
			
			if (DEBUG) {
				LogUtil.getInstance().log(TAG + "*****************Incoming Message Collect End******************");
			}
		} else if (DEBUG) {
			LogUtil.getInstance().log(TAG + ", *** SWERR *** collectInfo is null");
		}
	}
}
