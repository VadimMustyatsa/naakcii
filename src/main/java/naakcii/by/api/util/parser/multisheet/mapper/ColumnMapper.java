package naakcii.by.api.util.parser.multisheet.mapper;

public interface ColumnMapper {
	
	static String columnMappingMessage = "Column '{}' with index '{}' has been mapped on field '{}'.";
	static String resultColumnMappingMessage = "field '%s' - column '%d / %s'%c";
	
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
