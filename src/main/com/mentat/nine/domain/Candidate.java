
/**
 * 
 */
package main.com.mentat.nine.domain;

import java.util.Set;

import org.apache.log4j.Logger;

import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.domain.util.LogConfig;

/**
 * @author Ruslan
 *
 */
public class Candidate extends Person{
	
	
	static {
		LogConfig.loadLogConfig();
	}
	
	private static Logger log = Logger.getLogger(Candidate.class);
	
	/**
	 * 
	 */
	public Candidate() {
	}
	
	public Employees formCVForm(String name, int age, Set<String> skills, String education, String phone, 
			String email, int desiredSalary, String additionalInfo, String post, int workExpirience) {
		Employees cv = new Employees();
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
	
	public void sendCVForm(Employees form, HRDepartment hr) throws PersistException{
		hr.addCVForm(form);
	}
}
