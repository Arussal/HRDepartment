/**
 * 
 */
package test.com.mentat.nine;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import main.com.mentat.nine.dao.CVFormDAO;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.domain.CVForm;
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
	private Properties properties;

	@Before
	public void setUp() throws PersistException {
		cvDao = mock(CVFormDAO.class);
		properties = mock(Properties.class);
	}

	/**
	 * Test method for {@link main.com.mentat.nine.domain.Candidate#createCVForm(java.lang.String, int, 
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String)}.
	 */
	@Test
	public void testCreateCVForm() {
		Candidate candidate = new Candidate(properties);
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
		CVForm cv = candidate.formCVForm(name, age, skills, education, phone, email, 
				desiredSalary, additionalInfo, additionalInfo, desiredSalary);
		assertNotNull(cv);
	}

	/**
	 * Test method for {@link main.com.mentat.nine.domain.Candidate#sendCVForm(main.com.mentat.nine.domain.CVForm, 
	 * main.com.mentat.nine.domain.HRDepartment)}.
	 * @throws PersistException 
	 */
	@Test
	public void testSendCVForm() throws PersistException {
		HRDepartment hrDep = new HRDepartment(properties);
		hrDep.setCvDao(cvDao);
		CVForm cvMock = mock(CVForm.class);
		Candidate candidate = new Candidate(properties);
		candidate.sendCVForm(cvMock, hrDep);
		verify(cvDao).createCVForm(cvMock);
	}

}
