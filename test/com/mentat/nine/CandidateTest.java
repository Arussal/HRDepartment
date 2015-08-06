/**
 * 
 */
package com.mentat.nine;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.mentat.nine.dao.exceptions.PersistException;
import com.mentat.nine.domain.CVForm;
import com.mentat.nine.domain.Candidate;
import com.mentat.nine.domain.HRDepartment;

/**
 * @author Ruslan
 *
 */
public class CandidateTest {

	/**
	 * Test method for {@link com.mentat.nine.domain.Candidate#createCVForm(java.lang.String, int, 
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
		CVForm cv = candidate.createCVForm(name, age, skills, education, phone, email, desiredSalary, additionalInfo, additionalInfo, desiredSalary);
		assertNotNull(cv);
	}

	/**
	 * Test method for {@link com.mentat.nine.domain.Candidate#sendCVForm(com.mentat.nine.domain.CVForm, 
	 * com.mentat.nine.domain.HRDepartment)}.
	 * @throws PersistException 
	 */
	@Test
	public void testSendCVForm() throws PersistException {
		HRDepartment hrDep = new HRDepartment();
		assertEquals(0, hrDep.getCvs().size());
		Candidate candidate = new Candidate();
		CVForm cv = new CVForm();
		candidate.sendCVForm(cv, hrDep);
		assertEquals(1, hrDep.getCvs().size());
	}

}
