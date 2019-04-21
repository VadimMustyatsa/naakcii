package naakcii.by.api.subcategory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.category.Category;

@NoArgsConstructor
@Setter
@Getter
public class SubcategoryDTO {

    @ApiModelProperty(notes = "Id подкатегории товара", example = "1L")
    private Long id;
    @ApiModelProperty(notes = "Id торговой сети", example="Макароны")
    private String name;
    @ApiModelProperty(notes = "Id категории товара", example="1L")
    private Long categoryId;
    @ApiModelProperty(notes = "Приоритет", example="1")
    private Integer priority;
    
    public SubcategoryDTO(Subcategory subcategory) {
    	this.id = subcategory.getId();
    	this.name = subcategory.getName();
    	this.categoryId = subcategory.getOptionalCategory().map(Category::getId).orElse(null);
    	this.priority = subcategory.getPriority();
    }
}
