package naakcii.by.api.country;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class CountryTest {

    private static Validator validator;

    @BeforeClass
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void test_country_name_is_null() {
        Country country = new Country();
        country.setAlphaCode2("BY");
        country.setAlphaCode3("BLR");
        Set<ConstraintViolation<Country>> constraintViolations = validator.validate(country);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as country name is null:", 1, constraintViolations.size());
        assertEquals("Country's name mustn't be null.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_country_name_is_too_short() {
        Country country = new Country();
        country.setName("Ar");
        country.setAlphaCode2("AM");
        country.setAlphaCode3("ARM");
        Set<ConstraintViolation<Country>> constraintViolations = validator.validate(country);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as country name is too short:", 1, constraintViolations.size());
        assertEquals("Country's name 'Ar' must be between '3' and '50' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_country_trimmed_name_is_too_short() {
        Country country = new Country();
        country.setName(" Ar ");
        country.setAlphaCode2("AM");
        country.setAlphaCode3("ARM");
        Set<ConstraintViolation<Country>> constraintViolations = validator.validate(country);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as country name is too short:", 1, constraintViolations.size());
        assertEquals("Country's name ' Ar ' must be between '3' and '50' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_country_AlphaCode2_is_null() {
        Country country = new Country();
        country.setName("Belarus");
        country.setAlphaCode3("BLR");
        Set<ConstraintViolation<Country>> constraintViolations = validator.validate(country);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as country AlphaCode2 is null:", 1, constraintViolations.size());
        assertEquals("Country's AlphaCode2 mustn't be blank.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_country_AlphaCode2_is_blank() {
        Country country = new Country();
        country.setName("Belarus");
        country.setAlphaCode2("  ");
        country.setAlphaCode3("BLR");
        Set<ConstraintViolation<Country>> constraintViolations = validator.validate(country);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as country AlphaCode2 is blank:", 1, constraintViolations.size());
        assertEquals("Country's AlphaCode2 mustn't be blank.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_country_AlphaCode2_is_too_long() {
        Country country = new Country();
        country.setName("Belarus");
        country.setAlphaCode2("BLR");
        country.setAlphaCode3("BLR");
        Set<ConstraintViolation<Country>> constraintViolations = validator.validate(country);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as country AlphaCode2 is too long:", 1, constraintViolations.size());
        assertEquals("Country's AlphaCode2 'BLR' must be '2' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_country_AlphaCode2_is_too_short() {
        Country country = new Country();
        country.setName("Belarus");
        country.setAlphaCode2("B");
        country.setAlphaCode3("BLR");
        Set<ConstraintViolation<Country>> constraintViolations = validator.validate(country);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as country AlphaCode2 is too short:", 1, constraintViolations.size());
        assertEquals("Country's AlphaCode2 'B' must be '2' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_country_AlphaCode3_is_null() {
        Country country = new Country();
        country.setName("Belarus");
        country.setAlphaCode2("BY");
        Set<ConstraintViolation<Country>> constraintViolations = validator.validate(country);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as country AlphaCode3 is null:", 1, constraintViolations.size());
        assertEquals("Country's AlphaCode3 mustn't be blank.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_country_AlphaCode3_is_blank() {
        Country country = new Country();
        country.setName("Belarus");
        country.setAlphaCode2("BY");
        country.setAlphaCode3("   ");
        Set<ConstraintViolation<Country>> constraintViolations = validator.validate(country);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as country AlphaCode3 is blank:", 1, constraintViolations.size());
        assertEquals("Country's AlphaCode3 mustn't be blank.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_country_AlphaCode3_is_too_long() {
        Country country = new Country();
        country.setName("Belarus");
        country.setAlphaCode2("BY");
        country.setAlphaCode3("RBLR");
        Set<ConstraintViolation<Country>> constraintViolations = validator.validate(country);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as country AlphaCode3 is too long:", 1, constraintViolations.size());
        assertEquals("Country's AlphaCode3 'RBLR' must be '3' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_country_AlphaCode3_is_too_short() {
        Country country = new Country();
        country.setName("Belarus");
        country.setAlphaCode2("BY");
        country.setAlphaCode3("BR");
        Set<ConstraintViolation<Country>> constraintViolations = validator.validate(country);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as country AlphaCode3 is too short:", 1, constraintViolations.size());
        assertEquals("Country's AlphaCode3 'BR' must be '3' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_action_type_is_valid() {
        Country country = new Country(CountryCode.BY);
        Set<ConstraintViolation<Country>> constraintViolations = validator.validate(country);
        assertEquals("Expected size of the ConstraintViolation set should be 0, as country is valid:", 0, constraintViolations.size());
    }
}
