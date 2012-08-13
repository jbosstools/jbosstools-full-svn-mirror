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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.cdi.core.IBean;
import org.jboss.tools.cdi.core.IQualifier;
import org.jboss.tools.cdi.core.IQualifierDeclaration;
import org.jboss.tools.common.java.IParametedType;

/**
 * @author Alexey Kazakov
 */
public class EnterpriseQualifierDefinitionTest extends TCKTest {

	/**
	 * section 4.1 al)
	 * @throws CoreException 
	 */
	public void testQualifierDeclaredInheritedIsInherited() throws CoreException {
		IQualifierDeclaration hairy = getQualifierDeclarationFromClass("JavaSource/org/jboss/jsr299/tck/tests/definition/qualifier/enterprise/LongHairedDog.java", "org.jboss.jsr299.tck.tests.definition.qualifier.enterprise.Hairy");
		IParametedType type = getType("org.jboss.jsr299.tck.tests.definition.qualifier.enterprise.BorderCollieLocal");
		Collection<IBean> beans = cdiProject.getBeans(true, type, hairy);
//		assertEquals("Wrong number of beans.", 1, beans.size());
		IBean bean = beans.iterator().next();
		Collection<IQualifier> qualifiers = bean.getQualifiers();
		assertEquals("Wrong number of qualifiers.", 2, qualifiers.size());
		assertContainsQualifier(bean, hairy);
		assertContainsQualifierType(bean, "javax.enterprise.inject.Any");
	}

	/**
	 * section 4.1 ala)
	 * @throws JavaModelException 
	 */
	public void testQualifierNotDeclaredInheritedIsNotInherited() throws JavaModelException {
		IQualifierDeclaration skinny = getQualifierDeclarationFromClass("JavaSource/org/jboss/jsr299/tck/tests/definition/qualifier/enterprise/SkinnyHairlessCat.java", "org.jboss.jsr299.tck.tests.definition.qualifier.enterprise.Skinny");
		IParametedType type = getType("org.jboss.jsr299.tck.tests.definition.qualifier.enterprise.TameSkinnyHairlessCatLocal");
		Collection<IBean> beans = cdiProject.getBeans(true, type, skinny);
		assertTrue("Wrong number of beans.", beans.isEmpty());
	}

	/**
	 * section 4.1 ap)
	 * @throws CoreException 
	 */
	public void testQualifierDeclaredInheritedIsIndirectlyInherited() throws CoreException {
		IQualifierDeclaration hairy = getQualifierDeclarationFromClass("JavaSource/org/jboss/jsr299/tck/tests/definition/qualifier/enterprise/LongHairedDog.java", "org.jboss.jsr299.tck.tests.definition.qualifier.enterprise.Hairy");
		IParametedType type = getType("org.jboss.jsr299.tck.tests.definition.qualifier.enterprise.EnglishBorderCollieLocal");
		Collection<IBean> beans = cdiProject.getBeans(true, type, hairy);
		assertEquals("Wrong number of beans.", 1, beans.size());
		IBean bean = beans.iterator().next();
		Collection<IQualifier> qualifiers = bean.getQualifiers();
		assertEquals("Wrong number of qualifiers.", 2, qualifiers.size());
		assertContainsQualifier(bean, hairy);
		assertContainsQualifierType(bean, "javax.enterprise.inject.Any");
	}

	/**
	 * section 4.1 apa)
	 * @throws JavaModelException 
	 */
	public void testQualifierNotDeclaredInheritedIsNotIndirectlyInherited() throws JavaModelException {
		IQualifierDeclaration skinny = getQualifierDeclarationFromClass("JavaSource/org/jboss/jsr299/tck/tests/definition/qualifier/enterprise/SkinnyHairlessCat.java", "org.jboss.jsr299.tck.tests.definition.qualifier.enterprise.Skinny");
		IParametedType type = getType("org.jboss.jsr299.tck.tests.definition.qualifier.enterprise.FamousCatLocal");
		Collection<IBean> beans = cdiProject.getBeans(true, type, skinny);
		assertTrue("Wrong number of beans.", beans.isEmpty());
	}
}