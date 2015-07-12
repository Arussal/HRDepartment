/**
 * 
 */
package com.mentat.nine;

import java.util.HashSet;
import java.util.Set;

import com.mentat.nine.exceptions.NoSuchEmployeeException;

/**
 * @author Ruslan
 *
 */
public class Department {

	private String name;
	private String head;
	private Set<Employee> employees;
	
	/**
	 * 
	 */
	public Department() {
	}
	
	/**
	 * 
	 */
	public Department(String name) {
		super();
		this.name = name;
		employees = new HashSet<Employee>();
	}

	public void addEmployee(Employee employee) {
		if (null == employee) {
			throw new IllegalArgumentException();
		}
		employees.add(employee);
		
	}
	
	public void removeEmployee(Employee employee) throws NoSuchEmployeeException {
		if (null == employee) {
			throw new IllegalArgumentException();
		}
		if (!employees.contains(employee)) {
			throw new NoSuchEmployeeException();
		}
		employees.remove(employee);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public Set<Employee> getEmployees() {
		return employees;
	}
	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Department other = (Department) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
