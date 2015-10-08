/**
 * 
 */
package domain;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

/**
 * @author Ruslan
 *
 */
@Entity
@Table(name="cvform_applicant")
public class CVFormApplicant extends CVForm{
			
	@Column(name="send_status")
	private String sendStatus;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="cvform_applicant_skills", 
		joinColumns=@JoinColumn(name="id_skill"))
	@Column(name="skill")
	private Set<String> skills;
	

	/**
	 * 
	 */
	public CVFormApplicant() {
	}
	
	
	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}
	
	public Set<String> getSkills() {
		return skills;
	}

	public void setSkills(Set<String> skills) {
		this.skills = skills;
	}
	

	@Override
	public String toString() {
		return "CVForm [desiredSalary=" + getDesiredSalary() + ", additionalInfo="
				+ getAdditionalInfo() + ", id=" + id + ", name=" + getName() + ", age="
				+ getAge() + ", workExpirience=" + getWorkExpirience() + ", skills="
				+ getSkills() + ", education=" + getEducation() + ", phone=" + getPhone()
				+ ", email=" + getEmail() + ", post=" + getPost() + "]";
	}
}
