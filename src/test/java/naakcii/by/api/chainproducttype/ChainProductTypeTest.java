package naakcii.by.api.chainproducttype;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

public class ChainProductTypeTest {
	
private static Validator validator;

	@BeforeClass
	public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
	
	@Test
	public void test_chainProductType_name_is_null() {
		ChainProductType chainProductType = new ChainProductType(null, "good_price");
		Set<ConstraintViolation<ChainProductType>> constraintViolations = validator.validate(chainProductType);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProductType's name is null:", 1, constraintViolations.size());
        assertEquals("ChainProductType's name mustn't be null.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_chainProductType_name_is_too_short() {
		ChainProductType chainProductType = new ChainProductType("Хо", "good_price");
		Set<ConstraintViolation<ChainProductType>> constraintViolations = validator.validate(chainProductType);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProductType's name is too short:", 1, constraintViolations.size());
        assertEquals("ChainProductType's name 'Хо' must be between '3' and '25' characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_chainProductType_trimmed_name_is_too_short() {
		ChainProductType chainProductType = new ChainProductType(" Хо ", "good_price");
		Set<ConstraintViolation<ChainProductType>> constraintViolations = validator.validate(chainProductType);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProductType's trimmed name is too short:", 1, constraintViolations.size());
        assertEquals("ChainProductType's name ' Хо ' must be between '3' and '25' characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_chainProductType_synonym_is_null() {
		ChainProductType chainProductType = new ChainProductType("Хорошая цена", null);
		Set<ConstraintViolation<ChainProductType>> constraintViolations = validator.validate(chainProductType);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProductType's synonym is null:", 1, constraintViolations.size());
        assertEquals("ChainProductType's synonym mustn't be null.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_chainProductType_synonym_is_too_short() {
		ChainProductType chainProductType = new ChainProductType("Хорошая цена", "pr");
		Set<ConstraintViolation<ChainProductType>> constraintViolations = validator.validate(chainProductType);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProductType's synonym is too short:", 1, constraintViolations.size());
        assertEquals("ChainProductType's synonym 'pr' must be between '3' and '25' characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_chainProductType_trimmed_synonym_is_too_short() {
		ChainProductType chainProductType = new ChainProductType("Хорошая цена", " pr ");
		Set<ConstraintViolation<ChainProductType>> constraintViolations = validator.validate(chainProductType);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProductType's trimmed synonym is too short:", 1, constraintViolations.size());
        assertEquals("ChainProductType's synonym ' pr ' must be between '3' and '25' characters long.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_chainProductType_is_valid() {
		ChainProductType chainProductType = new ChainProductType("Хорошая цена", "good_price");
		chainProductType.setTooltip("Хорошая цена.");
		Set<ConstraintViolation<ChainProductType>> constraintViolations = validator.validate(chainProductType);
		assertEquals("Expected size of the ConstraintViolation set should be 0, as action type is valid:", 0, constraintViolations.size());
    }
}
