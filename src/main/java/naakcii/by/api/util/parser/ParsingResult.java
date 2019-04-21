package naakcii.by.api.util.parser;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.ConstraintViolation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Accumulates detailed information about the <i>parsing process</i> of instances, that all belong 
 * to the specific class {@code T}, provides utility method for writing the {@code parsing result} 
 * in a readable format.
 * 
 * @param <T> the specific class for the instances of which the {@code parsing result} is formed
 * @param <V> the specific class representing so called <i>parsing step</i> - the reference to the 
 * storage location of necessary information for creating a single instance of the 
 * {@code target class}
 */
@Setter
@Getter
@EqualsAndHashCode(exclude = {"warnings", 
							  "constraintViolations", 
							  "exceptions", 
							  "startTime", 
							  "finishTime", 
							  "parsingTime", 
							  "commonExceptions", 
							  "commonWarnings"})
public abstract class ParsingResult<T, V> {
	
	/**
	 * The format of date and time is {@value #DATE_AND_TIME_FORMAT}.
	 * Is used for presenting values of the {@code start time} and {@code finish time} in the 
	 * readable form.
	 * 
	 * @see #startTime
	 * @see #finishTime
	 */
	protected static final String DATE_AND_TIME_FORMAT = "yyyy'-'MM'-'dd HH':'mm':'ss'.'SSS";
	
	/**
	 * Represents the {@code status} of the completed <i>parsing process</i>.
	 * Possible values:
	 * <ul>
	 *   <ol>
	 *   {@code OK} takes place when:
	 *     <ul>
	 *       <li>
	 *         all entities were successfully saved to the database;
	 *       <li>
	 *       <li>
	 *         there were not any warnings, exceptions and constraint violations during the 
	 *         <i>parsing process</i>.
	 *       <li>  
	 *     </ul>
	 *   </ol>
	 *   <ol>
	 *   {@code WITH_WARNINGS} takes place when:
	 *     <ul>
	 *       <li>
	 *         at least one warning occurred during the <i>parsing process</i>;
	 *       </li>
	 *       <li>
	 *         there were not any exceptions and constraint violations during the <i>parsing 
	 *         process</i>;
	 *       </li>
	 *       <li>
	 *         probably, some (or all) entities were not saved to the database due to the failure 
	 *         of the <i>uniqueness check</i>1.
	 *       </li>
	 *     </ul>  
	 *   </ol>
	 *   <ol>
	 *   {@code WITH_CONSTRAINT_VIOLATIONS} takes place when:
	 *     <ul>
	 *       <li>
	 *         at least one constraint violation occurred during the <i>parsing process</i>;
	 *       </li>
	 *       <li>
	 *         there were not any exceptions during the <i>parsing process</i>;
	 *       </li>
	 *       <li>
	 *         probably, some (or all) entities were not saved to the database due to the failure 
	 *         of the <i>validation</i>.
	 *       </li>
	 *     </ul>
	 *   </ol>
	 *   <ol>
	 *   {@code WITH_EXCEPTIONS} takes place when:
	 *     <ul>
	 *       <li>
	 *         exception(s) occurred during the <i>parsing process</i>;
	 *       </li>
	 *       <li>
	 *         probably, some (or all) entities were not saved to the database due to the 
	 *         exception(s) occurrence.
	 *       </li>
	 *     </ul>
	 *   </ol>
	 * </ul> 
	 */
	enum Status {
		OK, WITH_WARNINGS, WITH_CONSTRAINT_VIOLATIONS, WITH_EXCEPTIONS;
	}
	
	/**
	 * The status of the completed <i>parsing process</i>.
	 * 
	 * @see #Status
	 */
	protected Status status;
	
	/**
	 * The specific class for the instances of which the {@code parsing result} is formed.
	 */
	protected Class<T> targetClass;
	
	/**
	 * The name of the source retrieving of data is made from.
	 * For example:
	 * <ul>
	 *   <li>name of the <i>enumeration class</i>;</li>
	 *   <li>name of the <i>.xls</i> or <i>.xlsx</i> file.</li>
	 * </ul>
	 */
	protected String source;
	
	/**
	 * The number of the {@code target class} instances, that were saved during the <i>process of 
	 * parsing</i>.
	 */
	protected Integer numberOfSavedInstances;
	
