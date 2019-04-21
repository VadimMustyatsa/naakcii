package naakcii.by.api.util.parser.multisheet.mapper;

import java.util.Formatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.chainproducttype.ChainProductType;

/**
 * Provides the base implementation of the {@link naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper} 
 * interface for parsing of {@link naakcii.by.api.chainproducttype.ChainProductType} class instances.
 * 
 * @see naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper
 * @see naakcii.by.api.chainproducttype.ChainProductType
 */
@NoArgsConstructor
@Setter
@Getter
public class ChainProductTypeColumnMapper implements ColumnMapper {
	
	private static final Logger logger = LogManager.getLogger(ChainProductTypeColumnMapper.class);
	
	/**
	 * The name of the field declaring {@code Chain Product Type's name} is {@value #NAME_FIELD}.
	 * 
	 * @see naakcii.by.api.chainproducttype.ChainProductType#name
	 */
	private static final String NAME_FIELD = "name";
	
	/**
	 * The name of the field representing {@code Chain Product Type's synonym} is {@value #SYNONYM_FIELD}.
	 * 
	 * @see naakcii.by.api.chainproducttype.ChainProductType#synonym
	 */
	private static final String SYNONYM_FIELD = "synonym";
	
	/**
	 * The name of the field declaring {@code Chain Product Type's tool tip} is {@value #TOOLTIP_FIELD}.
	 * 
	 * @see naakcii.by.api.chainproducttype.ChainProductType#tooltip
	 */
	private static final String TOOLTIP_FIELD = "tooltip";
	
	/**
	 * The header of the column containing data for the {@code @code Chain Product Type name} 
	 * field is {@value #NAME_COLUMN}.
	 * 
	 * @see naakcii.by.api.chainproducttype.ChainProductType#name
	 */
	private static final String NAME_COLUMN = "Тип акции";
	
	/**
	 * The header of the column containing data for the {@code @code Chain Product Type synonym} 
	 * field is {@value #SYNONYM_COLUMN}.
	 * 
	 * @see naakcii.by.api.chainproducttype.ChainProductType#synonym
	 */
	private static final String SYNONYM_COLUMN = "Синоним";
	
	/**
	 * The header of the column containing data for the {@code @code Chain Product Type tooltip} 
	 * field is {@value #TOOLTIP_COLUMN}.
	 * 
	 * @see naakcii.by.api.chainproducttype.ChainProductType#tooltip
	 */
	private static final String TOOLTIP_COLUMN = "Подсказка";
	
	/**
	 * The index of the column, that has {@value #NAME_COLUMN} header and keeps data for writing 
	 * into the {@code Chain Product Type name} field.
	 * 
	 * @see naakcii.by.api.chainproducttype.ChainProductType#name
	 */
	private Integer nameColumnIndex;
	
	/**
	 * The index of the column, that has {@value #SYNONYM_COLUMN} header and keeps data for writing 
	 * into the {@code Chain Product Type synonym} field.
	 * 
	 * @see naakcii.by.api.chainproducttype.ChainProductType#synonym
	 */
	private Integer synonymColumnIndex;
	
	/**
	 * The index of the column, that has {@value #TOOLTIP_COLUMN} header and keeps data for writing 
	 * into the {@code Chain Product Type tooltip} field.
	 * 
	 * @see naakcii.by.api.chainproducttype.ChainProductType#tooltip
	 */
	private Integer tooltipColumnIndex;

	/**
	 * Maps single column of the table on a field (property) of the {@code Chain Product Type} class.
	 * The data containing in the column will be used for filling mapped field (property) of the 
	 * {@code Chain Product Type} class instances.
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
	 * Reports whether {@code synonym column index} was defined.
	 * 
	 * @return {@code true} if {@code synonym column index} is not {@code null} or {@code false} otherwise
	 * @see #synonymColumnIndex
	 */
	public boolean isSynonymMapped() {
		return synonymColumnIndex != null;
	}
	
	/**
	 * Reports whether {@code tooltip column index} was defined.
	 * 
	 * @return {@code true} if {@code tooltip column index} is not {@code null} or {@code false} otherwise
	 * @see #tooltipColumnIndex
	 */
	public boolean isTooltipMapped() {
		return tooltipColumnIndex != null;
	}
	
	/**
	 * Returns a detail report of the completed <i>mapping process</i> in a readable format.
	 * The report includes collations for each pair of mapped column and field (property) of the 
	 * {@code Chain Product Type} class.
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
