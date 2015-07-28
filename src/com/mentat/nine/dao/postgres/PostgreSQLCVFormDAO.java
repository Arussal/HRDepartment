/**
 * 
 */
package com.mentat.nine.dao.postgres;

import java.util.List;

import com.mentat.nine.dao.CVFormDAO;
import com.mentat.nine.dao.exceptions.DAOException;
import com.mentat.nine.domain.CVForm;

/**
 * @author Ruslan
 *
 */
public class PostgreSQLCVFormDAO implements CVFormDAO {

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.CVFormDAO#createCVForm(java.lang.String, int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String)
	 */
	@Override
	public CVForm createCVForm(String name, int age, int workExpirience,
			String skills, String education, String phone, String email,
			String post, int desiredSalary, String additionalInfo)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.CVFormDAO#updateCVForm(int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String)
	 */
	@Override
	public CVForm updateCVForm(int age, int workExpirience, String skills,
			String education, String phone, String email, String post,
			int desiredSalary, String additionalInfo) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.CVFormDAO#selectCVFormByPost(java.lang.String)
	 */
	@Override
	public CVForm selectCVFormByPost(String post) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.CVFormDAO#selectCVFormByWorkExpirience(int)
	 */
	@Override
	public CVForm selectCVFormByWorkExpirience(int workExpirience)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.CVFormDAO#selectCVFormByEducation(java.lang.String)
	 */
	@Override
	public CVForm selectCVFormByEducation(String education) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.CVFormDAO#selectCVFormByDesiredSalary(int)
	 */
	@Override
	public CVForm selectCVFormByDesiredSalary(int desiredSalary)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.CVFormDAO#getAllCVForms()
	 */
	@Override
	public List<CVForm> getAllCVForms() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.CVFormDAO#deleteCVForm(java.lang.String)
	 */
	@Override
	public void deleteCVForm(String name) throws DAOException {
		// TODO Auto-generated method stub

	}

}
