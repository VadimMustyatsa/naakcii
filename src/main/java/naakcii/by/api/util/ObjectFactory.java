package naakcii.by.api.util;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ObjectFactory {
	
	private final Logger logger = LogManager.getRootLogger();
	
	public<T> T getInstance(Class<T> clazz, Object ... args) {
		try {
			Class<?>[] arrayOfArgsAsClasses = Arrays.stream(args).map(Object::getClass).toArray(Class[]::new);
			return clazz.getConstructor(arrayOfArgsAsClasses).newInstance(args);
		} catch(Exception exeption) {
			logger.error("Exception has occurred during the process of creating new instance of '{}': {}." , 
					clazz, printStackTrace(exeption));
			return null;
		}
	}
	
	private String printStackTrace(Exception exception) {
		StringBuilder stackTrace = new StringBuilder();
		stackTrace.append(exception.getClass().getName()).append(" - ").append(exception.getMessage());
		
		for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
			stackTrace.append(System.lineSeparator());
			stackTrace.append("\t").append(stackTraceElement);
		}
	
		return stackTrace.toString();
	}
}
