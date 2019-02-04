package naakcii.by.api.product;

import naakcii.by.api.category.Category;
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

    private Product firstProduct;
    private Product secondProduct;

    @Before
    public void setUp() {
        Category category = new Category("Рыба и морепродукты", true);
        testEntityManager.persist(category);
        Subcategory subcategory = new Subcategory("Ягоды, фрукты", true, category);   
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure(UnitCode.KG);
        testEntityManager.persist(unitOfMeasure);
        firstProduct = new Product("1000123456789", "Минтай с/м б/г Вес", unitOfMeasure, true, subcategory);
        secondProduct = new Product("1003732742789", "Хек с/м б/г Вес", unitOfMeasure, true, subcategory);
        testEntityManager.persist(unitOfMeasure);
        testEntityManager.persistAndFlush(category);
        testEntityManager.clear();
    }

    @Test
    public void test_soft_delete() {
        int numberOfUpdatedRows = productRepository.softDelete(firstProduct.getId());
        assertTrue("Number of updated rows in the database should be equal to 1, as 1 entity has been modified.", numberOfUpdatedRows == 1);
    }

    @Test
    public void test_soft_delete_with_wrong_id() {
        int numberOfUpdatedRows = productRepository.softDelete(firstProduct.getId() + 10);
        assertTrue("Number of updated rows in the database should be equal to 0, as nothing has been modified.", numberOfUpdatedRows == 0);
    }

    @Test
    public void test_find_by_name_and_barcode_and_unit_of_measure() {
    	Product expectedProduct = secondProduct;
        Product resultProduct = productRepository.findByNameAndBarcodeAndUnitOfMeasure(secondProduct.getName(), secondProduct.getBarcode(), secondProduct.getUnitOfMeasure()).get();
        assertEquals("Result product should be: {name:'Хек с/м б/г Вес', barcode:1003732742789, unitOfMeasure:{name:'кг', step:0.1}, isActive:true}", expectedProduct, resultProduct);

    }

    @After
    public void tearDown() {
    	firstProduct = null;
    	secondProduct = null;
    }
}
