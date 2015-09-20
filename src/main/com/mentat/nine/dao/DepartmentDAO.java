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

import org.apache.log4j.Logger;

import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.Closer;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.Department;
import main.com.mentat.nine.domain.util.LogConfig;

/**
 * @author Ruslan
 *
 */
public class DepartmentDAO{

	static {
		LogConfig.loadLogConfig();
	}
	private static Logger log = Logger.getLogger(DepartmentDAO.class);
	
	private DAOFactory daoFactory = null;
	
	public DepartmentDAO() {
		daoFactory = DAOFactory.getFactory();
	}

	public Department createDepartment(Department department) throws PersistException {
		
		Department createdDepartment = null;
		Connection connection = null;
		Statement statement = null;
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		int id = 0;

		try {
			
			//check if this Department does not persist
			try {
				if(log.isTraceEnabled()) {
					log.trace("try to check if Department with id " + department.getId() + " exists");
				}
				String sqlSelect = getSelectQuery() + " WHERE id = " + department.getId();
				connection = daoFactory.createConnection();
				log.trace("connection created");
				statement = connection.createStatement();
				log.trace("statement created");
				rs = statement.executeQuery(sqlSelect);
				log.trace("resultset got");
				List<Department> departments = parseResultSet(rs);
				if (departments.size() != 0) {
					log.warn("Department is already persist, id " + department.getId());
					throw new PersistException("Department is already persist, id " + 
													department.getId());
				} 
				if(log.isTraceEnabled()){
					log.trace("Department with id " + id + " is absent");
				}
			} catch (SQLException e) {
				log.error(" can't check Department by id");
				throw new PersistException(" can't check Department with id " + department.getId());
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(statement);
			}
			
			// create new Department persist
			try {
				log.trace("try to create new entity Department");
				String sqlCreate = getCreateQuery();
				pStatement = connection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);
				log.trace("pStatement created");
				prepareStatementForInsert(pStatement, department);
				int count = pStatement.executeUpdate();
				if (1 == count) {
					rs = pStatement.getGeneratedKeys();
					log.trace("generated pStatement keys");
					rs.next();
					id = rs.getInt("id");
				} else {
					log.error("new entity Department not created");
					throw new PersistException("Department hasn't been created");
				}
				log.info("new entity Department created, id " + id);
			} catch (SQLException e) {
				log.error("new entity Department not created");
				throw new PersistException(" can't create Department with id " + id);
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(pStatement);
			}
			
			//return the last entity
			try {
				if(log.isTraceEnabled()) {
					log.trace("try to get Department entity with id " + id);
				}
				String sqlSelect = getSelectQuery() + " WHERE id = " + id;
				statement = connection.createStatement();
				log.trace("statement created");
				rs = statement.executeQuery(sqlSelect);
				log.trace("resulset got");
				List<Department> departments = parseResultSet(rs);
				if (null == departments || departments.size() != 1) {
					log.warn("more than one Department with id " + id);
					throw new PersistException("Was created more than one persist with id = " + id);
				}
				createdDepartment = departments.get(0);
				log.info("return new Department with id " + id);
			} catch (SQLException e) {
				log.error(" can't return new Department with id " + id);
				throw new PersistException(" can't return new Department with id " + id);
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(statement);
			}
		} finally {
			Closer.closeConnection(connection);
		}
		
