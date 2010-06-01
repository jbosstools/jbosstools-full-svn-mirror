/*******************************************************************************
  * Copyright (c) 2007-2008 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.vpe.html.test.jbide;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.VpeDebug;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.html.test.HtmlAllTests;
import org.jboss.tools.vpe.ui.test.TestUtil;
import org.jboss.tools.vpe.ui.test.VpeTest;
import org.jboss.tools.vpe.xulrunner.util.DOMTreeDumper;
import org.mozilla.interfaces.nsIDOMDocument;

/**
 * Check if style of body before refresh equals style of body after refresh
 * 
 * @author yradtsevich
 *
 */
public class JBIDE3280Test extends VpeTest {

	private static final String TEST_PAGE_NAME="jbide3280/jbide3280.html"; //$NON-NLS-1$
	
	public JBIDE3280Test(String name) {
		super(name);
	}
	
	public void testJBIDE3280() throws Throwable {
		setException(null);

		IFile elementPageFile = (IFile) TestUtil.getComponentPath(
				TEST_PAGE_NAME, HtmlAllTests.IMPORT_PROJECT_NAME);

		IEditorInput input = new FileEditorInput(elementPageFile);

		TestUtil.waitForJobs();

		IEditorPart editor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().openEditor(input,
						EDITOR_ID, true);

		assertNotNull(editor);

		TestUtil.waitForJobs();

		VpeController controller = TestUtil.getVpeController((JSPMultiPageEditor) editor);

		final nsIDOMDocument oldDocument = controller.getXulRunnerEditor().getDOMDocument();
		final String oldStyle = oldDocument
				.getElementById(MozillaEditor.CONTENT_AREA_ID)
				.getAttribute(HTML.ATTR_STYLE);
		
		controller.visualRefresh();

		TestUtil.waitForIdle();

		final nsIDOMDocument newDocument = controller.getXulRunnerEditor().getDOMDocument();
		final String newStyle = newDocument
				.getElementById(MozillaEditor.CONTENT_AREA_ID)
				.getAttribute(HTML.ATTR_STYLE);
		
		
		// check if style of body before refresh equals style of body after refresh  
		assertEquals(oldStyle, newStyle);

		if (getException() != null) {
			throw getException();
		}
	}
}
