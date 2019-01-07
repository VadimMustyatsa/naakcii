package naakcii.by.api.util.parser;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.ConstraintViolation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(exclude = {"warnings", "constraintViolations", "exceptions"})
public class ParsingResult<T> {
	
	private static final String DATE_AND_TIME_FORMAT = "yyyy'-'MM'-'dd HH':'mm':'ss'.'SSS";
	
	private Class<T> targetClass;
	private String fileName;
	private String sheetName;
	private Integer sheetIndex;
	private Integer numberOfSavedInstances;
	private Integer numberOfUnsavedInstances;
	private Integer numberOfAlreadyExistingInstances;
	private Integer numberOfInvalidInstances;
	private Integer totalNumberOfInstances;
	private LocalDateTime startTime;
	private LocalDateTime finishTime;
	private Long parsingTime; // In milliseconds.
	/*
	 * Parameters for 'warnings', 'constraintViolations' and 'exceptions' maps:
	 * Key (Integer) - 
	 * number of row in '.xls' or '.xlsx' file, that has a warning, constraint violation or exception;
	 * Value (List of Strings with warnings, ConstraintViolations<T> or Exceptions) - 
	 * instances of warning String.class, ConstraintViolation<T>.class or Exception.class.
	 */
	private Map<Integer, List<String>> warnings = new HashMap<>();
	private Map<Integer, List<ConstraintViolation<T>>> constraintViolations = new HashMap<>();
	private Map<Integer, List<Exception>> exceptions = new HashMap<>();
	
	public ParsingResult(Class<T> targetClass, String fileName) {
		this.targetClass = targetClass;
		this.fileName = fileName;
		numberOfSavedInstances = 0;
		numberOfUnsavedInstances = 0;
		numberOfAlreadyExistingInstances = 0;
		numberOfInvalidInstances = 0;
		totalNumberOfInstances = 0;
	}
	
	public void increaseNumberOfSavedInstances() {
		numberOfSavedInstances++;
	}
	
	public void increaseNumberOfUnsavedInstances() {
		numberOfUnsavedInstances++;
	}
	
	public void increaseNumberOfAlreadyExistingInstances() {
		numberOfAlreadyExistingInstances++;
	}
	
	public void increaseNumberOfInvalidInstances() {
		numberOfInvalidInstances++;
	}
	
	public void increaseTotalNumberOfInstances() {
		totalNumberOfInstances++;
	}
	
	public void addWarning(Integer rowNumber, String warning) {
		if (warnings.containsKey(rowNumber)) {
			warnings.get(rowNumber).add(warning);
		} else {
			List<String> warningsInSingleRow = new ArrayList<>();
			warningsInSingleRow.add(warning);
			warnings.put(rowNumber, warningsInSingleRow);
		}
	}
	
	public void addConstraintViolation(Integer rowNumber, ConstraintViolation<T> constraintViolation) {
		if (constraintViolations.containsKey(rowNumber)) {
			constraintViolations.get(rowNumber).add(constraintViolation);
		} else {
			List<ConstraintViolation<T>> constraintViolationsInSingleRow = new ArrayList<>();
			constraintViolationsInSingleRow.add(constraintViolation);
			constraintViolations.put(rowNumber, constraintViolationsInSingleRow);
		}
	}
	
	public void addException(Integer rowNumber, Exception exception) {
		if (exceptions.containsKey(rowNumber)) {
			exceptions.get(rowNumber).add(exception);
		} else {
			List<Exception> exceptionsInSingleRow = new ArrayList<>();
			exceptionsInSingleRow.add(exception);
			exceptions.put(rowNumber, exceptionsInSingleRow);
		}
	}
	
	private int getTotalNumberOfWarnings() {
		return warnings
				.values()
				.stream()
				.filter(Objects::nonNull)
				.map((List<String> warningsInSingleRow) -> warningsInSingleRow.size())
				.reduce(0, (a, b) -> a + b);
	}
	
	private int getTotalNumberOfConstraintViolations() {
		return constraintViolations
				.values()
				.stream()
				.filter(Objects::nonNull)
				.map((List<ConstraintViolation<T>> constraintViolationsInSingleRow) -> constraintViolationsInSingleRow.size())
				.reduce(0, (a, b) -> a + b);
	}
	
	private int getTotalNumberOfExceptions() {
		return exceptions
				.values()
				.stream()
				.filter(Objects::nonNull)
				.map((List<Exception> exceptionsInSingleRow) -> exceptionsInSingleRow.size())
				.reduce(0, (a, b) -> a + b);
	}
	
	public void setStartTime() {
		startTime = LocalDateTime.now();
	}
	