	/**
	 * The number of the {@code target class} instances, that were not saved during the <i>process 
	 * of parsing</i> due to the failure of the <i>validation</i> or </i>uniqueness check</i>, 
	 * occurrence of saving exception(s).
	 */
	protected Integer numberOfUnsavedInstances;
	
	/**
	 * The number of the {@code target class} instances, that were not saved during the <i>process 
	 * of parsing</i> due to the failure of the <i>uniqueness check</i>.
	 */
	protected Integer numberOfAlreadyExistingInstances;
	
	/**
	 * The number of the {@code target class} instances, that were not saved during the <i>process 
	 * of parsing</i> due to the failure of the <i>validation</i>.
	 */
	protected Integer numberOfInvalidInstances;
	
	/**
	 * Total number of the {@code target class} instances, that were retrieved from the 
	 * {@code source} during the <i>parsing process</i>.
	 */
	protected Integer totalNumberOfInstances;
	
	/**
	 * The time, when <i>parsing process</i> was started.
	 */
	protected LocalDateTime startTime;
	
	/**
	 * The time, when <i>parsing process</i> was finished.
	 */
	protected LocalDateTime finishTime;
	
	/**
	 * Total time in milliseconds taking by the <i>parsing process</i>.
	 */
	protected Long parsingTime;
	
	/**
	 * The list of <i>common exceptions<i>, that occurred during the <i>parsing process</i>.
	 * Examples of <i>common exceptions<i>:
	 * <ul>
	 *   <li>input-output exception during opening the source file.</li>
	 * </ul>
	 */
	protected List<Exception> commonExceptions = new ArrayList<>();
	
	/**
	 * The list of <i>common warnings<i>, that occurred during the <i>parsing process</i>.
	 * Examples of <i>common warnings<i>:
	 * <ul>
	 *   <li>sheet (in <i>.xls</i> or <i>.xlss</i> file) with data was not found.</li>
	 * </ul>
	 */
	protected List<String> commonWarnings = new ArrayList<>();
		
	/**
	 * The map of <i>warnings</i>, that occurred during the <i>parsing process</i>.
	 * Key {@code V} specifies the <i>parsing step</i> or the reference to the storage location 
	 * of the minimum piece of information required to create a single instance of the 
	 * {@code target class} from, for example:
	 * <ul>
	 *   <li>one instance of the <i>enumeration class</i>;</li>
	 *   <li>number of the row in the <i>.xls</i> or <i>.xlsx</i> file.</li>
	 * </ul>
	 * <p>
	 * Value {@code List<String>} specifies a list of <i>warnings</i> occurred during the creating 
	 * of a single {@code target class} instance.
	 */ 
	protected Map<V, List<String>> warnings = new HashMap<>();
	
	/**
	 * The map of <i>constraint violations</i>, that occurred during the <i>parsing process</i>. 
	 * Key {@code V} specifies the <i>parsing step</i> or the reference to the storage location 
	 * of the minimum piece of information required to create a single instance of the  
	 * {@code target class} from, for example:
	 * <ul>
	 *   <li>one instance of the <i>enumeration class</i>;</li>
	 *   <li>number of the row in the <i>.xls</i> or <i>.xlsx</i> file.</li>
	 * </ul>
	 * <p>
	 * Value {@code List<String>} specifies a list of <i>warnings</i> occurred during the creating 
	 * of a single {@code target class} instance.
	 */
	protected Map<V, List<ConstraintViolation<T>>> constraintViolations = new HashMap<>();
	
	/**
	 * The map of <i>exceptions</i>, that occurred during the <i>parsing process</i>.
	 * Key {@code V} specifies the <i>parsing step</i> or the reference to the storage location 
	 * of the minimum piece of information required to create a single instance of the 
	 * {@code target class} from, for example:
	 * <ul>
	 *   <li>one instance of the <i>enumeration class</i>;</li>
	 *   <li>number of the row in the <i>.xls</i> or <i>.xlsx</i> file.</li>
	 * </ul>
	 * <p>
	 * Value {@code List<String>} specifies a list of <i>warnings</i> occurred during the creating 
	 * of a single {@code target class} instance.
	 */
	protected Map<V, List<Exception>> exceptions = new HashMap<>();
	
