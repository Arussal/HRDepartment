/**
 * 
 */
package domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import dao.exceptions.PersistException;

/**
 * @author Ruslan
 *
 */
@Entity
@Table(name="employee")
public class Employee extends Person{

	@Column(name="name")
	private String name;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="email")
	private String email;
		
	@Temporal(TemporalType.DATE)
	@Column(name="hireDate")
	private Date hireDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="fireDate")
	private Date fireDate;
	
	@Column(name="salary")
	private Integer salary;

	@ManyToOne(targetEntity = Department.class, cascade = CascadeType.ALL)
	@JoinColumn(name="id_department", referencedColumnName="id")
	private Department department;
	
	/**
	 * 
	 */
	public Employee() {
	}


	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public Date getFireDate() {
		return fireDate;
	}

	public void setFireDate(Date fireDate) {
		this.fireDate = fireDate;
	}

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		if (salary < 0) {
			throw new IllegalArgumentException();
		}
		this.salary = salary;
	}

	public Department getDepartment() throws PersistException {
		return department;
	}

	public void setDepartment(Department department) {
		if (null == department) {
			throw new IllegalArgumentException();
		}
		this.department = department;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((department == null) ? 0 : department.hashCode());
		result = prime * result
				+ ((hireDate == null) ? 0 : hireDate.hashCode());
		result = prime * result + ((post == null) ? 0 : post.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (hireDate == null) {
			if (other.hireDate != null)
				return false;
		} else if (!hireDate.equals(other.hireDate))
			return false;
		if (post == null) {
			if (other.post != null)
				return false;
		} else if (!post.equals(other.post))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "post=" + post + ", hireDate=" + hireDate + ", hireDate=" + hireDate + 
				", salary=" + salary + ", department=" + department + 
				", education=" + getEducation() + ", name=" + getName() + ", age=" + getAge() + 
				", phone=" + getPhone() + ", email=" + getEmail();
	}	
}
