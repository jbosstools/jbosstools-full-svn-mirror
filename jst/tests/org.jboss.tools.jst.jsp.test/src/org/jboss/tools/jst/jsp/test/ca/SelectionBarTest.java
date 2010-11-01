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

package org.jboss.tools.jst.jsp.test.ca;

import junit.framework.TestCase;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.State;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.test.TestUtil;
import org.jboss.tools.test.util.TestProjectProvider;
import org.jboss.tools.test.util.WorkbenchUtils;

/**
 * Junit which tests selection bar command behaviour and logic
 * @author mareshkau
 *
 */
public class SelectionBarTest extends TestCase{
	protected IProject project = null;
    private TestProjectProvider provider = null;
    private Command toggleSelBarCommand;
    private State toggleSelBarState;
	
    public void setUp() throws Exception {
        provider = new TestProjectProvider("org.jboss.tools.jst.jsp.test", null, "JsfJbide1791Test",false);  //$NON-NLS-1$ //$NON-NLS-2$
        project = provider.getProject();
		ICommandService commandService =
			(ICommandService) PlatformUI.getWorkbench()
				.getService(ICommandService.class);
		toggleSelBarCommand = commandService.getCommand(
		"org.jboss.tools.jst.jsp.commands.showSelectionBar"); //$NON-NLS-1$
		toggleSelBarState= toggleSelBarCommand
		.getState("org.eclipse.ui.commands.toggleState"); //$NON-NLS-1$
    }

    protected void tearDown() throws Exception {
        if(provider != null) {
            provider.dispose();
        }
    }
    
	public void testSelectionBarCommandState() throws Throwable{
		assertEquals("check command enabled command status",false,toggleSelBarCommand.isEnabled()); //$NON-NLS-1$
		IEditorPart editorPart = WorkbenchUtils.openEditor(project.getName()+"/WebContent/pages/selectionBar.xhtml");  //$NON-NLS-1$
		
		assertTrue("Should be opened JSPMultiPage Editor", //$NON-NLS-1$
				editorPart instanceof JSPMultiPageEditor);
		JSPMultiPageEditor multiPageEditor = (JSPMultiPageEditor) editorPart;
		multiPageEditor.pageChange(0);
		assertEquals("check command enabled command status",true,toggleSelBarCommand.isEnabled()&&(Boolean)toggleSelBarState.getValue()); //$NON-NLS-1$
		multiPageEditor.pageChange(multiPageEditor.getPreviewIndex());
		assertEquals("check command enabled command status",false,toggleSelBarCommand.isEnabled()&&(Boolean)toggleSelBarState.getValue()); //$NON-NLS-1$
		multiPageEditor.pageChange(0);
		assertEquals("check command enabled command status",true,toggleSelBarCommand.isEnabled()&&(Boolean)toggleSelBarState.getValue()); //$NON-NLS-1$
	}
}
