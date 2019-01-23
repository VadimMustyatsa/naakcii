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

import naakcii.by.api.category.Category;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.chainproduct.ChainProduct;
import naakcii.by.api.chainproducttype.ChainProductType;
import naakcii.by.api.country.Country;
import naakcii.by.api.country.CountryCode;
import naakcii.by.api.subcategory.Subcategory;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-unit-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProductRepositoryTest {
	/*
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
		ChainProductType actionType = new ChainProductType("Action type name");
		actionType.setTooltip("Action type tooltip text.");
		Country country = new Country(CountryCode.BG);
		product = new Product("1000123456789", "Product name", Unit.KG, true, subcategory);
		product.setUnit(Unit.KG);
		product.setPicture("Product picture");
		product.setManufacturer("Manufacturer");
		product.setBrand("Brand");
		product.setCountryOfOrigin(country);
		testEntityManager.persist(chain);
		testEntityManager.persist(category);
		testEntityManager.persist(actionType);
		testEntityManager.persist(country);
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.MONTH, 1);
		ChainProduct action = new ChainProduct(product, chain, new BigDecimal("7.50"), actionType, startDate, endDate);
		action.setDiscountPercent(new BigDecimal("25"));
		action.setBasePrice(new BigDecimal("10.00"));
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
	}*/
}
