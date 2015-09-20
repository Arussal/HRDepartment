/**
 * 
 */
package main.com.mentat.nine.dao.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import main.com.mentat.nine.dao.ApplicantDAO;
import main.com.mentat.nine.dao.ApplicationFormDAO;
import main.com.mentat.nine.dao.CVFormApplicantDAO;
import main.com.mentat.nine.dao.CVFormDAO;
import main.com.mentat.nine.dao.CandidateDAO;
import main.com.mentat.nine.dao.DepartmentDAO;
import main.com.mentat.nine.dao.EmployeeDAO;
import main.com.mentat.nine.dao.ManagerDAO;
import main.com.mentat.nine.dao.exceptions.NoSuitableDBPropertiesException;


/**
 * @author Ruslan
 *
 */
public class DAOFactory {
			
	private static DAOFactory daoFactory;	
	private Connection connection;	

	private static String user;
	private static String password;
	private static String url;
	private static String driverName;
	
	private DAOFactory(){	
	}
	
	public static void loadConnectProperties(Properties properties) 
			throws NoSuitableDBPropertiesException {
							
		user = properties.getProperty("db.user");
		password = properties.getProperty("db.password");
		url = properties.getProperty("db.host");
		driverName = properties.getProperty("db.driver");

		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}
		
	public static DAOFactory getFactory(){
		if (null == daoFactory) {
			daoFactory = new DAOFactory();
		} 
		return daoFactory;
	}

		
	public Connection createConnection() {
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	public ApplicationFormDAO getApplicationFormDAO() {
		return new ApplicationFormDAO();
	}

	public CandidateDAO getCandidateDAO() {
		return new CandidateDAO();
	}

	public CVFormDAO getCVFormDAO() {
		return new CVFormDAO();
	}

	public DepartmentDAO getDepartmentDAO() {
		return new DepartmentDAO();
	}

	public EmployeeDAO getEmployeeDAO() {
		return new EmployeeDAO();
	}

	public ManagerDAO getManagerDAO() {
		return new ManagerDAO();
	}
	
	public ApplicantDAO getApplicantDAO() {
		return new ApplicantDAO();
	}

	public CVFormApplicantDAO getCVFormApplicantDAO() {
		return new CVFormApplicantDAO();
	}

}
