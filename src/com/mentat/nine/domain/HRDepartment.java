/**
 * 
 */
package com.mentat.nine.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mentat.nine.domain.exceptions.NoSuchEmployeeException;
import com.mentat.nine.domain.exceptions.NoSuitableCandidateException;

/**
 * @author Ruslan
 *
 */
public class HRDepartment extends Department implements HRManager{

	private Set<Employee> staff;
	private Set<Employee> firedEmployees;
	private List<CVForm> cvs;
	
	/**
	 * 
	 */
	public HRDepartment() {
		cvs = new ArrayList<CVForm>();
		staff = new HashSet<Employee>();
		firedEmployees = new HashSet<Employee>();
	}
	
	@Override
	public void addCVForm(CVForm form) {
		if (null == form) {
			throw new IllegalArgumentException();
		}
		cvs.add(form);
	}

	@Override
	public Candidate findCandidate(ApplicationForm app) throws NoSuitableCandidateException {
		
		Candidate candidate = null;

		Map<String, Boolean> conditions = new HashMap<String, Boolean>();
		conditions.put("acceptAge", new Boolean(false));
		conditions.put("acceptWorkExperience", new Boolean(false));
		conditions.put("acceptEducation", new Boolean(false));
		conditions.put("acceptSkills", new Boolean(false));
		conditions.put("acceptPost", new Boolean(false));
		conditions.put("acceptSalary", new Boolean(false));
		
		outer: for (CVForm cv : cvs) {

			for (Entry<String, Boolean> condition : conditions.entrySet()) {
				condition.setValue(new Boolean(false));
			}
			
			if (Math.abs(cv.getAge() - app.getAge()) < 2) {
				conditions.put("acceptAge", new Boolean(true));								
			}
			if (cv.getWorkExpirience() >= app.getWorkExpirience()) {
				conditions.put("acceptWorkExperience", new Boolean(true));								
			}
			if (cv.getEducation().equals(app.getEducation())) {		
				conditions.put("acceptEducation", new Boolean(true)); 									
			}
			if (cv.getSkills().containsAll(app.getRequirements())) {
				conditions.put("acceptSkills", new Boolean(true));									
			}
			if (cv.getPost().equals(app.getPost())) {
				conditions.put("acceptPost", new Boolean(true)); 									
			}
			if (cv.getDesiredSalary() <= app.getSalary()) {
				conditions.put("acceptSalary", new Boolean(true));									
			}
 
			for (String condition : conditions.keySet()) {
				if (conditions.get(condition) == false) {
				continue outer;
				}
			}
			
			candidate = new Candidate();
			candidate.setName(cv.getName());
			candidate.setAge(cv.getAge());
			candidate.setEducation(cv.getEducation());
			candidate.setEmail(cv.getEmail());
			candidate.setPhone(cv.getPhone());
			candidate.setPost(cv.getPost());
			candidate.setSkills(cv.getSkills());
			candidate.setWorkExpirience(cv.getWorkExpirience());
			break;
			
		}
			
		if (null == candidate) {
			throw new NoSuitableCandidateException();
		}
		
		return candidate;
	}

	@Override
	public Employee hireEmployee(Candidate candidate, int salary, String post, 
			Date hireDate, Department department) {
		
		Employee employee = new Employee();
		employee.setAge(candidate.getAge());
		employee.setEducation(candidate.getEducation());
		employee.setEmail(candidate.getEmail());
		employee.setName(candidate.getName());
		employee.setPhone(candidate.getPhone());
		employee.setSkills(candidate.getSkills());
		employee.setDepartment(department);
		employee.setPost(post);
		employee.setSalary(salary);
		employee.setHireDate(hireDate);
		staff.add(employee);
		return employee;
		
	}

	@Override
	public void fireEmployee(Employee employee, Date fireDate) {
		for (Employee emp : staff) {
			if (emp.equals(employee)) {
				employee.setFireDate(fireDate);
				staff.remove(employee);
				firedEmployees.add(employee);
			}
		}
	}

	@Override
	public void changeSalary(Employee employee, int salary) throws NoSuchEmployeeException {
		if (!staff.contains(employee)) {
			throw new NoSuchEmployeeException();
		}
		for (Employee emp : staff) {
			if (emp.equals(employee)) {
				employee.setSalary(salary);
			}
		}
	}

	@Override
	public void changePost(Employee employee, String post) throws NoSuchEmployeeException {
		if (!staff.contains(employee)) {
			throw new NoSuchEmployeeException();
		}
		for (Employee emp : staff) {
			if (emp.equals(employee)) {
				employee.setPost(post);
			}
		}
	}

	@Override
	public ApplicationForm createApplicationForm(int age, String education, Set<String> requirements, 
			String post, int salary, int workExpirience, Date date) {
		
		ApplicationForm app = new ApplicationForm();
		app.setDate(date);
		app.setAge(age);
		app.setEducation(education);
		app.setPost(post);
		app.setRequirements(requirements);
		app.setSalary(salary);
		app.setWorkExpirience(workExpirience);
		return app;
	}
	
	public Set<Employee> getStaff() {
		return staff;
	}

	public void setStaff(Set<Employee> staff) {
		this.staff = staff;
	}

	public Set<Employee> getFiredEmployees() {
		return firedEmployees;
	}

	public void setFiredEmployees(Set<Employee> firedEmployees) {
		this.firedEmployees = firedEmployees;
	}

	public List<CVForm> getCvs() {
		return cvs;
	}

	public void setCvs(List<CVForm> cvs) {
		this.cvs = cvs;
	}
}
