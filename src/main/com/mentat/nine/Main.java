/**
 * 
 */
package main.com.mentat.nine;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import main.com.mentat.nine.dao.ApplicationFormDAO;
import main.com.mentat.nine.dao.CVFormDAO;
import main.com.mentat.nine.dao.CandidateDAO;
import main.com.mentat.nine.dao.DepartmentDAO;
import main.com.mentat.nine.dao.EmployeeDAO;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.ApplicationForm;
import main.com.mentat.nine.domain.CVForm;
import main.com.mentat.nine.domain.Candidate;
import main.com.mentat.nine.domain.Department;
import main.com.mentat.nine.domain.Employee;
import main.com.mentat.nine.domain.HRDepartment;
import main.com.mentat.nine.domain.exceptions.NoSuchEmployeeException;
import main.com.mentat.nine.domain.exceptions.NoSuitableCandidateException;

/**
 * @author Ruslan
 *
 */
public class Main {

	

	/**
	 * @param args
	 * @throws PersistException 
	 * @throws NoSuitableCandidateException 
	 * @throws NoSuchEmployeeException 
	 */
	public static void main(String[] args) throws PersistException, NoSuitableCandidateException, NoSuchEmployeeException {
		DAOFactory daoF = DAOFactory.getFactory();
		ApplicationFormDAO adao = daoF.getApplicationFormDAO();
		DepartmentDAO ddao = daoF.getDepartmentDAO();
		CandidateDAO cdao = daoF.getCandidateDAO();
		CVFormDAO cvdao = daoF.getCVFormDAO();
		EmployeeDAO edao = daoF.getEmployeeDAO();
		
		HRDepartment hr = new HRDepartment();
		int age = 30;
		String education = "high";
		Set<String> requirements = new HashSet<String>();
		requirements.add("requirement");
		String post = "post";
		int salary = 2000;
		int workExpirience = 5;
		Date date = new Date();
		ApplicationForm af = hr.formApplicationForm(age, education, requirements, post, salary, workExpirience, date);
			af = hr.createApplicationForm(af);

		// create candidate
		Candidate candidate = new Candidate();
		String name = "Ivan";
		Set<String> skills = requirements;
		String phone = "phone";
		String email = "email";
		int desiredSalary = 2000;
		String additionalInfo = "additional";
		CVForm cv = candidate.formCVForm(name, age, skills, education, phone, email, 
				desiredSalary, additionalInfo, post, workExpirience);
		cv = hr.addCVForm(cv);
		
		System.out.println("id cv in main " + cv.getId());
		
		CVForm cv1 = candidate.formCVForm("Vova", 56, skills, "high", phone, email, 
				desiredSalary, additionalInfo, post, workExpirience);
		cv1 = hr.addCVForm(cv1);
		
		System.out.println("id cv1 in main " + cv1.getId());

		// find candidates
		Set<Candidate> candidates = hr.findCandidates(af);
		
		Department department = new Department();
		department.setName("Department1");
		department.setHead("Head1");
		
		
		department = ddao.createDepartment(department);

		Employee employee = null;
		
		for(Candidate cand : candidates) {
			employee = hr.hireEmployee(cand, desiredSalary, post, new Date(), department);
			employee = hr.createEmployee(employee);
		}

		hr.changePost(employee, "new Post");
		hr.changeSalary(employee, 8000);
		
		hr.fireEmployee(employee, new Date());
		
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		adao.deleteApplicationForm(af);
		cvdao.deleteCVForm(cv);
		cvdao.deleteCVForm(cv1);
		ddao.deleteDepartment(department);
		for (Candidate cand : candidates) {
			cdao.deleteCandidate(cand);
		}
		edao.deleteEmployee(employee);
		
	}

}
