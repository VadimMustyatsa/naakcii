package naakcii.by.api.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import naakcii.by.api.config.ApiConfigConstants;

import java.util.List;

@RestController
@RequestMapping({"/categories"})
public class CategoryController {

    private CategoryService categoryService;
    
    @Autowired
    public CategoryController(CategoryService categoryService) {
    	this.categoryService = categoryService;
    }

    @GetMapping(produces = ApiConfigConstants.API_V_2_0)
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