	/**
	 * Increases the {@code number of saved target class instances} by one.
	 * 
	 * @see #numberOfSavedInstances
	 */
	public void increaseNumberOfSavedInstances() {
		numberOfSavedInstances++;
	}
	
	/**
	 * Increases the {@code number of unsaved target class instances} by one.
	 * 
	 * @see #numberOfUnsavedInstances
	 */
	public void increaseNumberOfUnsavedInstances() {
		numberOfUnsavedInstances++;
	}
	
	/**
	 * Increases the {@code number of already existing target class instances} by one.
	 * 
	 * @see #numberOfAlreadyExistingInstances
	 */
	public void increaseNumberOfAlreadyExistingInstances() {
		numberOfAlreadyExistingInstances++;
	}
	
	/**
	 * Increases the {@code number of invalid target class instances} by one.
	 * 
	 * @see #numberOfInvalidInstances
	 */
	public void increaseNumberOfInvalidInstances() {
		numberOfInvalidInstances++;
	}
	
	/**
	 * Increases the {@code total number of target class instances} by one.
	 * 
	 * @see #totalNumberOfInstances
	 */
	public void increaseTotalNumberOfInstances() {
		totalNumberOfInstances++;
	}
	
	/**
	 * Adds one <i>warning</i> occurred during the creating of a single {@code target class} 
	 * instance to the {@code warnings}.  
	 *  
	 * @param parsingStep the class representing the <i>parsing step</i> or the reference to the 
	 * storage location of the necessary information for creating a single instance of the {@code 
	 * target class}
	 * @param warning the string containing the warning, that occurred occurred during the creating 
	 * of a single {@code target class} instance.
	 * @see #warnings
	 */
	public void addWarning(V parsingStep, String warning) {
		if (warnings.containsKey(parsingStep)) {
			warnings.get(parsingStep).add(warning);
		} else {
			List<String> warningsInSingleRow = new ArrayList<>();
			warningsInSingleRow.add(warning);
			warnings.put(parsingStep, warningsInSingleRow);
		}
	}
	
	/**
	 * Adds one <i>constraint violation</i> occurred during the creating of a single {@code target 
	 * class} instance to the {@code constraint violations}.
	 * 
	 * @param parsingStep the class representing the <i>parsing step</i> or the reference to the 
	 * storage location of the necessary information for creating a single instance of the {@code  
	 * target class}
	 * @param constraintViolation the constraint violation, that occurred occurred during the creating 
	 * of a single {@code target class} instance
	 * @see #constraintViolations
	 */
	public void addConstraintViolation(V parsingStep, ConstraintViolation<T> constraintViolation) {
		if (constraintViolations.containsKey(parsingStep)) {
			constraintViolations.get(parsingStep).add(constraintViolation);
		} else {
			List<ConstraintViolation<T>> constraintViolationsInSingleRow = new ArrayList<>();
			constraintViolationsInSingleRow.add(constraintViolation);
			constraintViolations.put(parsingStep, constraintViolationsInSingleRow);
		}
	}
	
	/**
	 * Adds one <i>exception</i> occurred during the creating of a single {@code target class} 
	 * instance to the {@code exceptions}.
	 * 
	 * @param parsingStep the class representing the <i>parsing step</i> or the reference to the 
	 * storage location of the necessary information for creating a single instance of the {@code  
	 * target class}
	 * @param exception the exception, that occurred occurred during the creating of a single 
	 * {@code target class} instance
	 * @see #exceptions
	 */
	public void addException(V parsingStep, Exception exception) {
		if (exceptions.containsKey(parsingStep)) {
			exceptions.get(parsingStep).add(exception);
		} else {
			List<Exception> exceptionsInSingleRow = new ArrayList<>();
			exceptionsInSingleRow.add(exception);
			exceptions.put(parsingStep, exceptionsInSingleRow);
		}
	}
	
	/**
	 * Adds one <i>common exception</i> occurred during the <i>parsing process</i>, to the 
	 * {@code common exceptions}.
	 * 
	 * @param commonException the common exception, that occurred during the <i>parsing process</i>
	 * @see #commonExceptions
	 */
	public void addCommonException(Exception commonException) {
		commonExceptions.add(commonException);
	}
	
