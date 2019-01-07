package naakcii.by.api.util.parser.mapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.chain.Chain;

@NoArgsConstructor
@Setter
@Getter
public class ChainColumnMapper implements ColumnMapper {
	
	private static final Logger logger = LogManager.getLogger(ChainColumnMapper.class);
	private final static String NAME_FIELD = "name";
	private final static String LOGO_FIELD = "logo";
	private final static String LINK_FIELD = "link";
	private static final String NAME_COLUMN = "Торговая сеть";
	private static final String LINK_COLUMN = "Ссылка";
	private static final String LOGO_COLUMN = "Логотип";
	
	private Integer nameColumnIndex;
	private Integer linkColumnIndex;
	private Integer logoColumnIndex;
	
	public void mapColumn(String cellValue, int columnIndex) {
		try {
			switch (cellValue) {
				case NAME_COLUMN:
					nameColumnIndex = columnIndex;
					logger.info("Column '{}' with index '{}' was mapped on field '{}'.",
							cellValue, columnIndex, Chain.class.getDeclaredField(NAME_FIELD));
					break;
				
				case LINK_COLUMN:
					linkColumnIndex = columnIndex;
					logger.info("Column '{}' with index '{}' was mapped on field '{}'.",
							cellValue, columnIndex, Chain.class.getDeclaredField(LINK_FIELD));
					break;
					
				case LOGO_COLUMN:
					logoColumnIndex = columnIndex;
					logger.info("Column '{}' with index '{}' was mapped on field '{}'.",
							cellValue, columnIndex, Chain.class.getDeclaredField(LOGO_FIELD));
					break;
					
				default:
					logger.warn("Column '{}' with index '{}' wasn't mapped on any field of entity '{}'.",
							cellValue, columnIndex, Chain.class);
					break;
			}
		} catch (Exception exception) {
			logger.error("Column '{}' with index '{}' wasn't mapped on any field of entity '{}' due to exception: {}.",
					cellValue, columnIndex, Chain.class, exception);
		}	
	}
	
	public boolean isNameMapped() {
		return nameColumnIndex != null;
	}
	
	public boolean isLinkMapped() {
		return linkColumnIndex != null;
	}
	
	public boolean isLogoMapped() {
		return logoColumnIndex != null;
	}
	
	public String toString() {
		try {
			StringBuilder result = new StringBuilder("Header columns mapping on fields of entity '" + Chain.class.getName() + "':");
			result.append(System.lineSeparator());
			result.append("field " + Chain.class.getDeclaredField(NAME_FIELD) + " - " + nameColumnIndex + "/" + NAME_COLUMN + ";");
			result.append(System.lineSeparator());
			result.append("field " + Chain.class.getDeclaredField(LINK_FIELD) + " - " + linkColumnIndex + "/" + LINK_COLUMN + ";");
			result.append(System.lineSeparator());
			result.append("field " + Chain.class.getDeclaredField(LOGO_FIELD) + " - " + logoColumnIndex + "/" + LOGO_COLUMN + ".");
			return result.toString();
		} catch (Exception exception) {
			logger.error("Exception has occurred during the process of header columns mapping on fields of entity '{}': {}.", 
					Chain.class.getName(), exception);
			return "Header columns mapping on fields of entity '" + Chain.class.getName() + "' was finished with exception.";
		}
	}
}
