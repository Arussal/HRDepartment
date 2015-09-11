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
import java.util.List;

import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.Closer;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.Applicant;
import main.com.mentat.nine.domain.util.LogConfig;

import org.apache.log4j.Logger;

/**
 * @author Ruslan
 *
 */
public class ApplicantDAO {

	static {
		LogConfig.loadLogConfig();
	}
	
	private static Logger log = Logger.getLogger(ManagerDAO.class);
	private DAOFactory daoFactory; 
	
	/**
	 * 
	 */
	public ApplicantDAO() {
		daoFactory = DAOFactory.getFactory();
	}

		
	public Applicant createApplicant(Applicant applicant) throws PersistException{
		
		Connection connection = null;
		Statement statement = null;
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		Applicant createdApplicant = null;
		
		
		try {
			//check if this Manager already persist
			try {
				String selectSql = getSelectQuery() + " WHERE login = " + "'" + applicant.getLogin() + "'";
				connection = daoFactory.createConnection();
				statement = connection.createStatement();
				rs = statement.executeQuery(selectSql);
				List<Applicant> applicants = parseResultSet(rs);
				if (applicants.size() > 0) {
					log.warn("more than one Applicant with login " + applicant.getLogin());
					throw new PersistException("Applicant with login = " + applicant.getLogin() + " is already persist");
				}
				if(log.isTraceEnabled()){
					log.trace("Applicant with login: " + applicant.getLogin() + " is absent");
				}
			} catch (SQLException e) {
				throw new PersistException();
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(statement);
			}
			
			Integer id = null;
			//create new Manager entry
			try {
				String createSql = getCreateQuery();
				pStatement = connection.prepareStatement(createSql, Statement.RETURN_GENERATED_KEYS);
				preparedStatementForInsert(applicant, pStatement);
				int count = pStatement.executeUpdate();
				if (1 == count) {
					rs = pStatement.getGeneratedKeys();
					rs.next();
					id = rs.getInt("id");
				}
			} catch (SQLException e) {
				log.error("cant create new Applicant entry");
				throw new PersistException();
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(pStatement);
			}
			
				
			//return created Manager entry
			try {
				String selectSql = getSelectQuery() + " WHERE id = " + id;
				statement = connection.createStatement();
				rs = statement.executeQuery(selectSql);
				List<Applicant> applicants = parseResultSet(rs);
				if (applicants.size() != 1) {
					log.error("return more than one Applicant entry");
					throw new PersistException();
				}
				createdApplicant = applicants.get(0);
				createdApplicant.setId(id);
				log.info("new entry Applicant created, id " + id);
			} catch (SQLException e) {
				log.error("cant return Applicant entry");
				throw new PersistException();
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(statement);
			}
		} finally {
			Closer.closeConnection(connection);
		}
		return createdApplicant;
	}
	
	
	public Applicant getApplicantByLogin(String login) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		Applicant selectedApplicant = null;
		
