package naakcii.by.api.action;

import java.math.BigDecimal;
import java.util.Calendar;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.actiontype.ActionTypeDTO;

@NoArgsConstructor
@Setter
@Getter
public class ActionDTO {
	
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
    private Calendar startDate;
    private Calendar endDate;
    private ActionTypeDTO actionType;
    
    public ActionDTO(Action action) {
    	this.productId = action.getId().getProductId();
    	this.chainId = action.getId().getProductId();
    	this.name = action.getProduct().getName();
    	this.quantity = action.getProduct().getQuantity();
    	this.measure = action.getProduct().getMeasure();
    	this.manufacturer = action.getProduct().getManufacturer();
    	this.countryOfOrigin = action.getProduct().getCountryOfOrigin();
    	this.picture = action.getProduct().getPicture();
    	this.price = action.getPrice();
    	this.discount = action.getDiscount();
    	this.discountPrice = action.getDiscountPrice();
    	this.startDate = action.getStartDate();
    	this.endDate = action.getEndDate();
    	this.actionType = new ActionTypeDTO(action.getType());
    }
}
