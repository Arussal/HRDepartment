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
import com.mentat.nine.dao.util.DAOFactory;
import com.mentat.nine.domain.CVForm;

/**
 * @author Ruslan
 *
 */
public class CVFormDAO {
	
	DAOFactory postgreSQLFactory = null;


	public CVForm createCVForm(CVForm cv) throws PersistException {
		
		CVForm createdCV = null;
		Connection connection = null;
		Statement statement = null;
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		int id = 0;

		try {
			
			//check if this CVForm does not persist
			try {
				String sqlSelect = getSelectQuery() + " WHERE id = " + cv.getId();
				connection = postgreSQLFactory.createConnection();
				statement = connection.createStatement();
				rs = statement.executeQuery(sqlSelect);
				List<CVForm> cvList = parseResultSet(rs);
				if (cvList.size() != 0) {
					throw new PersistException("CVForm is already persist, id " + cv.getId());
				} 
			} finally {
				closeResultSet(rs);
				closeStatement(statement);
			}
			
			// create new CVForm persist
			try {
				id = 0;
				String sqlCreate = getCreateQuery();
				pStatement = connection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);
				prepareStatementForInsert(pStatement, cv);
				rs = pStatement.executeQuery();
				if (rs.next()) {
					id = rs.getInt("id"); 
				} else {
					throw new PersistException("Candidate hasn't been created");
				}
			} finally {
				closeResultSet(rs);
				closeStatement(pStatement);
			}
			
			//return the last entity
			try {
				String sqlSelect = getSelectQuery() + " WHERE id = " + id;
				statement = connection.createStatement();
				rs = statement.executeQuery(sqlSelect);
				List<CVForm> cvForms = parseResultSet(rs);
				if (null == cvForms || cvForms.size() != 1) {
					throw new PersistException("Was created more than one persist with id = " + id);
				}
			createdCV = cvForms.get(0);
			} finally {
				closeResultSet(rs);
				closeStatement(statement);
			}
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			closeConnection(connection);
		}
		
