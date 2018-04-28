package naakcii.by.api.controller;

import naakcii.by.api.service.CategoryService;
import naakcii.by.api.service.ChainService;
import naakcii.by.api.service.ProductService;
import naakcii.by.api.service.SubcategoryService;
import naakcii.by.api.service.modelDTO.CategoryDTO;
import naakcii.by.api.service.modelDTO.ChainDTO;
import naakcii.by.api.service.modelDTO.ProductDTO;
import naakcii.by.api.service.modelDTO.SubcategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping({"/api"})
public class APIController {

    @Autowired
    private ChainService chainService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SubcategoryService subcategoryService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping(path = {"/getCategory"})
    public List<CategoryDTO> findAllCategory() {
        return categoryService.findAll();
    }

    @GetMapping(path = {"/getChain"})
    public List<ChainDTO> findAllStorage() {
        return chainService.findAll();
    }

    @GetMapping(path = {"/getSubcategory"})
    public List<SubcategoryDTO> findSubcategoryByCategoryId(@RequestParam("id") Long id) {
        return subcategoryService.getSubcategoriesbyCategoryId(id);
    }

    @GetMapping(path = {"/getProducts"})
    public List<ProductDTO> findProductBySubcategoryId(@RequestParam("idSubcategory") Long id) {
        return productService.getProductsBySubcategoryID(id);
    }

    @GetMapping(path = {"/getFoodList"})
    public List<ProductDTO> findProductBySubcategoryIdLazyLoading(@RequestParam("first") Integer first,
                                                                  @RequestParam("last") Integer last,
                                                                  @RequestParam("SubcategoryList") List<Long> list) {
        List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();
        for (Long subcategoryId : list) {
            productDTOList.addAll(productService.getProductsBySubcategoryID(subcategoryId));
        }
        if (productDTOList.isEmpty()) {
            return productDTOList;
        } else {
            Integer size = productDTOList.size();
            if (last < size && first < size) {
                return productDTOList.subList(first, last);
            } else {
                if (first < size) {
                    return productDTOList.subList(first, size);
                } else {
                    productDTOList = new ArrayList<>();
                    return productDTOList;
                }
            }
        }
    }
}
