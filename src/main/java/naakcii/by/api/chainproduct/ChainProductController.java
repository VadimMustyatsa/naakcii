package naakcii.by.api.chainproduct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import naakcii.by.api.config.ApiConfigConstants;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping({"/products"})
public class ChainProductController {
	
	private static final Integer DEFAULT_PAGE_NIMBER = 0;
	private static final Integer DEFAULT_PAGE_SIZE = 12;
	private static final String DEFAULT_FIELD_FOR_SORTING = "discountPrice";
	
	private ChainProductService chainProductService;
	
	@Autowired
	public ChainProductController(ChainProductService chainProductService) {
		this.chainProductService = chainProductService;
	}
	
	@GetMapping(produces = ApiConfigConstants.API_V_2_0)
    public List<ChainProductDTO> getAllProductsByChainIdsAndSubcategoryIds(
    		@RequestParam("chainIds") Set<Long> chainIds,
    		@RequestParam("subcategoryIds") Set<Long> subcategoryIds,
    		@RequestParam("page") Integer page,
    		@RequestParam("size") Integer size) {
		if (page == null || page < 0) {
			page = DEFAULT_PAGE_NIMBER;
		}
		
		if (size == null || size <= 0) {
			size = DEFAULT_PAGE_SIZE;
		}
		
		Pageable pageRequest = PageRequest.of(page, size, Sort.DEFAULT_DIRECTION, DEFAULT_FIELD_FOR_SORTING);
		return chainProductService.getAllProductsByChainIdsAndSubcategoryIds(subcategoryIds, chainIds, pageRequest);
	}
}
