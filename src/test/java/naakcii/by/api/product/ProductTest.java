package naakcii.by.api.product;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import naakcii.by.api.action.Action;
import naakcii.by.api.actiontype.ActionType;
import naakcii.by.api.category.Category;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.product.Product;
import naakcii.by.api.subcategory.Subcategory;

public class ProductTest {

private static Validator validator;
	
	@BeforeClass
	public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
	
	public Subcategory getSubcategory() {
		Category category = new Category("Category name", true);
		Subcategory subcategory = new Subcategory("Subcategory name", category, true);
		return subcategory;
	}
	
	public Subcategory getInvalidSubcategory() {
		Subcategory subcategory = new Subcategory("Subcategory name", true);
		return subcategory;
	}
	
	public void createActions(Product product) {
		Chain chain = new Chain("Chain name", "Chain link", true);
		ActionType actionType = new ActionType("Action type name");
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 15); 
		Action firstAction = new Action(product, chain, new BigDecimal("2.25"), actionType, startDate, endDate);
		Action secondAction = new Action(product, chain, new BigDecimal("6.00"), actionType, startDate, endDate);
	}
	
	public void createInvalidActions(Product product) {
		Chain chain = new Chain("Chain name", "Chain link", true);
		ActionType actionType = new ActionType("Action type name");
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 15); 
		Action firstAction = new Action(product, chain, null, actionType, startDate, endDate);
		Action secondAction = new Action(product, chain, new BigDecimal("6.00"), null, startDate, endDate);
	}
	
	@Test
	public void test_product_barcode_is_null() {
		Product product = new Product(null, "Product name", true, getSubcategory());
		createActions(product);
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product barcode is null:", 1, constraintViolations.size());
        assertEquals("Barcode of the product mustn't be null.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_barcode_is_too_short() {
		Product product = new Product("123", "Product name", true, getSubcategory());
		createActions(product);
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product barcode is too short:", 1, constraintViolations.size());
        assertEquals("Barcode of the product '123' must be between '4' and '14' characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_barcode_does_not_contain_only_digits() {
		Product product = new Product("  100ax", "Product name", true, getSubcategory());
		createActions(product);
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product barcode doesn't contain only digits:", 1, constraintViolations.size());
        assertEquals("Barcode of the product must contain only digits.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_name_is_null() {
		Product product = new Product("1000123456789", null, true, getSubcategory());
		createActions(product);
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product name is null:", 1, constraintViolations.size());
        assertEquals("Name of the product mustn't be null.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_name_is_too_short() {
		Product product = new Product("1000123456789", "Pr", true, getSubcategory());
		createActions(product);
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product name is too short:", 1, constraintViolations.size());
        assertEquals("Name of the product 'Pr' must be between '3' and '100' characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_trimmed_name_is_too_short() {
		Product product = new Product("1000123456789", "  Pr  ", true, getSubcategory());
		createActions(product);
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product trimmed name is too short:", 1, constraintViolations.size());
        assertEquals("Name of the product '  Pr  ' must be between '3' and '100' characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_isActive_field_is_null() {
		Product product = new Product("1000123456789", "Product name", null, getSubcategory());
		createActions(product);
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product 'isActive' field is null:", 1, constraintViolations.size());
        assertEquals("Product must have field 'isActive' defined.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_does_not_belong_to_any_subcategory() {
		Product product = new Product("1000123456789", "Product name", false);
		createActions(product);
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product doesn't belong to any category:", 1, constraintViolations.size());
        assertEquals("Product must have subcategory.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_belongs_to_invalid_subcategory() {
		Product product = new Product("1000123456789", "Product name", false, getInvalidSubcategory());
		createActions(product);
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product belongs to subcategory with null 'category' field:", 1, constraintViolations.size());
        assertEquals("Subcategory must have category.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_set_of_actoins_contains_invalid_elements() {
		Product product = new Product("1000123456789", "Product name", false, getSubcategory());
		createInvalidActions(product);
		product.getActions().add(null);
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 3, as product set of actions contains invalid elements:", 3, constraintViolations.size());
		List<String> messages = constraintViolations.stream()
				.map((ConstraintViolation<Product> constraintViolation) -> constraintViolation.getMessage())
				.collect(Collectors.toList());
		assertTrue(messages.contains("Discount price of the action product mustn't be null."));
		assertTrue(messages.contains("Action must have type."));
		assertTrue(messages.contains("Action mustn't be null."));
	}
	
	@Test
	public void test_product_is_valid() {
		Product product = new Product("1000123456789", "Product name", true, getSubcategory());
		createActions(product);
		product.setUnit(Unit.KG);
		product.setPicture("Path to picture");
		product.setManufacturer("Manufacturer");
		product.setBrand("Brand");
		product.setCountryOfOrigin("Country of origin");
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product is valid:", 0, constraintViolations.size());
	}
}
