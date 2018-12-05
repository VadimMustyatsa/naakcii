package naakcii.by.api.subcategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/subcategory"})
public class SubcategoryController {

    private SubcategoryService subcategoryService;
    
    @Autowired
    public SubcategoryController(SubcategoryService subcategoryService) {
    	this.subcategoryService = subcategoryService;
    }

    @GetMapping(path = "/{categoryId}")
    public List<SubcategoryDTO> getAllSubcategoriesByCategoryId(@PathVariable("categoryId") Long categoryId) {
        return subcategoryService.getAllSubcategoriesByCategoryId(categoryId);
    }
}
