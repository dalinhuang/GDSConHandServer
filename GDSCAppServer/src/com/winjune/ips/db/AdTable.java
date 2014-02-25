package com.winjune.ips.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.winjune.ips.model.types.Ad;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

public class AdTable {
	public static final String TAG = AdTable.class.toString();
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	public static Ad getAdById(int adId) {
		MysqlManager instance = MysqlManager.getInstance();
		if (instance == null) {
			LogUtil.getInstance().log(
					TAG + ", " + "MysqlManager instance is null.");
			return null;
		}

		Connection connection = instance.getConnection();
		if (connection == null) {
			LogUtil.getInstance().log(TAG + ", " + "connection is null.");
			return null;
		}

		String this_sql = "SELECT thumbnail_img_url, large_img_url, url, duration, from_date, to_date FROM ad\n"
				+ "WHERE ad_id = " + adId + " LIMIT 1";
		try {
			PreparedStatement statement = connection.prepareStatement(this_sql);
			// statement.setInt(1, adId);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				String thumbnailImgUrl = rs.getString(1);
				String largeImgUrl = rs.getString(2);
				String url = rs.getString(3);
				int duration = rs.getInt(4);
				Date fromDate = rs.getDate(5);
				Date toDate = rs.getDate(6);

				Ad ad = new Ad();
				ad.setThumbnailImgUrl(thumbnailImgUrl);
				ad.setLargeImgUrl(largeImgUrl);
				ad.setUrl(url);
				ad.setDuration(duration);
				
				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd");
				ad.setFromDate(dateFormat.format(fromDate));
				ad.setToDate(dateFormat.format(toDate));
				ad.setId(adId);

				return ad;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.getInstance()
					.log(TAG + ", SQLException: " + e.getMessage());
		}

		return null;
	}
}