	/**
	 * Adds one <i>common warning</i>, occurred during the <i>parsing process</i>, to the 
	 * {@code common warnings}.
	 * 
	 * @param commonWarnings the string containing the common warning, that occurred during the 
	 * <i>parsing process</i>
	 * @see #commonWarnings
	 */
	public void addCommonWarning(String commonWarning) {
		commonWarnings.add(commonWarning);
	}
	
	/**
	 * Returns the total number of {@code warnings} occurred during the <i>parsing process</i>.
	 * {@code Common warnings} are not taken into account.
	 * 
	 * @return the total number of {@code warnings}
	 * @see #warnings
	 * @see #commonWarnings
	 */
	protected long getTotalNumberOfWarnings() {
		return warnings
				.values()
				.stream()
				.filter(Objects::nonNull)
				.map(Collection::size)
				.reduce(0, (a, b) -> a + b);			
	}
	
	/**
	 * Returns the total number of {@code constraint violations} occurred during the <i>parsing 
	 * process</i>.
	 * 
	 * @return the total number of the {@code constraint violations}
	 * @see #constraintViolations
	 */
	protected int getTotalNumberOfConstraintViolations() {
		return constraintViolations
				.values()
				.stream()
				.filter(Objects::nonNull)
				.map(Collection::size)
				.reduce(0, (a, b) -> a + b);
	}
	
	/**
	 * Returns the total number of {@code exceptions} occurred during the <i>parsing process</i>.
	 * {@code Common exceptions} are not taken into account.
	 * 
	 * @return the total number of the {@code exceptions}
	 * @see #exceptions
	 * @see #commonExceptions
	 */
	protected int getTotalNumberOfExceptions() {
		return exceptions
				.values()
				.stream()
				.filter(Objects::nonNull)
				.map(Collection::size)
				.reduce(0, (a, b) -> a + b);
	}
	
	/**
	 * Sets value of the <i>parsing process</i> {@code start time} as current time.
	 * 
	 * @see #startTime
	 */
	public void setStartTime() {
		startTime = LocalDateTime.now();
	}
	
	/**
	 * Sets value of the <i>parsing process</i> {@code finish time} as current time.
	 * Also sets values of the <i>parsing process</i> {@code parsing time} and {@code status} by 
	 * calling appropriate methods.
	 * 
	 * @see #finishTime
	 * @see #setParsingTime()
	 * @see #setStatus()
	 */
	public void setFinishTime() {
		finishTime = LocalDateTime.now();
		setParsingTime();
		setStatus();
	}
	
	/**
	 * Calculates the total time in milliseconds taking by the <i>parsing process</i> as difference 
	 * between values of the {@code start time} and {@code finish time}.
	 * 
	 * @see #startTime
	 * @see #finishTime 
	 */
	private void setParsingTime() {
		if (startTime != null && finishTime != null && startTime.isBefore(finishTime)) {
			parsingTime = Duration.between(startTime, finishTime).toMillis();
		}
	}
	
	/**
	 * Defines the {@code status} of the completed <i>parsing process</i> depending on the 
	 * existence of:
	 * <ul>
	 *   <li>{@code warnings} or {@code common warnings};</li>
	 *   <li>{@code constraint violations};</li>
	 *   <li>{@code exceptions} or {@code common exceptions}.</li>
	 * </ul>
	 * 
	 * @see #status
	 * @see #warnings
	 * @see #commonWarnings
	 * @see #constraintViolations
	 * @see #exceptions
	 * @see #commonExceptions
	 */
	private void setStatus() {
		if (!commonExceptions.isEmpty() || !exceptions.isEmpty()) {
			status = Status.WITH_EXCEPTIONS;
		}else if (!constraintViolations.isEmpty()) {
			status = Status.WITH_CONSTRAINT_VIOLATIONS;
		} else if (!commonWarnings.isEmpty() || !warnings.isEmpty()) {
			status = Status.WITH_WARNINGS;
		} else {
			status = Status.OK;
		}
	}
	
	/**
	 * Returns a description of the <i>constraint violation</i> in a readable format.
	 * 
	 * @param the constraint violation to make a readable description for
	 * @return the string containing description of the constraint violation
	 */
	protected String printConstraintViolation(ConstraintViolation<?> violation) {
		StringBuilder result = new StringBuilder();
		result.append("message: '" + violation.getMessage() + "'; ");
		result.append("class: " + violation.getRootBeanClass().getName() + "; ");
		result.append("field: " + violation.getPropertyPath() + "; ");
		result.append("value: " + violation.getInvalidValue() + ";");
		return result.toString();
	}
	
