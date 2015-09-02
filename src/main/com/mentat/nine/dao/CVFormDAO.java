/**
 * 
 */
package main.com.mentat.nine.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.Closer;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.CVForm;
import main.com.mentat.nine.domain.util.LogConfig;

/**
 * @author Ruslan
 *
 */
public class CVFormDAO {
	
	static {
		LogConfig.loadLogConfig();
	}
	private static Logger log = Logger.getLogger(CVFormDAO.class);
	
	private DAOFactory daoFactory = null;
	
	public CVFormDAO() throws PersistException{
		daoFactory = DAOFactory.getFactory();
	}

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
				if(log.isTraceEnabled()) {
					log.trace("try check if CVForm with id " + cv.getId() + " exists");
				}
				String sqlSelect = getSelectQuery() + " WHERE id = " + cv.getId();
				connection = daoFactory.createConnection();
				log.trace("connection created");
				statement = connection.createStatement();
				log.trace("statement created");
				rs = statement.executeQuery(sqlSelect);
				log.trace("resultset got");
				List<CVForm> cvList = parseResultSet(rs);
				if (cvList.size() != 0) {
					log.warn("CVForm is already persist, id " + cv.getId());
					throw new PersistException("CVForm is already persist, id " + cv.getId());
				} 
				if(log.isTraceEnabled()){
					log.trace("CVForm with id " + id + " is absent");
				}
			} catch (SQLException e) {
				log.error(" can't check CVForm by id");
				throw new PersistException(" can't check CVForm with id " + cv.getId());
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(statement);
			}
			
			// create new CVForm persist
			try {
				log.trace("try to create new entity CVForm");
				String sqlCreate = getCreateQuery();
				pStatement = connection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);
				log.trace("pStatement created");
				prepareStatementForInsert(pStatement, cv);
				int count = pStatement.executeUpdate();
				if (1 == count) {
					rs = pStatement.getGeneratedKeys();
					log.trace("generated pStatement keys");
					rs.next();
					id = rs.getInt("id"); 
				} else {
					log.error("new entity CVForm not created");
					throw new PersistException("CVForm hasn't been created");
				}
				log.info("new entity CVForm created, id " + id);
			} catch (SQLException e) {
				log.error("new entity CVForm not created");
				throw new PersistException(" can't create CVForm with id " + id);
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(pStatement);
			}
			
			//return the last entity
			try {
				if(log.isTraceEnabled()) {
					log.trace("try to return CVForm entity with id " + id);
				}
				String sqlSelect = getSelectQuery() + " WHERE id = " + id;
				statement = connection.createStatement();
				log.trace("statement created");
				rs = statement.executeQuery(sqlSelect);
				log.trace("resulset got");
				List<CVForm> cvForms = parseResultSet(rs);
				if (null == cvForms || cvForms.size() != 1) {
					log.warn("more than one CVForm with id " + id);
					throw new PersistException("Was created more than one persist with id = " + id);
				}
				createdCV = cvForms.get(0);
				log.info("return new CVForm with id " + id);
			} catch (SQLException e) {
				log.error(" can't return new CVForm with id " + id);
				throw new PersistException(" can't return new CVForm with id " + id);
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(statement);
			}
		} finally {
			Closer.closeConnection(connection);
		}
		
		return createdCV;
	}

	
	public CVForm getCVFormById(int id) throws PersistException {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<CVForm> cvList = null;
		
		CVForm cv = new CVForm();

		try {
			log.trace("try to get CVForm with id " + id);
			String sqlSelect = getSelectQuery() + " WHERE id = " + id;
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("resultset got");
			cvList = parseResultSet(rs);
			if (cvList.size() > 1) {
				log.warn("more than one CVForm with id " + id);
				throw new PersistException("Get more than one CVForm with id = " + id);
			} else if (cvList.size() < 1) {
				log.warn("no CVForm with id " + id);
				throw new PersistException("No CVForm with id = " + id);
			}
			cv = cvList.get(0);
			if (log.isTraceEnabled()) {
				log.trace("get CVForm with id " + id);
			}
		} catch (SQLException e) {
			log.error("can't get CVForm with id " + id);
			throw new PersistException(" can't get CVForm by id " + id);
		} finally {
			Closer.close(rs, statement, connection);
		}
		return cv;
	}

	
	public List<CVForm> getCVFormByPost(String post) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<CVForm> cvForms = null;
		
		try {
			log.trace("get CVForms with post " + post);
			String sqlSelect = getSelectQuery() + " WHERE post = " + post;
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("resultset got");
			cvForms = parseResultSet(rs);
			if (cvForms.size() < 1) {
				log.warn("no CVForms with post " + post);
				throw new PersistException("No CVForms with post = " + post);
			}
		} catch (SQLException e) {
			log.error("can't get CVForms with post " + post);
			throw new PersistException();
		} finally {
			Closer.close(rs, statement, connection);
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
			log.trace("get CVForms with workExpirience " + workExpirience);
			String sqlSelect = getSelectQuery() + " WHERE work_expirience = " + workExpirience;
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("resultset got");
			cvForms = parseResultSet(rs);
			if (cvForms.size() < 1) {
				log.warn("no CVForms with workExpirience " + workExpirience);
				throw new PersistException("No CVForms with workExpirience = " + workExpirience);
			}
		} catch (SQLException e) {
			log.error("can't get CVForms with workExpirience " + workExpirience);
			throw new PersistException();
		} finally {
			Closer.close(rs, statement, connection);
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
			log.trace("get CVForms with education " + education);
			String sqlSelect = getSelectQuery() + " WHERE education = " + education;
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("resultset got");
			cvForms = parseResultSet(rs);
			if (cvForms.size() < 1) {
				log.warn("no CVForms with education " + education);
				throw new PersistException("No CVForms with education = " + education);
			}
		} catch (SQLException e) {
			log.error("can't get CVForms with workExpirience " + education);
			throw new PersistException();
		} finally {
			Closer.close(rs, statement, connection);
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
			log.trace("get CVForms with desiredSalary " + desiredSalary);
			String sqlSelect = getSelectQuery() + " WHERE desiredSalary = " + desiredSalary;
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("resultset got");
			cvForms = parseResultSet(rs);
			if (cvForms.size() < 1) {
				log.warn("no CVForms with desiredSalary " + desiredSalary);
				throw new PersistException("No CVForms with desiredSalary = " + desiredSalary);
			}
		} catch (SQLException e) {
			log.error("can't get CVForms with desiredSalary " + desiredSalary);
			throw new PersistException();
		} finally {
			Closer.close(rs, statement, connection);
		}
		
		return cvForms;
	}

	
	public List<CVForm> getCVForm(Map<String, List<String>> queries) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<CVForm> cvForms = null;
		
		try {
			log.trace("get CVForms with different query parameters");
			StringBuilder selectBuilder = new StringBuilder();
			String selectSql = "";
			String selectPhrase = getSelectQuery();
			selectBuilder.append(selectPhrase);
			if (queries.size() != 0) {
				selectBuilder.append(" WHERE ");
				for (String key : queries.keySet()) {
					selectBuilder.append(key);
					if (queries.get(key).get(0) == null) {
						selectBuilder.append(" is null");
						selectSql = selectBuilder.toString();
					} else {
						selectBuilder.append(queries.get(key).get(1)+"'");
						selectBuilder.append(queries.get(key).get(0));
						selectBuilder.append("'");
						selectBuilder.append(" AND ");
						selectSql = selectBuilder.substring(0, selectBuilder.length()-5);
					}
				}
			} else {
				selectSql = selectBuilder.toString();
			}
			
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(selectSql);
			log.trace("resultset got");
			cvForms = parseResultSet(rs);
			if (cvForms.size() < 1) {
				log.warn("no CVForms with different query parameters");
				throw new PersistException("No CVForms with different query parameters");
			}
		} catch (SQLException e) {
			log.error("can't get CVForms with different query parameters");
			throw new PersistException();
		} finally {
			Closer.close(rs, statement, connection);
		}
		
		return cvForms;
	}

	public void updateCVForm(CVForm cv) throws PersistException {

		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		
		// check if there is CVForm entity
		if (null == cv) {
			throw new IllegalArgumentException();
		}
		if (null == cv.getId()) {
			log.warn("CVForm with id " + cv.getId() + " is not persist");
			throw new PersistException("CVForm does not persist yet");
		}
		CVForm selectedApp = this.getCVFormById(cv.getId());
		if (null == selectedApp) {
			log.warn("CVForm with id " + cv.getId() + " is not persist");
			throw new PersistException("CVForm does not persist yet");
		}	
		
		// update CVForm entity
		String sqlUpdate = getUpdateQuery() + " WHERE id = " + cv.getId();
		try {
			if(log.isTraceEnabled()) {
				log.trace("try to update CVForm with id " + cv.getId()); 
			}
			connection = daoFactory.createConnection();
			log.trace("create connection");
			pStatement = connection.prepareStatement(sqlUpdate, Statement.RETURN_GENERATED_KEYS);
			log.trace("create pStatement");
			prepareStatementForInsert(pStatement, cv);
			int count = pStatement.executeUpdate();
			if (count > 1) {
				log.warn("update more then one CVForm");
				throw new PersistException("Updated more than one CVForm");
			} else if (count < 1) {
				log.warn("no CVForm update");
				throw new PersistException("No one CVForm was updated");
			}
		} catch (SQLException e) {
			log.error("can't update CVForm");
			throw new PersistException();
		} finally {
			Closer.close(rs, pStatement, connection);
		}
		
	}


	public void deleteCVForm(CVForm cv) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		
		// check if there is CVForm entity
		if (null == cv) {
			throw new IllegalArgumentException();
		}
		if (null == cv.getId()) {
			log.warn("CVForm with id " + cv.getId() + " is not persist");
			throw new PersistException("CVForm does not persist yet");
		}
		CVForm selectedCVForm = this.getCVFormById(cv.getId());
		if (null == selectedCVForm) {
			log.warn("CVForm with id " + cv.getId() + " is not persist");
			throw new PersistException("CVForm does not persist yet");
		}	
		
		// delete CVForm entity

		try {
			if (log.isTraceEnabled()) {
				log.trace("try to delete CVForm with id " + cv.getId());
			}
			String sqlDelete = getDeleteQuery() + " WHERE id = " + cv.getId();
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			int count = statement.executeUpdate(sqlDelete);
			if (1 != count) {
				log.warn("delete more than one entity");
				throw new PersistException("On delete modify more then 1 record: " + count);
			}
		} catch (SQLException e) {
			log.error("can't delete CVForm");
			throw new PersistException();
		} finally {
			Closer.closeStatement(statement);
			Closer.closeConnection(connection);
		}
		
	}


	public List<CVForm> getAllCVForms() throws PersistException {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<CVForm> cvForms = null;
		String sqlSelect = getSelectQuery();
		
		try {
			log.trace("try to get all CVForms");
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("get resultset");
			cvForms = parseResultSet(rs);
			if (null == cvForms || 0 == cvForms.size()) {
				log.warn("no one CVForm persist");
				throw new PersistException(" there are not CVForm entities");
			}
		} catch (SQLException e) {
			log.error("can't get all CVForms");
			throw new PersistException();
		} finally {
			Closer.close(rs, statement, connection);
		}
		log.trace("return Set of cvForms");
		return cvForms;
	}

	private String getCreateQuery() {
		String sql = "INSERT INTO cvform (name, age, education, email, phone, \n"
				+ "post, skills, work_expirience, desired_salary, additional_info) \n"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return sql;
	}


	private String getUpdateQuery() {
		String sql = "UPDATE cvform SET name = ?, age = ?, education = ?, \n"
				+ "email = ?, phone = ?, skills = ?, work_expirience = ?, desired_salary = ?, \n"
				+ "additional_info = ?";
		return sql;
	}
	

	private String getSelectQuery() {
		String sql = "SELECT * FROM cvform";
		return sql;
	}
	
	
	private String getDeleteQuery() {
		String sql = "DELETE FROM cvform";
		return sql;
	}
	
	private List<CVForm> parseResultSet(ResultSet rs) throws PersistException {
		List<CVForm> cvForms = new ArrayList<CVForm>();

		try {
			String skill = "";
			while (rs.next()) {
				CVForm cv = new CVForm();
				cv.setId(rs.getInt("id"));
				cv.setName(rs.getString("name"));
				if (rs.getString("age") == null) {
					cv.setAge(null);
				} else {
					cv.setAge(rs.getInt("age"));
				}
				cv.setEducation(rs.getString("education"));
				cv.setEmail(rs.getString("email"));
				cv.setPhone(rs.getString("phone"));
				cv.setPost(rs.getString("Post"));
				skill = rs.getString("skills");
				if (rs.getString("work_expirience") == null) {
					cv.setWorkExpirience(null);
				} else {
					cv.setWorkExpirience(rs.getInt("work_expirience"));
				}
				if (rs.getString("desired_salary") == null) {
					cv.setDesiredSalary(null);
				} else {
					cv.setDesiredSalary(rs.getInt("desired_salary"));
				}
				cv.setAdditionalInfo(rs.getString("additional_info"));
				String[] skillArray = skill.split(";");
				Set<String> skills = new HashSet<String>(Arrays.asList(skillArray));
				cv.setSkills(skills);
				log.trace("parsed CVForm entity");
				cvForms.add(cv);
			}
		} catch (SQLException e) {
			log.error("can't parse query results");
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
			log.error("can't create arguments for pStatement");
			throw new PersistException();
		}	
		log.trace("create arguments for pStatement");
	}
	
	private String convertString(Set<String> set) {
		StringBuilder sb = new StringBuilder();
		for (String s : set) {
			sb.append(s);
			sb.append(";");
		}
		return sb.toString();
	}

}
