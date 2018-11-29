package naakcii.by.api.action;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Set;

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

public class ActionTest {
	
	private static Validator validator;
	
	@BeforeClass
	public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
	
	public ActionType getActionType() {
		return new ActionType("Action type name");
	}
	
	public Calendar getStartDate() {
		return Calendar.getInstance();
	}
	
	public Calendar getEndDate() {
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.MONTH, 1);
		return endDate;
	}
	
	@Test
	public void test_action_price_has_too_many_fraction_digits() {
		Action action = new Action(new Product(), new Chain(), new BigDecimal("5.25"), getActionType(), getStartDate(), getEndDate());
		action.setPrice(new BigDecimal("10.575"));
		Set<ConstraintViolation<Action>> constraintViolations = validator.validate(action);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action price has too many fraction digits:", 1, constraintViolations.size());
        assertEquals("Price of the action product '10.575' must have up to '2' integer digits and '2' fraction digits.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_price_is_too_high() {
		Action action = new Action(new Product(), new Chain(), new BigDecimal("5.25"), getActionType(), getStartDate(), getEndDate());
		action.setPrice(new BigDecimal("27.50"));
		Set<ConstraintViolation<Action>> constraintViolations = validator.validate(action);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action price is too high:", 1, constraintViolations.size());
        assertEquals("Price of the action product '27.50' must be lower than '25'.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_price_is_too_low() {
		Action action = new Action(new Product(), new Chain(), new BigDecimal("5.25"), getActionType(), getStartDate(), getEndDate());
		action.setPrice(new BigDecimal("0.15"));
		Set<ConstraintViolation<Action>> constraintViolations = validator.validate(action);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action price is too low:", 1, constraintViolations.size());
        assertEquals("Price of the action product '0.15' must be higher than '0.20'.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_discount_has_too_many_fraction_digits() {
		Action action = new Action(new Product(), new Chain(), new BigDecimal("5.25"), getActionType(), getStartDate(), getEndDate());
		action.setDiscount(new BigDecimal("25.50"));
		Set<ConstraintViolation<Action>> constraintViolations = validator.validate(action);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action discount has too many fraction digits:", 1, constraintViolations.size());
        assertEquals("Discount of the action product '25.50' must have up to '2' integer digits and '0' fraction digits.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_discount_is_too_high() {
		Action action = new Action(new Product(), new Chain(), new BigDecimal("5.25"), getActionType(), getStartDate(), getEndDate());
		action.setDiscount(new BigDecimal("75"));
		Set<ConstraintViolation<Action>> constraintViolations = validator.validate(action);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action discount is too high:", 1, constraintViolations.size());
        assertEquals("Discount of the action product '75' must be lower than '50'.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_discount_is_negative() {
		Action action = new Action(new Product(), new Chain(), new BigDecimal("5.25"), getActionType(), getStartDate(), getEndDate());
		action.setDiscount(new BigDecimal("-10"));
		Set<ConstraintViolation<Action>> constraintViolations = validator.validate(action);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action discount is negative:", 1, constraintViolations.size());
        assertEquals("Discount of the action product '-10' mustn't be negative.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_discount_price_is_null() {
		Action action = new Action(new Product(), new Chain(), null, getActionType(), getStartDate(), getEndDate());
		Set<ConstraintViolation<Action>> constraintViolations = validator.validate(action);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action discount price is null:", 1, constraintViolations.size());
        assertEquals("Discount price of the action product mustn't be null.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_discount_price_has_too_many_fraction_digits() {
		Action action = new Action(new Product(), new Chain(), new BigDecimal("5.575"), getActionType(), getStartDate(), getEndDate());
		Set<ConstraintViolation<Action>> constraintViolations = validator.validate(action);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action discount price has too many fraction digits:", 1, constraintViolations.size());
        assertEquals("Discount price of the action product '5.575' must have up to '2' integer digits and '2' fraction digits.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_discount_price_is_too_high() {
		Action action = new Action(new Product(), new Chain(), new BigDecimal("30.50"), getActionType(), getStartDate(), getEndDate());
		Set<ConstraintViolation<Action>> constraintViolations = validator.validate(action);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action discount price is too high:", 1, constraintViolations.size());
        assertEquals("Discount price of the action product '30.50' must be lower than '25'.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_discount_price_is_too_low() {
		Action action = new Action(new Product(), new Chain(), new BigDecimal("0.05"), getActionType(), getStartDate(), getEndDate());
		Set<ConstraintViolation<Action>> constraintViolations = validator.validate(action);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action discount price is too low:", 1, constraintViolations.size());
        assertEquals("Discount price of the action product '0.05' must be higher than '0.20'.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_has_not_chain() {
		Action action = new Action();
		action.setProduct(new Product());
		action.setDiscountPrice(new BigDecimal("5.25"));
		action.setType(getActionType());
		action.setStartDate(getStartDate());
		action.setEndDate(getEndDate());
		Set<ConstraintViolation<Action>> constraintViolations = validator.validate(action);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action has not chain:", 1, constraintViolations.size());
        assertEquals("Action must have chain.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_has_not_product() {
		Action action = new Action();
		action.setChain(new Chain());
		action.setDiscountPrice(new BigDecimal("5.25"));
		action.setType(getActionType());
		action.setStartDate(getStartDate());
		action.setEndDate(getEndDate());
		Set<ConstraintViolation<Action>> constraintViolations = validator.validate(action);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action has not product:", 1, constraintViolations.size());
        assertEquals("Action must have product.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_type_is_null() {
		Action action = new Action(new Product(), new Chain(), new BigDecimal("5.25"), null, getStartDate(), getEndDate());
		Set<ConstraintViolation<Action>> constraintViolations = validator.validate(action);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action type is null:", 1, constraintViolations.size());
        assertEquals("Action must have type.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_type_is_invalid() {
		ActionType actionType = new ActionType();
		Action action = new Action(new Product(), new Chain(), new BigDecimal("5.25"), actionType, getStartDate(), getEndDate());
		Set<ConstraintViolation<Action>> constraintViolations = validator.validate(action);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action type is invalid:", 1, constraintViolations.size());
        assertEquals("Name of the action type mustn't be null.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_start_date_is_null() {
		Action action = new Action(new Product(), new Chain(), new BigDecimal("15.25"), getActionType(), null, getEndDate());
		Set<ConstraintViolation<Action>> constraintViolations = validator.validate(action);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action start date is null:", 1, constraintViolations.size());
		assertEquals("Action must have have start date.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_end_date_is_null() {
		Action action = new Action(new Product(), new Chain(), new BigDecimal("15.25"), getActionType(), getStartDate(), null);
		Set<ConstraintViolation<Action>> constraintViolations = validator.validate(action);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action end date is null:", 1, constraintViolations.size());
		assertEquals("Action must have end date.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_end_date_is_in_the_past() {
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.MONTH, -1);
		Action action = new Action(new Product(), new Chain(), new BigDecimal("5.25"), getActionType(), getStartDate(), endDate);
		Set<ConstraintViolation<Action>> constraintViolations = validator.validate(action);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as action end date is in the past:", 1, constraintViolations.size());
        assertEquals("End date of the action must be in the future.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_action_is_valid() {
		Action action = new Action(new Product(), new Chain(), new BigDecimal("15.25"), getActionType(), getStartDate(), getEndDate());
		action.setPrice(new BigDecimal("20.75"));
		action.setDiscount(new BigDecimal("10"));
		Set<ConstraintViolation<Action>> constraintViolations = validator.validate(action);
		assertEquals("Expected size of the ConstraintViolation set should be 0, as action is valid:", 0, constraintViolations.size());
	}
}
