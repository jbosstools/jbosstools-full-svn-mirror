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
package org.jboss.tools.cdi.seam.config.core.test;

import java.util.Collection;

import org.jboss.tools.cdi.core.IBean;
import org.jboss.tools.cdi.core.IClassBean;
import org.jboss.tools.cdi.core.IInjectionPoint;
import org.jboss.tools.cdi.core.IProducerField;
import org.jboss.tools.cdi.core.IProducerMethod;
import org.jboss.tools.cdi.core.test.DependentProjectTest;

public class ConfigBeansInjectionTest extends SeamConfigTest {
	static String INJECTIONS_CLASS_PATH = "src/org/jboss/beans/injection/Injections.java";
	static String INJECTIONS2_CLASS_PATH = "src/org/jboss/beans/injection/Injections2.java";

	public void testClassBeanInjection() {
		IInjectionPoint p = DependentProjectTest.getInjectionPointField(cdiProject, INJECTIONS_CLASS_PATH, "b5");
		Collection<IBean> bs = cdiProject.getBeans(false, p);
		assertEquals(1, bs.size());
		IBean b = bs.iterator().next();
		assertTrue(b instanceof IClassBean);

		//The same in dependent project
		p = DependentProjectTest.getInjectionPointField(cdiDependentProject, INJECTIONS2_CLASS_PATH, "b5");
		bs = cdiDependentProject.getBeans(false, p);
		assertEquals(1, bs.size());
	}

	public void testVirtualFieldProducer() {
		IInjectionPoint p = DependentProjectTest.getInjectionPointField(cdiProject, INJECTIONS_CLASS_PATH, "s");
		Collection<IBean> bs = cdiProject.getBeans(false, p);
		assertEquals(1, bs.size());
		IBean b = bs.iterator().next();
		assertTrue(b instanceof IClassBean);

		//The same in dependent project
		p = DependentProjectTest.getInjectionPointField(cdiDependentProject, INJECTIONS2_CLASS_PATH, "s");
		bs = cdiDependentProject.getBeans(false, p);
		assertEquals(1, bs.size());
	}

	public void testMethodProducer() {
		IInjectionPoint p = DependentProjectTest.getInjectionPointField(cdiProject, INJECTIONS_CLASS_PATH, "t1");
		Collection<IBean> bs = cdiProject.getBeans(false, p);
		assertEquals(1, bs.size());
		IBean b = bs.iterator().next();
		assertTrue(b instanceof IProducerMethod);

		//The same in dependent project
		p = DependentProjectTest.getInjectionPointField(cdiDependentProject, INJECTIONS2_CLASS_PATH, "t1");
		bs = cdiDependentProject.getBeans(false, p);
		assertEquals(1, bs.size());
	}

	public void testFieldProducer() {
		IInjectionPoint p = DependentProjectTest.getInjectionPointField(cdiProject, INJECTIONS_CLASS_PATH, "t3");
		Collection<IBean> bs = cdiProject.getBeans(false, p);
		assertEquals(1, bs.size());
		IBean b = bs.iterator().next();
		assertTrue(b instanceof IProducerField);

		//The same in dependent project
		p = DependentProjectTest.getInjectionPointField(cdiDependentProject, INJECTIONS2_CLASS_PATH, "t3");
		bs = cdiDependentProject.getBeans(false, p);
		assertEquals(1, bs.size());
	}
}
