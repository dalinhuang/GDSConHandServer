package com.winjune.ips.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.winjune.ips.db.MysqlManager;
import com.winjune.ips.model.types.ApkVersionReply;
import com.winjune.ips.model.types.ApkVersionRequest;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;
import com.winjune.ips.utils.OffileDataManager;

/**
 * Servlet implementation class QueryApkVersionServlet
 */
public class QueryApkVersionServlet extends HttpServlet {
	public static final String TAG = "QueryApkVersionServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QueryApkVersionServlet() {
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
		// TODO Auto-generated method stub
		BufferedReader reader = request.getReader();
		
		LogUtil.getInstance()
		.log(TAG
				+ "*****************Incoming Message QueryApkVersion Begin******************");

		Gson gson = new Gson();
		
		ApkVersionRequest clientVersion = gson.fromJson(reader, ApkVersionRequest.class);
		
		// Query on Server
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
		
		int versionCode = -1;
		String versionName = "error";
		String apkUrl = "";

		String this_sql = "SELECT versionCode, versionName, fileURL FROM apk_version";
		try {
			PreparedStatement statement = connection.prepareStatement(this_sql);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				versionCode = rs.getInt(1);
				versionName = rs.getString(2);
				apkUrl = rs.getString(3);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.getInstance().log(TAG + ", SQLException: " + e.getMessage());
		}

		// Send down real URL only if needed
		if (versionCode == clientVersion.getVersionCode()) {
			apkUrl = "";
		}
		
		ApkVersionReply reply = new ApkVersionReply(versionCode, versionName, apkUrl);

		String replyString = gson.toJson(reply);

		JSONObject jsonObject = new JSONObject(replyString);

		jsonObject.write(response.getWriter());

		LogUtil.getInstance().log(TAG + ", " + "Response sent: " + jsonObject.toString());

		LogUtil.getInstance()
				.log(TAG
						+ "*****************Incoming Message QueryApkVersion End******************");
		String fileSeparator = System.getProperty("file.separator");
		String filePath = getServletContext().getRealPath(fileSeparator+"xml")+fileSeparator;
		OffileDataManager.setPoiFilePath(filePath);
		OffileDataManager.setVersionFilePath(filePath);
		OffileDataManager.setMapFilePath(filePath);
		OffileDataManager.setNaviNodeFilePath(filePath);
		OffileDataManager.setNaviPathFilePath(filePath);
		OffileDataManager.toXML();
	}

}
