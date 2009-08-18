package org.jboss.tools.vpe.resref.core;

import java.util.Map;

public abstract class ResourceReferenceValidator {
	
	public static final String SCOPE = "scope"; //$NON-NLS-1$
	
	protected Map<String, String> fields = null;
	protected String errorMessage = null;
	protected boolean pageComplete = false;
	
	public ResourceReferenceValidator() {}

	public ResourceReferenceValidator(Map<String, String> fields) {
		super();
		this.fields = fields;
	}
	
	void setFields(Map<String, String> fields) {
		this.fields = fields;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public boolean isPageComplete() {
		return pageComplete;
	}

	abstract protected boolean validate();
}
