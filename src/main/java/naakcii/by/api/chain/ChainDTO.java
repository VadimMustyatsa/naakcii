package naakcii.by.api.chain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.entity.AbstractDTOEntity;

@NoArgsConstructor
@Setter
@Getter
public class ChainDTO extends AbstractDTOEntity {

    private Long id;
    private String name;
    private String logo;
    private String link;
    private String synonym;
    private Boolean isActive;

    
    public ChainDTO(Chain chain) {
    	this.id = chain.getId();
    	this.name = chain.getName();
    	this.logo = chain.getLogo();
    	this.link = chain.getLink();
    	this.synonym = chain.getSynonym();
    	this.isActive = chain.getIsActive();
    }
}
