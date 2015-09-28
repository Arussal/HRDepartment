/**
 * 
 */
package domain.util;

import java.util.Properties;


/**
 * @author Ruslan
 *
 */
public class LogConfig {

	public static void loadLogConfig(Properties properties) {
			org.apache.log4j.PropertyConfigurator.configure(properties);
	}
	
}
