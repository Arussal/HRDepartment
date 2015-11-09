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
@Table(name="candidate_skills")
public class SkillCandidate extends Skill{
	
	@ManyToOne
	@JoinColumn(name="id_candidate", referencedColumnName="id")
	private Candidate candidate;

	public SkillCandidate() {
	}
	
	
	public Candidate getCandidate() {
		return candidate;
	}


	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
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
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return getName();
	}
}
