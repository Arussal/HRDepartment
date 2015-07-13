/**
 * 
 */
package com.mentat.nine;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

/**
 * @author Ruslan
 *
 */
public class CandidateTest {

	/**
	 * Test method for {@link com.mentat.nine.Candidate#createCVForm(java.lang.String, int, 
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String)}.
	 */
	@Test
	public void testCreateCVForm() {
		Candidate candidate = new Candidate();
		String name = "Ivan Pertov";
		int age = 25;
		Set<String> skills = new HashSet<String>();
		String skill = "working skills";
		skills.add(skill);
		String education = "technical education";
		String phone = "123456789";
		String email = "email@gmail.com";
		int desiredSalary = 10000;
		String additionalInfo = "addition";
		CVForm cv = candidate.createCVForm(name, age, skills, education, phone, email, desiredSalary, additionalInfo);
		assertNotNull(cv);
	}

	/**
	 * Test method for {@link com.mentat.nine.Candidate#sendCVForm(com.mentat.nine.CVForm, 
	 * com.mentat.nine.HRDepartment)}.
	 */
	@Test
	public void testSendCVForm() {
		HRDepartment hrDep = new HRDepartment();
		assertEquals(0, hrDep.getCvs().size());
		Candidate candidate = new Candidate();
		CVForm cv = new CVForm();
		candidate.sendCVForm(cv, hrDep);
		assertEquals(1, hrDep.getCvs().size());
	}

}
