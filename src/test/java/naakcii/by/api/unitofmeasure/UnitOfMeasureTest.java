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

    @Before
    public void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void test_unit_of_measure_name_is_null() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setStep(new BigDecimal("1.0"));
        Set<ConstraintViolation<UnitOfMeasure>> validate = validator.validate(unitOfMeasure);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as UoM's name is null:", 1, validate.size());
        assertEquals("UoM's name mustn't be null.", validate.iterator().next().getMessage());
    }

    @Test
    public void test_unit_of_measure_name_is_too_short() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setName("A");
        unitOfMeasure.setStep(new BigDecimal("1.0"));
        Set<ConstraintViolation<UnitOfMeasure>> validate = validator.validate(unitOfMeasure);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as UoM's name is too short:", 1, validate.size());
        assertEquals("UOM's name 'A' must be between '2' and '10' characters long.", validate.iterator().next().getMessage());
    }

    @Test
    public void test_unit_of_measure_step_is_null() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setName("кг");
        Set<ConstraintViolation<UnitOfMeasure>> validate = validator.validate(unitOfMeasure);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as UoM's step is null:", 1, validate.size());
        assertEquals("UoM's step mustn't be null.", validate.iterator().next().getMessage());
    }

    @Test
    public void test_unit_of_measure_step_has_too_much_integer_digits() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setName("кг");
        unitOfMeasure.setStep(new BigDecimal("1000.0"));
        Set<ConstraintViolation<UnitOfMeasure>> validate = validator.validate(unitOfMeasure);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as UoM's step has too much integer digits:", 1, validate.size());
        assertEquals("UOM's step '1000.0' must have up to '3' integer digits and '1' fraction digits.", validate.iterator().next().getMessage());
    }

    @Test
    public void test_unit_of_measure_step_has_too_much_fraction_digits() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setName("кг");
        unitOfMeasure.setStep(new BigDecimal("0.250"));
        Set<ConstraintViolation<UnitOfMeasure>> validate = validator.validate(unitOfMeasure);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as UoM's has too much fraction digits:", 1, validate.size());
        assertEquals("UOM's step '0.250' must have up to '3' integer digits and '1' fraction digits.", validate.iterator().next().getMessage());
    }

    @Test
    public void test_unit_of_measure_step_is_negative() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setName("кг");
        unitOfMeasure.setStep(new BigDecimal("-1.0"));
        Set<ConstraintViolation<UnitOfMeasure>> validate = validator.validate(unitOfMeasure);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as UoM's step is negative:", 1, validate.size());
        assertEquals("UoM's step '-1.0' must be positive.", validate.iterator().next().getMessage());
    }

    @Test
    public void test_unit_of_measure_is_valid() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setName("шт");
        unitOfMeasure.setStep(new BigDecimal("1.0"));
        Set<ConstraintViolation<UnitOfMeasure>> validate = validator.validate(unitOfMeasure);
        assertEquals("Expected size of the ConstraintViolation set should be 0, as UoM  is valid:", 0, validate.size());
    }
}
