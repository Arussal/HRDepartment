/**
 * 
 */
package com.mentat.nine.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;

import com.mentat.nine.dao.ApplicationFormDAO;
import com.mentat.nine.dao.exceptions.DAOException;
import com.mentat.nine.dao.exceptions.NoSuitDAOFactoryException;
import com.mentat.nine.dao.exceptions.NoSuitableDBPropertiesException;
import com.mentat.nine.dao.exceptions.PersistException;
import com.mentat.nine.dao.util.DAOFactory;
import com.mentat.nine.domain.ApplicationForm;

/**
 * @author Ruslan
 *
 */
public class PostgreSQLApplicationFormDAO implements ApplicationFormDAO {

	private Connection connection;
	
	public PostgreSQLApplicationFormDAO() throws NoSuitDAOFactoryException, NoSuitableDBPropertiesException {
		DAOFactory postgreSQLFactory = DAOFactory.getDAOFactory("POSTGRES");
		postgreSQLFactory.loadConnectProperties();
		connection = postgreSQLFactory.createConnection();
	}


	@Override
	public ApplicationForm createApplicitionForm(ApplicationForm af) throws DAOException, PersistException {
		if (af.getId() != null) {
			throw new PersistException("Object is already persist");
		}
		String sql = getCreateQuery();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			prepareStatementForInsert(statement, af);
		} catch (SQLException e) {
			
		}
		return null;
	}

	@Override
	public ApplicationForm readApplicationForm(int id) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateApplicationForm(ApplicationForm af) throws DAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteApplicationForm(ApplicationForm af) throws DAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ApplicationForm> getAllApplicationForms() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCreateQuery() {
		String sql = "INSERT INTO hrdepartment.application_form (date, age, education, requirements, \n"
				+ "post, salary) VALUES (?, ?, ?, ?, ?, ?)";
		return sql;
	}

	private void prepareStatementForInsert(PreparedStatement statement,
			ApplicationForm af) {
		java.sql.Date sqlDate = convert(af.getDate());
		try {
			statement.setDate(1, sqlDate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}


	private java.sql.Date convert(java.util.Date date) {
		java.sql.Date sqlDate = null;
		if (date != null) {
			sqlDate = new java.sql.Date(date.getTime());  
			return sqlDate;
		}
		return null;
	}
}
