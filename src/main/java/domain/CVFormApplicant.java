/**
 * 
 */
package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
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
	

	@Override
	public String toString() {
		return "CVForm [desiredSalary=" + getDesiredSalary() + ", additionalInfo="
				+ getAdditionalInfo() + ", id=" + id + ", name=" + getName() + ", age="
				+ getAge() + ", workExpirience=" + getWorkExpirience() + ", skills="
				+ getSkills() + ", education=" + getEducation() + ", phone=" + getPhone()
				+ ", email=" + getEmail() + ", post=" + getPost() + "]";
	}
}
