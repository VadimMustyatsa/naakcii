package naakcii.by.api.chain.repository.model;

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
import naakcii.by.api.product.repository.model.Product;
import naakcii.by.api.subcategory.repository.model.Subcategory;

public class ChainTest {

private static Validator validator;

	public Set<Action> getSetOfActions(Chain chain) {
		Product product = new Product("Product Name", true, new Subcategory("Subcategory name", true));
		Action firstAction = new Action(product, chain, new BigDecimal("5.50"), "Simple type");
		Action secondAction = new Action(product, chain, new BigDecimal("3.75"), "Simple type");
		Set<Action> actions = new HashSet<>();
		actions.add(firstAction);
		actions.add(secondAction);
		return actions;		
	}
	
	public Set<Action> getSetOfActionsWithInvalidElements(Chain chain) {
		Product product = new Product("Product Name", true, new Subcategory("Subcategory name", true));
		Action firstAction = new Action(product, chain, null, "Simple type");
		Action secondAction = new Action(product, chain, new BigDecimal("2.00"), null);
		Action thirdAction = null;
		Set<Action> actions = new HashSet<>();
		actions.add(firstAction);
		actions.add(secondAction);
		actions.add(thirdAction);
		return actions;		
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
        assertEquals("Name of the chain 'Ch' must be between 3 and 255 characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_chain_trimmed_name_is_too_short() {
		Chain chain = new Chain("  Ch  ", "Chain link", false);
		Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chain trimmed name is too short:", 1, constraintViolations.size());
        assertEquals("Name of the chain '  Ch  ' must be between 3 and 255 characters long.", constraintViolations.iterator().next().getMessage());
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
        assertEquals("Link of the chain 'link' must be between 10 and 255 characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_chain_trimmed_link_is_too_short() {
		Chain chain = new Chain("Chain name", "  link  ", true);
		Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chain trimmed link is too short:", 1, constraintViolations.size());
        assertEquals("Link of the chain '  link  ' must be between 10 and 255 characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_chain_set_of_actoins_contains_invalid_elements() {
		Chain chain = new Chain("Chain name", "Chain link", true);
		chain.setActions(getSetOfActionsWithInvalidElements(chain));
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
		chain.setActions(getSetOfActions(chain));
		Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
		assertEquals("Expected size of the ConstraintViolation set should be 0, as chain is valid:", 0, constraintViolations.size());
	}
}
