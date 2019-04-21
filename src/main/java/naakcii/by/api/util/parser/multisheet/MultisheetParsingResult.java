package naakcii.by.api.util.parser.multisheet;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.ConstraintViolation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import naakcii.by.api.util.parser.ParsingResult;

/**
 * Provides the model of the {@link naakcii.by.api.util.parser.ParsingResult} abstract class for 
 * use, when information is retrieved from the <i>.xls</i> or <i>.xlsx</i> files.  
 * Accumulates detailed information about the <i>parsing process</i> of instances, that all belong 
 * to the specific class {@code T}, provides utility method for writing the {@code parsing result}  
 * in a readable format.
 * <p>
 * The <i>parsing step</i> (or the reference to the storage location of the necessary information 
 * for creating a single instance of the {@code target class}) is equivalent to the number of the 
 * row in the <i>multisheet file</i>. In other words, parameter {@code V} is {@code Integer}.
 * 
 * @param <T> the specific class, for the instances of which the <i>parsing result</i> is formed
 * @see naakcii.by.api.util.parser.ParsingResult
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class MultisheetParsingResult<T> extends ParsingResult<T, Integer> {
	
	/**
	 * The name of the sheet in the <i>.xls</i> or <i>.xlsx</i> file, retrieving of data is made from.
	 */
	private String sheetName;
	
	/**
	 * The index of the sheet in the <i>.xls</i> or <i>.xlsx</i> file, retrieving of data is made from.
	 */
	private Integer sheetIndex; 
	
	/**
	 * Constructs a new instance of the {@code parsing result} specifying the type of the 
	 * {@code target class} and the name of the <i>multisheet file</i>.
	 * 
	 * @param targetClass the specific class, for the instances of which the parsing result is formed
	 * @param fileName the string containing the name of the <i>.xls</i> or <i>.xlsx</i> file, 
	 * retrieving of data is made from
	 */
	public MultisheetParsingResult(Class<T> targetClass, String fileName) {
		this.targetClass = targetClass;
		source = fileName;
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
	 *   <li>name of the {@code target class} of parsed instances;</li>
	 *   <li>name of the <i>.xls</i> or <i>.xlsx</i> file containing data to parse;</li>
	 *   <li>{@code name and index of the sheet} involved in the <i>parsing process</i>;</li>
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
	 * @see #sheetName
	 * @see #sheetIndex
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
		result.append("file - " + (source == null ? "undefined" : source) + ";");
		result.append(System.lineSeparator());
		result.append("sheet name - " + (sheetName == null ? "undefined" : sheetName) + ";");
		result.append(System.lineSeparator());
		result.append("sheet index - " + (sheetIndex == null ? "undefined" : sheetIndex) + ";");
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
			result.append(getTotalNumberOfWarnings() + " pieces in " + warnings.size() + " rows,");
			result.append(System.lineSeparator());
			result.append("\t").append("namely");
			result.append(System.lineSeparator());
			
			for(Integer numberOfRowWithWarnings : warnings.keySet()) {
				List<String> warningsInSingleRow = warnings.get(numberOfRowWithWarnings);
				result.append("\t").append("row number " + numberOfRowWithWarnings + " contains " + warningsInSingleRow.size() + " warnings,");
				result.append(System.lineSeparator());
				
				for(String warning : warningsInSingleRow) {  
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
			result.append(getTotalNumberOfConstraintViolations() + " pieces in " + constraintViolations.size() + " rows,");
			result.append(System.lineSeparator());
			result.append("\t").append("namely");
			result.append(System.lineSeparator());
			
			for(Integer numberOfRowWithConstraintViolations : constraintViolations.keySet()) {
				List<ConstraintViolation<T>> constraintViolationsInSingleRow = constraintViolations.get(numberOfRowWithConstraintViolations);
				result.append("\t").append("row number " + numberOfRowWithConstraintViolations + " contains " + constraintViolationsInSingleRow.size() + " constraint violations,");
				result.append(System.lineSeparator());
				
				for(ConstraintViolation<T> constraintViolation : constraintViolationsInSingleRow) {  
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
			result.append(getTotalNumberOfExceptions() + " pieces in " + exceptions.size() + " rows,");
			result.append(System.lineSeparator());
			result.append("\t").append("namely");
			result.append(System.lineSeparator());
			
			for(Integer numberOfRowWithExceptions : exceptions.keySet()) {
				List<Exception> exceptionsInSingleRow = exceptions.get(numberOfRowWithExceptions);
				result.append("\t").append("row number " + numberOfRowWithExceptions + " contains " + exceptionsInSingleRow.size() + " exceptions,");
				result.append(System.lineSeparator());
				
				for(Exception exception : exceptionsInSingleRow) {  
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
