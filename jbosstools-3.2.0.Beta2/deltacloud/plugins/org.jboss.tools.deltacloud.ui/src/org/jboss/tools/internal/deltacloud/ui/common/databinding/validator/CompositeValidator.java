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

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class CompositeValidator implements IValidator {

	private final LogicOp operator;

	private final IValidator[] validatorCollection;

	public static enum LogicOp {
		AND,
		OR
	};

	public CompositeValidator(final IValidator... validators) {
		this(LogicOp.AND, validators);
	}

	public CompositeValidator(final LogicOp operator, final IValidator... validators) {
		Assert.isLegal(operator != null);
		Assert.isLegal(validators != null);

		this.operator = operator;
		validatorCollection = validators;
	}

	public IStatus validate(final Object value) {
		return LogicOp.AND.equals(operator) ? andValidate(value) : orValidate(value);
	}

	/**
	 * validates on behalf of all validators. The validation stops, as soon as
	 * there's one validator that does <tt>NOT</tt> return Status#OK_Status
	 * 
	 * @param value
	 *            the value
	 * 
	 * @return the status of this validation process
	 */
	private IStatus andValidate(final Object value) {
		IStatus result = Status.OK_STATUS;
		;
		for (int i = 0; i < validatorCollection.length && result.isOK(); i++) {
			result = validatorCollection[i].validate(value);
		}
		return result;
	}

	/**
	 * validates on behalf of all validators. The validation stops, as soons as
	 * there's one validator that does return Status#OK_Status
	 * 
	 * @param value
	 *            the value
	 * 
	 * @return the status of this validation process
	 */
	private IStatus orValidate(final Object value) {
		IStatus result = Status.OK_STATUS;
		;
		for (int i = 0; i < validatorCollection.length && result.isOK(); i++) {
			result = validatorCollection[i].validate(value);
		}
		return result;
	}
}
