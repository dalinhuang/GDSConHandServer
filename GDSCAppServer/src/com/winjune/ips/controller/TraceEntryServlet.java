package com.winjune.ips.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.winjune.ips.db.ApkUsersTable;
import com.winjune.ips.model.types.User;
import com.winjune.ips.settings.WifiIpsSettings;
import com.winjune.ips.utils.LogUtil;

/**
 * Servlet implementation class TraceServlet
 */
public class TraceEntryServlet extends HttpServlet {
	public static final String TAG = "TraceEntryServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TraceEntryServlet() {
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
			LogUtil.getInstance().log(TAG + "*****************Incoming Message traceEntry Begin******************");
		}

		response.setContentType("text/html;charset=GB2312");  
		
		PrintWriter out = response.getWriter();
		
		out.write("<html>\n\t");
		out.write("<head>\n\t");
		out.write("<title>User Trace</title>\n\t");
		out.write("</head>\n\t");
		out.write("<body>\n\t");

		out.write("Current Available Devices:");
		out.write("<br>");

		ArrayList<User> userList = ApkUsersTable.queryUsers();

		if (userList == null) {
			out.write("No Users record.");
			out.write("<br>");
			out.write("</body>\n\t");
			out.write("</html>\n\t");
			return;
		}
		
		int i = 1;
		for (User user : userList) {
			out.write("<form method=\"POST\" action=\"trace\">");
			out.write("<input type=\"hidden\" name=\"mac\" value=\""+user.getDeviceMacAddress()+"\">");
			out.write("<input type=\"Submit\" value=\"Trace\">");
			out.write(i + " - " + user.getLoginName() + ", " + user.getDeviceMacAddress()
					+ ", " + user.getDeviceName());
			out.write("</form>");
			out.write("<br>");
			i++;
		}
		
		out.write("</body>\n\t");
		out.write("</html>\n\t");
		if (DEBUG) {
			LogUtil.getInstance().log(TAG + "*****************Incoming Message traceEntry End******************");
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
