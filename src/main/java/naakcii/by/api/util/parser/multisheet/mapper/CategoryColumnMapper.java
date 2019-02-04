package naakcii.by.api.util.parser.multisheet.mapper;

import java.util.Formatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.category.Category;

@NoArgsConstructor
@Setter
@Getter
public class CategoryColumnMapper implements ColumnMapper {
	
	private static final Logger logger = LogManager.getLogger(CategoryColumnMapper.class);
	private final static String NAME_FIELD = "name";
	private final static String ICON_FIELD = "icon";
	private final static String PRIORITY_FIELD = "priority";
	private final static String IS_ACTIVE_FIELD = "isActive";
	private final static String NAME_COLUMN = "Категория";
	private final static String ICON_COLUMN = "Значок";
	private final static String PRIORITY_COLUMN = "Приоритет";
	private final static String IS_ACTIVE_COLUMN = "Активность";
	
	private Integer nameColumnIndex;
	private Integer iconColumnIndex;
	private Integer priorityColumnIndex;
	private Integer isActiveColumnIndex;
	
	

	public void mapColumn(String cellValue, int columnIndex) {
		try {
			switch (cellValue) {
				case NAME_COLUMN:
					nameColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Category.class.getDeclaredField(NAME_FIELD));
					break;
					
				case ICON_COLUMN:
					iconColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Category.class.getDeclaredField(ICON_FIELD));
					break;
					
				case PRIORITY_COLUMN:
					priorityColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Category.class.getDeclaredField(PRIORITY_FIELD));
					break;
					
				case IS_ACTIVE_COLUMN:
					isActiveColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Category.class.getDeclaredField(IS_ACTIVE_FIELD));
					break;
					
				default:
					logger.warn(UNSUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Category.class.getName());
					break;
			}
		} catch (Exception exception) {
			logger.error(EXCEPTION_MAPPING_MESSAGE, cellValue, columnIndex, Category.class.getName(), printStackTrace(exception));
		}		
	}
	
	public boolean isNameMapped() {
		return nameColumnIndex != null;
	}
	
	public boolean isIconMapped() {
		return iconColumnIndex != null;
	}
	
	public boolean isPriorityMapped() {
		return priorityColumnIndex != null;
	}
	
	public boolean isActiveMapped() {
		return isActiveColumnIndex != null;
	}

	public String toString() {
		try(Formatter formatter = new Formatter()) {
			formatter.format(RESULT_HEADER_MESSAGE, Category.class.getName());
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, Category.class.getDeclaredField(NAME_FIELD), nameColumnIndex, NAME_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, Category.class.getDeclaredField(ICON_FIELD), iconColumnIndex, ICON_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, Category.class.getDeclaredField(PRIORITY_FIELD), priorityColumnIndex, PRIORITY_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, Category.class.getDeclaredField(IS_ACTIVE_FIELD), isActiveColumnIndex, IS_ACTIVE_COLUMN, '.');
			return formatter.toString();
		} catch (Exception exception) {
			logger.error(RESULT_EXCEPTION_MESSAGE, Category.class.getName(), printStackTrace(exception));
			return "Exception has occurred during the process of getting mapping result for entity '" + Category.class.getName() + "' (see logs above).";
		}
	}
}
