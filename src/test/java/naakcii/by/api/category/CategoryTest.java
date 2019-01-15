package naakcii.by.api.category;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import naakcii.by.api.subcategory.Subcategory;

public class CategoryTest {
	
	private static Validator validator;
	
	@BeforeClass
	public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
	
	public void createSubcategories(Category category) {
		Subcategory firstSubcategory = new Subcategory("First subcategory", category, true);
		Subcategory secondSubcategory = new Subcategory("Second subcategory", category, true);
		Subcategory thirdSubcategory = new Subcategory("Third subcategory", category, true);
	}
	
	public void createInvalidSubcategories(Category category) {
		Subcategory firstSubcategory = new Subcategory("First subcategory", category, null);
		Subcategory secondSubcategory = new Subcategory(null, category, true);
		Subcategory thirdSubcategory = new Subcategory("Third subcategory", category, true);
	}
	
	@Test
	public void test_category_name_is_null() {
		Category category = new Category(null, true);
		createSubcategories(category);
		Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as category name is null:", 1, constraintViolations.size());
        assertEquals("Name of the category mustn't be null.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_category_name_is_too_short() {
		Category category = new Category("Ca", true);
		createSubcategories(category);
		Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as category name is too short:", 1, constraintViolations.size());
		assertEquals("Name of the category 'Ca' must be between '3' and '100' characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_category_trimmed_name_is_too_short() {
		Category category = new Category("  N  ", true);
		createSubcategories(category);
		Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as category trimmed name is too short:", 1, constraintViolations.size());
		assertEquals("Name of the category '  N  ' must be between '3' and '100' characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_category_priority_is_negative() {
		Category category = new Category("Category name", true);
		createSubcategories(category);
		category.setPriority(-10);
		Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as category priority is negative:", 1, constraintViolations.size());
		assertEquals("Priority of the category '-10' must be positive.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_category_isActive_field_is_null() {
		Category category = new Category("Category name", null);
		createSubcategories(category);
		Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as category 'isActive' field is null:", 1, constraintViolations.size());
		assertEquals("Category must have field 'isActive' defined.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_category_set_of_subcategories_contains_null_elements() {
		Category category = new Category("Category", false);
		category.getSubcategories().add(null);
		createSubcategories(category);
		Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as category set of subcategories contains null elements:", 1, constraintViolations.size());
		assertEquals("Subcategory mustn't be null.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_category_set_of_subcategories_contains_invalid_elements() {
		Category category = new Category("Category name", false);
		createInvalidSubcategories(category);
		category.getSubcategories().add(null);
		Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
		assertEquals("Expected size of the ConstraintViolation set should be 3, as category set of subcategories contains invalid elements:", 3, constraintViolations.size());
		List<String> messages = constraintViolations.stream()
				.map((ConstraintViolation<Category> constraintViolation) -> constraintViolation.getMessage())
				.collect(Collectors.toList());	
		assertTrue(messages.contains("Name of the subcategory mustn't be null."));
		assertTrue(messages.contains("Subcategory must have field 'isActive' defined."));
		assertTrue(messages.contains("Subcategory mustn't be null."));
	}
	
	@Test
	public void test_category_is_valid() {
		Category category = new Category("Category name", false);
		createSubcategories(category);
		category.setPriority(5);
		category.setIcon("Path to icon");
		Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
		assertEquals("Expected size of the ConstraintViolation set should be 0, as category is valid:", 0, constraintViolations.size());
	}
}