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
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;

/**
 * A property tester for the command framework that answers if the given
 * DeltaCloud has images
 * 
 * @author Andre Dietisheim
 */
public class DeltaCloudPropertyTester extends PropertyTester {

	private static final String PROPERTY_HAS_IMAGES = "hasImages";
	private static final String PROPERTY_IS_VALID = "isValid";

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		DeltaCloud cloud = UIUtils.adapt(receiver, DeltaCloud.class);
		if (cloud == null) {
			return false;
		}

		if (PROPERTY_HAS_IMAGES.equals(property)) {
			return equalsExpectedValue(getImages(cloud), true);
		}

		if (PROPERTY_IS_VALID.equals(property)) {
			return equalsExpectedValue(isValid(cloud), true);
		}

		return false;
	}

	private DeltaCloudImage[] getImages(DeltaCloud cloud) {
		try {
			return cloud.getImages();
		} catch (DeltaCloudException e) {
			return new DeltaCloudImage[] {};
		}
	}

	private boolean equalsExpectedValue(DeltaCloudImage[] images, Object expectedValue) {
		Assert.isTrue(expectedValue instanceof Boolean);
		Boolean expectedBoolean = (Boolean) expectedValue;
		return expectedBoolean.equals(images.length > 0);
	}

	private boolean isValid(DeltaCloud cloud) {
		return cloud != null
				&& cloud.isValid();
	}

	private boolean equalsExpectedValue(boolean value, Object expectedValue) {
		Assert.isTrue(expectedValue instanceof Boolean);
		Boolean expectedBoolean = (Boolean) expectedValue;
		return expectedBoolean.equals(value);
	}

}
