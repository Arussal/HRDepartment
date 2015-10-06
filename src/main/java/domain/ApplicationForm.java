/**
 * 
 */
package domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * @author Ruslan
 *
 */
@Entity
@Table(name="application_form")
public class ApplicationForm extends Person {

	@Column(name="salary")
	private int salary;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="requirements_table", 
		joinColumns=@JoinColumn(name="id_requirements"))
	@Column(name="requirement")
	private Set<String> requirements;
	
	@Column(name="date")
	@Temporal(TemporalType.DATE)
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
