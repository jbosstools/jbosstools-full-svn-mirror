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

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.util.SelectionUtil;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.ui.test.TestUtil;
import org.jboss.tools.vpe.ui.test.VpeTest;
import org.jboss.tools.vpe.ui.test.VpeUiTests;
import org.w3c.dom.Node;

/**
 * Performs tests for the VPE pop-up menu. 
 * 
 * @author dmaliarevich
 * @author yradtsevich
 */
public class VpePopupMenuTest extends VpeTest {
	private static final String[] REQUIRED_MENU_ITEMS = {
			MessageFormat.format(
					VpeUIMessages.ATTRIBUTES_MENU_ITEM, "h:outputText"),
			MessageFormat.format(
					VpeUIMessages.PARENT_TAG_MENU_ITEM, "h3"),
			VpeUIMessages.INSERT_AROUND,
			VpeUIMessages.INSERT_BEFORE,
			VpeUIMessages.INSERT_AFTER,
			VpeUIMessages.INSERT_INTO,
			VpeUIMessages.REPLACE_WITH,
			VpeUIMessages.STRIP_TAG_MENU_ITEM
	};
	private static final Point CODE_POINT = new Point(14, 25);

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
        
        // Selecting h:outputText tag for popup menu.
		int position = TestUtil.getLinePositionOffcet(
				sourceEditor.getTextViewer(), CODE_POINT.x, CODE_POINT.y);
		Node sourceNode = SelectionUtil.getNodeBySourcePosition(sourceEditor, position);

		// Creating popup menu.
		MenuManager menuManager = new MenuManager("#popup"); //$NON-NLS-1$
		final Menu contextMenu = menuManager.createContextMenu(visualEditor.getControl());

		VpeMenuCreator menuCreator = new VpeMenuCreator(menuManager, sourceNode);
		assertEquals("Menu Manger should have no items before creation.", //$NON-NLS-1$
				0, menuManager.getSize());
		menuCreator.createMenu();

		

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
				try {
					// Check pop-up menu items menu after it is displayed.
					Set<String> menuItemNames = new HashSet<String>();
					for (MenuItem item : contextMenu.getItems()) {
						menuItemNames.add(item.getText());
					}
					
					for (String itemName : REQUIRED_MENU_ITEMS) {
						assertTrue("There is no '" + itemName  //$NON-NLS-1$
								+ "' item in the pop-up menu." //$NON-NLS-1$
								, menuItemNames.contains(itemName));
					}
				} finally {
					// close pop-up menu
					menu.setVisible(false);
				}
			}
		});
		/*
		 * Show context menu on the display.
		 * Menu will be filled in with items and drew on the screen.
		 * If there are any exceptions - they'll be thrown.
		 * After displaying the menu will be automatically hidden.
		 */
		contextMenu.setVisible(true);
//		TestUtil.waitForJobs();
//        TestUtil.delay(5000);

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
