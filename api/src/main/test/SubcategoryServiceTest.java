import naakcii.by.api.category.repository.model.Category;
import naakcii.by.api.subcategory.repository.SubcategoryRepository;
import naakcii.by.api.subcategory.repository.model.Subcategory;
import naakcii.by.api.subcategory.service.SubcategoryService;
import naakcii.by.api.subcategory.service.modelDTO.SubcategoryDTO;
import naakcii.by.api.subcategory.service.serviceImpl.SubcategoryServiceImpl;
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
import java.util.List;

import static org.hamcrest.CoreMatchers.is;


@DirtiesContext
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SubcategoryServiceTest.SubcategoryServiceImplTestContextConfiguration.class)
public class SubcategoryServiceTest {

    @Autowired
    private SubcategoryService subcategoryService;
    @MockBean
    private SubcategoryRepository subcategoryRepository;


    public List<Subcategory> setUp() {
        Category category = new Category();
        Subcategory subcategory = new Subcategory("sub_1", true, category);
        subcategory.setId(2L);
        List<Subcategory> subcategoryList = new ArrayList<>();
        subcategoryList.add(subcategory);
        category.setId(1L);
        category.setName("cat_1");
        category.setActive(true);

        category.setSubcategories(subcategoryList);
        return subcategoryList;
    }

    @Test
    public void fullTest() {
        List<Subcategory> subcategories = setUp();
        Mockito.when(subcategoryRepository.findByCategoryId(subcategories.get(0).getCategory().getId())).thenReturn(subcategories);
        Assert.assertThat(subcategories.size(), is(1));

        List<SubcategoryDTO> subcategoryDTOS = subcategoryService.getSubcategoryByCategoryId(subcategories.get(0).getCategory().getId());
        Assert.assertThat(subcategoryDTOS.size(), is(1));

        SubcategoryDTO subcategory = subcategoryDTOS.get(0);
        Assert.assertThat(subcategory.getName(), is("sub_1"));
    }

    @TestConfiguration
    @ComponentScan(basePackages = "naakcii.by.api.subcategory.service")
    static class SubcategoryServiceImplTestContextConfiguration {
        @Bean
        public SubcategoryService subcategoryService() {
            return new SubcategoryServiceImpl();
        }
    }
}
