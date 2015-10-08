/**
 * 
 */
package domain;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import dao.ApplicationFormDAO;
import dao.CVFormManagerDAO;
import dao.CandidateDAO;
import dao.EmployeeDAO;
import dao.exceptions.PersistException;
import dao.util.DAOFactory;
import domain.exceptions.NoSuchEmployeeException;
import domain.exceptions.NoSuitableCandidateException;
import domain.util.LogConfig;

/**
 * @author Ruslan
 *
 */
public class HRDepartment extends Department implements HRManager{

	private DAOFactory daoFactory;
	private Set<Employee> staff;
	
	private List<ApplicationForm> apps;
	private CVFormManagerDAO cvDao;
	private EmployeeDAO empDao;
	private ApplicationFormDAO appDao;
	private CandidateDAO candDao;
		
	private static Logger log = Logger.getLogger(HRDepartment.class);
	
	public HRDepartment(Properties properties)  {
		LogConfig.loadLogConfig(properties);
		daoFactory = DAOFactory.getFactory();
		appDao = daoFactory.getApplicationFormDAO();
		candDao = daoFactory.getCandidateDAO();
		cvDao = daoFactory.getCVFormDAO();
		empDao = daoFactory.getEmployeeDAO();
	}
		

	public CVFormManager addCVForm(CVFormApplicant form) {
		if (null == form) {
			log.error("CVForm is null");
			throw new IllegalArgumentException();
		}
		CVFormManager cvManager = makeCVForHRDepartment(form);
		return cvDao.createCVForm(cvManager);
	}

	
	private CVFormManager makeCVForHRDepartment(CVFormApplicant cv) {
		CVFormManager cvManager = new CVFormManager();
		cvManager.setName(cv.getName());
		cvManager.setAge(cv.getAge());
		cvManager.setAdditionalInfo(cv.getAdditionalInfo());
		cvManager.setDesiredSalary(cv.getDesiredSalary());
		cvManager.setEducation(cv.getEducation());
		cvManager.setEmail(cv.getEmail());
		cvManager.setPhone(cv.getPhone());
		cvManager.setPost(cv.getPost());
		cvManager.setSkills(cv.getSkills());
		cvManager.setWorkExpirience(cv.getWorkExpirience());
		return cvManager;
	}
	
	
	public Set<Candidate> findCandidates(ApplicationForm app) 
			throws NoSuitableCandidateException, PersistException {
		
		Set<Candidate> candidates = new HashSet<Candidate>();

		Map<String, Boolean> conditions = new HashMap<String, Boolean>();
		conditions.put("acceptAge", new Boolean(false));
		conditions.put("acceptWorkExperience", new Boolean(false));
		conditions.put("acceptEducation", new Boolean(false));
		conditions.put("acceptSkills", new Boolean(false));
		conditions.put("acceptPost", new Boolean(false));
		conditions.put("acceptSalary", new Boolean(false));
		
		List<CVFormManager> cvs = cvDao.getAllCVForms();
		log.trace("check conditions to find candidate");
		
		
		outer: for (CVFormManager cv : cvs) {

			// set "false" flag to all conditions
			for (Entry<String, Boolean> condition : conditions.entrySet()) {
				condition.setValue(new Boolean(false));
			}

			// check all conditions
			if (Math.abs(cv.getAge() - app.getAge()) < 2) {
				conditions.put("acceptAge", new Boolean(true));	
			}
			if (cv.getWorkExpirience() >= app.getWorkExpirience()) {
				conditions.put("acceptWorkExperience", new Boolean(true));
			}
			if (cv.getEducation().equals(app.getEducation())) {		
				conditions.put("acceptEducation", new Boolean(true));
			}
			if (cv.getSkills().containsAll(app.getRequirements())) {
				conditions.put("acceptSkills", new Boolean(true));
			}
			if (cv.getPost().equals(app.getPost())) {
				conditions.put("acceptPost", new Boolean(true));
			} 
			if (cv.getDesiredSalary() <= app.getSalary()) {
				conditions.put("acceptSalary", new Boolean(true));
			}
 
			for (String condition : conditions.keySet()) {
				if (conditions.get(condition) == false) {
					log.trace("condition is failed, next cvForm");
					continue outer;
				}
			}
			
			log.trace("candidate is found");
			Candidate candidate = new Candidate();
			candidate.setName(cv.getName());
			candidate.setAge(cv.getAge());
			candidate.setEducation(cv.getEducation());
			candidate.setEmail(cv.getEmail());
			candidate.setPhone(cv.getPhone());
			candidate.setPost(cv.getPost());
			candidate.setSkills(cv.getSkills());
			candidate.setWorkExpirience(cv.getWorkExpirience());
			candidates.add(candidate);
			changeCVStatusToCandidate(candidate, cv); 
		
			log.trace("add candidate to set");
		}
			
		if (0 == candidates.size()) {
			log.warn("candidate was not found");
			throw new NoSuitableCandidateException();
		}
		return candidates;
	}
	
	
	public void changeCVStatusToCandidate(Candidate candidate, CVFormManager cv) 
			throws PersistException {
			candDao.createCandidate(candidate);
			cvDao.deleteCVForm(cv);
	}
	
	
	public Candidate createCandidate(Candidate candidate) throws PersistException {
		return candDao.createCandidate(candidate);
	}

	
	public Employee hireEmployee(Candidate candidate, int salary, String post, 
			Date hireDate, Department department) {
		
		Employee employee = new Employee();
		employee.setAge(candidate.getAge());
		employee.setEducation(candidate.getEducation());
		employee.setEmail(candidate.getEmail());
		employee.setName(candidate.getName());
		employee.setPhone(candidate.getPhone());
		employee.setSkills(candidate.getSkills());
		employee.setDepartment(department);
		employee.setPost(post);
		employee.setSalary(salary);
		employee.setHireDate(hireDate);
		log.trace("employee by name " + employee.getName() + " formed");

		return employee;
	}
	

