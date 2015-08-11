/**
 * 
 */
package test.com.mentat.nine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.com.mentat.nine.dao.ApplicationFormDAO;
import main.com.mentat.nine.dao.CVFormDAO;
import main.com.mentat.nine.dao.CandidateDAO;
import main.com.mentat.nine.dao.DepartmentDAO;
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
	private DepartmentDAO departmentDao;
	private HRDepartment hrDep;
	private Department department;

	@Before
	public void setUp() throws PersistException{
		appFormDao = mock(ApplicationFormDAO.class);
		candidateDao = mock(CandidateDAO.class);
		employeeDao = mock(EmployeeDAO.class);
		try {
			hrDep = new HRDepartment();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddCVForm() throws PersistException {
		CVForm form = mock(CVForm.class);
		CVForm cv1 = new CVForm();
		cvDao = mock(CVFormDAO.class);
		hrDep.setCvDao(cvDao);
		when(cvDao.createCVForm(form)).thenReturn(cv1);
		assertEquals(cv1, hrDep.addCVForm(form));
	}
	  
	@Test
	public void testFindCandidate() throws PersistException, NoSuitableCandidateException {
		Candidate cand = mock(Candidate.class);
		CVForm form = mock(CVForm.class);
		Candidate candidate = new Candidate();
		
		String education = "";
		ApplicationForm app = new ApplicationForm();
		app.setEducation(education);
		when(cvDao.getAllCVForms()).thenReturn(new ArrayList<CVForm>());
		
		
		when(form.getEducation()).thenReturn(education).thenReturn(app.getEducation());
		when(candidateDao.createCandidate(cand)).thenReturn(candidate);
		assertEquals(candidate, hrDep.findCandidate(app));
	}
	
	@Test
	public void testHireEmployee() throws PersistException {
		Employee employee = mock(Employee.class);
		Department department = mock(Department.class);
		Employee employee1 = new Employee();
		Candidate candidate = new Candidate();
		when(employeeDao.createEmployee(employee)).thenReturn(employee1);
		assertNotNull(hrDep.hireEmployee(candidate, 3100, "post", new Date(), department));
	}
	
	@Test
	public void testFireEmployee() throws PersistException, NoSuchEmployeeException {
		Employee employee = mock(Employee.class);
		when(employeeDao.getAllEmployees()).thenReturn(null);
		hrDep.fireEmployee(employee, new Date());
		
	}

}