package naakcii.by.api.util.parser;

import java.util.List;

import naakcii.by.api.util.parser.multisheet.ParsingResult;

public interface IDataParser {

	List<ParsingResult<?>> parseBasicData(String file);
	List<ParsingResult<?>> parseActionProducts(String file, String chainSynonym);
	
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
