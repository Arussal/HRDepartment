/**
 * 
 */
package com.mentat.nine.dao.util;


import java.sql.Connection;



import com.mentat.nine.dao.ApplicationFormDAO;
import com.mentat.nine.dao.CVFormDAO;
import com.mentat.nine.dao.CandidateDAO;
import com.mentat.nine.dao.DepartmentDAO;
import com.mentat.nine.dao.EmployeeDAO;
import com.mentat.nine.dao.exceptions.NoSuitDAOFactoryException;
import com.mentat.nine.dao.exceptions.NoSuitableDBPropertiesException;


/**
 * @author Ruslan
 *
 */
public abstract class DAOFactory {

	protected String user;
	protected String password;
	protected String url;
	protected String driverName;
	protected Connection connection;
	
	public abstract ApplicationFormDAO getApplicationFormDAO();
	public abstract CandidateDAO getCandidateDAO();
	public abstract CVFormDAO getCVFormDAO();
	public abstract DepartmentDAO getDepartmentDAO();
	public abstract EmployeeDAO getEmployeeDAO();
	
	public abstract void loadConnectProperties() throws NoSuitableDBPropertiesException;
	public abstract Connection createConnection();

	public static DAOFactory getDAOFactory(String factory) throws NoSuitDAOFactoryException {
		if (factory.equalsIgnoreCase("postgres")) { 
			return new PostgreSQLDAOFactory();
		} else {
			throw new NoSuitDAOFactoryException();
		}
	}
}
