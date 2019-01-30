package naakcii.by.api.util.parser.multisheet.mapper;

import java.util.Formatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.chainproducttype.ChainProductType;

@NoArgsConstructor
@Setter
@Getter
public class ChainProductTypeColumnMapper implements ColumnMapper {
	
	private static final Logger logger = LogManager.getLogger(ChainProductTypeColumnMapper.class);
	private static final String NAME_FIELD = "name";
	private static final String SYNONYM_FIELD = "synonym";
	private static final String TOOLTIP_FIELD = "tooltip";
	private static final String NAME_COLUMN = "Тип акции";
	private static final String SYNONYM_COLUMN = "Синоним";
	private static final String TOOLTIP_COLUMN = "Подсказка";
		
	private Integer nameColumnIndex;
	private Integer synonymColumnIndex;
	private Integer tooltipColumnIndex;

	@Override
	public void mapColumn(String cellValue, int columnIndex) {
		try {
			switch (cellValue) {
				case NAME_COLUMN:
					nameColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, ChainProductType.class.getDeclaredField(NAME_FIELD));
					break;
					
				case SYNONYM_COLUMN:
					synonymColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, ChainProductType.class.getDeclaredField(SYNONYM_FIELD));
					break;
				
				case TOOLTIP_COLUMN:
					tooltipColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, ChainProductType.class.getDeclaredField(TOOLTIP_FIELD));
					break;
					
				default:
					logger.warn(UNSUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, ChainProductType.class.getName());
					break;
			}
		} catch (Exception exception) {
			logger.error(EXCEPTION_MAPPING_MESSAGE, cellValue, columnIndex, ChainProductType.class.getName(), printStackTrace(exception));
		}
	}
	
	public boolean isNameMapped() {
		return nameColumnIndex != null;
	}
	
	public boolean isSynonymMapped() {
		return synonymColumnIndex != null;
	}
	
	public boolean isTooltipMapped() {
		return tooltipColumnIndex != null;
	}
	
	public String toString() {
		try(Formatter formatter = new Formatter()) {
			formatter.format(RESULT_HEADER_MESSAGE, ChainProductType.class.getName());
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, ChainProductType.class.getDeclaredField(NAME_FIELD), nameColumnIndex, NAME_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, ChainProductType.class.getDeclaredField(SYNONYM_FIELD), synonymColumnIndex, SYNONYM_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, ChainProductType.class.getDeclaredField(TOOLTIP_FIELD), tooltipColumnIndex, TOOLTIP_COLUMN, '.');
			return formatter.toString();
		} catch (Exception exception) {
			logger.error(RESULT_EXCEPTION_MESSAGE, ChainProductType.class.getName(), printStackTrace(exception));
			return "Exception has occurred during the process of getting mapping result for entity '" + ChainProductType.class.getName() + "' (see logs above).";
		}
	}
}
