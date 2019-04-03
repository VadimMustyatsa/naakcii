package naakcii.by.api.chainproduct;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.entity.AbstractDTOEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
public class ChainProductDTO extends AbstractDTOEntity {

	private ChainProduct.Id id;
    private String chainName;
    private String productName;
	private BigDecimal basePrice;
	private BigDecimal discountPercent;
	private BigDecimal discountPrice;
	private LocalDate startDate;
	private LocalDate endDate;
    private String chainProductTypeName;
    
    public ChainProductDTO(ChainProduct chainProduct) {
    	this.id = chainProduct.getId();
    	this.chainName = chainProduct.getChain().getName();
    	this.productName = chainProduct.getProduct().getName();
    	this.basePrice = chainProduct.getBasePrice();
    	this.discountPercent = chainProduct.getDiscountPercent();
    	this.discountPrice = chainProduct.getDiscountPrice();
    	this.startDate = chainProduct.getStartDate();
    	this.endDate = chainProduct.getEndDate();
    	this.chainProductTypeName = chainProduct.getType().getName();
    }
}
