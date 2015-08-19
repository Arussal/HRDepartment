
/**
 * 
 */
package main.com.mentat.nine.domain;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.domain.util.LogConfig;

/**
 * @author Ruslan
 *
 */
public class Candidate extends Person{
	
	
	static{
	LogConfig.loadLogConfig();
	}
	private static Logger log = Logger.getLogger(Candidate.class);
	/**
	 * 
	 */
	public Candidate() {
		// TODO Auto-generated constructor stub
	}
	
	public CVForm formCVForm(String name, int age, Set<String> skills, String education, String phone, 
			String email, int desiredSalary, String additionalInfo, String post, int workExpirience) {
		CVForm cv = new CVForm();
		cv.setName(name);
		cv.setAge(age);
		cv.setSkills(skills);
		cv.setEducation(education);
		cv.setPhone(phone);
		cv.setEmail(email);
		cv.setDesiredSalary(desiredSalary);
		cv.setAdditionalInfo(additionalInfo);
		cv.setPost(post);
		cv.setWorkExpirience(workExpirience);
		
		log.info("CVForm by name: " + cv.getName() + " formed");
		
		return cv;
	}
	
	public void sendCVForm(CVForm form, HRDepartment hr) throws PersistException{
		hr.addCVForm(form);
	}

	
	static class Test {
		public static void main(String... args) {
			Candidate candidate = new Candidate();
			candidate.formCVForm("asdf", 24, new HashSet<String>(), "education", "phone", "email", 1400, "additionalInfo", "post", 3);
		}
	}
}
