package com.winjune.ips.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.winjune.ips.db.MysqlManager;

public class WifiIpsServletContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		MysqlManager instance = MysqlManager.getInstance();
		if (instance == null) {
			System.out.println("MysqlManager instance is null.");
			return;
		}

		instance.closeConnection();
		System.out.println("Mysql connection is closed.");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		// TODO Auto-generated method stub
		MysqlManager instance = MysqlManager.getInstance();
		if (instance == null) {
			System.out.println("MysqlManager instance is null.");
			return;
		}

		LogUtil logInstance = LogUtil.getInstance();
		if (logInstance == null) {
			System.out.println("LogUtil instance is null.");
			return;
		}

		logInstance.initialize();

		LogUtil.getInstance().log(
				"*****************WiFIIPS Server is Started******************");
	}

}
