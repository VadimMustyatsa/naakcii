package naakcii.by.api.subcategory.repository.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import naakcii.by.api.category.repository.model.Category;
import naakcii.by.api.product.repository.model.Product;
import naakcii.by.api.util.PureSize;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id", "priority", "products"})
@Entity
@Table(name = "SUBCATEGORY")
public class Subcategory implements Serializable {

	private static final long serialVersionUID = 4720680821468502372L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SUBCATEGORY_ID")
	private Long id;
	
	@Column(name = "SUBCATEGORY_NAME")
	@NotNull(message = "Name of the subcategory mustn't be null.")
    @PureSize(
    	min = 3, 
    	max = 255,
    	message = "Name of the subcategory '${validatedValue}' must be between {min} and {max} characters long."
    )
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "CATEGORY_ID")
	@NotNull(message = "Subcategory must have category.")
	@Valid
	private Category category;
	
	@OneToMany(mappedBy = "subcategory", cascade = CascadeType.ALL)
	private Set<
		@Valid
		@NotNull(message = "Product mustn't be null.")
		Product> products = new HashSet<Product>();
	
	@Column(name = "SUBCATEGORY_PRIORITY")
	@Positive(message = "Priority of the subcategory '${validatedValue}' must be positive.")
	private Integer priority;	
	
	@Column(name = "SUBCATEGORY_IS_ACTIVE")
	@NotNull(message = "Subcategory must have field 'isActive' defined.")
	private Boolean isActive;
	
	public Subcategory(String name, Boolean isActive) {
		this.name = name;
		this.isActive = isActive;
	}
	
	public Subcategory(String name, Category category, Boolean isActive) {
		this.name = name;
		this.category = category;
		this.isActive = isActive;
	}
}
