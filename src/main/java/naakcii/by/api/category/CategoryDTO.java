package naakcii.by.api.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CategoryDTO {

    @ApiModelProperty(notes = "Искусственный идентификатор (суррогатный ключ) категории товара.", example = "1L")
    private Long Id;

    @ApiModelProperty(notes = "Наименование категории товара.", example = "Хлебобулочные изделия")
    private String name;

    @ApiModelProperty(notes = "Приоритет категории товара.", example = "100")
    private Integer priority;

    @ApiModelProperty(notes = "Путь к файлу, содержащему иконку категории товара.", example = "С:/categories/icons/fish.jpg")
    private String icon;
    
    public CategoryDTO(Category category) {
    	this.Id = category.getId();
    	this.name = category.getName();
    	this.priority = category.getPriority();
    	this.icon = category.getIcon();
    }
}
