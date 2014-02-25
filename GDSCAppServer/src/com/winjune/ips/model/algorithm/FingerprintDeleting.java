package com.winjune.ips.model.algorithm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.winjune.ips.db.MysqlManager;
import com.winjune.ips.db.PositionTable;
import com.winjune.ips.model.types.Location;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

public class FingerprintDeleting {
	public static final String TAG = "FingerprintDeleting";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static FingerprintDeleting mSingletonInstance = null;

	private FingerprintDeleting() {

	}

	public static FingerprintDeleting getInstance() {
		if (mSingletonInstance == null) {
			mSingletonInstance = new FingerprintDeleting();
			return mSingletonInstance;
		}

		return mSingletonInstance;
	}

	public void delete(Location location) {
		if (location == null) {
			LogUtil.getInstance().log(TAG + ", Location is null.");
			return;
		}
		
		int positionId = PositionTable.getPositionId(location);
		if (positionId < 0) {
			LogUtil.getInstance().log(TAG + ", invalid position.");
			return;
		}
		
		// Delete the WIFI/NFC/QR fingerprints
		deleteFromDbs(positionId);
	}
	
	private void deleteFromDbs(int positionId) {
		MysqlManager instance = MysqlManager.getInstance();
		if (instance == null) {
			LogUtil.getInstance().log(TAG + ", MysqlManager instance is null.");
			return;
		}

		Connection connection = instance.getConnection();
		if (connection == null) {
			LogUtil.getInstance().log(TAG + ", connection is null.");
			return;
		}

		String this_sql = "{call delete_fingerprint_by_positionid(?)}";
		try {
			CallableStatement proc = connection.prepareCall(this_sql);
			proc.setInt("PositionID_IN", positionId);
			proc.execute();
			LogUtil.getInstance().log(TAG + ", position[" + positionId + "] deleted!");
			return;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.getInstance().log(TAG + ", SQLException: " + e.getMessage());
		}
	}
}
