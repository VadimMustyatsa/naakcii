package naakcii.by.api.product.repository.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import naakcii.by.api.action.repository.model.Action;
import naakcii.by.api.category.repository.model.Category;
import naakcii.by.api.chain.repository.model.Chain;
import naakcii.by.api.subcategory.repository.model.Subcategory;

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
		category.getSubcategories().add(subcategory);
		return subcategory;
	}
	
	public Subcategory getInvalidSubcategory() {
		Subcategory subcategory = new Subcategory("Subcategory name", true);
		return subcategory;
	}
	
	public Set<Action> getSetOfActions(Product product) {
		Chain chain = new Chain("Chain name", "Chain link", true);
		Action firstAction = new Action(product, chain, new BigDecimal("2.25"), "Simple type");
		Action secondAction = new Action(product, chain, new BigDecimal("6.00"), "Simple type");
		Set<Action> actions = new HashSet<>();
		actions.add(firstAction);
		actions.add(secondAction);
		return actions;		
	}
	
	public Set<Action> getSetOfActionsWithInvalidElements(Product product) {
		Chain chain = new Chain("Chain name", "Chain link", true);
		Action firstAction = new Action(product, chain, null, "Simple type");
		Action secondAction = new Action(product, chain, new BigDecimal("6.00"), null);
		Action thirdAction = null;
		Set<Action> actions = new HashSet<>();
		actions.add(firstAction);
		actions.add(secondAction);
		actions.add(thirdAction);
		return actions;		
	}
	
	@Test
	public void test_product_name_is_null() {
		Product product = new Product(null, true, getSubcategory());
		product.getActions().addAll(getSetOfActions(product));
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product name is null:", 1, constraintViolations.size());
        assertEquals("Name of the product mustn't be null.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_name_is_too_short() {
		Product product = new Product("Pr", true, getSubcategory());
		product.getActions().addAll(getSetOfActions(product));
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product name is too short:", 1, constraintViolations.size());
        assertEquals("Name of the product 'Pr' must be between 3 and 255 characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_trimmed_name_is_too_short() {
		Product product = new Product("  Pr  ", true, getSubcategory());
		product.getActions().addAll(getSetOfActions(product));
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product trimmed name is too short:", 1, constraintViolations.size());
        assertEquals("Name of the product '  Pr  ' must be between 3 and 255 characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_isActive_field_is_null() {
		Product product = new Product("Product name", null, getSubcategory());
		product.getActions().addAll(getSetOfActions(product));
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product 'isActive' field is null:", 1, constraintViolations.size());
        assertEquals("Product must have field 'isActive' defined.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_does_not_belong_to_any_subcategory() {
		Product product = new Product("Product name", false, null);
		product.getActions().addAll(getSetOfActions(product));
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product doesn't belong to any category:", 1, constraintViolations.size());
        assertEquals("Product must have subcategory.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_belongs_to_invalid_subcategory() {
		Product product = new Product("Product name", false, getInvalidSubcategory());
		product.getActions().addAll(getSetOfActions(product));
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product belongs to subcategory with null 'category' field:", 1, constraintViolations.size());
        assertEquals("Subcategory must have category.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_quantity_has_too_many_integer_digits() {
		Product product = new Product("Product name", true, getSubcategory());
		product.getActions().addAll(getSetOfActions(product));
		product.setQuantity(new BigDecimal("10000"));
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product quantity has too many integer digits:", 1, constraintViolations.size());
        assertEquals("Quantity of the product '10000' must have up to 4 integer digits and 3 fraction digits.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_quantity_has_too_many_fraction_digits() {
		Product product = new Product("Product name", true, getSubcategory());
		product.getActions().addAll(getSetOfActions(product));
		product.setQuantity(new BigDecimal("0.1000"));
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product quantity has too many fraction digits:", 1, constraintViolations.size());
        assertEquals("Quantity of the product '0.1000' must have up to 4 integer digits and 3 fraction digits.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_quantity_is_negative() {
		Product product = new Product("Product name", true, getSubcategory());
		product.getActions().addAll(getSetOfActions(product));
		product.setQuantity(new BigDecimal("-100"));
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product quantity is negative:", 1, constraintViolations.size());
        assertEquals("Quantity of the product '-100' must be positive.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_set_of_actoins_contains_invalid_elements() {
		Product product = new Product("Product name", false, getSubcategory());
		product.getActions().addAll(getSetOfActionsWithInvalidElements(product));
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
	public void test_product_set_of_actoins_is_null() {
		Product product = new Product("Product name", true, getSubcategory());
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product set of actions is null:", 1, constraintViolations.size());
        assertEquals("Product must have at least one action.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_set_of_actoins_is_empty() {
		Product product = new Product("Product name", true, getSubcategory());
		product.setActions(new HashSet<Action>());
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product set of actions is empty:", 1, constraintViolations.size());
        assertEquals("Product must have at least one action.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_product_is_valid() {
		Product product = new Product("Product name", true, getSubcategory());
		product.getActions().addAll(getSetOfActions(product));
		product.setMeasure("г");
		product.setPicture("Path to picture");
		product.setQuantity(new BigDecimal("1000.500"));
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product is valid:", 0, constraintViolations.size());
	}
}
