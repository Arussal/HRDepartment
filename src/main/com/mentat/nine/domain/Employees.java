/**
 * 
 */
package main.com.mentat.nine.domain;

/**
 * @author Ruslan
 *
 */
public class Employees extends Person{

	private Integer desiredSalary;
	private String additionalInfo;
	
	/**
	 * 
	 */
	public Employees() {
		// TODO Auto-generated constructor stub
	}
	
	public Integer getDesiredSalary() {
		return desiredSalary;
	}
	public void setDesiredSalary(Integer desiredSalary) {
		this.desiredSalary = desiredSalary;
	}
	
	@Override
	public String toString() {
		return "CVForm [desiredSalary=" + desiredSalary + ", additionalInfo="
				+ additionalInfo + ", id=" + id + ", name=" + name + ", age="
				+ age + ", workExpirience=" + workExpirience + ", skills="
				+ skills + ", education=" + education + ", phone=" + phone
				+ ", email=" + email + ", post=" + post + "]";
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
}
