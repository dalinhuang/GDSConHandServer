package com.winjune.ips.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.winjune.ips.model.algorithm.Querying;
import com.winjune.ips.model.types.InfoQueryRequest;
import com.winjune.ips.model.types.Location;
import com.winjune.ips.model.types.QueryInfo;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

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
		
		response.setContentType("application/json;charset=UTF-8");  // Handle Chinese
		response.setCharacterEncoding("UTF-8");		

		jsonObject.write(response.getWriter());

		LogUtil.getInstance().log(
				TAG + ", " + "Response sent: " + jsonObject.toString());

		LogUtil.getInstance()
				.log(TAG
						+ "*****************Incoming Message Query End******************");
	}

}
