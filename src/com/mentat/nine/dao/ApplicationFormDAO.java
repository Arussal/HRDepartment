/**
 * 
 */
package com.mentat.nine.dao;

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

import com.mentat.nine.dao.exceptions.PersistException;
import com.mentat.nine.dao.util.Closer;
import com.mentat.nine.dao.util.DAOFactory;
import com.mentat.nine.domain.ApplicationForm;

/**
 * @author Ruslan
 *
 */
public class ApplicationFormDAO {


	private DAOFactory daoFactory = null;
	
	public ApplicationFormDAO() throws PersistException{
		daoFactory = DAOFactory.getFactory();
	}


	public ApplicationForm createApplicationForm(ApplicationForm af) throws PersistException {
		
		ApplicationForm appForm = null;
		Connection connection = null;
		Statement statement = null;
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		int id = 0;

		try {
			
			//check if this ApplicationForm does not persist
			try {
				String sqlSelect = getSelectQuery() + " WHERE id = " + af.getId();
				connection = daoFactory.createConnection();
				statement = connection.createStatement();
				rs = statement.executeQuery(sqlSelect);
				List<ApplicationForm> list = parseResultSet(rs);
				if (list.size() != 0) {
					throw new PersistException("ApplicationForm is already persist, id " + af.getId());
				} 
			}finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(statement);
			}
			
			// create new ApplicationForm persist
			try {
				String sqlCreate = getCreateQuery();
				pStatement = connection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);
				prepareStatementForInsert(pStatement, af);
				int count = pStatement.executeUpdate();
				if (1 == count) {
					rs = pStatement.getGeneratedKeys();
					rs.next();
					id = rs.getInt("id"); 
				} else {
					throw new PersistException("ApplicationForm hasn't been created");
				}
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(pStatement);
			}
			
			//return the last entity
			try {
				String sqlSelect = getSelectQuery() + " WHERE id = " + id;
				statement = connection.createStatement();
				rs = statement.executeQuery(sqlSelect);
				List<ApplicationForm> list = parseResultSet(rs);
				if (null == list || list.size() != 1) {
					throw new PersistException("Created more than one ApplicationForm with id = " + id);
				}
				appForm = list.get(0);
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(statement);
			}
			
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			Closer.closeConnection(connection);
		}
		
		return appForm;
	}
		
	
	public ApplicationForm getApplicationFormById(int id) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		
		ApplicationForm appForm = new ApplicationForm();
		
		try {
			String sqlSelect = getSelectQuery() + "WHERE id = " + id;
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			List<ApplicationForm> appFormList = parseResultSet(rs);
			if (appFormList.size() > 1) {
				throw new PersistException("Get more than one ApplicationForm with id = " + id);
			} else if (appFormList.size() < 1) {
				throw new PersistException("No ApplicationForm with id = " + id);
			}
			appForm = appFormList.get(0);
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			Closer.close(rs, statement, connection);
		}
		return appForm;
	}

	
	public void updateApplicationForm(ApplicationForm af) throws PersistException {
		 
		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		
		// check if there is ApplicationForm entity
		if (null == af.getId()) {
			throw new PersistException("Object is not persist yet");
		}
		
		// update it
		String sqlUpdate = getUpdateQuery() + "WHERE id = " + af.getId();
		try {
			connection = daoFactory.createConnection();
			pStatement = connection.prepareStatement(sqlUpdate, Statement.RETURN_GENERATED_KEYS);
			prepareStatementForInsert(pStatement, af);
			int count = pStatement.executeUpdate();
			if (count > 1) {
				throw new PersistException("It was updated more than one persist");
			} else if (count < 1) {
				throw new PersistException("No one persist was updated");
			}
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			Closer.close(rs, pStatement, connection);
		}
	}


	public void deleteApplicationForm(ApplicationForm af) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		
		// check if there is ApplicationForm entity
		if (null == af.getId()) {
			throw new PersistException("ApplicationForm does not persist yet");
		}
		
		// delete ApplicationForm entity
		String sqlDelete = getDeleteQuery() + " WHERE id = " + af.getId();
		try {
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			int count = statement.executeUpdate(sqlDelete);
			if (1 != count) {
				throw new PersistException("On delete modify more then 1 record: " + count);
			}
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			Closer.closeStatement(statement);
			Closer.closeConnection(connection);;
		}
	}


	public List<ApplicationForm> getAllApplicationForms() throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		
		List<ApplicationForm> appForms = null;
		String sqlSelect = getSelectQuery();
		try { 
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			appForms = parseResultSet(rs);
			if (null == appForms || 0 == appForms.size()) {
				throw new PersistException(" there are not ApplicationForm entities");
			}
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			Closer.close(rs, statement, connection);
		}
		return appForms;
	}

	
	private String getCreateQuery() {
		String sql = "INSERT INTO hrdepartment.application_form (date, age, education, \n"
				+ "requirements, post, salary) VALUES (?, ?, ?, ?, ?, ?)";
		return sql;
	}


	private String getUpdateQuery() {
		String sql = "UPDATE hrdepartment.application_form SET date = ?, age = ?, education = ?, \n"
				+ "requirements = ?, post = ?, salary = ?";
		return sql;
	}
	

	private String getSelectQuery() {
		String sql = "SELECT * FROM hrdepartment.application_form";
		return sql;
	}
	
	
	private String getDeleteQuery() {
		String sql = "DELETE FROM hrdepartment.application_form";
		return sql;
	}
		

	private void prepareStatementForInsert(PreparedStatement statement,
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
			while (rs.next()) {
				ApplicationForm appForm = new ApplicationForm();
				appForm.setAge(rs.getInt("age"));
				appForm.setDate(rs.getDate("date"));
				appForm.setEducation(rs.getString("education"));
				require = rs.getString("requirements");
				appForm.setPost(rs.getString("post"));
				appForm.setSalary(rs.getInt("salary"));
				appForm.setWorkExpirience(rs.getInt("work_Expirience"));
				String[] requirementArray = require.split(";");
				Set<String> requirements = new HashSet<String>(Arrays.asList(requirementArray));
				appForm.setRequirements(requirements);
				list.add(appForm);
			}
		} catch (SQLException e) {
			throw new PersistException();
		}
		return list;
	}
	
	private String convertString(Set<String> set) {
		StringBuilder sb = new StringBuilder();
		for (String s : set) {
			sb.append(s);
			sb.append(";");
		}
		return sb.toString();
	}
	
	private java.sql.Date convertDate(java.util.Date date) {
		if (null == date) {
			throw new IllegalArgumentException(" incorrect date argument");
		}
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());  
		return sqlDate;
	}

}
