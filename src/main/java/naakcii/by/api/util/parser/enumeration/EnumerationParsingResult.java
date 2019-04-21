package naakcii.by.api.util.parser.enumeration;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.ConstraintViolation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import naakcii.by.api.util.parser.ParsingResult;

/**
 * Provides the model of the {@link naakcii.by.api.util.parser.ParsingResult} abstract class for 
 * use, when information is retrieved from the <i>enumeration class</i>.
 * Accumulates detailed information about the <i>parsing process</i> of instances, that all belong 
 * to the specific class {@code T}, provides utility method for writing the {@code parsing result} 
 * in a readable format.
 * <p>
 * The <i>parsing step</i> (or the reference to the storage location of the necessary information 
 * for creating a single instance of the {@code target class}) is equivalent to the one instance of 
 * the {@code source enumeration class}. In other words, parameter {@code V} is specific 
 * <i>enumeration class</i>.
 * 
 * @param <T> the specific class, for the instances of which the <i>parsing result</i> is formed
 * @param <V> the specific class representing so called <i>parsing step</i> - the specific 
 * <i>enumeration class</i>, every instance of which is used to create a single instance of the 
 * {@code target class} from
 * @see naakcii.by.api.util.parser.ParsingResult
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class EnumerationParsingResult<T, V extends Enum<?>> extends ParsingResult<T , V > {
	
	/**
	 * The specific <i>enumeration class</i>, retrieving of data is made from.
	 */
	private Class<V> sourceEnumeration;
	
	/**
	 * Constructs a new instance of the {@code parsing result} specifying the types of the 
	 * {@code target class} and {@code source enumeration class}.
	 * 
	 * @param targetClass the specific class, for the instances of which the parsing result is formed
	 * @param targetEnumeration the specific <i>enumeration class</i>, retrieving of data is made 
	 * from
	 */
	public EnumerationParsingResult(Class<T> targetClass, Class<V> sourceEnumeration) {
		this.targetClass = targetClass;
		this.sourceEnumeration = sourceEnumeration;
		source = sourceEnumeration.getName();
		numberOfSavedInstances = 0;
		numberOfUnsavedInstances = 0;
		numberOfAlreadyExistingInstances = 0;
		numberOfInvalidInstances = 0;
		totalNumberOfInstances = 0;
	}
	
	/**
	 * Returns a detail description of the completed <i>parsing process</i> in a readable format.
	 * The description includes:
	 * <ul>
	 *   <<li>name of the {@code target class} of parsed instances;</li>
	 *   <li>name of the {@code source enumeration class} to parse;</li>
	 *   <li>{@code status} of the <i>parsing process</i>;</li>
	 *   <li>{@code total number of parsed instances};</li>
	 *   <li>{@code number of saved instances};</li>
	 *   <li>{@code number of unsaved instances};</li>
	 *   <li>{@code number of already existing instances};</li>
	 *   <li>{@code number of invalid instances};</li>
	 *   <li>description of all occurred {@code common warnings};</li>
	 *   <li>description of all occurred {@code common exceptions};</li>
	 *   <li>description of all occurred {@code warnings};</li>
	 *   <li>description of all occurred {@code constraint violations};</li>
	 *   <li>description of all occurred {@code exceptions};</li>
	 *   <li>{@code start time} of the <i>parsing process</i> represented in the 
	 *   {code @link #DATE_AND_TIME_FORMAT} format;</li>
	 *   <li>{@code finish time} of the <i>parsing process</i> represented in the 
	 *   {code @link #DATE_AND_TIME_FORMAT} format;</li>
	 *   <li>total time in milliseconds taking by the <i>parsing process</i>.</li>
	 * </ul>
	 * 
	 * @return the string containing description of the <i>parsing process</i>
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.ParsingResult#source
	 * @see naakcii.by.api.util.parser.ParsingResult#totalNumberOfInstances
	 * @see naakcii.by.api.util.parser.ParsingResult#numberOfSavedInstances
	 * @see naakcii.by.api.util.parser.ParsingResult#numberOfUnsavedInstances
	 * @see naakcii.by.api.util.parser.ParsingResult#numberOfAlreadyExistingInstances
	 * @see naakcii.by.api.util.parser.ParsingResult#numberOfInvalidInstances
	 * @see naakcii.by.api.util.parser.ParsingResult#commonWarnings
	 * @see naakcii.by.api.util.parser.ParsingResult#commonExceptions
	 * @see naakcii.by.api.util.parser.ParsingResult#warnings
	 * @see naakcii.by.api.util.parser.ParsingResult#constraintViolations
	 * @see naakcii.by.api.util.parser.ParsingResult#exceptions
	 * @see naakcii.by.api.util.parser.ParsingResult#startTime
	 * @see naakcii.by.api.util.parser.ParsingResult#finishTime
	 * @see naakcii.by.api.util.parser.ParsingResult#parsingTime
	 */
	public String toString() {
		StringBuilder result = new StringBuilder("Parsing result:");
		result.append(System.lineSeparator());
		result.append("target class - " + (targetClass == null ? "undefined" : targetClass.getName()) + ";");
		result.append(System.lineSeparator());
		result.append("source enumeration class - " + (source == null ? "undefined" : source) + ";");
		result.append(System.lineSeparator());
		result.append("status - " + (status == null ? "undefined" : status) + ";");
		result.append(System.lineSeparator());
		result.append("total number of instances - " + totalNumberOfInstances + ";");
		result.append(System.lineSeparator());
		result.append("number of saved instances - " + numberOfSavedInstances + ";");
		result.append(System.lineSeparator());
		result.append("number of unsaved instances - " + numberOfUnsavedInstances + ",");
		result.append(System.lineSeparator());
		result.append("\t").append("including");
		result.append(System.lineSeparator());
		result.append("\t").append("number of already existing instances - " + numberOfAlreadyExistingInstances + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("number of invalid instances - " + numberOfInvalidInstances + ";");
		result.append(System.lineSeparator());
		result.append("number of common warnings - ");
		
		if(commonWarnings.isEmpty()) {
			result.append("0;");
			result.append(System.lineSeparator());
		} else {
			result.append(commonWarnings.size() + " pieces,");
			result.append(System.lineSeparator());
			result.append("\t").append("namely");
			result.append(System.lineSeparator());
			
			for(String commonWarning : commonWarnings) {
				result.append("\t").append(printWarning(commonWarning));
				result.append(System.lineSeparator());
			}
		}
		
		result.append("number of common exceptions - ");
		
		if(commonExceptions.isEmpty()) {
			result.append("0;");
			result.append(System.lineSeparator());
		} else {
			result.append(commonExceptions.size() + " pieces,");
			result.append(System.lineSeparator());
			result.append("\t").append("namely");
			result.append(System.lineSeparator());
			
			for(Exception commonException : commonExceptions) {
				result.append("\t").append(printException(commonException));
				result.append(System.lineSeparator());
			}
		}
		
		result.append("warnings - ");
		
		if (warnings.isEmpty()) {
			result.append("0;");
			result.append(System.lineSeparator());
		} else {
			result.append(getTotalNumberOfWarnings() + " pieces in " + warnings.size() + " enumerations,");
			result.append(System.lineSeparator());
			result.append("\t").append("namely");
			result.append(System.lineSeparator());
			
			for(V enumWithWarnings : warnings.keySet()) {
				List<String> warningsInSingleEnum = warnings.get(enumWithWarnings);
				result.append("\t").append("enumeration " + enumWithWarnings + " contains " + warningsInSingleEnum.size() + " warnings,");
				result.append(System.lineSeparator());
				
				for(String warning : warningsInSingleEnum) {  
					result.append("\t").append("\t").append(printWarning(warning));
					result.append(System.lineSeparator());
				}
			}
		}
		
		result.append("constraint violations - ");
		
		if (constraintViolations.isEmpty()) {
			result.append("0;");
			result.append(System.lineSeparator());
		} else {
			result.append(getTotalNumberOfConstraintViolations() + " pieces in " + constraintViolations.size() + " enumerations,");
			result.append(System.lineSeparator());
			result.append("\t").append("namely");
			result.append(System.lineSeparator());
			
			for(V enumWithConstraintViolations : constraintViolations.keySet()) {
				List<ConstraintViolation<T>> constraintViolationsInSingleEnum = constraintViolations.get(enumWithConstraintViolations);
				result.append("\t").append("enumeration " + enumWithConstraintViolations + " contains " + constraintViolationsInSingleEnum.size() + " constraint violations,");
				result.append(System.lineSeparator());
				
				for(ConstraintViolation<T> constraintViolation : constraintViolationsInSingleEnum) {  
					result.append("\t").append("\t").append(printConstraintViolation(constraintViolation));
					result.append(System.lineSeparator());
				}
			}
		}
		
		result.append("exceptions - ");
		
		if (exceptions.isEmpty()) {
			result.append("0;");
			result.append(System.lineSeparator());
		} else {
			result.append(getTotalNumberOfExceptions() + " pieces in " + exceptions.size() + " enumerations,");
			result.append(System.lineSeparator());
			result.append("\t").append("namely");
			result.append(System.lineSeparator());
			
			for(V enumWithExceptions : exceptions.keySet()) {
				List<Exception> exceptionsInSingleEnum = exceptions.get(enumWithExceptions);
				result.append("\t").append("enumeration " + enumWithExceptions + " contains " + exceptionsInSingleEnum.size() + " exceptions,");
				result.append(System.lineSeparator());
				
				for(Exception exception : exceptionsInSingleEnum) {  
					result.append("\t").append("\t").append(printException(exception));
					result.append(System.lineSeparator());
				}
			}
		}
		
		result.append("start time - ");
		result.append(startTime == null ? "undefined" : startTime.format(DateTimeFormatter.ofPattern(DATE_AND_TIME_FORMAT)));
		result.append(";");
		result.append(System.lineSeparator());
		result.append("finish time - ");
		result.append(finishTime == null ? "undefined" : finishTime.format(DateTimeFormatter.ofPattern(DATE_AND_TIME_FORMAT)));
		result.append(";");
		result.append(System.lineSeparator());
		result.append("total parsing time - ");
		result.append(parsingTime == null ? " impossible to calculate." : parsingTime + " milliseconds.");	
		return result.toString();
	}
}
