package naakcii.by.api.subcategory.controller;

import naakcii.by.api.config.ApiConfigConstants;
import naakcii.by.api.subcategory.service.SubcategoryService;
import naakcii.by.api.subcategory.service.modelDTO.SubcategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/subcategory"})
public class SubcategoryController {

    @Autowired
    SubcategoryService subcategoryService;

    @GetMapping(path = "/{id}", produces = ApiConfigConstants.API_V1_0)
    public List<SubcategoryDTO> getSubcategoriesByCategoryId(@PathVariable("id") Long id) {
        return subcategoryService.getSubcategoryByCategoryId(id);
    }
}
