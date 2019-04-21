package naakcii.by.api.chainproduct;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

import naakcii.by.api.chainproduct.ChainProductDTO;
import naakcii.by.api.chainproduct.ChainProductService;
import naakcii.by.api.chainproduct.ChainProductServiceImpl;
import naakcii.by.api.util.ObjectFactory;

@RunWith(MockitoJUnitRunner.class)
public class ChainProductServiceImplTest {
	
	private static Calendar currentDate = Calendar.getInstance();
	
	private ChainProductService productService;
	
	@Mock
	private ChainProductRepository chainProductRepository;
	
	@Mock
	private ObjectFactory objectFactory;
	
	@Mock
	private ChainProduct firstChainProduct;
	
	@Mock
	private ChainProduct secondChainProduct;
	
	@Mock
	private ChainProduct thirdChainProduct;
	
	@Mock
	private ChainProductDTO firstChainProductDTO;
	
	@Mock
	private ChainProductDTO secondChainProductDTO;
	
	@Mock
	private ChainProductDTO thirdChainProductDTO;
	
	@Mock
	private Set<Long> subcategoryIds;
	
	@Mock
	private Set<Long> chainIds;
	
	@Mock
	private Pageable pageable;
	
	@Before
	public void setUp() {
		productService = new ChainProductServiceImpl(chainProductRepository, objectFactory);
	}
	
	private Calendar getCurrentDate() {
		return new GregorianCalendar(currentDate.get(Calendar.YEAR), 
									 currentDate.get(Calendar.MONTH), 
									 currentDate.get(Calendar.DAY_OF_MONTH)
		);
	}
	
	private List<ChainProduct> createListOfChainProducts() {
		List<ChainProduct> chainProducts = new ArrayList<>();
		chainProducts.add(firstChainProduct);
		chainProducts.add(null);
		chainProducts.add(secondChainProduct);
		chainProducts.add(null);
		chainProducts.add(thirdChainProduct);
		return chainProducts;
	}
	
	@Test
	public void test_get_all_products_by_chain_ids_and_subcategory_ids() {
		List<ChainProductDTO> expectedProductDTOs = new ArrayList<>();
		expectedProductDTOs.add(firstChainProductDTO);
		expectedProductDTOs.add(secondChainProductDTO);
		expectedProductDTOs.add(thirdChainProductDTO);
		when(chainProductRepository.findByProductIsActiveTrueAndProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(subcategoryIds, chainIds, getCurrentDate(), getCurrentDate(), pageable))
			.thenReturn(createListOfChainProducts());
		when(objectFactory.getInstance(ChainProductDTO.class, firstChainProduct)).thenReturn(firstChainProductDTO);
		when(objectFactory.getInstance(ChainProductDTO.class, secondChainProduct)).thenReturn(secondChainProductDTO);
		when(objectFactory.getInstance(ChainProductDTO.class, thirdChainProduct)).thenReturn(thirdChainProductDTO);
		List<ChainProductDTO> resultProductDTOs = productService.getAllProductsByChainIdsAndSubcategoryIds(subcategoryIds, chainIds, pageable);
		verify(chainProductRepository).findByProductIsActiveTrueAndProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(subcategoryIds, chainIds, getCurrentDate(), getCurrentDate(), pageable);
		verify(objectFactory).getInstance(ChainProductDTO.class, firstChainProduct);
		verify(objectFactory).getInstance(ChainProductDTO.class, secondChainProduct);
		verify(objectFactory).getInstance(ChainProductDTO.class, thirdChainProduct);
		assertEquals("Size of the result list of product data transfer objects should be 3.", 3, resultProductDTOs.size());
		assertEquals("Result list of product data transfer objects should be: [firstProductDTO, secondProductDTO, thirdProductDTO].", expectedProductDTOs, resultProductDTOs);
	}
	
	@Test
	public void test_get_number_of_products_by_chain_ids_and_subcategory_ids() {
		when(chainProductRepository.countByProductIsActiveTrueAndProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(subcategoryIds, chainIds, getCurrentDate(), getCurrentDate()))
			.thenReturn(15L);	
		Long resultNumberOfProducts = productService.getNumberOfProductsByChainIdsAndSubcategoryIds(chainIds, subcategoryIds);
		verify(chainProductRepository).countByProductIsActiveTrueAndProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(subcategoryIds, chainIds, getCurrentDate(), getCurrentDate());
		assertEquals("Result number of products should be 15", 15L, resultNumberOfProducts.longValue());
	}
}
