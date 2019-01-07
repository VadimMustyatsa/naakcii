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
			logger.error(exeption.getMessage());
			return null;
		}
	}
}
