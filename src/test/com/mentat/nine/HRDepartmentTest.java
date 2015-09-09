/**
 * 
 */
package test.com.mentat.nine;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashSet;

import main.com.mentat.nine.dao.ApplicationFormDAO;
import main.com.mentat.nine.dao.CVFormDAO;
import main.com.mentat.nine.dao.CandidateDAO;
import main.com.mentat.nine.dao.EmployeeDAO;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.domain.ApplicationForm;
import main.com.mentat.nine.domain.CVForm;
import main.com.mentat.nine.domain.Candidate;
import main.com.mentat.nine.domain.Department;
import main.com.mentat.nine.domain.Employee;
import main.com.mentat.nine.domain.HRDepartment;
import main.com.mentat.nine.domain.exceptions.NoSuchEmployeeException;
import main.com.mentat.nine.domain.exceptions.NoSuitableCandidateException;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
/**
 * @author Ruslan
 *
 */
public class HRDepartmentTest {

	
	private ApplicationFormDAO appFormDao;
	private CVFormDAO cvDao;
	private CandidateDAO candidateDao;
	private EmployeeDAO employeeDao;
	private HRDepartment hrDep;

	@Before
	public void setUp() throws PersistException{
		try {
			hrDep = new HRDepartment();
		} catch (Exception e) {
			e.printStackTrace();
		}
		appFormDao = mock(ApplicationFormDAO.class);
		candidateDao = mock(CandidateDAO.class);
		employeeDao = mock(EmployeeDAO.class);
		cvDao = mock(CVFormDAO.class);
		hrDep.setCvDao(cvDao);
		hrDep.setCandDao(candidateDao);
		hrDep.setEmpDao(employeeDao);
		hrDep.setAppDao(appFormDao);
	}
	
	@Test
	public void testAddCVForm() throws PersistException {
		CVForm form = mock(CVForm.class);
		CVForm cv1 = new CVForm();
		when(cvDao.createCVForm(form)).thenReturn(cv1);
		assertEquals(cv1, hrDep.addCVForm(form));
	}
	
	@Test
	public void testCreateCandidate() throws PersistException {
		Candidate mockCandidate = mock(Candidate.class);
		Candidate candidate = new Candidate();
		when(candidateDao.createCandidate(mockCandidate)).thenReturn(candidate);
		assertEquals(candidate, hrDep.createCandidate(mockCandidate));
	}
	  
	@Test (expected = NoSuitableCandidateException.class)
	public void testFindCandidates() throws PersistException, NoSuitableCandidateException {
		hrDep.findCandidates(new ApplicationForm());
	}
	
	@Test
	public void testHireEmployee() throws PersistException {
		assertNotNull(hrDep.hireEmployee(new Candidate(), 5000, "post", new Date(), new Department()));
	}
	
	@Test
	public void testCreateEmployee() throws PersistException {
		Employee employeeMock = mock(Employee.class);
		Employee employee = new Employee();
 		when(employeeDao.createEmployee(employeeMock)).thenReturn(employee);
		assertEquals(employee, hrDep.createEmployee(employeeMock));
	}
	
	@Test (expected = NoSuchEmployeeException.class)
	public void testFireEmployee() throws PersistException, NoSuchEmployeeException {
		hrDep.fireEmployee(new Employee(), new Date());
	}
	
	@Test (expected = NoSuchEmployeeException.class)
	public void testChangePost() throws PersistException, NoSuchEmployeeException {
		hrDep.changePost(new Employee(), "post");
	}
	
	@Test (expected = NoSuchEmployeeException.class)
	public void testChangeSalary() throws PersistException, NoSuchEmployeeException {
		hrDep.changeSalary(new Employee(), 3000);
	}
	
	@Test
	public void testUpdateEmployee() throws PersistException {
		Employee employee = mock(Employee.class);
		hrDep.updateEmployee(employee);
		verify(employeeDao).updateEmployee(employee);
	}
	
	@Test
	public void testFormApplicationForm() {
		ApplicationForm app = hrDep.formApplicationForm(25, "education", new HashSet<String>(), 
				"post", 5000, 5, new Date()); 
		assertNotNull(app);
	}

	@Test
	public void testCreateApplicationForm() throws PersistException {
		ApplicationForm appMock = mock(ApplicationForm.class);
		ApplicationForm app = new ApplicationForm(); 
		when(appFormDao.createApplicationForm(appMock)).thenReturn(app);
		assertEquals(app, hrDep.createApplicationForm(appMock));
	}
	
}