/**
 * 
 */
package main.com.mentat.nine.domain;

/**
 * @author Ruslan
 *
 */
public class CVForm extends Person{

	private Integer desiredSalary;
	private String additionalInfo;
	private String sendStatus;
	
	/**
	 * 
	 */
	public CVForm() {
		// TODO Auto-generated constructor stub
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
	
	
	@Override
	public String toString() {
		return "CVForm [desiredSalary=" + desiredSalary + ", additionalInfo="
				+ additionalInfo + ", id=" + id + ", name=" + name + ", age="
				+ age + ", workExpirience=" + workExpirience + ", skills="
				+ skills + ", education=" + education + ", phone=" + phone
				+ ", email=" + email + ", post=" + post + "]";
	}
}
