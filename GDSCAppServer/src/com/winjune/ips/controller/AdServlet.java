package com.winjune.ips.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.winjune.ips.db.AdTable;
import com.winjune.ips.db.PositionAdTable;
import com.winjune.ips.db.PositionTable;
import com.winjune.ips.model.types.Ad;
import com.winjune.ips.model.types.AdGroup;
import com.winjune.ips.model.types.Location;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

public class AdServlet extends HttpServlet {
	public static final String TAG = AdServlet.class.toString();
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;
	/**
	 * 
	 */
	private static final long serialVersionUID = 980072488997320594L;

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

		Gson gson = new Gson();

		Location location = gson.fromJson(reader, Location.class);

		if (location != null) {
			if (DEBUG) {
				LogUtil.getInstance()
						.log(TAG
								+ "*****************Incoming Message AD Begin******************");
				LogUtil.getInstance().log(
						TAG + ", Receive AD message with Location: "
								+ "{map id: " + location.getMapId()
								+ ", map version: " + location.getMapVersion()
								+ ", (" + location.getRowId() + ", " + location.getColId() + ")");
			}
		} else {
			LogUtil.getInstance().log(Level.WARNING, "Location is null, invalid request");
			return;
		}
		
		// map version is not regarded as an input parameter, we need enhance it.
		//int positionId = PositionTable.getPositionId(location);
		int positionId = 324;
		if (positionId > 0) {
			List<Integer> adIds = PositionAdTable.getAdByPosition(positionId);
			AdGroup adGroup = new AdGroup();
			List<Ad> ads = new ArrayList<Ad>(adIds.size());
			adGroup.setAds(ads);
			
			for (Integer adId : adIds) {
				Ad ad = AdTable.getAdById(adId);
				ads.add(ad);
			}
			
			String adGroupJsonString = gson.toJson(adGroup);
			JSONObject jsonObject = new JSONObject(adGroupJsonString);

			jsonObject.write(response.getWriter());
			if (DEBUG) {
				LogUtil.getInstance().log(TAG + ", Response sent: " + jsonObject.toString());
				LogUtil.getInstance().log(TAG + "*****************Incoming Message AD End******************");			
			}
		}
	}
}
