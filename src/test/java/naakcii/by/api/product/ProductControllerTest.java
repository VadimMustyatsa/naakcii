package naakcii.by.api.product;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTest {

	private ProductController productController;
	private List<ProductDTO> productDTOs;
	
	@Mock
	private ProductService productService;
	
	@Mock
	private Set<Long> subcategoryIds;
	
	@Mock
	private Set<Long> chainIds;
	
	@Before
	public void setUp() {
		productController = new ProductController(productService);
	}
	
	@Test
	public void test_get_all_products_by_chain_ids_and_subcategory_ids() {
		when(productService.getAllProductsByChainIdsAndSubcategoryIds(subcategoryIds, chainIds)).thenReturn(productDTOs);
		productController.getAllProductsByChainIdsAndSubcategoryIds(subcategoryIds, chainIds);
		verify(productService).getAllProductsByChainIdsAndSubcategoryIds(subcategoryIds, chainIds);
	}
}
