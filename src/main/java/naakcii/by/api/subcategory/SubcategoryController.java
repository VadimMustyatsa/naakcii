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

    @GetMapping(path = "/{id}")
    public List<SubcategoryDTO> getAllSubcategoriesByCategoryId(@PathVariable("id") Long categoryId) {
        return subcategoryService.getAllSubcategoriesByCategoryId(categoryId);
    }
}
