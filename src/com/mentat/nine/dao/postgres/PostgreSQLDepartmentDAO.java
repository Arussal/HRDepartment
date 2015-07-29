/**
 * 
 */
package com.mentat.nine.dao.postgres;

import java.util.List;

import com.mentat.nine.dao.DepartmentDAO;
import com.mentat.nine.dao.exceptions.DAOException;
import com.mentat.nine.domain.Department;

/**
 * @author Ruslan
 *
 */
public class PostgreSQLDepartmentDAO implements DepartmentDAO {

	@Override
	public Department createDepartment() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Department readDepartmentByName(String name) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Department readDepartmentByHead(String head) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Department updateDepartment(String name, String head, String staff)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteDepartment(String name) throws DAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Department> getAllDepartments() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

}
