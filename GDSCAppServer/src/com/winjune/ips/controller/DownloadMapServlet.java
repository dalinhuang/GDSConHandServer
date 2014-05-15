package com.winjune.ips.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.winjune.ips.db.MysqlManager;
import com.winjune.ips.model.types.IndoorMapReply;
import com.winjune.ips.model.types.InitialMapReply;
import com.winjune.ips.model.types.VersionOrMapIdRequest;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

public class DownloadMapServlet extends HttpServlet {
	public static final String TAG = "DownloadMapServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DownloadMapServlet() {
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

		VersionOrMapIdRequest id = gson.fromJson(reader, VersionOrMapIdRequest.class);

		if (id != null) {
			if (DEBUG) {
				LogUtil.getInstance().log(TAG + "*****************Incoming Message DownloadMap Begin******************");
				LogUtil.getInstance().log(TAG + ", Receive DownloadMap message with mapId: " + id);
			}

			try {
				int mapId = id.getCode();
				
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
				
				String sql = "SELECT normal_map_url, large_map_url, name, label, cell_pixel, longitude, latitude, version_code"
						+ " FROM map WHERE map_id=?";
				
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, mapId);
				ResultSet rs = statement.executeQuery();

				if (rs.next()) {
					IndoorMapReply map = new IndoorMapReply();
					map.setNormalMapUrl(rs.getString(1));
					map.setLargeMapUrl(rs.getString(2));
					map.setName(rs.getString(3));
					map.setLabel(rs.getString(4));
					map.setCellPixel(rs.getInt(5));
					map.setLongitude(rs.getInt(6));
					map.setLatitude(rs.getInt(7));
					map.setVersionCode(rs.getInt(8));
					map.setId(mapId);
					
					String mapString = gson.toJson(map);
					JSONObject jsonObject = new JSONObject(mapString);

					jsonObject.write(response.getWriter());
				}

			} catch (Exception e) {
				e.printStackTrace();
				
				if (DEBUG) {
					LogUtil.getInstance().log(TAG + ", *** SWERR *** Refer to exception traceback");
				}
			}
			
			if (DEBUG) {
				LogUtil.getInstance().log(TAG + "*****************Incoming Message DownloadMap End******************");
			}
		} else if (DEBUG) {
			LogUtil.getInstance().log(TAG + ", *** SWERR *** mapId is null");
		}
	}
	
}
