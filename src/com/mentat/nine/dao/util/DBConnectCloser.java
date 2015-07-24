/**
 * 
 */
package com.mentat.nine.dao.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Ruslan
 *
 */
public class DBConnectCloser {

	public static void resultSetClose(ResultSet rs) {
		if (null != rs) {
			try {
				rs.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	public static void statementClose(Statement st) {
		if (null != st) {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
	}

	public static void connectionClose(Connection connection) {
		if (null != connection) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void close(ResultSet rs, Statement st, Connection connection) {
		resultSetClose(rs);
		statementClose(st);
		connectionClose(connection);
	}
}
