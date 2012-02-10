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
package org.jboss.tools.vpe.ui.test.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;
import org.jboss.tools.vpe.base.test.TestUtil;
import org.jboss.tools.vpe.base.test.VpeTest;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.ui.test.VpeUiTests;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsIDOMWindowInternal;
import org.mozilla.interfaces.nsIWebBrowser;

public class ScrollingTest_Jbide8701 extends VpeTest {

	private final String INITIALIZATION_FAILED = "Initialization failed!"; //$NON-NLS-1$
	private final String FILE_NAME = "facets.jsp"; //$NON-NLS-1$
	private final long DELAY_1S = 1*1000L;
	
	public ScrollingTest_Jbide8701(String name) {
		super(name);
	}
	
	public void _testScrolling_Jbide8701() throws Throwable {
		boolean testFinished = false;
		IFile file = (IFile) TestUtil.getComponentPath(FILE_NAME,
            	VpeUiTests.IMPORT_PROJECT_NAME);
        assertNotNull("Specified file does not exist: file name = " + FILE_NAME  //$NON-NLS-1$
        		+ "; project name = " + VpeUiTests.IMPORT_PROJECT_NAME, file); //$NON-NLS-1$
        /*
         * Open file in the VPE
         */
        IEditorInput input = new FileEditorInput(file);
        assertNotNull(INITIALIZATION_FAILED, input);
        
        JSPMultiPageEditor part = openEditor(input);
        assertNotNull(INITIALIZATION_FAILED, part);
        
        VpeEditorPart vep = (VpeEditorPart) part.getVisualEditor();
        assertNotNull(INITIALIZATION_FAILED, vep);
        
        VpeController vc = TestUtil.getVpeController(part);
        MozillaEditor visualEditor = vep.getVisualEditor();
        StructuredTextEditor sourceEditor = vc. getSourceEditor();
        if (visualEditor.getXulRunnerEditor() != null) {
			nsIWebBrowser webBrowser = visualEditor.getXulRunnerEditor().getWebBrowser();
			if (webBrowser != null) {
				/*
				 * Initialize mozilla browser content window
				 */
				nsIDOMWindow domWindow = webBrowser.getContentDOMWindow();
				nsIDOMWindowInternal windowInternal = org.jboss.tools.vpe.xulrunner.util.XPCOM
						.queryInterface(domWindow, nsIDOMWindowInternal.class);
				/*
				 * Set property to enable scroll correlation
				 */
				JspEditorPlugin.getDefault().getPreferenceStore().setValue(
						IVpePreferencesPage.SYNCHRONIZE_SCROLLING_BETWEEN_SOURCE_VISUAL_PANES, true);
				/*
				 * Set source position -- visual part should be scrolled.
				 */
				int scrollX = windowInternal.getScrollX();
				int scrollY = windowInternal.getScrollY();
				assertEquals("Initital visual position is wrong", 0, scrollX); //$NON-NLS-1$
				assertEquals("Initital visual position is wrong", 0, scrollY); //$NON-NLS-1$
				
				sourceEditor.setFocus();
				for (int i = 0; i < 15; i++) {
					pressKeyCode(PlatformUI.getWorkbench().getDisplay(), SWT.PAGE_DOWN);
				}
				TestUtil.delay(DELAY_1S);
				scrollX = windowInternal.getScrollX();
				scrollY = windowInternal.getScrollY();
				assertEquals("After 15x PG_DOWN presses visual position is wrong", 23, scrollX); //$NON-NLS-1$
				assertEquals("After 15x PG_DOWN presses visual position is wrong", 658, scrollY); //$NON-NLS-1$
		        /*
		         * Set visual position -- source part should be scrolled.
		         */
				domWindow.scrollTo(0,0);
				TestUtil.delay(DELAY_1S);
				int topIndex = sourceEditor.getTextViewer().getTopIndex();
				assertEquals("Top source line is wrong", 74, topIndex); //$NON-NLS-1$
				domWindow.scrollTo(0, windowInternal.getScrollMaxY()/2);
				TestUtil.delay(DELAY_1S);
				topIndex = sourceEditor.getTextViewer().getTopIndex();
				assertEquals("Top source line for the middle position is wrong", 638, topIndex); //$NON-NLS-1$
				testFinished = true;
			}
        }
        assertTrue("Test hasn't passed all steps", testFinished); //$NON-NLS-1$
	}
	
	public static void pressKeyCode(Display display, int keyCode) {
		Event keyPressed = new Event();
		keyPressed.keyCode = keyCode;
		keyPressed.type = SWT.KeyDown;
		display.post(keyPressed);
		Event keyReleased = new Event();
		keyReleased.keyCode = keyCode;
		keyReleased.type = SWT.KeyUp;
		display.post(keyReleased);
	}
}