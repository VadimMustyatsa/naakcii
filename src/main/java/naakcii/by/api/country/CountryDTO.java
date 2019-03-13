package naakcii.by.api.country;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CountryDTO {

    private Long id;
    private String name;
    private String alphaCode2;
    private String alphaCode3;

    public CountryDTO(Country country){
        this.id = country.getId();
        this.name = country.getName();
        this.alphaCode2 = country.getAlphaCode2();
        this.alphaCode3 = country.getAlphaCode3();
    }
}
