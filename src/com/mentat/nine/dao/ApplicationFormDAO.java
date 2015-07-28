/**
 * 
 */
package com.mentat.nine.dao;


import com.mentat.nine.dao.exceptions.DAOException;
import com.mentat.nine.domain.ApplicationForm;

/**
 * @author Ruslan
 *
 */
public interface ApplicationFormDAO {
	
	public ApplicationForm createApplicitionForm() throws DAOException;
	
	public ApplicationForm readApplicationForm(int id);
	
	public void updateApplicationForm(ApplicationForm af) throws DAOException;
	
	public void deleteApplicationForm(ApplicationForm af) throws DAOException;;
}
