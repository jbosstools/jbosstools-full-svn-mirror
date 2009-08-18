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

//import java.util.Properties;

//import org.eclipse.core.resources.IFile;
//import org.eclipse.swt.widgets.Shell;
//import org.jboss.tools.common.model.options.PreferenceModelUtilities;
//import org.jboss.tools.common.model.ui.ModelUIPlugin;
//import org.jboss.tools.common.model.ui.wizards.query.AbstractQueryDialog;
//import org.jboss.tools.common.model.ui.wizards.query.IQueryDialog;
//import org.jboss.tools.vpe.resref.VpeResourcesDialog;
//import org.jboss.tools.vpe.ui.test.TestUtil;
import org.jboss.tools.vpe.ui.test.VpeTest;
//import org.jboss.tools.vpe.ui.test.VpeUiTests;

public class VpeResourcesDialogTest extends VpeTest {
    
    private final String FILE_NAME = "hello.jsp"; //$NON-NLS-1$
    
    public VpeResourcesDialogTest(String name) {
	super(name);
    }

//    public void _testVpeResourcesDialogOpen() throws Throwable {
//        IFile file = (IFile) TestUtil.getComponentPath(FILE_NAME,
//        	VpeUiTests.IMPORT_PROJECT_NAME);
//        
//        assertNotNull("Specified file does not exist: fileName = " + FILE_NAME  //$NON-NLS-1$
//		+ "; projectName = " + VpeUiTests.IMPORT_PROJECT_NAME, file); //$NON-NLS-1$
//  
//        VpeResourcesDialog dialog = new VpeResourcesDialog();
//	Properties p = new Properties();
//	p.setProperty("help", "VpeResourcesDialog"); //$NON-NLS-1$ //$NON-NLS-2$
//	p.put("file", file); //$NON-NLS-1$
//	p.put("model", PreferenceModelUtilities.getPreferenceModel()); //$NON-NLS-1$
//	dialog.setObject(p);
//	Shell shell = ModelUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
//	IQueryDialog dialogWindow = new AbstractQueryDialog(shell);
//	
//	dialogWindow.setView(dialog.getView());
//	dialogWindow.getDialog().create();
//	dialog.getView().setDialog(dialogWindow.getDialog());
//	dialogWindow.getDialog().setBlockOnOpen(false);
//	dialogWindow.getDialog().open();
//	int code = dialogWindow.getDialog().getReturnCode();
//	
//	/*
//	 * Assert that window has been created.
//	 */
//	assertEquals(0, code);
//	
//	dialogWindow.getDialog().close();
//    }
    
    
}
