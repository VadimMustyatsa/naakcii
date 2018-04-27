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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "CATEGORY")
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
	@Size(min = 3, max = 45)
	private String name;
	
	@Column(name = "CATEGORY_IS_ACTIVE")
	@NotNull
	private boolean isActive;
	
	@Column(name = "CATEGORY_ICON")
	@Size(max = 255)
	private String icon;
	
	@OneToMany(
			mappedBy = "category", 
			cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, 
			fetch = FetchType.LAZY
	)
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
}
