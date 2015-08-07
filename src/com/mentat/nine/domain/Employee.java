/**
 * 
 */
package com.mentat.nine.domain;

import java.util.Date;

/**
 * @author Ruslan
 *
 */
public class Employee extends Person{

	private String post;
	private Date hireDate;
	private Date fireDate;
	private int salary;
	private Department department;
	
	/**
	 * 
	 */
	public Employee() {
		// TODO Auto-generated constructor stub
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		if (post == null || post.equals("")) {
			throw new IllegalArgumentException();
		}
		this.post = post;
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

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		if (salary < 0) {
			throw new IllegalArgumentException();
		}
		this.salary = salary;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		if (null == department) {
			throw new IllegalArgumentException();
		}
		this.department = department;
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
				", skills=" + getSkills() + ", phone=" + getPhone() + ", email=" + getEmail();
	}	
}
