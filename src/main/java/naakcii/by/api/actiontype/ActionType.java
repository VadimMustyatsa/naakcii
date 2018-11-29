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
import naakcii.by.api.util.PureSize;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id", "icon"})
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
	
	@Column(name = "ACTION_TYPE_ICON")
    @Size(
    	max = 255, 
    	message = "Path to the icon of the action type '${validatedValue}' mustn't be more than '{max}' characters long."
    )
	private String icon;
	
	public ActionType(String name) {
		this.name = name;
	}
}
