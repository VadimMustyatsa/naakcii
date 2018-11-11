package naakcii.by.api.subcategory.repository.model;

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
import naakcii.by.api.product.repository.model.Product;

public class SubcategoryTest {
	
	private static Validator validator;
	
	@BeforeClass
	public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
	
	public Category getCategory(Subcategory subcategory) {
		Set<Subcategory> subcategories = new HashSet<>(); 
		subcategories.add(subcategory);
		return new Category("Category name", subcategories, true);
	}
	
	public Category getInvalidCategory(Subcategory subcategory) {
		Set<Subcategory> subcategories = new HashSet<>(); 
		subcategories.add(subcategory);
		return new Category("Category name", subcategories, null);
	}

	public Set<Product> getSetOfProducts(Subcategory subcategory) {
		Chain chain = new Chain("Chain name", "Chain link", true);
		Product firstProduct = new Product("Name of the first product", true, subcategory);
		Product secondProduct = new Product("Name of the second product", true, subcategory);
		Action firstAction = new Action(firstProduct, chain, new BigDecimal("7.50"), "Simple type");
		Action secondAction = new Action(secondProduct, chain, new BigDecimal("5.25"), "Simple type");
		chain.getActions().add(firstAction);
		chain.getActions().add(secondAction);
		firstProduct.getActions().add(firstAction);
		secondProduct.getActions().add(secondAction);
		Set<Product> products = new HashSet<>();
		products.add(firstProduct);
		products.add(secondProduct);
		return products;
	}

	public Set<Product> getSetOfProductsWithInvalidElements(Subcategory subcategory) {
		Chain chain = new Chain("Chain name", "Chain link", true);
		Product firstProduct = new Product(null, true, subcategory);
		Product secondProduct = new Product("Name of the second product", null, subcategory);
		Product thirdProduct = null;
		Action firstAction = new Action(firstProduct, chain, new BigDecimal("7.50"), "Simple type");
		Action secondAction = new Action(secondProduct, chain, new BigDecimal("5.25"), "Simple type");
		chain.getActions().add(firstAction);
		chain.getActions().add(secondAction);
		firstProduct.getActions().add(firstAction);
		secondProduct.getActions().add(secondAction);
		Set<Product> products = new HashSet<>();
		products.add(firstProduct);
		products.add(secondProduct);
		products.add(thirdProduct);
		return products;
	}

	@Test
	public void test_subcategory_name_is_null() {
		Subcategory subcategory = new Subcategory(null, true);
		subcategory.setCategory(getCategory(subcategory));
		Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as subcategory name is null:", 1, constraintViolations.size());
        assertEquals("Name of the subcategory mustn't be null.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_subcategory_name_too_short() {
		Subcategory subcategory = new Subcategory("Su", true);
		subcategory.setCategory(getCategory(subcategory));
		Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as subcategory name is too short:", 1, constraintViolations.size());
        assertEquals("Name of the subcategory 'Su' must be between 3 and 255 characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_subcategory_trimmed_name_too_short() {
		Subcategory subcategory = new Subcategory("  Su  ", true);
		subcategory.setCategory(getCategory(subcategory));
		Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as subcategory trimmed name is too short:", 1, constraintViolations.size());
        assertEquals("Name of the subcategory '  Su  ' must be between 3 and 255 characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_subcategory_priority_is_negative() {
		Subcategory subcategory = new Subcategory("Subcategory name", true);
		subcategory.setCategory(getCategory(subcategory));
		subcategory.setPriority(-1);
		Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as subcategory priority is negative:", 1, constraintViolations.size());
        assertEquals("Priority of the subcategory '-1' must be positive.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_subcategory_isActive_field_is_null() {
		Subcategory subcategory = new Subcategory("Subcategory name", null);
		subcategory.setCategory(getCategory(subcategory));
		Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as subcategory 'isActive' field is null:", 1, constraintViolations.size());
        assertEquals("Subcategory must have field 'isActive' defined.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_subcategory_does_not_belong_to_any_category() {
		Subcategory subcategory = new Subcategory("Subcategory name", false);
		Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as subcategory doesn't belong to any category:", 1, constraintViolations.size());
        assertEquals("Subcategory must have category.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_subcategory_belongs_to_invalid_category() {
		Subcategory subcategory = new Subcategory("Subcategory name", false);
		subcategory.setCategory(getInvalidCategory(subcategory));
		Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as subcategory belongs to category with null 'isActive' field:", 1, constraintViolations.size());
        assertEquals("Category must have field 'isActive' defined.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_subcategory_set_of_products_contains_invalid_elements() {
		Subcategory subcategory = new Subcategory("Subcategory name", true);
		subcategory.setCategory(getCategory(subcategory));
		subcategory.setProducts(getSetOfProductsWithInvalidElements(subcategory));
		Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
		assertEquals("Expected size of the ConstraintViolation set should be 2, as subcategory set of products contains invalid elements:", 3, constraintViolations.size());
		List<String> messages = constraintViolations.stream()
				.map((ConstraintViolation<Subcategory> constraintViolation) -> constraintViolation.getMessage())
				.collect(Collectors.toList());
		assertTrue(messages.contains("Product must have field 'isActive' defined."));
		assertTrue(messages.contains("Name of the product mustn't be null."));
		assertTrue(messages.contains("Product mustn't be null."));
	}
		
	@Test
	public void test_subcategory_is_valid() {
		Subcategory subcategory = new Subcategory("Subcategory name", true);
		subcategory.setCategory(getCategory(subcategory));
		subcategory.setProducts(getSetOfProducts(subcategory));
		subcategory.setPriority(5);
		Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
		assertEquals("Expected size of the ConstraintViolation set should be 0, as subcategory is valid:", 0, constraintViolations.size());
	}
}
