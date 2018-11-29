package naakcii.by.api.chain;

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
import naakcii.by.api.chain.Chain;
import naakcii.by.api.product.Product;
import naakcii.by.api.subcategory.Subcategory;

public class ChainTest {

private static Validator validator;

	public void createActions(Chain chain) {
		Product product = new Product("1000123456789", "Product Name", true, new Subcategory("Subcategory name", true));
		ActionType actionType = new ActionType("Action type name");
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 10); 
		Action firstAction = new Action(product, chain, new BigDecimal("5.50"), actionType, startDate, endDate);
		Action secondAction = new Action(product, chain, new BigDecimal("3.75"), actionType, startDate, endDate);
	}
	
	public void createInvalidActions(Chain chain) {
		Product product = new Product("1000123456789", "Product Name", true, new Subcategory("Subcategory name", true));
		ActionType actionType = new ActionType("Action type name");
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 10); 
		Action firstAction = new Action(product, chain, null, actionType, startDate, endDate);
		Action secondAction = new Action(product, chain, new BigDecimal("2.00"), null, startDate, endDate);
	}
	
	@BeforeClass
	public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
	
	@Test
	public void test_chain_name_is_null() {
		Chain chain = new Chain(null, "Chain link", false);
		Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chain name is null:", 1, constraintViolations.size());
        assertEquals("Name of the chain mustn't be null.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_chain_name_is_too_short() {
		Chain chain = new Chain("Ch", "Chain link", false);
		Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chain name is too short:", 1, constraintViolations.size());
        assertEquals("Name of the chain 'Ch' must be between '3' and '100' characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_chain_trimmed_name_is_too_short() {
		Chain chain = new Chain("  Ch  ", "Chain link", false);
		Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chain trimmed name is too short:", 1, constraintViolations.size());
        assertEquals("Name of the chain '  Ch  ' must be between '3' and '100' characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_chain_isActive_field_is_null() {
		Chain chain = new Chain("Chain name", "Chain link", null);
		Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chain 'isActive' field is null:", 1, constraintViolations.size());
        assertEquals("Chain must have field 'isActive' defined.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_chain_link_is_null() {
		Chain chain = new Chain("Chain name", null, false);
		Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chain link is null:", 1, constraintViolations.size());
        assertEquals("Chain must have field 'link' defined.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_chain_link_is_too_short() {
		Chain chain = new Chain("Chain name", "link", true);
		Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chain link is too short:", 1, constraintViolations.size());
        assertEquals("Link of the chain 'link' must be between '10' and '75' characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_chain_trimmed_link_is_too_short() {
		Chain chain = new Chain("Chain name", "  link  ", true);
		Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chain trimmed link is too short:", 1, constraintViolations.size());
        assertEquals("Link of the chain '  link  ' must be between '10' and '75' characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_chain_set_of_actoins_contains_invalid_elements() {
		Chain chain = new Chain("Chain name", "Chain link", true);
		createInvalidActions(chain);
		chain.getActions().add(null);
		Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
		assertEquals("Expected size of the ConstraintViolation set should be 3, as chain set of actions contains invalid elements:", 3, constraintViolations.size());
		List<String> messages = constraintViolations.stream()
				.map((ConstraintViolation<Chain> constraintViolation) -> constraintViolation.getMessage())
				.collect(Collectors.toList());
		assertTrue(messages.contains("Discount price of the action product mustn't be null."));
		assertTrue(messages.contains("Action must have type."));
		assertTrue(messages.contains("Action mustn't be null."));
	}
	
	@Test
	public void test_chain_is_valid() {
		Chain chain = new Chain("Chain name", "Chain link", true);
		createActions(chain);
		Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
		assertEquals("Expected size of the ConstraintViolation set should be 0, as chain is valid:", 0, constraintViolations.size());
	}
}
