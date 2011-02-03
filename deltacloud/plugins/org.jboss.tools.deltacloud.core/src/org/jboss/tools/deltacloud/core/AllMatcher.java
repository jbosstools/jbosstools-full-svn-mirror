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
package org.jboss.tools.deltacloud.core;

/**
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class AllMatcher implements IFieldMatcher {

	@Override
	public boolean matches(String input) {
		return true;
	}
	
	@Override
	public String toString() {
		return ICloudElementFilter.ALL_MATCHER_EXPRESSION;
	}

	public boolean isMatchesAll() {
		return true;
	}
}
