package com.winjune.ips.model.algorithm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.winjune.ips.db.MysqlManager;
import com.winjune.ips.db.PositionTable;
import com.winjune.ips.model.types.Location;
import com.winjune.ips.model.types.LocationSet;
import com.winjune.ips.model.types.MyWifiInfo;
import com.winjune.ips.model.types.WifiFingerPrint;
import com.winjune.ips.model.types.WifiFingerPrintSample;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

public class Locating {
	public static final String TAG = "Locating";
	public static final boolean DEBUG = false;

	private static Locating mSingletonInstance = null;
	private static final int bad_rc = -1; // "Bad Return Point";

	private Locating() {

	}

	public static Locating getInstance() {
		if (mSingletonInstance == null) {
			mSingletonInstance = new Locating();
			return mSingletonInstance;
		}

		return mSingletonInstance;
	}

	public static LocationSet locate_me(String TAG, WifiFingerPrint wifiFingerPrint, int testMode, int reTest, Location inputLocation) {
		if (wifiFingerPrint != null) {
			wifiFingerPrint.logDebugData(TAG); // always record the input! Always!
		} else {
			LogUtil.getInstance().log(TAG + ", *** SWERR *** Receive locate message with no WifiFignerPrint.");
			return null;
		}

		Map<String, Integer> mapInput = new HashMap<String, Integer>();
		LocationSet locs = new LocationSet();
		Location loc = null;
		ArrayList<Integer> poses = new ArrayList<Integer>();
		int positionId = -1;
		
		// locate samples one by one.
		MysqlManager instance = MysqlManager.getInstance();

		if (instance == null) {
			LogUtil.getInstance().log(TAG + ", MysqlManager instance is null.");
			return null;
		}

		Connection connection = instance.getConnection();
		if (connection == null) {
			LogUtil.getInstance().log(TAG + ", No connection to Mysql");
			return null;
		}

		locs.setLocations(new ArrayList<Location>());
		for (WifiFingerPrintSample this_sample : wifiFingerPrint.getSamples()) {
			mapInput.clear(); // reset the input map.
			for (MyWifiInfo wifi : this_sample.getNeighboringAPs()) {
				mapInput.put(wifi.getMac(), wifi.getDbm());
			}

			positionId = Locating.locate_one_sample(mapInput, connection);

			if (bad_rc != positionId) {
				loc = PositionTable.getPositionById(positionId);
				if (null == loc) {
					LogUtil.getInstance().log(TAG + ", *** SWERR *** Failed when NEW a location, it should not happen!");
					return null;
				}
				locs.addLocation(loc);
				poses.add(positionId);
			}
		}

		// save trace after localization.
		Tracing tracingInstance = Tracing.getInstance();
		if (null == tracingInstance) {
			LogUtil.getInstance().log(TAG + ", *** SWERR *** Tracing instance is null.");
		} else {
			String myWifiMac = wifiFingerPrint.getMyWifiMac(),
			       reliabString, traceNote, testRecordNote = "";
			String[] modeString = {"wifi", "test locate", "test collect"};

			if (0 == reTest) {
				reliabString = "Coarse";
			} else {
				reliabString = "Fine";
				testRecordNote = " 1st";
			}

			traceNote = reliabString + " location from " + modeString[testMode];

			tracingInstance.saveOrUpdateUser(myWifiMac,	wifiFingerPrint.getDeviceName(), wifiFingerPrint.getAccountName());

			for (int thispos : poses) {
				tracingInstance.saveTrace(myWifiMac, thispos, traceNote);			
			}
			
			if(0 != testMode) {
				testRecordNote = modeString[testMode] + testRecordNote;
				for (Location this_output : locs.getLocations()) {
					tracingInstance.saveTestRecord(myWifiMac, inputLocation, this_output, testRecordNote);						
				}
			}
		}

		return locs;
	}

