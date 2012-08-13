/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.cdi.seam.solder.core.test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.jboss.tools.cdi.core.CDICorePlugin;
import org.jboss.tools.cdi.core.IBean;
import org.jboss.tools.cdi.core.ICDIProject;
import org.jboss.tools.cdi.core.IClassBean;
import org.jboss.tools.cdi.core.IInjectionPoint;
import org.jboss.tools.cdi.core.IInjectionPointField;
import org.jboss.tools.cdi.core.IProducer;
import org.jboss.tools.cdi.core.IProducerMethod;
import org.jboss.tools.cdi.seam.solder.core.generic.GenericBeanProducerMethod;
import org.jboss.tools.cdi.seam.solder.core.generic.GenericClassBean;

/**
 *   
 * @author Viacheslav Kabanovich
 *
 */
public class GenericBeanTest extends SeamSolderTest {

	public GenericBeanTest() {}

	public void testGenericBeanEndPointInjections() throws CoreException {
		ICDIProject cdi = CDICorePlugin.getCDIProject(getTestProject(), true);
	
		/*
		 * Case 1. (default qualifier case)
		 * Injection point: in class MyBeanInjections
		 *     @Inject MyBean first1
		 * Generic bean producer method: MyGenericBean.createMyFirstBean()
		 * Configuration producer method: MyConfigurationProducer.getOneConfig()
		 */
		IInjectionPointField injection = getInjectionPointField(cdi, "src/org/jboss/generic/MyBeanInjections.java", "first1");

		Collection<IBean> bs = cdi.getBeans(false, injection);
		assertEquals(1, bs.size());
		IBean b = bs.iterator().next();
		assertTrue(b instanceof IProducerMethod);
		IProducerMethod m = (IProducerMethod)b;
		assertEquals("createMyFirstBean", m.getMethod().getElementName());
		assertTrue(b instanceof GenericBeanProducerMethod);
		GenericBeanProducerMethod gm = (GenericBeanProducerMethod)b;
		GenericClassBean cb = (GenericClassBean)gm.getClassBean();
		IBean gb = cb.getGenericProducerBean();
		assertTrue(gb instanceof IProducerMethod);
		IProducerMethod gbm = (IProducerMethod)gb;
		assertEquals("getOneConfig", gbm.getMethod().getElementName());

		/*
		 * Case 2. (non-default qualifier case)
		 * Injection point: in class MyBeanInjections
		 *     @Inject @Qualifier1 MyBean first2
		 * Generic bean producer method: MyGenericBean.createMyFirstBean()
		 * Configuration producer method: MyConfigurationProducer.getSecondConfig()
		 */
		injection = getInjectionPointField(cdi, "src/org/jboss/generic/MyBeanInjections.java", "first2");

		bs = cdi.getBeans(false, injection);
		assertEquals(1, bs.size());
		b = bs.iterator().next();
		assertTrue(b instanceof IProducerMethod);
		m = (IProducerMethod)b;
		assertEquals("createMyFirstBean", m.getMethod().getElementName());
		assertTrue(b instanceof GenericBeanProducerMethod);
		gm = (GenericBeanProducerMethod)b;
		cb = (GenericClassBean)gm.getClassBean();
		gb = cb.getGenericProducerBean();
		assertTrue(gb instanceof IProducerMethod);
		gbm = (IProducerMethod)gb;
		assertEquals("getSecondConfig", gbm.getMethod().getElementName());

		/*
		 * Case 3. (case of configuration provided by extending config class)
		 * Injection point: in class MyBeanInjections
		 *     @Inject @Qualifier2 MyBean first3
		 * Generic bean producer method: MyGenericBean.createMyFirstBean()
		 * Configuration bean: by MyExtendedConfiguration
		 */
		injection = getInjectionPointField(cdi, "src/org/jboss/generic/MyBeanInjections.java", "first3");

		bs = cdi.getBeans(false, injection);
		assertEquals(1, bs.size());
		b = bs.iterator().next();
		assertTrue(b instanceof IProducerMethod);
		m = (IProducerMethod)b;
		assertEquals("createMyFirstBean", m.getMethod().getElementName());
		assertTrue(b instanceof GenericBeanProducerMethod);
		gm = (GenericBeanProducerMethod)b;
		cb = (GenericClassBean)gm.getClassBean();
		gb = cb.getGenericProducerBean();
		assertTrue(gb instanceof IClassBean);
		IClassBean gbc = (IClassBean)gb;
		assertEquals("MyExtendedConfiguration", gbc.getBeanClass().getElementName());
	}

	public void testGenericBeanInjectionIntoGenericPoint() throws CoreException {
		ICDIProject cdi = CDICorePlugin.getCDIProject(getTestProject(), true);
		
		/*
		 * Injection point: in class MyGenericBean2
		 *     @Inject @Generic MyBean c;
		 * There are 5 configurations, hence there are 5 beans MyGenericBean2, 
		 * each has that injection point; 
		 * in all cases bean is produced by MyGenericBean.createMyFirstBean()
		 */
		Collection<IInjectionPointField> injections = getGenericInjectionPointField(cdi, "src/org/jboss/generic/MyGenericBean2.java", "c");
		assertEquals(5, injections.size());
		for (IInjectionPointField injection: injections) {
			Collection<IBean> bs = cdi.getBeans(false, injection);
			assertEquals(1, bs.size());
			IBean b = bs.iterator().next();
			assertTrue(b instanceof IProducerMethod);
			IProducerMethod m = (IProducerMethod)b;
			assertEquals("createMyFirstBean", m.getMethod().getElementName());
		}
	}

