/**
 * 
 */
package com.mentat.nine.dao;

import java.util.Date;
import java.util.Set;

import com.mentat.nine.dao.exceptions.DAOException;
import com.mentat.nine.domain.Employee;

/**
 * @author Ruslan
 *
 */
public interface EmployeeDAO {

	public Employee createEmployee(String name, int age, String education, String email, String phone, 
			String skills, String department, String post, int salary, Date hiredate) throws DAOException;
	
	public Employee updateEmployee(String name, int age, String education, String email, String phone, 
			String skills, String department, String post, int salary) throws DAOException;
	
	public Employee selectEmployeeByName(String name) throws DAOException;
	
	public Employee selectEmployeeByEducation(String education) throws DAOException;
	
	public Employee selectEmployeeByDepartament(String departament) throws DAOException;
	
	public Employee selectEmployeeByPost(String post) throws DAOException;
	
	public Employee selectEmployeeBySalary(int salary) throws DAOException;
	
	public Employee selectEmployeeByAge(int age) throws DAOException;
	
	public void deleteEmployee(String name) throws DAOException;
	
	public Set<Employee> getAllEmployees() throws DAOException;
	
}
