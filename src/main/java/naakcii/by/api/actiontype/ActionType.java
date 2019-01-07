package naakcii.by.api.actiontype;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.util.annotations.PureSize;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id", "tooltip"})
@Entity
@Table(name = "ACTION_TYPE")
public class ActionType implements Serializable {
	
	private static final long serialVersionUID = 5111366809131618230L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACTION_TYPE_ID")
    private Long id;
	
	@Column(name = "ACTION_TYPE_NAME")
    @NotNull(message = "Name of the action type mustn't be null.")
    @PureSize(
    	min = 3, 
    	max = 100,
    	message = "Name of the action type '${validatedValue}' must be between '{min}' and '{max}' characters long."
    )
	private String name;
	
	@Column(name = "ACTION_TYPE_TOOLTIP")
	@Size(
		max = 225,
		message = "Tooltip of the action type '${vallidatedVAlue}' mustn't be more than '{max}' characters long."
	)
	private String tooltip;
	
	public ActionType(String name) {
		this.name = name;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("Instance of " + ActionType.class + ":");
		result.append(System.lineSeparator());
		result.append("id - " + id + ";");
		result.append(System.lineSeparator());
		result.append("name - " + name + ";");
		result.append(System.lineSeparator());
		result.append("tooltip - " + tooltip + ".");
		result.append(System.lineSeparator());
		return result.toString();
	}
}
