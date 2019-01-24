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
		category.getSubcategories().add(new Subcategory("Напитки", true, category));
		category.getSubcategories().add(new Subcategory("Соки", true, category));
		category.getSubcategories().add(new Subcategory("Кофе и заменители", true, category));
	}
	
	public void createInvalidSubcategories(Category category) {
		category.getSubcategories().add(new Subcategory("Напитки", null, category));
		category.getSubcategories().add(new Subcategory(null, true, category));
		category.getSubcategories().add(new Subcategory("Кофе и заменители", true, category));
	}
	
	@Test
	public void test_category_name_is_null() {
		Category category = new Category(null, true);
		createSubcategories(category);
		Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as category's name is null:", 1, constraintViolations.size());
        assertEquals("Category's name mustn't be null.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_category_name_is_too_short() {
		Category category = new Category("На", true);
		createSubcategories(category);
		Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as category's name is too short:", 1, constraintViolations.size());
		assertEquals("Category's name 'На' must be between '3' and '50' characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_category_trimmed_name_is_too_short() {
		Category category = new Category("  На  ", true);
		createSubcategories(category);
		Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as category's trimmed name is too short:", 1, constraintViolations.size());
		assertEquals("Category's name '  На  ' must be between '3' and '50' characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_category_priority_is_negative() {
		Category category = new Category("Напитки, кофе, чай, соки", true);
		createSubcategories(category);
		category.setPriority(-10);
		Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as category's priority is negative:", 1, constraintViolations.size());
		assertEquals("Category's priority '-10' must be positive.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_category_isActive_field_is_null() {
		Category category = new Category("Напитки, кофе, чай, соки", null);
		createSubcategories(category);
		Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as category's 'isActive' field is null:", 1, constraintViolations.size());
		assertEquals("Category must have field 'isActive' defined.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_category_set_of_subcategories_contains_null_elements() {
		Category category = new Category("Напитки, кофе, чай, соки", false);
		category.getSubcategories().add(null);
		createSubcategories(category);
		Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as category's set of subcategories contains null elements:", 1, constraintViolations.size());
		assertEquals("Category must have list of subcategories without null elements.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_category_set_of_subcategories_contains_invalid_elements() {
		Category category = new Category("Напитки, кофе, чай, соки", false);
		createInvalidSubcategories(category);
		category.getSubcategories().add(null);
		Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
		assertEquals("Expected size of the ConstraintViolation set should be 3, as category's set of subcategories contains invalid elements:", 3, constraintViolations.size());
		List<String> messages = constraintViolations.stream()
				.map((ConstraintViolation<Category> constraintViolation) -> constraintViolation.getMessage())
				.collect(Collectors.toList());	
		assertTrue(messages.contains("Subcategory's name mustn't be null."));
		assertTrue(messages.contains("Subcategory must have field 'isActive' defined."));
		assertTrue(messages.contains("Category must have list of subcategories without null elements."));
	}
	
	@Test
	public void test_category_is_valid() {
		Category category = new Category("Напитки, кофе, чай, соки", false);
		createSubcategories(category);
		category.setPriority(5);
		category.setIcon("D:/categories/drinks_coffee_tea_juices/icons/drinks_coffee_tea_juices.jpg");
		Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
		assertEquals("Expected size of the ConstraintViolation set should be 0, as category is valid:", 0, constraintViolations.size());
	}
}
