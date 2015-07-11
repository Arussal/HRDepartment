/**
 * 
 */
package com.mentat.nine;

/**
 * @author Ruslan
 *
 */
public interface HRManager {
	
	public ApplicationForm createApplicationForm(); 
	public Candidate findCandidate();
	public Employee hireEmployee(Candidate candidate);
	public void fireEmployee(Employee employee);
	public void changeSalary(Employee employee);
	public void changePost(Employee employee);
	
}
