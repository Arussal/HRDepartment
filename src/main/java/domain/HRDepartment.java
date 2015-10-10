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
import dao.SkillCandidateDAO;
import dao.SkillEmployeeDAO;
import dao.SkillManagerDAO;
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
	private SkillManagerDAO skillManagerDao;
	private SkillCandidateDAO skillCandidateDao;
	private SkillEmployeeDAO skillEmployeeDao;
		
	private static Logger log = Logger.getLogger(HRDepartment.class);
	
	public HRDepartment(Properties properties)  {
		LogConfig.loadLogConfig(properties);
		daoFactory = DAOFactory.getFactory();
		appDao = daoFactory.getApplicationFormDAO();
		candDao = daoFactory.getCandidateDAO();
		cvDao = daoFactory.getCVFormManagerDAO();
		empDao = daoFactory.getEmployeeDAO();
		skillManagerDao = daoFactory.getSkillManagerDAO();
		skillCandidateDao = daoFactory.getSkillCandidateDAO();
		skillEmployeeDao = daoFactory.getSkillEmployeeDAO();
	}
		

	public CVFormManager addCVForm(CVFormApplicant form) throws PersistException {
		if (null == form) {
			log.error("CVFormApplicant is null");
			throw new IllegalArgumentException();
		}
		CVFormManager cvManager = makeCVFormManager(form);
		
		Set<SkillManagerCV> skillsManagerCV = new HashSet<SkillManagerCV>();
		for(SkillApplicantCV skillApplicantCV : form.getSkills()) {
			String oneSkillApplicantCV = skillApplicantCV.getSkill();
			SkillManagerCV skillManagerCV = new SkillManagerCV();
			skillManagerCV.setSkill(oneSkillApplicantCV);
			skillManagerCV = skillManagerDao.createSkill(skillManagerCV);
			skillsManagerCV.add(skillManagerCV);
		}
		
		cvManager = cvDao.createCVForm(cvManager);
		for (SkillManagerCV skillManagerCV : skillsManagerCV) {
			skillManagerCV.setCvManager(cvManager);
		}
		cvManager.setSkills(skillsManagerCV);
		
		cvDao.updateCVForm(cvManager);
		
		return cvManager;
	}

	
	private CVFormManager makeCVFormManager(CVFormApplicant cv) {
		
		CVFormManager cvManager = new CVFormManager();
		cvManager.setName(cv.getName());
		cvManager.setAge(cv.getAge());
		cvManager.setAdditionalInfo(cv.getAdditionalInfo());
		cvManager.setDesiredSalary(cv.getDesiredSalary());
		cvManager.setEducation(cv.getEducation());
		cvManager.setEmail(cv.getEmail());
		cvManager.setPhone(cv.getPhone());
		cvManager.setPost(cv.getPost());
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
			
			Set<String> cvSkillTitles = new HashSet<String>();
			for(SkillManagerCV skill : cv.getSkills()) {
				cvSkillTitles.add(skill.getSkill());
			}

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
			if (cvSkillTitles.containsAll(app.getRequirements())) {
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
			
			Set<SkillCandidate> candidateSkills = new HashSet<SkillCandidate>();
			
			for (String oneSkillManagerCV : cvSkillTitles) {
				SkillCandidate skillCandidate = new SkillCandidate();		
				skillCandidate.setSkill(oneSkillManagerCV);
				skillCandidate = skillCandidateDao.createSkill(skillCandidate);
				candidateSkills.add(skillCandidate);
			}
				
			log.trace("candidate is found");
			Candidate candidate = new Candidate();
			candidate.setName(cv.getName());
			candidate.setAge(cv.getAge());
			candidate.setEducation(cv.getEducation());
			candidate.setEmail(cv.getEmail());
			candidate.setPhone(cv.getPhone());
			candidate.setPost(cv.getPost());
			candidate.setSkills(candidateSkills);
			candidate.setWorkExpirience(cv.getWorkExpirience());
			candidates.add(candidate);
			candidate = changeCVStatusToCandidate(candidate, cv); 
		
			for(SkillCandidate oneSkillCandidate : candidateSkills) {
				oneSkillCandidate.setCandidate(candidate);
			}
			
			candidate.setSkills(candidateSkills);
			candDao.updateCandidate(candidate);
			
			log.trace("add candidate to set");
		}
			
		if (0 == candidates.size()) {
			log.warn("candidate was not found");
			throw new NoSuitableCandidateException();
		}
		return candidates;
	}
	
	
	public Candidate changeCVStatusToCandidate(Candidate candidate, CVFormManager cv) 
			throws PersistException {
		cvDao.deleteCVForm(cv);
		return candDao.createCandidate(candidate);
	}
	
	
	public Candidate createCandidate(Candidate candidate) throws PersistException {
		return candDao.createCandidate(candidate);
	}

	
	public Employee hireEmployee(Candidate candidate, int salary, String post, 
			Date hireDate, Department department) throws PersistException {
		
		Employee employee = new Employee();
		employee.setAge(candidate.getAge());
		employee.setEducation(candidate.getEducation());
		employee.setEmail(candidate.getEmail());
		employee.setName(candidate.getName());
		employee.setPhone(candidate.getPhone());
		employee.setDepartment(department);
		employee.setPost(post);
		employee.setSalary(salary);
		employee.setHireDate(hireDate);
		employee.setWorkExpirience(candidate.getWorkExpirience());
		log.trace("employee by name " + employee.getName() + " formed");
		
		employee = createEmployee(employee);
		
		Set<SkillEmployee> skills = makeEmployeeSkills(candidate, employee);
		for(SkillEmployee oneSkillEmployee : skills) {
			oneSkillEmployee.setEmployee(employee);
		}
		
		employee.setSkills(skills);
		
		empDao.updateEmployee(employee);
		return employee;
	}
	

	private Set<SkillEmployee> makeEmployeeSkills(Candidate candidate,
			Employee employee) {
		Set<SkillEmployee> skills = new HashSet<SkillEmployee>();
		for (SkillCandidate oneSkillCandidate : candidate.getSkills()){
			String candidateSkillTitle = oneSkillCandidate.getSkill();
			SkillEmployee oneSkillEmployee = new SkillEmployee();
			oneSkillEmployee.setSkill(candidateSkillTitle);
			oneSkillEmployee = skillEmployeeDao.createSkill(oneSkillEmployee);
			skills.add(oneSkillEmployee);
		}
		return skills;
	}


	public Employee createEmployee(Employee employee) {
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
