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
public class ActionTypeColumnMapper implements ColumnMapper {
	
	private static final Logger logger = LogManager.getLogger(ActionTypeColumnMapper.class);
	private static final String NAME_FIELD = "name";
	private static final String TOOLTIP_FIELD = "tooltip";
	private static final String NAME_COLUMN = "Тип акции";
	private static final String TOOLTIP_COLUMN = "Подсказка";
		
	private Integer nameColumnIndex;
	private Integer tooltipColumnIndex;

	@Override
	public void mapColumn(String cellValue, int columnIndex) {
		try {
			switch (cellValue) {
				case NAME_COLUMN:
					nameColumnIndex = columnIndex;
					logger.info(columnMappingMessage, cellValue, columnIndex, ChainProductType.class.getDeclaredField(NAME_FIELD));
					break;
				
				case TOOLTIP_COLUMN:
					tooltipColumnIndex = columnIndex;
					logger.info(columnMappingMessage, cellValue, columnIndex, ChainProductType.class.getDeclaredField(TOOLTIP_FIELD));
					break;
					
				default:
					logger.warn("Column '{}' with index '{}' hasn't been mapped on any field of entity '{}'.",
							cellValue, columnIndex, ChainProductType.class);
					break;
			}
		} catch (Exception exception) {
			logger.error("Column '{}' with index '{}' hasn't been mapped on any field of entity '{}' due to exception: {}.",
					cellValue, columnIndex, ChainProductType.class, printStackTrace(exception));
		}
	}
	
	public boolean isNameMapped() {
		return nameColumnIndex != null;
	}
	
	public boolean isTooltipMapped() {
		return tooltipColumnIndex != null;
	}
	
	public String toString() {
		try(Formatter formatter = new Formatter()) {
			formatter.format("Columns mapping on fields of entity '%s':", ChainProductType.class.getName());
			formatter.format("%n");
			formatter.format(resultColumnMappingMessage, ChainProductType.class.getDeclaredField(NAME_FIELD), nameColumnIndex, NAME_COLUMN, ';');
			formatter.format("%n");
			formatter.format(resultColumnMappingMessage, ChainProductType.class.getDeclaredField(TOOLTIP_FIELD), tooltipColumnIndex, TOOLTIP_COLUMN, '.');
			return formatter.toString();
		} catch (Exception exception) {
			logger.error("Exception has occurred during the process of getting mapping result for entity '{}': {}.", 
					ChainProductType.class, printStackTrace(exception));
			return "Exception has occurred during the process of getting mapping result for entity '" + ChainProductType.class + "' (see logs).";
		}
	}
}
