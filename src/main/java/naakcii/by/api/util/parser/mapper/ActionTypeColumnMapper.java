package naakcii.by.api.util.parser.mapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.actiontype.ActionType;

@NoArgsConstructor
@Setter
@Getter
public class ActionTypeColumnMapper implements ColumnMapper {
	
	private static final Logger logger = LogManager.getLogger(ActionTypeColumnMapper.class);
	private final static String NAME_FIELD = "name";
	private final static String TOOLTIP_FIELD = "tooltip";
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
					logger.info("Column '{}' with index '{}' was mapped on field '{}'.",
							cellValue, columnIndex, ActionType.class.getDeclaredField(NAME_FIELD));
					break;
				
				case TOOLTIP_COLUMN:
					tooltipColumnIndex = columnIndex;
					logger.info("Column '{}' with index '{}' was mapped on field '{}'.",
							cellValue, columnIndex, ActionType.class.getDeclaredField(TOOLTIP_FIELD));
					break;
					
				default:
					logger.warn("Column '{}' with index '{}' wasn't mapped on any field of entity '{}'.",
							cellValue, columnIndex, ActionType.class);
					break;
			}
		} catch (Exception exception) {
			logger.error("Column '{}' with index '{}' wasn't mapped on any field of entity '{}' due to exception: {}.",
					cellValue, columnIndex, ActionType.class, exception);
		}
	}
	
	public boolean isNameMapped() {
		return nameColumnIndex != null;
	}
	
	public boolean isTooltipMapped() {
		return tooltipColumnIndex != null;
	}
	
	public String toString() {
		try {
			StringBuilder result = new StringBuilder("Header columns mapping on fields of entity '" + ActionType.class.getName() + "':");
			result.append(System.lineSeparator());
			result.append("field " + ActionType.class.getDeclaredField(NAME_FIELD) + " - " + nameColumnIndex + "/" + NAME_COLUMN + ";");
			result.append(System.lineSeparator());
			result.append("field " + ActionType.class.getDeclaredField(TOOLTIP_FIELD) + " - " + tooltipColumnIndex + "/" + TOOLTIP_COLUMN + ".");
			return result.toString();
		} catch (Exception exception) {
			logger.error("Exception has occurred during the process of header columns mapping on fields of entity '{}': {}.", 
					ActionType.class.getName(), exception);
			return "Header columns mapping on fields of entity '" + ActionType.class.getName() + "' was finished with exception.";
		}
	}
}
