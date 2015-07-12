/**
 * 
 */
package com.mentat.nine;

import java.util.Date;

import com.mentat.nine.exceptions.NoSuchEmployeeException;

/**
 * @author Ruslan
 *
 */
public interface HRManager {
	
	public ApplicationForm createApplicationForm(int age, String education, String skills, 
			String responsibilities, String requirements, String post, int salary, Date date); 
	
	public Candidate findCandidate(int age, String education, String skills, String post, int salary);
	
	public Employee hireEmployee(Candidate candidate, int salary, String post, Date date, 
			Department department);
	
	public void fireEmployee(Employee employee, Date fireDate) throws NoSuchEmployeeException;
	
	public void changeSalary(Employee employee, int salary) throws NoSuchEmployeeException;
	
	public void changePost(Employee employee, String post) throws NoSuchEmployeeException;
	
	public void addCVForm(CVForm form);
	
}
