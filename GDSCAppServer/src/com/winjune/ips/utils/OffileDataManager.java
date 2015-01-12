package com.winjune.ips.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.winjune.ips.db.MysqlManager;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.wifiindoor.lib.map.MapDataT;
import com.winjune.wifiindoor.lib.map.NaviNodeT;
import com.winjune.wifiindoor.lib.map.NaviPathT;
import com.winjune.wifiindoor.lib.poi.BusLineR;
import com.winjune.wifiindoor.lib.poi.MovieInfoR;
import com.winjune.wifiindoor.lib.poi.POIType;
import com.winjune.wifiindoor.lib.poi.PlaceOfInterestR;
import com.winjune.wifiindoor.lib.poi.PlayhouseInfoR;
import com.winjune.wifiindoor.lib.poi.PoiOfflineData;
import com.winjune.wifiindoor.lib.version.VersionInfoT;

public class OffileDataManager {
	public static final String TAG = "POI";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;
	private static PoiOfflineData offlineData;
	private static VersionInfoT versionData;
	private static MapDataT mapData;
	private static NaviNodeT naviNodeData;
	private static NaviPathT naviPathData;
	private static String poiFilePath;
	private static String versionFilePath;
	private static String mapFilePath;
	private static String naviPathFilePath;
	private static String naviNodeFilePath;

	public static void setPoiFilePath(String poiFilePath) {
		OffileDataManager.poiFilePath = poiFilePath;
	}

	public static void setVersionFilePath(String versionFilePath) {
		OffileDataManager.versionFilePath = versionFilePath;
	}

	public static void setMapFilePath(String mapFilePath) {
		OffileDataManager.mapFilePath = mapFilePath;
	}
	public static void setNaviPathFilePath(String naviPathFilePath) {
		OffileDataManager.naviPathFilePath = naviPathFilePath;
	}
	public static void setNaviNodeFilePath(String naviNodeFilePath) {
		OffileDataManager.naviNodeFilePath = naviNodeFilePath;
	}

	public static void toXML() {
		offlineData = new PoiOfflineData(poiFilePath);
		versionData = new VersionInfoT();
		mapData = new MapDataT();
		naviNodeData = new NaviNodeT();
		naviPathData = new NaviPathT();

		addFestivals();
		addPOIs();
		addBusStations();
		addTheatres();
		addRestaurants();

		addPlayhouses();
		
		addVersions();
		addMaps();
		addNaviNodes();
		addNaviPaths();

		offlineData.toXML();
		versionData.toXML(versionFilePath+"version_table.xml",versionData);
		mapData.toXML(mapFilePath+"map_table.xml", mapData);
		naviPathData.toXML(naviPathFilePath+"navi_path_table.xml", naviPathData);
		naviNodeData.toXML(naviNodeFilePath+"navi_node_table.xml", naviNodeData);
	}

	public static void toJson() {
		offlineData = new PoiOfflineData(poiFilePath);
		versionData = new VersionInfoT();
		mapData = new MapDataT();
		naviNodeData = new NaviNodeT();
		naviPathData = new NaviPathT();

		addFestivals();
		addPOIs();
		addBusStations();
		addTheatres();
		addRestaurants();

		addPlayhouses();
		
		addVersions();
		addMaps();
		addNaviNodes();
		addNaviPaths();

		offlineData.toJson();
		versionData.toJson(versionFilePath+"version_table.json",versionData);
		mapData.toJson(mapFilePath+"map_table.json", mapData);
		naviPathData.toJson(naviPathFilePath+"navi_path_table.json", naviPathData);
		naviNodeData.toJson(naviNodeFilePath+"navi_node_table.json", naviNodeData);

	}

	public static void addFestivals() {
		try {
			MysqlManager instance = MysqlManager.getInstance();
			if (instance == null) {
				LogUtil.getInstance().log(
						TAG + ", " + "MysqlManager instance is null.");
				return;
			}

			Connection connection = instance.getConnection();
			if (connection == null) {
				LogUtil.getInstance().log(TAG + ", " + "connection is null.");
				return;
			}

			String sql = "SELECT date" + " FROM festival";

			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				String date = rs.getString(1);
				offlineData.festivalTable.addFestivalDay(date);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (DEBUG) {
				LogUtil.getInstance().log(
						TAG + ", *** SWERR *** Refer to exception traceback");
			}
		}
	}

