/******************************************************************************* 
 * Copyright (c) 2010 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.cdi.core.test.tck;

import java.util.Collection;

import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.cdi.core.IBean;
import org.jboss.tools.cdi.core.IStereotypeDeclaration;

/**
 * @author Alexey Kazakov
 */
public class EnterpriseStereotypeDefinitionTest extends TCKTest {

	/**
	 * section 4.1 am)
	 * 
	 * @throws JavaModelException
	 */
	public void testStereotypeDeclaredInheritedIsInherited()
			throws JavaModelException {
		Collection<IBean> beans = getBeans("org.jboss.jsr299.tck.tests.definition.stereotype.enterprise.BorderCollieLocal");
		IBean bean = beans.iterator().next();
		assertEquals("Wrong scope type",
				"javax.enterprise.context.RequestScoped", bean.getScope()
						.getSourceType().getFullyQualifiedName());
	}

	/**
	 * section 4.1 ama)
	 * 
	 * @throws JavaModelException
	 */
	public void testStereotypeNotDeclaredInheritedIsNotInherited()
			throws JavaModelException {
		Collection<IBean> beans = getBeans("org.jboss.jsr299.tck.tests.definition.stereotype.enterprise.BarracudaLocal");
		IBean bean = beans.iterator().next();
		assertFalse("Wrong scope type",
				"javax.enterprise.context.RequestScoped".equals(bean.getScope()
						.getSourceType().getFullyQualifiedName()));
	}

	/**
	 * section 4.1 aq)
	 * 
	 * @throws JavaModelException
	 */
	public void testStereotypeDeclaredInheritedIsIndirectlyInherited()
			throws JavaModelException {
		Collection<IBean> beans = getBeans("org.jboss.jsr299.tck.tests.definition.stereotype.enterprise.EnglishBorderCollieLocal");
		IBean bean = beans.iterator().next();
		assertEquals("Wrong scope type",
				"javax.enterprise.context.RequestScoped", bean.getScope()
						.getSourceType().getFullyQualifiedName());
	}

	/**
	 * section 4.1 aqa)
	 * 
	 * @throws JavaModelException
	 */
	public void testStereotypeNotDeclaredInheritedIsNotIndirectlyInherited()
			throws JavaModelException {
		Collection<IBean> beans = getBeans("org.jboss.jsr299.tck.tests.definition.stereotype.enterprise.TameBarracudaLocal");
		IBean bean = beans.iterator().next();
		assertFalse("Wrong scope type",
				"javax.enterprise.context.RequestScoped".equals(bean.getScope()
						.getSourceType().getFullyQualifiedName()));
	}

	/**
	 * section 4.1 hhj)
	 * 
	 * @throws JavaModelException
	 */
	public void testStereotypeScopeIsOverriddenByInheritedScope()
			throws JavaModelException {
		Collection<IBean> beans = getBeans("org.jboss.jsr299.tck.tests.definition.stereotype.enterprise.ChihuahuaLocal");
		IBean bean = beans.iterator().next();
		assertEquals("Wrong scope type",
				"javax.enterprise.context.SessionScoped", bean.getScope()
						.getSourceType().getFullyQualifiedName());
	}

	/**
	 * section 4.1 hhk)
	 * 
	 * @throws JavaModelException
	 */
	public void testStereotypeScopeIsOverriddenByIndirectlyInheritedScope()
			throws JavaModelException {
		Collection<IBean> beans = getBeans("org.jboss.jsr299.tck.tests.definition.stereotype.enterprise.MexicanChihuahuaLocal");
		IBean bean = beans.iterator().next();
		assertEquals("Wrong scope type",
				"javax.enterprise.context.SessionScoped", bean.getScope()
						.getSourceType().getFullyQualifiedName());
	}

	public void testAdditionalStereotype() throws JavaModelException {
		Collection<IBean> beans = getBeans("org.jboss.jsr299.tck.tests.definition.stereotype.defaultNamed.Sambar");
		IBean bean = beans.iterator().next();
		assertEquals("sambar", bean.getName());
		Collection<IStereotypeDeclaration> s = bean.getStereotypeDeclarations();
		assertEquals(2, s.size());
	}	
}