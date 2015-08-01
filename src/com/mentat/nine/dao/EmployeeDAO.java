/**
 * 
 */
package com.mentat.nine.dao;

import java.util.List;
import java.util.Set;

import com.mentat.nine.dao.exceptions.DAOException;
import com.mentat.nine.dao.exceptions.PersistException;
import com.mentat.nine.domain.Employee;

/**
 * @author Ruslan
 *
 */
public interface EmployeeDAO {

	public Employee createEmployee(Employee employee) throws PersistException;
	
	public Employee getEmployeeByName(String name) throws PersistException;
	
	public Set<Employee> getEmployeeByEducation(String education) throws PersistException;
	
	public Set<Employee> getEmployeeByDepartament(String departament) throws PersistException;
	
	public Set<Employee> getEmployeeByPost(String post) throws PersistException;
	
	public Set<Employee> getEmployeeBySalary(int salary) throws PersistException;
	
	public Set<Employee> readEmployeeByAge(int age) throws PersistException;
	
	public void updateEmployee(Employee employee) throws PersistException;
	
	public void deleteEmployee(Employee employee) throws PersistException;
	
	public Set<Employee> getAllEmployees() throws PersistException;
}
