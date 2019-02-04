package naakcii.by.api.statistics;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class StatisticsTest {

    public static Validator validator;

    public Calendar calendar;

    @Before
    public void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        calendar = new GregorianCalendar(2018, 0, 1);
    }

    @Test
    public void test_statistics_chain_quantity_is_null() {
        Statistics statistics = new Statistics(null, 1, 1, calendar);
        Set<ConstraintViolation<Statistics>> validate = validator.validate(statistics);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as chainQuantity is null:", 1, validate.size());
        assertEquals("Chain quantity of the statistics mustn't be null.", validate.iterator().next().getMessage());
    }

    @Test
    public void test_statistics_chain_quantity_is_negative() {
        Statistics statistics = new Statistics(-1, 1, 1, calendar);
        Set<ConstraintViolation<Statistics>> validate = validator.validate(statistics);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as chainQuantity is negative:", 1, validate.size());
        assertEquals("Chain quantity of the statistics '-1' must be positive.", validate.iterator().next().getMessage());
    }

    @Test
    public void test_statistics_discounted_products_is_null() {
        Statistics statistics = new Statistics(1, null, 1, calendar);
        Set<ConstraintViolation<Statistics>> validate = validator.validate(statistics);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as discountedProducts is null:", 1, validate.size());
        assertEquals("Discounted products of the statistics mustn't be null.", validate.iterator().next().getMessage());
    }

    @Test
    public void test_statistics_discounted_products_is_negative() {
        Statistics statistics = new Statistics(1, -1, 1, calendar);
        Set<ConstraintViolation<Statistics>> validate = validator.validate(statistics);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as discountedProducts is negative:", 1, validate.size());
        assertEquals("Discounted products of the statistics '-1' must be positive.", validate.iterator().next().getMessage());
    }

    @Test
    public void test_statistics_average_discount_percentage_is_null() {
        Statistics statistics = new Statistics(1, 1, null, calendar);
        Set<ConstraintViolation<Statistics>> validate = validator.validate(statistics);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as averageDiscountPercentage is null:", 1, validate.size());
        assertEquals("Average discount percentage of the statistics mustn't be null.", validate.iterator().next().getMessage());
    }

    @Test
    public void test_statistics_average_discount_percentage_is_negative() {
        Statistics statistics = new Statistics(1, 1, -1, calendar);
        Set<ConstraintViolation<Statistics>> validate = validator.validate(statistics);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as averageDiscountPercentage is negative:", 1, validate.size());
        assertEquals("Average discount percentage of the statistics '-1' must be positive.", validate.iterator().next().getMessage());
    }

    @Test
    public void test_statistics_average_discount_percentage_is_too_large() {
        Statistics statistics = new Statistics(1, 1, 200, calendar);
        Set<ConstraintViolation<Statistics>> validate = validator.validate(statistics);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as averageDiscountPercentage is more than 100:", 1, validate.size());
        assertEquals("Average discount percentage of the statistics '200' must be less 100", validate.iterator().next().getMessage());
    }

    @Test
    public void test_statistics_creation_date_is_null() {
        Statistics statistics = new Statistics(1, 1, 1, null);
        Set<ConstraintViolation<Statistics>> validate = validator.validate(statistics);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as creationDate is null:", 1, validate.size());
        assertEquals("Creation date of the statistics mustn't be null.", validate.iterator().next().getMessage());
    }
}
