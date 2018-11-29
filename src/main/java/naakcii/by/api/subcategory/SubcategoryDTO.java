package naakcii.by.api.subcategory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class SubcategoryDTO {
	
    private Long id;
    private String name;
    private Long categoryId;
    private Integer priority;
    
    public SubcategoryDTO(Subcategory subcategory) {
    	this.id = subcategory.getId();
    	this.name = subcategory.getName();
    	this.categoryId = subcategory.getCategory().getId();
    	this.priority = subcategory.getPriority();
    }
}
