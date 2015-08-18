package main.com.mentat.nine.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.Closer;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.Department;
import main.com.mentat.nine.domain.Employee;

public class EmployeeDAO {

	private DAOFactory daoFactory = null;
	
	public EmployeeDAO() throws PersistException{
		daoFactory = DAOFactory.getFactory();
	}
	
	public Employee createEmployee(Employee employee) throws PersistException {

		Employee createdEmployee = null;
		Connection connection = null;
		Statement statement = null;
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		int id = 0;
		
		try {
		//check if there is already persist the Employee
			try {
				String sqlSelect = getSelectQuery() + " WHERE id = " + employee.getId();
				connection = daoFactory.createConnection();
				statement = connection.createStatement();
				rs = statement.executeQuery(sqlSelect);
				Set <Employee> employees = parseResultSet(rs);
				if (0 != employees.size()) {
					throw new PersistException("Employee with id - " + employee.getId() + 
							" already persist");
				}
			} catch (SQLException e) {
				throw new PersistException(" can't check Employee with id " + employee.getId());
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(statement);
			}
			
			// create new Employee persist
			try {
				String sqlCreate = getCreateQuery();
				pStatement = connection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);
				prepareStatementForInsert(pStatement, employee);
				int count = pStatement.executeUpdate();
				if (1 == count) {
					rs = pStatement.getGeneratedKeys();
					rs.next();
					id = rs.getInt("id");
				} else {
					throw new PersistException("Employee hasn't been created");
				}
			}catch (SQLException e) {
				throw new PersistException(" can't create new Employee with id " + id);
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(pStatement);
			}
			
			// return the last entity
			try {
				String sqlSelect = getSelectQuery() + " WHERE id = " + id;
				statement = connection.createStatement();
				rs = statement.executeQuery(sqlSelect);
				Set<Employee> employees = parseResultSet(rs);
				if (null == employees || 1 != employees.size()) {
					throw new PersistException("Was created more than one persist with id = " + id);
					}
				for (Employee emp : employees) {
					createdEmployee = emp;
				}
				createdEmployee.setId(id);
			} catch (SQLException e) {
				throw new PersistException(" can't return Employee with id " + id);
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(statement);
			}
		} finally {
			Closer.closeConnection(connection);
		}
		return createdEmployee;
	}


	public Employee getEmployeeById(int id) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		Employee createdEmployee = null;
		
		try {
			String sqlSelect = getSelectQuery() + " WHERE id = " + id;
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			Set<Employee> employees = parseResultSet(rs);
			if (employees.size() > 1) {
				throw new PersistException("Get more than one Employee with id = " + id);
			} else if (employees.size() < 1) {
				throw new PersistException("No Employee with id = " + id);
			}
			for (Employee emp : employees) {
				createdEmployee = emp;
			}
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			Closer.close(rs, statement, connection);
		}
		
		return createdEmployee;
	}

	
	public Set<Employee> getEmployeeByEducation(String education)
			throws PersistException {

		Set<Employee> employees = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		
		try { 
			String sqlSelect = getSelectQuery() + " WHERE education = " + education;
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			employees = parseResultSet(rs);
			if (employees.size() < 1) {
				throw new PersistException("No one Employee with education " + education);
			}
		} catch (SQLException e) {
			throw new PersistException();
		}
		
		return employees;
	}

	
	public Set<Employee> getEmployeeByDepartament(String departament)
			throws PersistException {
		
		Set<Employee> employees = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		
		try { 
			String sqlSelect = getSelectQuery() + " WHERE departament = " + departament;
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			employees = parseResultSet(rs);
			if (employees.size() < 1) {
				throw new PersistException("No one Employee with departament " + departament);
			}
		} catch (SQLException e) {
			throw new PersistException();
		}
		
		return employees;
	}

	
	public Set<Employee> getEmployeeByPost(String post) throws PersistException {
		Set<Employee> employees = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		
		try { 
			String sqlSelect = getSelectQuery() + " WHERE post = " + post;
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			employees = parseResultSet(rs);
			if (employees.size() < 1) {
				throw new PersistException("No one Employee with post " + post);
			}
		} catch (SQLException e) {
			throw new PersistException();
		}
		
		return employees;
	}

	
	public Set<Employee> getEmployeeBySalary(int salary)
			throws PersistException {
		Set<Employee> employees = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		
		try { 
			String sqlSelect = getSelectQuery() + " WHERE salary = " + salary;
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			employees = parseResultSet(rs);
			if (employees.size() < 1) {
				throw new PersistException("No one Employee with salary " + salary);
			}
		} catch (SQLException e) {
			throw new PersistException();
		}
		
		return employees;
	}

	
	public Set<Employee> getEmployeeByAge(int age) throws PersistException {
		
		Set<Employee> employees = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		
		try { 
			String sqlSelect = getSelectQuery() + " WHERE age = " + age;
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			employees = parseResultSet(rs);
			if (employees.size() < 1) {
				throw new PersistException("No one Employee with age " + age);
			}
		} catch (SQLException e) {
			throw new PersistException();
		}
		
		return employees;
	}

	
	public void updateEmployee(Employee employee) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		
		try {
			String sqlUpdate = getUpdateQuery() + " WHERE id = " + employee.getId();
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			int count = statement.executeUpdate(sqlUpdate);
			if (count > 1) {
				throw new PersistException("Updated more than one Employee");
			} else if (count < 1) {
				throw new PersistException("No one Employee was updated");
			}
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			Closer.closeStatement(statement);
			Closer.closeConnection(connection);
		}
		
	}
		
	
	public void deleteEmployee(Employee employee) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		
		try {
			String sqlDelete = getDeleteQuery() + " WHERE id = " + employee.getId(); 
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
			Closer.closeConnection(connection);
		}
	}

	
	public Set<Employee> getAllEmployees() throws PersistException {
		
		Set<Employee> employees = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		
		try { 
			String sqlSelect = getSelectQuery();
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			employees = parseResultSet(rs);
			if (employees.size() < 1) {
				throw new PersistException("No one Employee persist");
			}
		} catch (SQLException e) {
			throw new PersistException();
		}
		
		return employees;
	}

	private String getCreateQuery() {
		String sql = "INSERT INTO employee (name, age, education, email, phone, \n"
				+ "post, skills, department_id, salary, hiredate, firedate) \n"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return sql;
	}


	private String getUpdateQuery() {
		String sql = "UPDATE employee SET name = ?, age = ?, education = ?, \n"
				+ "email = ?, phone = ?, post = ?, skills = ?, department_id = ?, \n"
				+ "salary = ?, hiredate = ?, firedate = ?";
		return sql;
	}
	

	private String getSelectQuery() {
		String sql = "SELECT * FROM employee";
		return sql;
	}
	
	
	private String getDeleteQuery() {
		String sql = "DELETE FROM employee";
		return sql;
	}
	
	private Set<Employee> parseResultSet(ResultSet rs) throws PersistException {
		Set<Employee> employees = new HashSet<Employee>();

		try {
			String skill = "";
			while (rs.next()) {
				Employee employee = new Employee();
				employee.setName(rs.getString("name"));
				employee.setAge(rs.getInt("age"));
				employee.setEducation(rs.getString("education"));
				employee.setEmail(rs.getString("email"));
				employee.setPhone(rs.getString("phone"));
				employee.setPost(rs.getString("post"));
				employee.setSalary(rs.getInt("salary"));
				employee.setHireDate(rs.getDate("hiredate"));
				employee.setFireDate(rs.getDate("firedate"));
				
				skill = rs.getString("skills");
				String[] skillArray = skill.split(";");
				Set<String> skills = new HashSet<String>(Arrays.asList(skillArray));
				employee.setSkills(skills);
				
				DepartmentDAO departmentDao = new DepartmentDAO();
				Department department = 
						departmentDao.getDepartmentById(Integer.parseInt(rs.getString("department_id")));
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

		String skills = convertString(employee.getSkills());
		try {
			statement.setString(1, employee.getName());
			statement.setInt(2, employee.getAge());
			statement.setString(3, employee.getEducation());
			statement.setString(4, employee.getEmail());
			statement.setString(5, employee.getPhone());
			statement.setString(6, employee.getPost());
			statement.setString(7, skills);
			statement.setInt(8, employee.getDepartment().getId());
			statement.setInt(9, employee.getSalary());
			statement.setDate(10, convertDate(employee.getHireDate()));
			statement.setDate(11, convertDate(employee.getFireDate()));
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
			return null;
		}
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());  
		return sqlDate;
	}
}
