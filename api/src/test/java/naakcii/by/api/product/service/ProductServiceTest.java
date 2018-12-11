import naakcii.by.api.action.repository.ActionRepository;
import naakcii.by.api.action.repository.model.Action;
import naakcii.by.api.category.repository.model.Category;
import naakcii.by.api.chain.repository.model.Chain;
import naakcii.by.api.product.repository.ProductRepository;
import naakcii.by.api.product.repository.model.Product;
import naakcii.by.api.product.service.ProductService;
import naakcii.by.api.product.service.modelDTO.ProductDTO;
import naakcii.by.api.product.service.serviceImpl.ProductServiceImpl;
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
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

@DirtiesContext
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ProductServiceTest.ProductServiceTestContextConfiguration.class)
public class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private ActionRepository actionRepository;


    public List<Product> setUp() {
        Chain chain = new Chain();
        Action action = new Action();
        Product product = new Product();
        List<Product> products = new ArrayList<>();
        Category category = new Category();
        Subcategory subcategory = new Subcategory("subcategory_1", true, category);
        List<Subcategory> subcategories = new ArrayList<>();
        subcategories.add(subcategory);
        subcategory.setId(1L);
        subcategory.setProducts(products);
        category.setId(1L);
        category.setName("category_1");
        product.setId(1L);
        product.setName("product_1");
        product.setQuantity(2);
        category.setSubcategories(subcategories);
        product.setSubcategory(subcategory);
        chain.setId(1L);
        chain.setName("chain_1");
        action.setChain(chain);
        action.setProduct(product);
        action.setPrice(250);
        action.setDiscountPrice(200);
        action.setDiscount(20);
        List<Action> actions = new ArrayList<>();
        actions.add(action);
        product.setActions(actions);
        products.add(product);
        return products;
    }

    @Test
    public void fullTest() {
        List<Product> products = setUp();
        List<Action> actions = new ArrayList<>(products.get(0).getActions());
        Mockito.when(productRepository.getBySubcategoryId(products.get(0).getSubcategory().getId())).thenReturn(products);
        Mockito.when(actionRepository.findAllByProductId(products.get(0).getId())).thenReturn(actions);
        Assert.assertThat(products.size(), is(1));

        List<ProductDTO> productDTOList = productService.getProductsByChainIdAndSubcategoryId(products.get(0).getSubcategory().getId());
        Assert.assertThat(productDTOList.size(), is(1));

        ProductDTO product = productDTOList.get(0);
        Assert.assertThat(product.getName(), is("product_1"));
    }

    @TestConfiguration
    @ComponentScan(basePackages = "naakcii.by.api.product.service")
    static class ProductServiceTestContextConfiguration {
        @Bean
        public ProductService productService() {
            return new ProductServiceImpl();
        }
    }
}