	public void setFinishTime() {
		finishTime = LocalDateTime.now();
		setParsingTime();
	}
	
	private void setParsingTime() {
		if (startTime != null && finishTime != null && startTime.isBefore(finishTime)) {
			parsingTime = Duration.between(startTime, finishTime).toMillis();
		}
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("Parsing result:");
		result.append(System.lineSeparator());
		result.append("target class - " + targetClass.getName() + ";");
		result.append(System.lineSeparator());
		result.append("file - " + fileName + ";");
		result.append(System.lineSeparator());
		result.append("sheet - " + sheetName + ";");
		result.append(System.lineSeparator());
		result.append("sheet index - " + sheetIndex + ";");
		result.append(System.lineSeparator());
		result.append("total number of instances - " + totalNumberOfInstances + ";");
		result.append(System.lineSeparator());
		result.append("number of saved instances - " + numberOfSavedInstances + ";");
		result.append(System.lineSeparator());
		result.append("number of unsaved instances - " + numberOfUnsavedInstances + ",");
		result.append(System.lineSeparator());
		result.append("including");
		result.append(System.lineSeparator());
		result.append("number of already existing instances - " + numberOfAlreadyExistingInstances + ";");
		result.append(System.lineSeparator());
		result.append("number of invalid instances - " + numberOfInvalidInstances + ";");
		result.append(System.lineSeparator());
		result.append("warnings: ");
		
		if (warnings.isEmpty()) {
			result.append("not found;");
			result.append(System.lineSeparator());
		} else {
			result.append(warnings.size() + " rows with " + getTotalNumberOfWarnings() + " pieces,");
			result.append(System.lineSeparator());
			result.append("namely");
			result.append(System.lineSeparator());
			
			for(Integer numberOfRowWithWarning : warnings.keySet()) {
				List<String> warningsInSingleRow = warnings.get(numberOfRowWithWarning);
				result.append("row number " + numberOfRowWithWarning + " has " + warningsInSingleRow.size() + " warnings,");
				result.append(System.lineSeparator());
				
				for(String warning : warningsInSingleRow) {  
					result.append(warning + ";");
					result.append(System.lineSeparator());
				}
			}
		}
		
		result.append("constraint violations: ");
		
		if (constraintViolations.isEmpty()) {
			result.append("not found;");
			result.append(System.lineSeparator());
		} else {
			result.append(constraintViolations.size() + " rows with " + getTotalNumberOfConstraintViolations() + " pieces,");
			result.append(System.lineSeparator());
			result.append("namely");
			result.append(System.lineSeparator());
			
			for(Integer numberOfRowWithConstraintViolations : constraintViolations.keySet()) {
				List<ConstraintViolation<T>> constraintViolationsInSingleRow = constraintViolations.get(numberOfRowWithConstraintViolations);
				result.append("row number " + numberOfRowWithConstraintViolations + " has " + constraintViolationsInSingleRow.size() + " constraint violations,");
				result.append(System.lineSeparator());
				
				for(ConstraintViolation<T> constraintViolation : constraintViolationsInSingleRow) {  
					result.append(constraintViolation + ";");
					result.append(System.lineSeparator());
				}
			}
		}
		
		result.append("exceptions: ");
		
		if (exceptions.isEmpty()) {
			result.append("not found;");
			result.append(System.lineSeparator());
		} else {
			result.append(exceptions.size() + " rows with " + getTotalNumberOfExceptions() + " pieces,");
			result.append(System.lineSeparator());
			result.append("namely");
			result.append(System.lineSeparator());
			
			for(Integer numberOfRowWithExceptions : exceptions.keySet()) {
				List<Exception> exceptionsInSingleRow = exceptions.get(numberOfRowWithExceptions);
				result.append("row number " + numberOfRowWithExceptions + " has " + exceptionsInSingleRow.size() + " exceptions,");
				result.append(System.lineSeparator());
				
				for(Exception exception : exceptionsInSingleRow) {  
					result.append(exception + ";");
					result.append(System.lineSeparator());
				}
			}
		}
		
		result.append("start time - ");
		result.append(startTime == null ? "undefined" : startTime.format(DateTimeFormatter.ofPattern(DATE_AND_TIME_FORMAT)));
		result.append(System.lineSeparator());
		result.append("finish time - ");
		result.append(";");
		result.append(finishTime == null ? "undefined" : finishTime.format(DateTimeFormatter.ofPattern(DATE_AND_TIME_FORMAT)));
		result.append(";");
		result.append(System.lineSeparator());
		result.append("total parsing time - ");
		result.append(parsingTime == null ? "impossible to calculate" : parsingTime + "milliseconds");	
		result.append(".");
		return result.toString();
	}
}
