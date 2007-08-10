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
package org.jboss.tools.vpe.mozilla.browser;

public class nsUnitConversion {
	public static final float ROUND_CONST_FLOAT = 0.5f;
	
	public static int nsToIntRound(float value) {
		return ((0.0f <= value) ? (int)(value + ROUND_CONST_FLOAT) : (int)(value - ROUND_CONST_FLOAT));
	}
}
