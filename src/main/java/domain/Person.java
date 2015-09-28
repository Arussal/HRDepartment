/**
 * 
 */
package domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author Ruslan
 *
 */
@MappedSuperclass
public class Person {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer id;
	
	//protected String name;


	@Column(name="age")
	protected Integer age;
	
	@Column(name="work_expirience")
	protected Integer workExpirience;
	
	//protected Set<String> skills;
	
	@Column(name="education")
	protected String education;
	//protected String phone;
	//protected String email;
	
	@Column(name="post")
	protected String post;

	/**
	 * 
	 */
	public Person() {
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result
				+ ((education == null) ? 0 : education.hashCode());
		result = prime * result + ((post == null) ? 0 : post.hashCode());
		result = prime * result
				+ ((workExpirience == null) ? 0 : workExpirience.hashCode());
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
		Person other = (Person) obj;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (education == null) {
			if (other.education != null)
				return false;
		} else if (!education.equals(other.education))
			return false;
		if (post == null) {
			if (other.post != null)
				return false;
		} else if (!post.equals(other.post))
			return false;
		if (workExpirience == null) {
			if (other.workExpirience != null)
				return false;
		} else if (!workExpirience.equals(other.workExpirience))
			return false;
		return true;
	}
}
