/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.test.handlers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.State;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.bundle.BundleMap;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditorPart;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.VpeVisualDomBuilder;
import org.jboss.tools.vpe.handlers.PageDesignOptionsHandler;
import org.jboss.tools.vpe.handlers.PreferencesHandler;
import org.jboss.tools.vpe.handlers.RefreshHandler;
import org.jboss.tools.vpe.handlers.RotateEditorsHandler;
import org.jboss.tools.vpe.handlers.ShowBorderHandler;
import org.jboss.tools.vpe.handlers.ShowBundleAsELHandler;
import org.jboss.tools.vpe.handlers.ShowNonVisualTagsHandler;
import org.jboss.tools.vpe.handlers.ShowTextFormattingHandler;
import org.jboss.tools.vpe.ui.test.TestUtil;
import org.jboss.tools.vpe.ui.test.VpeTest;
import org.jboss.tools.vpe.ui.test.VpeUiTests;

/**
 * Class which created for testing VPE commands behavior, see
 * https://jira.jboss.org/browse/JBIDE-7383
 * 
 * @author mareshkau
 * 
 */
public class VpeCommandsTests extends VpeTest {

	private static String[] VPE_COMMAND_ID;
	private Command[] commands;
	private IHandlerService handlerService;

	private static final int ROTATION_NUM = 4;

	static {
		VPE_COMMAND_ID = new String[] { PageDesignOptionsHandler.COMMAND_ID,
				PreferencesHandler.COMMAND_ID, RefreshHandler.COMMAND_ID,
				RotateEditorsHandler.COMMAND_ID, ShowBorderHandler.COMMAND_ID,
				ShowBundleAsELHandler.COMMAND_ID,
				ShowNonVisualTagsHandler.COMMAND_ID,
				ShowTextFormattingHandler.COMMAND_ID };
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ICommandService commandService = (ICommandService) PlatformUI
				.getWorkbench().getService(ICommandService.class);
		commands = new Command[VPE_COMMAND_ID.length];
		for (int i = 0; i < commands.length; i++) {
			commands[i] = commandService.getCommand(VPE_COMMAND_ID[i]);
		}
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		IViewReference[] views = page.getViewReferences();
		for (IViewReference iViewReference : views) {
			page.hideView(iViewReference);
		}
		handlerService = (IHandlerService) PlatformUI.getWorkbench()
				.getService(IHandlerService.class);
	}

	public VpeCommandsTests(String name) {
		super(name);
	}

	/**
	 * Test VPE command state
	 * 
	 * @throws Throwable
	 */
	public void testCommandState() throws Throwable {
		JSPMultiPageEditor multiPageEditor = openInputUserNameJsp();
		checkCommandState(true);
		pageChange(multiPageEditor, 1);
		checkCommandState(false);
		pageChange(multiPageEditor, multiPageEditor.getPreviewIndex());
		checkCommandState(false);
		pageChange(multiPageEditor, 0);
		checkCommandState(true);
	}

	/**
	 * Test 'Rotate editors' toolbar button
	 * 
	 * @throws Throwable
	 */
	public void testRotateEditors() throws Throwable {

		JSPMultiPageEditor multiPageEditor = openInputUserNameJsp();
		VpeController vpeController = (VpeController) multiPageEditor
				.getVisualEditor().getController();
		VpeEditorPart editPart = vpeController.getPageContext().getEditPart();
		int oldVisualOrientation = editPart.getContainer().getOrientation();
		int prevVisualOrientation = oldVisualOrientation;

		IPreferenceStore preferences = JspEditorPlugin.getDefault()
				.getPreferenceStore();
		String oldPrefOrientation = preferences
				.getString(IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING);

		for (int i = 0; i < ROTATION_NUM; i++) {

			handlerService
					.executeCommand(RotateEditorsHandler.COMMAND_ID, null);
			TestUtil.delay(500);

			int newVisualOrientation = editPart.getContainer().getOrientation();
			String newPrefOrientation = preferences
					.getString(IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING);

			if (i != ROTATION_NUM - 1) {
				assertNotSame(oldPrefOrientation, newPrefOrientation);
				assertNotSame(prevVisualOrientation, newVisualOrientation);
			} else {
				assertEquals(oldPrefOrientation, newPrefOrientation);
				assertEquals(oldVisualOrientation, newVisualOrientation);
			}

			if (prevVisualOrientation == SWT.HORIZONTAL) {
				assertEquals(SWT.VERTICAL, newVisualOrientation);
			} else {
				assertEquals(SWT.HORIZONTAL, newVisualOrientation);
			}

			prevVisualOrientation = newVisualOrientation;
		}
	}

	/**
	 * Test 'Show border for unknown tags' toolbar button
	 * 
	 * @throws Throwable
	 */
	public void testShowBorderForUnknownTags() throws Throwable {
		
		JSPMultiPageEditor multiPageEditor = openInputUserNameJsp();

		Command command = getCommandById(ShowBorderHandler.COMMAND_ID);
		State state = command.getState(RegistryToggleState.STATE_ID);
		boolean oldToogleState = ((Boolean) state.getValue()).booleanValue();

		handlerService.executeCommand(ShowBorderHandler.COMMAND_ID, null);
		TestUtil.delay(500);

		boolean newToogleState = ((Boolean) state.getValue()).booleanValue();
		assertEquals(!oldToogleState, newToogleState);

		IPreferenceStore prefStore = JspEditorPlugin.getDefault()
				.getPreferenceStore();
		boolean prefBorderVisibility = prefStore
				.getBoolean(IVpePreferencesPage.SHOW_BORDER_FOR_UNKNOWN_TAGS);
		assertEquals(!oldToogleState, prefBorderVisibility);
		
		VpeController vpeController = (VpeController) multiPageEditor
				.getVisualEditor().getController();
		VpeVisualDomBuilder visualDomBuilder = vpeController.getVisualBuilder();
		boolean uiBorderVisibility = visualDomBuilder
				.isShowBorderForUnknownTags();
		assertEquals(!oldToogleState, uiBorderVisibility);
	}
	
