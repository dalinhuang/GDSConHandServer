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

public class TunerServlet extends HttpServlet {
	public static final String TAG = "TunerServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TunerServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
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

		Tuner tuner = TunerTable.getRecord("CURRENT");

		if (tuner == null) {
			out.write("No CURRENT Configuration Stored, read Default!");
			out.write("<br>");
			tuner = new Tuner("CURRENT", String.valueOf(WifiIpsSettings.DBM_LINEAR_AVERAGE), WifiIpsSettings.LOGGER_FILE, 
					WifiIpsSettings.Database_Server, WifiIpsSettings.pilot_power_threshold, 
					WifiIpsSettings.mse_threshold, WifiIpsSettings.mse_ref_threshold);
		} else {		
			if (!Tuner.syncToTuner(tuner)){
				out.write("Error: Sync Failed with CURRENT Stored Configuration.");
				out.write("<br>");
				out.write("</body>\n\t");
				out.write("</html>\n\t");
				return;
			} else {
				out.write("Synced with CURRENT Stored Configuration Successfully.");
				out.write("<br>");
			}
		}
		
		out.write("<form method=\"POST\" action=\"saveTuner\">");
		out.write("<input type=\"text\" name=\"DBM_LINEAR_AVERAGE\" value=\""+tuner.getDBM_LINEAR_AVERAGE()+"\">");
		out.write("DBM_LINEAR_AVERAGE<br>");
		out.write("<input type=\"text\" name=\"LOGGER_FILE\" value=\""+tuner.getLOGGER_FILE()+"\">");
		out.write("LOGGER_FILE<br>");
		out.write("<input type=\"text\" name=\"Database_Server\" value=\""+tuner.getDatabase_Server()+"\">");
		out.write("Database_Server<br>");
		out.write("<input type=\"text\" name=\"pilot_power_threshold\" value=\""+tuner.getPilot_power_threshold()+"\">");
		out.write("pilot_power_threshold<br>");
		out.write("<input type=\"text\" name=\"mse_threshold\" value=\""+tuner.getMse_threshold()+"\">");
		out.write("mse_threshold<br>");
		out.write("<input type=\"text\" name=\"mse_ref_threshold\" value=\""+tuner.getMse_ref_threshold()+"\">");
		out.write("mse_ref_threshold<br>");
		out.write("<input type=\"Submit\" value=\"Save\">");
		out.write("</form>");
		out.write("<br>");
		
		out.write("</body>\n\t");
		out.write("</html>\n\t");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
}