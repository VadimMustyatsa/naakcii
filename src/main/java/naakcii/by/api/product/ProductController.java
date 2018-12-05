package naakcii.by.api.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping({"/products"})
public class ProductController {
	
	private static final Integer DEFAULT_PAGE_NIMBER = 0;
	private static final Integer DEFAULT_PAGE_SIZE = 12;
	private static final String DEFAULT_FIELD_FOR_SORTING = "discountPrice";
	
	private ProductService productService;
	
	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping(path = "/{chainSet}/{subcategorySet}/{page}/{size}")
    public List<ProductDTO> getAllProductsByChainIdsAndSubcategoryIds(
    		@PathVariable("chainSet") Set<Long> chainIds,
    		@PathVariable("subcategorySet") Set<Long> subcategoryIds,
    		@PathVariable("page") Integer page,
    		@PathVariable("size") Integer size) {
		if (page == null || page < 0) {
			page = DEFAULT_PAGE_NIMBER;
		}
		
		if (size == null || size <= 0) {
			size = DEFAULT_PAGE_SIZE;
		}
		
		Pageable pageRequest = PageRequest.of(page, size, Sort.DEFAULT_DIRECTION, DEFAULT_FIELD_FOR_SORTING);
		return productService.getAllProductsByChainIdsAndSubcategoryIds(subcategoryIds, chainIds, pageRequest);
	}
}
