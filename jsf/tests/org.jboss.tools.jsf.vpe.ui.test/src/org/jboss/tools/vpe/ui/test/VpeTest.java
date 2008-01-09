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


import java.io.File;

import junit.framework.TestCase;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIDOMDocument;

/**
 * @author Max Areshkau
 *	
 *	Base Class for VPE tests
 */
public class VpeTest extends TestCase implements ILogListener {

	/**
	 * Editor in which we open visual page
	 */
	private final static String EDITOR_ID = "org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor";
	
	/**
	 * Collects exceptions
	 */
	private Throwable exception;
	
	// check warning log
	private  Boolean checkWarning;
	
	/**
	 * Contains project name with information for testing
	 */
	private  String importProjectName;
	
	/**
	 * Contains plugin resource path
	 */
	private String pluginResourcePath;
	/**
	 * 
	 * @param name
	 * @param importProjectName
	 */
	
	public VpeTest(String name, String importProjectName,String pluginResourcePath) {
		super(name);
		setImportProjectName(importProjectName);
		setPluginResourcePath(pluginResourcePath);
	}
	/**
	 * Perform pre-test initialization.
	 * 
	 * @throws Exception
	 * 
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {

		super.setUp();
		closeEditors();
		if (ResourcesPlugin.getWorkspace().getRoot().findMember(getImportProjectName()) == null) {
			closeEditors();
			TestUtil.importProjectIntoWorkspace((getPluginResourcePath()
					+ File.separator+getImportProjectName()),getImportProjectName());
		}
		Platform.addLogListener(this);
		closeEditors();
		}
	/**
	 * Perform post-test cleanup.
	 * 
	 * @throws Exception
	 * 
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {

		super.tearDown();
		
		closeEditors();

		TestUtil.removeProject(getImportProjectName());
		Platform.removeLogListener(this);

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.ILogListener#logging(org.eclipse.core.runtime.IStatus,
	 *      java.lang.String)
	 */
	public void logging(IStatus status, String plugin) {
		switch (status.getSeverity()) {
		case IStatus.ERROR:
			setException(status.getException());
			break;
		case IStatus.WARNING:
			if (getCheckWarning())
				setException(status.getException());
			break;
		default:
			break;
		}

	}
	/**
	 * close all opened editors
	 */
	protected void closeEditors() {

		// wait
		TestUtil.waitForJobs();

		// close
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.closeAllEditors(true);

	}
	/**
	 * get xulrunner source page
	 * 
	 * @param part -
	 *            JSPMultiPageEditor
	 * @return nsIDOMDocument
	 */
	protected nsIDOMDocument getVpeVisualDocument(JSPMultiPageEditor part) {

		VpeEditorPart visualEditor = (VpeEditorPart) part.getVisualEditor();
		VpeController vpeController = visualEditor.getController();

		// get xulRunner editor
		XulRunnerEditor xulRunnerEditor = vpeController.getXulRunnerEditor();

		// get dom document
		nsIDOMDocument document = xulRunnerEditor.getDOMDocument();

		return document;
	}
	/**
	 * Perfoms test for some page
	 * 
	 * @param componentPage
	 * @throws PartInitException
	 * @throws Throwable
	 */
	protected void performTestForJsfComponent(String componentPage)
			throws PartInitException, Throwable {
		TestUtil.waitForJobs();

		setException(null);

		IFile file = (IFile) TestUtil.getComponentPath(componentPage,getImportProjectName());
		IEditorInput input = new FileEditorInput(file);
		
		TestUtil.waitForJobs();
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.openEditor(input, EDITOR_ID, true);

		TestUtil.waitForJobs();
		TestUtil.delay(3000);
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.closeAllEditors(true);

		if (getException() != null) {
			throw getException();
		}
	}	
	/**
	 * Open JSPMultiPageEditor editor
	 * 
	 * @param input
	 * @return
	 * @throws PartInitException
	 */
	protected JSPMultiPageEditor openEditor(IEditorInput input)
			throws PartInitException {

		// get editor
		JSPMultiPageEditor part = (JSPMultiPageEditor) PlatformUI
				.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.openEditor(input, EDITOR_ID, true);

		// wait for jobs
		TestUtil.waitForJobs();
//		// wait full initialization of vpe
		TestUtil.delay(3000);

		return part;

	}
	/**
	 * @return the exception
	 */
	protected Throwable getException() {
		return exception;
	}
	/**
	 * @param exception the exception to set
	 */
	protected void setException(Throwable exception) {
		this.exception = exception;
	}
	/**
	 * @return the checkWarning
	 */
	protected Boolean getCheckWarning() {
		return checkWarning;
	}
	/**
	 * @param checkWarning the checkWarning to set
	 */
	protected void setCheckWarning(Boolean checkWarning) {
		this.checkWarning = checkWarning;
	}
	/**
	 * @return the importProjectName
	 */
	protected String getImportProjectName() {
		return importProjectName;
	}
	/**
	 * @param importProjectName the importProjectName to set
	 */
	protected void setImportProjectName(String importProjectName) {
		this.importProjectName = importProjectName;
	}
	/**
	 * @return the pluginResourcePath
	 */
	protected String getPluginResourcePath() {
		return pluginResourcePath;
	}
	/**
	 * @param pluginResourcePath the pluginResourcePath to set
	 */
	protected void setPluginResourcePath(String pluginResourcePath) {
		this.pluginResourcePath = pluginResourcePath;
	}

}
