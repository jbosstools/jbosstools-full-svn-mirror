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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.eclipse.osgi.util.NLS;

public class TaglibResourceReferenceValidator extends
		ResourceReferenceValidator {

	public static final String TAGLIB_URI = "taglibUri"; //$NON-NLS-1$
	public static final String TAGLIB_PREFIX = "taglibPrefix"; //$NON-NLS-1$
	public static final String PREFIX_STR_PATTERN = "[A-Za-z_]|[^\\x00-\\x7F]"; //$NON-NLS-1$
	public static final String PREFIX_CHAR_PATTERN = "[A-Za-z0-9_.-]|[^\\x00-\\x7F]"; //$NON-NLS-1$
	public static final String PREFIX_PATTERN = '(' + PREFIX_STR_PATTERN + ')'+'(' + PREFIX_CHAR_PATTERN + ")*"; //$NON-NLS-1$
	
	public TaglibResourceReferenceValidator() {
		super();
	}

	public TaglibResourceReferenceValidator(Map<String, String> fields) {
		super(fields);
	}

	@Override
	protected boolean validate() {
		pageComplete = false;
		if (null != fields) {
			String taglibUri = fields.get(TAGLIB_URI);
			String taglibPrefix = fields.get(TAGLIB_PREFIX);
			
			if ((null != taglibUri) && (taglibUri.length() > 0)) {
				if ((null != taglibPrefix) && (taglibPrefix.length() > 0)) {
					try {
						new URI(taglibUri);
						/*
						 * Correct URI;
						 * Page is complete. Remove any error message.
						 */
						pageComplete = true;
						errorMessage = null;
				 	}catch (URISyntaxException ex) {
				 		errorMessage = NLS.bind(Messages.INCORRECT_URI, taglibUri);
				 	}
				 	if(taglibPrefix.matches(PREFIX_PATTERN)) {
				 		/*
						 * Correct Prefix;
						 * Page is complete. Remove any error message.
						 */
						pageComplete = true;
				 		errorMessage = null;
				 	} else {
				 		errorMessage = NLS.bind(Messages.INCORRECT_PREFIX, taglibPrefix);
				 	}
				} else {
					errorMessage = Messages.PREFIX_SHOULD_BE_SET;
				}
			} else {
				errorMessage = Messages.URI_SHOULD_BE_SET;
			}
		}
		return pageComplete;
	}

}
