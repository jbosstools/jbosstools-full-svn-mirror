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
package org.jboss.tools.cdi.core.test.tck.validation;

import org.jboss.tools.tests.IAnnotationTest;

/**
 * @author Alexey Kazakov
 */
public class AYTBeansXmlValidationTest extends BeansXmlValidationTest {

	private CDIAnnotationTest annotationTest = new CDIAnnotationTest();

	@Override
	protected IAnnotationTest getAnnotationTest() {
		return annotationTest;
	}
}