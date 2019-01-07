package naakcii.by.api.util.parser.mapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.action.Action;
import naakcii.by.api.product.Product;

@NoArgsConstructor
@Setter
@Getter
public class ActionProductColumnMapper implements ColumnMapper {
	
	private static final Logger logger = LogManager.getLogger(ActionProductColumnMapper.class);
	private final static String NAME_FIELD = "name";
	private final static String BARCODE_FIELD = "barcode";
	private final static String UNIT_FIELD = "unit";
	private final static String MANUFACTURER_FIELD = "manufacturer";
	private final static String BRAND_FIELD = "brand";
	private final static String COUNTRY_OF_ORIGIN_FIELD = "countryOfOrigin";
	private final static String BASE_PRICE_FIELD = "basePrice";
	private final static String DISCOUNT_PRICE_FIELD = "discountPrice";
	private final static String START_DATE_FIELD = "startDate";
	private final static String END_DATE_FIELD = "endDate";
	private static final String NAME_COLUMN = "Наименование";
	private static final String BARCODE_COLUMN = "Штрих-код";
	private static final String UNIT_COLUMN = "Единица продажи";
	private static final String MANUFACTURER_COLUMN = "Производитель";
	private static final String BRAND_COLUMN = "Бренд";
	private static final String COUNTRY_OF_ORIGIN_COLUMN = "Код страны";
	private final static String BASE_PRICE_COLUMN = "Цена до акции";
	private final static String DISCOUNT_PRICE_COLUMN = "Цена";
	private final static String START_DATE_COLUMN = "Дата начала действия акции";
	private final static String END_DATE_COLUMN = "Дата окончания действия акции";
	
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
	