		return createdDepartment;
	}

	public Department getDepartmentById(int id) throws PersistException {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<Department> departments = null;
		
		Department department = new Department();

		try {
			log.trace("try to get Department with id " + id);
			String sqlSelect = getSelectQuery() + " WHERE id = " + id;
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("resultset got");
			departments = parseResultSet(rs);
			if (departments.size() > 1) {
				log.warn("more than one Department with id " + id);
				throw new PersistException("Get more than one Department with id = " + id);
			} else if (departments.size() < 1) {
				log.warn("no Department with id " + id);
				throw new PersistException("No Department with id = " + id);
			}
			department = departments.get(0);
			if (log.isTraceEnabled()) {
				log.trace("get Department with id " + id);
			}
		} catch (SQLException e) {
			log.error("can't get Department with id " + id);
			throw new PersistException(" can't get Department by id " + id);
		} finally {
			Closer.close(rs, statement, connection);
		}
		return department;
	}
	
	public Department getDepartmentByName(String name) throws PersistException {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<Department> departments = null;
		
		Department department = new Department();

		try {
			log.trace("get Departments with post " + name);
			String sqlSelect = getSelectQuery() + " WHERE name = '" + name + "'";
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("resultset got");
			departments = parseResultSet(rs);
			if (departments.size() > 1) {
				log.warn("no Departments with post " + name);
				throw new PersistException("Get more than one Department with name = " + name);
			} else if (departments.size() < 1) {
				log.warn("no Department with id " + name);
				throw new PersistException("No Department with name = " + name);
			}
			department = departments.get(0);	
		} catch (SQLException e) {
			log.error("can't get Departments with post " + name);
			throw new PersistException(" can't get Department by name " + name);
		} finally {
			Closer.close(rs, statement, connection);
		}
		return department;
	}


	public List<Department> getDepartmentsByHead(String head) throws PersistException {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<Department> departments = null;
		
		try {
			String sqlSelect = getSelectQuery() + " WHERE head = '" + head + "'";
			log.trace("get Department with id " + head);
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("resultset got");
			departments = parseResultSet(rs);
			if (departments.size() < 1) {
				log.warn("no Department with id " + head);
				throw new PersistException("No Departments with head = " + head);
			}
			if (log.isTraceEnabled()) {
				log.trace("get Departments with head " + head);
			}
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			Closer.close(rs, statement, connection);
		}
		
		return departments;
	}


	public void updateDepartment(Department department) throws PersistException {
		
		Connection connection = null;
		PreparedStatement pStatement = null;
		
		// check if there is Department entry
		if (null == department) {
			throw new IllegalArgumentException();
		}
		if (null == department.getId()) {
			log.warn("Department with id " + department.getId() + " is not persist");
			throw new PersistException("Department does not persist yet");
		}
		Department selectedDepartment = this.getDepartmentById(department.getId());
		if (null == selectedDepartment) {
			log.warn("Department with id " + department.getId() + " is not persist");
			throw new PersistException("Department does not persist yet");
		}			
			
		
		// update Department entry
		try {
			if (log.isTraceEnabled()) {
				log.trace("try to update Department with id " + department.getId());
			}
			String sqlUpdate = getUpdateQuery() + " WHERE id = " + department.getId();
			connection = daoFactory.createConnection();
			log.trace("create connection");
			pStatement = connection.prepareStatement(sqlUpdate, Statement.RETURN_GENERATED_KEYS);
			log.trace("create pStatement");
			int count = pStatement.executeUpdate();
			if (count > 1) {
				throw new PersistException("Updated more than one Department: " + count);
			} else if (count < 1) {
				throw new PersistException("No one Department was updated");
			}
		} catch (SQLException e) {
			log.error("can't update Department");
			throw new PersistException();
		} finally {
			Closer.closeStatement(pStatement);
			Closer.closeConnection(connection);
		}
		
	}

	public void deleteDepartment(Department department) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		
		// check if there is Department entity
		if (null == department) {
			throw new IllegalArgumentException();
		}
		if (null == department.getId()) {
			log.warn("Department with id " + department.getId() + " is not persist");
			throw new PersistException("Department does not persist yet");
		}
		Department selectedDepartment = this.getDepartmentById(department.getId());
		if (null == selectedDepartment) {
			log.warn("Department with id " + department.getId() + " is not persist");
			throw new PersistException("Department does not persist yet");
		}			
				
		try {
			if (log.isTraceEnabled()) {
				log.trace("try to delete Department with id " + department.getId());
			}
			String sqlDelete = getDeleteQuery() + " WHERE id = " + department.getId(); 
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
			log.error("can't delete Department");
			throw new PersistException();
		} finally {
			Closer.closeStatement(statement);
			Closer.closeConnection(connection);
		}
	}


	public List<Department> getAllDepartments() throws PersistException {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<Department> departments = null;

		try {
			log.trace("get all Departments");
			String sqlSelect = getSelectQuery();
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("get resultset");
			departments = parseResultSet(rs);
			if (null == departments || 0 == departments.size()) {
				log.warn("no one Department persist");
				throw new PersistException(" there are not Department entities");
			}
		} catch (SQLException e) {
			log.error("can't get all Departments");
			throw new PersistException();
		} finally {
			Closer.close(rs, statement, connection);
		}
		return departments;
		
	}

	private String getCreateQuery() {
		String sql = "INSERT INTO department (name, head) VALUES (?, ?)";
		return sql;
	}


	private String getUpdateQuery() {
		String sql = "UPDATE department SET name = ?, head = ?";
		return sql;
	}
	

	private String getSelectQuery() {
		String sql = "SELECT * FROM department";
		return sql;
	}
	
	
	private String getDeleteQuery() {
		String sql = "DELETE FROM department";
		return sql;
	}
	
	private List<Department> parseResultSet(ResultSet rs) throws PersistException {
		List<Department> departments = new ArrayList<Department>();

		try {
			while (rs.next()) {
				Department department = new Department();
				department.setId(rs.getInt("id"));
				department.setName(rs.getString("name"));
				department.setHead(rs.getString("head"));
				log.trace("parsed Department entity");
				departments.add(department);
			}
		} catch (SQLException e) {
			log.error("can't parse query results");
			throw new PersistException();
		}
		return departments;
	}
	
	/**
	 * @param statement
	 * @param department
	 * @throws PersistException
	 */
	private void prepareStatementForInsert(PreparedStatement statement,
			Department department) throws PersistException {
		try {
			statement.setString(1, department.getName());
			statement.setString(2, department.getHead());
		} catch (SQLException e) {
			log.error("can't create arguments for pStatement");
			throw new PersistException();
		}
		log.trace("create arguments for pStatement");
	}
	
		
}
