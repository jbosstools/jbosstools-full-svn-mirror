/*******************************************************************************
 * Copyright (c) 2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.test.dialog;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.vpe.resref.core.VpeResourcesDialog;
import org.jboss.tools.vpe.ui.test.TestUtil;
import org.jboss.tools.vpe.ui.test.VpeTest;
import org.jboss.tools.vpe.ui.test.VpeUiTests;

public class VpeResourcesDialogTest extends VpeTest {

	private final String FILE_NAME = "hello.jsp"; //$NON-NLS-1$

	public VpeResourcesDialogTest(String name) {
		super(name);
	}

	public void testVpeResourcesDialogOpen() throws Throwable {
		IFile file = (IFile) TestUtil.getComponentPath(FILE_NAME,
				VpeUiTests.IMPORT_PROJECT_NAME);

		assertNotNull("Specified file does not exist: fileName = " + FILE_NAME //$NON-NLS-1$
				+ "; projectName = " + VpeUiTests.IMPORT_PROJECT_NAME, file); //$NON-NLS-1$

		VpeResourcesDialog dialog = new VpeResourcesDialog(PlatformUI
				.getWorkbench().getDisplay().getActiveShell(), file);

		dialog.setBlockOnOpen(false);
		int code = dialog.open();

		/*
		 * Assert that window has been created.
		 */
		assertEquals(Window.OK, code);

		dialog.close();
	}

}
