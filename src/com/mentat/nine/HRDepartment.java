/**
 * 
 */
package com.mentat.nine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mentat.nine.exceptions.NoSuchEmployeeException;
import com.mentat.nine.exceptions.NoSuitableCandidateException;

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
		
		boolean acceptAge = false;
		boolean acceptWorkExperience = false;
		boolean acceptEducation = false;
		boolean acceptSkills = false;
		boolean acceptPost = false;
		boolean acceptSalary = false;

		Set<Boolean> conditions = new HashSet<Boolean>();
		conditions.add(acceptAge);
		conditions.add(acceptWorkExperience);
		conditions.add(acceptEducation);
		conditions.add(acceptSkills);
		conditions.add(acceptPost);
		conditions.add(acceptSalary);
	
		outer: for (CVForm cv : cvs) {
			
			for (Boolean condition : conditions) {
				condition = false;
			}
			
			if (Math.abs(cv.getAge() - app.getAge()) < 2) {
				acceptAge = true;
			}
			if (cv.getWorkExpirience() >= app.getWorkExpirience()) {
				acceptWorkExperience = true;
			}
			if (cv.getEducation().equals(app.getEducation())) {
				acceptEducation = true;
			}
			if (cv.getSkills().containsAll(app.getRequirements())) {
				acceptSkills = true;
			}
			if (cv.getPost().equals(app.getPost())) {
				acceptPost = true;
			}
			if (cv.getDesiredSalary() <= app.getSalary()) {
				acceptSalary = true;
			}
			
			for (Boolean condition : conditions) {
				if (condition == false) {
					continue outer;
				}
			candidate = new Candidate();
			break;
			}
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
			String post, int salary, Date date) {
		
		ApplicationForm app = new ApplicationForm();
		app.setDate(date);
		app.setAge(age);
		app.setEducation(education);
		app.setPost(post);
		app.setRequirements(requirements);
		app.setSalary(salary);
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
