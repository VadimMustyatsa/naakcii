package naakcii.by.api.util.parser.multisheet.mapper;

import java.util.Formatter;

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
	private final static String IS_ACTIVE_FIELD = "isActive";
	private final static String SYNONYM_FIELD = "synonym";
	private static final String NAME_COLUMN = "Торговая сеть";
	private static final String LINK_COLUMN = "Ссылка";
	private static final String LOGO_COLUMN = "Логотип";
	private final static String IS_ACTIVE_COLUMN = "Активность";
	private final static String SYNONYM_COLUMN = "Синоним";
	
	private Integer nameColumnIndex;
	private Integer linkColumnIndex;
	private Integer logoColumnIndex;
	private Integer isActiveColumnIndex;
	private Integer synonymColumnIndex;
	
	public void mapColumn(String cellValue, int columnIndex) {
		try {
			switch (cellValue) {
				case NAME_COLUMN:
					nameColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Chain.class.getDeclaredField(NAME_FIELD));
					break;
				
				case LINK_COLUMN:
					linkColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Chain.class.getDeclaredField(LINK_FIELD));
					break;
					
				case LOGO_COLUMN:
					logoColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Chain.class.getDeclaredField(LOGO_FIELD));
					break;
					
				case IS_ACTIVE_COLUMN:
					isActiveColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Chain.class.getDeclaredField(IS_ACTIVE_FIELD));
					break;
					
				case SYNONYM_COLUMN:
					synonymColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Chain.class.getDeclaredField(SYNONYM_FIELD));
					break;
					
				default:
					logger.warn(UNSUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Chain.class.getName());
					break;
			}
		} catch (Exception exception) {
			logger.error(EXCEPTION_MAPPING_MESSAGE,	cellValue, columnIndex, Chain.class.getName(), printStackTrace(exception));
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
	
	public boolean isActiveMapped() {
		return isActiveColumnIndex != null;
	}
	
	public boolean isSynonymMapped() {
		return synonymColumnIndex != null;
	}
	
	public String toString() {
		try(Formatter formatter = new Formatter()) {
			formatter.format(RESULT_HEADER_MESSAGE, Chain.class.getName());
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, Chain.class.getDeclaredField(NAME_FIELD), nameColumnIndex, NAME_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, Chain.class.getDeclaredField(LINK_FIELD), linkColumnIndex, LINK_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, Chain.class.getDeclaredField(LOGO_FIELD), logoColumnIndex, LOGO_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, Chain.class.getDeclaredField(IS_ACTIVE_FIELD), isActiveColumnIndex, IS_ACTIVE_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, Chain.class.getDeclaredField(SYNONYM_FIELD), synonymColumnIndex, SYNONYM_COLUMN, '.');
			return formatter.toString();
		} catch (Exception exception) {
			logger.error(RESULT_EXCEPTION_MESSAGE, Chain.class.getName(), printStackTrace(exception));
			return "Exception has occurred during the process of getting mapping result for entity '" + Chain.class.getName() + "' (see logs above).";
		}
	}
}
