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

    @ApiModelProperty(notes = "Искусственный идентификатор (суррогатный ключ) подкатегории товара.", example = "1L")
    private Long id;
    @ApiModelProperty(notes = "Наименование подкатегории товара", example = "Сдобные изделия")
    private String name;
    @ApiModelProperty(notes = "Идентификатор категории товара, к которой принадлежит данная подкатегория.", example = "1L")
    private Long categoryId;
    @ApiModelProperty(notes = "Приоритет подкатегории товара.", example = "100")
    private Integer priority;
    
    public SubcategoryDTO(Subcategory subcategory) {
    	this.id = subcategory.getId();
    	this.name = subcategory.getName();
    	this.categoryId = subcategory.getOptionalCategory().map(Category::getId).orElse(null);
    	this.priority = subcategory.getPriority();
    }
}
