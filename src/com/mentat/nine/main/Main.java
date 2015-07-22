/**
 * 
 */
package com.mentat.nine.main;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.mentat.nine.Employee;
import com.mentat.nine.dbase.DBConnectMaker;

/**
 * @author Ruslan
 *
 */
public class Main {

	/**
	 * 
	 */
	public Main() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String user = "postgres";
		String password = "cfnnfh21091990";
		String url = "jdbc:postgresql://localhost:5432/HRDepartment";
		String driverName = "org.postgresql.Driver";
		
		Employee emp = null;
		Set<Employee> employees = new HashSet<Employee>();
		
		DBConnectMaker dbcm = new DBConnectMaker(user, password, url, driverName);
		dbcm.doConnect();
		Connection conn = dbcm.getConnection();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("select * from employees");
			while(rs.next()) {
				emp = new Employee();
				emp.setName(rs.getString(2));
				emp.setAge(rs.getInt(3));
				emp.setEducation(rs.getString(4));
				employees.add(emp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} 
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		for (Employee e : employees) {
			System.out.println(e.getName());
		}
	}
}
