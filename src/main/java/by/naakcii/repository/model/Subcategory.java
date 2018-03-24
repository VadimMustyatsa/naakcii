package by.naakcii.repository.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "SUBCATEGORY")
@NamedQueries({
	@NamedQuery(name = "Subcategory.findAll", query = "select sub from Subcategory sub"),
	@NamedQuery(name = "Subcategory.findAllWithDetails", 
			query = "select sub from Subcategory sub left join fetch sub.category cat"),
	@NamedQuery(name = "Subcategory.findById", 
		query = "select sub from Subcategory sub where sub.id = :id"),
	@NamedQuery(name = "Subcategory.findByIdWithDetails", 
	query = "select sub from Subcategory sub left join fetch sub.category cat where sub.id = :id")
})
public class Subcategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8463117187992698704L;
	
	@Id
	@GeneratedValue(generator = "ID_GENERATOR")
	@Column(name = "SUBCATEGORY_ID")
	private Long id;
	
	@Column(name = "CATEGORY_NAME")
	@NotNull
	@Size(min = 2, max = 45)
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "CATEGORY_ID",
			updatable = false,
			insertable = false
			)
	@NotNull
	private Category category;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBCATEGORY_ID",	nullable = false)
	private List<Product> products = new ArrayList<Product>();
	
	@Column(name = "IS_ACTIVE")
	@NotNull
	private boolean isActive;
	
	public Subcategory() {
		
	}
	
	public Subcategory(String name, boolean isActive, Category category) {
		this.name = name;
		this.isActive = isActive;
		this.category = category;
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
	
}