	public void testGenericTypeInjection() throws CoreException {
		ICDIProject cdi = CDICorePlugin.getCDIProject(getTestProject(), true);

		/*
		 * Injection point: in class MyGenericBean2
		 *     @Inject MyGenericType type;
		 * There are 5 configurations, hence there are 5 beans MyGenericBean2, 
		 * each has that injection point; 
		 * in all cases we insert a dummy bean of type org.jboss.generic.MyGenericType
		 */
		Collection<IInjectionPointField> injections = getGenericInjectionPointField(cdi, "src/org/jboss/generic/MyGenericBean2.java", "type");
		assertEquals(5, injections.size());
		for (IInjectionPointField injection: injections) {
			Collection<IBean> bs = cdi.getBeans(false, injection);
			assertEquals(1, bs.size());
			IBean b = bs.iterator().next();
			assertTrue(b instanceof IClassBean);
			IType t = ((IClassBean)b).getBeanClass();
			assertEquals("org.jboss.generic.MyGenericType", t.getFullyQualifiedName());
		}
	}
	//TODO - more tests

	protected Collection<IInjectionPointField> getGenericInjectionPointField(ICDIProject cdi, String beanClassFilePath, String fieldName) {
		Collection<IInjectionPointField> result = new HashSet<IInjectionPointField>();
		IFile file = cdi.getNature().getProject().getFile(beanClassFilePath);
		Collection<IBean> beans = cdi.getBeans(file.getFullPath());
		Iterator<IBean> it = beans.iterator();
		while(it.hasNext()) {
			IBean b = it.next();
			if(b instanceof IProducer) it.remove();
		}

		for (IBean b: beans) {
			Collection<IInjectionPoint> injections = b.getInjectionPoints();
			for (IInjectionPoint injectionPoint : injections) {
				if(injectionPoint instanceof IInjectionPointField) {
					IInjectionPointField field = (IInjectionPointField)injectionPoint;
					if(fieldName.equals(field.getField().getElementName())) {
						result.add(field);
					}
				}
			}
		}
		return result;
	}

	public void testVetoedGenericBeanInjectionIntoGenericPoint() throws CoreException {
		ICDIProject cdi = CDICorePlugin.getCDIProject(getTestProject(), true);

		/*
		 * Injection point: in class MessageManager
		 *     @Inject @Generic MessageQueue queue;
		 * There are 3 configurations, hence there are 3 beans MessageQueue, 
		 * each has that injection point; 
		 * in all cases bean is produced by MyGenericBean.createMyFirstBean()
		 */
		IClassBean beanToBeVetoed = null;
		Collection<IInjectionPointField> injections = getGenericInjectionPointField(cdi, "src/org/jboss/generic2/MessageManager.java", "queue");
		assertEquals(3, injections.size());
		for (IInjectionPointField injection: injections) {
			Collection<IBean> bs = cdi.getBeans(false, injection);
			assertTrue(bs.size() >= 1);
			for (IBean b: bs) {
				assertTrue(b instanceof GenericBeanProducerMethod);
				GenericBeanProducerMethod m = (GenericBeanProducerMethod)b;
				assertEquals("messageQueueProducer", m.getMethod().getElementName());
				IBean g = ((GenericClassBean) m.getClassBean()).getGenericProducerBean();
				if(g instanceof IClassBean) {
					beanToBeVetoed = (IClassBean)g;
				}
			}
		}	
		assertNotNull(beanToBeVetoed);

		/*
		 * Replace DurableQueueConfiguration.java with vetoed version.
		 * After that there are only 2 configurations.
		 */
		replaceFile(getTestProject(), "src/org/jboss/generic2/DurableQueueConfiguration.vetoed",
				"src/org/jboss/generic2/DurableQueueConfiguration.java");

		beanToBeVetoed = null;
		injections = getGenericInjectionPointField(cdi, "src/org/jboss/generic2/MessageManager.java", "queue");		
		assertEquals(2, injections.size());
		for (IInjectionPointField injection: injections) {
			Collection<IBean> bs = cdi.getBeans(false, injection);
			assertEquals(1, bs.size());
			IBean b = bs.iterator().next();
			assertTrue(b instanceof GenericBeanProducerMethod);
			GenericBeanProducerMethod m = (GenericBeanProducerMethod)b;
			assertEquals("messageQueueProducer", m.getMethod().getElementName());
			IBean g = ((GenericClassBean) m.getClassBean()).getGenericProducerBean();
			if(g instanceof IClassBean) {
				beanToBeVetoed = (IClassBean)g;
			}
		}
		assertNull(beanToBeVetoed);

		/*
		 * Set original DurableQueueConfiguration.java back.
		 * Make sure that there are again 3 configurations.
		 */
		replaceFile(getTestProject(), "src/org/jboss/generic2/DurableQueueConfiguration.original",
				"src/org/jboss/generic2/DurableQueueConfiguration.java");

		beanToBeVetoed = null;
		injections = getGenericInjectionPointField(cdi, "src/org/jboss/generic2/MessageManager.java", "queue");		
		assertEquals(3, injections.size());
		for (IInjectionPointField injection: injections) {
			Collection<IBean> bs = cdi.getBeans(false, injection);
			assertTrue(bs.size() >= 1);
			for (IBean b: bs) {
				assertTrue(b instanceof GenericBeanProducerMethod);
				GenericBeanProducerMethod m = (GenericBeanProducerMethod)b;
				assertEquals("messageQueueProducer", m.getMethod().getElementName());
				IBean g = ((GenericClassBean) m.getClassBean()).getGenericProducerBean();
				if(g instanceof IClassBean) {
					beanToBeVetoed = (IClassBean)g;
				}
			}
		}	
		assertNotNull(beanToBeVetoed);
	}

	static void replaceFile(IProject project, String sourcePath, String targetPath) throws CoreException {
		GenericBeanValidationTest.writeFile(project, sourcePath, targetPath);
	}
}