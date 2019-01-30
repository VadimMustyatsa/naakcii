package naakcii.by.api.util.parser.multisheet.mapper;

public interface ColumnMapper {
	
	static String SUCCESS_MAPPING_MESSAGE = "Column '{}' with index '{}' has been mapped on field '{}'.";
	static String UNSUCCESS_MAPPING_MESSAGE = "Column '{}' with index '{}' hasn't been mapped on any field of entity '{}'.";
	static String EXCEPTION_MAPPING_MESSAGE = "Column '{}' with index '{}' hasn't been mapped on any field of entity '{}' due to exception: {}.";
	static String RESULT_HEADER_MESSAGE = "Columns mapping on fields of entity '%s':";
	static String RESULT_MAPPING_MESSAGE = "field '%s' - column '%d / %s'%c";
	static String RESULT_EXCEPTION_MESSAGE = "Exception has occurred during the process of getting mapping result for entity '{}': {}.";
	
	void mapColumn(String cellValue, int columnIndex);
	
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
