package by.naakcii.repository.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "PRODUCT")
@NamedQueries({
	@NamedQuery(name = "Product.findAll", query = "select p from Product p"),
	//@NamedQuery(name = "Product.findAllWithDetails", 
	//query = "select distinct cat from Category cat left join fetch cat.subcategories sub order by cat.id"), 
	@NamedQuery(name = "Product.findById", 
		query = "select p from Product p where p.id = :id"),
	//@NamedQuery(name = "Category.findByIdWithDetails", 
	//query = "select cat from Category cat left join fetch cat.subcategories sub where cat.id = :id"),
	@NamedQuery(name = "Product.softDelete", 
		query = "update Product p set p.isActive=false where p.id = :id")
})
public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "ID_GENERATOR")
	@Column(name = "PRODUCT_ID")
	private Long id;
	
	@Column(name = "PRODUCT_NAME")
	@NotNull
	@Size(min = 2, max = 45)
	private String name;
	
	@Column(name = "IS_ACTIVE")
	@NotNull
	private boolean isActive;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "SUBCATEGORY_ID",
			updatable = false,
			insertable = false
			)
	@NotNull
	private Subcategory subcategory;
	
	@OneToOne(
			mappedBy = "product",
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY
	)
	private ProductInfo productInfo;
	
	public Product() {
		
	}
	
	public Product(String name, boolean isActive, Subcategory subcategory) {
		this.name = name;
		this.isActive = isActive;
		this.subcategory = subcategory;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Subcategory getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(Subcategory subcategory) {
		this.subcategory = subcategory;
	}

	public ProductInfo getProductInfo() {
		return productInfo;
	}

	public void setProductInfo(ProductInfo productInfo) {
		this.productInfo = productInfo;
	}

}
