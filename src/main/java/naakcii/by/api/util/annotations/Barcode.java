package naakcii.by.api.util.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import naakcii.by.api.util.annotations.Barcode.List;

/**
 * Checks that the annotated character sequence is a valid bar-code.
 * The length of the number is verified.
 * The check digit isn't verified.
 * <p>
 * Supported types are:
 * <ul>
 *     <li>{@code CharSequence} (length of character sequence is evaluated)</li>
 * </ul>
 * <p>
 * {@code null} elements are considered valid.
 */

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(List.class)
@Documented
@Constraint(validatedBy = BarcodeValidator.class)
public @interface Barcode {

	String message() default "{by.naakcii.validation.constraints.Barcode.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
    
    /**
	 * @return length of the element must equal to one of the elements
	 */
    int[] lengths = {4, 8, 12, 13, 14};
    
    /**
	 * Defines several {@link Barcode} annotations on the same element.
	 *
	 * @see PureSize
	 */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	@Documented
	@interface List {
    	Barcode[] value();
	}
}
