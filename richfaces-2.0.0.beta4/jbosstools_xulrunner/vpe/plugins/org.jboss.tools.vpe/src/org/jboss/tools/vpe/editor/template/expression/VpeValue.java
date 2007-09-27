/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.template.expression;

import org.jboss.tools.vpe.messages.VpeUIMessages;

public class VpeValue {
	public static final int NONE_VALUE = 0;
	public static final int BOOLEAN_VALUE = 1;
	public static final int STRING_VALUE = 2;

	private static final String ERROR_TYPE_CONVERSION = VpeUIMessages.ERROR_OF_TYPE_CONVERSION;
	
	private int type = NONE_VALUE;
	private boolean booleanValue;
	private String stringValue;
	
	public VpeValue(boolean value) {
		booleanValue = value;
		type = BOOLEAN_VALUE;
	}
	
	public VpeValue(String value) {
		stringValue = value;
		type = STRING_VALUE;
	}
	
	public int type() {
		return type;
	}
	
	public boolean booleanValue() {
		if (type == BOOLEAN_VALUE) {
			return booleanValue;
		} else {
			throw new VpeExpressionError(ERROR_TYPE_CONVERSION);
		}
	}
	
	public String stringValue() {
		switch (type) {
		case STRING_VALUE:
			return stringValue;
		case BOOLEAN_VALUE:
			return booleanValue ? "true" : "false"; //$NON-NLS-1$ //$NON-NLS-2$
		default:
			return ""; //$NON-NLS-1$
		}
	}
	
	VpeValue equal(VpeValue other) {
		if (type != other.type()) {
			throw new VpeExpressionError(ERROR_TYPE_CONVERSION);
		}
		boolean value;
		switch (type) {
		case STRING_VALUE:
			value = stringValue.equals(other.stringValue());
			break;
		case BOOLEAN_VALUE:
			value = booleanValue == other.booleanValue();
			break;
		default:
			value = false;
		break;
		}
		return new VpeValue(value); 
	}
	
	VpeValue not() {
		if (type != BOOLEAN_VALUE) {
			throw new VpeExpressionError(ERROR_TYPE_CONVERSION);
		}
		return new VpeValue(!booleanValue);
	}
	
	VpeValue and(VpeValue other) {
		if (type != BOOLEAN_VALUE || other.type() != BOOLEAN_VALUE) {
			throw new VpeExpressionError(ERROR_TYPE_CONVERSION);
		}
		return new VpeValue(booleanValue && other.booleanValue());
	}
	
	VpeValue or(VpeValue other) {
		if (type != BOOLEAN_VALUE || other.type() != BOOLEAN_VALUE) {
			throw new VpeExpressionError(ERROR_TYPE_CONVERSION);
		}
		return new VpeValue(booleanValue || other.booleanValue());
	}
	
	
	VpeValue plus(VpeValue other) {
		if (type != STRING_VALUE || other.type() != STRING_VALUE) {
			throw new VpeExpressionError(ERROR_TYPE_CONVERSION);
		}
		return new VpeValue(stringValue +  other.stringValue());
	}
}
