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
	
	public CVForm createCVForm() throws DAOException;
	
	public CVForm readCVFormByPost(String post) throws DAOException;
	
	public CVForm readCVFormByWorkExpirience(int workExpirience) throws DAOException;
	
	public CVForm readCVFormByEducation(String education) throws DAOException;
	
	public CVForm readCVFormByDesiredSalary(int desiredSalary) throws DAOException;
	
	public CVForm updateCVForm(CVForm cv) throws DAOException;
	
	public void deleteCVForm(CVForm cv) throws DAOException;
	
	public List<CVForm> getAllCVForms() throws DAOException;
}
