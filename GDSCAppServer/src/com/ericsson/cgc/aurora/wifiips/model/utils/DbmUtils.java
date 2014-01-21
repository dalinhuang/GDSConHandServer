package com.ericsson.cgc.aurora.wifiips.model.utils;

import java.util.ArrayList;

public class DbmUtils {
	public static float averageDbmsGeneral(ArrayList<Integer> dBms) {
		float dBmTotal = 0;
		for (Integer dBm : dBms) {
			dBmTotal += dBm.intValue();
		}
		
		if (dBms.size() > 0) {
			return dBmTotal/dBms.size(); 
		} else 
			return 0;
	}
	
	public static float averageDbmsLinear(ArrayList<Integer> dBms) {
		double dBmLinearTotal = 0;
		
		for (Integer dBm : dBms) {
			double dBmLinear = convertDbmToLinear(dBm);
			dBmLinearTotal += dBmLinear;
		}
		
		if (dBms.size() > 0) {
			double dBmLinearAve = dBmLinearTotal/dBms.size();
			double dBmAve = revertDbmFromLinear(dBmLinearAve);
			return (float) dBmAve;
		} else
			return 0;
	}

	public static double convertDbmToLinear(Integer dBm) {
		// dBm转线性功率： linear_power = POWER(10, dBm/10)

		double dbmLinear = Math.pow(10.0, dBm / 10.0);

		return dbmLinear;
	}

	public static double revertDbmFromLinear(double dBmLinear) {
		// 线性功率转dBm： dBm = 10*log(linear_power)
		double dBm = 10.0 * Math.log10(dBmLinear);

		return dBm;
	}
}
