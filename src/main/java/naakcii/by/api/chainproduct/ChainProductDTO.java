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

    @ApiModelProperty(notes = "Идентификатор товара.", example = "5L")
    private Long productId;
    @ApiModelProperty(notes = "Идентификатор торговой сети.", required = true, example = "1L")
    private Long chainId;
    @ApiModelProperty(notes = "Название торговой сети.", example = "Алми")
    private String chainName;
    @ApiModelProperty(notes = "Наименование товара.", example = "Апельсины крупные")
    private String name;
    @ApiModelProperty(notes = "Единица измерения товара.", example = "0.1 кг")
    private UnitOfMeasureDTO unitOfMeasure;
    @ApiModelProperty(notes = "Производитель товара.", example = "Бабушкина крынка")
    private String manufacturer;
    @ApiModelProperty(notes = "Торговая марка товара.", example = "Бабушкина крынка")
    private String brand;
    @ApiModelProperty(notes = "Страна происхождения товара.", example = "Беларусь")
    private String countryOfOrigin;
    @ApiModelProperty(notes = "Путь к файлу, содержащему изображение товара.", example = "С:/products/pictures/oranges.jpg")
    private String picture;
    @ApiModelProperty(notes = "Первоначальная цена товара.", example = "20.75")
    private BigDecimal basePrice;
    @ApiModelProperty(notes = "Процент скидки.", example = "10")
    private BigDecimal discountPercent;
    @ApiModelProperty(notes = "Акционная цена товара.", example = "18.50")
    private BigDecimal discountPrice;
    @ApiModelProperty(notes = "Дата начала действия акционного предложения.", example = "10-01-2019")
    private Long startDate;
    @ApiModelProperty(notes = "Дата окончания действия акционного предложения.", example = "31-01-2019")
    private Long endDate;
    @ApiModelProperty(notes = "Тип акционного предложения.", example = "1+1. Два товара по цене одного.")
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
