package naakcii.by.api.chainproduct;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import naakcii.by.api.chainproduct.ChainProductController;
import naakcii.by.api.chainproduct.ChainProductDTO;
import naakcii.by.api.chainproduct.ChainProductService;

@RunWith(MockitoJUnitRunner.class)
public class ChainProductControllerTest {
	
	private static final Integer DEFAULT_PAGE_NIMBER = 0;
	private static final Integer DEFAULT_PAGE_SIZE = 12;
	private static final String DEFAULT_FIELD_FOR_SORTING = "discountPrice";
	
	private Long numberOfChainProducts;
	private ChainProductController chainProductController;
	private List<ChainProductDTO> chainProductDTOs;
	
	@Mock
	private ChainProductService chainProductProductService;
	
	@Mock
	private Set<Long> subcategoryIds;
	
	@Mock
	private Set<Long> chainIds;
		
	@Mock
	private ChainProductDTO firstChainProductDTO;
	
	@Mock
	private ChainProductDTO secondChainProductDTO;
	
	@Mock
	private ChainProductDTO thirdChainProductDTO;
	
	@Before
	public void setUp() {
		chainProductController = new ChainProductController(chainProductProductService);
		chainProductDTOs = new ArrayList<>();
		chainProductDTOs.add(firstChainProductDTO);
		chainProductDTOs.add(secondChainProductDTO);
		chainProductDTOs.add(thirdChainProductDTO);
	}
	
	@Test
	public void test_get_all_products_by_chain_ids_and_subcategory_ids() {
		Integer page = 0;
		Integer size = 12;
		Pageable pageRequest = PageRequest.of(page, size, Sort.DEFAULT_DIRECTION, DEFAULT_FIELD_FOR_SORTING);
		when(chainProductProductService.getAllProductsByChainIdsAndSubcategoryIds(subcategoryIds, chainIds, pageRequest)).thenReturn(chainProductDTOs);
		when(chainProductProductService.getNumberOfProductsByChainIdsAndSubcategoryIds(chainIds, subcategoryIds)).thenReturn(numberOfChainProducts);
		chainProductController.getAllProductsByChainIdsAndSubcategoryIds(chainIds, subcategoryIds, page, size);
		verify(chainProductProductService).getAllProductsByChainIdsAndSubcategoryIds(subcategoryIds, chainIds, pageRequest);
		verify(chainProductProductService).getNumberOfProductsByChainIdsAndSubcategoryIds(chainIds, subcategoryIds);
	}
	
	@Test
	public void test_get_all_products_by_chain_ids_and_subcategory_ids_when_page_number_is_negative() {
		Integer page = -5;
		Integer size = 12;
		Pageable pageRequest = PageRequest.of(DEFAULT_PAGE_NIMBER, size, Sort.DEFAULT_DIRECTION, DEFAULT_FIELD_FOR_SORTING);
		when(chainProductProductService.getAllProductsByChainIdsAndSubcategoryIds(subcategoryIds, chainIds, pageRequest)).thenReturn(chainProductDTOs);
		when(chainProductProductService.getNumberOfProductsByChainIdsAndSubcategoryIds(chainIds, subcategoryIds)).thenReturn(numberOfChainProducts);
		chainProductController.getAllProductsByChainIdsAndSubcategoryIds(chainIds, subcategoryIds, page, size);
		verify(chainProductProductService).getAllProductsByChainIdsAndSubcategoryIds(subcategoryIds, chainIds, pageRequest);
		verify(chainProductProductService).getNumberOfProductsByChainIdsAndSubcategoryIds(chainIds, subcategoryIds);
	}
	
	@Test
	public void test_get_all_products_by_chain_ids_and_subcategory_ids_when_page_size_is_zero() {
		Integer page = 2;
		Integer size = 0;
		Pageable pageRequest = PageRequest.of(page, DEFAULT_PAGE_SIZE, Sort.DEFAULT_DIRECTION, DEFAULT_FIELD_FOR_SORTING);
		when(chainProductProductService.getAllProductsByChainIdsAndSubcategoryIds(subcategoryIds, chainIds, pageRequest)).thenReturn(chainProductDTOs);
		when(chainProductProductService.getNumberOfProductsByChainIdsAndSubcategoryIds(chainIds, subcategoryIds)).thenReturn(numberOfChainProducts);
		chainProductController.getAllProductsByChainIdsAndSubcategoryIds(chainIds, subcategoryIds, page, size);
		verify(chainProductProductService).getAllProductsByChainIdsAndSubcategoryIds(subcategoryIds, chainIds, pageRequest);
		verify(chainProductProductService).getNumberOfProductsByChainIdsAndSubcategoryIds(chainIds, subcategoryIds);
	}
	
	@Test
	public void test_get_all_products_by_chain_ids_and_subcategory_ids_when_page_size_and_number_are_both_negative() {
		Integer page = -1;
		Integer size = -5;
		Pageable pageRequest = PageRequest.of(DEFAULT_PAGE_NIMBER, DEFAULT_PAGE_SIZE, Sort.DEFAULT_DIRECTION, DEFAULT_FIELD_FOR_SORTING);
		when(chainProductProductService.getAllProductsByChainIdsAndSubcategoryIds(subcategoryIds, chainIds, pageRequest)).thenReturn(chainProductDTOs);
		when(chainProductProductService.getNumberOfProductsByChainIdsAndSubcategoryIds(chainIds, subcategoryIds)).thenReturn(numberOfChainProducts);
		chainProductController.getAllProductsByChainIdsAndSubcategoryIds(chainIds, subcategoryIds, page, size);
		verify(chainProductProductService).getAllProductsByChainIdsAndSubcategoryIds(subcategoryIds, chainIds, pageRequest);
		verify(chainProductProductService).getNumberOfProductsByChainIdsAndSubcategoryIds(chainIds, subcategoryIds);
	}
	
	@After
	public void tearDown() {
		chainProductDTOs = null;
	}
}
