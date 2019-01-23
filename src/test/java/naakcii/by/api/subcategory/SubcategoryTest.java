package naakcii.by.api.subcategory;

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

import naakcii.by.api.category.Category;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.chainproduct.ChainProduct;
import naakcii.by.api.chainproducttype.ChainProductType;
import naakcii.by.api.product.Product;
import naakcii.by.api.subcategory.Subcategory;

public class SubcategoryTest {
/*	
	private static Validator validator;
	
	@BeforeClass
	public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
	
	public Category getCategory(Subcategory subcategory) {
		Category category = new Category("Category name", true);
		category.getSubcategories().add(subcategory);
		return category;
	}
	
	public Category getInvalidCategory(Subcategory subcategory) {
		Category category = new Category("Category name", null);
		category.getSubcategories().add(subcategory);
		return category;
	}

	public void createProducts(Subcategory subcategory) {
		Chain chain = new Chain("Chain name", "Chain link", true);
		Product firstProduct = new Product("1000123456789", "Name of the first product", Unit.PC, true, subcategory);
		Product secondProduct = new Product("1000123456789", "Name of the second product", Unit.KG, true, subcategory);
		ChainProductType actionType = new ChainProductType("Action type name");
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 5); 
		ChainProduct firstAction = new ChainProduct(firstProduct, chain, new BigDecimal("7.50"), actionType, startDate, endDate);
		ChainProduct secondAction = new ChainProduct(secondProduct, chain, new BigDecimal("5.25"), actionType, startDate, endDate);
	}

	public void createInvalidProducts(Subcategory subcategory) {
		Chain chain = new Chain("Chain name", "Chain link", true);
		Product firstProduct = new Product("1000123456789", null, Unit.PC, true, subcategory);
		Product secondProduct = new Product("1000123456789", "Name of the second product", Unit.KG, null, subcategory);
		ChainProductType actionType = new ChainProductType("Action type name");
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 5); 
		ChainProduct firstAction = new ChainProduct(firstProduct, chain, new BigDecimal("7.50"), actionType, startDate, endDate);
		ChainProduct secondAction = new ChainProduct(secondProduct, chain, new BigDecimal("5.25"), actionType, startDate, endDate);
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
        assertEquals("Name of the subcategory 'Su' must be between '3' and '100' characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_subcategory_trimmed_name_too_short() {
		Subcategory subcategory = new Subcategory("  Su  ", true);
		subcategory.setCategory(getCategory(subcategory));
		Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as subcategory trimmed name is too short:", 1, constraintViolations.size());
        assertEquals("Name of the subcategory '  Su  ' must be between '3' and '100' characters long.", constraintViolations.iterator().next().getMessage());
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
		createInvalidProducts(subcategory);
		subcategory.getProducts().add(null);
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
		createProducts(subcategory);
		subcategory.setPriority(5);
		Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
		assertEquals("Expected size of the ConstraintViolation set should be 0, as subcategory is valid:", 0, constraintViolations.size());
	}*/
}
