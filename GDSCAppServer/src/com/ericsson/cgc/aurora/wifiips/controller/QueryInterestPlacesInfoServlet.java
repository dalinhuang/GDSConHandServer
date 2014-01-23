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
import com.ericsson.cgc.aurora.wifiips.model.types.InterestPlaceReply;
import com.ericsson.cgc.aurora.wifiips.model.types.InterestPlacesInfoReply;
import com.ericsson.cgc.aurora.wifiips.model.types.VersionOrMapIdRequest;
import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiips.utils.LogUtil;
import com.google.gson.Gson;

public class QueryInterestPlacesInfoServlet extends HttpServlet {
	public static final String TAG = "QueryInterestPlacesInfoServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QueryInterestPlacesInfoServlet() {
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
				LogUtil.getInstance().log(TAG + "*****************Incoming Message QueryInterestPlacesInfo Begin******************");
				LogUtil.getInstance().log(TAG + ", Receive QueryInterestPlacesInfo message with MapId: " + code);
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
				
				String sql = "SELECT row_id, col_id, text_info, pic_info, audio_info, video_info"
						+ " FROM interest_places WHERE map_id=?";
				
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, mapId);
				ResultSet rs = statement.executeQuery();
				
				InterestPlacesInfoReply interestPlacesInfo = new InterestPlacesInfoReply();
				ArrayList<InterestPlaceReply> places = new ArrayList<InterestPlaceReply>();

				while (rs.next()) {
					int rowNo = rs.getInt(1);
					int colNo = rs.getInt(2);
					String textInfo = rs.getString(3);
					String picInfo = rs.getString(4);
					String audioInfo = rs.getString(5);
					String videoInfo = rs.getString(6);
					
					if ((colNo == -1) || (rowNo == -1)) {
						continue;
					}
					
					InterestPlaceReply place = new InterestPlaceReply();
					place.setY(rowNo);  // Row is Y
					place.setX(colNo);  // Col is X
					place.setInfo(textInfo);
					place.setUrlPic(picInfo);
					place.setUrlAudio(audioInfo);
					place.setUrlVideo(videoInfo);
					places.add(place);
				}
				
				String sql1 = "SELECT version_code"
						+ " FROM map WHERE map_id=?";
				
				PreparedStatement statement1 = connection.prepareStatement(sql1);
				statement1.setInt(1, mapId);
				ResultSet rs1 = statement1.executeQuery();
				
				if (rs1.next()) {
					interestPlacesInfo.setVersionCode(rs1.getInt(1));
				}
				
				interestPlacesInfo.setId(mapId);
				interestPlacesInfo.setFields(places);
				
				String infoString = gson.toJson(interestPlacesInfo);
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
				LogUtil.getInstance().log(TAG + "*****************Incoming Message QueryInterestPlacesInfo End******************");
			}
		} else if (DEBUG) {
			LogUtil.getInstance().log(TAG + ", *** SWERR *** MapId is null");
		}
	}

}
