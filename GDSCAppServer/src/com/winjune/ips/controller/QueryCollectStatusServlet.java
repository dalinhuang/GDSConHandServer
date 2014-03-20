package com.winjune.ips.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.winjune.ips.model.types.CollectStatusReply;
import com.winjune.ips.model.types.VersionOrMapIdRequest;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

public class QueryCollectStatusServlet extends HttpServlet {

	public static final String TAG = "QueryCollectStatusServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QueryCollectStatusServlet() {
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
		
		BufferedReader reader = request.getReader();

		Gson gson = new Gson();

		VersionOrMapIdRequest code = gson.fromJson(reader, VersionOrMapIdRequest.class);

		if (code != null) {
			if (DEBUG) {
				LogUtil.getInstance().log(TAG + "*****************Incoming Message QueryCollectStatus Begin******************");
				LogUtil.getInstance().log(TAG + ", Receive QueryCollectStatus message with MapId: " + code);
			}

			try {
				
				int mapId = code.getCode();
				
				CollectStatusReply csr = new CollectStatusReply();
				
				csr.queryCollectStatus(mapId);
				
				String infoString = gson.toJson(csr);
				JSONObject jsonObject = new JSONObject(infoString);				
				LogUtil.getInstance().log(TAG + "  JSON " + new String(jsonObject.toString().getBytes("utf-8")));				

				response.setContentType("application/json;charset=UTF-8");  // Handle Chinese
				response.setCharacterEncoding("UTF-8");
				jsonObject.write(response.getWriter());

			} catch (Exception e) {
				e.printStackTrace();
				
				if (DEBUG) {
					LogUtil.getInstance().log(TAG + ", *** SWERR *** Refer to exception traceback");
				}
			}
			
			if (DEBUG) {
				LogUtil.getInstance().log(TAG + "*****************Incoming Message QueryCollectStatusInfo End******************");
			}
		} else if (DEBUG) {
			LogUtil.getInstance().log(TAG + ", *** SWERR *** MapId is null");
		}
	}

}
