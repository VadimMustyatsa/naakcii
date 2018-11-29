package naakcii.by.api.chain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ChainDTO {

    private Long id;
    private String name;
    private String logo;
    private String logoSmall;
    private String link;
    private Integer numberOfActions;
    private Integer averageDiscount;
    
    public ChainDTO(Chain chain) {
    	this.id = chain.getId();
    	this.name = chain.getName();
    	this.logo = chain.getLogo();
    	this.logoSmall = chain.getLogoSmall();
    	this.link = chain.getLink();
    }
}
