/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/


package org.jboss.tools.vpe.ui.test;


import java.util.Collection;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.sse.ui.internal.reconcile.StructuredRegionProcessor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.jboss.tools.common.model.util.ClassLoaderUtil;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.util.SelectionUtil;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * The Class VpeTest.
 * 
 * @author Max Areshkau
 * 
 * Base Class for VPE tests
 */
public class VpeTest extends TestCase implements ILogListener {

    /** Editor in which we open visual page. */
    protected final static String EDITOR_ID = "org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor"; //$NON-NLS-1$

    /** Collects exceptions. */
    private Throwable exception;

    /** check warning log. */
    private boolean checkWarning = false;
  
    // FIX for JBIDE-1628
    static {
        ClassLoaderUtil.init();
        // wait for initialization
        TestUtil.delay(3000);
        JspEditorPlugin.getDefault().getPreferenceStore().setValue(IVpePreferencesPage.INFORM_WHEN_PROJECT_MIGHT_NOT_BE_CONFIGURED_PROPERLY_FOR_VPE, false);
    }

    /**
     * The Constructor.
     * 
     * @param importProjectName      * @param name the name
     */

    public VpeTest(String name) {
        super(name);

    }

    /**
     * Perform pre-test initialization.
     * 
     * @throws Exception the exception
     * 
     * @see TestCase#setUp()
     */
    @Override
	protected void setUp() throws Exception {
        super.setUp();
        Platform.addLogListener(this);
        // String jbossPath = System.getProperty(
        // "jbosstools.test.jboss.home.4.2", "C:\\java\\jboss-4.2.2.GA");
        // JBossASAdapterInitializer.initJBossAS(jbossPath, new
        // NullProgressMonitor());
        closeEditors();
    }

    /**
     * Perform post-test cleanup.
     * 
     * @throws Exception the exception
     * 
     * @see TestCase#tearDown()
     */
    @Override
	protected void tearDown() throws Exception {
    	
    	boolean isJobsCheck = true;
		while (isJobsCheck){
			isJobsCheck = false;
		 	Job[] jobs = Job.getJobManager().find(null);
			for (Job job : jobs) {
				if (job instanceof StructuredRegionProcessor) {
					TestUtil.delay(50);
					isJobsCheck = true;
					break;
				}
			}
		}
   
        closeEditors();

        Platform.removeLogListener(this);
        
        super.tearDown();

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.core.runtime.ILogListener#logging(org.eclipse.core.runtime
     * .IStatus, java.lang.String)
     */
    /**
     * Logging.
     * 
     * @param status the status
     * @param plugin the plugin
     */
    public void logging(IStatus status, String plugin) {
        switch (status.getSeverity()) {
        case IStatus.ERROR:
            setException(status.getException());
            break;
        case IStatus.WARNING:
            if (isCheckWarning())
                setException(status.getException());
            break;
        default:
            break;
        }

    }

    /**
     * close all opened editors.
     */
    protected void closeEditors() {

        // wait
//        TestUtil.waitForJobs();
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IWorkbenchPart part = page.getViewReferences()[0].getPart(false);
        page.activate(part);
        // close
       	page.closeAllEditors(false);

    }
 
    /**
	 * 
	 * @return source document
	 */
	protected Document getSourceDocument(VpeController controller) {

		return controller.getSourceBuilder().getSourceDocument();

	}

    /**
     * Perfoms test for some page.
     * 
     * @param componentPage the component page
     * 
     * @throws Throwable the throwable
     * @throws PartInitException the part init exception
     */
    protected void performTestForVpeComponent(IFile componentPage) throws PartInitException, Throwable {
        TestUtil.waitForJobs();

        setException(null);

        // IFile file = (IFile)
        // TestUtil.getComponentPath(componentPage,getImportProjectName());
        IEditorInput input = new FileEditorInput(componentPage);

        TestUtil.waitForJobs();

        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, EDITOR_ID, true);
        //here we wait for inintialization VPE controller
        TestUtil.getVpeController((JSPMultiPageEditor) editor);
        
        assertNotNull(editor);

        TestUtil.waitForJobs();
        //JBIDE-1628
//        TestUtil.delay(1000);

        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(true);

