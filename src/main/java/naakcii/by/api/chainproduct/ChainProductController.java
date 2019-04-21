package naakcii.by.api.chainproduct;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import naakcii.by.api.config.ApiConfigConstants;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Api(description = "REST API для сущности ChainProduct")
@RestController
@RequestMapping({"/products"})
public class ChainProductController {
	
	private static final Integer DEFAULT_PAGE_NIMBER = 0;
	private static final Integer DEFAULT_PAGE_SIZE = 12;
	private static final String DEFAULT_FIELD_FOR_SORTING = "discountPrice";
	private static final String CHAIN_PRODUCTS_KEY = "chainProducts";
	private static final String NUMBER_OF_CHAIN_PRODUCTS_KEY = "numberOfChainProducts";
	
	private ChainProductService chainProductService;
	
	@Autowired
	public ChainProductController(ChainProductService chainProductService) {
		this.chainProductService = chainProductService;
	}
	
	@GetMapping(produces = ApiConfigConstants.API_V_2_0)
	@ApiOperation("Возвращает список товаров по id торговой сети и id подкатегории, отсортированный по цене товара со скидкой")
    public Map<String, ?> getAllProductsByChainIdsAndSubcategoryIds(
			@ApiParam(value = "Ids торговой сети", required = true)
    		@RequestParam("chainIds") Set<Long> chainIds,
			@ApiParam(value = "Ids подкатегорий товаров", required = true)
    		@RequestParam("subcategoryIds") Set<Long> subcategoryIds,
			@ApiParam(value = "Номер страницы. По умолчанию = 0", required = false)
    		@RequestParam(value = "page", required = false) Integer page,
			@ApiParam(value = "Количество ответов на странице. По умолчанию = 12", required = false)
    		@RequestParam(value = "size", required = false) Integer size) {
		if (page == null || page < 0) {
			page = DEFAULT_PAGE_NIMBER;
		}
		
		if (size == null || size <= 0) {
			size = DEFAULT_PAGE_SIZE;
		}
		
		Pageable pageRequest = PageRequest.of(page, size, Sort.DEFAULT_DIRECTION, DEFAULT_FIELD_FOR_SORTING);
		Map<String, Object> result = new LinkedHashMap<>();
		result.put(CHAIN_PRODUCTS_KEY, 
				   chainProductService.getAllProductsByChainIdsAndSubcategoryIds(subcategoryIds, chainIds, pageRequest)
									  .stream().filter(Objects::nonNull).collect(Collectors.toList()));
		result.put(NUMBER_OF_CHAIN_PRODUCTS_KEY, 
				   chainProductService.getNumberOfProductsByChainIdsAndSubcategoryIds(chainIds, subcategoryIds));
		return result;
	}
}