	public static void addPOIs() {

		try {
			MysqlManager instance = MysqlManager.getInstance();
			if (instance == null) {
				LogUtil.getInstance().log(
						TAG + ", " + "MysqlManager instance is null.");
				return;
			}

			Connection connection = instance.getConnection();
			if (connection == null) {
				LogUtil.getInstance().log(TAG + ", " + "connection is null.");
				return;
			}

			String sql = "SELECT id, type, hallId, ttsNo, mapId, placeX, placeY, neareastNaviNode, iconUrl, audioUrl, webUrl, picUrl, label, generalDesc, detailedDesc, shareble, reachable, readable, scale, alpha, rotation, minZoomFactor, maxZoomFactor"
					+ " FROM poi";

			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				PlaceOfInterestR poiR = new PlaceOfInterestR(
						POIType.values()[rs.getInt(2)]);
				poiR.id = rs.getInt(1);
				poiR.hallId = rs.getInt(3);
				poiR.ttsNo = rs.getInt(4);
				poiR.mapId = rs.getInt(5);
				poiR.placeX = rs.getInt(6);
				poiR.placeY = rs.getInt(7);
				poiR.nearNaviNode = rs.getInt(8);
				poiR.iconUrl = rs.getString(9);
				poiR.audioUrl = rs.getString(10);
				poiR.webUrl = rs.getString(11);
				poiR.picUrl = rs.getString(12);
				poiR.label = rs.getString(13);
				poiR.generalDesc = rs.getString(14);
				poiR.detailedDesc = rs.getString(15);
				poiR.shareble = rs.getBoolean(16);
				poiR.reachable = rs.getBoolean(17);
				poiR.readable = rs.getBoolean(18);
				poiR.scale = rs.getFloat(19);
				poiR.alpha = rs.getFloat(20);
				poiR.rotation = rs.getFloat(21);
				poiR.minZoomFactor = rs.getFloat(22);
				poiR.maxZoomFactor = rs.getFloat(23);
				offlineData.poiTable.poiData.add(poiR);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (DEBUG) {
				LogUtil.getInstance().log(
						TAG + ", *** SWERR *** Refer to exception traceback");
			}
		}

	}

	public static void addBusStations() {

		try {
			MysqlManager instance = MysqlManager.getInstance();
			if (instance == null) {
				LogUtil.getInstance().log(
						TAG + ", " + "MysqlManager instance is null.");
				return;
			}

			Connection connection = instance.getConnection();
			if (connection == null) {
				LogUtil.getInstance().log(TAG + ", " + "connection is null.");
				return;
			}

			String sql = "SELECT poiId, lineName, startTime, endTime, priceInfo, stopInfo"
					+ " FROM busline";

			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				BusLineR aBusLine;
				int poiId = rs.getInt(1);
				String lineName = rs.getString(2);
				String startTime = rs.getString(3);
				String endTime = rs.getString(4);
				String priceInfo = rs.getString(5);
				String stopInfo = rs.getString(6);
				aBusLine = new BusLineR(poiId, // busStation POI id;
						lineName, startTime, endTime, priceInfo, stopInfo);
				offlineData.buslineTable.addBusLine(aBusLine);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (DEBUG) {
				LogUtil.getInstance().log(
						TAG + ", *** SWERR *** Refer to exception traceback");
			}
		}

	}

	public static void addTheatres() {

		try {
			MysqlManager instance = MysqlManager.getInstance();
			if (instance == null) {
				LogUtil.getInstance().log(
						TAG + ", " + "MysqlManager instance is null.");
				return;
			}

			Connection connection = instance.getConnection();
			if (connection == null) {
				LogUtil.getInstance().log(TAG + ", " + "connection is null.");
				return;
			}

			String sql = "SELECT poiId, name, generalDesc, priceInfo, iconUrl, posterUrl, todaySchedule, tomorrowSchedule"
					+ " FROM movie";

			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				MovieInfoR aMovie;
				int poiId = rs.getInt(1);
				String name = rs.getString(2);
				String generalDesc = rs.getString(3);
				String priceInfo = rs.getString(4);
				String iconUrl = rs.getString(5);
				String posterUrl = rs.getString(6);
				String todaySchedule = rs.getString(7);
				String tomorrowSchedule = rs.getString(8);
				aMovie = new MovieInfoR(poiId, name, priceInfo, generalDesc);
				aMovie.addTodaySchedule(todaySchedule);
				offlineData.movieTable.addMovie(aMovie);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (DEBUG) {
				LogUtil.getInstance().log(
						TAG + ", *** SWERR *** Refer to exception traceback");
			}
		}

	}

	public static void addRestaurants() {
		try {
			MysqlManager instance = MysqlManager.getInstance();
			if (instance == null) {
				LogUtil.getInstance().log(
						TAG + ", " + "MysqlManager instance is null.");
				return;
			}

			Connection connection = instance.getConnection();
			if (connection == null) {
				LogUtil.getInstance().log(TAG + ", " + "connection is null.");
				return;
			}

			String sql = "SELECT poiId, category, name, iconUrl, price"
					+ " FROM menu";

			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				int poiId = rs.getInt(1);
				String category = rs.getString(2);
				String name = rs.getString(3);
				String iconUrl = rs.getString(4);
				String price = rs.getString(5);
				offlineData.restaurantTable.addMenuItem(poiId, category, name,
						iconUrl, price);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (DEBUG) {
				LogUtil.getInstance().log(
						TAG + ", *** SWERR *** Refer to exception traceback");
			}
		}

	}