	public Employee createEmployee(Employee employee) throws PersistException {
		return empDao.createEmployee(employee);
	}
	

	public void fireEmployee(Employee employee, Date fireDate) 
			throws PersistException, NoSuchEmployeeException {
		
		staff = empDao.getAllEmployees();
		if (!staff.contains(employee)) {
			log.warn("employee with id " + employee.getId() + " not found");
			throw new NoSuchEmployeeException();
		}
		for (Employee emp : staff) {
			if (emp.equals(employee)) {
				employee.setFireDate(fireDate);
			}
		}		
		
		updateEmployee(employee);
	}


	public void changeSalary(Employee employee, int salary) 
			throws NoSuchEmployeeException, PersistException {
		
		staff = empDao.getAllEmployees();
		if (!staff.contains(employee)) {
			log.warn("employee with id " + employee.getId() + " not found");
			throw new NoSuchEmployeeException();
		}
		for (Employee emp : staff) {
			if (emp.equals(employee)) {
				employee.setSalary(salary);
			}
		}
		
		updateEmployee(employee);
	}


	public void changePost(Employee employee, String post) throws NoSuchEmployeeException, PersistException {
	
		staff = empDao.getAllEmployees();
		if (!staff.contains(employee)) {
			log.warn("employee with id " + employee.getId() + " not found");
			throw new NoSuchEmployeeException();
		}
		for (Employee emp : staff) {
			if (emp.equals(employee)) {
				employee.setPost(post);
			}
		}
		
		updateEmployee(employee);
	}
	

	public void updateEmployee(Employee employee) throws PersistException {
		log.info("employee with id " + employee.getId() + " updated");
		empDao.updateEmployee(employee); 
	}
	

	public ApplicationForm formApplicationForm(int age, String education, Set<String> requirements, 
			String post, int salary, int workExpirience, Date date){
		
		ApplicationForm app = new ApplicationForm();
		app.setDate(date);
		app.setAge(age);
		app.setEducation(education);
		app.setPost(post);
		app.setRequirements(requirements);
		app.setSalary(salary);
		app.setWorkExpirience(workExpirience);
		log.trace("ApplicationForm to post " + post + " formed");
		
		return app;
	}
	

	public ApplicationForm createApplicationForm(ApplicationForm app) throws PersistException {
		ApplicationForm createdApplicationForm = appDao.createApplicationForm(app);
		return createdApplicationForm;
	}
	
	public Set<Employee> getStaff() {
		return staff;
	}

	public void setStaff(Set<Employee> staff) {
		if (null == staff) {
			throw new IllegalArgumentException("set Staff is null");
		}
		this.staff = staff;
	}

	public List<ApplicationForm> getApps() {
		return apps;
	}

	public void setApps(List<ApplicationForm> apps) {
		if (null == apps) {
			throw new IllegalArgumentException(("set Apps is null"));
		}
		this.apps = apps;
	}


	public CandidateDAO getCandDao() {
		return candDao;
	}

	public void setCandDao(CandidateDAO candDao) {
		if (null == candDao) {
			throw new IllegalArgumentException("cvDao is null");
		}
		this.candDao = candDao;
	}

	public EmployeeDAO getEmpDao() {
		return empDao;
	}

	public void setEmpDao(EmployeeDAO empDao) {
		if (null == empDao) {
			throw new IllegalArgumentException("empDao is null");
		}
		this.empDao = empDao;
	}

	public ApplicationFormDAO getAppDao() {
		return appDao;
	}

	public void setAppDao(ApplicationFormDAO appDao) {
		if (null == appDao) {
			throw new IllegalArgumentException("cvDao is null");
		}
		this.appDao = appDao;
	}

}