		return createdCV;
	}


	public List<CVForm> getCVFormByPost(String post) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<CVForm> cvForms = null;
		
		try {
			connection = postgreSQLFactory.createConnection();
			String sqlSelect = getSelectQuery() + " WHERE post = " + post;
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			cvForms = parseResultSet(rs);
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			close(connection, statement, rs);
		}
		
		return cvForms;
	}


	public List<CVForm> geyCVFormByWorkExpirience(int workExpirience)
			throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<CVForm> cvForms = null;
		
		try {
			connection = postgreSQLFactory.createConnection();
			String sqlSelect = getSelectQuery() + " WHERE work_expirience = " + workExpirience;
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			cvForms = parseResultSet(rs);
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			close(connection, statement, rs);
		}
		
		return cvForms;
	}


	public List<CVForm> getCVFormByEducation(String education)
			throws PersistException {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<CVForm> cvForms = null;
		
		try {
			connection = postgreSQLFactory.createConnection();
			String sqlSelect = getSelectQuery() + " WHERE education = " + education;
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			cvForms = parseResultSet(rs);
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			close(connection, statement, rs);
		}
		
		return cvForms;
	}


	public List<CVForm> geyCVFormByDesiredSalary(int desiredSalary)
			throws PersistException {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<CVForm> cvForms = null;
		
		try {
			connection = postgreSQLFactory.createConnection();
			String sqlSelect = getSelectQuery() + " WHERE desired_salary = " + desiredSalary;
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			cvForms = parseResultSet(rs);
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			close(connection, statement, rs);
		}
		
		return cvForms;
	}


	public void updateCVForm(CVForm cv) throws PersistException {

		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		
		// check if there is CVForm entity
		if (null == cv.getId()) {
			throw new PersistException("CVForm does not persist yet");
		}
		
		// update CVForm entity
		String sqlUpdate = getUpdateQuery() + "WHERE id = " + cv.getId();
		try {
			connection = postgreSQLFactory.createConnection();
			pStatement = connection.prepareStatement(sqlUpdate, Statement.RETURN_GENERATED_KEYS);
			prepareStatementForInsert(pStatement, cv);
			int count = pStatement.executeUpdate();
			if (1 != count) {
				throw new PersistException("Updated more than one CVForm");
			}
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			close(connection, pStatement, rs);
		}
		
	}


	public void deleteCVForm(CVForm cv) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		
		// check if there is CVForm entity
		if (null == cv.getId()) {
			throw new PersistException("CVForm does not persist");
		}
		
		// delete Candidate entity
		String sqlDelete = getDeleteQuery() + " WHERE id = " + cv.getId();
		try {
			connection = postgreSQLFactory.createConnection();
			statement = connection.createStatement();
			int count = statement.executeUpdate(sqlDelete);
			if (count != 1) {
				throw new PersistException("On delete modify more then 1 record: " + count);
			}
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			closeStatement(statement);
			closeConnection(connection);
		}
		
	}


	public List<CVForm> getAllCVForms() throws PersistException {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<CVForm> cvForms = null;
		
		try {
			connection = postgreSQLFactory.createConnection();
			String sqlSelect = getSelectQuery();
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			cvForms = parseResultSet(rs);
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			close(connection, statement, rs);
		}
		
		return cvForms;
	}

	private String getCreateQuery() {
		String sql = "INSERT INTO hrdepartment.cvform (name, age, education, email, phone, \n"
				+ "post, skills, work_expirience, desired_salalry, additional_info) \n"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return sql;
	}


	private String getUpdateQuery() {
		String sql = "UPDATE hrdepartment.cvform SET name = ?, age = ?, education = ?, \n"
				+ "email = ?, phone = ?, skills, work_expirience = ?, desired_salary = ?, \n"
				+ "additional_info = ?, VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return sql;
	}
	

	private String getSelectQuery() {
		String sql = "SELECT * FROM hrdepartment.cvform";
		return sql;
	}
	
	
	private String getDeleteQuery() {
		String sql = "DELETE FROM hrdepartment.cvform";
		return sql;
	}
	
	private List<CVForm> parseResultSet(ResultSet rs) throws PersistException {
		List<CVForm> cvForms = new ArrayList<CVForm>();

		try {
			String skill = "";
			CVForm cv = new CVForm();
			while (rs.next()) {
				cv.setName(rs.getString("name"));
				cv.setAge(rs.getInt("age"));
				cv.setEducation(rs.getString("education"));
				cv.setEmail(rs.getString("email"));
				cv.setPhone(rs.getString("phone"));
				cv.setPost(rs.getString("Post"));
				skill = rs.getString("skills");
				cv.setWorkExpirience(rs.getInt("work_expirience"));
				cv.setDesiredSalary(rs.getInt("desired_salary"));
				cv.setAdditionalInfo(rs.getString("additional_info"));
				String[] skillArray = skill.split(";");
				Set<String> skills = new HashSet<String>(Arrays.asList(skillArray));
				cv.setSkills(skills);
				cvForms.add(cv);
			}
		} catch (SQLException e) {
			throw new PersistException();
		}
		return cvForms;
	}
	
	private void prepareStatementForInsert(PreparedStatement statement,
			CVForm cv) throws PersistException {
		String skills = convertString(cv.getSkills());
		try {
			statement.setString(1, cv.getName());
			statement.setInt(2, cv.getAge());
			statement.setString(3, cv.getEducation());
			statement.setString(4, cv.getEmail());
			statement.setString(5, cv.getPhone());
			statement.setString(6, cv.getPost());
			statement.setString(7, skills);
			statement.setInt(8, cv.getWorkExpirience());
			statement.setInt(9, cv.getDesiredSalary());
			statement.setString(10, cv.getAdditionalInfo());
		} catch (SQLException e) {
			throw new PersistException();
		}	
	}
	
	private String convertString(Set<String> set) {
		StringBuilder sb = new StringBuilder();
		for (String s : set) {
			sb.append(s);
			sb.append(";");
		}
		return sb.toString();
	}

	private void closeResultSet(ResultSet rs) throws PersistException {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			throw new PersistException();
		}
	}
	
	private void closeStatement(Statement statement) throws PersistException {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			throw new PersistException();
		}
	}
	
	private void closeConnection(Connection connection) throws PersistException {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			throw new PersistException();
		}
	}
		
	private void close(Connection connection, Statement statement, ResultSet rs) 
			throws PersistException {
		closeResultSet(rs);
		closeStatement(statement);
		closeConnection(connection);
	}
}
