package naakcii.by.api.util.parser.multisheet.mapper;

import java.util.Formatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.chainproduct.ChainProduct;
import naakcii.by.api.product.Product;

/**
 * Provides the base implementation of the {@link naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper} 
 * interface for parsing of {@link naakcii.by.api.chainproduct.ChainProduct} and 
 * {@link naakcii.by.api.product.Product} classes instances.
 * 
 * @see naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper
 * @see naakcii.by.api.chainproducttype.ChainProductType
 */
@NoArgsConstructor
@Setter
@Getter
public class ChainProductColumnMapper implements ColumnMapper {
	
	private static final Logger logger = LogManager.getLogger(ChainProductColumnMapper.class);
	
	/**
	 * The name of the field declaring {@code Product's name} is {@value #NAME_FIELD}.
	 * 
	 * @see naakcii.by.api.product.Product#name
	 */
	private final static String NAME_FIELD = "name";
	
	/**
	 * The name of the field declaring {@code Product's barcode} is {@value #BARCODE_FIELD}.
	 * 
	 * @see naakcii.by.api.product.Product#barcode
	 */
	private final static String BARCODE_FIELD = "barcode";
	
	/**
	 * {@value #UNIT_FIELD} is name of the {@code Product unit of measure} field being
	 * the reference to the instance of {@link naakcii.by.api.unitofmeasure.UnitOfMeasure} class.
	 * 
	 * @see naakcii.by.api.unitofmeasure.UnitOfMeasure
	 * @see naakcii.by.api.product.Product#unitOfMeasure
	 */
	private final static String UNIT_FIELD = "unitOfMeasure";
	
	/**
	 * The name of the field declaring {@code Product's manufacturer} is {@value #MANUFACTURER_FIELD}.
	 * 
	 * @see naakcii.by.api.product.Product#manufacturer
	 */
	private final static String MANUFACTURER_FIELD = "manufacturer";
	
	/**
	 * The name of the field declaring {@code Product's brand} is {@value #BRAND_FIELD}.
	 * 
	 * @see naakcii.by.api.product.Product#brand
	 */
	private final static String BRAND_FIELD = "brand";
	
	/**
	 * The name of the field representing {@code Chain Product's base price} is {@value #BASE_PRICE_FIELD}.
	 * 
	 * @see naakcii.by.api.chainproduct.ChainProduct#basePrice
	 */
	private final static String BASE_PRICE_FIELD = "basePrice";
	
	/**
	 * The name of the field representing {@code Chain Product's discount price} is {@value #DISCOUNT_PRICE_FIELD}.
	 * 
	 * @see naakcii.by.api.chainproduct.ChainProduct#discountPrice
	 */
	private final static String DISCOUNT_PRICE_FIELD = "discountPrice";
	
	/**
	 * The name of the field declaring {@code Chain Product's start date} is {@value #START_DATE_FIELD}.
	 * 
	 * @see naakcii.by.api.chainproduct.ChainProduct#startDate
	 */
	private final static String START_DATE_FIELD = "startDate";
	
	/**
	 * The name of the field declaring {@code Chain Product's end date} is {@value #END_DATE_FIELD}.
	 * 
	 * @see naakcii.by.api.chainproduct.ChainProduct#endDate
	 */
	private final static String END_DATE_FIELD = "endDate";
	
	/**
	 * {@value #TYPE_FIELD} is name of the {@code Chain Product type} field being 
	 * the reference to the instance of {@link naakcii.by.api.chainproducttype.ChainProductType} class.
	 * 
	 * @see naakcii.by.api.chainproducttype.ChainProductType
	 * @see naakcii.by.api.chainproduct.ChainProduct#type
	 */
	private final static String TYPE_FIELD = "type";
	
	/**
	 * {@value #TYPE_FIELD} is name of the {@code Product country of origin} field being 
	 * the reference to the instance of {@link naakcii.by.api.country.Country} class.
	 * 
	 * @see naakcii.by.api.country.Country
	 * @see naakcii.by.api.product.Product#countryOfOrigin
	 */
	private final static String COUNTRY_OF_ORIGIN_FIELD = "countryOfOrigin";
	
