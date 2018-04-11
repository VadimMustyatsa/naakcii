package naakcii.by.api.repository.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "PRODUCT")
@NamedQueries({
	@NamedQuery(name = "Product.findAll", query = "select p from Product p"),
	@NamedQuery(name = "Product.findAllWithDetails", 
	query = "select p from Product p left join fetch p.productInfo pi left join fetch p.subcategory sub"), 
	@NamedQuery(name = "Product.findById", 
		query = "select p from Product p where p.id = :id"),
	@NamedQuery(name = "Product.findByIdWithDetails", 
	query = "select p from Product p left join fetch p.productInfo pi left join fetch p.subcategory sub where p.id = :id"),
	@NamedQuery(name = "Product.findBySubcategoryId", 
	query = "select p from Product p left join fetch p.subcategory sub where sub.id = :id"),
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
	//@Size(min = 2, max = 45)
	private String name;
	
	@Column(name = "IS_ACTIVE")
	@NotNull
	private boolean isActive;
	
	@Column(name = "ICON")
	@Size(max = 45)
	private String icon;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "SUBCATEGORY_ID"//,
			//updatable = false,
			//insertable = false
	)
	@NotNull
	private Subcategory subcategory;
	
	@OneToOne(
			mappedBy = "product",
			cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
			fetch = FetchType.LAZY
	)
	private ProductInfo productInfo;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<Action> actions = new HashSet<Action>();
	
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Set<Action> getActions() {
		return actions;
	}

	public void setActions(Set<Action> actions) {
		this.actions = actions;
	}

}
