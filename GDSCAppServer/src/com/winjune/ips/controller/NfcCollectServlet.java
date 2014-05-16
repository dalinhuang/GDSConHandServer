package com.winjune.ips.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.winjune.ips.db.MapTable;
import com.winjune.ips.model.algorithm.NfcCollecting;
import com.winjune.ips.model.types.Location;
import com.winjune.ips.model.types.NfcLocation;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

/**
 * Servlet implementation class NfcCollectServlet
 */
public class NfcCollectServlet extends HttpServlet {
	public static final String TAG = "NfcCollectServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NfcCollectServlet() {
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

		NfcLocation nfcLocation = gson.fromJson(reader, NfcLocation.class);

		if (nfcLocation != null) {
			if (DEBUG) {
				LogUtil.getInstance()
						.log(TAG
								+ "*****************Incoming Message nfcCollect Begin******************");
				LogUtil.getInstance()
						.log(TAG
								+ ", "
								+ "Receive nfcCollect message with Location: ");
			}

			Location location = nfcLocation.getLocation();
			if (location != null) {
				LogUtil.getInstance().log(TAG + ", " + nfcLocation.getTagId() + " - " + location.toString());
				
			} else {
				LogUtil.getInstance().log(TAG + ", Location is null!");
				return;
			}
		} else {
			LogUtil.getInstance().log(TAG + ", *****Incoming Message nfcCollect is NULL!******");
			return;
		}

		// collect
		NfcCollecting nfcCollectingInstance = NfcCollecting.getInstance();
		if (nfcCollectingInstance == null) {
			LogUtil.getInstance().log(
					TAG + ", " + "Collecting instance is null.");
			return;
		}

		nfcCollectingInstance.collect(nfcLocation);
		
		LogUtil.getInstance()
		.log(TAG
				+ "*****************Incoming Message nfcCollect End******************");
	}

}
