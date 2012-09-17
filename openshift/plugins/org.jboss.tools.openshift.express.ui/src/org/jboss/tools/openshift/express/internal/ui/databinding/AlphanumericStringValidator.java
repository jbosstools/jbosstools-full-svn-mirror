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
package org.jboss.tools.openshift.express.internal.ui.databinding;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.jboss.tools.openshift.express.internal.ui.utils.StringUtils;

/**
 * @author Andre Dietisheim
 */
public class AlphanumericStringValidator implements IValidator {

	private String fieldName;

	public AlphanumericStringValidator(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public IStatus validate(Object value) {
		String name = (String) value;
		if (StringUtils.isEmpty(name)) {
			return ValidationStatus.cancel("You have to provide a " + fieldName);
		} else if (!StringUtils.isAlphaNumeric(name)) {
			return ValidationStatus.cancel("You have to provide an alphanumeric " + fieldName);
		}
		return ValidationStatus.ok();
	}

}
