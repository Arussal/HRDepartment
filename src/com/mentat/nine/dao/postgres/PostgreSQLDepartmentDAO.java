/**
 * 
 */
package com.mentat.nine.dao.postgres;

import java.util.Set;

import com.mentat.nine.dao.DepartmentDAO;
import com.mentat.nine.dao.exceptions.DAOException;
import com.mentat.nine.domain.Department;

/**
 * @author Ruslan
 *
 */
public class PostgreSQLDepartmentDAO implements DepartmentDAO {

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.DepartmentDAO#createDepartment(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Department createDepartment(String name, String head, String staff)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.DepartmentDAO#updateDepartment(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Department updateDepartment(String name, String head, String staff)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.DepartmentDAO#selectDepartmentByName(java.lang.String)
	 */
	@Override
	public Department selectDepartmentByName(String name) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.DepartmentDAO#selectDepartmentByHead(java.lang.String)
	 */
	@Override
	public Department selectDepartmentByHead(String head) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.DepartmentDAO#getAllDepartments()
	 */
	@Override
	public Set<Department> getAllDepartments() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.DepartmentDAO#deleteDepartment(java.lang.String)
	 */
	@Override
	public void deleteDepartment(String name) throws DAOException {
		// TODO Auto-generated method stub

	}

}
