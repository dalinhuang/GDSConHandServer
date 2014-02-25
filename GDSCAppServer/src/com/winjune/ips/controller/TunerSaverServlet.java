package com.winjune.ips.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.winjune.ips.db.TunerTable;
import com.winjune.ips.model.types.Tuner;
import com.winjune.ips.settings.WifiIpsSettings;

public class TunerSaverServlet extends HttpServlet {
	public static final String TAG = "TunerSaverServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TunerSaverServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html;charset=GB2312");  
		
		PrintWriter out = response.getWriter();
		
		out.write("<html>\n\t");
		out.write("<head>\n\t");
		out.write("<title>Tuner</title>\n\t");
		out.write("</head>\n\t");
		out.write("<body>\n\t");

		out.write("Preferences:");
		out.write("<br>");
		
		String DBM_LINEAR_AVERAGE = request.getParameter("DBM_LINEAR_AVERAGE");
		String LOGGER_FILE = request.getParameter("LOGGER_FILE");
		String Database_Server = request.getParameter("Database_Server");
		String pilot_power_threshold = request.getParameter("pilot_power_threshold");
		String mse_threshold = request.getParameter("mse_threshold");
		String mse_ref_threshold = request.getParameter("mse_ref_threshold");
		
		float f_pilot_power_threshold;
		float f_mse_threshold;
		float f_mse_ref_threshold;
		
		try {
			f_pilot_power_threshold = Float.valueOf(pilot_power_threshold);
		} catch (Exception e) {
			out.write("Error Type: pilot_power_threshold is not a float");
			out.write("<br>");
			out.write("</body>\n\t");
			out.write("</html>\n\t");
			return;
		}
		
		try {
			f_mse_threshold = Float.valueOf(mse_threshold);
		} catch (Exception e) {
			out.write("Error Type: mse_threshold is not a float");
			out.write("<br>");
			out.write("</body>\n\t");
			out.write("</html>\n\t");
			return;
		}
		
		try {
			f_mse_ref_threshold = Float.valueOf(mse_ref_threshold);
		} catch (Exception e) {
			out.write("Error Type: mse_ref_threshold is not a float");
			out.write("<br>");
			out.write("</body>\n\t");
			out.write("</html>\n\t");
			return;
		}
		
		try {
			Boolean.parseBoolean(DBM_LINEAR_AVERAGE);
		} catch (Exception e) {
			out.write("Error Type: DBM_LINEAR_AVERAGE is not a boolean");
			out.write("<br>");
			out.write("</body>\n\t");
			out.write("</html>\n\t");
			return;
		}
				
		Tuner tuner = new Tuner("CURRENT", DBM_LINEAR_AVERAGE, LOGGER_FILE, Database_Server, f_pilot_power_threshold, f_mse_threshold, f_mse_ref_threshold);
		
		if (TunerTable.saveTunerWithHistory(tuner)){
			out.write("Save successed!");
			out.write("<br>");
		} else {
			out.write("ERROR: Save failed!");
			out.write("<br>");
		}								
		
		out.write("</body>\n\t");
		out.write("</html>\n\t");
	}
}
