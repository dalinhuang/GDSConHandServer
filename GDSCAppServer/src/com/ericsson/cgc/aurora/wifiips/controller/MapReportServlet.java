package com.ericsson.cgc.aurora.wifiips.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ericsson.cgc.aurora.wifiips.model.types.MapCell;
import com.ericsson.cgc.aurora.wifiips.model.types.MapReport;
import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiips.utils.LogUtil;

/**
 * Servlet implementation class MapReportServlet
 */
public class MapReportServlet extends HttpServlet {
	public static final String TAG = "MapReportServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MapReportServlet() {
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
			LogUtil.getInstance()
					.log(TAG
							+ "***************** MapReport Begin ******************");
		}
		
		String map = request.getParameter("mapId");
		MapReport mapReport = new MapReport();
		mapReport.queryMapInfo(map);
		
		response.setContentType("text/html;charset=GB2312");  

		PrintWriter out = response.getWriter();
		
		out.write("<html>\n\t");
		out.write("<head>\n\t");
		out.write("<title>Map Report for " + mapReport.getMapName() + "(" + map + ")" + "</title>\n\t");
		out.write("</head>\n\t");
		out.write("<body>\n\t");
		
		out.write("<font face='Courier New' size='9'>\n\t");
		
		out.write("<table border='1' cellspacing='0'>\n\t");
		mapReport.queryMapData();
		
		int[][] counters = new int[mapReport.getRows()][mapReport.getColumns()];
		int rowCount = mapReport.getRows(),
			colCount = mapReport.getColumns(),
			i, j;
		for (i=0; i<rowCount; i++) {
			for (j=0; j<colCount; j++) {
				counters[i][j] = 0;
			}
		}
		
		for (MapCell cell:mapReport.getCells()) {
			/*if ((cell.getY() >= mapReport.getRows()) || (cell.getX() >= mapReport.getColumns())) {
				LogUtil.getInstance().log(TAG
						+ "[" +map+","+cell.getX()+","+cell.getY()+"] position has dirty data in tables");
				continue;
			} //[Geoffrey Chen] No need to check it again here. MapReport.queryMapData had already done this. */
			counters[cell.getY()][cell.getX()] = cell.getCount();
		}
		
		out.write("<tr height='40'>\n\t");
		out.write("<td width='40'><b>" + mapReport.getMapId() + "</b></td>\n\t");
		for (j=0; j<colCount; j++) {
			out.write("<td width='40'><b>" + idxTo3Chars(j) + "</b></td>\n\t");
		}		
		out.write("</tr>\n\t");
		
		for (i=0; i<rowCount; i++) {
			out.write("<tr height='40'>\n\t");
			out.write("<td width='40'><b>" + idxTo3Chars(i) + "</b></td>\n\t");
			for (j=0; j<colCount; j++) {
				out.write("<td width='40' title='" + j + "," + i + "'>" + countTo3Chars(counters[i][j]) + "</td>\n\t");
			}
			out.write("</tr>\n\t");
		}
		
		out.write("</table>\n\t");
		
		out.write("</font>\n\t");
		out.write("</body>\n\t");
		out.write("</html>\n\t");
		if (DEBUG) {
			LogUtil.getInstance().log(TAG + "***************** MapReport End ******************");
		}
	}
	
	private String idxTo3Chars(int i) {	
		if (i>99) {
			return "" + i;
		}
		
		if (i>9) {
			return "0" + i;
		}
		
		return "00" + i;
	}
	
	private String countTo3Chars(int i) {
		if (i==0){
			return "";
		}
		
		return "<font color='red'>" + i + "</font>";
	}
}
