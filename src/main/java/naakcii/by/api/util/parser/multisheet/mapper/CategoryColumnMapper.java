package naakcii.by.api.util.parser.multisheet.mapper;

import java.util.Formatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.category.Category;

/**
 * Provides the base implementation of the {@link naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper} 
 * interface for parsing of {@link naakcii.by.api.category.Category} class instances.
 * 
 * @see naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper
 * @see naakcii.by.api.category.Category
 */
@NoArgsConstructor
@Setter
@Getter
public class CategoryColumnMapper implements ColumnMapper {
	
	private static final Logger logger = LogManager.getLogger(CategoryColumnMapper.class);
	
	/**
	 * The name of the field declaring {@code Category's name} is {@value #NAME_FIELD}.
	 * 
	 * @see naakcii.by.api.category.Category#name
	 */
	private final static String NAME_FIELD = "name";
	
	/**
	 * The name of the field declaring a path to the file with {@code Category's icon} is 
	 * {@value #ICON_FIELD}.
	 * 
	 * @see naakcii.by.api.category.Category#icon
	 */
	private final static String ICON_FIELD = "icon";
	
	/**
	 * The name of the field representing {@code Category's priority} is {@value #PRIORITY_FIELD}.
	 * 
	 * @see naakcii.by.api.category.Category#priority
	 */
	private final static String PRIORITY_FIELD = "priority";
	
	/**
	 * The name of the field describing {@code Category's activity} is {@value #IS_ACTIVE_FIELD}.
	 * 
	 * @see naakcii.by.api.category.Category#isActive
	 */
	private final static String IS_ACTIVE_FIELD = "isActive";
	
	/**
	 * The header of the column containing the data for the {@code Category name} 
	 * field is {@value #NAME_COLUMN}.
	 * 
	 * @see naakcii.by.api.category.Category#name
	 */
	private final static String NAME_COLUMN = "Категория";
	
	/**
	 * The header of the column containing data for the {@code Category icon} field is 
	 * {@value #ICON_COLUMN}.
	 * 
	 * @see naakcii.by.api.category.Category#icon
	 */
	private final static String ICON_COLUMN = "Значок";
	
	/**
	 * The header of the column containing data for the {@code Category priority} field is 
	 * {@value #PRIORITY_COLUMN}.
	 * 
	 * @see naakcii.by.api.category.Category#priority
	 */
	private final static String PRIORITY_COLUMN = "Приоритет";
	
	/**
	 * The header of the column containing data for the {@code Category activity} field is 
	 * {@value #IS_ACTIVE_COLUMN}.
	 * 
	 * @see naakcii.by.api.category.Category#isActive
	 */
	private final static String IS_ACTIVE_COLUMN = "Активность";
	
	/**
	 * The index of the column, that has {@value #NAME_COLUMN} header and keeps data for 
	 * writing into the {@code Category name} field.
	 * 
	 * @see naakcii.by.api.category.Category#name
	 */
	private Integer nameColumnIndex;
	
	/**
	 * The index of the column, that has {@value #ICON_COLUMN} header and keeps data for 
	 * writing into the {@code Category icon} field.
	 * 
	 * @see naakcii.by.api.category.Category#icon
	 */
	private Integer iconColumnIndex;
	
	/**
	 * The index of the column, that has {@value #PRIORITY_COLUMN} header and keeps data for 
	 * writing into the {@code Category priority} field.
	 * 
	 * @see naakcii.by.api.category.Category#priority
	 */
	private Integer priorityColumnIndex;
	
	/**
	 * The index of the column, that has {@value #IS_ACTIVE_COLUMN} header and keeps data for 
	 * writing into the {@code Category activity} field.
	 * 
	 * @see naakcii.by.api.category.Category#isActive
	 */
	private Integer isActiveColumnIndex;
	
	/**
	 * Maps single column of the table on a field (property) of the {@code Category} class.
	 * The data containing in the column will be used for filling mapped field (property) of the 
	 * {@code Category} class instances.
	 * <p>
	 * Both <i>successful</i> and <i>unsuccessful</i> compilations of the mapping operation are 
	 * described by the special log messages. If exception occurs during the <i>mapping process</i> 
	 * then appropriate message containing detailed description of the {@code Throwable} is written 
	 * into the log.
	 * 
	 * @param cellValue the string representing the header of the mapped column
	 * @param columnIndex the index number of the mapped column
	 * @see naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper#SUCCESS_MAPPING_MESSAGE
	 * @see naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper#UNSUCCESS_MAPPING_MESSAGE
	 * @see naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper#EXCEPTION_MAPPING_MESSAGE
	 * @see naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper#printStackTrace(Exception)
	 */
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
	
	/**
	 * Reports whether {@code name column index} was defined.
	 * 
	 * @return {@code true} if {@code name column index} is not {@code null} or {@code false} otherwise
	 * @see #nameColumnIndex
	 */
	public boolean isNameMapped() {
		return nameColumnIndex != null;
	}
	
	/**
	 * Reports whether {@code icon column index} was defined.
	 * 
	 * @return {@code true} if {@code icon column index} is not {@code null} or {@code false} otherwise
	 * @see #iconColumnIndex
	 */
	public boolean isIconMapped() {
		return iconColumnIndex != null;
	}
	
	/**
	 * Reports whether {@code priority column index} was defined.
	 * 
	 * @return {@code true} if {@code priority column index} is not {@code null} or {@code false} otherwise
	 * @see #priorityColumnIndex
	 */
	public boolean isPriorityMapped() {
		return priorityColumnIndex != null;
	}
	
	/**
	 * Reports whether {@code activity column index} was defined.
	 * 
	 * @return {@code true} if {@code activity column index} is not {@code null} or {@code false} otherwise
	 * @see #isActiveColumnIndex
	 */
	public boolean isActiveMapped() {
		return isActiveColumnIndex != null;
	}

	/**
	 * Returns a detail report of the completed <i>mapping process</i> in a readable format.
	 * The report includes collations for each pair of mapped column and field (property) of the 
	 * {@code Category} class.
	 * <p>
	 * If exception occurs during getting the mapping report then appropriate message containing 
	 * detailed description of the {@code Throwable} is written into the log.
	 * 
	 * @return the string containing report of the mapping process
	 * @see naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper#RESULT_HEADER_MESSAGE
	 * @see naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper#RESULT_MAPPING_MESSAGE
	 * @see naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper#RESULT_EXCEPTION_MESSAGE
	 * @see naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper#printStackTrace(Exception)
	 */
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
