/**
 * 
 */
package com.mentat.nine.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mentat.nine.dao.ApplicationFormDAO;
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
	public ApplicationForm createApplicationForm(ApplicationForm af) throws PersistException {
		ApplicationForm appForm = null;
		if (af.getId() != null) {
			throw new PersistException("Object is already persist");
		}
		int id = 0;
		String sqlCreate = getCreateQuery();
		try (PreparedStatement statement = connection.prepareStatement(sqlCreate)) {
			prepareStatementForInsert(statement, af);
			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt("id"); 
			}
		} catch (SQLException e) {
			throw new PersistException();
		}
		//return the last entity
		String sqlSelect = getSelectQuery() + "WHERE id = " + id;
		try (Statement statement = connection.createStatement()) {
			ResultSet rs = statement.executeQuery(sqlSelect);
			List<ApplicationForm> list = parseResultSet(rs);
			if (null == list || list.size() != 1) {
				throw new PersistException();
			}
			appForm = list.get(0);
		} catch (SQLException e) {
			throw new PersistException();
		}
		return appForm;
	}




	@Override
	public ApplicationForm readApplicationForm(int id) throws PersistException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateApplicationForm(ApplicationForm af) throws PersistException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteApplicationForm(ApplicationForm af) throws PersistException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ApplicationForm> getAllApplicationForms() throws PersistException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCreateQuery() {
		String sql = "INSERT INTO hrdepartment.application_form (date, age, education, requirements, \n"
				+ "post, salary) VALUES (?, ?, ?, ?, ?, ?)";
		return sql;
	}

	
	@Override
	public String getSelectQuery() {
		String sql = "SELECT * FROM hrdepartment.application_form";
		return sql;
	}
	
	
	@Override
	public void prepareStatementForInsert(PreparedStatement statement,
			ApplicationForm af) throws PersistException {
		java.sql.Date sqlDate = convertDate(af.getDate());
		String requirements = convertString(af.getRequirements());
		try {
			statement.setDate(1, sqlDate);
			statement.setInt(2, af.getAge());
			statement.setString(3, af.getEducation());
			statement.setString(4, requirements);
			statement.setString(5, af.getPost());
			statement.setInt(6, af.getSalary());
		} catch (SQLException e) {
			throw new PersistException();
		}	
	}

	
	private List<ApplicationForm> parseResultSet(ResultSet rs) throws PersistException {
		List<ApplicationForm> list = new ArrayList<ApplicationForm>();

		try {
			String require = "";
			ApplicationForm appForm = new ApplicationForm();
			while (rs.next()) {
				appForm.setAge(rs.getInt("age"));
				appForm.setDate(rs.getDate("date"));
				appForm.setEducation(rs.getString("education"));
				require = rs.getString("requirements");
				appForm.setPost(rs.getString("post"));
				appForm.setSalary(rs.getInt("salary"));
			}
			String[] requirementArray = require.split(";");
			Set<String> requirements = new HashSet<String>(Arrays.asList(requirementArray));
			appForm.setRequirements(requirements);
			list.add(appForm);
		} catch (SQLException e) {
			throw new PersistException();
		}
		return list;
	}
	
	
	private String convertString(Set<String> set) {
		StringBuilder sb = new StringBuilder();
		for (String s : set) {
			sb.append(s);
		}
		return sb.toString();
	}
	
	private java.sql.Date convertDate(java.util.Date date) {
		java.sql.Date sqlDate = null;
		if (date != null) {
			sqlDate = new java.sql.Date(date.getTime());  
			return sqlDate;
		}
		return null;
	}


}
