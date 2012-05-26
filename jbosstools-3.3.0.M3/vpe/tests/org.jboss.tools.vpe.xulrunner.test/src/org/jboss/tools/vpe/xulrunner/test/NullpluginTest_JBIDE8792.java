/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.xulrunner.test;

import junit.framework.TestCase;

import org.eclipse.core.runtime.Platform;
import org.jboss.tools.vpe.xulrunner.browser.XulRunnerBrowser;
import org.osgi.framework.Bundle;

/**
 * Check that there is no nullplugin in the XULRunner bundle.
 * <p>
 * The nullplugin is the default plug-in for Adobe Flash. The
 * presence of it is undesirable for VPE, because it shows annoying
 * message suggesting to download Adobe Flash plugin.
 * 
 * @see <a href="https://issues.jboss.org/browse/JBIDE-8792">JBIDE-8792</a>
 * @author Yahor Radtsevich
 */
public class NullpluginTest_JBIDE8792 extends TestCase {
	public void testNullplugin() {
		Bundle xulRunnerBundle = Platform.getBundle(XulRunnerBrowser.getXulRunnerBundle());
		assertTrue("Nullplugin is found, but it should not be there.", 
				xulRunnerBundle.getResource("xulrunner/plugins/libnullplugin.so") == null &&
				xulRunnerBundle.getResource("xulrunner/plugins/npnul32.dll") == null);				
	}
}
