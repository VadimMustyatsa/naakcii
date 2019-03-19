package naakcii.by.api.subcategory;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.category.Category;
import naakcii.by.api.product.Product;
import naakcii.by.api.util.annotations.PureSize;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
	@NotNull(message = "Subcategory's name mustn't be null.")
    @PureSize(
    	min = 3, 
    	max = 50,
    	message = "Subcategory's name '${validatedValue}' must be between '{min}' and '{max}' characters long."
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
		@NotNull(message = "Subcategory must have list of products without null elements.")
		Product> products = new HashSet<Product>();
	
	@Column(name = "SUBCATEGORY_PRIORITY")
	@Positive(message = "Subcategory's priority '${validatedValue}' must be positive.")
	private Integer priority;	
	
	@Column(name = "SUBCATEGORY_IS_ACTIVE")
	@NotNull(message = "Subcategory must have field 'isActive' defined.")
	private Boolean isActive;
	
	public Subcategory(SubcategoryDTO subcategoryDTO) {
		this.id = subcategoryDTO.getId();
		this.name = subcategoryDTO.getName();
		this.isActive = subcategoryDTO.getIsActive();
		this.priority = subcategoryDTO.getPriority();
	}
	
	public String toString() {
    	StringBuilder result = new StringBuilder("Instance of " + Subcategory.class + ":");
    	result.append(System.lineSeparator());
		result.append("\t").append("id - " + id + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("name - " + name + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("category id/name - " + (category == null ? null + "/" + null : category.getId() + "/" + category.getName()) + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("priority - " + priority + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("isActive - " + isActive + ".");
    	return result.toString();
    }
}
