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
	private ChainProductRepository actionRepository;
	
	@Mock
	private ObjectFactory objectFactory;
	
	@Mock
	private ChainProduct firstAction;
	
	@Mock
	private ChainProduct secondAction;
	
	@Mock
	private ChainProduct thirdAction;
	
	@Mock
	private ChainProductDTO firstProductDTO;
	
	@Mock
	private ChainProductDTO secondProductDTO;
	
	@Mock
	private ChainProductDTO thirdProductDTO;
	
	@Mock
	private Set<Long> subcategoryIds;
	
	@Mock
	private Set<Long> chainIds;
	
	@Mock
	private Pageable pageable;
	
	@Before
	public void setUp() {
		productService = new ChainProductServiceImpl(actionRepository, objectFactory);
	}
	
	private Calendar getCurrentDate() {
		return new GregorianCalendar(currentDate.get(Calendar.YEAR), 
									 currentDate.get(Calendar.MONTH), 
									 currentDate.get(Calendar.DAY_OF_MONTH)
		);
	}
	
	private List<ChainProduct> createListOfActions() {
		List<ChainProduct> actions = new ArrayList<>();
		actions.add(firstAction);
		actions.add(null);
		actions.add(secondAction);
		actions.add(null);
		actions.add(thirdAction);
		return actions;
	}
	
	@Test
	public void test_get_all_products_by_chain_ids_and_subcategory_ids() {
		List<ChainProductDTO> expectedProductDTOs = new ArrayList<>();
		expectedProductDTOs.add(firstProductDTO);
		expectedProductDTOs.add(secondProductDTO);
		expectedProductDTOs.add(thirdProductDTO);
		when(actionRepository.findByProductIsActiveTrueAndProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(subcategoryIds, chainIds, getCurrentDate(), getCurrentDate(), pageable))
			.thenReturn(createListOfActions());
		when(objectFactory.getInstance(ChainProductDTO.class, firstAction)).thenReturn(firstProductDTO);
		when(objectFactory.getInstance(ChainProductDTO.class, secondAction)).thenReturn(secondProductDTO);
		when(objectFactory.getInstance(ChainProductDTO.class, thirdAction)).thenReturn(thirdProductDTO);
		List<ChainProductDTO> resultProductDTOs = productService.getAllProductsByChainIdsAndSubcategoryIds(subcategoryIds, chainIds, pageable);
		verify(actionRepository).findByProductIsActiveTrueAndProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(subcategoryIds, chainIds, getCurrentDate(), getCurrentDate(), pageable);
		verify(objectFactory).getInstance(ChainProductDTO.class, firstAction);
		verify(objectFactory).getInstance(ChainProductDTO.class, secondAction);
		verify(objectFactory).getInstance(ChainProductDTO.class, thirdAction);
		assertEquals("Size of the result list of product data transfer objects should be 3.", resultProductDTOs.size(), 3);
		assertEquals("Result list of product data transfer objects should be: [firstProductDTO, secondProductDTO, thirdProductDTO].", expectedProductDTOs, resultProductDTOs);
	}
}
