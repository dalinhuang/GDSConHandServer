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
import com.winjune.ips.model.types.NaviDataReply;
import com.winjune.ips.model.types.NaviInfoReply;
import com.winjune.ips.model.types.NaviNodeReply;
import com.winjune.ips.model.types.VersionOrMapIdRequest;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

public class QueryNaviInfoServlet extends HttpServlet {
	public static final String TAG = "QueryNaviInfoServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QueryNaviInfoServlet() {
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
				LogUtil.getInstance().log(TAG + "*****************Incoming Message QueryNaviInfo Begin******************");
				LogUtil.getInstance().log(TAG + ", Receive QueryNaviInfo message with MapId: " + code);
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
				
				// Get this Map and all Neighbor Maps
				String sql = "SELECT to_map"
						+ " FROM navigator_neighbor_maps WHERE from_map=?";
				
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, mapId);
				ResultSet rs = statement.executeQuery();	
							
				String maps = "";
				while (rs.next()) {					
					maps += rs.getInt(1);
					maps += ",";
				}
				
				NaviInfoReply naviInfo = new NaviInfoReply();				
				naviInfo.setId(mapId);
				
				String sql1 = "SELECT version_code"
						+ " FROM map WHERE map_id=?";
				
				PreparedStatement statement1 = connection.prepareStatement(sql1);
				statement1.setInt(1, mapId);
				ResultSet rs1 = statement1.executeQuery();
				
				if (rs1.next()) {
					naviInfo.setVersionCode(rs1.getInt(1));
				}
				
				// Select out all Nodes in this Map and neighboring maps
				String sql2 = "SELECT id, general_name_id, map_id, col_id, row_id, name"
						+ " FROM navigator_nodes WHERE map_id IN (" 
						+ maps + mapId + ")";
				
				PreparedStatement statement2 = connection.prepareStatement(sql2);
				ResultSet rs2 = statement2.executeQuery();
				
				ArrayList<NaviNodeReply> nodes = new ArrayList<NaviNodeReply>();
				String nodeIds = "";
				while (rs2.next()) {					
					int nodeId2 = rs2.getInt(1);
					int nameId = rs2.getInt(2);
					int mapId2 = rs2.getInt(3);
					int colId2 = rs2.getInt(4);
					int rowId2 = rs2.getInt(5);
					String name2 = rs2.getString(6);
					
					if ((mapId2 == -1) ||  (name2 == null) || name2.isEmpty()) {
						continue;
					}
					
					nodeIds += nodeId2;
					nodeIds += ",";

					NaviNodeReply node = new NaviNodeReply();
					node.setId(nodeId2);
					node.setNameId(nameId);
					node.setMapId(mapId2);
					node.setX(colId2);
					node.setY(rowId2);
					node.setName(name2);
					nodes.add(node);
				}
				
				if (!nodes.isEmpty()) {
					naviInfo.setNodes(nodes);
					nodeIds = nodeIds.substring(0, nodeIds.length()-1); // Delete last ","
					
					String sql3 = "SELECT from_node, to_node, distance, forward_guide_info, backward_guide_info"
							+ " FROM navigator_data WHERE from_node IN (" + nodeIds + ") AND to_node IN (" + nodeIds + ")";
					
					PreparedStatement statement3 = connection.prepareStatement(sql3);
					ResultSet rs3 = statement3.executeQuery();
					
					ArrayList<NaviDataReply> paths = new ArrayList<NaviDataReply>();
					while (rs3.next()) {
						int fromNode3 = rs3.getInt(1);
						int toNode3 = rs3.getInt(2);
						int distance3 = rs3.getInt(3);
						String fInfo3 = rs3.getString(4);
						String bInfo3 = rs3.getString(5);
						
						if (distance3 < 0) {
							continue;
						}
						
						NaviDataReply path = new NaviDataReply();
						path.setFrom(fromNode3);
						path.setTo(toNode3);
						path.setDistance(distance3);
						path.setForwardInfo(fInfo3);
						path.setBackwardInfo(bInfo3);
						paths.add(path);
					}
					
					if (!paths.isEmpty()) {
						naviInfo.setPaths(paths);
					}
				}

				String infoString = gson.toJson(naviInfo);
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
				LogUtil.getInstance().log(TAG + "*****************Incoming Message QueryNaviInfo End******************");
			}
		} else if (DEBUG) {
			LogUtil.getInstance().log(TAG + ", *** SWERR *** MapId is null");
		}
	}

}
