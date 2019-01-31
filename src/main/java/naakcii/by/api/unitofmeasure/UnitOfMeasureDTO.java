package naakcii.by.api.unitofmeasure;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.util.CustomUnitStepSerializer;

@NoArgsConstructor
@Setter
@Getter
public class UnitOfMeasureDTO {
	
	private String name;
	
	@JsonSerialize(using = CustomUnitStepSerializer.class)
	private BigDecimal step;
	
	public UnitOfMeasureDTO(UnitOfMeasure unitOfMeasure) {
		this.name = unitOfMeasure.getName();
		this.step = unitOfMeasure.getStep();
	}
}
