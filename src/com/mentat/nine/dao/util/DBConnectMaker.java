/**
 * 
 */
package com.mentat.nine.dao.util;

import java.sql.Connection;
import java.sql.DriverManager;

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
	
	
	public DBConnectMaker(String user, String password, String url, String driverName) {
		super();
		this.user = user;
		this.password = password;
		this.url = url;
		this.driverName = driverName;
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
