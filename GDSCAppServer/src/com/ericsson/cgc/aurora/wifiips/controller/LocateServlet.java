package com.ericsson.cgc.aurora.wifiips.controller;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import com.ericsson.cgc.aurora.wifiips.model.algorithm.Locating;
import com.ericsson.cgc.aurora.wifiips.model.types.WifiFingerPrint;
import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiips.utils.LogUtil;
import com.google.gson.Gson;

/**
 * Servlet implementation class LocateServlet
 */
public class LocateServlet extends HttpServlet {
	public static final String TAG = "LocateServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LocateServlet() {
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
		if (DEBUG) {
			LogUtil.getInstance().log(TAG + "*****************Incoming Message Locate Begin******************");
			LogUtil.getInstance().log(TAG + ", Receive locate message with WifiFingerPrint.");
		}
		Gson gson = new Gson();
		String locationsString = gson.toJson(Locating.locate_me(TAG, gson.fromJson(reader,	WifiFingerPrint.class), 0, 0, null));
		JSONObject jsonObject = new JSONObject(locationsString);

		jsonObject.write(response.getWriter());
		if (DEBUG) {
			LogUtil.getInstance().log(TAG + ", Response sent: " + jsonObject.toString());
			LogUtil.getInstance().log(TAG + "*****************Incoming Message Locate End******************");			
		}
	}
}