	public static void addPlayhouses() {

		try {
			MysqlManager instance = MysqlManager.getInstance();
			if (instance == null) {
				LogUtil.getInstance().log(
						TAG + ", " + "MysqlManager instance is null.");
				return;
			}

			Connection connection = instance.getConnection();
			if (connection == null) {
				LogUtil.getInstance().log(TAG + ", " + "connection is null.");
				return;
			}

			String sql = "SELECT poiId, normalDayTime, weekendTime, festivalTime"
					+ " FROM playhouse";

			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				int poiId = rs.getInt(1);
				String normalDayTime = rs.getString(2);
				String weekendTime = rs.getString(3);
				String festivalTime = rs.getString(4);
				PlayhouseInfoR mSchedule = new PlayhouseInfoR(poiId);
				mSchedule.addNormalDayTimes(normalDayTime);
				mSchedule.addWeekendTimes(weekendTime);
				mSchedule.addFestivalTimes(festivalTime);
				offlineData.playhouseTable.schedules.add(mSchedule);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (DEBUG) {
				LogUtil.getInstance().log(
						TAG + ", *** SWERR *** Refer to exception traceback");
			}
		}

	}
	
	public static void addVersions() {

		try {
			MysqlManager instance = MysqlManager.getInstance();
			if (instance == null) {
				LogUtil.getInstance().log(
						TAG + ", " + "MysqlManager instance is null.");
				return;
			}

			Connection connection = instance.getConnection();
			if (connection == null) {
				LogUtil.getInstance().log(TAG + ", " + "connection is null.");
				return;
			}

			String sql = "SELECT tableName, version"
					+ " FROM version";

			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			

			while (rs.next()) {
				String tableName = rs.getString(1);
				int version = rs.getInt(2);
				versionData.addVersionItem(tableName, version);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (DEBUG) {
				LogUtil.getInstance().log(
						TAG + ", *** SWERR *** Refer to exception traceback");
			}
		}

	}
	
	public static void addMaps() {

		try {
			MysqlManager instance = MysqlManager.getInstance();
			if (instance == null) {
				LogUtil.getInstance().log(
						TAG + ", " + "MysqlManager instance is null.");
				return;
			}

			Connection connection = instance.getConnection();
			if (connection == null) {
				LogUtil.getInstance().log(TAG + ", " + "connection is null.");
				return;
			}

			String sql = "SELECT map_id, normal_map_url, large_map_url, name, label, cell_pixel, longitude, latitude"
					+ " FROM map";

			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			

			while (rs.next()) {
				int mapId = rs.getInt(1);
				String normalMapUrl = rs.getString(2);
				String largeMapUrl = rs.getString(3);
				String name = rs.getString(4);
				String label = rs.getString(5);
				int cellPixel = rs.getInt(6);
				int longitude = rs.getInt(7);
				int latitude = rs.getInt(8);
				mapData.addMapItem(mapId, normalMapUrl, largeMapUrl, name, label, cellPixel, longitude, latitude);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (DEBUG) {
				LogUtil.getInstance().log(
						TAG + ", *** SWERR *** Refer to exception traceback");
			}
		}

	}
	
	public static void addNaviNodes() {

		try {
			MysqlManager instance = MysqlManager.getInstance();
			if (instance == null) {
				LogUtil.getInstance().log(
						TAG + ", " + "MysqlManager instance is null.");
				return;
			}

			Connection connection = instance.getConnection();
			if (connection == null) {
				LogUtil.getInstance().log(TAG + ", " + "connection is null.");
				return;
			}

			String sql = "SELECT id, map_id, placeX, placeY, label"
					+ " FROM navi_node";

			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			

			while (rs.next()) {
				int id = rs.getInt(1);
				int mapId = rs.getInt(2);
				int placeX = rs.getInt(3);
				int placeY = rs.getInt(4);
				String label = rs.getString(5);
				naviNodeData.addNode(id, mapId, placeX, placeY, label);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (DEBUG) {
				LogUtil.getInstance().log(
						TAG + ", *** SWERR *** Refer to exception traceback");
			}
		}

	}
	
	public static void addNaviPaths() {

		try {
			MysqlManager instance = MysqlManager.getInstance();
			if (instance == null) {
				LogUtil.getInstance().log(
						TAG + ", " + "MysqlManager instance is null.");
				return;
			}

			Connection connection = instance.getConnection();
			if (connection == null) {
				LogUtil.getInstance().log(TAG + ", " + "connection is null.");
				return;
			}

			String sql = "SELECT id, fromNode, toNode, direction, forwardGuide, backwardGuide"
					+ " FROM navi_path";

			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			

			while (rs.next()) {
				int id = rs.getInt(1);
				int fromNode = rs.getInt(2);
				int toNode = rs.getInt(3);
				byte direction = rs.getByte(4);
				String forwardGuide = rs.getString(5);
				String backwardGuide = rs.getString(6);
				naviPathData.addPath(id, fromNode, toNode, direction, forwardGuide, backwardGuide);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (DEBUG) {
				LogUtil.getInstance().log(
						TAG + ", *** SWERR *** Refer to exception traceback");
			}
		}

	}
}
