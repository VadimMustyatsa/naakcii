package naakcii.by.api.category.controller;

import naakcii.by.api.category.service.CategoryService;
import naakcii.by.api.category.service.model.CategoryDTO;
import naakcii.by.api.config.ApiConfigConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenCategoriesDtoThenReturnJsonArray() throws Exception {
        CategoryDTO firstCategoryDto = new CategoryDTO();
        CategoryDTO secondCategoryDto = new CategoryDTO();
        firstCategoryDto.setName("firstCategoryDto");
        secondCategoryDto.setName("secondCategoryDto");
        given(categoryService.findAll()).willReturn(Arrays.asList(firstCategoryDto, secondCategoryDto));
        mockMvc.perform(get("/category")
                .accept(ApiConfigConstants.API_V1_0))
                .andExpect(status().isOk())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(content().contentTypeCompatibleWith(ApiConfigConstants.API_V1_0))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(firstCategoryDto.getName())))
                .andExpect(jsonPath("$[1].name", is(secondCategoryDto.getName())));
    }
}
