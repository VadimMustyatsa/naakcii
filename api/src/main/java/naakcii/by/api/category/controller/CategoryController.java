package naakcii.by.api.category.controller;


import naakcii.by.api.category.service.CategoryService;
import naakcii.by.api.category.service.model.CategoryDTO;
import naakcii.by.api.config.ApiConfigConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/category"})
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping(produces = ApiConfigConstants.API_V1_0)
    public List<CategoryDTO> findAllCategories() {
        return categoryService.findAll();
    }

}
