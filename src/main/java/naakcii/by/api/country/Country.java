package naakcii.by.api.country;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.util.annotations.PureSize;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "COUNTRY")
public class Country {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COUNTRY_ID")
    private Long id;
	
	@Column(name = "COUNTRY_NAME", unique = true, length = 50)
    @NotNull(message = "Country's name mustn't be null.")
    @PureSize(
    	min = 3, 
    	max = 50,
    	message = "Country's name '${validatedValue}' must be between '{min}' and '{max}' characters long."
    )
	private String name;
	
	@Column(name = "COUNTRY_ALPHA_CODE_2", unique = true, length = 2)
    @NotBlank(message = "Country's AlphaCode2 mustn't be blank.")
    @Size(
    	min = 2, 
    	max = 2,
    	message = "Country's AlphaCode2 '${validatedValue}' must be '{min}' characters long."
    )
	private String alphaCode2;
	
	@Column(name = "COUNTRY_ALPHA_CODE_3", unique = true, length = 3)
    @NotBlank(message = "Country's AlphaCode3 mustn't be blank.")
    @Size(
    	min = 3, 
    	max = 3,
    	message = "Country's AlphaCode3 '${validatedValue}' must be '{min}' characters long."
    )
	private String alphaCode3;
	
	public Country(CountryCode countryCode) {
		this.alphaCode2 = countryCode.getAlphaCode2();
		this.alphaCode3 = countryCode.getAlphaCode3();
		this.name = countryCode.getCountryName();
	}

	public Country(CountryDTO countryDTO) {
		this.id = countryDTO.getId();
		this.name = countryDTO.getName();
		this.alphaCode2 = countryDTO.getAlphaCode2();
		this.alphaCode3 = countryDTO.getAlphaCode3();
	}
}
