package naakcii.by.api.subcategory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import naakcii.by.api.config.ApiConfigConstants;

import java.util.List;

@Api(description = "REST API для сущности Subcategory")
@RestController
@RequestMapping({"/subcategories"})
public class SubcategoryController {

    private SubcategoryService subcategoryService;
    
    @Autowired
    public SubcategoryController(SubcategoryService subcategoryService) {
    	this.subcategoryService = subcategoryService;
    }

    @GetMapping(path = "/{categoryId}", produces = ApiConfigConstants.API_V_2_0)
    @ApiOperation("Возвращает список всех подкатегорий товаров с параметром 'isActive' = true входящих в заданную категорию. "
    			+ "Список упорядочен по возрастанию параметра 'priority' подкатегории.")
    public List<SubcategoryDTO> getAllSubcategoriesByCategoryId(
            @ApiParam(value = "Идентификатор категории товара.", required = true)
            @PathVariable("categoryId") Long categoryId) {
        return subcategoryService.getAllSubcategoriesByCategoryId(categoryId);
    }
}
