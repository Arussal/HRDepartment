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

import com.mentat.nine.dao.exceptions.PersistException;
import com.mentat.nine.domain.ApplicationForm;
import com.mentat.nine.domain.CVForm;
import com.mentat.nine.domain.Candidate;
import com.mentat.nine.domain.Department;
import com.mentat.nine.domain.Employee;
import com.mentat.nine.domain.HRDepartment;
import com.mentat.nine.domain.exceptions.NoSuchEmployeeException;
import com.mentat.nine.domain.exceptions.NoSuitableCandidateException;

/**
 * @author Ruslan
 *
 */
public class HRDepartmentTest {

	/**
	 * Test method for {@link com.mentat.nine.domain.HRDepartment#
	 * addCVForm(com.mentat.nine.domain.CVForm)}.
	 */
	@Test
	public void testAddCVForm() {
		HRDepartment hrDep = new HRDepartment();
		CVForm cv = new CVForm();
		hrDep.addCVForm(cv);
		assertTrue(hrDep.getCvs().size() == 1);
	}

	/**
	 * Test method for {@link com.mentat.nine.domain.HRDepartment#
	 * findCandidate(com.mentat.nine.domain.ApplicationForm)}.
	 * @throws NoSuitableCandidateException 
	 * @throws PersistException 
	 */
	@Test
	public void testFindCandidate() throws NoSuitableCandidateException, PersistException {
		
		HRDepartment hrDep = new HRDepartment();
		ApplicationForm af = new ApplicationForm();
		Set<String> requirements = new HashSet<String>();
		requirements.add("Skill1_1");
		requirements.add("Skill2_1");
		requirements.add("Skill3_1");
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
		
		
		for (int i = 0; i < 5; i++) {
			CVForm form = new CVForm();
			form.setAge(24);
			form.setWorkExpirience(5 + i);
			form.setEducation("IT");
			form.setEmail("email" + i);
			form.setName("Name" + i);
			form.setPhone("phone" + i);
			form.setPost("head");
			Set<String> skills = new HashSet<String>();
			skills.add("Skill1_" + i);
			skills.add("Skill2_" + i);
			skills.add("Skill3_" + i);
			form.setSkills(skills);
			form.setDesiredSalary(9900 + i);
			
			hrDep.getCvs().add(form);
		}
		
		Candidate candidate = hrDep.findCandidate(af);	
		assertNotNull(candidate);
		assertEquals("Name1", candidate.getName());
	}

	/**
	 * Test method for {@link com.mentat.nine.domain.HRDepartment#
	 * hireEmployee(com.mentat.nine.domain.Candidate, 
	 * int, java.lang.String, java.util.Date, com.mentat.nine.domain.Department)}.
	 * @throws PersistException 
	 */
	@Test
	public void testHireEmployee() throws PersistException {
		HRDepartment hrDep = new HRDepartment();
		Candidate candidate = new Candidate();
		Employee employee = hrDep.hireEmployee(candidate, 1000, "Post", new Date(), new Department());
		assertNotNull(employee);
	}

	/**
	 * Test method for {@link com.mentat.nine.domain.HRDepartment#
	 * fireEmployee(com.mentat.nine.domain.Employee, 
	 * java.util.Date)}.
	 * @throws NoSuchEmployeeException 
	 * @throws PersistException 
	 */
	@Test
	public void testFireEmployee() throws NoSuchEmployeeException, PersistException {
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
	 * Test method for {@link com.mentat.nine.domain.HRDepartment#
	 * changeSalary(com.mentat.nine.domain.Employee, int)}.
	 * @throws NoSuchEmployeeException 
	 * @throws PersistException 
	 */
	@Test (expected = NoSuchEmployeeException.class)
	public void testChangeSalary1() throws NoSuchEmployeeException, PersistException {
		HRDepartment hrDep = new HRDepartment();
		Employee emp = new Employee();
		emp.setName("Employee");
		hrDep.changeSalary(emp, 1500);
	}
	
	/**
	 * Test method for {@link com.mentat.nine.domain.HRDepartment#
	 * changeSalary(com.mentat.nine.domain.Employee, int)}.
	 * @throws NoSuchEmployeeException 
	 * @throws PersistException 
	 */
	@Test 
	public void testChangeSalary2() throws NoSuchEmployeeException, PersistException {
		HRDepartment hrDep = new HRDepartment();
		Employee emp = new Employee();
		emp.setSalary(2500);
		hrDep.getStaff().add(emp);
		hrDep.changeSalary(emp, 1500);
		assertNotEquals(2500, emp.getSalary());
	}

	/**
	 * Test method for {@link com.mentat.nine.domain.HRDepartment#
	 * changePost(com.mentat.nine.domain.Employee, 
	 * java.lang.String)}.
	 * @throws NoSuchEmployeeException 
	 * @throws PersistException 
	 */
	@Test (expected = NoSuchEmployeeException.class)
	public void testChangePost1() throws NoSuchEmployeeException, PersistException {
		HRDepartment hrDep = new HRDepartment();
		Employee emp = new Employee();
		hrDep.changePost(emp, "Head");
	}
	
	/**
	 * Test method for {@link com.mentat.nine.domain.HRDepartment#
	 * changePost(com.mentat.nine.domain.Employee, 
	 * java.lang.String)}.
	 * @throws NoSuchEmployeeException 
	 * @throws PersistException 
	 */
	@Test
	public void testChangePost2() throws NoSuchEmployeeException, PersistException {
		HRDepartment hrDep = new HRDepartment();
		Employee emp = new Employee();
		emp.setPost("SubHead");
		hrDep.getStaff().add(emp);
		hrDep.changePost(emp, "Head");
		assertEquals("Head", emp.getPost());
	}

	/**
	 * Test method for {@link com.mentat.nine.domain.HRDepartment#
	 * createApplicationForm(int, java.lang.String, 
	 * java.util.Set, java.lang.String, int, java.util.Date)}.
	 * @throws PersistException 
	 */
	@Test
	public void testCreateApplicationForm() throws PersistException {
		HRDepartment hrDep = new HRDepartment();
		int age = 25;
		String education = "IT science";
		String requirement = "working requirement";
		Set<String> requirements = new HashSet<String>();
		requirements.add(requirement);
		String post = "available post";
		int salary = 10000;
		int workExpirience = 5;
		Date date = Calendar.getInstance().getTime();
		ApplicationForm aForm = hrDep.createApplicationForm(age, education, requirements, post, 
				salary, workExpirience, date);
		assertNotNull(aForm);
	}

	/**
	 * Test method for {@link com.mentat.nine.domain.Department#
	 * addEmployee(com.mentat.nine.domain.Employee)}.
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
	 * Test method for {@link com.mentat.nine.domain.Department#
	 * removeEmployee(com.mentat.nine.domain.Employee)}.
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
	 * Test method for {@link com.mentat.nine.domain.Department#
	 * removeEmployee(com.mentat.nine.domain.Employee)}.
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
