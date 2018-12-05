package naakcii.by.api.subcategory.repository.model;

import naakcii.by.api.category.repository.model.Category;
import naakcii.by.api.product.repository.model.Product;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SUBCATEGORY")
//@Cacheable
//@org.hibernate.annotations.Cache(
//		usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,
//		region = "naakcii.by.repository.modelDTO.cache.Subcategory"
//)
public class Subcategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4720680821468502372L;

	@Id
	@GeneratedValue(generator = "ID_GENERATOR")
	@Column(name = "SUBCATEGORY_ID")
	private Long id;
	
	@Column(name = "SUBCATEGORY_NAME")
	@NotNull
	@Size(min = 3, max = 45)
	private String name;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_ID")
	@NotNull
	private Category category;
	
	@OneToMany(
			mappedBy = "subcategory", 
			cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, 
			fetch = FetchType.LAZY
	)
//	@org.hibernate.annotations.Cache(
//			usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE
//	)
	private List<Product> products = new ArrayList<Product>();
	
	@Column(name = "SUBCATEGORY_IS_ACTIVE")
	@NotNull
	private boolean isActive;

	@Column(name = "SUBCATEGORY_PRIORITY")
	@NotNull
	private int priority;
	
	public Subcategory() {
		
	}
	
	public Subcategory(String name, boolean isActive, Category category) {
		this.name = name;
		this.isActive = isActive;
		this.category = category;
	}

	public Subcategory(String name, boolean isActive, Category category, int priority) {
		this.name = name;
		this.category = category;
		this.isActive = isActive;
		this.priority = priority;
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public int getPriority() { return priority;	}

	public void setPriority(int priority) {	this.priority = priority; }
	
}
