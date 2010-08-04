package org.jboss.tools.vpe.spring.test.springtest.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class BeanValidator extends UserValidator {

	@Override
	public void validate(Object target, Errors errors) {
		super.validate(target, errors);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "verificationNum", "required", "Verification Number is required.");
	}

}
