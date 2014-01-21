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
 * Servlet implementation class CollectTestServlet
 */
public class CollectTestServlet extends HttpServlet {
	public static final String TAG = "CollectTestServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CollectTestServlet() {
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
			
			LogUtil.getInstance().log(TAG + "*****************Incoming Message CollectTest Begin******************");
			LogUtil.getInstance().log(TAG + ", Receive CollectTest message with Location & WifiFingerPrint.");
			
			int reTest = 0; // No need to reTest

			if (!PositionTable.this_position_has_data(inputLocation)) {
				LogUtil.getInstance().log(TAG + ": Test on new position detected, insert input data as fingerprint.");
				reTest = 1;
			}
			
			
			// Verify valid map version code
			int mapVersionCode = inputLocation.getMapVersion();
			int mapId = inputLocation.getMapId();
			if (!MapTable.verifyMapVersion(mapId, mapVersionCode)) {
				LogUtil.getInstance().log(TAG + ", Inconsistent map version!");
				// Not return here, but require reTest
				reTest = 1;
			} else {
				// collect this location when the map is valid
				Collecting.collect_this_location(TAG, inputLocation, wifiFingerPrint);
			}
					
			// locate test now.
			LocationSet outputLocations = Locating.locate_me(TAG, wifiFingerPrint, 2, reTest, inputLocation);
			TestLocateCollectReply testLocation = new TestLocateCollectReply(inputLocation, outputLocations, 
					testPosition.getTimestamp(), receiveTimestamp, System.currentTimeMillis(), reTest);
			JSONObject jsonObject = new JSONObject(gson.toJson(testLocation));
			jsonObject.write(response.getWriter());

			LogUtil.getInstance().log(TAG + ", Response sent: " + jsonObject.toString());
			LogUtil.getInstance().log(TAG + "*****************Incoming Message CollectTest End******************");			
		} else {
			LogUtil.getInstance().log(TAG + ", Receive locate message with no testPosition.");
			return;
		}
	}

}
