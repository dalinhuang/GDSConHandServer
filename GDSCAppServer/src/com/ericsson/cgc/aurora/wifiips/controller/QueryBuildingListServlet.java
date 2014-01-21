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
import com.ericsson.cgc.aurora.wifiips.model.types.BuildingManagerReply;
import com.ericsson.cgc.aurora.wifiips.model.types.BuildingReply;
import com.ericsson.cgc.aurora.wifiips.model.types.VersionOrMapIdRequest;
import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiips.utils.LogUtil;
import com.google.gson.Gson;

public class QueryBuildingListServlet extends HttpServlet {
	public static final String TAG = "QueryBuildingListServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QueryBuildingListServlet() {
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
				LogUtil.getInstance().log(TAG + "*****************Incoming Message QueryBuildingList Begin******************");
				LogUtil.getInstance().log(TAG + ", Receive QueryBuildingList message with versionCode: " + version);
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
				
				String sql = "SELECT id, category, name, latitude, longitude, maps, generation"
						+ " FROM building WHERE generation>?";
				
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, versionCode);
				ResultSet rs = statement.executeQuery();
				
				BuildingManagerReply manager = new BuildingManagerReply();
				ArrayList<BuildingReply> buildings = new ArrayList<BuildingReply>();
				int generation = versionCode;

				while (rs.next()) {
					BuildingReply building = new BuildingReply();
					building.setId(rs.getInt(1));
					building.setCategory(rs.getString(2));
					building.setName(rs.getString(3));
					building.setLatitude(rs.getDouble(4));
					building.setLongitude(rs.getDouble(5));
					building.setMaps(rs.getString(6));
					buildings.add(building);
					
					if (rs.getInt(7) > generation) {
						generation = rs.getInt(7);
					}
				}
				
				manager.setVersionCode(generation);
				manager.setBuildings(buildings);
				
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
				LogUtil.getInstance().log(TAG + "*****************Incoming Message QueryBuildingList End******************");
			}
		} else if (DEBUG) {
			LogUtil.getInstance().log(TAG + ", *** SWERR *** versionCode is null");
		}
	}
}
