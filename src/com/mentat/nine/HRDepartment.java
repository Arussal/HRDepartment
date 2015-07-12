/**
 * 
 */
package com.mentat.nine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Ruslan
 *
 */
public class HRDepartment implements HRManager{

	private Set<Employee> staff;
	private Set<Employee> firedEmployees;
	private List<CVForm> cvs;
	
	/**
	 * 
	 */
	public HRDepartment() {
		cvs = new ArrayList<CVForm>();
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
	
	@Override
	public void addCVForm(CVForm form) {
		if (null == form) {
			throw new IllegalArgumentException();
		}
		cvs.add(form);
	}

	@Override
	public Candidate findCandidate(int age, String education, String skills, String post, int salary) {
		
		boolean acceptAge = false;
		boolean acceptEducation = false;
		boolean acceptSkills = false;
		boolean acceptResponsibilities = false;
		boolean acceptRequirements = false;
		boolean acceptPost = false;
		boolean acceptSalary = false;

		Set<Boolean> conditions = new HashSet<Boolean>();
		conditions.add(acceptAge);
		conditions.add(acceptEducation);
		conditions.add(acceptSkills);
		conditions.add(acceptResponsibilities);
		conditions.add(acceptRequirements);
		conditions.add(acceptPost);
		conditions.add(acceptSalary);
	
		for (CVForm cv : cvs) {
			if (Math.abs(cv.getAge() - age) < 2) {
				acceptAge = true;
			}
			if (cv.getEducation().equals(education)) {
				acceptEducation = true;
			}
			if (cv.getSkills().equals(skills)) {
				acceptSkills = true;
			}
			if (cv.getPost().equals(post)) {
				acceptPost = true;
				dafda
			}
		}
		
		return null;
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
	public void changeSalary(Employee employee, int salary) {
		for (Employee emp : staff) {
			if (emp.equals(employee)) {
				employee.setSalary(salary);
			}
		}
	}

	@Override
	public void changePost(Employee employee, String post) {
		for (Employee emp : staff) {
			if (emp.equals(employee)) {
				employee.setPost(post);
			}
		}
	}

	@Override
	public ApplicationForm createApplicationForm(int age,
			String education, String skills, String responsibilities,
			String requirements, String post, int salary, Date date) {
		
		ApplicationForm app = new ApplicationForm();
		app.setDate(date);
		app.setAge(age);
		app.setEducation(education);
		app.setPost(post);
		app.setRequirements(requirements);
		app.setResponsibilities(responsibilities);
		app.setSalary(salary);
		app.setSkills(skills);
		return app;
		
	}
}
