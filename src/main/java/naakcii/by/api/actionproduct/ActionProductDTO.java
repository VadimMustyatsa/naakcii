package naakcii.by.api.actionproduct;

import java.math.BigDecimal;
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
public class ActionProductDTO {
		
	private Long productId;
    private Long chainId;
    private String name;
    private String unit;
    private String manufacturer;
    private String brand;
    private String countryOfOrigin;
    private String picture;
    private BigDecimal basePrice;
    private BigDecimal discountPercent;
    private BigDecimal discountPrice;
    private Long startDate;
    private Long endDate;
    private ActionTypeDTO actionType;
    
    public ActionProductDTO(Action action) {
    	this.productId = action.getId().getProductId();
    	this.chainId = action.getId().getProductId();
    	this.name = action.getProduct().getName();
    	this.unit = action.getProduct().getUnit().getRepresentation();
    	this.manufacturer = action.getProduct().getManufacturer();
    	this.brand = action.getProduct().getBrand();
    	this.countryOfOrigin = action.getProduct().getCountryOfOrigin().getName();
    	this.picture = action.getProduct().getPicture();
    	this.basePrice = action.getBasePrice();
    	this.discountPercent = action.getDiscountPercent();
    	this.discountPrice = action.getDiscountPrice();
    	this.startDate = action.getStartDate().getTimeInMillis();
    	this.endDate = action.getEndDate().getTimeInMillis();
    	this.actionType = new ActionTypeDTO(action.getType());
    }
    
    private String getFormattedDate(Calendar date) {
    	Formatter formatter = new Formatter();
    	String formattedDate = formatter.format("%td-%tm-%tY", date, date, date).toString();
    	formatter.close();
    	return formattedDate;
    }
}
