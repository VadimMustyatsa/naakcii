package naakcii.by.api.chainproducttype;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ChainProductTypeDTO {
	
	private String name;
	private String tooltip;
	
	public ChainProductTypeDTO(ChainProductType chainProductType) {
		this.name = chainProductType.getName();
		this.tooltip = chainProductType.getTooltip();
	}
}
