package naakcii.by.api.chainproduct;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.chainproducttype.ChainProductType;
import naakcii.by.api.product.Product;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Formatter;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "CHAIN_PRODUCT")
@org.hibernate.annotations.Immutable
public class ChainProduct implements Serializable {

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

    @Column(name = "CHAIN_PRODUCT_BASE_PRICE")
    @Digits(
    	integer = 2, 
    	fraction = 2,
    	message = "ChainProduct's base price '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @DecimalMax(
    	value = "75", 
    	inclusive = true,
    	message = "ChainProduct's base price '${validatedValue}' must be lower than '{value}'."
    )
    @DecimalMin(
       	value = "0.20", 
       	inclusive = true,
       	message = "ChainProduct's base price '${validatedValue}' must be higher than '{value}'."
    )
    private BigDecimal basePrice;

    @Column(name = "CHAIN_PRODUCT_DISCOUNT_PRICE")
    @NotNull(message = "ChainProduct's discount price mustn't be null.")
    @Digits(
    	integer = 2, 
    	fraction = 2,
    	message = "ChainProduct's discount price '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @DecimalMax(
        value = "50", 
        inclusive = true,
        message = "ChainProduct's discount price '${validatedValue}' must be lower than '{value}'."
    )
    @DecimalMin(
      	value = "0.20", 
       	inclusive = true,
       	message = "ChainProduct's discount price '${validatedValue}' must be higher than '{value}'."
    )
    private BigDecimal discountPrice;
    
    @Column(name = "CHAIN_PRODUCT_DISCOUNT_PERCENT")
    @Digits(
    	integer = 2, 
    	fraction = 0,
    	message = "ChainProduct's discount percent '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @DecimalMax(
        value = "50", 
        inclusive = true,
        message = "ChainProduct's discount percent '${validatedValue}' must be lower than '{value}'."
    )
    @PositiveOrZero(message = "ChainProduct's discount percent '${validatedValue}' mustn't be negative.")
    private BigDecimal discountPercent;

    @Column(name = "CHAIN_PRODUCT_START_DATE")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "ChainProduct must have have start date.")
    private Calendar startDate;

    @Column(name = "CHAIN_PRODUCT_END_DATE")
    @Temporal(TemporalType.DATE)
    @Future(message = "ChainProduct's end date must be in the future.")
    @NotNull(message = "ChainProduct must have end date.")
    private Calendar endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "PRODUCT_ID",
        updatable = false,
        insertable = false
    )
    @NotNull(message = "ChainProduct must have product.")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "CHAIN_ID",
        updatable = false,
        insertable = false
    )
    @NotNull(message = "ChainProduct must have chain.")
    private Chain chain;

    @ManyToOne
    @JoinColumn(name = "CHAIN_PRODUCT_TYPE_ID")
    @NotNull(message = "ChainProduct must have type.")
    @Valid
    private ChainProductType type;
    
    public ChainProduct(Product product, Chain chain, BigDecimal discountPrice, ChainProductType type, Calendar startDate, Calendar endDate) {
        this.product = product;
        this.chain = chain;
        this.discountPrice = discountPrice;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.id.productId = product.getId();
        this.id.chainId = chain.getId();
        product.getChainProducts().add(this);
        chain.getChainProducts().add(this);
    }
    
    public ChainProduct(Product product, Chain chain, BigDecimal basePrice, BigDecimal discountPrice, BigDecimal discountPercent, ChainProductType type, Calendar startDate, Calendar endDate) {
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
        product.getChainProducts().add(this);
        chain.getChainProducts().add(this);
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
    	StringBuilder result = new StringBuilder("Instance of " + ChainProduct.class + ":");
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
		result.append("\t").append("type id/name - " + (type == null ? null + "/" + null : type.getId() + "/" + type.getName()) + ".");
		return result.toString();
    }
    
    private String getFormattedDate(Calendar date) {
    	Formatter formatter = new Formatter();
    	String formattedDate = formatter.format("%td-%tm-%tY", date, date, date).toString();
    	formatter.close();
    	return formattedDate;
    }
}
