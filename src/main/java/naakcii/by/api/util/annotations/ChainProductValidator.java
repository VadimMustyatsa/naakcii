package naakcii.by.api.util.annotations;

import java.util.Calendar;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import naakcii.by.api.chainproduct.ChainProduct;

public class ChainProductValidator implements ConstraintValidator<ValidChainProduct, ChainProduct> {
	
	@Override
    public void initialize(ValidChainProduct validChainProductAnnotation) { }

	@Override
	public boolean isValid(ChainProduct chainProduct, ConstraintValidatorContext context) {
		boolean isValid = true;
		
		if (chainProduct == null) {
			return isValid;
		}
		
		Calendar startDate = chainProduct.getStartDate();
		Calendar endDate = chainProduct.getEndDate();
		
		if ((startDate != null) && (endDate != null)) {
			if (startDate.after(endDate)) {
				isValid = false;
			}
		}
		
		return isValid;
	}
}
