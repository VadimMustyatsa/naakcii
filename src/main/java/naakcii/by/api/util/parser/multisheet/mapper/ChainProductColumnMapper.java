package naakcii.by.api.util.parser.multisheet.mapper;

import java.util.Formatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.chainproduct.ChainProduct;
import naakcii.by.api.product.Product;

@NoArgsConstructor
@Setter
@Getter
public class ChainProductColumnMapper implements ColumnMapper {
	
	private static final Logger logger = LogManager.getLogger(ChainProductColumnMapper.class);
	private final static String NAME_FIELD = "name";
	private final static String BARCODE_FIELD = "barcode";
	private final static String UNIT_FIELD = "unitOfMeasure";
	private final static String MANUFACTURER_FIELD = "manufacturer";
	private final static String BRAND_FIELD = "brand";
	private final static String BASE_PRICE_FIELD = "basePrice";
	private final static String DISCOUNT_PRICE_FIELD = "discountPrice";
	private final static String START_DATE_FIELD = "startDate";
	private final static String END_DATE_FIELD = "endDate";
	private final static String TYPE_FIELD = "type";
	private final static String COUNTRY_OF_ORIGIN_FIELD = "countryOfOrigin";
	private static final String NAME_COLUMN = "Наименование";
	private static final String BARCODE_COLUMN = "Штрих-код";
	private static final String UNIT_COLUMN = "Единица продажи";
	private static final String MANUFACTURER_COLUMN = "Производитель";
	private static final String BRAND_COLUMN = "Бренд";
	private static final String COUNTRY_OF_ORIGIN_COLUMN = "КодСтраны";
	private final static String BASE_PRICE_COLUMN = "Цена до акции";
	private final static String DISCOUNT_PRICE_COLUMN = "Цена";
	private final static String START_DATE_COLUMN = "Дата действия «с»";
	private final static String END_DATE_COLUMN = "Дата действия «по»";
	private final static String TYPE_COLUMN = "ВидРеклМероприятия";
	
	private Integer nameColumnIndex;
	private Integer barcodeColumnIndex;
	private Integer unitColumnIndex;
	private Integer manufacturerColumnIndex;
	private Integer brandColumnIndex;
	private Integer countryOfOriginColumnIndex;
	private Integer basePriceColumnIndex;
	private Integer discountPriceColumnIndex;
	private Integer startDateColumnIndex;
	private Integer endDateColumnIndex;
	private Integer typeColumnIndex;
	
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
	
	public boolean isNameMapped() {
		return nameColumnIndex != null;
	}
	
	public boolean isBarcodeMapped() {
		return barcodeColumnIndex != null;
	}
	
	public boolean isUnitMapped() {
		return unitColumnIndex != null;
	}
	
	public boolean isManufacturerMapped() {
		return manufacturerColumnIndex != null;
	}
	
	public boolean isBrandMapped() {
		return brandColumnIndex != null;
	}
	
	public boolean isCountryOfOriginMapped() {
		return countryOfOriginColumnIndex != null;
	}
	
	public boolean isBasePriceMapped() {
		return basePriceColumnIndex != null;
	}
	
	public boolean isDiscountPriceMapped() {
		return discountPriceColumnIndex != null;
	}
	
	public boolean isStartDateMapped() {
		return startDateColumnIndex != null;
	}
	
	public boolean isEndDateMapped() {
		return endDateColumnIndex != null;
	}
	
	public boolean isTypeMapped() {
		return typeColumnIndex != null;
	}
	
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
