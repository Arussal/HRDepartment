/**
 * 
 */
package com.mentat.nine.dao.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * @author Ruslan
 *
 */
public class DBConnectMaker {

	private String user;
	private String password;
	private String url;
	private String driverName;
	private Connection connection;
	
	public void loadConnectProperties(String propertyPath) {
		Properties properties = new Properties();
		
		try(FileInputStream fis = new FileInputStream(propertyPath)) {
			properties.load(fis);
			
			user = properties.getProperty("db.user");
			password = properties.getProperty("db.password");
			url = properties.getProperty("db.host");
			driverName = properties.getProperty("db.driver");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void doConnect() {
		
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		if (null == connection) {
			throw new NullPointerException();
		}
		return connection;
	}
}
