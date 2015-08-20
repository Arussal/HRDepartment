/**
 * 
 */
package main.com.mentat.nine.domain.util;

/**
 * @author Ruslan
 *
 */
public class LogConfig {
	private final static String PATH = "./resources/log4j.properties";
	
	public static void loadLogConfig() {
		org.apache.log4j.PropertyConfigurator.configure(PATH);
	}
}
