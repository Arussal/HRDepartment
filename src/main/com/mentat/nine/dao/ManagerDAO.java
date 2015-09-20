package main.com.mentat.nine.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.Closer;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.Manager;
import main.com.mentat.nine.domain.util.LogConfig;

public class ManagerDAO {
	
	private static Logger log = Logger.getLogger(ManagerDAO.class);
	private DAOFactory daoFactory; 
	
	public ManagerDAO(String logPath) {
		LogConfig.loadLogConfig(logPath);
		daoFactory = DAOFactory.getFactory();
	}
	
	public Manager createManager(Manager manager) throws PersistException{
		
		Connection connection = null;
		Statement statement = null;
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		Manager createdManager = null;
		
		
		try {
			//check if this Manager already persist
			try {
				String selectSql = getSelectQuery() + " WHERE login = " + "'" + manager.getLogin() + "'";
				connection = daoFactory.createConnection();
				statement = connection.createStatement();
				rs = statement.executeQuery(selectSql);
				List<Manager> managers = parseResultSet(rs);
				if (managers.size() > 0) {
					log.warn("more than one Manager with login " + manager.getLogin());
					throw new PersistException("Manager with login = " + manager.getLogin() + " is already persist");
				}
				if(log.isTraceEnabled()){
					log.trace("Manager with login: " + manager.getLogin() + " is absent");
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
				preparedStatementForInsert(manager, pStatement);
				int count = pStatement.executeUpdate();
				if (1 == count) {
					rs = pStatement.getGeneratedKeys();
					rs.next();
					id = rs.getInt("id");
				}
			} catch (SQLException e) {
				log.error("cant create new Manager entry");
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
				List<Manager> managers = parseResultSet(rs);
				if (managers.size() != 1) {
					log.error("return more than one Manager entry");
					throw new PersistException();
				}
				createdManager = managers.get(0);
				createdManager.setId(id);
				log.info("new entry Manager created, id " + id);
			} catch (SQLException e) {
				log.error("cant return Manager entry");
				throw new PersistException();
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(statement);
			}
		} finally {
			Closer.closeConnection(connection);
		}
		return createdManager;
	}
	
	
	public Manager getManagerByLogin(String login) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		Manager selectedManager = null;
		
		try {
			String selectSql = getSelectQuery() + " WHERE login = " + "'" + login + "'"; 
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(selectSql);
			List<Manager> managers = parseResultSet(rs);
			if (1 != managers.size()) {
				log.error("return not the only Manager: " + managers.size());
				throw new PersistException();
			}
			selectedManager = managers.get(0);
		} catch (SQLException e) {
			log.error("cant return Manager whith login: " + login);
		} finally {
			Closer.close(rs, statement, connection);
		}
		return selectedManager;
	}
	
	
	public void updateManager(Manager manager) throws PersistException {
		
		//check if there Manager persist
		if (null == manager) {
			throw new IllegalArgumentException();
		}
		if (null == manager.getId()) {
			log.warn("Manager with id " + manager.getId() + " is not persist");
			throw new PersistException("Manager does not persist yet");
		}
		Manager selectedManager = this.getManagerByLogin(manager.getLogin());
		if (null == selectedManager) {
			log.warn("Manager with id " + manager.getId() + " is not persist");
			throw new PersistException("Manager does not persist yet");
		}	
		
		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String updateSql = getUpdateQuery() + " WHERE login = " + "'" + manager.getLogin() + "'";
		
		try {
			connection = daoFactory.createConnection();
			pStatement = connection.prepareStatement(updateSql);
			preparedStatementForInsert(manager, pStatement);
			int count = pStatement.executeUpdate();
			if (1 != count) {
				log.error("Updated " + count + " Manager entries");
				throw new PersistException("Updated " + count + " Manager entries");
			}			
		} catch (SQLException e) {
			log.error("can't update Manager entry");
			throw new PersistException("can't update Manager entry");
		} finally {
			Closer.close(rs, pStatement, connection);
		}
	}
	
	
	public void deleteManager(Manager manager) throws PersistException {
		
		//check if there Manager persist
		if (null == manager) {
			throw new IllegalArgumentException();
		}
		if (null == manager.getId()) {
			log.warn("Manager with id " + manager.getId() + " is not persist");
			throw new PersistException("Manager does not persist yet");
		}
		Manager selectedManager = this.getManagerByLogin(manager.getLogin());
		if (null == selectedManager) {
			log.warn("Manager with id " + manager.getId() + " is not persist");
			throw new PersistException("Manager does not persist yet");
		}
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		String deleteSql = getDeleteQuery() + " WHERE login = '" + manager.getLogin() + "'";
		System.out.println(deleteSql);
		
		try {
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			int count = statement.executeUpdate(deleteSql);
			if (1 != count) {
				log.error("Deleted " + count + " Manager entries");
				throw new PersistException("Deleted " + count + " Manager entries");
			}			
		} catch (SQLException e) {
			log.error("can't delete Manager entry");
			throw new PersistException("can't delete Manager entry");
		} finally {
			Closer.close(rs, statement, connection);
		}
	}
	
	
	public List<Manager> getAllManagers() throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<Manager> managers = null;
		
		try {
			String selectSql = getSelectQuery();
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(selectSql);
			managers = parseResultSet(rs);
			if (log.isTraceEnabled()) {
				log.trace("get all managers");
			}
		} catch (SQLException e) {
			log.error("cant get all managers");
			throw new PersistException();
		} finally {
			Closer.close(rs, statement, connection);
		}		
		return managers;
	}


	public String getSelectQuery() {
		return "SELECT * FROM manager";
	}
	
	
	public String getCreateQuery() {
		return "INSERT INTO manager (login, password) VALUES (?, ?)";
	}
	
	
	public String getUpdateQuery() {
		return "UPDATE manager SET login = ?, password = ?";
	}
	
	
	public String getDeleteQuery() {
		return "DELETE FROM manager";
	}
	

	private List<Manager> parseResultSet(ResultSet rs) throws PersistException {
		
		List<Manager> managers = new ArrayList<Manager>();
		try {
			while(rs.next()) {
				Manager manager = new Manager();
				manager.setId(rs.getInt("id"));
				manager.setLogin(rs.getString("login"));
				manager.setPassword(rs.getString("password"));
				managers.add(manager);
			}
		} catch (SQLException e) {
			log.error("can't parse query results");
			throw new PersistException();
		}
		return managers;
	}
	
	
	private void preparedStatementForInsert(Manager manager, PreparedStatement statement) 
			throws PersistException {
		
		try {
			statement.setString(1, manager.getLogin());
			statement.setString(2, manager.getPassword());
		} catch (SQLException e) {
			log.error("can't prepare statement");
			throw new PersistException();
		}
	}

}
