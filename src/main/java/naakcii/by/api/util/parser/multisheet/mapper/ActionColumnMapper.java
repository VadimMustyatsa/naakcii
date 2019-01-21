package naakcii.by.api.util.parser.multisheet.mapper;

import java.util.Formatter;

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
public class ActionColumnMapper implements ColumnMapper {

	private static final Logger logger = LogManager.getLogger(ActionColumnMapper.class);
	private final static String PRODUCT_NAME_FIELD = "name";
	private final static String PRODUCT_BARCODE_FIELD = "barcode";
	private final static String PRODUCT_UNIT_FIELD = "unit";
	private final static String BASE_PRICE_FIELD = "basePrice";
	private final static String DISCOUNT_PRICE_FIELD = "discountPrice";
	private final static String START_DATE_FIELD = "startDate";
	private final static String END_DATE_FIELD = "endDate";
	private final static String ACTION_TYPE_FIELD = "type";
	private final static String PRODUCT_NAME_COLUMN = "Наименование";
	private static final String PRODUCT_BARCODE_COLUMN = "Штрих-код";
	private static final String PRODUCT_UNIT_COLUMN = "Единица продажи";
	private final static String BASE_PRICE_COLUMN = "Цена до акции";
	private final static String DISCOUNT_PRICE_COLUMN = "Цена";
	private final static String START_DATE_COLUMN = "Дата начала действия акции";
	private final static String END_DATE_COLUMN = "Дата окончания действия акции";
	private final static String ACTION_TYPE_COLUMN = "Тип акции";
	
	private Integer productNameColumnIndex;
	private Integer productBarcodeColumnIndex;
	private Integer productUnitColumnIndex;
	private Integer basePriceColumnIndex;
	private Integer discountPriceColumnIndex;
	private Integer startDateColumnIndex;
	private Integer endDateColumnIndex;
	private Integer actionTypeColumnIndex;
	
	public void mapColumn(String cellValue, int columnIndex) {
		try {
			switch (cellValue) {
				case PRODUCT_NAME_COLUMN:
					productNameColumnIndex = columnIndex;
					logger.info(columnMappingMessage, cellValue, columnIndex, Product.class.getDeclaredField(PRODUCT_NAME_FIELD));
					break;
					
				case PRODUCT_BARCODE_COLUMN:
					productBarcodeColumnIndex = columnIndex;
					logger.info(columnMappingMessage, cellValue, columnIndex, Product.class.getDeclaredField(PRODUCT_BARCODE_FIELD));
					break;
					
				case PRODUCT_UNIT_COLUMN:
					productUnitColumnIndex = columnIndex;
					logger.info(columnMappingMessage, cellValue, columnIndex, Product.class.getDeclaredField(PRODUCT_UNIT_FIELD));
					break;
			
				case BASE_PRICE_COLUMN:
					basePriceColumnIndex = columnIndex;
					logger.info(columnMappingMessage, cellValue, columnIndex, Action.class.getDeclaredField(BASE_PRICE_FIELD));
					break;
					
				case DISCOUNT_PRICE_COLUMN:
					discountPriceColumnIndex = columnIndex;
					logger.info(columnMappingMessage, cellValue, columnIndex, Action.class.getDeclaredField(DISCOUNT_PRICE_FIELD));
					break;
					
				case START_DATE_COLUMN:
					startDateColumnIndex = columnIndex;
					logger.info(columnMappingMessage, cellValue, columnIndex, Action.class.getDeclaredField(START_DATE_FIELD));
					break;
					
				case END_DATE_COLUMN:
					endDateColumnIndex = columnIndex;
					logger.info(columnMappingMessage, cellValue, columnIndex, Action.class.getDeclaredField(END_DATE_FIELD));
					break;
					
				case ACTION_TYPE_COLUMN:
					actionTypeColumnIndex = columnIndex;
					logger.info(columnMappingMessage, cellValue, columnIndex, Action.class.getDeclaredField(ACTION_TYPE_FIELD));
					break;
					
				default:
					logger.warn("Column '{}' with index '{}' hasn't been mapped on any field of entity '{}'.",
							cellValue, columnIndex, Action.class);
					break;
			}
		} catch (Exception exception) {
			logger.error("Column '{}' with index '{}' hasn't been mapped on any field of entity '{}' due to exception: {}.",
					cellValue, columnIndex, Action.class, printStackTrace(exception));
		}	
	}
	
	public boolean isProductNameMapped() {
		return productNameColumnIndex != null;
	}
	
	public boolean isProductBarcodeMapped() {
		return productBarcodeColumnIndex != null;
	}
	
	public boolean isProductUnitMapped() {
		return productUnitColumnIndex != null;
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
	
	public boolean isActionTypeMapped() {
		return actionTypeColumnIndex != null;
	}
	
	public String toString() {
		try(Formatter formatter = new Formatter()) {
			formatter.format("Columns mapping on fields of entity '%s':", Action.class.getName());
			formatter.format("%n");
			formatter.format(resultColumnMappingMessage, Product.class.getDeclaredField(PRODUCT_NAME_FIELD), productNameColumnIndex, PRODUCT_NAME_COLUMN, ';');
			formatter.format("%n");
			formatter.format(resultColumnMappingMessage, Product.class.getDeclaredField(PRODUCT_BARCODE_FIELD), productBarcodeColumnIndex, PRODUCT_BARCODE_COLUMN, ';');
			formatter.format("%n");
			formatter.format(resultColumnMappingMessage, Product.class.getDeclaredField(PRODUCT_UNIT_FIELD), productUnitColumnIndex, PRODUCT_UNIT_COLUMN, ';');
			formatter.format("%n");
			formatter.format(resultColumnMappingMessage, Action.class.getDeclaredField(BASE_PRICE_FIELD), basePriceColumnIndex, BASE_PRICE_COLUMN, ';');
			formatter.format("%n");
			formatter.format(resultColumnMappingMessage, Action.class.getDeclaredField(DISCOUNT_PRICE_FIELD), discountPriceColumnIndex, DISCOUNT_PRICE_COLUMN, ';');
			formatter.format("%n");
			formatter.format(resultColumnMappingMessage, Action.class.getDeclaredField(START_DATE_FIELD), startDateColumnIndex, START_DATE_COLUMN, ';');
			formatter.format("%n");
			formatter.format(resultColumnMappingMessage, Action.class.getDeclaredField(END_DATE_FIELD), endDateColumnIndex, END_DATE_COLUMN, ';');
			formatter.format("%n");
			formatter.format(resultColumnMappingMessage, Action.class.getDeclaredField(ACTION_TYPE_FIELD), actionTypeColumnIndex, ACTION_TYPE_COLUMN, '.');
			return formatter.toString();
		} catch (Exception exception) {
			logger.error("Exception has occurred during the process of getting mapping result for entity '{}': {}.", 
					Action.class, printStackTrace(exception));
			return "Exception has occurred during the process of getting mapping result for entity '" + Action.class + "' (see logs).";
		}
	}
}
