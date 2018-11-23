package naakcii.by.api.subcategory.controller;

import naakcii.by.api.config.ApiConfigConstants;
import naakcii.by.api.subcategory.service.SubcategoryService;
import naakcii.by.api.subcategory.service.modelDTO.SubcategoryDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SubcategoryController.class)
public class SubcategoryControllerTest {

    @MockBean
    private SubcategoryService subcategoryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenSubcategoryDtoByCategoryIdThenReturnJsonArray() throws Exception {
        SubcategoryDTO firstSubcategory = new SubcategoryDTO();
        firstSubcategory.setCategoryId(1L);
        firstSubcategory.setName("firstSubcategory");
        given(subcategoryService.getSubcategoryByCategoryId(firstSubcategory.getCategoryId()))
                .willReturn(Collections.singletonList(firstSubcategory));
        mockMvc.perform(get("/subcategory/{id}", firstSubcategory.getCategoryId())
                .accept(ApiConfigConstants.API_V1_0))
                .andExpect(status().isOk())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(content().contentTypeCompatibleWith(ApiConfigConstants.API_V1_0))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(firstSubcategory.getName())));
    }
}
