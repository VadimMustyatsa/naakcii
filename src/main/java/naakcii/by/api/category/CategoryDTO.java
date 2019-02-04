package naakcii.by.api.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CategoryDTO {

    @ApiModelProperty(notes = "Id категории товара", example="1L")
    private Long Id;

    @ApiModelProperty(notes = "Наименование категории товара", example="Бакалея")
    private String name;

    @ApiModelProperty(notes = "Приоритет", example="1")
    private Integer priority;

    @ApiModelProperty(notes = "Путь к файлу с изображением категории товара", example="http://pathtoicon/icon.jpg")
    private String icon;
    
    public CategoryDTO(Category category) {
    	this.Id = category.getId();
    	this.name = category.getName();
    	this.priority = category.getPriority();
    	this.icon = category.getIcon();
    }
}
