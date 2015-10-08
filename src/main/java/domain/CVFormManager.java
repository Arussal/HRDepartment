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
@Table(name="cvform")
public class CVFormManager extends CVForm {
				
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="cvform_manager_skills", 
		joinColumns=@JoinColumn(name="id_skill"))
	@Column(name="skill")
	private Set<String> skills;
	
	
	public CVFormManager() {
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
				+ age + ", workExpirience=" + workExpirience + ", skills="
				+ skills + ", education=" + education + ", phone=" + getPhone()
				+ ", email=" + getEmail() + ", post=" + post + "]";
	}
}
