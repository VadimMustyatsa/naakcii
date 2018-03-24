package by.naakcii.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "PRODUCT_INFO")
@NamedQueries({
	@NamedQuery(name = "ProductInfo.findAll", query = "select pi from ProductInfo pi"),
	@NamedQuery(name = "ProductInfo.findAllWithDetails", 
			query = "select pi from ProductInfo pi left join fetch pi.product p"),
	@NamedQuery(name = "ProductInfo.findById", 
		query = "select pi from ProductInfo pi where pi.id = :id"),
	@NamedQuery(name = "ProductInfo.findByIdWithDetails", 
	query = "select pi from ProductInfo pi left join fetch pi.product p where pi.id = :id")
})
public class ProductInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2357476801377668338L;

	@Id
	@GeneratedValue(generator = "infoKeyGenerator")
	@org.hibernate.annotations.GenericGenerator(
			name = "infoKeyGenerator", 
			strategy = "foreign", 
			parameters = 
			@org.hibernate.annotations.Parameter(name = "property", value = "product")
	)
	@Column(name = "PRODUCT_INFO_ID")
	private Long id;
	
	@Column(name = "DESCR")
	@NotNull
	private String description;
	
	@Column(name = "QUANTITY")
	@NotNull
	private double quantity;
	
	@Column(name = "MEASURE")
	@Size(min = 2, max = 45)
	@NotNull
	private String measure;
	
	@Column(name = "PRICE")
	@NotNull
	private double price;
	
	@Column(name = "DISCOUNT")
	private double discount;
	
	@Column(name = "DISCOUNT_PRICE")
	private double discountPrice;
	
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private Product product;
	
	public ProductInfo() {
		
	}
	
	public ProductInfo(String description, double quantity, String measure, double price) {
		this.description = description;
		this.quantity = quantity;
		this.measure = measure;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getMeasure() {
		return measure;
	}

	public void setMeasure(String measure) {
		this.measure = measure;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
}
