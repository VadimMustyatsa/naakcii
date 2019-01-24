package naakcii.by.api.unitofmeasure;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UnitOfMeasureDTO {
	
	private String name;
	private BigDecimal step;
	
	public UnitOfMeasureDTO(UnitOfMeasure unitOfMeasure) {
		this.name = unitOfMeasure.getName();
		this.step = unitOfMeasure.getStep();
	}
}
