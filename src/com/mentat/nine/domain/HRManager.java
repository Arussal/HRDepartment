/**
 * 
 */
package com.mentat.nine.domain;

import java.util.Date;
import java.util.Set;

import com.mentat.nine.dao.exceptions.PersistException;
import com.mentat.nine.domain.exceptions.NoSuchEmployeeException;
import com.mentat.nine.domain.exceptions.NoSuitableCandidateException;

/**
 * @author Ruslan
 *
 */
public interface HRManager {
	
	public ApplicationForm createApplicationForm(int age, String education, Set<String> requirements, 
			String post, int salary, int workExpirience, Date date) throws PersistException; 
	
	public Candidate findCandidate(ApplicationForm app) 
			throws NoSuitableCandidateException, PersistException;
	
	public Employee hireEmployee(Candidate candidate, int salary, String post, Date date, 
			Department department) throws PersistException;
	
	public void fireEmployee(Employee employee, Date fireDate) 
			throws NoSuchEmployeeException, PersistException;
	
	public void changeSalary(Employee employee, int salary) 
			throws NoSuchEmployeeException, PersistException;
	
	public void changePost(Employee employee, String post) 
			throws NoSuchEmployeeException, PersistException;
	
	public void addCVForm(CVForm form);
	
}
