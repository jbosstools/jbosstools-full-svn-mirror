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
package org.jboss.tools.vpe.editor.menu;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.util.SelectionUtil;
import org.jboss.tools.vpe.ui.test.TestUtil;
import org.jboss.tools.vpe.ui.test.VpeTest;
import org.jboss.tools.vpe.ui.test.VpeUiTests;
import org.w3c.dom.Node;

public class VpePopupMenuTest extends VpeTest {

	private final String INITIALIZATION_FAILED = "Initialization failed!"; //$NON-NLS-1$
	private final String FILE_NAME = "hello.jsp"; //$NON-NLS-1$

	public VpePopupMenuTest(String name) {
		super(name);
	}
	
	public void testPopupMenu() throws Throwable {
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
        
        /*
         * Selecting h:outputText tag for popup menu.
         */
		int position = TestUtil.getLinePositionOffcet(sourceEditor.getTextViewer(), 14, 25);
		Node sourceNode = SelectionUtil.getNodeBySourcePosition(sourceEditor, position);
		/*
		 * Creating popup menu.
		 */
		MenuManager menuManager = new MenuManager("#popup"); //$NON-NLS-1$
		final Menu contextMenu = menuManager.createContextMenu(visualEditor.getControl());
		contextMenu.addMenuListener(new MenuListener() {
			Menu menu = contextMenu;
			public void menuHidden(MenuEvent e) {
				Display.getCurrent().asyncExec(new Runnable() {
					public void run() {
						menu.dispose();
					}
				});
			}
			public void menuShown(MenuEvent e) {
				/*
				 * Close popup menu after it is displayed.
				 */
				menu.setVisible(false);
			}
		});
		VpeMenuCreator menuCreator = new VpeMenuCreator(menuManager, sourceNode);
		assertNotNull(INITIALIZATION_FAILED, menuCreator);
		assertEquals("Menu Manger should have no items before initialization.", //$NON-NLS-1$
				0, menuManager.getSize());
		menuCreator.createMenu();
		assertEquals(
				"After menu creation the Menu Manger should have 10 items in it.", //$NON-NLS-1$
				10, menuManager.getSize());
		/*
		 * Searching InsertContributionItem among menu manger items.
		 */
		IContributionItem[] menuItems = menuManager.getItems();
		InsertContributionItem insertItem = null;
		for (IContributionItem item : menuItems) {
			if (item instanceof InsertContributionItem) {
				insertItem = (InsertContributionItem) item;
			}
		}
		assertNotNull(
				"InsertContributionItem should exist in the list of menu items but it is not.", //$NON-NLS-1$
				insertItem);
		/*
		 * Show context menu on the display.
		 * Menu will be filled in with items and drew on the screen.
		 * If there are any exceptions - they'll be thrown.
		 * After displaying the menu will be automatically hidden.
		 */
		contextMenu.setVisible(true);

		/*
		 * It is possible to send synthetic mouse and key events in XULRunner 1.9.
		 * So the current test could be updated with sending nsIDOMEvent. 
		 */
//      VpeController vc = openInVpe(VpeUiTests.IMPORT_PROJECT_NAME, FILE_NAME);
//      assertNotNull(INITIALIZATION_FAILED, vc);
//      XulRunnerEditor xulRunnerEditor = vc.getXulRunnerEditor();
//      assertNotNull(INITIALIZATION_FAILED, xulRunnerEditor);
//		nsIInterfaceRequestor req = (nsIInterfaceRequestor) xulRunnerEditor
//				.getWebBrowser().getContentDOMWindow().queryInterface(
//						nsIInterfaceRequestor.NS_IINTERFACEREQUESTOR_IID);
//		assertNotNull(INITIALIZATION_FAILED, req);
//		nsIDOMWindowUtils utils = (nsIDOMWindowUtils) req
//				.getInterface(nsIDOMWindowUtils.NS_IDOMWINDOWUTILS_IID);
//		assertNotNull(INITIALIZATION_FAILED, utils);
//		utils.sendMouseEvent("mousedown", 10, 10, 0, 1, 0);
	}
	
}
