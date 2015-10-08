/**
 * 
 */
package domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Ruslan
 *
 */
@Entity
@Table(name="cvform_manager_skills")
public class SkillManagerCV extends Skill{
	
	@ManyToOne(targetEntity = SkillManagerCV.class)
	@JoinColumn(name="id_manager", referencedColumnName="id")
	private Manager manager;

	public SkillManagerCV(){	
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getSkill() == null) ? 0 : getSkill().hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SkillApplicantCV other = (SkillApplicantCV) obj;
		if (getSkill() == null) {
			if (other.getSkill() != null)
				return false;
		} else if (!getSkill().equals(other.getSkill()))
			return false;
		return true;
	}

}
