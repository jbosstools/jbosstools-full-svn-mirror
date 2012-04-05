/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.gwt.tests;

import org.eclipse.core.runtime.Platform;
import org.junit.Test;
import org.osgi.framework.Bundle;

import static org.junit.Assert.*;

public class GWTTest {

	@Test
	public void testGwtPlugisArePresentAndActivated() {
		Bundle bundle = Platform.getBundle("org.jboss.tools.gwt.core");
		assertNotNull(bundle);
		bundle = Platform.getBundle("org.jboss.tools.gwt.ui");
		assertNotNull(bundle);
	}
}