	/**
	 * The header of the column containing data for the {@code Product name} field is 
	 * {@value #NAME_COLUMN}.
	 * 
	 * @see naakcii.by.api.product.Product#name
	 */
	private static final String NAME_COLUMN = "Наименование";
	
	/**
	 * The header of the column containing data for the {@code Product barcode} field is 
	 * {@value #BARCODE_COLUMN}.
	 * 
	 * @see naakcii.by.api.product.Product#barcode
	 */
	private static final String BARCODE_COLUMN = "Штрих-код";
	
	/**
	 * {@value #UNIT_COLUMN} is header of the column containing references to instances 
	 * of the {@link naakcii.by.api.unitofmeasure.UnitOfMeasure} class to write into the 
	 * {@code Product unit of measure} field.
	 * 
	 * @see naakcii.by.api.unitofmeasure.UnitOfMeasure
	 * @see naakcii.by.api.product.Product#unitOfMeasure
	 */
	private static final String UNIT_COLUMN = "Единица продажи";
	
	/**
	 * The header of the column containing data for the {@code Product manufacturer} field is 
	 * {@value #MANUFACTURER_COLUMN}.
	 * 
	 * @see naakcii.by.api.product.Product#manufacturer
	 */
	private static final String MANUFACTURER_COLUMN = "Производитель";
	
	/**
	 * The header of the column containing data for the {@code Product brand} field is 
	 * {@value #BRAND_COLUMN}.
	 * 
	 * @see naakcii.by.api.product.Product#brand
	 */
	private static final String BRAND_COLUMN = "Бренд";
	
	/**
	 * {@value #COUNTRY_OF_ORIGIN_COLUMN} is header of the column containing references to instances 
	 * of the {@link naakcii.by.api.country.Country} class to write into the
	 * {@code Product country of origin} field.
	 * 
	 * @see naakcii.by.api.country.Country
	 * @see naakcii.by.api.product.Product#countryOfOrigin
	 */
	private static final String COUNTRY_OF_ORIGIN_COLUMN = "КодСтраны";
	
	/**
	 * The header of the column containing data for the {@code Chain Product base price} field is 
	 * {@value #BASE_PRICE_COLUMN}.
	 * 
	 * @see naakcii.by.api.chainproduct.ChainProduct#basePrice
	 */
	private final static String BASE_PRICE_COLUMN = "Цена до акции";
	
	/**
	 * The header of the column containing data for the {@code Chain Product discount price} field 
	 * is {@value #DISCOUNT_PRICE_COLUMN}.
	 * 
	 * @see naakcii.by.api.chainproduct.ChainProduct#discountPrice
	 */
	private final static String DISCOUNT_PRICE_COLUMN = "Цена";
	
	/**
	 * The header of the column containing data for the {@code Chain Product start date} field is 
	 * {@value #START_DATE_COLUMN}.
	 * 
	 * @see naakcii.by.api.chainproduct.ChainProduct#startDate
	 */
	private final static String START_DATE_COLUMN = "Дата действия «с»";
	
	/**
	 * The header of the column containing data for the {@code Chain Product end date} field is 
	 * {@value #END_DATE_COLUMN}.
	 * 
	 * @see naakcii.by.api.chainproduct.ChainProduct#endDate
	 */
	private final static String END_DATE_COLUMN = "Дата действия «по»";
	
	/**
	 * {@value #TYPE_COLUMN} is header of the column containing references to instances 
	 * of the {@link naakcii.by.api.chainproducttype.ChainProductType} class to write into the 
	 * {@code Chain Product type} field.
	 * 
	 * @see naakcii.by.api.chainproducttype.ChainProductType
	 * @see naakcii.by.api.chainproduct.ChainProduct#type
	 */
	private final static String TYPE_COLUMN = "ВидРеклМероприятия";
	
	/**
	 * The index of the column, that has {@value #NAME_COLUMN} header and keeps data for writing 
	 * into the {@code Product name} field.
	 * 
	 * @see naakcii.by.api.product.Product#name
	 */
	private Integer nameColumnIndex;
	
