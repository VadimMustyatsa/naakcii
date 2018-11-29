package naakcii.by.api.category;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CategoryControllerTest {

	private CategoryController categoryController;
	private List<CategoryDTO> categoryDTOs;
	
	@Mock
	private CategoryService categoryService;
	
	@Before
	public void setUp() {
		categoryController = new CategoryController(categoryService);
	}
	
	@Test
	public void test_get_all_categories() {
		when(categoryService.getAllCategories()).thenReturn(categoryDTOs);
		categoryController.getAllCategories();
		verify(categoryService).getAllCategories();
	}
	
	@After
	public void tearDown() {
		categoryController = null;
	}
}
