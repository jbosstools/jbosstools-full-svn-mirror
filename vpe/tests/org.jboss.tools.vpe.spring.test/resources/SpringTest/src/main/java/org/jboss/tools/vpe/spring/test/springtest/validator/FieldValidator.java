package org.jboss.tools.vpe.spring.test.springtest.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class FieldValidator implements Validator{
	
	public boolean supports(Class<?> clazz) {
		return true;
	}
	
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newName", "required","Name is required.");
	}
}
