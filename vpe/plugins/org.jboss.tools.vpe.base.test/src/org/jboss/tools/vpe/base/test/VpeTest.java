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

package org.jboss.tools.vpe.base.test;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.util.SelectionUtil;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The Class VpeTest.
 * 
 * @author Max Areshkau
 * 
 *         Base Class for VPE tests
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
		JspEditorPlugin
				.getDefault()
				.getPreferenceStore()
				.setValue(
						IVpePreferencesPage.INFORM_WHEN_PROJECT_MIGHT_NOT_BE_CONFIGURED_PROPERLY_FOR_VPE,
						false);
	}

	/**
	 * The Constructor.
	 * 
	 * @param importProjectName
	 *            * @param name the name
	 */

	public VpeTest(String name) {
		super(name);

	}

	/**
	 * Perform pre-test initialization.
	 * 
	 * @throws Exception
	 *             the exception
	 * 
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Platform.addLogListener(this);
		closeEditors();
		setException(null);
	}

	/**
	 * Perform post-test cleanup.
	 * 
	 * @throws Exception
	 *             the exception
	 * 
	 * @see TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
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
	 * @param status
	 *            the status
	 * @param plugin
	 *            the plugin
	 */
	public void logging(IStatus status, String plugin) {
		// Not perfect solution but at least now exceptions in other plug-ins aren't going to break VPE tests
		if (VpePlugin.PLUGIN_ID.equals(status.getPlugin())) {
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
	}
	
	/**
	 * Closes given {@code editor}.
	 */
    protected void closeEditor(IEditorPart editor) {
		boolean closed = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().closeEditor(editor, false);
		
		assertTrue(closed);
	}

	/**
	 * close all opened editors.
	 */
	protected void closeEditors() {
		// clean up defrerred events 
		while (Display.getCurrent().readAndDispatch());
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
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
	 * @param componentPage
	 *            the component page
	 * 
	 * @throws Throwable
	 *             the throwable
	 * @throws PartInitException
	 *             the part init exception
	 */
	protected void performTestForVpeComponent(IFile componentPage)
			throws PartInitException, Throwable {
		// IFile file = (IFile)
		// TestUtil.getComponentPath(componentPage,getImportProjectName());
		IEditorInput input = new FileEditorInput(componentPage);

		IEditorPart editor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().openEditor(input,
						getEditorID(), true);
		// here we wait for inintialization VPE controller
		TestUtil.getVpeController((JSPMultiPageEditor) editor);

		assertNotNull(editor);

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.closeAllEditors(true);

		if (getException() != null) {
			throw getException();
		}
	}

	/**
	 * Open JSPMultiPageEditor editor.
	 * 
	 * @param input
	 *            the input
	 * 
	 * @return the JSP multi page editor
	 * 
	 * @throws PartInitException
	 *             the part init exception
	 */
	protected JSPMultiPageEditor openEditor(IEditorInput input)
			throws PartInitException {

		// get editor
		JSPMultiPageEditor part = (JSPMultiPageEditor) PlatformUI
				.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.openEditor(input, getEditorID(), true);

		assertNotNull(part);
		// It is needed to fix issues related with deferred messages processing like
		// java.lang.NullPointerException
		//		at org.eclipse.wst.sse.ui.internal.style.SemanticHighlightingReconciler.reconcile(SemanticHighlightingReconciler.java:115)
		//		at org.eclipse.wst.sse.ui.internal.reconcile.DocumentRegionProcessor.endProcessing(DocumentRegionProcessor.java:119)
		//		at org.eclipse.wst.sse.ui.internal.reconcile.DirtyRegionProcessor.run(DirtyRegionProcessor.java:682)
		//		at org.eclipse.core.internal.jobs.Worker.run(Worker.java:54)
		// it happen because test goes so fast and editor is got closed until deferred events are processed
		while (Display.getCurrent().readAndDispatch());

		return part;

	}

	/**
	 * Open JSPMultiPageEditor editor.
	 * 
	 * @param input
	 *            the input
	 * 
	 * @return the JSP multi page editor
	 * 
	 * @throws PartInitException
	 *             the part init exception
	 */
	protected JSPMultiPageEditor openEditor(IFile input)
			throws PartInitException {

		// get editor
		JSPMultiPageEditor part = (JSPMultiPageEditor) IDE.openEditor(PlatformUI
				.getWorkbench().getActiveWorkbenchWindow().getActivePage(),
				input);

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
	 * @param exception
	 *            the exception to set
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
	 * @param checkWarning
	 *            the checkWarning to set
	 */
	protected void setCheckWarning(boolean checkWarning) {
		this.checkWarning = checkWarning;
	}

	/**
	 * Compares source nodes selection and visual selection
	 * 
	 * @param VPE
	 *            Editor part
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
			Node sourceNode = nodeMapping.getSourceNode();
			nsIDOMNode visualNode = nodeMapping.getVisualNode();
			if (!(sourceNode instanceof IDOMDocument)
					&& (visualNode != null)) {

				SelectionUtil.setSourceSelection(controller.getPageContext(),
						sourceNode, 1, 0);

				TestUtil.delay();

				assertNotNull(getSelectedNode(xulRunnerEditor));

				nsIDOMNode sample;
				if (sourceNode.getNodeType() == Node.TEXT_NODE) {

					sample = ((VpeElementMapping) nodeMapping).getElementData()
							.getNodesData().get(0).getVisualNode();
				} else {
					sample = visualNode;
				}

				assertEquals(sample, getSelectedNode(xulRunnerEditor));
			}
		}
	}

	/**
	 * Opens specified file in the VPE editor.
	 * 
	 * @param projectName
	 *            the name of the project
	 * @param fileName
	 *            the name of the file
	 * 
	 * @return VpeController
	 * @throws CoreException
	 * @throws IOException
	 */
	protected VpeController openInVpe(String projectName, String fileName)
			throws CoreException, IOException {
		// get test page path
		final IFile file = (IFile) TestUtil.getComponentPath(fileName,
				projectName);
		assertNotNull("Could not open specified file." //$NON-NLS-1$
				+ " componentPage = " + fileName //$NON-NLS-1$
				+ ";projectName = " + projectName, file); //$NON-NLS-1$

		final IEditorInput input = new FileEditorInput(file);
		assertNotNull("Editor input is null", input); //$NON-NLS-1$

		// open and get the editor
		final JSPMultiPageEditor part = openEditor(input);

		final VpeController vpeController = TestUtil.getVpeController(part);
		return vpeController;
	}

	/**
	 * find source element by "id"
	 * 
	 * @param controller
	 * @param elementId
	 * @return
	 */
	protected Element findSourceElementById(VpeController controller,
			String elementId) {

		return getSourceDocument(controller).getElementById(elementId);
	}

	/**
	 * find visual element by "id" entered in source part of vpe
	 * 
	 * @param controller
	 * @param elementId
	 * @return
	 */
	protected nsIDOMElement findElementById(VpeController controller,
			String elementId) {

		Element sourceElement = findSourceElementById(controller, elementId);

		VpeNodeMapping nodeMapping = controller.getDomMapping().getNodeMapping(
				sourceElement);

		if (nodeMapping == null)
			return null;

		return (nsIDOMElement) nodeMapping.getVisualNode();
	}
	
	protected String getEditorID(){
		return EDITOR_ID;
	}
	/**
	 * @author mareshkau
	 * @param xulRunnerEditor
	 * @return first node in nodes selection, if it selected
	 */
	protected static nsIDOMNode getSelectedNode(XulRunnerEditor xulRunnerEditor){
		return (xulRunnerEditor.getSelectedNodes().size()>0)?xulRunnerEditor.getSelectedNodes().get(0):null;
	}
}