	/**
	 * The index of the column, that has {@value #BARCODE_COLUMN} header and keeps data for writing 
	 * into the {@code Product barcode} field.
	 * 
	 * @see naakcii.by.api.product.Product#barcode
	 */
	private Integer barcodeColumnIndex;
	
	/**
	 * The index of the column, that has {@value #UNIT_COLUMN} header and keeps references to 
	 * instances of the {@link naakcii.by.api.unitofmeasure.UnitOfMeasure} class to write into the 
	 * {@code Product unit of measure} field. 
	 * 
	 * @see naakcii.by.api.unitofmeasure.UnitOfMeasure
	 * @see naakcii.by.api.product.Product#unitOfMeasure
	 */
	private Integer unitColumnIndex;
	
	/**
	 * The index of the column, that has {@value #MANUFACTURER_COLUMN} header and keeps data for 
	 * writing into the {@code Product manufacturer} field.
	 * 
	 * @see naakcii.by.api.product.Product#manufacturer
	 */
	private Integer manufacturerColumnIndex;
	
	/**
	 * The index of the column, that has {@value #BRAND_COLUMN} header and keeps data for writing 
	 * into the {@code Product brand} field.
	 * 
	 * @see naakcii.by.api.product.Product#brand
	 */
	private Integer brandColumnIndex;
	
	/**
	 * The index of the column, that has {@value #COUNTRY_OF_ORIGIN_COLUMN} header and keeps 
	 * references to instances of the {@link naakcii.by.api.country.Country} class to write into 
	 * the {@code Product country of origin} field. 
	 * 
	 * @see naakcii.by.api.country.Country
	 * @see naakcii.by.api.product.Product#countryOfOrigin
	 */
	private Integer countryOfOriginColumnIndex;
	
	/**
	 * The index of the column, that has {@value #BASE_PRICE_COLUMN} header and keeps data for 
	 * writing into the {@code Chain Product base price} field.
	 * 
	 * @see naakcii.by.api.chainproduct.ChainProduct#basePrice
	 */
	private Integer basePriceColumnIndex;
	
	/**
	 * The index of the column, that has {@value #DISCOUNT_PRICE_COLUMN} header and keeps data for 
	 * writing into the {@code Chain Product discount price} field.
	 * 
	 * @see naakcii.by.api.chainproduct.ChainProduct#discountPrice
	 */
	private Integer discountPriceColumnIndex;
	
	/**
	 * The index of the column, that has {@value #START_DATE_COLUMN} header and keeps data for 
	 * writing into the {@code Chain Product start date} field.
	 * 
	 * @see naakcii.by.api.chainproduct.ChainProduct#startDate
	 */
	private Integer startDateColumnIndex;
	
	/**
	 * The index of the column, that has {@value #END_DATE_COLUMN} header and keeps data for 
	 * writing into the {@code Chain Product end date} field.
	 * 
	 * @see naakcii.by.api.chainproduct.ChainProduct#endDate
	 */
	private Integer endDateColumnIndex;
	
	/**
	 * The index of the column, that has {@value #TYPE_COLUMN} header and keeps references to 
	 * instances of the {@link naakcii.by.api.chainproducttype.ChainProductType} class to write 
	 * into the {@code Chain Product type} field. 
	 * 
	 * @see naakcii.by.api.chainproducttype.ChainProductType
	 * @see naakcii.by.api.chainproduct.ChainProduct#type
	 */
	private Integer typeColumnIndex;
	
	/**
	 * Maps single column of the table on a field (property) of the {@code Product} or 
	 * {@code Chain Product} classes.
	 * The data containing in the column will be used for filling mapped field (property) of the 
	 * {@code Product} or {@code Chain Product} classes instances.
	 * <p>
	 * Both <i>successful</i> and <i>unsuccessful</i> compilations of the mapping operation are 
	 * described by the special log messages. If exception occurs during the <i>mapping process</i> 
	 * then appropriate message containing detailed description of the {@code Throwable} is written 
	 * into the log.
	 * 
	 * @param cellValue the string representing the header of the mapped column
	 * @param columnIndex the index number of the mapped column
	 * @see naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper#SUCCESS_MAPPING_MESSAGE
	 * @see naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper#printStackTrace(Exception)
	 */
	public void mapColumn(String cellValue, int columnIndex) {
		try {
			switch (cellValue) {
				case NAME_COLUMN:
					nameColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Product.class.getDeclaredField(NAME_FIELD));
					break;
				
				case BARCODE_COLUMN:
					barcodeColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Product.class.getDeclaredField(BARCODE_FIELD));
					break;
					
				case UNIT_COLUMN:
					unitColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Product.class.getDeclaredField(UNIT_FIELD));
					break;
					
