package naakcii.by.api.repository.model;

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
	@GeneratedValue(generator = "productInfoKeyGenerator")
	@Column(name = "PRODUCT_INFO_ID")
	private Long id;
	
	@Column(name = "DESCR")
	@NotNull
	private String description;
	
	@Column(name = "QUANTITY")
	@NotNull
	private double quantity;
	
	@Column(name = "MEASURE")
	@Size(min = 2, max = 200)
	@NotNull
	private String measure;
	
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private Product product;
	
	public ProductInfo() {
		
	}
	
	public ProductInfo(Product product, String description, double quantity, String measure) {
		this.product = product;
		this.description = description;
		this.quantity = quantity;
		this.measure = measure;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
}
