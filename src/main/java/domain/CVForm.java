/**
 * 
 */
package domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Ruslan
 *
 */
@Entity
@Table(name="cvform")
public class CVForm extends Person{
			
	@Column(name="name")
	private String name;
	
	@Column(name="skills")
	private Set<String> skills;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="email")
	private String email;
	
	@Column(name="desiredSalary")
	private Integer desiredSalary;
	
	@Column(name="additionalInfo")
	private String additionalInfo;
	
	@Column(name="sendStatus")
	private String sendStatus;
	
	
	/**
	 * 
	 */
	public CVForm() {
	}
	
	public Integer getDesiredSalary() {
		return desiredSalary;
	}
	public void setDesiredSalary(Integer desiredSalary) {
		this.desiredSalary = desiredSalary;
	}
	
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
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

	@Override
	public String toString() {
		return "CVForm [desiredSalary=" + desiredSalary + ", additionalInfo="
				+ additionalInfo + ", id=" + id + ", name=" + name + ", age="
				+ age + ", workExpirience=" + workExpirience + ", skills="
				+ skills + ", education=" + education + ", phone=" + phone
				+ ", email=" + email + ", post=" + post + "]";
	}
}
