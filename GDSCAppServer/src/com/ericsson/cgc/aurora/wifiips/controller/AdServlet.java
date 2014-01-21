package com.ericsson.cgc.aurora.wifiips.controller;

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

import com.ericsson.cgc.aurora.wifiips.db.AdTable;
import com.ericsson.cgc.aurora.wifiips.db.PositionAdTable;
import com.ericsson.cgc.aurora.wifiips.db.PositionTable;
import com.ericsson.cgc.aurora.wifiips.model.types.Ad;
import com.ericsson.cgc.aurora.wifiips.model.types.AdGroup;
import com.ericsson.cgc.aurora.wifiips.model.types.Location;
import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiips.utils.LogUtil;
import com.google.gson.Gson;

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
