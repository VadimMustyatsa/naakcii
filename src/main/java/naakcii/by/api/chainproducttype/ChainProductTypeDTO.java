package naakcii.by.api.chainproducttype;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ChainProductTypeDTO {

	@ApiModelProperty(notes = "Название акции",example="1+1")
	private String name;
	@ApiModelProperty(notes = "Всплывающее сообщение",example="1+1")
	private String tooltip;
	
	public ChainProductTypeDTO(ChainProductType chainProductType) {
		this.name = chainProductType.getName();
		this.tooltip = chainProductType.getTooltip();
	}
}
