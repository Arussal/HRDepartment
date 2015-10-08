/**
 * 
 */
package domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Ruslan
 *
 */
@Entity
@Table(name="cvform_applicant")
public class CVFormApplicant {
	

	/**
	 * 
	 */
	public CVFormApplicant() {
	}
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer id;

	@Column(name="age")
	protected Integer age;
	
	@Column(name="work_expirience")
	protected Integer workExpirience;
	
	@Column(name="education")
	protected String education;
	
	@Column(name="post")
	protected String post;
	
	@Column(name="send_status")
	private String sendStatus;
	
	@OneToMany(targetEntity = SkillApplicantCV.class, mappedBy = "cvApplicant", 
    cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<SkillApplicantCV> skills;
	
	@Column(name="name")
	private String name;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="email")
	private String email;
	
	@Column(name="desired_salary")
	private Integer desiredSalary;
	
	@Column(name="additional_info")
	private String additionalInfo;
	
	

		
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}
	
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getWorkExpirience() {
		return workExpirience;
	}


	public void setWorkExpirience(Integer workExpirience) {
		this.workExpirience = workExpirience;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}
	
	public Set<SkillApplicantCV> getSkills() {
		return skills;
	}

	public void setSkills(Set<SkillApplicantCV> skills) {
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