	private static int locate_one_sample(Map<String, Integer> input, Connection connection) {
		int input_ap_count = input.size();
		List<Map.Entry<String, Integer>> assistList = new ArrayList<Map.Entry<String, Integer>>(input.entrySet());
		Collections.sort(assistList,
				new Comparator<Map.Entry<String, Integer>>() {
					public int compare(Map.Entry<String, Integer> o1,
									   Map.Entry<String, Integer> o2) {
						return (o2.getValue() - o1.getValue()); // descending.
					}
				});

		try {
			String pilot_sql = "{call powerful_get_fingerprint_from_pilots(?, ?, ?, ?, ?, ?, ?, ?, ?)}",
				   this_key;
			
			int best_match_pos = -1, //"404 NOT FOUND"
				best_match_count = -1;
			double this_average, this_percentage, this_mse, init_match_mse = 10000.0, best_match_mse = init_match_mse;

			List<Double> power_percentage_list = new LinkedList<Double>();
			Map<String, Float> rs_hashMap = new HashMap<String, Float>();
			TreeSet<Integer> searchRange = new TreeSet<Integer>();

			CallableStatement proc = connection.prepareCall(pilot_sql);
			proc.setString("Mac1", assistList.get(0).getKey());
			proc.setString("Mac2", assistList.get(1).getKey());
			proc.setString("Mac3", assistList.get(2).getKey());
			proc.setFloat("Power1", assistList.get(0).getValue());
			proc.setFloat("Power2", assistList.get(1).getValue());
			proc.setFloat("Power3", assistList.get(2).getValue());
			proc.setFloat("Power_threshold", WifiIpsSettings.pilot_power_threshold);
			proc.setFloat("MSE_Ref_threshold", WifiIpsSettings.mse_ref_threshold);
			proc.setBoolean("DBM_LINEAR_AVERAGE", WifiIpsSettings.DBM_LINEAR_AVERAGE);
			
			proc.execute();
			ResultSet rs = proc.getResultSet();

			// now handle the return result for pilot positions (now 3 pilots).
			while (rs.next()) {
				searchRange.add(rs.getInt("PositionID"));
			}

			if (searchRange.size() == 0) {
				LogUtil.getInstance().log(TAG + ", "
						+ "*** WARNING *** No pilot positions returned!\n"
						+ "\t\t\t\tBad luck! we have to loop all the defined positions as a remedy.\n"
						+ "\t\t\t\tMaybe the threshold (now "
						+ WifiIpsSettings.pilot_power_threshold + "dBm) is too small???");
				pilot_sql = "{call powerful_get_fingerprint_for_all_position(?, ?)}";
				proc = connection.prepareCall(pilot_sql);
				proc.setFloat("MSE_Ref_threshold", WifiIpsSettings.mse_ref_threshold);
				proc.setBoolean("DBM_LINEAR_AVERAGE", WifiIpsSettings.DBM_LINEAR_AVERAGE);

				proc.execute();
				rs = proc.getResultSet();
				while (rs.next()) {
					searchRange.add(rs.getInt("PositionID"));
				}
			}

			List<Integer> MinMatchCountRequirement = new LinkedList<Integer>();
			
			if (input_ap_count <= 10) { // two few inputs, hardcode 6, 5, 4, 3 as the requirement.
				MinMatchCountRequirement.add(6);
				MinMatchCountRequirement.add(5);
				MinMatchCountRequirement.add(4);
				MinMatchCountRequirement.add(3);
			} else {
				// drop 30% of the AP count.
				for (int i=(int) (input_ap_count*.7); i>=3; i--) {
					MinMatchCountRequirement.add(i);
				}
			}
			
			for(Integer i : MinMatchCountRequirement)
			{
				rs.first();
				rs.previous();
				for (Integer this_pos_can : searchRange) {
					power_percentage_list.clear();
					this_average = 0;
					rs_hashMap.clear();
					while (rs.next()) {
						if (rs.getInt("PositionID") != this_pos_can) {
							// we have all data for current position.
							break;
						}
						rs_hashMap.put(rs.getString("MacAddress"),
									   rs.getFloat("pwr"));
					}
					rs.previous();
					
					// now loop all input APs.
					for (Map.Entry<String, Integer> this_entry : input.entrySet()) {
						this_key = this_entry.getKey();
						if (rs_hashMap.containsKey(this_key)) {
							this_percentage = this_entry.getValue()	/ rs_hashMap.get(this_key);
							power_percentage_list.add(this_percentage);
							this_average += this_percentage;
						}
					}
	
					if (power_percentage_list.size() >= i) {
						this_average /= power_percentage_list.size();
						this_mse = 0.0;
	
						for (Double this_per : power_percentage_list) {
							this_mse += Math.pow(this_per - this_average, 2);
						}
						this_mse = this_mse / power_percentage_list.size();
	
						if (this_mse < best_match_mse) {
							best_match_mse = this_mse;
							best_match_pos = this_pos_can;
							best_match_count = power_percentage_list.size();
						}
					}
				}
				
				if (best_match_mse != init_match_mse)
					break;
				else
					LogUtil.getInstance().log(TAG + ", nothing found: Min Match Count Requirement Fallback from "
							+ i + " to " + (i-1));
			}

			LogUtil.getInstance().log(TAG
					+ ":\t>> Match Result: " + best_match_pos
					+ ";   Match Index: "  + String.format("%.6f", best_match_mse)
					+ ";   Match Count: "  + best_match_count	+ "/" + input_ap_count);

			rs.close();
			
			if (best_match_mse < WifiIpsSettings.mse_threshold)	{
				return best_match_pos;
			}
			else
			{
				return bad_rc;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LogUtil.getInstance().log(TAG + ", MySQL returns failure: " + e.getMessage());
		}
		return bad_rc;
	}
}
