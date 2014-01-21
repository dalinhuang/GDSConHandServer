package com.ericsson.cgc.aurora.wifiips.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.ericsson.cgc.aurora.wifiips.model.algorithm.Querying;
import com.ericsson.cgc.aurora.wifiips.model.types.InfoQueryRequest;
import com.ericsson.cgc.aurora.wifiips.model.types.Location;
import com.ericsson.cgc.aurora.wifiips.model.types.QueryInfo;
import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiips.utils.LogUtil;
import com.google.gson.Gson;

/**
 * Servlet implementation class QueryServlet
 */
public class QueryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final String TAG = "QueryServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QueryServlet() {
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
		if (DEBUG) {
			LogUtil.getInstance()
					.log(TAG
							+ "*****************Incoming Message Query Begin******************");
		}

		BufferedReader reader = request.getReader();

		Gson gson = new Gson();

		InfoQueryRequest infoQueryRequest = gson.fromJson(reader,
				InfoQueryRequest.class);
		if (infoQueryRequest == null) {
			if (DEBUG)
				LogUtil.getInstance().log(
						TAG + ", " + "info query request is null");
			return;
		}

		if (DEBUG) {
			ArrayList<Location> locations = infoQueryRequest.getLocations();
			StringBuffer locationsString = new StringBuffer();
			for (Location location : locations) {
				locationsString.append(location.toString() + " .");
			}
			LogUtil.getInstance().log(TAG + ", " + locationsString.toString());
		}

		Querying queryingInstance = Querying.getInstance();

		if (queryingInstance == null) {
			LogUtil.getInstance()
					.log(TAG + ", " + "Querying instance is null.");
			return;
		}

		QueryInfo queryInfo = queryingInstance.query(infoQueryRequest);

		String queryInfoString = gson.toJson(queryInfo);

		JSONObject jsonObject = new JSONObject(queryInfoString);

		jsonObject.write(response.getWriter());

		LogUtil.getInstance().log(
				TAG + ", " + "Response sent: " + jsonObject.toString());

		LogUtil.getInstance()
				.log(TAG
						+ "*****************Incoming Message Query End******************");
	}

}
