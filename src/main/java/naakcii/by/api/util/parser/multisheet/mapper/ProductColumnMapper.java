package naakcii.by.api.util.parser.multisheet.mapper;

import java.util.Formatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.product.Product;

@NoArgsConstructor
@Setter
@Getter
public class ProductColumnMapper implements ColumnMapper {
	
	private static final Logger logger = LogManager.getLogger(ProductColumnMapper.class);
	private final static String NAME_FIELD = "name";
	private final static String BARCODE_FIELD = "barcode";
	private final static String UNIT_FIELD = "unit";
	private final static String MANUFACTURER_FIELD = "manufacturer";
	private final static String BRAND_FIELD = "brand";
	private final static String COUNTRY_OF_ORIGIN_FIELD = "countryOfOrigin";
	private static final String NAME_COLUMN = "Наименование";
	private static final String BARCODE_COLUMN = "Штрих-код";
	private static final String UNIT_COLUMN = "Единица продажи";
	private static final String MANUFACTURER_COLUMN = "Производитель";
	private static final String BRAND_COLUMN = "Бренд";
	private static final String COUNTRY_OF_ORIGIN_COLUMN = "Код страны";
	
	private Integer nameColumnIndex;
	private Integer barcodeColumnIndex;
	private Integer unitColumnIndex;
	private Integer manufacturerColumnIndex;
	private Integer brandColumnIndex;
	private Integer countryOfOriginColumnIndex;
	
	public void mapColumn(String cellValue, int columnIndex) {
		try {
			switch (cellValue) {
				case NAME_COLUMN:
					nameColumnIndex = columnIndex;
					logger.info(columnMappingMessage, cellValue, columnIndex, Product.class.getDeclaredField(NAME_FIELD));
					break;
				
				case BARCODE_COLUMN:
					barcodeColumnIndex = columnIndex;
					logger.info(columnMappingMessage, cellValue, columnIndex, Product.class.getDeclaredField(BARCODE_FIELD));
					break;
					
				case UNIT_COLUMN:
					unitColumnIndex = columnIndex;
					logger.info(columnMappingMessage, cellValue, columnIndex, Product.class.getDeclaredField(UNIT_FIELD));
					break;
					
				case MANUFACTURER_COLUMN:
					manufacturerColumnIndex = columnIndex;
					logger.info(columnMappingMessage, cellValue, columnIndex, Product.class.getDeclaredField(MANUFACTURER_FIELD));
					break;
					
				case BRAND_COLUMN:
					brandColumnIndex = columnIndex;
					logger.info(columnMappingMessage, cellValue, columnIndex, Product.class.getDeclaredField(BRAND_FIELD));
					break;
					
				case COUNTRY_OF_ORIGIN_COLUMN:
					countryOfOriginColumnIndex = columnIndex;
					logger.info(columnMappingMessage, cellValue, columnIndex, Product.class.getDeclaredField(COUNTRY_OF_ORIGIN_FIELD));
					break;
					
				default:
					logger.warn("Column '{}' with index '{}' hasn't been mapped on any field of entity '{}'.",
							cellValue, columnIndex, Product.class);
					break;
			}
		} catch (Exception exception) {
			logger.error("Column '{}' with index '{}' hasn't been mapped on any field of entity '{}' due to exception: {}.",
					cellValue, columnIndex, Product.class, printStackTrace(exception));
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
		try(Formatter formatter = new Formatter()) {
			formatter.format("Columns mapping on fields of entity '%s':", Product.class.getName());
			formatter.format("%n");
			formatter.format(resultColumnMappingMessage, Product.class.getDeclaredField(NAME_FIELD), nameColumnIndex, NAME_COLUMN, ';');
			formatter.format("%n");
			formatter.format(resultColumnMappingMessage, Product.class.getDeclaredField(BARCODE_FIELD), barcodeColumnIndex, BARCODE_COLUMN, ';');
			formatter.format("%n");
			formatter.format(resultColumnMappingMessage, Product.class.getDeclaredField(UNIT_FIELD), unitColumnIndex, UNIT_COLUMN, ';');
			formatter.format("%n");
			formatter.format(resultColumnMappingMessage, Product.class.getDeclaredField(MANUFACTURER_FIELD), manufacturerColumnIndex, MANUFACTURER_COLUMN, ';');
			formatter.format("%n");
			formatter.format(resultColumnMappingMessage, Product.class.getDeclaredField(BRAND_FIELD), brandColumnIndex, BRAND_COLUMN, ';');
			formatter.format("%n");
			formatter.format(resultColumnMappingMessage, Product.class.getDeclaredField(COUNTRY_OF_ORIGIN_FIELD), countryOfOriginColumnIndex, COUNTRY_OF_ORIGIN_COLUMN, '.');
			return formatter.toString();
		} catch (Exception exception) {
			logger.error("Exception has occurred during the process of getting mapping result for entity '{}': {}.", 
					Product.class, printStackTrace(exception));
			return "Exception has occurred during the process of getting mapping result for entity '" + Product.class + "' (see logs).";
		}
	}
}
