/**
 * 
 */
package com.mentat.nine.dao;

import java.util.List;

import com.mentat.nine.dao.exceptions.DAOException;
import com.mentat.nine.domain.CVForm;

/**
 * @author Ruslan
 *
 */
public interface CVFormDAO {
	
	public CVForm createCVForm(String name, int age, int workExpirience, String skills, 
			String education, String phone, String email, String post, int desiredSalary, 
			String additionalInfo) throws DAOException;
	
	public CVForm updateCVForm(int age, int workExpirience, String skills, 
			String education, String phone, String email, String post, int desiredSalary, 
			String additionalInfo) throws DAOException;
	
	public CVForm selectCVFormByPost(String post) throws DAOException;
	
	public CVForm selectCVFormByWorkExpirience(int workExpirience) throws DAOException;
	
	public CVForm selectCVFormByEducation(String education) throws DAOException;
	
	public CVForm selectCVFormByDesiredSalary(int desiredSalary) throws DAOException;
	
	public List<CVForm> getAllCVForms() throws DAOException;
	
	public void deleteCVForm(String name) throws DAOException;
}
