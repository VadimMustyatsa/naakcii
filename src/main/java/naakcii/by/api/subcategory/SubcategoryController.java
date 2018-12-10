package naakcii.by.api.subcategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import naakcii.by.api.config.ApiConfigConstants;

import java.util.List;

@RestController
@RequestMapping({"/subcategories"})
public class SubcategoryController {

    private SubcategoryService subcategoryService;
    
    @Autowired
    public SubcategoryController(SubcategoryService subcategoryService) {
    	this.subcategoryService = subcategoryService;
    }

    @GetMapping(path = "/{categoryId}", produces = ApiConfigConstants.API_V_2_0)
    public List<SubcategoryDTO> getAllSubcategoriesByCategoryId(@PathVariable("categoryId") Long categoryId) {
        return subcategoryService.getAllSubcategoriesByCategoryId(categoryId);
    }
}
