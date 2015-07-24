/**
 * 
 */
package com.mentat.nine.domain;

import java.util.Set;

/**
 * @author Ruslan
 *
 */
public class Candidate extends Person{
	
	/**
	 * 
	 */
	public Candidate() {
		// TODO Auto-generated constructor stub
	}
	
	public CVForm createCVForm(String name, int age, Set<String> skills, String education, String phone, 
			String email, int desiredSalary, String additionalInfo) {
		CVForm cv = new CVForm();
		cv.setName(name);
		cv.setAge(age);
		cv.setSkills(skills);
		cv.setEducation(education);
		cv.setPhone(phone);
		cv.setEmail(email);
		cv.setDesiredSalary(desiredSalary);
		cv.setAdditionalInfo(additionalInfo);
		return cv;
	}
	
	public void sendCVForm(CVForm form, HRDepartment hr){
		hr.addCVForm(form);
	}

}
