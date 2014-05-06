package com.winjune.ips.utils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.winjune.ips.db.MysqlManager;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.wifiindoor.lib.poi.BusLineR;
import com.winjune.wifiindoor.lib.poi.MovieInfoR;
import com.winjune.wifiindoor.lib.poi.POIType;
import com.winjune.wifiindoor.lib.poi.PlaceOfInterestR;
import com.winjune.wifiindoor.lib.poi.PlayhouseInfoR;
import com.winjune.wifiindoor.lib.poi.PoiOfflineData;

public class PoiDB2XML {
	public static final String TAG = "POI";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;
	private static PoiOfflineData offlineData;
	private static String poiFilePath;

	public static void setPoiFilePath(String poiFilePath) {
		PoiDB2XML.poiFilePath = poiFilePath;
	}

	public static void toXML() {
		offlineData = new PoiOfflineData(poiFilePath);

		addFestivals();
		addPOIs();
		addBusStations();
		addTheatres();
		addRestaurants();

		addPlayhouses();

		offlineData.toXML();

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

			String sql = "SELECT id, type, hallId, ttsNo, mapId, placeX, placeY, iconUrl, audioUrl, webUrl, picUrl, label, generalDesc, detailedDesc, shareble, reachable, readable, scale, alpha, rotation, minZoomFactor, maxZoomFactor"
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
				poiR.iconUrl = rs.getString(8);
				poiR.audioUrl = rs.getString(9);
				poiR.webUrl = rs.getString(10);
				poiR.picUrl = rs.getString(11);
				poiR.label = rs.getString(12);
				poiR.generalDesc = rs.getString(13);
				poiR.detailedDesc = rs.getString(14);
				poiR.shareble = rs.getBoolean(15);
				poiR.reachable = rs.getBoolean(16);
				poiR.readable = rs.getBoolean(17);
				poiR.scale = rs.getFloat(18);
				poiR.alpha = rs.getFloat(19);
				poiR.rotation = rs.getFloat(20);
				poiR.minZoomFactor = rs.getFloat(21);
				poiR.maxZoomFactor = rs.getFloat(22);
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
}