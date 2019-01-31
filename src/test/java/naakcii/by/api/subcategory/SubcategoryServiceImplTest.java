package naakcii.by.api.subcategory;

import naakcii.by.api.util.ObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubcategoryServiceImplTest {

    private SubcategoryService subcategoryService;

    @Mock
    private SubcategoryRepository subcategoryRepository;

    @Mock
    private ObjectFactory objectFactory;

    @Mock
    private Subcategory firstSubcategory;

    @Mock
    private Subcategory secondSubcategory;

    @Mock
    private Subcategory thirdSubcategory;

    @Mock
    private SubcategoryDTO firstSubcategoryDTO;

    @Mock
    private SubcategoryDTO secondSubcategoryDTO;

    @Mock
    private SubcategoryDTO thirdSubcategoryDTO;

    @Before
    public void setUp() {
        subcategoryService = new SubcategoryServiceImpl(subcategoryRepository, objectFactory);
    }

    private List<Subcategory> createListOfSubcategories() {
        List<Subcategory> subcategories = new ArrayList<>();
        subcategories.add(firstSubcategory);
        subcategories.add(null);
        subcategories.add(secondSubcategory);
        subcategories.add(null);
        subcategories.add(thirdSubcategory);
        return subcategories;
    }

    @Test
    public void test_get_all_subcategories_by_category_id() {
        Long categoryId = 100500L;
        List<SubcategoryDTO> expectedSubcategoryDTOs = new ArrayList<>();
        expectedSubcategoryDTOs.add(firstSubcategoryDTO);
        expectedSubcategoryDTOs.add(secondSubcategoryDTO);
        expectedSubcategoryDTOs.add(thirdSubcategoryDTO);
        when(subcategoryRepository.findByIsActiveTrueAndCategoryIdOrderByPriorityAsc(categoryId)).thenReturn(createListOfSubcategories());
        when(objectFactory.getInstance(SubcategoryDTO.class, firstSubcategory)).thenReturn(firstSubcategoryDTO);
        when(objectFactory.getInstance(SubcategoryDTO.class, secondSubcategory)).thenReturn(secondSubcategoryDTO);
        when(objectFactory.getInstance(SubcategoryDTO.class, thirdSubcategory)).thenReturn(thirdSubcategoryDTO);
        List<SubcategoryDTO> resultSubcategoryDTOs = subcategoryService.getAllSubcategoriesByCategoryId(categoryId);
        verify(subcategoryRepository).findByIsActiveTrueAndCategoryIdOrderByPriorityAsc(categoryId);
        verify(objectFactory).getInstance(SubcategoryDTO.class, firstSubcategory);
        verify(objectFactory).getInstance(SubcategoryDTO.class, secondSubcategory);
        verify(objectFactory).getInstance(SubcategoryDTO.class, thirdSubcategory);
        assertEquals("Size of the result list of subcategory data transfer objects should be 3.", resultSubcategoryDTOs.size(), 3);
        assertEquals("Result list of subcategory data transfer objects should be: [firstSubcategoryDTO, secondSubcategoryDTO, thirdSubcategoryDTO].", expectedSubcategoryDTOs, resultSubcategoryDTOs);
    }
}
