package com.ericsson.cgc.aurora.wifiips.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.ericsson.cgc.aurora.wifiips.db.MysqlManager;
import com.ericsson.cgc.aurora.wifiips.model.types.MapManagerItemReply;
import com.ericsson.cgc.aurora.wifiips.model.types.MapManagerReply;
import com.ericsson.cgc.aurora.wifiips.model.types.VersionOrMapIdRequest;
import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiips.utils.LogUtil;
import com.google.gson.Gson;

public class QueryMapListServlet extends HttpServlet {
	public static final String TAG = "QueryMapListServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QueryMapListServlet() {
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

		VersionOrMapIdRequest version = gson.fromJson(reader, VersionOrMapIdRequest.class);

		if (version != null) {
			if (DEBUG) {
				LogUtil.getInstance().log(TAG + "*****************Incoming Message QueryMapList Begin******************");
				LogUtil.getInstance().log(TAG + ", Receive QueryMapList message with versionCode: " + version);
			}

			try {
				int versionCode = version.getCode();
				
				MysqlManager instance = MysqlManager.getInstance();
				if (instance == null) {
					LogUtil.getInstance().log(
							TAG + ", " + "MysqlManager instance is null.");
					return;
				}
	
				Connection connection = instance.getConnection();
				if (connection == null) {
					LogUtil.getInstance().log(
							TAG + ", " + "connection is null.");
					return;
				}
				
				String sql = "SELECT map_id, name, version, rows, columns, generation"
						+ " FROM map WHERE generation>?";
				
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, versionCode);
				ResultSet rs = statement.executeQuery();
				
				MapManagerReply manager = new MapManagerReply();
				ArrayList<MapManagerItemReply> maps = new ArrayList<MapManagerItemReply>();
				int generation = versionCode;

				while (rs.next()) {
					MapManagerItemReply map = new MapManagerItemReply();
					map.setMapId(rs.getInt(1));
					map.setTitle(rs.getString(2));
					map.setVersion(rs.getString(3));
					map.setRows(rs.getInt(4));
					map.setColumns(rs.getInt(5));
					maps.add(map);
					
					if (rs.getInt(6) > generation) {
						generation = rs.getInt(6);
					}
				}
				
				manager.setVersionCode(generation);
				manager.setMapItems(maps);
				
				String listString = gson.toJson(manager);
				JSONObject jsonObject = new JSONObject(listString);

				jsonObject.write(response.getWriter());

			} catch (Exception e) {
				e.printStackTrace();
				
				if (DEBUG) {
					LogUtil.getInstance().log(TAG + ", *** SWERR *** Refer to exception traceback");
				}
			}
			
			if (DEBUG) {
				LogUtil.getInstance().log(TAG + "*****************Incoming Message QueryMapList End******************");
			}
		} else if (DEBUG) {
			LogUtil.getInstance().log(TAG + ", *** SWERR *** versionCode is null");
		}
	}

}
