package com.winjune.ips.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.winjune.ips.db.MysqlManager;
import com.winjune.ips.model.types.Test;
import com.winjune.ips.settings.WifiIpsSettings;

/**
 * Servlet implementation class TestServlet
 */
public class TestServlet extends HttpServlet {
	public static final String TAG = "TestServlet";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TestServlet() {
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
		
		System.out.println("Get");
		
		Test output = getDataFromDb(1);

		System.out.println("Sent: " + output.getName() + ", "
				+ output.getNote());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		BufferedReader reader = request.getReader();

		Gson gson = new Gson();

		Test myBean = gson.fromJson(reader, Test.class);

		System.out.println("Received: " + myBean.getName() + ", "
				+ myBean.getNote());

		Test output = getDataFromDb(2);

		System.out.println("Sent: " + output.getName() + ", "
				+ output.getNote());

		String json = gson.toJson(output);

		JSONObject jsonObject = new JSONObject(json);

		jsonObject.write(response.getWriter());

		// response.getWriter().write(gson.toString());

	} // vaadin, jquery, dhtmlx

	private Test getDataFromDb(int id) {
		MysqlManager instance = MysqlManager.getInstance();
		if (instance == null) {
			System.out.println(TAG + ", MysqlManager instance is null.");
			return null;
		}

		Connection connection = instance.getConnection();

		if (connection != null) {

			String sql = "select id, name, note" + " from Test where id=?";
			try {
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, id);
				ResultSet rs = statement.executeQuery();
				if (rs.next()) {
					String name = rs.getString(2);
					String note = rs.getString(3);

					Test test = new Test();
					test.setName(name);
					test.setNote(note);
					
					return test;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

}
