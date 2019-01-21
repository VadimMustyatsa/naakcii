package naakcii.by.api.category;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import naakcii.by.api.util.ObjectFactory;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceImplTest {
	
	private CategoryService categoryService;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	@Mock
	private ObjectFactory objectFactory;
		
	@Mock
	private Category firstCategory;
	
	@Mock
	private Category secondCategory;
	
	@Mock
	private Category thirdCategory;
	
	@Mock
	private CategoryDTO firstCategoryDTO;
	
	@Mock
	private CategoryDTO secondCategoryDTO;
	
	@Mock
	private CategoryDTO thirdCategoryDTO;
	
	@Before
	public void setUp() {
		categoryService = new CategoryServiceImpl(categoryRepository, objectFactory);
	}
	
	private List<Category> createListOfCategories() {
		List<Category> categories = new ArrayList<>();
		categories.add(firstCategory);
		categories.add(null);
		categories.add(secondCategory);
		categories.add(null);
		categories.add(thirdCategory);
		return categories;
	}
	
	@Test
	public void test_get_all_categories() {
		List<CategoryDTO> expectedCategoryDTOs = new ArrayList<>();
		expectedCategoryDTOs.add(firstCategoryDTO);
		expectedCategoryDTOs.add(secondCategoryDTO);
		expectedCategoryDTOs.add(thirdCategoryDTO);
		when(categoryRepository.findAllByIsActiveTrueOrderByPriorityAsc()).thenReturn(createListOfCategories());
		when(objectFactory.getInstance(CategoryDTO.class, firstCategory)).thenReturn(firstCategoryDTO);
		when(objectFactory.getInstance(CategoryDTO.class, secondCategory)).thenReturn(secondCategoryDTO);
		when(objectFactory.getInstance(CategoryDTO.class, thirdCategory)).thenReturn(thirdCategoryDTO);
		List<CategoryDTO> resultCategoryDTOs = categoryService.getAllCategories();
		verify(categoryRepository).findAllByIsActiveTrueOrderByPriorityAsc();
		verify(objectFactory).getInstance(CategoryDTO.class, firstCategory);
		verify(objectFactory).getInstance(CategoryDTO.class, secondCategory);
		verify(objectFactory).getInstance(CategoryDTO.class, thirdCategory);
		assertEquals("Size of the result list of category data transfer objects should be 3.", resultCategoryDTOs.size(), 3);
		assertEquals("Result list of category data transfer objects should be: [firstCategoryDTO, secondCategoryDTO, thirdCategoryDTO].", expectedCategoryDTOs, resultCategoryDTOs);
	}
}
