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
import org.jboss.tools.internal.deltacloud.ui.utils.WorkbenchUtils;

/**
 * A property tester for the command framework that answers if the given
 * instance is stopped
 * 
 * @author Andre Dietisheim
 */
public class DeltaCloudInstancePropertyTester extends PropertyTester {

	private static final String PROPERTY_ACTION_CANSTART = "canStart";
	private static final String PROPERTY_ACTION_CANSTOP = "canStop";
	private static final String PROPERTY_ACTION_CANREBOOT = "canReboot";
	private static final String PROPERTY_ACTION_CANDESTROY = "canDestroy";
	private static final String PROPERTY_ACTION_ISRUNNING = "isRunning";

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		DeltaCloudInstance instance = WorkbenchUtils.adapt(receiver, DeltaCloudInstance.class);
		if (instance == null) {
			return false;
		}

		if (PROPERTY_ACTION_CANSTART.equals(property)) {
			return equalsExpectedValue(instance.canStart(), expectedValue);
		}
		else if (PROPERTY_ACTION_CANSTOP.equals(property)) {
			return equalsExpectedValue(instance.canStop(), expectedValue);
		}
		else if (PROPERTY_ACTION_CANREBOOT.equals(property)) {
			return equalsExpectedValue(instance.canReboot(), expectedValue);
		}
		else if (PROPERTY_ACTION_CANDESTROY.equals(property)) {
			return equalsExpectedValue(instance.canDestroy(), expectedValue);
		}
		else if (PROPERTY_ACTION_ISRUNNING.equals(property)) {
			return equalsExpectedValue(instance.isRunning(), expectedValue);
		}
		return false;
	}

	private boolean equalsExpectedValue(boolean propertyValue, Object expectedValue) {
		Assert.isTrue(expectedValue instanceof Boolean);
		Boolean expectedBoolean = (Boolean) expectedValue;
		return expectedBoolean.equals(propertyValue);
	}
	
}
