package naakcii.by.api.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CategoryDTO {

    private Long Id;

    private String name;

    private Integer priority;

    private String icon;

    private Boolean isActive;
    
    public CategoryDTO(Category category) {
    	this.Id = category.getId();
    	this.name = category.getName();
    	this.priority = category.getPriority();
    	this.icon = category.getIcon();
    	this.isActive = category.getIsActive();
    }
}
