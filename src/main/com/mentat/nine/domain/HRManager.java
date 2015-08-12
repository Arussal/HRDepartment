/**
 * 
 */
package main.com.mentat.nine.domain;

import java.util.Date;
import java.util.Set;

import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.domain.exceptions.NoSuchEmployeeException;
import main.com.mentat.nine.domain.exceptions.NoSuitableCandidateException;

/**
 * @author Ruslan
 *
 */
public interface HRManager {
	
	public ApplicationForm formApplicationForm(int age, String education, Set<String> requirements, 
			String post, int salary, int workExpirience, Date date); 

	public ApplicationForm createApplicationForm(ApplicationForm app) throws PersistException;
	
	public Set<Candidate> findCandidates(ApplicationForm app) 
			throws NoSuitableCandidateException, PersistException;
	
	public Candidate createCandidate(Candidate candidate) throws PersistException;
	
	public Employee hireEmployee(Candidate candidate, int salary, String post, Date date, 
			Department department);
	
	public Employee createEmployee(Employee employee) throws PersistException;
	
	public void fireEmployee(Employee employee, Date fireDate) 
			throws NoSuchEmployeeException, PersistException;
	
	public void changeSalary(Employee employee, int salary) 
			throws NoSuchEmployeeException, PersistException;

	public void updateEmployee(Employee employee) throws PersistException;
	
	public void changePost(Employee employee, String post) 
			throws NoSuchEmployeeException, PersistException;
	
	public CVForm addCVForm(CVForm form) throws PersistException;

	

}
