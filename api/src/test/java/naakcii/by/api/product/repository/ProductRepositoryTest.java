package naakcii.by.api.product.repository;

import naakcii.by.api.category.repository.model.Category;
import naakcii.by.api.product.repository.model.Product;
import naakcii.by.api.subcategory.repository.model.Subcategory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    TestEntityManager entityManager;

    private Subcategory secondSubcategory;
    private Product firstProduct;

    @Before
    public void setUp() {
        Category category = new Category("firstCategory", true);
        entityManager.persist(category);
        Subcategory firstSubcategory = new Subcategory("firstSubcategory", true, category);
        secondSubcategory = new Subcategory("secondSubcategory", true, category);
        entityManager.persist(firstSubcategory);
        entityManager.persist(secondSubcategory);
        firstProduct = new Product("firstProduct", true, firstSubcategory);
        entityManager.persist(firstProduct);
    }

    @Test
    public void softDelete() {
        productRepository.softDelete(firstProduct.getId());
        entityManager.refresh(firstProduct);
        entityManager.flush();
        assertThat(firstProduct.isActive()).isEqualTo(false);
    }

    @Test
    public void getBySubcategoryId() {
        Product secondProduct = new Product("secondProduct", true, secondSubcategory);
        Product thirdProduct = new Product("thirdProduct", true, secondSubcategory);
        entityManager.persist(firstProduct);
        entityManager.persist(secondProduct);
        entityManager.persist(thirdProduct);
        entityManager.flush();
        List<Product> productList = productRepository.getBySubcategoryId(secondSubcategory.getId());
        assertThat(productList.size()).isEqualTo(2);
        assertThat(productList.get(0).getName()).isEqualTo("secondProduct");
        assertThat(productList.get(1).getName()).isEqualTo("thirdProduct");
    }
}