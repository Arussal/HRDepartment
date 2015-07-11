/**
 * 
 */
package com.mentat.nine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Ruslan
 *
 */
public class HRDepartment {

	private Set<Employee> staff;
	private Set<Employee> firedEmployees;
	private List<CVForm> cvs;
	
	/**
	 * 
	 */
	public HRDepartment() {
		cvs = new ArrayList<CVForm>();
	}

	public void addCVForm(CVForm form) {
		if (null == form) {
			throw new IllegalArgumentException();
		}
		cvs.add(form);
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
