package naakcii.by.api.chainproduct;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Formatter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.chainproducttype.ChainProductTypeDTO;
import naakcii.by.api.unitofmeasure.UnitOfMeasureDTO;

@NoArgsConstructor
@Setter
@Getter
public class ChainProductDTO {

	@ApiModelProperty(notes = "Id товара",example="5L")
	private Long productId;
	@ApiModelProperty(notes = "Id торговой сети",required=true,example="1L")
    private Long chainId;
	@ApiModelProperty(notes = "Название торговой сети",example="Алми")
    private String chainName;
	@ApiModelProperty(notes = "Наименование товара",example="Молоко")
    private String name;
	@ApiModelProperty(notes = "Единица измерения товара",example="Кг")
    private UnitOfMeasureDTO unitOfMeasure;
	@ApiModelProperty(notes = "Производитель товара",example="Бабушкина крынка")
    private String manufacturer;
	@ApiModelProperty(notes = "Торговая марка товара",example="Бабушкина крынка")
    private String brand;
	@ApiModelProperty(notes = "Страна происхождения товара",example="Беларусь")
    private String countryOfOrigin;
	@ApiModelProperty(notes = "Путь к изображению товара",example="http://pathtoimage/image.jpg")
    private String picture;
	@ApiModelProperty(notes = "Базовая цена товара",example="20")
	private BigDecimal basePrice;
	@ApiModelProperty(notes = "Процент скидки",example="10")
	private BigDecimal discountPercent;
	@ApiModelProperty(notes = "Цена со скидкой",example="18")
	private BigDecimal discountPrice;
	@ApiModelProperty(notes = "Дата начала акции",example="1414602645000")
	private Long startDate;
	@ApiModelProperty(notes = "Дата окончания акции",example="1425716845000")
	private Long endDate;
	@ApiModelProperty(notes = "Тип товара торговой сети")
    private ChainProductTypeDTO chainProductType;
    
    public ChainProductDTO(ChainProduct chainProduct) {
    	this.productId = chainProduct.getId().getProductId();
    	this.chainId = chainProduct.getId().getProductId();
    	this.chainName = chainProduct.getChain().getName();
    	this.name = chainProduct.getProduct().getName();
    	this.unitOfMeasure = new UnitOfMeasureDTO(chainProduct.getProduct().getUnitOfMeasure());
    	this.manufacturer = chainProduct.getProduct().getManufacturer();
    	this.brand = chainProduct.getProduct().getBrand();
    	this.countryOfOrigin = chainProduct.getProduct().getCountryOfOrigin().getName();
    	this.picture = chainProduct.getProduct().getPicture();
    	this.basePrice = chainProduct.getBasePrice();
    	this.discountPercent = chainProduct.getDiscountPercent();
    	this.discountPrice = chainProduct.getDiscountPrice();
    	this.startDate = chainProduct.getStartDate().getTimeInMillis();
    	this.endDate = chainProduct.getEndDate().getTimeInMillis();
    	this.chainProductType = new ChainProductTypeDTO(chainProduct.getType());
    }
    
    private String getFormattedDate(Calendar date) {
    	Formatter formatter = new Formatter();
    	String formattedDate = formatter.format("%td-%tm-%tY", date, date, date).toString();
    	formatter.close();
    	return formattedDate;
    }
}
