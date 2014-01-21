package com.ericsson.cgc.aurora.wifiips.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ericsson.cgc.aurora.wifiips.db.TestRecordTable;
import com.ericsson.cgc.aurora.wifiips.model.algorithm.Tracing;
import com.ericsson.cgc.aurora.wifiips.model.types.TestRecord;
import com.ericsson.cgc.aurora.wifiips.model.types.Location;
import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiips.utils.LogUtil;

/**
 * Servlet implementation class TraceServlet
 */
public class QueryTestRecordServlet extends HttpServlet {
	public static final String TAG = "QueryTestRecordServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QueryTestRecordServlet() {
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
		
		if (DEBUG) {
			LogUtil.getInstance().log(TAG + "*****************Query Test Record Begin******************");
		}

		response.setContentType("text/html;charset=GB2312");  
		
		PrintWriter out = response.getWriter();
		
		out.write("<html>\n\t");
		out.write("<head>\n\t");
		out.write("<title>Test Record</title>\n\t");
		out.write("</head>\n\t");
		out.write("<body>\n\t");

		Tracing tracingInstance = Tracing.getInstance();
		if (tracingInstance == null) {
			LogUtil.getInstance().log(TAG + ", " + "Tracing instance is null.");
			out.write("Error on query the trace records.");
			out.write("<br>\n\t");	
			out.write("</body>\n\t");
			out.write("</html>\n\t");
			return;
		}

		ArrayList<TestRecord> recordList = TestRecordTable.queryTestRecord();			
		
		if (recordList == null) {
			out.write("No Test record.");
			out.write("<br>\n\t");			
			out.write("</body>\n\t");
			out.write("</html>\n\t");		
			return;
		}
		
		out.write("<font face='Courier New'>\n\t");
		
		int i = 1, rowDif, colDif;
		double errorDistance;
		Location fromLoc, resLoc;
		for (TestRecord record : recordList) {
			fromLoc = record.getFromLocation();
			resLoc  = record.getLocateLocation();
			
			out.write(i + " - " + record.getTimestamp() + " - " + record.getMac());
			out.write("<br>\n\t");
			out.write("------FROM [" + fromLoc.getMapId()
					+ ": " + fromLoc.getRowId()
					+ ", " + fromLoc.getColId() + "]");
			out.write("<br>\n\t");
			out.write("----LOCATE [" + resLoc.getMapId()
					+ ": " + resLoc.getRowId()
					+ ", " + resLoc.getColId() + "]");
			out.write("<br>\n\t");
			String result = "";
			
			if (fromLoc.getMapId() == resLoc.getMapId()) {
				rowDif = Math.abs(fromLoc.getRowId() - resLoc.getRowId());
				colDif = Math.abs(fromLoc.getColId() - resLoc.getColId());
				errorDistance = Math.sqrt(Math.pow(rowDif, 2) + Math.pow(colDif, 2));
				result = "GAP: [" + rowDif + ", " + colDif
					   + "], Error Distance: " + String.format("%.2f", errorDistance);
			} else {
				result = "Different Map!";
			}
			
			out.write("----RESULT: " + result + "; by: " + record.getFrom());
			out.write("<br>\n\t");
			i++;
		}
		
		out.write("</font>\n\t");
		
		out.write("</body>\n\t");
		out.write("</html>\n\t");
		if (DEBUG) {
			LogUtil.getInstance().log(TAG + "*****************Query Test Record End******************");
		}
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
