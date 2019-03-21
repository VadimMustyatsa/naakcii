package naakcii.by.api.chainproducttype;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.entity.AbstractDTOEntity;

@NoArgsConstructor
@Setter
@Getter
public class ChainProductTypeDTO extends AbstractDTOEntity {

	private Long id;
	private String name;
	private String synonym;
	private String tooltip;
	
	public ChainProductTypeDTO(ChainProductType chainProductType) {
		this.id = chainProductType.getId();
		this.name = chainProductType.getName();
		this.synonym = chainProductType.getSynonym();
		this.tooltip = chainProductType.getTooltip();
	}
}
