package naakcii.by.api.unitofmeasure;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.util.annotations.PureSize;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "UOM")
public class UnitOfMeasure {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UOM_ID")
    private Long id;
	
	@Column(name = "UOM_NAME")
    @NotNull(message = "UOM's name mustn't be null.")
    @PureSize(
    	min = 2, 
    	max = 10,
    	message = "UOM's name '${validatedValue}' must be between '{min}' and '{max}' characters long."
    )
	private String name;
	
	@Column(name = "UOM_STEP")
	@NotNull(message = "UOM's step mustn't be null.")
    @Digits(
    	integer = 3, 
    	fraction = 3,
    	message = "UOM's step '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    private BigDecimal step;
	
	public UnitOfMeasure(UnitCode unitCode) {
		this.name = unitCode.getRepresentation();
		this.step = unitCode.getDefaultStep();
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("Instance of " + UnitOfMeasure.class + ":");
		result.append(System.lineSeparator());
		result.append("\t").append("id - " + id + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("name - " + name + ";");
		result.append(System.lineSeparator());
		result.append("\t").append("step - " + step + ".");
		return result.toString();
	}
}