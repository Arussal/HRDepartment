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
public class Closer {

	public static void closeResultSet(ResultSet rs) {
		if (null != rs) {
			try {
				rs.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	public static void closeStatement(Statement st) {
		if (null != st) {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
	}

	public static void closeConnection(Connection connection) {
		if (null != connection) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void close(ResultSet rs, Statement st, Connection connection) {
		closeResultSet(rs);
		closeStatement(st);
		closeConnection(connection);
	}
}
