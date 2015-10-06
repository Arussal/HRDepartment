
/**
 * 
 */
package domain;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.ElementCollection;
import javax.persistence.JoinColumn;


/**
 * @author Ruslan
 *
 */
@Entity
@Table(name="candidate")
public class Candidate extends Person{
		
	@Column(name="name")
	private String name;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="skills_table", 
		joinColumns=@JoinColumn(name="id_skill"))
	@Column(name="skill")
	private Set<String> skills;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="email")
	private String email;
	
	
	/**
	 * 
	 */
	public Candidate() {
	}
	
	public CVFormApplicant formCVForm(String name, int age, Set<String> skills, String education, 
			String phone, String email, int desiredSalary, String additionalInfo, String post, 
			int workExpirience) {
		CVFormApplicant cv = new CVFormApplicant();
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
		cv.setSendStatus("not Sent");
		return cv;
	}
	
	public void sendCVForm(CVFormApplicant form, HRDepartment hr) {
		form.setSendStatus("Sent");
		hr.addCVForm(form);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getSkills() {
		return skills;
	}

	public void setSkills(Set<String> skills) {
		this.skills = skills;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
