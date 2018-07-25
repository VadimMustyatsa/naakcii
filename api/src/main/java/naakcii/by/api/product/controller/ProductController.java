package naakcii.by.api.product.controller;

import naakcii.by.api.product.service.ProductService;
import naakcii.by.api.product.service.modelDTO.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping({"/product"})
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping(path = "/{id}")
    public List<ProductDTO> getProductsBySubcategory(@PathVariable("id") Long subcategoryId) {
        return productService.getProductsByChainIdAndSubcategoryId(subcategoryId);
    }

    @GetMapping
    public List<ProductDTO> findProductBySubcategoryIdLazyLoading(@RequestParam("first") Integer first,
                                                                  @RequestParam("last") Integer last,
                                                                  @RequestParam("SubcategoryList") List<Long> list) {
        List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();
        for (Long subcategoryId : list) {
            productDTOList.addAll(productService.getProductsByChainIdAndSubcategoryId(subcategoryId));
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
