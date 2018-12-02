package naakcii.by.api.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping({"/product"})
public class ProductController {
	
	private ProductService productService;
	
	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping(path = "/{chainList}/{subcategoryList}")
    public List<ProductDTO> getAllProductsByChainIdsAndSubcategoryIds(
    			@RequestParam("subcategoryList") Set<Long> subcategoryIds,
    			@RequestParam("chainList") Set<Long> chainIds) {
		return productService.getAllProductsByChainIdsAndSubcategoryIds(subcategoryIds, chainIds);
	}
/*
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
    }*/
}
