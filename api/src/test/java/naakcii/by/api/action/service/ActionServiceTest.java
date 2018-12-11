import naakcii.by.api.action.repository.ActionRepository;
import naakcii.by.api.action.repository.model.Action;
import naakcii.by.api.action.service.ActionService;
import naakcii.by.api.action.service.serviceImpl.ActionServiceImpl;
import naakcii.by.api.category.repository.model.Category;
import naakcii.by.api.chain.repository.model.Chain;
import naakcii.by.api.chain.service.util.ChainConverter;
import naakcii.by.api.product.repository.model.Product;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ActionServiceTest.ActionServiceTestConfig.class)
public class ActionServiceTest {

    @Autowired
    private ActionService actionService;
    @Autowired
    private ChainConverter chainConverter;
    @MockBean
    private ActionRepository actionRepository;


    public List<Action> setUp() {
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
        Set<Action> actionSet = new HashSet<>();
        actionSet.addAll(actions);
        chain.setActions(actionSet);
        List<Chain> chains = new ArrayList<>();
        chains.add(chain);
        return actions;
    }

    @Test
    public void actionTest() {
        List<Action> actions = setUp();
        Mockito.when(actionRepository.findAllByChain(actions.get(0).getChain())).thenReturn(actions);
        Assert.assertThat(actions.size(), is(1));

        Chain chain = actions.get(0).getChain();
        List<Integer> result = actionService.getDiscountAndAllActionsByChain(chainConverter.convert(chain));
        result.forEach(System.out::println);
    }


    @TestConfiguration
    @ComponentScan(basePackages = "naakcii.by.api.action.service")
    static class ActionServiceTestConfig {
        @Bean
        public ActionService actionService() {
            return new ActionServiceImpl();
        }

        @Bean
        public ChainConverter chainConverter() {
            return new ChainConverter();
        }
    }
}
