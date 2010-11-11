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
package org.jboss.tools.deltacloud.ui.commands;

import org.eclipse.core.expressions.PropertyTester;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;

/**
 * A property tester for the command framework that answers if the given
 * instance is stopped
 * 
 * @author Andre Dietisheim
 */
public class AdaptableToDeltaCloudInstancePropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		DeltaCloudInstance instance = UIUtils.adapt(receiver, DeltaCloudInstance.class);
		Boolean isAdaptableExpectedValue = (Boolean) expectedValue;
		return isAdaptableExpectedValue.equals(instance != null);
	}
}
