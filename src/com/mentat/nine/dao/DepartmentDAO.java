/**
 * 
 */
package com.mentat.nine.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mentat.nine.dao.exceptions.PersistException;
import com.mentat.nine.dao.util.DAOFactory;
import com.mentat.nine.domain.Department;
import com.mentat.nine.domain.Employee;

/**
 * @author Ruslan
 *
 */
public class DepartmentDAO{

	
	DAOFactory postgreSQLFactory = null;
	

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
				String sqlSelect = getSelectQuery() + " WHERE id = " + department.getId();
				connection = postgreSQLFactory.createConnection();
				statement = connection.createStatement();
				rs = statement.executeQuery(sqlSelect);
				List<Department> departments = parseResultSet(rs);
				if (departments.size() != 0) {
					throw new PersistException("Department is already persist, id " + 
													department.getId());
				} 
			} finally {
				closeResultSet(rs);
				closeStatement(statement);
			}
			
			// create new Department persist
			try {
				String sqlCreate = getCreateQuery();
				pStatement = connection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);
				prepareStatementForInsert(pStatement, department);
				int count = pStatement.executeUpdate();
				if (1 == count) {
					rs = pStatement.getGeneratedKeys();
					rs.next();
					id = rs.getInt("id");
				} else {
					throw new PersistException("Department hasn't been created");
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
				List<Department> departments = parseResultSet(rs);
				if (null == departments || departments.size() != 1) {
					throw new PersistException("Was created more than one persist with id = " + id);
				}
				createdDepartment = departments.get(0);
			} finally {
				closeResultSet(rs);
				closeStatement(statement);
			}
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			closeConnection(connection);
		}
		
		return createdDepartment;
	}


	public Department getDepartmentByName(String name) throws PersistException {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<Department> departments = null;
		
		Department department = new Department();

		try {
			String sqlSelect = getSelectQuery() + " WHERE name = " + name;
			connection = postgreSQLFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			departments = parseResultSet(rs);
			if (departments.size() > 1) {
				throw new PersistException("Get more than one Department with name = " + name);
			} else if (departments.size() < 1) {
				throw new PersistException("No Department with name = " + name);
			}
			department = departments.get(0);	
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			close(connection, statement, rs);
		}
		return department;
	}


	public List<Department> getDepartmentsByHead(String head) throws PersistException {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<Department> departments = null;
		
		try {
			String sqlSelect = getSelectQuery() + " WHERE head = " + head;
			connection = postgreSQLFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			departments = parseResultSet(rs);
			if (departments.size() < 1) {
				throw new PersistException("No Departments with head = " + head);
			}
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			close(connection, statement, rs);
		}
		
		return departments;
	}


	public void updateDepartment(Department department) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		
		try {
			String sqlUpdate = getUpdateQuery() + " WHERE id = " + department.getId();
			connection = postgreSQLFactory.createConnection();
			statement = connection.createStatement();
			int count = statement.executeUpdate(sqlUpdate);
			if (count > 1) {
				throw new PersistException("Updated more than one Department");
			} else if (count < 1) {
				throw new PersistException("No one Department was updated");
			}
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			closeStatement(statement);
			closeConnection(connection);
		}
		
	}


	public void deleteDepartment(Department department) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		
		try {
			String sqlDelete = getDeleteQuery() + " WHERE id = " + department.getId(); 
			connection = postgreSQLFactory.createConnection();
			statement = connection.createStatement();
			int count = statement.executeUpdate(sqlDelete);
			if (1 != count) {
				throw new PersistException("On delete modify more then 1 record: " + count);
			}
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			closeStatement(statement);
			closeConnection(connection);
		}
	}


	public List<Department> getAllDepartments() throws PersistException {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<Department> departments = null;

		try {
			String sqlSelect = getSelectQuery();
			connection = postgreSQLFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			departments = parseResultSet(rs);
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			close(connection, statement, rs);
		}
		return departments;
		
	}

	private String getCreateQuery() {
		String sql = "INSERT INTO hrdepartment.department (name, head, employees) \n"
				+ "VALUES (?, ?, ?)";
		return sql;
	}


	private String getUpdateQuery() {
		String sql = "UPDATE hrdepartment.departament SET name = ?, head = ?, employees = ?, \n"
				+ "VALUES (?, ?, ?)";
		return sql;
	}
	

	private String getSelectQuery() {
		String sql = "SELECT * FROM hrdepartment.department";
		return sql;
	}
	
	
	private String getDeleteQuery() {
		String sql = "DELETE FROM hrdepartment.department";
		return sql;
	}
	
	private List<Department> parseResultSet(ResultSet rs) throws PersistException {
		List<Department> departments = new ArrayList<Department>();

		try {
			while (rs.next()) {
				Department department = new Department();
				department.setName(rs.getString("name"));
				department.setHead(rs.getString("head"));
				String employeesString = rs.getString("employees");
				Set<Employee> employees = convertEmployee(employeesString, department);
				department.setEmployees(employees);
				departments.add(department);
			}
		} catch (SQLException e) {
			throw new PersistException();
		}
		return departments;
	}
	
	private void prepareStatementForInsert(PreparedStatement statement,
			Department department) throws PersistException {
		String employees = convertString(department.getEmployees());
		try {
			statement.setString(1, department.getName());
			statement.setString(2, department.getHead());
			statement.setString(3, employees);
		} catch (SQLException e) {
			throw new PersistException();
		}	
	}
	
	private String convertString(Set<Employee> set) {
		StringBuilder sb = new StringBuilder();
		for (Employee employee : set) {
			sb.append(employee.toString());
			sb.append("|");
		}
		return sb.toString();
	}
	
	private Set<Employee> convertEmployee(String line, Department department) 
			throws PersistException {
		Set<Employee> employees = new HashSet<Employee>();
		String[] employeeArray = line.split("|");
		for (int i = 0; i < employeeArray.length; i++) {
			Employee employee = new Employee();
			String[] attributes = employeeArray[i].split(", ");
			for (int j = 0; j < attributes.length; j++) {
				String[] attribute = attributes[j].split("=");
				if (attribute[0].equals("post")) {
					employee.setPost(attribute[1]);
				} else if (attribute[0].equals("hireDate")) {
					employee.setHireDate(convertDate(attribute[1]));
				} else if (attribute[0].equals("salary")) {
					employee.setSalary(Integer.parseInt(attribute[1]));
				} else if (attribute[0].equals("department")) {
					employee.setDepartment(department);
				} else if (attribute[0].equals("education")) {
					employee.setEducation(attribute[1]);
				} else if (attribute[0].equals("name")) {
					employee.setName(attribute[1]);
				} else if (attribute[0].equals("age")) {
					employee.setAge(Integer.parseInt(attribute[1]));
				} else if (attribute[0].equals("skills")) {
					employee.setSkills(convertSet(attribute[1], ";"));
				} else if (attribute[0].equals("phone")) {
					employee.setPhone(attribute[1]);
				} else if (attribute[0].equals("email")) {
					employee.setEmail(attribute[1]);
				}
			}
			employees.add(employee);
		}
		return employees;
	}

	private Date convertDate(String line) throws PersistException {
		Date date;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(line);
		} catch (ParseException e) {
			throw new PersistException(" incorrect Date");
		}
		return date;
	}
	
	private Set<String> convertSet(String line, String dilimiter) {
		String[] stringArray = line.split(dilimiter);
		Set<String> set = new HashSet<String>(Arrays.asList(stringArray));
		return set;
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
