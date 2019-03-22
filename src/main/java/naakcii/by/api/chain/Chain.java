package naakcii.by.api.chain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.chainproduct.ChainProduct;
import naakcii.by.api.util.annotations.PureSize;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id", "logo", "chainProducts"})
@Entity
@Table(name = "CHAIN")
public class Chain implements Serializable {

	private static final long serialVersionUID = -4338838997190141797L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CHAIN_ID")
	private Long id;
	
	@Column(name = "CHAIN_NAME")
	@NotNull(message = "Chain's name mustn't be null.")
	@PureSize(
	   	min = 3, 
	   	max = 25,
	   	message = "Chain's name '${validatedValue}' must be between '{min}' and '{max}' characters long."
	)
	private String name;
	
	@Column(name = "CHAIN_SYNONYM", unique = true, length = 50)
	@NotNull(message = "Chain's synonym chain mustn't be null.")
	@PureSize(
	   	min = 3, 
	   	max = 25,
	   	message = "Chain's synonym '${validatedValue}' must be between '{min}' and '{max}' characters long."
	)
	private String synonym;
	
	@Column(name = "CHAIN_LOGO")
	@Size(
	   	max = 255,
	   	message = "Path to the logo of the chain '${validatedValue}' mustn't be more than '{max}' characters long."
	)
	private String logo;
	
	@Column(name = "CHAIN_LINK")
	@NotNull(message = "Chain's link mustn't be null.")
	@PureSize(
	   	min = 10, 
	  	max = 255,
	   	message = "Chain's link '${validatedValue}' must be between '{min}' and '{max}' characters long."
	)
	private String link;
	
	@OneToMany(mappedBy = "chain")
	private Set<
		@Valid 
		@NotNull(message = "Chain must have list of chainProducts without null elements.")
				ChainProduct> chainProducts = new HashSet<>();
		
	@Column(name = "CHAIN_IS_ACTIVE")
	@NotNull(message = "Chain must have field 'isActive' defined.")
	private Boolean isActive;

	public Chain(ChainDTO chainDTO) {
		this.id = chainDTO.getId();
		this.name = chainDTO.getName();
		this.logo = chainDTO.getLogo();
		this.synonym = chainDTO.getSynonym();
		this.link = chainDTO.getLink();
		this.isActive = chainDTO.getIsActive();
	}
}
