/**
 * 
 */
package com.mentat.nine.dao;


import java.sql.SQLException;
import java.util.List;

import com.mentat.nine.dao.exceptions.DAOException;
import com.mentat.nine.dao.exceptions.PersistException;
import com.mentat.nine.domain.ApplicationForm;

/**
 * @author Ruslan
 *
 */
public interface ApplicationFormDAO {
	
	public ApplicationForm createApplicitionForm(ApplicationForm af) throws DAOException, PersistException;
	
	public ApplicationForm readApplicationForm(int id) throws DAOException;
	
	public void updateApplicationForm(ApplicationForm af) throws DAOException;
	
	public void deleteApplicationForm(ApplicationForm af) throws DAOException;
	
	public List<ApplicationForm> getAllApplicationForms() throws DAOException;

	public String getCreateQuery();

}
