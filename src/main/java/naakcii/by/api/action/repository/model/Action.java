package naakcii.by.api.action.repository.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import naakcii.by.api.chain.repository.model.Chain;
import naakcii.by.api.product.repository.model.Product;

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

    }

    @EmbeddedId
    private Id id = new Id();

    @Column(name = "ACTION_PRICE")
    @Digits(
    	integer = 2, 
    	fraction = 2,
    	message = "Price of the action product '${validatedValue}' must have up to {integer} integer digits and {fraction} fraction digits."
    )
    @DecimalMax(
    	value = "25", 
    	inclusive = true,
    	message = "Price of the action product '${validatedValue}' must be lower than {value}"
    )
    @DecimalMin(
       	value = "0.20", 
       	inclusive = true,
       	message = "Price of the action product '${validatedValue}' must be higher than {value}"
    )
    private BigDecimal price;

    @Column(name = "ACTION_DISCOUNT")
    @Digits(
    	integer = 2, 
    	fraction = 0,
    	message = "Discount of the action product '${validatedValue}' must have up to {integer} integer digits and {fraction} fraction digits."
    )
    @DecimalMax(
        value = "50", 
        inclusive = true,
        message = "Discount of the action product '${validatedValue}' must be lower than {value}."
    )
    @PositiveOrZero(message = "Discount of the action product '${validatedValue}' mustn't be negative.")
    private BigDecimal discount;

    @Column(name = "ACTION_DISCOUNT_PRICE")
    @NotNull(message = "Discount price of the action product mustn't be null.")
    @Digits(
    	integer = 2, 
    	fraction = 2,
    	message = "Discount price of the action product '${validatedValue}' must have up to {integer} integer digits and {fraction} fraction digits."
    )
    @DecimalMax(
        value = "25", 
        inclusive = true,
        message = "Discount price of the action product '${validatedValue}' must be lower than {value}"
    )
    @DecimalMin(
      	value = "0.20", 
       	inclusive = true,
       	message = "Discount price of the action product '${validatedValue}' must be higher than {value}"
    )
    private BigDecimal discountPrice;

    @Column(name = "ACTION_START_DATE")
    @Temporal(TemporalType.DATE)
    private Calendar startDate;

    @Column(name = "ACTION_END_DATE")
    @Temporal(TemporalType.DATE)
    @Future(message = "End date of the action must be in the future.")
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

    @Column(name = "ACTION_TYPE")
    @NotNull(message = "Action must have type.")
    private String type;
    
    public Action(Product product, Chain chain, BigDecimal discountPrice, String type) {
        this.product = product;
        this.chain = chain;
        this.discountPrice = discountPrice;
        this.type = type;
        this.id.productId = product.getId();
        this.id.chainId = chain.getId();
        product.getActions().add(this);
        chain.getActions().add(this);
    }
}
