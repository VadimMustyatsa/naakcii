package naakcii.by.api.category;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import naakcii.by.api.config.ApiConfigConstants;

import java.util.List;

@Api(description = "REST API для сущности Category")
@RestController
@RequestMapping({"/categories"})
public class CategoryController {

    private CategoryService categoryService;
    
    @Autowired
    public CategoryController(CategoryService categoryService) {
    	this.categoryService = categoryService;
    }

    @GetMapping(produces = ApiConfigConstants.API_V_2_0)
    @ApiOperation("Возвращает список всех категорий товаров")
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