	/**
	 * Returns a description of the <i>exception</i> in a readable format.
	 * 
	 * @param the exception to make a readable description for
	 * @return the string containing description of the exception
	 */
	protected String printException(Exception exception) {
		StringBuilder result = new StringBuilder();
		result.append("message: '" + exception.getMessage() + "'; ");
		result.append("class: " + exception.getClass().getName() + ";");
		return result.toString();
	}
	
	/**
	 * Returns a description of the <i>warning</i> in a readable format.
	 * 
	 * @param the string containing the warning to make a readable description for
	 * @return the string containing description of the warning
	 */
	protected String printWarning(String warning) {
		StringBuilder result = new StringBuilder();
		result.append("message: '" + warning + "';");
		return result.toString();
	}
	
	/**
	 * Returns a detail description of the completed <i>parsing process</i> in a readable format.
	 * The description includes:
	 * <ul>
	 *   <li>name of the {@code target class} of parsed instances;</li>
	 *   <li>name of the {@code source} containing data to parse;</li>
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
	 * @see #targetClass
	 * @see #source
	 * @see #totalNumberOfInstances
	 * @see #numberOfSavedInstances
	 * @see #numberOfUnsavedInstances
	 * @see #numberOfAlreadyExistingInstances
	 * @see #numberOfInvalidInstances
	 * @see #commonWarnings
	 * @see #commonExceptions
	 * @see #warnings
	 * @see #constraintViolations
	 * @see #exceptions
	 * @see #startTime
	 * @see #finishTime
	 * @see #parsingTime
	 */
	public String toString() {
		StringBuilder result = new StringBuilder("Parsing result:");
		result.append(System.lineSeparator());
		result.append("target class - " + (targetClass == null ? "undefined" : targetClass.getName()) + ";");
		result.append(System.lineSeparator());
		result.append("source - " + (source == null ? "undefined" : source) + ";");
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
			result.append(getTotalNumberOfWarnings() + " pieces on " + warnings.size() + " parsing steps,");
			result.append(System.lineSeparator());
			result.append("\t").append("namely");
			result.append(System.lineSeparator());
			
			for(V stepWithWarnings : warnings.keySet()) {
				List<String> warningsOnSingleStep = warnings.get(stepWithWarnings);
				result.append("\t").append("step " + stepWithWarnings + " contains " + warningsOnSingleStep.size() + " warnings,");
				result.append(System.lineSeparator());
				
				for(String warning : warningsOnSingleStep) {  
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
			result.append(getTotalNumberOfConstraintViolations() + " pieces on " + constraintViolations.size() + " parsing steps,");
			result.append(System.lineSeparator());
			result.append("\t").append("namely");
			result.append(System.lineSeparator());
			
			for(V stepWithConstraintViolations : constraintViolations.keySet()) {
				List<ConstraintViolation<T>> constraintViolationsOnSingleStep = constraintViolations.get(stepWithConstraintViolations);
				result.append("\t").append("step " + stepWithConstraintViolations + " contains " + constraintViolationsOnSingleStep.size() + " constraint violations,");
				result.append(System.lineSeparator());
				
				for(ConstraintViolation<T> constraintViolation : constraintViolationsOnSingleStep) {  
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
			result.append(getTotalNumberOfExceptions() + " pieces on " + exceptions.size() + " rows,");
			result.append(System.lineSeparator());
			result.append("\t").append("namely");
			result.append(System.lineSeparator());
			
			for(V stepWithExceptions : exceptions.keySet()) {
				List<Exception> exceptionsOnSingleStep = exceptions.get(stepWithExceptions);
				result.append("\t").append("step " + stepWithExceptions + " contains " + exceptionsOnSingleStep.size() + " exceptions,");
				result.append(System.lineSeparator());
				
				for(Exception exception : exceptionsOnSingleStep) {  
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
		result.append(parsingTime == null ? "impossible to calculate." : parsingTime + " milliseconds.");	
		return result.toString();
	}
}
