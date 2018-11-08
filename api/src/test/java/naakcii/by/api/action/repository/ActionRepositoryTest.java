package naakcii.by.api.action.repository;

import naakcii.by.api.action.repository.model.Action;
import naakcii.by.api.category.repository.model.Category;
import naakcii.by.api.chain.repository.model.Chain;
import naakcii.by.api.product.repository.model.Product;
import naakcii.by.api.subcategory.repository.model.Subcategory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ActionRepositoryTest {

    @Autowired
    ActionRepository actionRepository;

    @Autowired
    TestEntityManager entityManager;

    private Subcategory firstSubcategory;
    private Subcategory secondSubcategory;
    private Product firstProduct;
    private Chain firstChain;
    private Calendar testDate = new Calendar.Builder()
            .setDate(2018, 6, 1)
            .build();

    @Before
    public void setUp() {
        Category category = new Category("firstCategory", true);
        entityManager.persist(category);
        firstSubcategory = new Subcategory("firstSubcategory", true, category);
        secondSubcategory = new Subcategory("secondSubcategory", true, category);
        entityManager.persist(firstSubcategory);
        entityManager.persist(secondSubcategory);
        firstProduct = new Product("firstProduct", true, firstSubcategory);
        Product secondProduct = new Product("secondProduct", true, secondSubcategory);
        entityManager.persist(firstProduct);
        entityManager.persist(secondProduct);
        firstChain = new Chain("firstChain", "firstLink", true);
        Chain secondChain = new Chain("secondChain", "secondLink", true);
        entityManager.persist(firstChain);
        entityManager.persist(secondChain);
        Action firstAction = new Action(
                firstProduct,
                firstChain,
                "1+1",
                new Calendar.Builder().setDate(2018, 1, 1).build(),
                new Calendar.Builder().setDate(2018, 12, 1).build()
        );
        Action secondAction = new Action(
                firstProduct,
                secondChain,
                "1+1",
                new Calendar.Builder().setDate(2018, 1, 1).build(),
                new Calendar.Builder().setDate(2018, 12, 1).build()
        );
        Action thirdAction = new Action(
                secondProduct,
                firstChain,
                "1+1",
                new Calendar.Builder().setDate(2018, 1, 1).build(),
                new Calendar.Builder().setDate(2018, 12, 1).build()
        );
        Action fourthAction = new Action(
                secondProduct,
                secondChain,
                "1+1",
                new Calendar.Builder().setDate(2018, 1, 1).build(),
                new Calendar.Builder().setDate(2018, 12, 1).build()
        );
        entityManager.persist(firstAction);
        entityManager.persist(secondAction);
        entityManager.persist(thirdAction);
        entityManager.persist(fourthAction);
        entityManager.flush();
    }

    @Test
    public void findAllBySubcategoryId() {
        List<Action> actionList = actionRepository.findAllBySubcategoryId(firstSubcategory.getId(), testDate);
        assertThat(actionList.size()).isEqualTo(2);
        assertThat(actionList.get(0).getProduct().getName()).isEqualTo("firstProduct");
        assertThat(actionList.get(1).getProduct().getName()).isEqualTo("firstProduct");
    }

    @Test
    public void findAllBySubcategoriesIds() {
        Set<Long> idSet = new HashSet<Long>();
        idSet.add(firstSubcategory.getId());
        idSet.add(secondSubcategory.getId());
        List<Action> actionList = actionRepository.findAllBySubcategoriesIds(idSet, testDate);
        assertThat(actionList.size()).isEqualTo(4);
    }

    @Test
    public void findAllByChain() {
        List<Action> actionList = actionRepository.findAllByChain(firstChain);
        assertThat(actionList.size()).isEqualTo(2);
        assertThat(actionList.get(0).getProduct().getName()).isEqualTo("firstProduct");
        assertThat(actionList.get(1).getProduct().getName()).isEqualTo("secondProduct");
    }

    @Test
    public void findAllByProductId() {
        List<Action> actionList = actionRepository.findAllByProductId(firstProduct.getId());
        assertThat(actionList.get(0).getChain().getName()).isEqualTo("firstChain");
        assertThat(actionList.get(1).getChain().getName()).isEqualTo("secondChain");
    }
}