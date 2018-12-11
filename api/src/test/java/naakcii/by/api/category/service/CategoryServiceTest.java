import naakcii.by.api.category.repository.CategoryRepository;
import naakcii.by.api.category.repository.model.Category;
import naakcii.by.api.category.service.CategoryService;
import naakcii.by.api.category.service.model.CategoryDTO;
import naakcii.by.api.category.service.serviceImpl.CategoryServiceImpl;
import naakcii.by.api.subcategory.repository.model.Subcategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

@DirtiesContext
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CategoryServiceTest.CategoryServiceImplTestContextConfiguration.class)
public class CategoryServiceTest {

    @TestConfiguration
    @ComponentScan(basePackages = "naakcii.by.api.category.service")
    static class CategoryServiceImplTestContextConfiguration {
        @Bean
        public CategoryService categoryService() {
            return new CategoryServiceImpl();
        }
    }

    @MockBean
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;


    @Test
    public void fullTest() {
        List<Category> categories = createCategoryList();
        Mockito.when(categoryRepository.findAll()).thenReturn(categories);
        Assert.assertThat(categories.size(), is(2));

        List<CategoryDTO> categoryDTOS = categoryService.findAll();
        Assert.assertThat(categoryDTOS.size(), is(2));

        CategoryDTO cat1 = categoryDTOS.get(0);
        CategoryDTO cat2 = categoryDTOS.get(1);

        Assert.assertThat(cat1.getId(), is(2L));
        Assert.assertThat(cat1.getName(), is("cat2"));

        Assert.assertThat(cat2.getId(), is(1L));
        Assert.assertThat(cat2.getName(), is("cat1"));


    }

    private List<Category> createCategoryList() {
        List<Category> categories = new ArrayList<>();
        Category cat1 = new Category();
        Category cat2 = new Category();
        Subcategory subcategory = new Subcategory("subcat", true, null);
        cat1.setId(1L);
        cat1.setName("cat1");
        cat1.setPriority(1);
        cat2.setId(2L);
        cat2.setName("cat2");
        cat2.setPriority(5);
        cat1.setSubcategories(Arrays.asList(subcategory));
        categories.add(cat1);
        categories.add(cat2);
        return categories;
    }
}
