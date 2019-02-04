package naakcii.by.api.util.annotations;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BarcodeValidator implements ConstraintValidator<Barcode, String> {
	
	private int[] lengths;
	
	@Override
    public void initialize(Barcode barcodeAnnotation) {
		this.lengths = Barcode.lengths;
    }
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		boolean isValid = true;
		
		if (value == null) {
			return isValid;
		}
		
		context.disableDefaultConstraintViolation();
		
		if ((value.trim().length() == 0) || (Arrays.stream(lengths).noneMatch((int length) -> value.trim().length() == length))) {
			context
				.buildConstraintViolationWithTemplate("Bar-code '" + value + "' must be 4, 8, 12, 13 or 14 characters long.")
				.addConstraintViolation();
			isValid = false;
		}
		
		Pattern pattern = Pattern.compile("[0-9]+");
		Matcher matcher = pattern.matcher(value.trim());
		
		if (!matcher.matches()) {
			context
				.buildConstraintViolationWithTemplate("Bar-code '" + value + "' must consist only of digits.")
				.addConstraintViolation();
			isValid = false;
		}
		
		return isValid;
	}
}
