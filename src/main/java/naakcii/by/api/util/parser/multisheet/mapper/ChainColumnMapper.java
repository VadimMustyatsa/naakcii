package naakcii.by.api.util.parser.multisheet.mapper;

import java.util.Formatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.chain.Chain;

/**
 * Provides the base implementation of the {@link naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper} 
 * interface for parsing of {@link naakcii.by.api.chain.Chain} class instances. 
 * 
 * @see naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper
 * @see naakcii.by.api.chain.Chain
 */
@NoArgsConstructor
@Setter
@Getter
public class ChainColumnMapper implements ColumnMapper {
	
	private static final Logger logger = LogManager.getLogger(ChainColumnMapper.class);
	
	/**
	 * The name of the field declaring {@code Chain's name} is {@value #NAME_FIELD}.
	 * 
	 * @see naakcii.by.api.chain.Chain#name
	 */
	private final static String NAME_FIELD = "name";
	
	/**
	 * The name of the field declaring a path to the file with {@code Chain's logotype} is {@value #LOGO_FIELD}.
	 * 
	 * @see naakcii.by.api.chain.Chain#logo
	 */
	private final static String LOGO_FIELD = "logo";
	
	/**
	 * The name of the field declaring a path to the {@code Chain's official website} is {@value #LINK_FIELD}.
	 * 
	 * @see naakcii.by.api.chain.Chain#link
	 */
	private final static String LINK_FIELD = "link";
	
	/**
	 * The name of the field describing {@code Chain's activity} is {@value #IS_ACTIVE_FIELD}.
	 * 
	 * @see naakcii.by.api.chain.Chain#isActive
	 */
	private final static String IS_ACTIVE_FIELD = "isActive";
	
	/**
	 * The name of the field representing {@code Chain's synonym} is {@value #SYNONYM_FIELD}.
	 * 
	 * @see naakcii.by.api.chain.Chain#synonym
	 */
	private final static String SYNONYM_FIELD = "synonym";
	
	/**
	 * The header of the column containing data for the {@code Chain name} field is 
	 * {@value #NAME_COLUMN}.
	 * 
	 * @see naakcii.by.api.chain.Chain#name
	 */
	private static final String NAME_COLUMN = "Торговая сеть";
	
	/**
	 * The header of the column containing data for the {@code Chain link} field is 
	 * {@value #LINK_COLUMN}.
	 * 
	 * @see naakcii.by.api.chain.Chain#link
	 */
	private static final String LINK_COLUMN = "Ссылка";
	
	/**
	 * The header of the column containing data for the {@code Chain logo} field is 
	 * {@value #LOGO_COLUMN}.
	 * 
	 * @see naakcii.by.api.chain.Chain#logo
	 */
	private static final String LOGO_COLUMN = "Логотип";
	
	/**
	 * The header of the column containing data for the {@code Chain activity} field is 
	 * {@value #IS_ACTIVE_COLUMN}.
	 * 
	 * @see naakcii.by.api.chain.Chain#isActive
	 */
	private final static String IS_ACTIVE_COLUMN = "Активность";
	
	/**
	 * The header of the column containing data for the {@code Chain synonym} field is 
	 * {@value #SYNONYM_COLUMN}.
	 * 
	 * @see naakcii.by.api.chain.Chain#synonym
	 */
	private final static String SYNONYM_COLUMN = "Синоним";
	
	/**
	 * The index of the column, that has {@value #NAME_COLUMN} header and keeps data for 
	 * writing into the {@code Chain name} field.
	 * 
	 * @see naakcii.by.api.chain.Chain#name
	 */
	private Integer nameColumnIndex;
	
	/**
	 * The index of the column, that has {@value #LINK_COLUMN} header and keeps data for 
	 * writing into the {@code Chain link} field.
	 * 
	 * @see naakcii.by.api.chain.Chain#link
	 */
	private Integer linkColumnIndex;
	
	/**
	 * The index of the column, that has {@value #LOGO_COLUMN} header and keeps data for 
	 * writing into the {@code Chain logo} field.
	 * 
	 * @see naakcii.by.api.chain.Chain#logo
	 */
	private Integer logoColumnIndex;
	
	/**
	 * The index of the column, that has {@value #IS_ACTIVE_COLUMN} header and keeps data for 
	 * writing into the {@code Chain activity} field.
	 * 
	 * @see naakcii.by.api.chain.Chain#isActive
	 */
	private Integer isActiveColumnIndex;
	
	/**
	 * The index of the column, that has {@value #SYNONYM_COLUMN} header and keeps data for 
	 * writing into the {@code Chain synonym} field.
	 * 
	 * @see naakcii.by.api.chain.Chain#synonym
	 */
	private Integer synonymColumnIndex;
	
	/**
	 * Maps single column of the table on a field (property) of the {@code Chain} class.
	 * The data containing in the column will be used for filling mapped field (property) of the 
	 * {@code Chain} class instances.
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
	 * Reports whether {@code link column index} was defined.
	 * 
	 * @return {@code true} if {@code link column index} is not {@code null} or {@code false} otherwise
	 * @see #linkColumnIndex
	 */
	public boolean isLinkMapped() {
		return linkColumnIndex != null;
	}
	
	/**
	 * Reports whether {@code logo column index} was defined.
	 * 
	 * @return {@code true} if {@code logo column index} is not {@code null} or {@code false} otherwise
	 * @see #logoColumnIndex
	 */
	public boolean isLogoMapped() {
		return logoColumnIndex != null;
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
	 * Reports whether {@code synonym column index} was defined.
	 * 
	 * @return {@code true} if {@code synonym column index} is not {@code null} or {@code false} otherwise
	 * @see #synonymColumnIndex
	 */
	public boolean isSynonymMapped() {
		return synonymColumnIndex != null;
	}
	
	/**
	 * Returns a detail report of the completed <i>mapping process</i> in a readable format.
	 * The report includes collations for each pair of mapped column and field (property) of the 
	 * {@code Chain} class.
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
