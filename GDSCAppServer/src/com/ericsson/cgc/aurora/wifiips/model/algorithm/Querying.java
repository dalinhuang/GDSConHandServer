package com.ericsson.cgc.aurora.wifiips.model.algorithm;

import java.util.ArrayList;

import com.ericsson.cgc.aurora.wifiips.db.MapTable;
import com.ericsson.cgc.aurora.wifiips.db.NewsTable;
import com.ericsson.cgc.aurora.wifiips.db.PositionTable;
import com.ericsson.cgc.aurora.wifiips.model.types.InfoQueryRequest;
import com.ericsson.cgc.aurora.wifiips.model.types.Location;
import com.ericsson.cgc.aurora.wifiips.model.types.LocationQueryInfo;
import com.ericsson.cgc.aurora.wifiips.model.types.QueryInfo;
import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiips.utils.LogUtil;

public class Querying {
	public static final String TAG = "Querying";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static Querying mSingletonInstance = null;

	private Querying() {

	}

	public static Querying getInstance() {
		if (mSingletonInstance == null) {
			mSingletonInstance = new Querying();
			return mSingletonInstance;
		}

		return mSingletonInstance;
	}

	public QueryInfo query(InfoQueryRequest request) {
		if (DEBUG)
			LogUtil.getInstance().log(TAG + ", query");

		QueryInfo queryInfo = new QueryInfo();
		
		ArrayList<Location> locations = request.getLocations();

		for (Location location : locations) {			
			// Verify valid map version code
			int mapVersionCode = location.getMapVersion();
			int mapId = location.getMapId();			
			if (!MapTable.verifyMapVersion(mapId, mapVersionCode)) {
				LogUtil.getInstance().log(TAG + ", Inconsistent map version!");
				continue;
			}			
			
			int positionId = PositionTable.getPositionId(location);
			if (positionId < 0) {
				LogUtil.getInstance().log(TAG + ", invalid position.");
				continue;
			}

			// search information by position id
			ArrayList<String> messages = NewsTable
					.getMessagesByPositionId(positionId);
			if ((messages == null) || (messages.size() == 0)) {
				//if (DEBUG)
				//	LogUtil.getInstance().log(
				//			TAG + ", "
				//					+ "no advertisement found for position id "
				//					+ positionId);
			} else {
				LocationQueryInfo info = new LocationQueryInfo(location,
						messages);
				
				queryInfo.add(info);
			}
		}
		
		return queryInfo;
	}
}
