package naakcii.by.api.product;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;

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

import naakcii.by.api.action.Action;
import naakcii.by.api.actiontype.ActionType;
import naakcii.by.api.category.Category;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.subcategory.Subcategory;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
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
		Category category = new Category("Category name", true);
		category.setIcon("Category icon");
		category.setPriority(5);
		Subcategory subcategory = new Subcategory("Subcategory name", category, true);
		subcategory.setPriority(10);
		Chain chain = new Chain("Chain name", "Chain link", true);
		chain.setLogo("Chain logo");
		chain.setLogoSmall("Chain small logo");
		ActionType actionType = new ActionType("Action type name");
		actionType.setIcon("Action type icon");
		product = new Product("1000123456789", "Product name", true, subcategory);
		product.setMeasure("Unit of measure");
		product.setPicture("Product picture");
		product.setQuantity(new BigDecimal("100.00"));
		testEntityManager.persist(chain);
		testEntityManager.persist(category);
		testEntityManager.persist(actionType);
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.MONTH, 1);
		Action action = new Action(product, chain, new BigDecimal("7.50"), actionType, startDate, endDate);
		action.setDiscount(new BigDecimal("25"));
		action.setPrice(new BigDecimal("10.00"));
		testEntityManager.flush();
		testEntityManager.detach(actionType);
		testEntityManager.detach(category);
		testEntityManager.detach(chain);
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
	
	@After
	public void tearDown() {
		product = null;
	}
}
