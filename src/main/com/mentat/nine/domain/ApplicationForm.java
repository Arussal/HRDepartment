/**
 * 
 */
package main.com.mentat.nine.domain;

import java.util.Date;
import java.util.Set;


/**
 * @author Ruslan
 *
 */
public class ApplicationForm extends Person {

	private int salary;
	private Set<String> requirements;
	private Date date;
	
	/**
	 * 
	 */
	public ApplicationForm() {
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

	public Set<String> getRequirements() {
		return requirements;
	}

	public void setRequirements(Set<String> requirements) {
		this.requirements = requirements;
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
