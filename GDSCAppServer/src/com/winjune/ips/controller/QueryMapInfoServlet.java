package com.winjune.ips.controller;

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

import com.google.gson.Gson;
import com.winjune.ips.db.MysqlManager;
import com.winjune.ips.model.types.FieldInfoReply;
import com.winjune.ips.model.types.MapInfoReply;
import com.winjune.ips.model.types.VersionOrMapIdRequest;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

public class QueryMapInfoServlet extends HttpServlet {
	public static final String TAG = "QueryMapInfoServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QueryMapInfoServlet() {
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
				LogUtil.getInstance().log(TAG + "*****************Incoming Message QueryMapInfo Begin******************");
				LogUtil.getInstance().log(TAG + ", Receive QueryMapInfo message with MapId: " + code);
			}

			try {
				int mapId = code.getCode();
				
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
				
				String sql = "SELECT row_id, col_id, title, title_rotation, title_alpha, title_scale, min_zoomfactor, max_zoomfactor"
						+ " FROM position WHERE map_id=?";
				
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, mapId);
				ResultSet rs = statement.executeQuery();
				
				MapInfoReply mapInfo = new MapInfoReply();
				ArrayList<FieldInfoReply> fields = new ArrayList<FieldInfoReply>();

				while (rs.next()) {
					int rowNo = rs.getInt(1);
					int colNo = rs.getInt(2);
					String info = rs.getString(3);
					
					if ((colNo == -1) || (rowNo == -1) || (info == null)) {
						continue;
					}
					
					if (info.isEmpty()) {
						continue;
					}
					
					float rotation = rs.getFloat(4);
					float alpha = rs.getFloat(5);
					float scale = rs.getFloat(6);
					float minZoomFactor = rs.getFloat(7);
					float maxZoomFactor = rs.getFloat(8);
						
					FieldInfoReply field = new FieldInfoReply();
					field.setY(rowNo);  // Row is Y
					field.setX(colNo);  // Col is X
					field.setInfo(info);
					field.setRotation(rotation);
					if (alpha == 0) { // Give a default value the null
						alpha = 1;
					}
					field.setAlpha(alpha);
					if (scale == 0) { // Give a default value the null
						scale = 1;
					}
					field.setScale(scale);
					field.setMinZoomFactor(minZoomFactor);
					field.setMaxZoomFactor(maxZoomFactor);
					fields.add(field);
				}
				
				String sql1 = "SELECT version_code"
						+ " FROM map WHERE map_id=?";
				
				PreparedStatement statement1 = connection.prepareStatement(sql1);
				statement1.setInt(1, mapId);
				ResultSet rs1 = statement1.executeQuery();
				
				if (rs1.next()) {
					mapInfo.setVersionCode(rs1.getInt(1));
				}
				
				mapInfo.setId(mapId);
				mapInfo.setFields(fields);
				
				String infoString = gson.toJson(mapInfo);
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
				LogUtil.getInstance().log(TAG + "*****************Incoming Message QueryMapInfo End******************");
			}
		} else if (DEBUG) {
			LogUtil.getInstance().log(TAG + ", *** SWERR *** MapId is null");
		}
	}

}
