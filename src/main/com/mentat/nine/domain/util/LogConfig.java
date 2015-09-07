/**
 * 
 */
package main.com.mentat.nine.domain.util;


/**
 * @author Ruslan
 *
 */
public class LogConfig {

	// TODO make relative path
	public final static String PATH = "C:\\workspace\\HRDepartment\\resources\\log4j.properties";

	public static void loadLogConfig() {
			org.apache.log4j.PropertyConfigurator.configure(PATH);
	}
	
}
