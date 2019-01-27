package naakcii.by.api.subcategory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubcategoryControllerTest {

    private SubcategoryController subcategoryController;

    @Mock
    private SubcategoryService subcategoryService;

    @Before
    public void setUp() {
        subcategoryController = new SubcategoryController(subcategoryService);
    }

    @Test
    public void test_get_all_subcategories_by_category_id() {
        Long categoryId = 500L;
        when(subcategoryService.getAllSubcategoriesByCategoryId(categoryId)).thenReturn(Collections.emptyList());
        subcategoryController.getAllSubcategoriesByCategoryId(categoryId);
        verify(subcategoryService).getAllSubcategoriesByCategoryId(categoryId);
    }
}
