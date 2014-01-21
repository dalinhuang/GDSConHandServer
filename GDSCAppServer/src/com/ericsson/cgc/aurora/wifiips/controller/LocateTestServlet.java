package com.ericsson.cgc.aurora.wifiips.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.ericsson.cgc.aurora.wifiips.db.MapTable;
import com.ericsson.cgc.aurora.wifiips.db.PositionTable;
import com.ericsson.cgc.aurora.wifiips.model.algorithm.Collecting;
import com.ericsson.cgc.aurora.wifiips.model.algorithm.Locating;
import com.ericsson.cgc.aurora.wifiips.model.types.Location;
import com.ericsson.cgc.aurora.wifiips.model.types.LocationSet;
import com.ericsson.cgc.aurora.wifiips.model.types.TestLocateCollectReply;
import com.ericsson.cgc.aurora.wifiips.model.types.TestLocateCollectRequest;
import com.ericsson.cgc.aurora.wifiips.model.types.WifiFingerPrint;
import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiips.utils.LogUtil;
import com.google.gson.Gson;

/**
 * Servlet implementation class LocateTestServlet
 */
public class LocateTestServlet extends HttpServlet {
	public static final String TAG = "LocateTestServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LocateTestServlet() {
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
		
		TestLocateCollectRequest testPosition = gson.fromJson(reader, TestLocateCollectRequest.class);

		if (testPosition != null) {
			Location inputLocation = testPosition.getLocation();
			WifiFingerPrint wifiFingerPrint = testPosition.getFignerPrint();
			long receiveTimestamp = System.currentTimeMillis();			
			
			LogUtil.getInstance().log(TAG + "*****************Incoming Message LocateTest Begin******************");
			LogUtil.getInstance().log(TAG + ", Receive LocateTest message with Location & WifiFingerPrint.");
				
			if (inputLocation != null) {	
				LogUtil.getInstance().log(TAG + ", " + inputLocation.toString());
			} else {
				LogUtil.getInstance().log(TAG + ", " + "No Location is found.");
				return;
			}

			int reTest = 0; // No need to reTest

			// Verify valid map version code
			int mapVersionCode = inputLocation.getMapVersion();
			int mapId = inputLocation.getMapId();
			if (!MapTable.verifyMapVersion(mapId, mapVersionCode)) {
				LogUtil.getInstance().log(TAG + ", Inconsistent map version!");
				// Not return here, but require reTest
				reTest = 1;
			} else {
				// insert only when the map version is valid
				// if there is no data for incoming position, insert one.
				if (!PositionTable.this_position_has_data(inputLocation)) {
					LogUtil.getInstance().log(TAG + ": Test on new position detected, insert input data as fingerprint.");
					Collecting.collect_this_location(TAG, inputLocation, wifiFingerPrint);
					reTest = 1;
				}
			}
			
			// locate test now.
			LocationSet outputLocations = Locating.locate_me(TAG, wifiFingerPrint, 1, reTest, inputLocation);
			TestLocateCollectReply testLocation = new TestLocateCollectReply(inputLocation, outputLocations, 
					testPosition.getTimestamp(), receiveTimestamp, System.currentTimeMillis(), reTest);
			JSONObject jsonObject = new JSONObject(gson.toJson(testLocation));
			jsonObject.write(response.getWriter());

			LogUtil.getInstance().log(TAG + ", Response sent: " + jsonObject.toString());
			LogUtil.getInstance().log(TAG + "*****************Incoming Message LocateTest End******************");
		} else {
			LogUtil.getInstance().log(TAG + ", Receive locate message with no testPosition.");
			return;
		}
	}

}
