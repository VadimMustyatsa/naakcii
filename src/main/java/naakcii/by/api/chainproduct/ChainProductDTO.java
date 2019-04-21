package naakcii.by.api.chainproduct;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Optional;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.chainproducttype.ChainProductTypeDTO;
import naakcii.by.api.country.Country;
import naakcii.by.api.product.Product;
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
    	this.chainId = chainProduct.getId().getChainId();
    	this.chainName = chainProduct.getOptionalChain().map(Chain::getName).orElse(null);
    	Optional<Product> optionalProduct = chainProduct.getOptionalProduct();
    	
    	if (optionalProduct.isPresent()) {
    		this.name = optionalProduct.get().getName();
    		this.unitOfMeasure = optionalProduct.flatMap(Product::getOptionalUnitOfMeasure).map(UnitOfMeasureDTO::new).orElse(null);
    		this.manufacturer = optionalProduct.get().getManufacturer();
    		this.brand = optionalProduct.get().getBrand();
    		this.countryOfOrigin = optionalProduct.flatMap(Product::getOptionalCountry).map(Country::getName).orElse(null);
    		this.picture = optionalProduct.get().getPicture();
    	}
    	
    	this.basePrice = chainProduct.getBasePrice();
    	this.discountPercent = chainProduct.getDiscountPercent();
    	this.discountPrice = chainProduct.getDiscountPrice();
    	this.startDate = chainProduct.getOptionalStartDate().map(Calendar::getTimeInMillis).orElse(null);
    	this.endDate = chainProduct.getOptionalEndDate().map(Calendar::getTimeInMillis).orElse(null);
    	this.chainProductType = chainProduct.getOptionalType().map(ChainProductTypeDTO::new).orElse(null);
    }
    
    private String getFormattedDate(Calendar date) {
    	Formatter formatter = new Formatter();
    	String formattedDate = formatter.format("%td-%tm-%tY", date, date, date).toString();
    	formatter.close();
    	return formattedDate;
    }
}
