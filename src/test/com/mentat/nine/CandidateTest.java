/**
 * 
 */
package test.com.mentat.nine;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import main.com.mentat.nine.dao.CVFormDAO;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.domain.Employees;
import main.com.mentat.nine.domain.Candidate;
import main.com.mentat.nine.domain.HRDepartment;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * @author Ruslan
 *
 */
public class CandidateTest {
	
	private CVFormDAO cvDao;

	@Before
	public void setUp() throws PersistException {
		cvDao = mock(CVFormDAO.class);
	}

	/**
	 * Test method for {@link main.com.mentat.nine.domain.Candidate#createCVForm(java.lang.String, int, 
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
		Employees cv = candidate.formCVForm(name, age, skills, education, phone, email, 
				desiredSalary, additionalInfo, additionalInfo, desiredSalary);
		assertNotNull(cv);
	}

	/**
	 * Test method for {@link main.com.mentat.nine.domain.Candidate#sendCVForm(main.com.mentat.nine.domain.Employees, 
	 * main.com.mentat.nine.domain.HRDepartment)}.
	 * @throws PersistException 
	 */
	@Test
	public void testSendCVForm() throws PersistException {
		HRDepartment hrDep = new HRDepartment();
		hrDep.setCvDao(cvDao);
		Employees cvMock = mock(Employees.class);
		Candidate candidate = new Candidate();
		candidate.sendCVForm(cvMock, hrDep);
		verify(cvDao).createCVForm(cvMock);
	}

}
