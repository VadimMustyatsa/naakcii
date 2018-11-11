package naakcii.by.api.chain.repository.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import naakcii.by.api.action.repository.model.Action;
import naakcii.by.api.util.PureSize;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id", "logo", "logoSmall", "actions"})
@Entity
@Table(name = "CHAIN")
public class Chain implements Serializable {

	private static final long serialVersionUID = -4338838997190141797L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CHAIN_ID")
	private Long id;
	
	@Column(name = "CHAIN_NAME")
	@NotNull(message = "Name of the chain mustn't be null.")
	@PureSize(
	   	min = 3, 
	   	max = 255,
	   	message = "Name of the chain '${validatedValue}' must be between {min} and {max} characters long."
	)
	private String name;
	
	@Column(name = "CHAIN_LOGO")
	@Size(
	   	max = 255,
	   	message = "Path to the logo of the chain '${validatedValue}' mustn't be more than {max} characters long."
	)
	private String logo;
	
	@Column(name = "CHAIN_LOGO_SMALL")
	@Size(
	   	max = 255,
	   	message = "Path to the small logo of the chain '${validatedValue}' mustn't be more than {max} characters long."
	)
	private String logoSmall;

	@Column(name = "CHAIN_IS_ACTIVE")
	@NotNull(message = "Chain must have field 'isActive' defined.")
	private Boolean isActive;
	
	@Column(name = "CHAIN_LINK")
	@NotNull(message = "Chain must have field 'link' defined.")
	@PureSize(
	   	min = 10, 
	  	max = 255,
	   	message = "Link of the chain '${validatedValue}' must be between {min} and {max} characters long."
	)
	private String link;
	
	@OneToMany(mappedBy = "chain", cascade = CascadeType.ALL)
	private Set<
		@Valid
		@NotNull(message = "Action mustn't be null.")
		Action> actions = new HashSet<Action>();
		
	public Chain(String name, String link, Boolean isActive) {
		this.name = name;
		this.link = link;
		this.isActive = isActive;
	}	
}
