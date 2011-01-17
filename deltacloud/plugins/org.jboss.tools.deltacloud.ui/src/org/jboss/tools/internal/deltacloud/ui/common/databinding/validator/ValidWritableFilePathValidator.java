/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.internal.deltacloud.ui.common.databinding.validator;

import java.io.File;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

/**
 * @author André Dietisheim
 */
public class ValidWritableFilePathValidator implements IValidator {

	@Override
	public IStatus validate(Object value) {
		if (value instanceof String) {
			File file = new File((String) value);
			if (file.exists() &&
										file.canWrite()) {
				return ValidationStatus.ok();
			}
		}
		return ValidationStatus.error("You must choose a valid and writable location");
	}
}
