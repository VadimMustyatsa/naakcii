package naakcii.by.api.unitofmeasure;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class UnitOfMeasureTest {

    public static Validator validator;

    public BigDecimal step;

    @Before
    public void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        step = new BigDecimal(1);
    }

    @Test
    public void test_unit_of_measure_name_is_null() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setStep(step);
        Set<ConstraintViolation<UnitOfMeasure>> validate = validator.validate(unitOfMeasure);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as unit of measure name is null:", 1, validate.size());
        assertEquals("UoM's name mustn't be null.", validate.iterator().next().getMessage());
    }

    @Test
    public void test_unit_of_measure_name_is_too_short() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setName("A");
        unitOfMeasure.setStep(step);
        Set<ConstraintViolation<UnitOfMeasure>> validate = validator.validate(unitOfMeasure);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as unit of measure name is too short:", 1, validate.size());
        assertEquals("UOM's name 'A' must be between '2' and '10' characters long.", validate.iterator().next().getMessage());
    }

    @Test
    public void test_unit_of_measure_step_is_null() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setName("кг");
        Set<ConstraintViolation<UnitOfMeasure>> validate = validator.validate(unitOfMeasure);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as unit of measure step is null:", 1, validate.size());
        assertEquals("UoM's step mustn't be null.", validate.iterator().next().getMessage());
    }

    @Test
    public void test_unit_of_measure_step_has_too_much_integer_digits() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setName("кг");
        unitOfMeasure.setStep(new BigDecimal(1111));
        Set<ConstraintViolation<UnitOfMeasure>> validate = validator.validate(unitOfMeasure);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as unit of measure step has too much integer digits:", 1, validate.size());
        assertEquals("UOM's step '1111' must have up to '3' integer digits and '3' fraction digits.", validate.iterator().next().getMessage());
    }

    @Test
    public void test_unit_of_measure_step_has_too_much_fraction_digits() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setName("кг");
        BigDecimal step = new BigDecimal("1.1112");
        unitOfMeasure.setStep(step);
        Set<ConstraintViolation<UnitOfMeasure>> validate = validator.validate(unitOfMeasure);
        assertEquals("Expected size of the ConstraintViolation set should be 1, asunit of measure step has too much fraction digits:", 1, validate.size());
        assertEquals("UOM's step '1.1112' must have up to '3' integer digits and '3' fraction digits.", validate.iterator().next().getMessage());
    }

    @Test
    public void test_unit_of_measure_step_is_negative() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setName("кг");
        unitOfMeasure.setStep(new BigDecimal(-10));
        Set<ConstraintViolation<UnitOfMeasure>> validate = validator.validate(unitOfMeasure);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as unit of measure step is negative:", 1, validate.size());
        assertEquals("UOM's step '-10' must be positive.", validate.iterator().next().getMessage());
    }

}
