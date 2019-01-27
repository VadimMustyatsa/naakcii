package naakcii.by.api.subscriber;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SubscriberTest {

    private static Validator validator;

    @BeforeClass
    public static void setUpValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void test_subscriber_invalid_email() {
        Subscriber subscriber = new Subscriber("invalid,email@gmail.com");
        Set<ConstraintViolation<Subscriber>> constraintViolations = validator.validate(subscriber);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as subscribers email is invalid:", 1, constraintViolations.size());
        assertEquals("Email should be valid", constraintViolations.iterator().next().getMessage());
    }

}
