package naakcii.by.api.product;

import naakcii.by.api.category.Category;
import naakcii.by.api.country.Country;
import naakcii.by.api.country.CountryCode;
import naakcii.by.api.subcategory.Subcategory;
import naakcii.by.api.unitofmeasure.UnitCode;
import naakcii.by.api.unitofmeasure.UnitOfMeasure;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-unit-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @Before
    public void setUp() {
        Category category = new Category("Рыба и морепродукты", true);
        testEntityManager.persist(category);
        Subcategory subcategory = new Subcategory("Ягоды, фрукты", true, category);
        testEntityManager.persist(subcategory);
        Country country = new Country(CountryCode.BG);
        testEntityManager.persist(country);
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure(UnitCode.KG);
        testEntityManager.persist(unitOfMeasure);
        product = new Product("1000123456789", "Минтай с/м б/г Вес", unitOfMeasure, true, subcategory);
        product.setCountryOfOrigin(country);
        testEntityManager.persist(product);
        Product secondProduct = new Product("1003732742789", "Хек с/м б/г Вес", unitOfMeasure, true, subcategory);
        testEntityManager.persist(secondProduct);
        testEntityManager.flush();
    }

    @Test
    public void test_soft_delete() {
        int numberOfUpdatedRows = productRepository.softDelete(product.getId());
        assertTrue("Number of updated rows in the database should be equal to 1, as 1 entity has been modified.", numberOfUpdatedRows == 1);
    }

    @Test
    public void test_soft_delete_with_wrong_id() {
        int numberOfUpdatedRows = productRepository.softDelete(product.getId() + 10);
        assertTrue("Number of updated rows in the database should be equal to 0, as nothing has been modified.", numberOfUpdatedRows == 0);
    }

    @Test
    public void test_find_by_name_and_barcode_and_unit_of_measure() {
        Optional<Product> optionalProduct = productRepository
                .findByNameAndBarcodeAndUnitOfMeasure(product.getName(), product.getBarcode(), product.getUnitOfMeasure());
        assertTrue("Product was found in the database", optionalProduct.isPresent());
        assertEquals("Болгария", optionalProduct.get().getCountryOfOrigin().getName());

    }

    @After
    public void tearDown() {
        product = null;
    }
}
