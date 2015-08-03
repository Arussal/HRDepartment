package com.mentat.nine.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mentat.nine.dao.exceptions.PersistException;
import com.mentat.nine.dao.util.DAOFactory;
import com.mentat.nine.dao.util.PostgreSQLDAOFactory;
import com.mentat.nine.domain.Candidate;
import com.mentat.nine.domain.Department;
import com.mentat.nine.domain.Employee;

public class PostgreSQLEmployeeDAO implements EmployeeDAO {

	DAOFactory postgresDAOFactory = null;
	
	@Override
	public Employee createEmployee(Employee employee) throws PersistException {

		Connection connection = null;
		Statement statement = null;
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		
		//check if there is alredy persist the Employee
		try {
			String sqlSelect = getSelectQuery() + " WHERE id = " + employee.getId();
			connection = postgresDAOFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			Set <Employee> employees = parseResultSet(rs);
			if (0 != employees.size()) {
				throw new PersistException("Employee with id - " + employee.getId() + 
						" already persist");
			}
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			closeResultSet(rs);
			closeStatement(statement);
		}
		
		// create new Employee persist
		try {
			String sqlCreate = getCreateQuery();
			connection = postgresDAOFactory.createConnection();
			pStatement = connection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);
			prepareStatementForInsert(pStatement, employee);
			rs = pStatement.

		}
		
		return null;
	}

	@Override
	public Employee getEmployeeByName(String name) throws PersistException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Employee> getEmployeeByEducation(String education)
			throws PersistException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Employee> getEmployeeByDepartament(String departament)
			throws PersistException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Employee> getEmployeeByPost(String post) throws PersistException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Employee> getEmployeeBySalary(int salary)
			throws PersistException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Employee> readEmployeeByAge(int age) throws PersistException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateEmployee(Employee employee) throws PersistException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteEmployee(Employee employee) throws PersistException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Employee> getAllEmployees() throws PersistException {
		// TODO Auto-generated method stub
		return null;
	}

	private String getCreateQuery() {
		String sql = "INSERT INTO hrdepartment.employee (name, age, education, email, phone, \n"
				+ "post, skills, department, salary, hiredate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return sql;
	}


	private String getUpdateQuery() {
		String sql = "UPDATE hrdepartment.employee SET name = ?, age = ?, education = ?, \n"
				+ "email = ?, phone = ?, post = ?, skills = ?, department = ?, \n"
				+ "salary = ?, hiredate = ?";
		return sql;
	}
	

	private String getSelectQuery() {
		String sql = "SELECT * FROM hrdepartment.employee";
		return sql;
	}
	
	
	private String getDeleteQuery() {
		String sql = "DELETE FROM hrdepartment.employee";
		return sql;
	}
	
	private Set<Employee> parseResultSet(ResultSet rs) throws PersistException {
		Set<Employee> employees = new HashSet<Employee>();

		try {
			String skill = "";
			Employee employee = new Employee();
			while (rs.next()) {
				
				employee.setName(rs.getString("name"));
				employee.setAge(rs.getInt("age"));
				employee.setEducation(rs.getString("education"));
				employee.setEmail(rs.getString("email"));
				employee.setPhone(rs.getString("phone"));
				employee.setPost(rs.getString("post"));
				employee.setSalary(rs.getInt("salary"));
				employee.setHireDate(rs.getDate("hiredate"));
				
				skill = rs.getString("skills");
				String[] skillArray = skill.split(";");
				Set<String> skills = new HashSet<String>(Arrays.asList(skillArray));
				employee.setSkills(skills);
				
				DepartmentDAO departmentDao = new DepartmentDAO();
				Department department = departmentDao.getDepartmentByName(rs.getString("department"));
				employee.setDepartment(department);

				employees.add(employee);
			}
		} catch (SQLException e) {
			throw new PersistException();
		}
		return employees;
	}
	
	private void prepareStatementForInsert(PreparedStatement statement,
			Employee employee) throws PersistException {
		name, age, education, email, phone, \n"
				+ "post, skills, department, salary, hiredate)"
				+ "
		String skills = convertString(employee.getSkills());
		try {
			statement.setString(1, employee.getName());
			statement.setInt(2, employee.getAge());
			statement.setString(3, employee.getEducation());
			statement.setString(4, employee.getEmail());
			statement.setString(5, employee.getPhone());
			statement.setString(6, employee.getPost());
			statement.setString(7, skills);
			statement.setInt(8, employee.getDepartment());
			statement.setInt(9, employee.getSalary());
			statement.setDate(10, convertDate(employee.getHireDate()));
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

	private java.sql.Date convertDate(java.util.Date date) {
		if (null == date) {
			throw new IllegalArgumentException(" incorrect date argument");
		}
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());  
		return sqlDate;
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
