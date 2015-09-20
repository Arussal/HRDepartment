/**
 * 
 */
package main.com.mentat.nine.domain.util;


/**
 * @author Ruslan
 *
 */
public class LogConfig {

	public static void loadLogConfig(String path) {
			org.apache.log4j.PropertyConfigurator.configure(path);
	}
	
}
