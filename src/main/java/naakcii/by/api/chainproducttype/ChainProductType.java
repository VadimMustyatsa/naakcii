package naakcii.by.api.chainproducttype;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.util.annotations.PureSize;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id", "tooltip"})
@Entity
@Table(name = "CHAIN_PRODUCT_TYPE")
public class ChainProductType implements Serializable {
	
	private static final long serialVersionUID = 5111366809131618230L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHAIN_PRODUCT_TYPE_ID")
    private Long id;
	
	@Column(name = "CHAIN_PRODUCT_TYPE_NAME")
    @NotNull(message = "ChainProductType's name mustn't be null.")
    @PureSize(
    	min = 3, 
    	max = 25,
    	message = "ChainProductType's name '${validatedValue}' must be between '{min}' and '{max}' characters long."
    )
	private String name;
	
	@Column(name = "CHAIN_PRODUCT_TYPE_SYNONYM")
    @NotNull(message = "ChainProductType's synonym mustn't be null.")
    @PureSize(
    	min = 3, 
    	max = 25,
    	message = "ChainProductType's synonym '${validatedValue}' must be between '{min}' and '{max}' characters long."
    )
	private String synonym;
	
	@Column(name = "CHAIN_PRODUCT_TYPE_TOOLTIP")
	@Size(
		max = 225,
		message = "ChainProductType's tooltip '${vallidatedVAlue}' mustn't be more than '{max}' characters long."
	)
	private String tooltip;
	
	public ChainProductType(String name) {
		this.name = name;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("Instance of " + ChainProductType.class + ":");
		result.append(System.lineSeparator());
		result.append("\t").append("id - " + id + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("name - " + name + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("synonym - " + synonym + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("tooltip - " + tooltip + ".");
		return result.toString();
	}
}
