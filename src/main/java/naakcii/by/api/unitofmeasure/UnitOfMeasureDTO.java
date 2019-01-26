package naakcii.by.api.unitofmeasure;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UnitOfMeasureDTO {

	@ApiModelProperty(notes = "Наименование единицы измерения", example="Кг")
	private String name;
	@ApiModelProperty(notes = "Шаг изменения единицы измерения", example="0.1")
	private BigDecimal step;
	
	public UnitOfMeasureDTO(UnitOfMeasure unitOfMeasure) {
		this.name = unitOfMeasure.getName();
		this.step = unitOfMeasure.getStep();
	}
}
