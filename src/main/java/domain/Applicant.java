package domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * @author Ruslan
 *
 */
@Entity
@Table(name="applicant")
@SessionAttributes("applicant")
public class Applicant {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank 
	@Column(name="surname")
	private String surname;
	
	@NotBlank 
	@Column(name="name")
	private String name;
	
	@NotBlank 
	@Column(name="last_name")
	private String lastName;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@Past
	@Column(name="birthday")
	private Date birthDay;
	
	@NotBlank 
	@Column(name="education")
	private String education;
	
	@NotBlank
	@Digits(fraction = 0, integer = 12)
	@Column(name="phone")
	private String phone;
	
	@NotBlank
	@Email 
	@Column(name="email")
	private String email;
	
	@Size(min=6, max=10)
	@Column(name="login")
	private String login;
	
	
	@Size(min=8, max=14)
	@Column(name="password")
	private String password;
	
	public Applicant() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}