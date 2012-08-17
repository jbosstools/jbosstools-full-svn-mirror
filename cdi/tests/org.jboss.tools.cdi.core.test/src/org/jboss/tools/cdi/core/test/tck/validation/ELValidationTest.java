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
package org.jboss.tools.cdi.core.test.tck.validation;

import java.text.MessageFormat;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.tools.common.base.test.validation.TestUtil;
import org.jboss.tools.jst.web.kb.internal.validation.ELValidationMessages;
import org.jboss.tools.test.util.ResourcesUtils;
import org.jboss.tools.tests.AbstractResourceMarkerTest;

/**
 * @author Alexey Kazakov
 */
public class ELValidationTest extends ValidationTest {

	public void testEls() throws Exception {
		boolean saveAutoBuild = ResourcesUtils.setBuildAutomatically(false);

		try {
			IFile file = tckProject.getFile("JavaSource/org/jboss/jsr299/tck/tests/jbt/validation/el/TestBean.java");
			AbstractResourceMarkerTest.assertMarkerIsNotCreated(file, MessageFormat.format(ELValidationMessages.UNKNOWN_EL_VARIABLE_PROPERTY_NAME, "foo"), 6);
			file = tckProject.getFile("WebContent/elValidation.xhtml");
			AbstractResourceMarkerTest.assertMarkerIsNotCreated(file, MessageFormat.format(ELValidationMessages.UNKNOWN_EL_VARIABLE_PROPERTY_NAME, "foo"), 9);

			IFile namedBean = tckProject.getFile("JavaSource/org/jboss/jsr299/tck/tests/jbt/validation/el/NamedBean.java");
			IFile newNamedBean = tckProject.getFile("JavaSource/org/jboss/jsr299/tck/tests/jbt/validation/el/NewNamedBean.validation");
			namedBean.setContents(newNamedBean.getContents(), IFile.FORCE, new NullProgressMonitor());
			TestUtil.validate(namedBean);

			file = tckProject.getFile("JavaSource/org/jboss/jsr299/tck/tests/jbt/validation/el/TestBean.java");
			AbstractResourceMarkerTest.assertMarkerIsCreated(file, MessageFormat.format(ELValidationMessages.UNKNOWN_EL_VARIABLE_PROPERTY_NAME, "foo"), 6);
			file = tckProject.getFile("WebContent/elValidation.xhtml");
			AbstractResourceMarkerTest.assertMarkerIsCreated(file, MessageFormat.format(ELValidationMessages.UNKNOWN_EL_VARIABLE_PROPERTY_NAME, "foo"), 9);

			newNamedBean = tckProject.getFile("JavaSource/org/jboss/jsr299/tck/tests/jbt/validation/el/NamedBean.java");
			namedBean = tckProject.getFile("JavaSource/org/jboss/jsr299/tck/tests/jbt/validation/el/NamedBean.validation");
			newNamedBean.setContents(namedBean.getContents(), IFile.FORCE, new NullProgressMonitor());
			TestUtil.validate(newNamedBean);

			file = tckProject.getFile("JavaSource/org/jboss/jsr299/tck/tests/jbt/validation/el/TestBean.java");
			AbstractResourceMarkerTest.assertMarkerIsNotCreated(file, MessageFormat.format(ELValidationMessages.UNKNOWN_EL_VARIABLE_PROPERTY_NAME, "foo"), 6);
			file = tckProject.getFile("WebContent/elValidation.xhtml");
			AbstractResourceMarkerTest.assertMarkerIsNotCreated(file, MessageFormat.format(ELValidationMessages.UNKNOWN_EL_VARIABLE_PROPERTY_NAME, "foo"), 9);
		} finally {
			IFile newNamedBean = tckProject.getFile("JavaSource/org/jboss/jsr299/tck/tests/jbt/validation/el/NamedBean.java");
			IFile namedBean = tckProject.getFile("JavaSource/org/jboss/jsr299/tck/tests/jbt/validation/el/NamedBean.validation");
			newNamedBean.setContents(namedBean.getContents(), IFile.FORCE, new NullProgressMonitor());
			TestUtil.validate(newNamedBean);

			ResourcesUtils.setBuildAutomatically(saveAutoBuild);
		}
	}
}