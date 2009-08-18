package org.jboss.tools.vpe.resref.core;

import java.io.File;
import java.util.Map;

public class CSSResourceReferenceValidator extends
		ResourceReferenceValidator {

	public static final String CSS_FILE_PATH = "cssFilePath";  //$NON-NLS-1$

	public CSSResourceReferenceValidator() {
		super();
	}

	public CSSResourceReferenceValidator(Map<String, String> fields) {
		super(fields);
	}

	public boolean validate() {
		pageComplete = false;
		if (null != fields) {
			String cssFilePath = fields.get(CSS_FILE_PATH);
			if ((null != cssFilePath) && (cssFilePath.length() > 0)) {
				if (new File(cssFilePath).exists()) {
					/*
					 * Page is complete. Remove any error message.
					 */
					pageComplete = true;
					errorMessage = null;
				} else {
					errorMessage = Messages.CSS_FILE_DOES_NOT_EXIST;
				}
			} else {
				errorMessage = Messages.CSS_FILE_PATH_SHOULD_BE_SET;
			}
		}
		return pageComplete;
	}

}
