/**
 * 
 */
package com.mentat.nine.dao.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.mentat.nine.dao.CVFormDAO;
import com.mentat.nine.dao.DepartmentDAO;
import com.mentat.nine.dao.EmployeeDAO;
import com.mentat.nine.dao.ApplicationFormDAO;
import com.mentat.nine.dao.CandidateDAO;
import com.mentat.nine.dao.exceptions.NoSuitableDBPropertiesException;
import com.mentat.nine.dao.exceptions.PersistException;

/**
 * @author Ruslan
 *
 */
public class DAOFactory {
	
	private static DAOFactory daoFactory;	
	private String user;
	private String password;
	private String url;
	private String driverName;
	private Connection connection;
	
	private DAOFactory(){
		try{
			loadConnectProperties();
		} catch (NoSuitableDBPropertiesException e){
			e.printStackTrace();
		}
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

	
	public void loadConnectProperties() throws NoSuitableDBPropertiesException {
		Properties properties = new Properties();
		
		try(FileInputStream fis = new FileInputStream("resources/postgres.properties")) {
			properties.load(fis);

			user = properties.getProperty("db.user");
			password = properties.getProperty("db.password");
			url = properties.getProperty("db.host");
			driverName = properties.getProperty("db.driver");
		} catch (IOException e) {
			throw new NoSuitableDBPropertiesException();
		}
	}
	
	
	public Connection createConnection() {
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.util.DAOFactory#getApplicationFormDAO()
	 */
	public ApplicationFormDAO getApplicationFormDAO() throws PersistException{
		return new ApplicationFormDAO();
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.util.DAOFactory#getCandidateDAO()
	 */
	public CandidateDAO getCandidateDAO() throws PersistException {
		return new CandidateDAO();
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.util.DAOFactory#getCVFormDAO()
	 */
	public CVFormDAO getCVFormDAO() throws PersistException {
		return new CVFormDAO();
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.util.DAOFactory#getDepartmentDAO()
	 */
	public DepartmentDAO getDepartmentDAO() throws PersistException {
		return new DepartmentDAO();
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.util.DAOFactory#getEmployeeDAO()
	 */
	public EmployeeDAO getEmployeeDAO() throws PersistException {
		return new EmployeeDAO();
	}


}
