/**
 * 
 */
package main.com.mentat.nine;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import main.com.mentat.nine.dao.CandidateDAO;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.domain.ApplicationForm;
import main.com.mentat.nine.domain.CVForm;
import main.com.mentat.nine.domain.Candidate;
import main.com.mentat.nine.domain.HRDepartment;
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
	 */
	public static void main(String[] args) throws NoSuitableCandidateException, PersistException {
		HRDepartment hr = null;
		try {
			hr = new HRDepartment();
		} catch (PersistException e1) {
			e1.printStackTrace();
		}
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

		CandidateDAO dao = new CandidateDAO();
		Set<Candidate> cands = dao.getAllCandidates();
		System.out.println(cands.size());
	//	for (Candidate cand : cands) {
	//		dao.deleteCandidate(cand);
	//
		}
		
		Candidate candidate = new Candidate();
		String name = "name";
		Set<String> skills = requirements;
		String phone = "phone";
		String email = "email";
		int desiredSalary = salary;
		String additionalInfo = "";
		CVForm cv = candidate.formCVForm(name, age, skills, education, phone, email, desiredSalary, additionalInfo, post, workExpirience);
		candidate.sendCVForm(cv, hr);

		
		Set<Candidate> candidates = hr.findCandidates(af);
		for(Candidate cand : candidates){
			System.out.println(cand);
		}

	}

}
