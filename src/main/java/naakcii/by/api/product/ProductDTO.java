package naakcii.by.api.product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Formatter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import naakcii.by.api.action.Action;
import naakcii.by.api.actiontype.ActionTypeDTO;

@NoArgsConstructor
@Setter
@Getter
public class ProductDTO {
		
	private Long productId;
    private Long chainId;
    private String name;
    private BigDecimal quantity;
    private String measure;
    private String manufacturer;
    private String countryOfOrigin;
    private String picture;
    private BigDecimal price;
    private BigDecimal discount;
    private BigDecimal discountPrice;
    private String startDate;
    private String endDate;
    private ActionTypeDTO actionType;
    
    public ProductDTO(Action action) {
    	this.productId = action.getId().getProductId();
    	this.chainId = action.getId().getProductId();
    	this.name = action.getProduct().getName();
    	this.quantity = action.getProduct().getQuantity().setScale(1, RoundingMode.HALF_DOWN);
    	this.measure = action.getProduct().getMeasure();
    	this.manufacturer = action.getProduct().getManufacturer();
    	this.countryOfOrigin = action.getProduct().getCountryOfOrigin();
    	this.picture = action.getProduct().getPicture();
    	this.price = action.getPrice();
    	this.discount = action.getDiscount();
    	this.discountPrice = action.getDiscountPrice();
    	this.startDate = getFormattedDate(action.getStartDate());
    	this.endDate = getFormattedDate(action.getEndDate());;
    	this.actionType = new ActionTypeDTO(action.getType());
    }
    
    private String getFormattedDate(Calendar date) {
    	Formatter formatter = new Formatter();
    	String formattedDate = formatter.format("%td-%tm-%tY", date, date, date).toString();
    	formatter.close();
    	return formattedDate;
    }
}
