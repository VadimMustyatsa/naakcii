package naakcii.by.api.unitofmeasure;

import java.math.BigDecimal;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.util.CustomUnitStepSerializer;

@NoArgsConstructor
@Setter
@Getter
public class UnitOfMeasureDTO {

	@ApiModelProperty(notes = "Наименование единицы измерения.", example = "кг")
	private String name;

	@ApiModelProperty(notes = "Шаг изменения количества товара для данной единицы измерения.", example = "0.1")
	@JsonSerialize(using = CustomUnitStepSerializer.class)
	private BigDecimal step;
	
	public UnitOfMeasureDTO(UnitOfMeasure unitOfMeasure) {
		this.name = unitOfMeasure.getName();
		this.step = unitOfMeasure.getStep();
	}
}
