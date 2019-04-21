package naakcii.by.api.util.parser.multisheet.mapper;

/**
 * Provides the base interface for mapping columns of the table on fields (properties) 
 * of the <i>specific class</i> during the <i>parsing process</i>.
 * Maps each columns of the table containing information for creating instances of the <i>specific 
 * entity</i> on fields (properties) of this entity during the <i>parsing process</i>. 
 * <p>
 * Provides utility method for writing the <i>mapping result</i> in a readable format.
 * <p>
 * The source of the data to parse is <i>.xls</i> or <i>.xlsx</i> file.
 */
public interface ColumnMapper {
	
	/**
	 * The message pattern that describes successful finishing of the column mapping.
	 * Contains three parameters:
	 * <ul>
	 *   <li>the string representing the header of the mapped column;</li>
	 *   <li>the index of the mapped column;</li>
	 *   <li>the string containing the name of the <i>specific class</i> field (property) the 
	 *   column is mapped on.</li>
	 * </ul>
	 */
	static String SUCCESS_MAPPING_MESSAGE = "Column '{}' with index '{}' has been mapped on field '{}'.";
	
	/**
	 * The message pattern that describes unsuccessful finishing of the column mapping.
	 * Contains three parameters:
	 * <ul>
	 *   <li>the string representing the header of the mapped column;</li>
	 *   <li>the index of the mapped column;</li>
	 *   <li>the string containing the name of the <i>specific class</i> involved in the 
	 *   <i>process of mapping</i>.</li>
	 * </ul>
	 */
	static String UNSUCCESS_MAPPING_MESSAGE = "Column '{}' with index '{}' hasn't been mapped on any field of entity '{}'.";
	
	/**
	 * The message pattern that describes finishing of the column mapping with exception occurrence.
	 * Contains four parameters:
	 * <ul>
	 *   <li>the string representing the header of the mapped column;</li>
	 *   <li>the index of the mapped column;</li>
	 *   <li>the string containing the name of the <i>specific class</i> involved in the 
	 *   <i>process of mapping</i>;</li>
	 *   <li>the string containing the description of the occurred exception.</li>
	 * </ul>
	 */
	static String EXCEPTION_MAPPING_MESSAGE = "Column '{}' with index '{}' hasn't been mapped on any field of entity '{}' due to exception: {}.";
	
	/**
	 * The message pattern that represents the header of the <i>mapping result</i>.
	 * Contains single parameter: the string containing the name of the <i>specific class</i> 
	 * involved in the <i>process of mapping</i>.
	 */
	static String RESULT_HEADER_MESSAGE = "Columns mapping on fields of entity '%s':";
	
	/**
	 * The message pattern that represents a single collation between a column and a field 
	 * (property) of the specific entity in the <i>mapping result</i>.
	 * Contains four parameters:
	 * <ul>
	 *   <li>the first - the string containing the name of the <i>specific class</i> field 
	 *   (property) the column is mapped on;</li>
	 *   <li>the second - the index of the mapped column;</li>
	 *   <li>the third - the string representing the header of the mapped column;</li>
	 *   <li>the fourth - the character describing the line ending symbol (<i>dot</i> or <i>semicolon</i>).</li>
	 * </ul>
	 */
	static String RESULT_MAPPING_MESSAGE = "field '%s' - column '%d / %s'%c";
	
	/**
	 * The message pattern that represents exception occurrence during the process of getting 
	 * <i>mapping result</i>.
	 * Contains four parameters:
	 * <ul>
	 *   <li>the first - the string containing the name of the <i>specific class</i> involved in 
	 *   the <i>process of mapping</i>;</li>
	 *   <li>the second - the string containing the description of the occurred exception.</li>
	 * </ul>
	 */
	static String RESULT_EXCEPTION_MESSAGE = "Exception has occurred during the process of getting mapping result for entity '{}': {}.";
	
	/**
	 * Maps single column of the table on a field (property) of the <i>specific entity</i>.
	 * The data containing in the column will be used for filling mapped field (property) of the 
	 * <i>specific entity</i> instances.
	 * 
	 * @param cellValue the string containing header of the mapped column
	 * @param columnIndex the index number of the mapped column
	 */
	void mapColumn(String cellValue, int columnIndex);
	
	/**
	 * Returns a detail report of the completed <i>mapping process</i> in a readable format.
	 * 
	 * @return the string containing description of the <i>mapping process</i>
	 */
	String toString();
	
	/**
	 * Returns a readable exception description, including formatted list of stack trace elements.
	 * Each element of the list represents one stack frame and is written in a separate line. 
	 * The zeroth element of the list (assuming the list's length is non-zero) represents the top 
	 * of the stack, which is the last method invocation in the sequence. Typically, this is the 
	 * point at which this {@code Throwable} was created and thrown. The last element of the list 
     * (assuming the list's length is non-zero) represents the bottom of the stack, which is the 
     * first method invocation in the sequence.
	 * 
	 * @param the exception to make a readable description for
	 * @return the string containing description of the exception
	 */
	default String printStackTrace(Exception exception) {
		StringBuilder stackTrace = new StringBuilder();
		stackTrace.append(exception.getClass().getName()).append(" - ").append(exception.getMessage());
		
		for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
			stackTrace.append(System.lineSeparator());
			stackTrace.append("\t").append(stackTraceElement);
		}
	
		return stackTrace.toString();
	}
}
