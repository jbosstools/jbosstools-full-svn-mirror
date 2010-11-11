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
import org.eclipse.core.runtime.Assert;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;

/**
 * A property tester for the command framework that answers if the given
 * instance is stopped
 * 
 * @author Andre Dietisheim
 */
public class InstancePropertyTester extends PropertyTester {

	private static final String PROPERTY_STATE_STOPPED = "isStopped";
	
	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		DeltaCloudInstance instance = UIUtils.adapt(receiver, DeltaCloudInstance.class);
		if (instance == null) {
			return false;
		}
		if (PROPERTY_STATE_STOPPED.equals(property)) {
			return isStopped(instance, expectedValue);
		}
		return false;
	}

	private boolean isStopped(DeltaCloudInstance instance, Object expectedValue) {
		Assert.isTrue(expectedValue instanceof Boolean);
		Boolean expectedBoolean = (Boolean) expectedValue;
		Object propertyValue = instance.isStopped();
		return expectedBoolean.equals(propertyValue);
	}
}
