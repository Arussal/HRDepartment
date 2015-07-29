package com.mentat.nine.dao;

import java.util.List;

import com.mentat.nine.dao.exceptions.DAOException;
import com.mentat.nine.domain.Department;

public interface DepartmentDAO {

	public Department createDepartment() throws DAOException;
	
	public Department readDepartmentByName(String name) throws DAOException;
	
	public Department readDepartmentByHead(String head) throws DAOException;
	
	public Department updateDepartment(String name, String head, String staff) throws DAOException;
	
	public void deleteDepartment(String name) throws DAOException;
		
	public List<Department> getAllDepartments() throws DAOException;
}
