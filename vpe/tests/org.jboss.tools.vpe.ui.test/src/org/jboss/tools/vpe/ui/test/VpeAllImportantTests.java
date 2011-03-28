/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.test;

import java.lang.reflect.Method;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * Test suite to run all important VPE tests from all plugins that extend 
 * {@value VPETestPlugin#VPE_TEST_EXTENTION_POINT_ID}. 
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
public class VpeAllImportantTests {

	public static final String IMPORTANT_TEST_SUITE_PARAM = "importantTestSuite"; //$NON-NLS-1$	

	public static Test suite() {

		TestSuite result = new TestSuite();
		IExtension[] extensions = VPETestPlugin.getDefault().getVpeTestExtensions();
		for (IExtension extension : extensions) {
			IConfigurationElement[] confElements = extension
					.getConfigurationElements();
			for (IConfigurationElement configurationElement : confElements) {
				String clazz = configurationElement
						.getAttribute(IMPORTANT_TEST_SUITE_PARAM);
				if (VpeAllTests.TESTS_ELEMENT.equals(configurationElement.getName())) {
					try {
						Bundle bundle = Platform.getBundle(configurationElement
								.getNamespaceIdentifier());
						Class<?> testObject = bundle.loadClass(clazz);
						Method method = testObject.getMethod(VpeAllTests.METHOD_SUITE_NAME, null);
						// null -because static method
						Object res = method.invoke(null, null);
						if (res instanceof Test) {
							Test testSuite = (Test) res;
							result.addTest(testSuite);
						}
					} catch (Exception e) {
						VPETestPlugin.getDefault().logError(e);
					}
				}
			}
		}
		return result;

	}
}
