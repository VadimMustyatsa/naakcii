package naakcii.by.api.repository.model;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "CATEGORY")
@NamedQueries({
	@NamedQuery(name = "Category.findAll", query = "select cat from Category cat"),
	@NamedQuery(name = "Category.findAllWithDetails", 
	query = "select cat from Category cat left join fetch cat.subcategories sub order by cat.id"), 
	@NamedQuery(name = "Category.findById", 
		query = "select cat from Category cat where cat.id = :id"),
	@NamedQuery(name = "Category.findByIdWithDetails", 
	query = "select cat from Category cat left join fetch cat.subcategories sub where cat.id = :id"),
	@NamedQuery(name = "Category.findByNameWithDetails", 
	query = "select cat from Category cat left join fetch cat.subcategories sub where cat.name = :name"),
	@NamedQuery(name = "Category.softDelete", 
		query = "update Category cat set cat.isActive=false where cat.id = :id")
})
public class Category implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -782539646608262755L;

	@Id
	@GeneratedValue(generator = "ID_GENERATOR")
	@Column(name = "CATEGORY_ID")
	private Long id;
	
	@Column(name = "CATEGORY_NAME")
	@NotNull
	@Size(min = 2, max = 45)
	private String name;
	
	@Column(name = "IS_ACTIVE")
	@NotNull
	private boolean isActive;
	
	@Column(name = "ICON")
	@Size(max = 45)
	private String icon;
	
	@OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	//@JoinColumn(name = "CATEGORY_ID")
	private List<Subcategory> subcategories = new ArrayList<Subcategory>();
	
	public Category() {
		
	}
	
	public Category(String name, boolean isActive) {
		this.name = name;
		this.isActive = isActive;
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

	public List<Subcategory> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(List<Subcategory> subcategories) {
		this.subcategories = subcategories;
	}
	
}
