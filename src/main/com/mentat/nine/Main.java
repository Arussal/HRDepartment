/**
 * 
 */
package main.com.mentat.nine;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.domain.ApplicationForm;
import main.com.mentat.nine.domain.HRDepartment;

/**
 * @author Ruslan
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
		try {
			af = hr.createApplicationForm(af);
		} catch (PersistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
