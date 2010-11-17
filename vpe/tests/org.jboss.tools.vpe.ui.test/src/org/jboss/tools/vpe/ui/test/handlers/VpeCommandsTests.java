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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.eclipse.core.commands.Command;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditorPart;
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
 * Class which created for testing VPE commands behavior,
 * see https://jira.jboss.org/browse/JBIDE-7383
 * 
 * @author mareshkau
 *
 */
public class VpeCommandsTests extends VpeTest{

	private static String[] VPE_COMMAND_ID;
	private Command[] commands;
	
	static {
		VPE_COMMAND_ID = new String[]{
				PageDesignOptionsHandler.COMMAND_ID,
				PreferencesHandler.COMMAND_ID,
				RefreshHandler.COMMAND_ID,
				RotateEditorsHandler.COMMAND_ID,
				ShowBorderHandler.COMMAND_ID,
				ShowBundleAsELHandler.COMMAND_ID,
				ShowNonVisualTagsHandler.COMMAND_ID,
				ShowTextFormattingHandler.COMMAND_ID
		};
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ICommandService commandService =
			(ICommandService) PlatformUI.getWorkbench()
				.getService(ICommandService.class);
		commands = new Command[VPE_COMMAND_ID.length];
		for(int i=0;i<commands.length;i++){
			commands[i] = commandService.getCommand(VPE_COMMAND_ID[i]);
		}
		IWorkbenchPage page = PlatformUI.getWorkbench()
		.getActiveWorkbenchWindow().getActivePage();
		IViewReference[] views = page.getViewReferences();
		for (IViewReference iViewReference : views) {
			page.hideView(iViewReference);
		}
	}

	public VpeCommandsTests(String name) {
		super(name);
	}
	
	/**
	 * Test VPE command state
	 * @throws Throwable
	 */
	 public void testCommandState() throws Throwable {
	   //initially all commands should be disabled
	   IFile vpeFile = (IFile) TestUtil.getComponentPath("inputUserName.jsp",
	           	VpeUiTests.IMPORT_PROJECT_NAME);
	            
	   /*
	   * Open file in the VPE
	   */
	   IEditorInput input = new FileEditorInput(vpeFile);
	   JSPMultiPageEditor multiPageEditor = openEditor(input);
	   TestUtil.delay(500);
	   pageChange(multiPageEditor, 0);
	   checkCommadState(true);
	   pageChange(multiPageEditor, 1);
	   checkCommadState(false);
	   pageChange(multiPageEditor, multiPageEditor.getPreviewIndex());
	   checkCommadState(false);
	   pageChange(multiPageEditor,0);
	   checkCommadState(true);
	 }
	 
	 private void pageChange(JSPMultiPageEditor multiPageEditor, int index) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		 Method pageChange;
		 pageChange = JSPMultiPageEditorPart.class.getDeclaredMethod("setActivePage", new Class[]{int.class});
		 pageChange.setAccessible(true);
		 pageChange.invoke(multiPageEditor, index);
		 multiPageEditor.pageChange(index);
		 TestUtil.delay(1500);
	 }
	 //checks command state
	 private void checkCommadState(boolean expected){
		 for (Command vpeCommand : commands) {
				assertEquals("Command "+vpeCommand.getId()+" should be active",expected,vpeCommand.isEnabled());
			}
	 }
}
