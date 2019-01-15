package naakcii.by.api.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Formatter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import naakcii.by.api.actiontype.ActionType;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.product.Product;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "ACTION")
@org.hibernate.annotations.Immutable
public class Action implements Serializable {

	private static final long serialVersionUID = 1525810593299011676L;

    @Embeddable
    public static class Id implements Serializable {

        private static final long serialVersionUID = 6978249943191744201L;

        @Column(name = "PRODUCT_ID")
        private Long productId;

        @Column(name = "CHAIN_ID")
        private Long chainId;

        public Id() {

        }

        public Id(Long productId, Long chainId) {
            this.productId = productId;
            this.chainId = chainId;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.productId.equals(that.productId) && this.chainId.equals(that.chainId);
            }
            
            return false;
        }

        public int hashCode() {
            return productId.hashCode() + chainId.hashCode();
        }

        public Long getChainId() {
            return chainId;
        }
        
        public Long getProductId() {
            return productId;
        }

    }

    @EmbeddedId
    private Id id = new Id();

    @Column(name = "ACTION_BASE_PRICE")
    @Digits(
    	integer = 2, 
    	fraction = 2,
    	message = "Base price of the action product '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @DecimalMax(
    	value = "25", 
    	inclusive = true,
    	message = "Base price of the action product '${validatedValue}' must be lower than '{value}'."
    )
    @DecimalMin(
       	value = "0.20", 
       	inclusive = true,
       	message = "Base price of the action product '${validatedValue}' must be higher than '{value}'."
    )
    private BigDecimal basePrice;

    @Column(name = "ACTION_DISCOUNT_PRICE")
    @NotNull(message = "Discount price of the action product mustn't be null.")
    @Digits(
    	integer = 2, 
    	fraction = 2,
    	message = "Discount price of the action product '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @DecimalMax(
        value = "25", 
        inclusive = true,
        message = "Discount price of the action product '${validatedValue}' must be lower than '{value}'."
    )
    @DecimalMin(
      	value = "0.20", 
       	inclusive = true,
       	message = "Discount price of the action product '${validatedValue}' must be higher than '{value}'."
    )
    private BigDecimal discountPrice;
    
    @Column(name = "ACTION_DISCOUNT_PERCENT")
    @Digits(
    	integer = 2, 
    	fraction = 0,
    	message = "Discount percent of the action product '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @DecimalMax(
        value = "50", 
        inclusive = true,
        message = "Discount percent of the action product '${validatedValue}' must be lower than '{value}'."
    )
    @PositiveOrZero(message = "Discount percent of the action product '${validatedValue}' mustn't be negative.")
    private BigDecimal discountPercent;

    @Column(name = "ACTION_START_DATE")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Action must have have start date.")
    private Calendar startDate;

    @Column(name = "ACTION_END_DATE")
    @Temporal(TemporalType.DATE)
    @Future(message = "End date of the action must be in the future.")
    @NotNull(message = "Action must have end date.")
    private Calendar endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "PRODUCT_ID",
        updatable = false,
        insertable = false
    )
    @NotNull(message = "Action must have product.")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "CHAIN_ID",
        updatable = false,
        insertable = false
    )
    @NotNull(message = "Action must have chain.")
    private Chain chain;

    @ManyToOne
    @JoinColumn(name = "ACTION_TYPE_ID")
    @NotNull(message = "Action must have type.")
    @Valid
    private ActionType type;
    
    public Action(Product product, Chain chain, BigDecimal discountPrice, ActionType type, Calendar startDate, Calendar endDate) {
        this.product = product;
        this.chain = chain;
        this.discountPrice = discountPrice;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.id.productId = product.getId();
        this.id.chainId = chain.getId();
        product.getActions().add(this);
        chain.getActions().add(this);
    }
    
    public Action(Product product, Chain chain, BigDecimal basePrice, BigDecimal discountPrice, BigDecimal discountPercent, ActionType type, Calendar startDate, Calendar endDate) {
        this.product = product;
        this.chain = chain;
        this.basePrice = basePrice;
        this.discountPrice = discountPrice;
        this.discountPercent = discountPercent;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.id.productId = product.getId();
        this.id.chainId = chain.getId();
        product.getActions().add(this);
        chain.getActions().add(this);
    }
    
    public void setProduct(Product product) {
    	this.product = product;
    	this.id.productId = product.getId();
    }
    
    public void setChain(Chain chain) {
    	this.chain = chain;
    	this.id.chainId = chain.getId();
    }
    
    public String toString() {
    	StringBuilder result = new StringBuilder("Instance of " + Action.class + ":");
		result.append(System.lineSeparator());
		result.append("\t").append("product id/name - " + (product == null ? null + "/" + null : id.productId + "/" + product.getName()) + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("chain id/name - " + (chain == null ? null + "/" + null : id.chainId + "/" + chain.getName()) + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("base price - " + basePrice + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("discount price - " + discountPrice + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("discount percent - " + discountPercent + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("start date - " + startDate == null ? null : getFormattedDate(startDate) + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("end date - " + endDate == null ? null : getFormattedDate(endDate) + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("action type id/name - " + (type == null ? null + "/" + null : type.getId() + "/" + type.getName()) + ".");
		return result.toString();
    }
    
    private String getFormattedDate(Calendar date) {
    	Formatter formatter = new Formatter();
    	String formattedDate = formatter.format("%td-%tm-%tY", date, date, date).toString();
    	formatter.close();
    	return formattedDate;
    }
}
