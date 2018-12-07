package naakcii.by.api.actiontype;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ActionTypeDTO {
	
	private String name;
	private String icon;
	private String tooltipText;
	
	public ActionTypeDTO(ActionType actionType) {
		this.name = actionType.getName();
		this.icon = actionType.getIcon();
		this.tooltipText = actionType.getTooltipText();
	}
}
