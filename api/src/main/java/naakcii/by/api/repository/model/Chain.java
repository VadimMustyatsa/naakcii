package naakcii.by.api.repository.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
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
@Table(name = "CHAIN")
@Cacheable
@org.hibernate.annotations.Cache(
		usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,
		region = "naakcii.by.repository.model.cache.Chain"
)
@org.hibernate.annotations.NaturalIdCache
public class Chain implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4338838997190141797L;

	@Id
	@GeneratedValue(generator = "ID_GENERATOR")
	@Column(name = "CHAIN_ID")
	private Long id;
	
	@Column(name = "CHAIN_NAME")
	@NotNull
	@Size(min = 3, max = 45)
	@org.hibernate.annotations.NaturalId(mutable = true)
	private String name;
	
	@Column(name = "CHAIN_LOGO")
	@Size(max = 255)
	private String logo;
	
	@Column(name = "CHAIN_LOGO_SMALL")
	@Size(max = 255)
	private String logoSmall;

	@Column(name = "CHAIN_IS_ACTIVE")
	@NotNull
	private boolean isActive;
	
	@Column(name = "CHAIN_LINK")
	@NotNull
	@Size(max = 255)
	private String link;
	
	@OneToMany(mappedBy = "chain", fetch = FetchType.LAZY)
	private Set<Action> actions = new HashSet<Action>();
	
	public Chain() {
		
	}
	
	public Chain(String name, String link, boolean isActive) {
		this.name = name;
		this.link = link;
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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Set<Action> getActions() {
		return actions;
	}

	public void setActions(Set<Action> actions) {
		this.actions = actions;
	}
	
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getLogoSmall() {
		return logoSmall;
	}

	public void setLogoSmall(String logoSmall) {
		this.logoSmall = logoSmall;
	}
	
}