				case MANUFACTURER_COLUMN:
					manufacturerColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Product.class.getDeclaredField(MANUFACTURER_FIELD));
					break;
					
				case BRAND_COLUMN:
					brandColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Product.class.getDeclaredField(BRAND_FIELD));
					break;
					
				case COUNTRY_OF_ORIGIN_COLUMN:
					countryOfOriginColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, Product.class.getDeclaredField(COUNTRY_OF_ORIGIN_FIELD));
					break;
					
				case BASE_PRICE_COLUMN:
					basePriceColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, ChainProduct.class.getDeclaredField(BASE_PRICE_FIELD));
					break;
					
				case DISCOUNT_PRICE_COLUMN:
					discountPriceColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, ChainProduct.class.getDeclaredField(DISCOUNT_PRICE_FIELD));
					break;
					
				case START_DATE_COLUMN:
					startDateColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, ChainProduct.class.getDeclaredField(START_DATE_FIELD));
					break;
					
				case END_DATE_COLUMN:
					endDateColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, ChainProduct.class.getDeclaredField(END_DATE_FIELD));
					break;
					
				case TYPE_COLUMN:
					typeColumnIndex = columnIndex;
					logger.info(SUCCESS_MAPPING_MESSAGE, cellValue, columnIndex, ChainProduct.class.getDeclaredField(TYPE_FIELD));
					break;
					
				default:
					logger.warn("Column '{}' with index '{}' hasn't been mapped on any field of entities '{}' or '{}'.",
							cellValue, columnIndex, Product.class.getName(), ChainProduct.class.getName());
					break;
			}
		} catch (Exception exception) {
			logger.error("Column '{}' with index '{}' hasn't been mapped on any field of entities '{}' or '{}' due to exception: {}.",
					cellValue, columnIndex, Product.class.getName(), ChainProduct.class.getName(), printStackTrace(exception));
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
	 * Reports whether {@code barcode column index} was defined.
	 * 
	 * @return {@code true} if {@code barcode column index} is not {@code null} or {@code false} otherwise
	 * @see #barcodeColumnIndex
	 */
	public boolean isBarcodeMapped() {
		return barcodeColumnIndex != null;
	}
	
	/**
	 * Reports whether {@code unit column index} was defined.
	 * 
	 * @return {@code true} if {@code unit column index} is not {@code null} or {@code false} otherwise
	 * @see #unitColumnIndex
	 */
	public boolean isUnitMapped() {
		return unitColumnIndex != null;
	}
	
	/**
	 * Reports whether {@code manufacturer column index} was defined.
	 * 
	 * @return {@code true} if {@code manufacturer column index} is not {@code null} or 
	 * {@code false} otherwise
	 * @see #manufacturerColumnIndex
	 */
	public boolean isManufacturerMapped() {
		return manufacturerColumnIndex != null;
	}
	
	/**
	 * Reports whether {@code brand column index} was defined.
	 * 
	 * @return {@code true} if {@code brand column index} is not {@code null} or {@code false} 
	 * otherwise
	 * @see #brandColumnIndex
	 */
	public boolean isBrandMapped() {
		return brandColumnIndex != null;
	}
	
	/**
	 * Reports whether {@code country of origin column index} was defined.
	 * 
	 * @return {@code true} if {@code country of origin column index} is not {@code null} or 
	 * {@code false} otherwise
	 * @see #countryOfOriginColumnIndex
	 */
	public boolean isCountryOfOriginMapped() {
		return countryOfOriginColumnIndex != null;
	}
	
	/**
	 * Reports whether {@code base price column index} was defined.
	 * 
	 * @return {@code true} if {@code base price column index} is not {@code null} or {@code false} 
	 * otherwise
	 * @see #basePriceColumnIndex
	 */
	public boolean isBasePriceMapped() {
		return basePriceColumnIndex != null;
	}
	
	/**
	 * Reports whether {@code discount price column index} was defined.
	 * 
	 * @return {@code true} if {@code discount price column index} is not {@code null} or {@code false} 
	 * otherwise
	 * @see #discountPriceColumnIndex
	 */
	public boolean isDiscountPriceMapped() {
		return discountPriceColumnIndex != null;
	}
	
	/**
	 * Reports whether {@code start date column index} was defined.
	 * 
	 * @return {@code true} if {@code start date column index} is not {@code null} or {@code false} 
	 * otherwise
	 * @see #startDatePriceColumnIndex
	 */
	public boolean isStartDateMapped() {
		return startDateColumnIndex != null;
	}
	
	/**
	 * Reports whether {@code end date column index} was defined.
	 * 
	 * @return {@code true} if {@code end date column index} is not {@code null} or {@code false} 
	 * otherwise
	 * @see #endDatePriceColumnIndex
	 */
	public boolean isEndDateMapped() {
		return endDateColumnIndex != null;
	}
	
	/**
	 * Reports whether {@code type column index} was defined.
	 * 
	 * @return {@code true} if {@code type column index} is not {@code null} or {@code false} 
	 * otherwise
	 * @see #typePriceColumnIndex
	 */
	public boolean isTypeMapped() {
		return typeColumnIndex != null;
	}
	
	/**
	 * Returns a detail report of the completed <i>mapping process</i> in a readable format.
	 * The report includes collations for each pair of mapped column and field (property) of the 
	 * {@code Product and Chain Product} classes.
	 * <p>
	 * If exception occurs during getting the mapping report then appropriate message containing 
	 * detailed description of the {@code Throwable} is written into the log.
	 * 
	 * @return the string containing report of the mapping process
	 * @see naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper#RESULT_HEADER_MESSAGE
	 * @see naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper#RESULT_MAPPING_MESSAGE
	 * @see naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper#printStackTrace(Exception)
	 */
	public String toString() {
		try(Formatter formatter = new Formatter()) {
			formatter.format("Columns mapping on fields of entities '%s' and '%s':", Product.class.getName(), ChainProduct.class.getName());
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, Product.class.getDeclaredField(NAME_FIELD), nameColumnIndex, NAME_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, Product.class.getDeclaredField(BARCODE_FIELD), barcodeColumnIndex, BARCODE_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, Product.class.getDeclaredField(UNIT_FIELD), unitColumnIndex, UNIT_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, Product.class.getDeclaredField(MANUFACTURER_FIELD), manufacturerColumnIndex, MANUFACTURER_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, Product.class.getDeclaredField(BRAND_FIELD), brandColumnIndex, BRAND_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, Product.class.getDeclaredField(COUNTRY_OF_ORIGIN_FIELD), countryOfOriginColumnIndex, COUNTRY_OF_ORIGIN_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, ChainProduct.class.getDeclaredField(BASE_PRICE_FIELD), basePriceColumnIndex, BASE_PRICE_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, ChainProduct.class.getDeclaredField(DISCOUNT_PRICE_FIELD), discountPriceColumnIndex, DISCOUNT_PRICE_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, ChainProduct.class.getDeclaredField(START_DATE_FIELD), startDateColumnIndex, START_DATE_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, ChainProduct.class.getDeclaredField(END_DATE_FIELD), endDateColumnIndex, END_DATE_COLUMN, ';');
			formatter.format(System.lineSeparator());
			formatter.format(RESULT_MAPPING_MESSAGE, ChainProduct.class.getDeclaredField(TYPE_FIELD), typeColumnIndex, TYPE_COLUMN, '.');
			return formatter.toString();
		} catch (Exception exception) {
			logger.error("Exception has occurred during the process of getting mapping result for entities '{}' and '{}': {}.", 
					Product.class.getName(), ChainProduct.class.getName(), printStackTrace(exception));
			return "Exception has occurred during the process of getting mapping result for entities '" + Product.class.getName() + "' and '" + ChainProduct.class.getName() + "' (see logs above).";
		}
	}
}
