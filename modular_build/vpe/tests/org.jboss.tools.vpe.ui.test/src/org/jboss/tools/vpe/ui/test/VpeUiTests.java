/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.tools.vpe.editor.menu.VpePopupMenuTest;
import org.jboss.tools.vpe.ui.test.dialog.VpeEditAnyDialogTest;
import org.jboss.tools.vpe.ui.test.dialog.VpeResourcesDialogTest;
import org.jboss.tools.vpe.ui.test.editor.CustomSashFormTest;
import org.jboss.tools.vpe.ui.test.preferences.VpeEditorPreferencesPageTest;
import org.jboss.tools.vpe.ui.test.wizard.VpeImportExportUnknownTagsWizardsTest;

/**
 * @author mareshkau
 *
 */
public class VpeUiTests {
    public static final String IMPORT_PROJECT_NAME = "TestProject"; //$NON-NLS-1$
	public static Test suite(){
		TestSuite suite = new TestSuite("UI Tests for vpe"); //$NON-NLS-1$
		suite.addTestSuite(VpeResourcesDialogTest.class);
		suite.addTestSuite(VpeEditorPreferencesPageTest.class);
		suite.addTestSuite(CustomSashFormTest.class);
		suite.addTestSuite(VpePopupMenuTest.class);
		suite.addTestSuite(VpeEditAnyDialogTest.class);
		suite.addTestSuite(VpeImportExportUnknownTagsWizardsTest.class);
		
		return new VpeTestSetup(suite);
	}
}
