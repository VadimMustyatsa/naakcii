package naakcii.by.api.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.action.Action;
import naakcii.by.api.subcategory.Subcategory;
import naakcii.by.api.util.PureSize;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id", "picture", "quantity", "unit", "actions", "manufacturer", "brand", "countryOfOrigin"})
@Entity
@Table(name = "PRODUCT")
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PRODUCT_ID")
	private Long id;
	
	@Column(name = "PRODUCT_BARCODE")
	@NotNull(message = "Barcode of the product mustn't be null.")
	@Size(
	    	min = 4, 
	    	max = 14,
	    	message = "Barcode of the product '${validatedValue}' must be between '{min}' and '{max}' characters long."
	)
	@Pattern(regexp = "[0-9]+", message = "Barcode of the product must contain only digits.")
	private String barcode;
	
	@Column(name = "PRODUCT_NAME")
	@NotNull(message = "Name of the product mustn't be null.")
    @PureSize(
    	min = 3, 
    	max = 100,
    	message = "Name of the product '${validatedValue}' must be between '{min}' and '{max}' characters long."
    )
	private String name;
	
	@Column(name = "PRODUCT_PICTURE")
	@Size(
	    	max = 255, 
	    	message = "Path to the picture of the product '${validatedValue}' mustn't be more than '{max}' characters long."
	)
	private String picture;
	
	@Column(name = "PRODUCT_QUANTITY")
	@Positive(message = "Quantity of the product '${validatedValue}' must be positive.")
	@Digits(
		integer = 4, 
		fraction = 3,
		message = "Quantity of the product '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
	)
	private BigDecimal quantity;
	
	@Column(name = "PRODUCT_UNIT")
	@Enumerated(EnumType.STRING)
	private Unit unit;
	
	@Column(name = "PRODUCT_MANUFACTURER")
	@Size(
	   	max = 50,
	   	message = "Manufacturer of the product '${validatedValue}' mustn't be more than '{max}' characters long."
	)
	private String manufacturer;
	
	@Column(name = "PRODUCT_BRAND")
	@Size(
	   	max = 50,
	   	message = "Brand of the product '${validatedValue}' mustn't be more than '{max}' characters long."
	)
	private String brand;
	
	@Column(name = "PRODUCT_COUNTRY_OF_ORIGIN")
	@Size(
	   	max = 50,
	   	message = "Origin country of the product '${validatedValue}' mustn't be more than '{max}' characters long."
	)
	private String countryOfOrigin;
	
	@ManyToOne
	@JoinColumn(name = "SUBCATEGORY_ID")
	@NotNull(message = "Product must have subcategory.")
	@Valid
	private Subcategory subcategory;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private	Set<
		@Valid
		@NotNull(message = "Action mustn't be null.")
		Action> actions = new HashSet<Action>();
	
	@Column(name = "PRODUCT_IS_ACTIVE")
	@NotNull(message = "Product must have field 'isActive' defined.")
	private Boolean isActive;
	
	public Product(String barcode, String name, Boolean isActive) {
		this.barcode = barcode;
		this.name = name;
		this.isActive = isActive;
	}
		
	public Product(String barcode, String name, Boolean isActive, Subcategory subcategory) {
		this.barcode = barcode;
		this.name = name;
		this.isActive = isActive;
		this.subcategory = subcategory;
		subcategory.getProducts().add(this);
	}
	
	public Product(String barcode, String name, Boolean isActive, Subcategory subcategory, Set<Action> actions) {
		this.barcode = barcode;
		this.name = name;
		this.isActive = isActive;
		this.subcategory = subcategory;
		this.actions = actions;
		subcategory.getProducts().add(this);
	}
}
