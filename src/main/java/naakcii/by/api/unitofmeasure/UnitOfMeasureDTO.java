package naakcii.by.api.unitofmeasure;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.entity.AbstractDTOEntity;

import java.math.BigDecimal;

@NoArgsConstructor
@Setter
@Getter
public class UnitOfMeasureDTO extends AbstractDTOEntity {

    private Long id;
    private String name;
    private BigDecimal step;

    public UnitOfMeasureDTO(UnitOfMeasure unitOfMeasure) {
        this.id = unitOfMeasure.getId();
        this.name = unitOfMeasure.getName();
        this.step = unitOfMeasure.getStep();
    }
}