	public void mapColumn(String cellValue, int columnIndex) {
		try {
			switch (cellValue) {
				case NAME_COLUMN:
					nameColumnIndex = columnIndex;
					logger.info("Column '{}' with index '{}' was mapped on field '{}'.",
							cellValue, columnIndex, Product.class.getDeclaredField(NAME_FIELD));
					break;
				
				case BARCODE_COLUMN:
					barcodeColumnIndex = columnIndex;
					logger.info("Column '{}' with index '{}' was mapped on field '{}'.",
							cellValue, columnIndex, Product.class.getDeclaredField(BARCODE_FIELD));
					break;
					
				case UNIT_COLUMN:
					unitColumnIndex = columnIndex;
					logger.info("Column '{}' with index '{}' was mapped on field '{}'.",
							cellValue, columnIndex, Product.class.getDeclaredField(UNIT_FIELD));
					break;
					
				case MANUFACTURER_COLUMN:
					manufacturerColumnIndex = columnIndex;
					logger.info("Column '{}' with index '{}' was mapped on field '{}'.",
							cellValue, columnIndex, Product.class.getDeclaredField(MANUFACTURER_FIELD));
					break;
					
				case BRAND_COLUMN:
					brandColumnIndex = columnIndex;
					logger.info("Column '{}' with index '{}' was mapped on field '{}'.",
							cellValue, columnIndex, Product.class.getDeclaredField(BRAND_FIELD));
					break;
					
				case COUNTRY_OF_ORIGIN_COLUMN:
					countryOfOriginColumnIndex = columnIndex;
					logger.info("Column '{}' with index '{}' was mapped on field '{}'.",
							cellValue, columnIndex, Product.class.getDeclaredField(COUNTRY_OF_ORIGIN_FIELD));
					break;
					
				case BASE_PRICE_COLUMN:
					basePriceColumnIndex = columnIndex;
					logger.info("Column '{}' with index '{}' was mapped on field '{}'.",
							cellValue, columnIndex, Action.class.getDeclaredField(BASE_PRICE_FIELD));
					break;
					
				case DISCOUNT_PRICE_COLUMN:
					discountPriceColumnIndex = columnIndex;
					logger.info("Column '{}' with index '{}' was mapped on field '{}'.",
							cellValue, columnIndex, Action.class.getDeclaredField(DISCOUNT_PRICE_FIELD));
					break;
					
				case START_DATE_COLUMN:
					startDateColumnIndex = columnIndex;
					logger.info("Column '{}' with index '{}' was mapped on field '{}'.",
							cellValue, columnIndex, Action.class.getDeclaredField(START_DATE_FIELD));
					break;
					
				case END_DATE_COLUMN:
					endDateColumnIndex = columnIndex;
					logger.info("Column '{}' with index '{}' was mapped on field '{}'.",
							cellValue, columnIndex, Action.class.getDeclaredField(END_DATE_FIELD));
					break;
					
				default:
					logger.warn("Column '{}' with index '{}' wasn't mapped on any field of entities '{}' and '{}'.",
							cellValue, columnIndex, Product.class, Action.class);
					break;
			}
		} catch (Exception exception) {
			logger.error("Column '{}' with index '{}' wasn't mapped on any field of entities '{}' and '{}' due to exception: {}.",
					cellValue, columnIndex, Product.class, Action.class, exception);
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
	
	public String toString() {
		try {
			StringBuilder result = new StringBuilder("Header columns mapping on fields of entities '" + Product.class.getName() + "' and '" + Action.class.getName() + "':");
			result.append(System.lineSeparator());
			result.append("field " + Product.class.getDeclaredField(NAME_FIELD) + " - " + nameColumnIndex + "/" + NAME_COLUMN + ";");
			result.append(System.lineSeparator());
			result.append("field " + Product.class.getDeclaredField(BARCODE_FIELD) + " - " + barcodeColumnIndex + "/" + BARCODE_COLUMN + ";");
			result.append(System.lineSeparator());
			result.append("field " + Product.class.getDeclaredField(UNIT_FIELD) + " - " + unitColumnIndex + "/" + UNIT_COLUMN + ";");
			result.append(System.lineSeparator());
			result.append("field " + Product.class.getDeclaredField(MANUFACTURER_FIELD) + " - " + manufacturerColumnIndex + "/" + MANUFACTURER_COLUMN + ";");
			result.append(System.lineSeparator());
			result.append("field " + Product.class.getDeclaredField(BRAND_FIELD) + " - " + brandColumnIndex + "/" + BRAND_COLUMN + ";");
			result.append(System.lineSeparator());
			result.append("field " + Action.class.getDeclaredField(COUNTRY_OF_ORIGIN_FIELD) + " - " + countryOfOriginColumnIndex + "/" + COUNTRY_OF_ORIGIN_COLUMN + ".");
			result.append(System.lineSeparator());
			result.append("field " + Action.class.getDeclaredField(BASE_PRICE_FIELD) + " - " + basePriceColumnIndex + "/" + BASE_PRICE_COLUMN + ".");
			result.append(System.lineSeparator());
			result.append("field " + Action.class.getDeclaredField(DISCOUNT_PRICE_FIELD) + " - " + discountPriceColumnIndex + "/" + DISCOUNT_PRICE_COLUMN + ".");
			result.append(System.lineSeparator());
			result.append("field " + Action.class.getDeclaredField(START_DATE_FIELD) + " - " + startDateColumnIndex + "/" + START_DATE_COLUMN + ".");
			result.append(System.lineSeparator());
			result.append("field " + Action.class.getDeclaredField(END_DATE_FIELD) + " - " + endDateColumnIndex + "/" + END_DATE_COLUMN + ".");
			return result.toString();
		} catch (Exception exception) {
			logger.error("Exception has occurred during the process of header columns mapping on fields of entities '{}' and '{}': {}.", 
					Product.class.getName(), Action.class.getName(), exception);
			return "Header columns mapping on fields of entities '" + Product.class.getName() + "' and '" + Action.class.getName() + "' was finished with exception.";
		}
	}
}
