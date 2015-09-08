/**
 * 
 */
package main.com.mentat.nine.dao.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.log4j.Logger;

import main.com.mentat.nine.dao.ApplicationFormDAO;
import main.com.mentat.nine.dao.CVFormDAO;
import main.com.mentat.nine.dao.CandidateDAO;
import main.com.mentat.nine.dao.DepartmentDAO;
import main.com.mentat.nine.dao.EmployeeDAO;
import main.com.mentat.nine.dao.ManagerDAO;
import main.com.mentat.nine.dao.exceptions.NoSuitableDBPropertiesException;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.domain.util.LogConfig;

/**
 * @author Ruslan
 *
 */
public class DAOFactory {
	
	static {
		LogConfig.loadLogConfig();
	}
	
	private static Logger log = Logger.getLogger(DAOFactory.class);
	
	// TODO make relative path
	public final static String PATH = "C:\\workspace\\HRDepartment\\resources\\postgres.properties"; 
	
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
	
		try(FileInputStream fis = new FileInputStream(PATH)) {
			properties.load(fis);
			log.trace("file with db properties loaded");
			
			user = properties.getProperty("db.user");
			password = properties.getProperty("db.password");
			url = properties.getProperty("db.host");
			driverName = properties.getProperty("db.driver");
			
			log.trace("properties for db loaded");
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

	public ManagerDAO getManagerDAO() throws PersistException {
		return new ManagerDAO();
	}

}
