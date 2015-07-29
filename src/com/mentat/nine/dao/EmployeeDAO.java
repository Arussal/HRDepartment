/**
 * 
 */
package com.mentat.nine.dao;

import java.util.List;

import com.mentat.nine.dao.exceptions.DAOException;
import com.mentat.nine.domain.Employee;

/**
 * @author Ruslan
 *
 */
public interface EmployeeDAO {

	public Employee createEmployee() throws DAOException;
	
	public Employee readEmployeeByName(String name) throws DAOException;
	
	public Employee readEmployeeByEducation(String education) throws DAOException;
	
	public Employee readEmployeeByDepartament(String departament) throws DAOException;
	
	public Employee readEmployeeByPost(String post) throws DAOException;
	
	public Employee readEmployeeBySalary(int salary) throws DAOException;
	
	public Employee readEmployeeByAge(int age) throws DAOException;
	
	public void updateEmployee(Employee employee) throws DAOException;
	
	public void deleteEmployee(Employee employee) throws DAOException;
	
	public List<Employee> getAllEmployees() throws DAOException;
}
