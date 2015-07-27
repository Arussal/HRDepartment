/**
 * 
 */
package com.mentat.nine.dao;

import java.util.Date;

import com.mentat.nine.dao.exceptions.DAOException;
import com.mentat.nine.domain.ApplicationForm;

/**
 * @author Ruslan
 *
 */
public interface ApplicationFormDAO {
	
	public ApplicationForm createApplicitionForm(Date date, int age, String education, String post, 
			String requirements,  int salary) throws DAOException;
	
	public ApplicationForm updateApplicationForm(Date date, int age, String education, String post, 
			String requirements, int salary) throws DAOException;
	
	public void deleteApplicationForm(String post) throws DAOException;;
}
