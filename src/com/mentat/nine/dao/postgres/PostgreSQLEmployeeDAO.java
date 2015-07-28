package com.mentat.nine.dao.postgres;

import java.util.Date;
import java.util.Set;

import com.mentat.nine.dao.EmployeeDAO;
import com.mentat.nine.dao.exceptions.DAOException;
import com.mentat.nine.domain.Employee;

public class PostgreSQLEmployeeDAO implements EmployeeDAO {

	@Override
	public Employee createEmployee(String name, int age, String education,
			String email, String phone, String skills, String department,
			String post, int salary, Date hiredate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee updateEmployee(String name, int age, String education,
			String email, String phone, String skills, String department,
			String post, int salary) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee selectEmployeeByName(String name) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee selectEmployeeByEducation(String education)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee selectEmployeeByDepartament(String departament)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee selectEmployeeByPost(String post) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee selectEmployeeBySalary(int salary) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee selectEmployeeByAge(int age) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteEmployee(String name) throws DAOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<Employee> getAllEmployees() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

}
