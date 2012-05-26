/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
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
