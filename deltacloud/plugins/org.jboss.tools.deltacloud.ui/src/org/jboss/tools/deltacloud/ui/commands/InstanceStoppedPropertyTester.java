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
import org.jboss.tools.deltacloud.core.client.Instance.State;
import org.jboss.tools.deltacloud.ui.views.CVInstanceElement;

/**
 * A property tester for the command framework that answers if the given
 * instance is stopped
 * 
 * @author Andre Dietisheim
 */
public class InstanceStoppedPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		CVInstanceElement cvInstance = (CVInstanceElement) receiver;
		DeltaCloudInstance instance = (DeltaCloudInstance) cvInstance.getElement();
		Assert.isTrue(expectedValue instanceof Boolean);
		Boolean isExpectedValue = (Boolean) expectedValue;
		boolean isStopped = State.STOPPED.toString().equals(instance.getState());
		return isExpectedValue.equals(isStopped);
	}
}
