/**
 * 
 */
package com.mentat.nine;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.mentat.nine.exceptions.NoSuchEmployeeException;

/**
 * @author Ruslan
 *
 */
public class HRDepartmentTest {

	/**
	 * Test method for {@link com.mentat.nine.HRDepartment#addCVForm(com.mentat.nine.CVForm)}.
	 */
	@Test
	public void testAddCVForm() {
		HRDepartment hrDep = new HRDepartment();
		CVForm cv = new CVForm();
		hrDep.addCVForm(cv);
		assertTrue(hrDep.getCvs().size() == 1);
	}

	/**
	 * Test method for {@link com.mentat.nine.HRDepartment#findCandidate(com.mentat.nine.ApplicationForm)}.
	 */
	@Test
	public void testFindCandidate() {
		HRDepartment hrDep = new HRDepartment();
		ApplicationForm af = new ApplicationForm();
		Set<String> requirements = new HashSet<String>();
		requirements.add("Java");
		requirements.add("Hibernate");
		requirements.add("Spring");
		af.setAge(25);
		af.setDate(Calendar.getInstance().getTime());
		af.setEducation("IT");
		af.setEmail("email");
		af.setName("Name");
		af.setPhone("phone");
		af.setPost("head");
		af.setRequirements(requirements);
		af.setSalary(10000);
		af.setWorkExpirience(5);
		
		CVFor
		
	}

	/**
	 * Test method for {@link com.mentat.nine.HRDepartment#hireEmployee(com.mentat.nine.Candidate, 
	 * int, java.lang.String, java.util.Date, com.mentat.nine.Department)}.
	 */
	@Test
	public void testHireEmployee() {
		HRDepartment hrDep = new HRDepartment();
		Candidate candidate = new Candidate();
		Employee employee = hrDep.hireEmployee(candidate, 1000, "Post", new Date(), new Department());
		assertNotNull(employee);
	}

	/**
	 * Test method for {@link com.mentat.nine.HRDepartment#fireEmployee(com.mentat.nine.Employee, 
	 * java.util.Date)}.
	 * @throws NoSuchEmployeeException 
	 */
	@Test
	public void testFireEmployee() throws NoSuchEmployeeException {
		HRDepartment hrDep = new HRDepartment();
		Employee emp = new Employee();
		hrDep.getStaff().add(emp);
		assertEquals(1, hrDep.getStaff().size());
		assertEquals(0, hrDep.getFiredEmployees().size());
		hrDep.fireEmployee(emp, Calendar.getInstance().getTime());
		assertEquals(0, hrDep.getStaff().size());
		assertEquals(1, hrDep.getFiredEmployees().size());
	}

	/**
	 * Test method for {@link com.mentat.nine.HRDepartment#changeSalary(com.mentat.nine.Employee, int)}.
	 * @throws NoSuchEmployeeException 
	 */
	@Test (expected = NoSuchEmployeeException.class)
	public void testChangeSalary1() throws NoSuchEmployeeException {
		HRDepartment hrDep = new HRDepartment();
		Employee emp = new Employee();
		emp.setName("Employee");
		hrDep.changeSalary(emp, 1500);
	}
	
	/**
	 * Test method for {@link com.mentat.nine.HRDepartment#changeSalary(com.mentat.nine.Employee, int)}.
	 * @throws NoSuchEmployeeException 
	 */
	@Test 
	public void testChangeSalary2() throws NoSuchEmployeeException {
		HRDepartment hrDep = new HRDepartment();
		Employee emp = new Employee();
		emp.setSalary(2500);
		hrDep.getStaff().add(emp);
		hrDep.changeSalary(emp, 1500);
		assertNotEquals(2500, emp.getSalary());
	}

	/**
	 * Test method for {@link com.mentat.nine.HRDepartment#changePost(com.mentat.nine.Employee, 
	 * java.lang.String)}.
	 * @throws NoSuchEmployeeException 
	 */
	@Test (expected = NoSuchEmployeeException.class)
	public void testChangePost1() throws NoSuchEmployeeException {
		HRDepartment hrDep = new HRDepartment();
		Employee emp = new Employee();
		hrDep.changePost(emp, "Head");
	}
	
	/**
	 * Test method for {@link com.mentat.nine.HRDepartment#changePost(com.mentat.nine.Employee, 
	 * java.lang.String)}.
	 * @throws NoSuchEmployeeException 
	 */
	@Test
	public void testChangePost2() throws NoSuchEmployeeException {
		HRDepartment hrDep = new HRDepartment();
		Employee emp = new Employee();
		emp.setPost("SubHead");
		hrDep.getStaff().add(emp);
		hrDep.changePost(emp, "Head");
		assertEquals("Head", emp.getPost());
	}

	/**
	 * Test method for {@link com.mentat.nine.HRDepartment#createApplicationForm(int, java.lang.String, 
	 * java.util.Set, java.lang.String, int, java.util.Date)}.
	 */
	@Test
	public void testCreateApplicationForm() {
		HRDepartment hrDep = new HRDepartment();
		int age = 25;
		String education = "IT science";
		String requirement = "working requirement";
		Set<String> requirements = new HashSet<String>();
		requirements.add(requirement);
		String post = "available post";
		int salary = 10000;
		Date date = Calendar.getInstance().getTime();
		ApplicationForm aForm = hrDep.createApplicationForm(age, education, requirements, post, salary, date);
		assertNotNull(aForm);
	}

	/**
	 * Test method for {@link com.mentat.nine.Department#addEmployee(com.mentat.nine.Employee)}.
	 */
	@Test
	public void testAddEmployee() {
		HRDepartment hrDep = new HRDepartment();
		assertEquals(0, hrDep.getEmployees().size());
		Employee emp = new Employee();
		hrDep.addEmployee(emp);
		assertEquals(1, hrDep.getEmployees().size());
	}

	/**
	 * Test method for {@link com.mentat.nine.Department#removeEmployee(com.mentat.nine.Employee)}.
	 * @throws NoSuchEmployeeException 
	 */
	@Test
	public void testRemoveEmployee1() throws NoSuchEmployeeException {
		HRDepartment hrDep = new HRDepartment();
		assertEquals(0, hrDep.getEmployees().size());
		Employee emp = new Employee();
		hrDep.addEmployee(emp);
		assertEquals(1, hrDep.getEmployees().size());
		hrDep.removeEmployee(emp);
		assertEquals(0, hrDep.getEmployees().size());
	}
	
	/**
	 * Test method for {@link com.mentat.nine.Department#removeEmployee(com.mentat.nine.Employee)}.
	 * @throws NoSuchEmployeeException 
	 */
	@Test (expected = NoSuchEmployeeException.class)
	public void testRemoveEmployee2() throws NoSuchEmployeeException {
		HRDepartment hrDep = new HRDepartment();
		assertEquals(0, hrDep.getEmployees().size());
		Employee emp = new Employee();
		emp.setDepartment(new Department("Department1"));
		emp.setPost("Post1");
		hrDep.addEmployee(emp);
		assertEquals(1, hrDep.getEmployees().size());
		Employee emp1 = new Employee();
		emp.setDepartment(new Department("Department2"));
		emp.setPost("Post2");
		hrDep.removeEmployee(emp1);
	}

}
