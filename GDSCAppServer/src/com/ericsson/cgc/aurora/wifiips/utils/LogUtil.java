package com.ericsson.cgc.aurora.wifiips.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.ericsson.cgc.aurora.wifiips.settings.WifiIpsSettings;

public class LogUtil {
	private static Logger logger;

	private static LogUtil mSingletonInstance = null;

	private LogUtil() {

	}

	public static LogUtil getInstance() {
		if (mSingletonInstance == null) {
			mSingletonInstance = new LogUtil();
			return mSingletonInstance;
		}

		return mSingletonInstance;
	}

	public void initialize() {
		logger = Logger.getLogger("com.ericsson.cgc.aurora.wifiips");
		logger.setLevel(Level.INFO);

		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.INFO);
		logger.addHandler(consoleHandler);

		FileHandler fileHandler;
		try {
			if (WifiIpsSettings.RUNNING_IN_LINUX)
				fileHandler = new FileHandler(WifiIpsSettings.LINUX_LOGGER_FILE);
			else
				fileHandler = new FileHandler(WifiIpsSettings.LOGGER_FILE);
			fileHandler.setLevel(Level.INFO);
			fileHandler.setFormatter(new DefLogHander());
			logger.addHandler(fileHandler);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void log(String message) {
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String date = sdf.format(new Date(currentTime));

		logger.info(date + " " + message);
	}

	public void log(Level level, String message) {
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String date = sdf.format(new Date(currentTime));

		logger.log(level, date + " " + message);
	}

	public class DefLogHander extends Formatter {
		@Override
		public String format(LogRecord record) {
			return record.getLevel() + ":" + record.getMessage() + "\n";
		}
	}
}
