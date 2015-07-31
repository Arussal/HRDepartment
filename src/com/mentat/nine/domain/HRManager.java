/**
 * 
 */
package com.mentat.nine.domain;

import java.util.Date;
import java.util.Set;

import com.mentat.nine.domain.exceptions.NoSuchEmployeeException;
import com.mentat.nine.domain.exceptions.NoSuitableCandidateException;

/**
 * @author Ruslan
 *
 */
public interface HRManager {
	
	public ApplicationForm createApplicationForm(int age, String education, Set<String> requirements, 
			String post, int salary, int workExpirience, Date date); 
	
	public Candidate findCandidate(ApplicationForm app) throws NoSuitableCandidateException;
	
	public Employee hireEmployee(Candidate candidate, int salary, String post, Date date, 
			Department department);
	
	public void fireEmployee(Employee employee, Date fireDate) throws NoSuchEmployeeException;
	
	public void changeSalary(Employee employee, int salary) throws NoSuchEmployeeException;
	
	public void changePost(Employee employee, String post) throws NoSuchEmployeeException;
	
	public void addCVForm(CVForm form);
	
}
