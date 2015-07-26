/**
 * 
 */
package com.mentat.nine.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Set;

import com.mentat.nine.dao.util.DBConnectMaker;
import com.mentat.nine.domain.ApplicationForm;
import com.mentat.nine.domain.CVForm;
import com.mentat.nine.domain.Candidate;
import com.mentat.nine.domain.Department;
import com.mentat.nine.domain.Employee;
import com.mentat.nine.domain.HRManager;
import com.mentat.nine.domain.exceptions.NoSuchEmployeeException;
import com.mentat.nine.domain.exceptions.NoSuitableCandidateException;

/**
 * @author Ruslan
 *
 */
public class HRDepartmentDAO implements HRManager{

	private DBConnectMaker dbcm;
	private String propertyPath;
	/**
	 * 
	 */
	public HRDepartmentDAO(String propertyPath) {
		dbcm = new DBConnectMaker();
		this.propertyPath = propertyPath;
	}

	@Override
	public ApplicationForm createApplicationForm(int age, String education,
			Set<String> requirements, String post, int salary, Date date) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ApplicationForm af = null;
		String sql = "insert into application_forms () values ("
		
		try {
			dbcm.loadConnectProperties(propertyPath);
			dbcm.doConnect();
			conn = dbcm.getConnection();
			try {
				ps = conn.prepareStatement(sql);
			}
		}
				
		
		return null;
	}

	@Override
	public Candidate findCandidate(ApplicationForm app)
			throws NoSuitableCandidateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee hireEmployee(Candidate candidate, int salary, String post,
			Date date, Department department) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fireEmployee(Employee employee, Date fireDate)
			throws NoSuchEmployeeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeSalary(Employee employee, int salary)
			throws NoSuchEmployeeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changePost(Employee employee, String post)
			throws NoSuchEmployeeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCVForm(CVForm form) {
		// TODO Auto-generated method stub
		
	}

}
