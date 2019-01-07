package naakcii.by.api.util.parser;

import java.util.List;

public interface DataParser {

	List<ParsingResult<?>> parseBasicData(String file);
	List<ParsingResult<?>> parseActionProducts(String file);
}