	/**
	 * Test 'Show non-visual tags' toolbar button
	 * 
	 * @throws Throwable
	 */
	public void testShowNonVisualTags() throws Throwable {
		
		JSPMultiPageEditor multiPageEditor = openInputUserNameJsp();

		Command command = getCommandById(ShowNonVisualTagsHandler.COMMAND_ID);
		State state = command.getState(RegistryToggleState.STATE_ID);
		boolean oldToogleState = ((Boolean) state.getValue()).booleanValue();

		handlerService.executeCommand(ShowNonVisualTagsHandler.COMMAND_ID, null);
		TestUtil.delay(500);

		boolean newToogleState = ((Boolean) state.getValue()).booleanValue();
		assertEquals(!oldToogleState, newToogleState);

		IPreferenceStore prefStore = JspEditorPlugin.getDefault()
				.getPreferenceStore();
		boolean prefNonVisualTagsVisibility = prefStore
				.getBoolean(IVpePreferencesPage.SHOW_NON_VISUAL_TAGS);
		assertEquals(!oldToogleState, prefNonVisualTagsVisibility);
		
		VpeController vpeController = (VpeController) multiPageEditor
				.getVisualEditor().getController();
		VpeVisualDomBuilder visualDomBuilder = vpeController.getVisualBuilder();
		boolean uiNonVisualTagsVisibility = visualDomBuilder
				.isShowInvisibleTags();
		assertEquals(!oldToogleState, uiNonVisualTagsVisibility);
	}
	
	/**
	 * Test 'Show bundle's messages as EL expressions' toolbar button
	 * 
	 * @throws Throwable
	 */
	public void testShowBundleAsEL() throws Throwable {
		
		JSPMultiPageEditor multiPageEditor = openInputUserNameJsp();

		Command command = getCommandById(ShowBundleAsELHandler.COMMAND_ID);
		State state = command.getState(RegistryToggleState.STATE_ID);
		boolean oldToogleState = ((Boolean) state.getValue()).booleanValue();

		handlerService.executeCommand(ShowBundleAsELHandler.COMMAND_ID, null);
		TestUtil.delay(500);

		boolean newToogleState = ((Boolean) state.getValue()).booleanValue();
		assertEquals(!oldToogleState, newToogleState);

		IPreferenceStore prefStore = JspEditorPlugin.getDefault()
				.getPreferenceStore();
		boolean prefBundleAsELVisibility = prefStore
				.getBoolean(IVpePreferencesPage.SHOW_RESOURCE_BUNDLES_USAGE_AS_EL);
		assertEquals(!oldToogleState, prefBundleAsELVisibility);
		
		VpeController vpeController = (VpeController) multiPageEditor
				.getVisualEditor().getController();
		BundleMap bundle = vpeController.getPageContext().getBundle();
		boolean uiBundleAsELVisibility = bundle.isShowBundleUsageAsEL();
		assertEquals(!oldToogleState, uiBundleAsELVisibility);
	}

	private JSPMultiPageEditor openInputUserNameJsp() throws CoreException,
			IOException, SecurityException, IllegalArgumentException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		IFile vpeFile = (IFile) TestUtil.getComponentPath("inputUserName.jsp", //$NON-NLS-1$
				VpeUiTests.IMPORT_PROJECT_NAME);
		return openFileInVpe(vpeFile);
	}

	private JSPMultiPageEditor openFileInVpe(IFile fileToOpen)
			throws PartInitException, SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		// Open file in the VPE
		IEditorInput input = new FileEditorInput(fileToOpen);
		JSPMultiPageEditor multiPageEditor = openEditor(input);
		TestUtil.delay(500);
		// Open the 'Visual/Source' tab
		pageChange(multiPageEditor, 0);
		return multiPageEditor;
	}

	private void pageChange(JSPMultiPageEditor multiPageEditor, int index)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Method pageChange;
		pageChange = JSPMultiPageEditorPart.class.getDeclaredMethod(
				"setActivePage", new Class[] { int.class });
		pageChange.setAccessible(true);
		pageChange.invoke(multiPageEditor, index);
		multiPageEditor.pageChange(index);
		TestUtil.delay(1500);
	}

	// checks command state
	private void checkCommandState(boolean expected) {
		for (Command vpeCommand : commands) {
			assertEquals("Command " + vpeCommand.getId() + " should be active",
					expected, vpeCommand.isEnabled());
		}
	}

	private Command getCommandById(String commandId) throws Exception {

		for (Command vpeCommand : commands) {
			if (vpeCommand.getId().equals(commandId)) {
				return vpeCommand;
			}
		}
		throw new IllegalArgumentException("There is no commands with id " //$NON-NLS-1$
				+ commandId);
	}
}
