package main.com.mentat.nine.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.Closer;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.Department;
import main.com.mentat.nine.domain.Employee;
import main.com.mentat.nine.domain.util.LogConfig;

public class EmployeeDAO {

	static {
		LogConfig.loadLogConfig();
	}
	private static Logger log = Logger.getLogger(EmployeeDAO.class);
	
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
				if(log.isTraceEnabled()) {
					log.trace("try to check if Employee with id " + employee.getId() + " exists");
				}
				String sqlSelect = getSelectQuery() + " WHERE id = " + employee.getId();
				connection = daoFactory.createConnection();
				log.trace("connection created");
				statement = connection.createStatement();
				log.trace("statement created");
				rs = statement.executeQuery(sqlSelect);
				log.trace("resultset got");
				Set <Employee> employees = parseResultSet(rs);
				if (0 != employees.size()) {
					log.warn("Employee is already persist, id " + employee.getId());
					throw new PersistException("Employee with id - " + employee.getId() + 
							" already persist");
				}
				if(log.isTraceEnabled()){
					log.trace("Employee with id " + id + " is absent");
				}
			} catch (SQLException e) {
				log.error(" can't check Employee by id");
				throw new PersistException(" can't check Employee with id " + employee.getId());
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(statement);
			}
			
			// create new Employee persist
			try {
				log.trace("try to create new entity Employee");
				String sqlCreate = getCreateQuery();
				pStatement = connection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);
				log.trace("pStatement created");
				prepareStatementForInsert(pStatement, employee);
				int count = pStatement.executeUpdate();
				if (1 == count) {
					rs = pStatement.getGeneratedKeys();
					log.trace("generated pStatement keys");
					rs.next();
					id = rs.getInt("id");
				} else {
					log.error("new entity Employee not created");
					throw new PersistException("Employee hasn't been created");
				}
				log.info("new entity Employee created, id " + id);
			}catch (SQLException e) {
				log.error("new entity Employee not created");
				throw new PersistException(" can't create new Employee with id " + id);
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(pStatement);
			}
			
			// return the last entity
			try {
				if(log.isTraceEnabled()) {
					log.trace("try to get Employee entity with id " + id);
				}
				String sqlSelect = getSelectQuery() + " WHERE id = " + id;
				statement = connection.createStatement();
				log.trace("statement created");
				rs = statement.executeQuery(sqlSelect);
				log.trace("resulset got");
				Set<Employee> employees = parseResultSet(rs);
				if (null == employees || 1 != employees.size()) {
					log.warn("more than one Employee with id " + id);
					throw new PersistException("Was created more than one persist with id = " + id);
					}
				for (Employee emp : employees) {
					createdEmployee = emp;
				}
				log.info("return new Employee with id " + id);
				createdEmployee.setId(id);
			} catch (SQLException e) {
				log.error(" can't return new Employee with id " + id);
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
			log.trace("get Employee with id " + id);
			String sqlSelect = getSelectQuery() + " WHERE id = " + id;
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("resultset got");
			Set<Employee> employees = parseResultSet(rs);
			if (employees.size() > 1) {
				log.warn("more than one Employee with id " + id);
				throw new PersistException("Get more than one Employee with id = " + id);
			} else if (employees.size() < 1) {
				log.warn("no Employee with id " + id);
				throw new PersistException("No Employee with id = " + id);
			}
			for (Employee emp : employees) {
				createdEmployee = emp;
			}
			if (log.isTraceEnabled()) {
				log.trace("get Employee with id " + id);
			}
		} catch (SQLException e) {
			log.error("can't get Employee with id " + id);
			throw new PersistException();
		} finally {
			Closer.close(rs, statement, connection);
		}
		
		return createdEmployee;
	}

	
	public Set<Employee> getEmployeesByEducation(String education)
			throws PersistException {

		Set<Employee> employees = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		
		try { 
			log.trace("get Employees with education " + education);
			String sqlSelect = getSelectQuery() + " WHERE education = " + education;
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("resultset got");
			employees = parseResultSet(rs);
			if (employees.size() < 1) {
				log.warn("no Employees with education " + education);
				throw new PersistException("No one Employee with education " + education);
			}
		} catch (SQLException e) {
			log.error("can't get Employees with workExpirience " + education);
			throw new PersistException();
		}
		
		return employees;
	}

	
	public Set<Employee> getEmployeesByDepartament(String departament)
			throws PersistException {
		
		Set<Employee> employees = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		
		try { 
			log.trace("get Employees with departament " + departament);
			String sqlSelect = getSelectQuery() + " WHERE departament = " + departament;
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("resultset got");
			employees = parseResultSet(rs);
			if (employees.size() < 1) {
				log.warn("no Employees with departament " + departament);
				throw new PersistException("No one Employee with departament " + departament);
			}
		} catch (SQLException e) {
			log.error("can't get Employees with departament " + departament);
			throw new PersistException();
		}
		
		return employees;
	}

	
	public Set<Employee> getEmployeesByPost(String post) throws PersistException {
		Set<Employee> employees = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		
		try { 
			log.trace("get Employees with post " + post);
			String sqlSelect = getSelectQuery() + " WHERE post = " + post;
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("resultset got");
			employees = parseResultSet(rs);
			if (employees.size() < 1) {
				log.warn("no Employees with post " + post);
				throw new PersistException("No one Employee with post " + post);
			}
		} catch (SQLException e) {
			log.error("can't get Employees with post " + post);
			throw new PersistException();
		}
		
		return employees;
	}

	
	public Set<Employee> getEmployeesBySalary(int salary)
			throws PersistException {
		Set<Employee> employees = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		
		try { 
			log.trace("get Employees with salary " + salary);
			String sqlSelect = getSelectQuery() + " WHERE salary = " + salary;
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("resultset got");
			employees = parseResultSet(rs);
			if (employees.size() < 1) {
				log.warn("no Employees with salary " + salary);
				throw new PersistException("No one Employee with salary " + salary);
			}
		} catch (SQLException e) {
			log.error("can't get Employees with salary " + salary);
			throw new PersistException();
		}
		
		return employees;
	}

	
	public Set<Employee> getEmployeesByAge(int age) throws PersistException {
		
		Set<Employee> employees = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		
		try { 
			log.trace("get Employees with age " + age);
			String sqlSelect = getSelectQuery() + " WHERE age = " + age;
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("resultset got");
			employees = parseResultSet(rs);
			if (employees.size() < 1) {
				log.warn("no Employees with age " + age);
				throw new PersistException("No one Employee with age " + age);
			}
		} catch (SQLException e) {
			log.error("can't get Employees with age " + age);
			throw new PersistException();
		}
		
		return employees;
	}

	
	public void updateEmployee(Employee employee) throws PersistException {
		
		Connection connection = null;
		PreparedStatement pStatement = null;
		
		// check if there is Employee entity
		if (null == employee) {
			throw new IllegalArgumentException();
		}
		if (null == employee.getId()) {
			log.warn("Employee with id " + employee.getId() + " is not persist");
			throw new PersistException("Employee does not persist yet");
		}
		Employee selectedEmployee = this.getEmployeeById(employee.getId());
		if (null == selectedEmployee) {
			log.warn("Employee with id " + employee.getId() + " is not persist");
			throw new PersistException("Employee does not persist yet");
		}	
		
		try {
			if(log.isTraceEnabled()) {
				log.trace("try to update Employee with id " + employee.getId()); 
			}
			String sqlUpdate = getUpdateQuery() + " WHERE id = " + employee.getId();
			connection = daoFactory.createConnection();
			log.trace("create connection");
			pStatement = connection.prepareStatement(sqlUpdate, Statement.RETURN_GENERATED_KEYS);
			log.trace("create pStatement");
			prepareStatementForInsert(pStatement, employee);
			int count = pStatement.executeUpdate();
			if (count > 1) {
				log.warn("update more then one Employee");
				throw new PersistException("Updated more than one Employee");
			} else if (count < 1) {
				log.warn("no Employee update");
				throw new PersistException("No one Employee was updated");
			}
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			Closer.closeStatement(pStatement);
			Closer.closeConnection(connection);
		}
		
	}
	
	public void deleteEmployee(Employee employee) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		
		// check if there is Employee entity
		if (null == employee) {
			throw new IllegalArgumentException();
		}
		if (null == employee.getId()) {
			log.warn("Employee with id " + employee.getId() + " is not persist");
			throw new PersistException("Employee does not persist yet");
		}
		Employee selectedEmployee = this.getEmployeeById(employee.getId());
		if (null == selectedEmployee) {
			log.warn("Employee with id " + employee.getId() + " is not persist");
			throw new PersistException("Employee does not persist yet");
		}
		
		
		try {
			if(log.isTraceEnabled()) {
				log.trace("try to delete Employee with id " + employee.getId()); 
			}
			String sqlDelete = getDeleteQuery() + " WHERE id = " + employee.getId(); 
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			int count = statement.executeUpdate(sqlDelete);
			if (1 != count) {
				if(log.isTraceEnabled()) {
					log.warn("On delete modify more then 1 record: " + count); 
				}
				throw new PersistException("On delete modify more then 1 record: " + count);
			}
		} catch (SQLException e) {
			log.error("can't delete Employee");
			throw new PersistException();
		} finally {
			Closer.closeStatement(statement);
			Closer.closeConnection(connection);
		}
	}

	
	public Set<Employee> getEmployees(Map<String, List<String>> queries) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		Set<Employee> employees = null;
		
		try {
			log.trace("get employees with different query parameters");
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
			employees = parseResultSet(rs);
			if (employees.size() < 1) {
				log.warn("no employees with different query parameters");
				throw new PersistException("No employees with different query parameters");
			}
		} catch (SQLException e) {
			log.error("can't get employees with different query parameters");
			throw new PersistException();
		} finally {
			Closer.close(rs, statement, connection);
		}
		
		return employees;
	}
	
	
	public Set<Employee> getAllEmployees() throws PersistException {
		
		Set<Employee> employees = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		String sqlSelect = getSelectQuery();
		
		try { 
			log.trace("get all Employees");
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("get resultset");
			employees = parseResultSet(rs);
			if (employees.size() < 1) {
				log.warn("no one Employee persist");
				throw new PersistException("No one Employee persist");
			}
		} catch (SQLException e) {
			log.error("can't get all Employees");
			throw new PersistException();
		}
		
		return employees;
	}

	private String getCreateQuery() {
		String sql = "INSERT INTO employee (name, age, education, email, phone, \n"
				+ "post, skills, id_department, salary, hiredate, firedate) \n"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return sql;
	}


	private String getUpdateQuery() {
		String sql = "UPDATE employee SET name = ?, age = ?, education = ?, email = ?, phone = ?, \n"
				+ "post = ?, skills = ?, id_department = ?, salary = ?, hiredate = ?, firedate = ?";
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
				employee.setId(rs.getInt("id"));
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
						departmentDao.getDepartmentById(Integer.parseInt(rs.getString("id_department")));

				employee.setDepartment(department);
				log.trace("parsed Employee entity");
				employees.add(employee);
			}
		} catch (SQLException e) {
			log.error("can't parse query results");
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

	private java.sql.Date convertDate(java.util.Date date) {
		if (null == date) {
			return null;
		}
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());  
		return sqlDate;
	}
}
