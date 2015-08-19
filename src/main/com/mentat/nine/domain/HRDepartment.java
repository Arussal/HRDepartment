/**
 * 
 */
package main.com.mentat.nine.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import main.com.mentat.nine.dao.ApplicationFormDAO;
import main.com.mentat.nine.dao.CVFormDAO;
import main.com.mentat.nine.dao.CandidateDAO;
import main.com.mentat.nine.dao.EmployeeDAO;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.exceptions.NoSuchEmployeeException;
import main.com.mentat.nine.domain.exceptions.NoSuitableCandidateException;
import main.com.mentat.nine.domain.util.LogConfig;

/**
 * @author Ruslan
 *
 */
public class HRDepartment extends Department implements HRManager{

	private DAOFactory daoFactory;
	private Set<Employee> staff;
	
	private List<ApplicationForm> apps;
	private CVFormDAO cvDao;
	private EmployeeDAO empDao;
	private ApplicationFormDAO appDao;
	private CandidateDAO candDao;
	
	/**
	 * @throws PersistException 
	 * 
	 */
	
	static{
		LogConfig.loadLogConfig();
	}
	private static Logger log = Logger.getLogger(HRDepartment.class);
	
	public HRDepartment() throws PersistException {
		daoFactory = DAOFactory.getFactory();
		appDao = daoFactory.getApplicationFormDAO();
		candDao = daoFactory.getCandidateDAO();
		cvDao = daoFactory.getCVFormDAO();
		empDao = daoFactory.getEmployeeDAO();
	}
		
	@Override
	public CVForm addCVForm(CVForm form) throws PersistException {
		if (null == form) {
			log.error("CVForm is null");
			throw new IllegalArgumentException();
		}
		log.info("CVForm by name " + form.getName() + " created");
		return cvDao.createCVForm(form);
	}

	@Override
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
		
		List<CVForm> cvs = cvDao.getAllCVForms();
		log.trace("get all cvForms");
		
		
		outer: for (CVForm cv : cvs) {

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
			createCandidate(candidate);
			log.info("candidate created, id: " + candidate.getId());
			candidates.add(candidate);
			log.trace("add candidate to set");
		}
			
		if (0 == candidates.size()) {
			log.error("candidate was not found");
			throw new NoSuitableCandidateException();
		}
		return candidates;
	}
	
	@Override
	public Candidate createCandidate(Candidate candidate) throws PersistException {
		return candDao.createCandidate(candidate);
	}

	@Override
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
		
		return employee;
	}
	
	@Override
	public Employee createEmployee(Employee employee) throws PersistException {
		return empDao.createEmployee(employee);
	}
	
	@Override
	public void fireEmployee(Employee employee, Date fireDate) 
			throws PersistException, NoSuchEmployeeException {
		
		staff = empDao.getAllEmployees();
		if (!staff.contains(employee)) {
			throw new NoSuchEmployeeException();
		}
		for (Employee emp : staff) {
			if (emp.equals(employee)) {
				employee.setFireDate(fireDate);
			}
		}		
		
		updateEmployee(employee);
	}

	@Override
	public void changeSalary(Employee employee, int salary) 
			throws NoSuchEmployeeException, PersistException {
		
		staff = empDao.getAllEmployees();
		if (!staff.contains(employee)) {
			throw new NoSuchEmployeeException();
		}
		for (Employee emp : staff) {
			if (emp.equals(employee)) {
				employee.setSalary(salary);
			}
		}
		
		updateEmployee(employee);
	}

	@Override
	public void changePost(Employee employee, String post) throws NoSuchEmployeeException, PersistException {
	
		staff = empDao.getAllEmployees();
		if (!staff.contains(employee)) {
			throw new NoSuchEmployeeException();
		}
		for (Employee emp : staff) {
			if (emp.equals(employee)) {
				employee.setPost(post);
			}
		}
		
		updateEmployee(employee);
	}
	
	@Override
	public void updateEmployee(Employee employee) throws PersistException {
		empDao.updateEmployee(employee);
	}
	
	@Override
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
		
		return app;
	}
	
	@Override
	public ApplicationForm createApplicationForm(ApplicationForm app) throws PersistException {
		return appDao.createApplicationForm(app);
	}
	
	public Set<Employee> getStaff() {
		return staff;
	}

	public void setStaff(Set<Employee> staff) {
		this.staff = staff;
	}

	public List<ApplicationForm> getApps() {
		return apps;
	}

	public void setApps(List<ApplicationForm> apps) {
		this.apps = apps;
	}

	public CVFormDAO getCvDao() {
		return cvDao;
	}

	public void setCvDao(CVFormDAO cvDao) {
		this.cvDao = cvDao;
	}

	public CandidateDAO getCandDao() {
		return candDao;
	}

	public void setCandDao(CandidateDAO candDao) {
		this.candDao = candDao;
	}

	public EmployeeDAO getEmpDao() {
		return empDao;
	}

	public void setEmpDao(EmployeeDAO empDao) {
		this.empDao = empDao;
	}

	public ApplicationFormDAO getAppDao() {
		return appDao;
	}

	public void setAppDao(ApplicationFormDAO appDao) {
		this.appDao = appDao;
	}
}
