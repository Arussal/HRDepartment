/**
 * 
 */
package dao.exceptions;

/**
 * @author Ruslan
 *
 */
public class NoSuitableDBPropertiesException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSuitableDBPropertiesException() {
		super();
	}

	public NoSuitableDBPropertiesException(String message) {
		super(message);
	}

}
