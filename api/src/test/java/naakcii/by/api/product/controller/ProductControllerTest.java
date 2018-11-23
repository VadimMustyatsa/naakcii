package naakcii.by.api.product.controller;

import naakcii.by.api.config.ApiConfigConstants;
import naakcii.by.api.product.service.ProductService;
import naakcii.by.api.product.service.modelDTO.ProductDTO;
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
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenProductDtoBySubcategoryIdThenReturnJsonArray() throws Exception {
        ProductDTO firstProduct = new ProductDTO();
        firstProduct.setName("firstProduct");
        firstProduct.setSubcategoryId(1L);
        given(productService.getProductsByChainIdAndSubcategoryId(firstProduct.getSubcategoryId()))
                .willReturn(Collections.singletonList(firstProduct));
        mockMvc.perform(get("/product/{id}", firstProduct.getSubcategoryId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(ApiConfigConstants.API_V1_0 + ";charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(firstProduct.getName())));
    }

    @Test
    public void givenProductDtoBySubcategoryIdLazyLoadingThenReturnJsonArray() throws Exception {
        ProductDTO firstProduct = new ProductDTO();
        firstProduct.setName("firstProduct");
        firstProduct.setSubcategoryId(1L);
        ProductDTO secondProduct = new ProductDTO();
        secondProduct.setName("secondProduct");
        secondProduct.setSubcategoryId(2L);
        given(productService.getProductsByChainIdAndSubcategoryId(firstProduct.getSubcategoryId()))
                .willReturn(Collections.singletonList(firstProduct));
        given(productService.getProductsByChainIdAndSubcategoryId(secondProduct.getSubcategoryId()))
                .willReturn(Collections.singletonList(secondProduct));
        mockMvc.perform(get("/product")
                .param("first", "0")
                .param("last", "10")
                .param("SubcategoryList", firstProduct.getSubcategoryId().toString())
                .param("SubcategoryList", secondProduct.getSubcategoryId().toString())
                .accept(ApiConfigConstants.API_V1_0))
                .andExpect(status().isOk())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(content().contentTypeCompatibleWith(ApiConfigConstants.API_V1_0))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(firstProduct.getName())))
                .andExpect(jsonPath("$[1].name", is(secondProduct.getName())));
    }
}
