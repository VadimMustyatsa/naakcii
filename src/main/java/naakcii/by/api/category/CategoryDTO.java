package naakcii.by.api.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.entity.AbstractDTOEntity;

@NoArgsConstructor
@Setter
@Getter
public class CategoryDTO extends AbstractDTOEntity {

    private Long id;

    private String name;

    private Integer priority;

    private String icon;

    private Boolean isActive;

    public CategoryDTO(Category category){
    	this.id = category.getId();
    	this.name = category.getName();
    	this.priority = category.getPriority();
    	this.icon = category.getIcon();
    	this.isActive = category.getIsActive();
    }
}
