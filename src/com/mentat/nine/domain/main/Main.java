/**
 * 
 */
package com.mentat.nine.domain.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import com.mentat.nine.dao.EmployeeDAO;
import com.mentat.nine.dao.exceptions.PersistException;
import com.mentat.nine.dao.util.DAOFactory;
import com.mentat.nine.domain.ApplicationForm;
import com.mentat.nine.domain.CVForm;
import com.mentat.nine.domain.Candidate;
import com.mentat.nine.domain.Department;
import com.mentat.nine.domain.Employee;
import com.mentat.nine.domain.HRDepartment;
import com.mentat.nine.domain.exceptions.NoSuitableCandidateException;

/**
 * @author Ruslan
 *
 */
public class Main {

	/**
	 * @param args
	 * @throws NoSuitableCandidateException 
	 * @throws PersistException 
	 */
	public static void main(String[] args) throws NoSuitableCandidateException, PersistException {
		
		DAOFactory daoFactory = DAOFactory.getFactory();
		HRDepartment hr = new HRDepartment();
		Candidate candidate = new Candidate();
		EmployeeDAO employeeDao = daoFactory.getEmployeeDAO();
		Department department = new Department();
		Set<Employee> employees = null;
		department.setHead("Head 1");
		department.setName("Department 1");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse("2003-08-15");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Set<String> requirements = new HashSet<String>();
		requirements.add("requirement one");
		try {
			//employees = employeeDao.getAllEmployees();
			//department.setEmployees(employees);
			CVForm cvform = candidate.createCVForm("Antonov Stepa", 26, requirements, "high", "0987", "email", 2400, "additionalInfo", "engineer", 6);
			candidate.sendCVForm(cvform, hr);
			ApplicationForm af = hr.createApplicationForm(27, "high", requirements, "engineer", 2500, 5, date);
			candidate = hr.findCandidate(af);
			Employee employee = hr.hireEmployee(candidate, 3100, "great post", null, department);
			
			
		} catch (PersistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 
	}

}
