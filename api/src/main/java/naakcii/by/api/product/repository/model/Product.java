package naakcii.by.api.product.repository.model;

import naakcii.by.api.action.repository.model.Action;
import naakcii.by.api.subcategory.repository.model.Subcategory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "PRODUCT")
//@Cacheable
//@org.hibernate.annotations.Cache(
//		usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_ONLY,
//		region = "naakcii.by.repository.modelDTO.cache.Product"
//)
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
	@Size(min = 3, max = 200)
	private String name;
	
	@Column(name = "PRODUCT_IS_ACTIVE")
	@NotNull
	private boolean isActive;
	
	@Column(name = "PRODUCT_PICTURE")
	@Size(max = 255)
	private String picture;
	
	@Column(name = "PRODUCT_QUANTITY")
	private double quantity;
	
	@Column(name = "PRODUCT_MEASURE")
	@Size(max = 15)
	private String measure;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBCATEGORY_ID")
	@NotNull
	private Subcategory subcategory;
	
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	private List<Action> actions = new ArrayList<>();
	
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

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
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

}
