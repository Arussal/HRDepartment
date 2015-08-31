/**
 * 
 */
package main.com.mentat.nine.domain;

/**
 * @author Ruslan
 *
 */
public class CVForm extends Person{

	private int desiredSalary;
	private String additionalInfo;
	
	/**
	 * 
	 */
	public CVForm() {
		// TODO Auto-generated constructor stub
	}
	
	public int getDesiredSalary() {
		return desiredSalary;
	}
	public void setDesiredSalary(int desiredSalary) {
		if (desiredSalary < 0) {
			throw new IllegalArgumentException();
		}
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