		try {
			String selectSql = getSelectQuery() + " WHERE login = " + "'" + login + "'"; 
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(selectSql);
			List<Applicant> applicants = parseResultSet(rs);
			if (1 != applicants.size()) {
				log.error("return not the only Applicant: " + applicants.size());
				throw new PersistException();
			}
			selectedApplicant = applicants.get(0);
		} catch (SQLException e) {
			log.error("cant return Applicant whith login: " + login);
		} finally {
			Closer.close(rs, statement, connection);
		}
		return selectedApplicant;
	}
	
	
	public void updateApplicant(Applicant applicant) throws PersistException {
		
		//check if there Manager persist
		if (null == applicant) {
			throw new IllegalArgumentException();
		}
		if (null == applicant.getId()) {
			log.warn("Applicant with id " + applicant.getId() + " is not persist");
			throw new PersistException("Applicant does not persist yet");
		}
		Applicant selectedApplicant = this.getApplicantByLogin(applicant.getLogin());
		if (null == selectedApplicant) {
			log.warn("Applicant with id " + applicant.getId() + " is not persist");
			throw new PersistException("Applicant does not persist yet");
		}	
		
		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String updateSql = getUpdateQuery() + " WHERE login = " + "'" + applicant.getLogin() + "'";
		
		try {
			connection = daoFactory.createConnection();
			pStatement = connection.prepareStatement(updateSql);
			preparedStatementForInsert(applicant, pStatement);
			int count = pStatement.executeUpdate();
			if (1 != count) {
				log.error("Updated " + count + " Applicant entries");
				throw new PersistException("Updated " + count + " Applicant entries");
			}			
		} catch (SQLException e) {
			log.error("can't update Applicant entry");
			throw new PersistException("can't update Applicant entry");
		} finally {
			Closer.close(rs, pStatement, connection);
		}
	}
	
	
	public void deleteApplicant(Applicant applicant) throws PersistException {
		
		//check if there Manager persist
		if (null == applicant) {
			throw new IllegalArgumentException();
		}
		if (null == applicant.getId()) {
			log.warn("Applicant with id " + applicant.getId() + " is not persist");
			throw new PersistException("Applicant does not persist yet");
		}
		Applicant selectedManager = this.getApplicantByLogin(applicant.getLogin());
		if (null == selectedManager) {
			log.warn("Applicant with id " + applicant.getId() + " is not persist");
			throw new PersistException("Applicant does not persist yet");
		}
		
		Connection connection = null;
		Statement statement = null;
		String deleteSql = getDeleteQuery() + " WHERE login = '" + applicant.getLogin() + "'";
		
		try { 							
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			int count = statement.executeUpdate(deleteSql);
			if (1 != count) {
				log.error("Deleted " + count + " Applicant entries");
				throw new PersistException("Deleted " + count + " Applicant entries");
			}			
		} catch (SQLException e) {
			log.error("can't delete Applicant entry");
			throw new PersistException("can't delete Applicant entry");
		} finally {
			Closer.closeStatement(statement);
			Closer.closeConnection(connection);
		}
	}
	
	
	public List<Applicant> getAllApplicants() throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<Applicant> applicants = new ArrayList<Applicant>();
		
		try {
			String selectSql = getSelectQuery();
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(selectSql);
			List<Applicant> parsedList = parseResultSet(rs);
			if (parsedList != null) {
				applicants = parsedList;
			}
			if (log.isTraceEnabled()) {
				log.trace("get all applicants");
			}
		} catch (SQLException e) {
			log.error("cant get all applicants");
			throw new PersistException();
		} finally {
			Closer.close(rs, statement, connection);
		}		
		return applicants;
	}


	public String getSelectQuery() {
		return "SELECT * FROM applicant";
	}
	
	
	public String getCreateQuery() {
		return "INSERT INTO applicant (login, password, name) VALUES (?, ?, ?)";
	}
	
	
	public String getUpdateQuery() {
		return "UPDATE applicant SET login = ?, password = ?, name = ?";
	}
	
	
	public String getDeleteQuery() {
		return "DELETE FROM applicant";
	}
	

	private List<Applicant> parseResultSet(ResultSet rs) throws PersistException {
		
		List<Applicant> applicants = new ArrayList<Applicant>();
		try {
			while(rs.next()) {
				Applicant applicant = new Applicant();
				applicant.setId(rs.getInt("id"));
				applicant.setLogin(rs.getString("login"));
				applicant.setPassword(rs.getString("password"));
				applicant.setName(rs.getString("name"));
				applicants.add(applicant);
			}
		} catch (SQLException e) {
			log.error("can't parse query results");
			throw new PersistException();
		}
		return applicants;
	}
	
	
	private void preparedStatementForInsert(Applicant applicant, PreparedStatement statement) 
			throws PersistException {
		
		try {
			statement.setString(1, applicant.getLogin());
			statement.setString(2, applicant.getPassword());
			statement.setString(3, applicant.getName());
		} catch (SQLException e) {
			log.error("can't prepare statement");
			throw new PersistException();
		}
	}

}