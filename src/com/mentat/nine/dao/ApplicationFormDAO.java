/**
 * 
 */
package com.mentat.nine.dao;


import java.sql.PreparedStatement;
import java.util.List;

import com.mentat.nine.dao.exceptions.PersistException;
import com.mentat.nine.domain.ApplicationForm;

/**
 * @author Ruslan
 *
 */
public interface ApplicationFormDAO {
	
	public ApplicationForm createApplicationForm(ApplicationForm af) throws PersistException;
	
	public ApplicationForm readApplicationForm(int id) throws PersistException;
	
	public void updateApplicationForm(ApplicationForm af) throws PersistException;
	
	public void deleteApplicationForm(ApplicationForm af) throws PersistException;
	
	public List<ApplicationForm> getAllApplicationForms() throws PersistException;

	public String getCreateQuery();
	
	public String getSelectQuery();
	
	public void prepareStatementForInsert(PreparedStatement statement, ApplicationForm af) throws PersistException;

}
