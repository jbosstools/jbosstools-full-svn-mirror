/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jsf.vpe.seam.test;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Class for testing all Seam components
 * 
 * @author dsakovich@exadel.com
 * 
 */
public class SeamComponentTest extends TestCase implements ILogListener{

    private final static String EDITOR_ID = "org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor"; // $NON-NLS-1$
	private final static String TEST_PROJECT_JAR_PATH = "/seamtest.jar"; // $NON-NLS-1$

	// check warning log
	private final static boolean checkWarning = false;
	private Throwable exception;

	public SeamComponentTest(String name) {
		super(name);
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

		// TODO: Use TestSetup to create and remove project once for all tests 
		// not for every one 
		if(ResourcesPlugin.getWorkspace().getRoot().findMember("SeamTest")==null) {
		
			ImportSeamComponents.importSeamPages(SeamTestPlugin
					.getPluginResourcePath() + TEST_PROJECT_JAR_PATH);
			
			waitForJobs();
			waitForJobs();
			delay(5000);
		}
		Platform.addLogListener(this);
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
		Platform.removeLogListener(this);
	}

	/**
	 * Process UI input but do not return for the specified time interval.
	 * 
	 * @param waitTimeMillis
	 *                the number of milliseconds
	 */
	private void delay(long waitTimeMillis) {
		Display display = Display.getCurrent();
		if (display != null) {
			long endTimeMillis = System.currentTimeMillis() + waitTimeMillis;
			while (System.currentTimeMillis() < endTimeMillis) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			display.update();
		}
		// Otherwise, perform a simple sleep.
		else {
			try {
				Thread.sleep(waitTimeMillis);
			} catch (InterruptedException e) {
				// Ignored.
			}
		}
	}

	/**
	 * Wait until all background tasks are complete.
	 */
	public void waitForJobs() {
		while (Job.getJobManager().currentJob() != null)
			delay(5000);
	}

	public void testButton() throws PartInitException, Throwable {
		performTestForSeamComponent("button.xhtml"); // $NON-NLS-1$
	}
	
	public void testDecorate() throws PartInitException, Throwable {
		performTestForSeamComponent("decorate.xhtml"); // $NON-NLS-1$
	}
	
	public void testDiv() throws PartInitException, Throwable {
		performTestForSeamComponent("div.xhtml"); // $NON-NLS-1$
	}
	
	public void testFormattedText() throws PartInitException, Throwable {
		performTestForSeamComponent("formattedText.xhtml"); // $NON-NLS-1$
	}
	
	public void testSpan() throws PartInitException, Throwable {
		performTestForSeamComponent("span.xhtml"); // $NON-NLS-1$
	}
	
	public void testLabel() throws PartInitException, Throwable {
		performTestForSeamComponent("label.xhtml"); // $NON-NLS-1$
	}
	
	public void testLink() throws PartInitException, Throwable {
		performTestForSeamComponent("link.xhtml"); // $NON-NLS-1$
	}
	
	public void testMessage() throws PartInitException, Throwable {
		performTestForSeamComponent("message.xhtml"); // $NON-NLS-1$
	}
	
	public void testAllComponentsOnSinglePage() throws PartInitException, Throwable {
		performTestForSeamComponent("seamtest.xhtml"); // $NON-NLS-1$
	}
	
	private void performTestForSeamComponent(String componentPage) throws PartInitException, Throwable {
		waitForJobs();

		exception = null;
		IPath componentPath = ImportSeamComponents.getComponentPath(componentPage);
		
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(componentPath);
		IEditorInput input = new FileEditorInput(file);
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, EDITOR_ID, true);

		waitForJobs();
		delay(3000);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(true);

		if (exception != null) {
			throw exception;
		}
	}

	public void logging(IStatus status, String plugin) {
		switch (status.getSeverity()) {
		case IStatus.ERROR:
			exception = status.getException();
			break;
		case IStatus.WARNING:
			if (checkWarning)
				exception = status.getException();
			break;
		default:
			break;
		}

	}

}
