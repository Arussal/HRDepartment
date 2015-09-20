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
import java.util.Set;

import org.apache.log4j.Logger;

import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.Closer;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.ApplicationForm;
import main.com.mentat.nine.domain.util.LogConfig;

/**
 * @author Ruslan
 *
 */
public class ApplicationFormDAO {
	
	private static Logger log = Logger.getLogger(ApplicationForm.class);
	
	private DAOFactory daoFactory = null;
	
	public ApplicationFormDAO(String logPath) {
		LogConfig.loadLogConfig(logPath);
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
				if(log.isTraceEnabled()) {
					log.trace("try to check if ApplicationForm with id " + af.getId() + " exists");
				}
				String sqlSelect = getSelectQuery() + " WHERE id = " + af.getId();
				connection = daoFactory.createConnection();
				log.trace("connection created");
				statement = connection.createStatement();
				log.trace("statement created");
				rs = statement.executeQuery(sqlSelect);
				log.trace("resultset got");
				List<ApplicationForm> list = parseResultSet(rs);
				if (list.size() != 0) {
					log.warn("ApplicationForm is already persist, id " + af.getId());
					throw new PersistException("ApplicationForm is already persist, id " + af.getId());
				}
				if(log.isTraceEnabled()){
					log.trace("ApplicationForm with id " + id + " is absent");
				}
			}catch (SQLException e) {
				log.error(" can't check ApplicationForm by id");
				throw new PersistException(" can't check ApplicationForm by id");
			}finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(statement);
			}
			
			// create new ApplicationForm persist
			try {
				log.trace("try to create new entity ApplicationForm");
				String sqlCreate = getCreateQuery();
				pStatement = connection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);
				log.trace("pStatement created");
				prepareStatementForInsert(pStatement, af);
				int count = pStatement.executeUpdate();
				if (1 == count) {
					rs = pStatement.getGeneratedKeys();
					log.trace("generated pStatement keys");
					rs.next();
					id = rs.getInt("id"); 
				} else {
					log.error("new entity ApplicationForm not created");
					throw new PersistException("ApplicationForm hasn't been created");
				}
				log.info("new entity ApplicationForm created, id " + id);
			}catch (SQLException e) {
				log.error("new entity ApplicationForm not created");
				throw new PersistException(" can't create new ApplicationForm");
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(pStatement);
			}
			
			//return the last entity
			try {
				if(log.isTraceEnabled()) {
					log.trace("try to return ApplicationForm entity with id " + id);
				}
				String sqlSelect = getSelectQuery() + " WHERE id = " + id;
				statement = connection.createStatement();
				log.trace("statement created");
				rs = statement.executeQuery(sqlSelect);
				log.trace("resulset got");
				List<ApplicationForm> list = parseResultSet(rs);
				if (null == list || list.size() != 1) {
					log.warn("more than one ApplicationForm with id " + id);
					throw new PersistException("Created more than one ApplicationForm with id = " + id);
				}
				appForm = list.get(0);
				log.info("return new ApplicationForm with id " + id);
				appForm.setId(id);
			}catch (SQLException e) {
				log.error(" can't return new ApplicationForm with id " + id);
				throw new PersistException(" can't return new ApplicationForm");
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(statement);
			}
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
			log.trace("get ApplicationForm with id " + id);
			String sqlSelect = getSelectQuery() + " WHERE id = " + id;
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("resultset got");
			List<ApplicationForm> appFormList = parseResultSet(rs);
			if (appFormList.size() > 1) {
				log.warn("more than one ApplicationForm with id " + id);
				throw new PersistException("Get more than one ApplicationForm with id = " + id);
			} else if (appFormList.size() < 1) {
				log.warn("no AppForm with id " + id);
				throw new PersistException("No ApplicationForm with id = " + id);
			}
			appForm = appFormList.get(0);
			if (log.isTraceEnabled()) {
				log.trace("get ApplicationForm with id " + id);
			}
		} catch (SQLException e) {
			log.error("can't get ApplicationForm with id " + id);
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
		if (null == af) {
			throw new IllegalArgumentException();
		}
		if (null == af.getId()) {
			log.warn("ApplicationForm with id " + af.getId() + " is not persist");
			throw new PersistException("ApplicationForm does not persist yet");
		}
		ApplicationForm selectedApp = this.getApplicationFormById(af.getId());
		if (null == selectedApp) {
			log.warn("ApplicationForm with id " + af.getId() + " is not persist");
			throw new PersistException("ApplicationForm does not persist yet");
		}	
		
		// update it
		String sqlUpdate = getUpdateQuery() + "WHERE id = " + af.getId();
		try {
			if(log.isTraceEnabled()) {
				log.trace("try to update ApplicationForm with id " + af.getId()); 
			}
			connection = daoFactory.createConnection();
			log.trace("create connection");
			pStatement = connection.prepareStatement(sqlUpdate, Statement.RETURN_GENERATED_KEYS);
			log.trace("create pStatement");
			prepareStatementForInsert(pStatement, af);
			int count = pStatement.executeUpdate();
			if (count > 1) {
				log.warn("update more then one ApplicationForm");
				throw new PersistException("It was updated more than one persist: " + count);
			} else if (count < 1) {
				log.warn("no ApplicationForm update");
				throw new PersistException("No one persist was updated");
			}
		} catch (SQLException e) {
			log.error("can't update ApplicationForm");
			throw new PersistException();
		} finally {
			Closer.close(rs, pStatement, connection);
		}
	}


	public void deleteApplicationForm(ApplicationForm af) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		
		// check if there is ApplicationForm entity
		if (null == af) {
			throw new IllegalArgumentException();
		}
		if (null == af.getId()) {
			log.warn("ApplicationForm with id " + af.getId() + " is not persist");
			throw new PersistException("ApplicationForm does not persist yet");
		}
		ApplicationForm selectedApp = this.getApplicationFormById(af.getId());
		if (null == selectedApp) {
			log.warn("ApplicationForm with id " + af.getId() + " is not persist");
			throw new PersistException("ApplicationForm does not persist yet");
		}	
		
		// delete ApplicationForm entity
		String sqlDelete = getDeleteQuery() + " WHERE id = " + af.getId();
		try {
			if (log.isTraceEnabled()) {
				log.trace("try to delete ApplicationForm with id " + af.getId());
			}
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			int count = statement.executeUpdate(sqlDelete);
			if (1 != count) {
				log.warn("delete more than one persist");
				throw new PersistException("On delete modify more then 1 record: " + count);
			}
		} catch (SQLException e) {
			log.error("can't delete ApplicationForm");
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
			log.trace("get all ApplicationForms");
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("get resultset");
			appForms = parseResultSet(rs);
			if (null == appForms || 0 == appForms.size()) {
				log.warn("no one ApplicationForm persist");
				throw new PersistException(" there are not ApplicationForm entities");
			}
		} catch (SQLException e) {
			log.error("can't get all ApplicationForms");
			throw new PersistException();
		} finally {
			Closer.close(rs, statement, connection);
		}
		return appForms;
	}

	
	private String getCreateQuery() {
		String sql = "INSERT INTO application_form (date, age, education, \n"
				+ "requirements, post, salary, work_expirience) VALUES (?, ?, ?, ?, ?, ?, ?)";
		return sql;
	}


	private String getUpdateQuery() {
		String sql = "UPDATE application_form SET date = ?, age = ?, education = ?, \n"
				+ "requirements = ?, post = ?, salary = ?, work_expirience = ?";
		return sql;
	}
	

	private String getSelectQuery() {
		String sql = "SELECT * FROM application_form";
		return sql;
	}
	
	
	private String getDeleteQuery() {
		String sql = "DELETE FROM application_form";
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
			statement.setInt(7, af.getWorkExpirience());
		} catch (SQLException e) {
			log.error("can't create arguments for pStatement");
			throw new PersistException();
		}
		log.trace("create arguments for pStatement");
	}

	
	private List<ApplicationForm> parseResultSet(ResultSet rs) throws PersistException {
		List<ApplicationForm> list = new ArrayList<ApplicationForm>();

		try {
			String require = "";
			while (rs.next()) {
				ApplicationForm appForm = new ApplicationForm();
				appForm.setId(rs.getInt("id"));
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
				log.trace("parsed ApplicationForm entity");
				list.add(appForm);
			}
		} catch (SQLException e) {
			log.error("can't parse query results");
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
