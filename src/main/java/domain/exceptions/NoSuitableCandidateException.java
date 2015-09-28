/**
 * 
 */
package domain.exceptions;

/**
 * @author Ruslan
 *
 */
public class NoSuitableCandidateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public NoSuitableCandidateException() {
	}

	/**
	 * @param arg0
	 */
	public NoSuitableCandidateException(String arg0) {
		super(arg0);
	}
}
