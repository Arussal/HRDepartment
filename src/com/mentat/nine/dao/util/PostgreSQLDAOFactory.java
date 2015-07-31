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
import com.mentat.nine.dao.PostgreSQLCandidateDAO;
import com.mentat.nine.dao.exceptions.NoSuitableDBPropertiesException;
import com.mentat.nine.dao.exceptions.PersistException;
import com.mentat.nine.dao.postgres.PostgreSQLCVFormDAO;
import com.mentat.nine.dao.postgres.PostgreSQLDepartmentDAO;
import com.mentat.nine.dao.postgres.PostgreSQLEmployeeDAO;

/**
 * @author Ruslan
 *
 */
public class PostgreSQLDAOFactory extends DAOFactory {

	@Override
	public void loadConnectProperties() throws NoSuitableDBPropertiesException {
		Properties properties = new Properties();
		
		try(FileInputStream fis = new FileInputStream("/resources/postgres.properties")) {
			properties.load(fis);
			
			user = properties.getProperty("db.user");
			password = properties.getProperty("db.password");
			url = properties.getProperty("db.host");
			driverName = properties.getProperty("db.driver");
		} catch (IOException e) {
			throw new NoSuitableDBPropertiesException();
		}
	}
	
	@Override
	public Connection createConnection() {
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
		return connection;
	}
	
	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.util.DAOFactory#getApplicationFormDAO()
	 */
	@Override
	public ApplicationFormDAO getApplicationFormDAO() throws PersistException{
		return new ApplicationFormDAO();
		
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.util.DAOFactory#getCandidateDAO()
	 */
	@Override
	public CandidateDAO getCandidateDAO() throws PersistException {
		return new PostgreSQLCandidateDAO();
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.util.DAOFactory#getCVFormDAO()
	 */
	@Override
	public CVFormDAO getCVFormDAO() {
		return new PostgreSQLCVFormDAO();
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.util.DAOFactory#getDepartmentDAO()
	 */
	@Override
	public DepartmentDAO getDepartmentDAO() {
		return new PostgreSQLDepartmentDAO();
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.util.DAOFactory#getEmployeeDAO()
	 */
	@Override
	public EmployeeDAO getEmployeeDAO() {
		return new PostgreSQLEmployeeDAO();
	}


}
