/*******************************************************************************
 * Copyright (c) 2007-2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
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
