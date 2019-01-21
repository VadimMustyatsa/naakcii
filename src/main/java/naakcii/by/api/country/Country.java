package naakcii.by.api.country;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
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
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "COUNTRY")
public class Country {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COUNTRY_ID")
    private Long id;
	
	@Column(name = "COUNTRY_NAME")
    @NotNull(message = "Name of the country mustn't be null.")
    @PureSize(
    	min = 3, 
    	max = 50,
    	message = "Name of the country '${validatedValue}' must be between '{min}' and '{max}' characters long."
    )
	private String name;
	
	@Column(name = "COUNTRY_ALPHA_CODE_2")
    @NotBlank(message = "AlphaCode2 of the country mustn't be blank.")
    @Size(
    	min = 2, 
    	max = 2,
    	message = "AlphaCode2 of the country '${validatedValue}' must be '{min}' characters long."
    )
	private String alphaCode2;
	
	@Column(name = "COUNTRY_ALPHA_CODE_3")
    @NotBlank(message = "AlphaCode3 of the country mustn't be blank.")
    @Size(
    	min = 3, 
    	max = 3,
    	message = "AlphaCode3 of the country '${validatedValue}' must be '{min}' characters long."
    )
	private String alphaCode3;
	
	public Country(CountryCode countryCode) {
		this.alphaCode2 = countryCode.getAlphaCode2();
		this.alphaCode3 = countryCode.getAlphaCode3();
		this.name = countryCode.getCountryName();
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("Instance of " + Country.class + ":");
		result.append(System.lineSeparator());
		result.append("\t").append("id - " + id + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("name - " + name + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("alpha code 2 - " + alphaCode2 + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("alpha code 3 - " + alphaCode3 + ".");
		return result.toString();
	}
}
