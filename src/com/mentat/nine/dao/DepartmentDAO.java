package com.mentat.nine.dao;

import java.util.Set;

import com.mentat.nine.dao.exceptions.DAOException;
import com.mentat.nine.domain.Department;

public interface DepartmentDAO {

	public Department createDepartment(String name, String head, String staff) throws DAOException;
	
	public Department updateDepartment(String name, String head, String staff) throws DAOException;
	
	public Department selectDepartmentByName(String name) throws DAOException;
	
	public Department selectDepartmentByHead(String head) throws DAOException;
	
	public Set<Department> getAllDepartments() throws DAOException;
	
	public void deleteDepartment(String name) throws DAOException;
}
