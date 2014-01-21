package com.ericsson.cgc.aurora.wifiips.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ericsson.cgc.aurora.wifiips.model.algorithm.Tracing;
import com.ericsson.cgc.aurora.wifiips.model.types.TraceRecord;
import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiips.utils.LogUtil;

/**
 * Servlet implementation class TraceServlet
 */
public class TraceServlet extends HttpServlet {
	public static final String TAG = "TraceServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TraceServlet() {
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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (DEBUG) {
			LogUtil.getInstance().log(TAG + "*****************Incoming Message trace Begin******************");
		}
		
		String mac = request.getParameter("mac");
		String loginName = request.getParameter("loginName");
		
		response.setContentType("text/html;charset=GB2312");  

		PrintWriter out = response.getWriter();
		
		out.write("<html>\n\t");
		out.write("<head>\n\t");
		out.write("<title>User Trace for " + mac + "(" + loginName + ")" + "</title>\n\t");
		out.write("</head>\n\t");
		out.write("<body>\n\t");

		ArrayList<TraceRecord> recordList = null;
		
		if (loginName != null){
			out.write(loginName);
			out.write("<br>\n\t");

			if (DEBUG) {
				LogUtil.getInstance().log(TAG + ", " + "loginName = " + loginName);
			}

			Tracing tracingInstance = Tracing.getInstance();
			if (tracingInstance == null) {
				LogUtil.getInstance().log(TAG + ", " + "Tracing instance is null.");
				out.write("Error on query the trace records.");
				out.write("<br>\n\t");	
				out.write("</body>\n\t");
				out.write("</html>\n\t");
				return;
			}

			recordList = tracingInstance.tracePerLoginName(loginName);			
		} // if (loginName != null)
		
		if (mac != null){

			out.write(mac);
			out.write("<br>\n\t");
	
			if (DEBUG) {
				LogUtil.getInstance().log(TAG + ", " + "MAC = " + mac);
			}
	
			Tracing tracingInstance = Tracing.getInstance();
			if (tracingInstance == null) {
				LogUtil.getInstance().log(TAG + ", " + "Tracing instance is null.");
				out.write("Error on query the trace records.");
				out.write("<br>\n\t");	
				out.write("</body>\n\t");
				out.write("</html>\n\t");
				return;
			}
	
			recordList = tracingInstance.tracePerMac(mac);
		} // if (mac != null){
		
		if (recordList == null) {
			out.write("No trace record.");
			out.write("<br>\n\t");			
			out.write("</body>\n\t");
			out.write("</html>\n\t");		
			return;
		}
		
		int i = 1;
		for (TraceRecord record : recordList) {
			out.write(i + " - " + record.getTimestamp() + "  >>  " + record.getPositionId()
					+ "[" + record.getMapId() + ": " + record.getRowId() + ", " + record.getColId() + "]"
					+ "  >>  "  + record.getPositionDescription() + "  >>  "
					+ record.getFromSignal());
			out.write("<br>\n\t");
			i++;
		}
		
		out.write("</body>\n\t");
		out.write("</html>\n\t");
		if (DEBUG) {
			LogUtil.getInstance().log(TAG + "*****************Incoming Message trace End******************");
		}
	}
}
