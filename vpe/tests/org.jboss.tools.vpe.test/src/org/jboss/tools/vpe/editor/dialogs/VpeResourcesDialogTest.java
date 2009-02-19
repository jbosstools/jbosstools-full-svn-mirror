/*******************************************************************************
 * Copyright (c) 2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.dialogs;

import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.common.model.ui.wizards.query.AbstractQueryDialog;
import org.jboss.tools.common.model.ui.wizards.query.IQueryDialog;
import org.jboss.tools.test.util.JobUtils;
import org.jboss.tools.test.util.ProjectImportTestSetup;
import org.jboss.tools.test.util.ResourcesUtils;
import org.jboss.tools.vpe.resref.VpeResourcesDialog;
import org.jboss.tools.vpe.ui.test.TestUtil;

import junit.framework.TestCase;

public class VpeResourcesDialogTest extends TestCase {
    
    private final String BUNDLE_NAME =  "org.jboss.tools.vpe.test"; //$NON-NLS-1$
    private final String PROJECT_PATH = "resources/TestProject"; //$NON-NLS-1$
    private final String PROJECT_NAME = "TestProject"; //$NON-NLS-1$
    private final String FILE_NAME = "hello.jsp"; //$NON-NLS-1$
    
    private IProject project;

    @Override
    protected void setUp() throws Exception {
	super.setUp();
	project = (IProject) ResourcesPlugin.getWorkspace().getRoot()
		.findMember(PROJECT_NAME);
	if (project == null) {
	    ProjectImportTestSetup setup = new ProjectImportTestSetup(this,
		    BUNDLE_NAME, PROJECT_PATH, PROJECT_NAME);
	    project = setup.importProject();
	}
	this.project = project.getProject();

	JobUtils.waitForIdle();
    }

    @Override
    protected void tearDown() throws Exception {
	super.tearDown();
	boolean saveAutoBuild = ResourcesUtils.setBuildAutomatically(false);
	try {
	    JobUtils.waitForIdle();
	    if (project != null) {
		project.close(new NullProgressMonitor());
		project.delete(true, new NullProgressMonitor());
		project = null;
		JobUtils.waitForIdle();
	    }
	} finally {
	    ResourcesUtils.setBuildAutomatically(saveAutoBuild);
	}
    }
    
    public void testVpeResourcesDialogOpen() throws Throwable {
        
        IFile file = (IFile) TestUtil.getComponentPath(FILE_NAME,
		PROJECT_NAME);
        
        assertNotNull("Specified file does not exist: fileName = " + FILE_NAME  //$NON-NLS-1$
		+ "; projectName = " + PROJECT_NAME, file); //$NON-NLS-1$
  
        VpeResourcesDialog dialog = new VpeResourcesDialog();
	Properties p = new Properties();
	p.setProperty("help", "VpeResourcesDialog"); //$NON-NLS-1$ //$NON-NLS-2$
	p.put("file", file); //$NON-NLS-1$
	p.put("model", PreferenceModelUtilities.getPreferenceModel()); //$NON-NLS-1$
	dialog.setObject(p);
	Shell shell = ModelUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
	IQueryDialog dialogWindow = new AbstractQueryDialog(shell);
	
	dialogWindow.setView(dialog.getView());
	dialogWindow.getDialog().create();
	dialog.getView().setDialog(dialogWindow.getDialog());
	dialogWindow.getDialog().setBlockOnOpen(false);
	dialogWindow.getDialog().open();
	int code = dialogWindow.getDialog().getReturnCode();
	
	/*
	 * Assert that window has been created.
	 */
	assertEquals(0, code);
	
	dialogWindow.getDialog().close();
    }
    
    
}
