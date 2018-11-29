package naakcii.by.actiontype;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import naakcii.by.api.actiontype.ActionType;

public class ActionTypeTest {
	
private static Validator validator;
	
	@BeforeClass
	public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
	
	@Test
	public void test_action_type_name_is_null() {
		ActionType actionType = new ActionType();
		Set<ConstraintViolation<ActionType>> constraintViolations = validator.validate(actionType);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action type name is null:", 1, constraintViolations.size());
        assertEquals("Name of the action type mustn't be null.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_type_name_is_too_short() {
		ActionType actionType = new ActionType("Ac");
		Set<ConstraintViolation<ActionType>> constraintViolations = validator.validate(actionType);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action type name is too short:", 1, constraintViolations.size());
        assertEquals("Name of the action type 'Ac' must be between '3' and '100' characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_type_trimmed_name_is_too_short() {
		ActionType actionType = new ActionType(" Ac ");
		Set<ConstraintViolation<ActionType>> constraintViolations = validator.validate(actionType);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action type trimmed name is too short:", 1, constraintViolations.size());
        assertEquals("Name of the action type ' Ac ' must be between '3' and '100' characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_type_is_valid() {
		ActionType actionType = new ActionType("Action type name");
		actionType.setIcon("Path to icon");
		Set<ConstraintViolation<ActionType>> constraintViolations = validator.validate(actionType);
		assertEquals("Expected size of the ConstraintViolation set should be 0, as action type is too valid:", 0, constraintViolations.size());
    }
}
