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
	
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
}
