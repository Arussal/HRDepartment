/**
 * 
 */
package com.mentat.nine.dao;

import java.util.Date;

import com.mentat.nine.domain.ApplicationForm;

/**
 * @author Ruslan
 *
 */
public interface ApplicationFormDAO {
	
	public ApplicationForm createApplicitionForm(Date date, int age, String education, 
			String post, String requirements,  int salary);
	
	public ApplicationForm updateApplicationForm(String post);
	
	public void deleteApplicationForm(String post);
}
