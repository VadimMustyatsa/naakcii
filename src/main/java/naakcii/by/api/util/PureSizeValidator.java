package naakcii.by.api.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PureSizeValidator implements ConstraintValidator<PureSize, String> {
	
	private int min;
	private int max;
	
	@Override
    public void initialize(PureSize constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
            return true;
        }
		
		String pureValue = value.trim();
		return (pureValue.length() >= min) && (pureValue.length() <= max);	
	}
}
