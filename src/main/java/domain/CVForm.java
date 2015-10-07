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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * @author Ruslan
 *
 */
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name="cvform")
public class CVForm {
	
	@Id
	@GeneratedValue(generator="LICENSE_TABLE_SEQ",strategy=GenerationType.TABLE)
	@TableGenerator(name="LICENSE_TABLE_SEQ",
	table="sequences",
	pkColumnName="seq_name", // Specify the name of the column of the primary key
	valueColumnName="seq_number", // Specify the name of the column that stores the last value generated
	pkColumnValue="id", // Specify the primary key column value that would be considered as a primary key generator
	allocationSize=1)
	protected Integer id;
	
	@Column(name="age")
	protected Integer age;
	
	@Column(name="work_expirience")
	protected Integer workExpirience;
	
	@Column(name="education")
	protected String education;
	
	@Column(name="post")
	protected String post;
			
	@Column(name="name")
	private String name;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="cvform_skills", 
		joinColumns=@JoinColumn(name="id_skill"))
	@Column(name="skill")
	private Set<String> skills;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="email")
	private String email;
	
	@Column(name="desired_salary")
	private Integer desiredSalary;
	
	@Column(name="additional_info")
	private String additionalInfo;
	
	
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

	@Override
	public String toString() {
		return "CVForm [desiredSalary=" + desiredSalary + ", additionalInfo="
				+ additionalInfo + ", id=" + id + ", name=" + name + ", age="
				+ age + ", workExpirience=" + workExpirience + ", skills="
				+ skills + ", education=" + education + ", phone=" + phone
				+ ", email=" + email + ", post=" + post + "]";
	}
}
