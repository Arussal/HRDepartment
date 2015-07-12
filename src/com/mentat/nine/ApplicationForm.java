/**
 * 
 */
package com.mentat.nine;

import java.util.Date;

/**
 * @author Ruslan
 *
 */
public class ApplicationForm extends Person {

	private int salary;
	private String requirements;
	private String responsibilities;
	private Date date;
	
	/**
	 * 
	 */
	public ApplicationForm() {
		// TODO Auto-generated constructor stub
	}

	public ApplicationForm(String post) {
		super();
		this.post = post;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		if (salary < 0) {
			throw new IllegalArgumentException();
		}
		this.salary = salary;
	}

	public String getRequirements() {
		return requirements;
	}

	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}

	public String getResponsibilities() {
		return responsibilities;
	}

	public void setResponsibilities(String responsibilities) {
		this.responsibilities = responsibilities;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		if (null == date) {
			throw new IllegalArgumentException();
		}
		this.date = date;
	}

}
