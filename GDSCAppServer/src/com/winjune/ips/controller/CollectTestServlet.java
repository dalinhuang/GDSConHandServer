package com.winjune.ips.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.winjune.ips.db.MapTable;
import com.winjune.ips.db.PositionTable;
import com.winjune.ips.model.algorithm.Collecting;
import com.winjune.ips.model.algorithm.Locating;
import com.winjune.ips.model.types.Location;
import com.winjune.ips.model.types.LocationSet;
import com.winjune.ips.model.types.TestLocateCollectReply;
import com.winjune.ips.model.types.TestLocateCollectRequest;
import com.winjune.ips.model.types.WifiFingerPrint;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

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
			
			

				// collect this location when the map is valid
				Collecting.collect_this_location(TAG, inputLocation, wifiFingerPrint);
					
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
