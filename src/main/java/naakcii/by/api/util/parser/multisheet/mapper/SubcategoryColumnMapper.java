package naakcii.by.api.util.parser.multisheet.mapper;

import java.util.Formatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.category.Category;
import naakcii.by.api.subcategory.Subcategory;

@NoArgsConstructor
@Setter
@Getter
public class SubcategoryColumnMapper implements ColumnMapper {
	
	private static final Logger logger = LogManager.getLogger(SubcategoryColumnMapper.class);
	private final static String CATEGORY_FIELD = "category";
	private final static String NAME_FIELD = "name";
	private final static String PRIORITY_FIELD = "priority";
	private final static String IS_ACTIVE_FIELD = "isActive";
	private final static String CATEGORY_COLUMN = "Категория";
	private final static String NAME_COLUMN = "Подкатегория";
	private final static String PRIORITY_COLUMN = "Приоритет";
	private final static String IS_ACTIVE_COLUMN = "Активность";
	
	private Integer categoryColumnIndex;
	private Integer nameColumnIndex;
	private Integer priorityColumnIndex;
	private Integer isActiveColumnIndex;

	public void mapColumn(String cellValue, int columnIndex) {
		try {
			switch (cellValue) {
				case CATEGORY_COLUMN:
					categoryColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Subcategory.class.getDeclaredField(CATEGORY_FIELD));
					break;
				
				case NAME_COLUMN:
					nameColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Subcategory.class.getDeclaredField(NAME_FIELD));
					break;
					
				case PRIORITY_COLUMN:
					priorityColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Subcategory.class.getDeclaredField(PRIORITY_FIELD));
					break;
					
				case IS_ACTIVE_COLUMN:
					isActiveColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Subcategory.class.getDeclaredField(IS_ACTIVE_FIELD));
					break;
					
				default:
					logger.warn(UNSUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Subcategory.class.getName());
					break;
			}
		} catch (Exception exception) {
			logger.error(EXCEPTION_MAPPING_MESSAGE, cellValue, columnIndex, Category.class, Subcategory.class.getName(), printStackTrace(exception));
		}		
	}
	
	public boolean isCategoryMapped() {
		return categoryColumnIndex != null;
	}
	
	public boolean isNameMapped() {
		return nameColumnIndex != null;
	}
	
	public boolean isPriorityMapped() {
		return priorityColumnIndex != null;
	}
	
	public boolean isActiveMapped() {
		return isActiveColumnIndex != null;
	}

	public String toString() {
		try(Formatter formatter = new Formatter()) {
			formatter.format(RESULT_HEADER_MESSAGE, Subcategory.class.getName());
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, Subcategory.class.getDeclaredField(NAME_FIELD), nameColumnIndex, NAME_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, Subcategory.class.getDeclaredField(CATEGORY_FIELD), categoryColumnIndex, CATEGORY_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, Subcategory.class.getDeclaredField(PRIORITY_FIELD), priorityColumnIndex, PRIORITY_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, Subcategory.class.getDeclaredField(IS_ACTIVE_FIELD), isActiveColumnIndex, IS_ACTIVE_COLUMN, '.');
			return formatter.toString();
		} catch (Exception exception) {
			logger.error(RESULT_EXCEPTION_MESSAGE, Subcategory.class.getName(), printStackTrace(exception));
			return "Exception has occurred during the process of getting mapping result for entity '" + Subcategory.class.getName() + "' (see logs above).";
		}
	}
}
