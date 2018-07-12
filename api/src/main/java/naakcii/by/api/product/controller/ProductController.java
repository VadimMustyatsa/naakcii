package naakcii.by.api.product.controller;

import naakcii.by.api.product.service.ProductService;
import naakcii.by.api.product.service.moddelDTO.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
