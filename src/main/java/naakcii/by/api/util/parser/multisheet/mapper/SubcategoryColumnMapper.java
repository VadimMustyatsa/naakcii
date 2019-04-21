package naakcii.by.api.util.parser.multisheet.mapper;

import java.util.Formatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.category.Category;
import naakcii.by.api.subcategory.Subcategory;

/**
 * Provides the base implementation of the {@link naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper} 
 * interface for parsing of {@link naakcii.by.api.subcategory.Subcategory} class instances.
 * 
 * @see naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper
 * @see naakcii.by.api.subcategory.Subcategory
 */
@NoArgsConstructor
@Setter
@Getter
public class SubcategoryColumnMapper implements ColumnMapper {
	
	private static final Logger logger = LogManager.getLogger(SubcategoryColumnMapper.class);
	
	/**
	 * {@value #CATEGORY_FIELD} is name of the {@code Subcategory category} field being
	 * the reference to the instance of {@link naakcii.by.api.category.Category} class.
	 * 
	 * @see naakcii.by.api.category.Category
	 * @see naakcii.by.api.subcategory.Subcategory#category
	 */
	private final static String CATEGORY_FIELD = "category";
	
	/**
	 * The name of the field declaring {@code Subcategory's name} is {@value #NAME_FIELD}.
	 * 
	 * @see naakcii.by.api.subcategory.Subcategory#name
	 */
	private final static String NAME_FIELD = "name";
	
	/**
	 * The name of the field representing {@code Subcategory's priority} is {@value #PRIORITY_FIELD}.
	 * 
	 * @see naakcii.by.api.subcategory.Subcategory#priority
	 */
	private final static String PRIORITY_FIELD = "priority";
	
	/**
	 * The name of the field describing {@code Subcategory's activity} is {@value #IS_ACTIVE_FIELD}.
	 * 
	 * @see naakcii.by.api.subcategory.Subcategory#isActive
	 */
	private final static String IS_ACTIVE_FIELD = "isActive";
	
	/**
	 * {@value #CATEGORY_COLUMN} is header of the column containing references to instances 
	 * of the {@link naakcii.by.api.category.Category} class to write into the
	 * {@code Subcategory category} field.
	 * 
	 * @see naakcii.by.api.category.Category
	 * @see naakcii.by.api.subcategory.Subcategory#category
	 */
	private final static String CATEGORY_COLUMN = "Категория";
	
	/**
	 * The header of the column containing data for the {@code Subcategory name} field is 
	 * {@value #NAME_COLUMN}.
	 * 
	 * @see naakcii.by.api.subcategory.Subcategory#name
	 */
	private final static String NAME_COLUMN = "Подкатегория";
	
	/**
	 * The header of the column containing data for the {@code Subcategory priority} field is 
	 * {@value #PRIORITY_COLUMN}.
	 * 
	 * @see naakcii.by.api.subcategory.Subcategory#priority
	 */
	private final static String PRIORITY_COLUMN = "Приоритет";
	
	/**
	 * The header of the column containing data for the {@code Subcategory activity} field is 
	 * {@value #IS_ACTIVE_COLUMN}.
	 * 
	 * @see naakcii.by.api.subcategory.Subcategory#isActive
	 */
	private final static String IS_ACTIVE_COLUMN = "Активность";
	
	/**
	 * The index of the column, that has {@value #CATEGORY_COLUMN} header and keeps references to 
	 * instances of the {@link naakcii.by.api.category.Category} class to write into the 
	 * {@code Subcategory category} field. 
	 * 
	 * @see naakcii.by.api.category.Category
	 * @see naakcii.by.api.subcategory.Subcategory#category
	 */
	private Integer categoryColumnIndex;
	
	/**
	 * The index of the column, that has {@value #NAME_COLUMN} header and keeps data for 
	 * writing into the {@code Subcategory name} field.
	 * 
	 * @see naakcii.by.api.subcategory.Subcategory#name
	 */
	private Integer nameColumnIndex;
	
	/**
	 * The index of the column, that has {@value #PRIORITY_COLUMN} header and keeps data for 
	 * writing into the {@code Subcategory priority} field.
	 * 
	 * @see naakcii.by.api.subcategory.Subcategory#priority
	 */
	private Integer priorityColumnIndex;
	
	/**
	 * The index of the column, that has {@value #IS_ACTIVE_COLUMN} header and keeps data for 
	 * writing into the {@code Subcategory activity} field.
	 * 
	 * @see naakcii.by.api.subcategory.Subcategory#isActive
	 */
	private Integer isActiveColumnIndex;

	/**
	 * Maps single column of the table on a field (property) of the {@code Subcategory} class.
	 * The data containing in the column will be used for filling mapped field (property) of the 
	 * {@code Subcategory} class instances.
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
	
	/**
	 * Reports whether {@code category column index} was defined.
	 * 
	 * @return {@code true} if {@code category column index} is not {@code null} or {@code false} otherwise
	 * @see #categoryColumnIndex
	 */
	public boolean isCategoryMapped() {
		return categoryColumnIndex != null;
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
	 * {@code Subcategory} class.
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
