package org.jboss.tools.vpe.ui.test.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.jst.jsp.editor.IVisualEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.base.test.TestUtil;
import org.jboss.tools.vpe.base.test.VpeTest;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.ui.test.VpeUiTests;

public class CustomSashFormTest extends VpeTest {

    private final String FILE_NAME_ONE = "hello.jsp"; //$NON-NLS-1$
    private final String FILE_NAME_TWO = "inputUserName.jsp"; //$NON-NLS-1$
    
    public CustomSashFormTest(String name) {
	super(name);
    }

    public void testSashChangesInJBIDE3140() throws Throwable {
        IFile file1 = (IFile) TestUtil.getComponentPath(FILE_NAME_ONE,
        	VpeUiTests.IMPORT_PROJECT_NAME);
        IFile file2 = (IFile) TestUtil.getComponentPath(FILE_NAME_TWO,
        	VpeUiTests.IMPORT_PROJECT_NAME);
        
        /*
         * Open file in the VPE
         */
        IEditorInput input = new FileEditorInput(file1);
        JSPMultiPageEditor part = openEditor(input);
        VpeEditorPart visualEditor = (VpeEditorPart) part.getVisualEditor();
        /*
         * Maximize visual part, switch to source part, 
         * close the file, open file again.
         * Sash should be restored without any exception. 
         */
        visualEditor.maximizeVisual();
        visualEditor.setVisualMode(IVisualEditor.SOURCE_MODE);
        TestUtil.waitForJobs();
        /*
         * Close editor part
         */
        ((WorkbenchPage)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()).getEditorPresentation().closeEditor(part);
        TestUtil.waitForJobs();
        TestUtil.delay(5000);
        input = new FileEditorInput(file1);
        part = openEditor(input);
        visualEditor = (VpeEditorPart) part.getVisualEditor();
        TestUtil.waitForJobs();
        ((WorkbenchPage)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()).getEditorPresentation().closeEditor(part);
        
        /*
         * Test it on another file
         */
        
        /*
         * Open file in the VPE
         */
        input = new FileEditorInput(file2);
        part = openEditor(input);
        visualEditor = (VpeEditorPart) part.getVisualEditor();
        
        /*
         * Maximize visual part, switch to source part, 
         * close the file, open file again.
         * Sash should be restored without any exception. 
         */
        visualEditor.maximizeVisual();
        visualEditor.setVisualMode(IVisualEditor.SOURCE_MODE);
        TestUtil.waitForJobs();
        /*
         * Close part without saving
         */
        ((WorkbenchPage)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()).getEditorPresentation().closeEditor(part);
        TestUtil.waitForJobs();
        part = openEditor(input);
        
        /*
         * If there are any exceptions it would be thrown.
         */
        TestUtil.waitForJobs();
	if(getException()!=null) {
		throw getException();
	}
        
    }
    
}
