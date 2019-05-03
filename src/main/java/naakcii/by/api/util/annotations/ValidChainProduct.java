package naakcii.by.api.util.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Checks that the annotated chain product is valid.
 * Values of end and start dates are verified.
 * <p>
 * Supported types are:
 * <ul>
 *     <li>{@code ChainProduct}</li>
 * </ul>
 * <p>
 * {@code null} elements are considered valid.
 */
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ChainProductValidator.class)
public @interface ValidChainProduct {

	String message() default "{by.naakcii.validation.constraints.ChainProduct.message}";
	
	Class<?>[] groups() default { };
	
	Class<? extends Payload>[] payload() default { };
}
