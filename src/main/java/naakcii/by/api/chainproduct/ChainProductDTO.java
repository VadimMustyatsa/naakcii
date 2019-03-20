package naakcii.by.api.chainproduct;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.chainproducttype.ChainProductTypeDTO;
import naakcii.by.api.unitofmeasure.UnitOfMeasureDTO;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Formatter;

@NoArgsConstructor
@Setter
@Getter
public class ChainProductDTO {

	private Long productId;
    private Long chainId;
    private String chainName;
    private String name;
    private UnitOfMeasureDTO unitOfMeasure;
    private String manufacturer;
    private String brand;
    private String countryOfOrigin;
    private String picture;
	private BigDecimal basePrice;
	private BigDecimal discountPercent;
	private BigDecimal discountPrice;
	private Long startDate;
	private Long endDate;
    private ChainProductTypeDTO chainProductType;
    
    public ChainProductDTO(ChainProduct chainProduct) {
    	this.productId = chainProduct.getId().getProductId();
    	this.chainId = chainProduct.getId().getChainId();
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
