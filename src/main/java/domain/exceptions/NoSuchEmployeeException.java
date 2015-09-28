/**
 * 
 */
package domain.exceptions;

/**
 * @author Ruslan
 *
 */
public class NoSuchEmployeeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public NoSuchEmployeeException() {
	}

	/**
	 * @param arg0
	 */
	public NoSuchEmployeeException(String arg0) {
		super(arg0);
	}

}