        if (getException() != null) {
            throw getException();
        }
    }

    /**
     * Open JSPMultiPageEditor editor.
     * 
     * @param input the input
     * 
     * @return the JSP multi page editor
     * 
     * @throws PartInitException the part init exception
     */
    protected JSPMultiPageEditor openEditor(IEditorInput input) throws PartInitException {

        // get editor
        JSPMultiPageEditor part = (JSPMultiPageEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(
                input, EDITOR_ID, true);

        assertNotNull(part);
        return part;

    }

    /**
     * Gets the exception.
     * 
     * @return the exception
     */
    protected Throwable getException() {
        return exception;
    }

    /**
     * Sets the exception.
     * 
     * @param exception the exception to set
     */
    protected void setException(Throwable exception) {
        this.exception = exception;
    }

    /**
     * Checks if is check warning.
     * 
     * @return the checkWarning
     */
    protected boolean isCheckWarning() {
        return checkWarning;
    }

    /**
     * Sets the check warning.
     * 
     * @param checkWarning the checkWarning to set
     */
    protected void setCheckWarning(boolean checkWarning) {
        this.checkWarning = checkWarning;
    }
    /**
     * Compares source nodes selection and visual selection
     * @param VPE Editor part
     */
    protected void checkSourceSelection(JSPMultiPageEditor part) {
		// get controller
		VpeController controller = TestUtil.getVpeController(part);
		assertNotNull(controller);

		// get dommapping
		VpeDomMapping domMapping = controller.getDomMapping();

		assertNotNull(domMapping);

		// get source map
		Map<Node, VpeNodeMapping> sourceMap = domMapping.getSourceMap();
		assertNotNull(sourceMap);

		// get collection of VpeNodeMapping
		Collection<VpeNodeMapping> mappings = sourceMap.values();
		assertNotNull(mappings);

		// get editor control
		StyledText styledText = part.getSourceEditor().getTextViewer()
				.getTextWidget();
		assertNotNull(styledText);

		// get xulrunner editor
		XulRunnerEditor xulRunnerEditor = controller.getXulRunnerEditor();
		assertNotNull(xulRunnerEditor);

		for (VpeNodeMapping nodeMapping : mappings) {

			/**
			 * exclude out DomDocument ( it is added to mapping specially ) and
			 * nodes without visual representation
			 */
			if (!(nodeMapping.getSourceNode() instanceof IDOMDocument)
					&& (nodeMapping.getVisualNode() != null)) {

				SelectionUtil.setSourceSelection(controller.getPageContext(),
						nodeMapping.getSourceNode(), 1, 0);

				TestUtil.delay(50);

				assertNotNull(xulRunnerEditor.getLastSelectedNode());
				
				nsIDOMNode sample;
				if (nodeMapping.getSourceNode().getNodeType() == Node.TEXT_NODE
						&& ((VpeElementMapping) nodeMapping).getElementData() != null) {

					sample = ((VpeElementMapping) nodeMapping).getElementData().getNodesData().get(0).getVisualNode();
				}
				else {
					sample = nodeMapping.getVisualNode();
				}
				
				assertEquals(sample, xulRunnerEditor
						.getLastSelectedNode());
			}
		}
    }

	/**
	 * Opens specified file in the VPE editor.
	 * 
	 * @param projectName the name of the project
	 * @param fileName the name of the file
	 * 
	 * @return VpeController
	 * @throws CoreException 
	 */
	protected VpeController openInVpe(String projectName, String fileName) throws CoreException
				 {
		// get test page path
		final IFile file =
			(IFile) TestUtil.getComponentPath(fileName, projectName);
		assertNotNull("Could not open specified file."		//$NON-NLS-1$
				+ " componentPage = " + fileName			//$NON-NLS-1$
				+ ";projectName = " + projectName, file);	//$NON-NLS-1$

		final IEditorInput input = new FileEditorInput(file);
		assertNotNull("Editor input is null", input);		//$NON-NLS-1$

		// open and get the editor
		final JSPMultiPageEditor part = openEditor(input);

		final VpeController vpeController = TestUtil.getVpeController(part);
		return vpeController;
	}
}
