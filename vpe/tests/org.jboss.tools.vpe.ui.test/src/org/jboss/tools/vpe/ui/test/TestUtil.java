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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.jboss.tools.vpe.ui.test.beans.ImportBean;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.xpcom.XPCOMException;

/**
 * Class for importing project from jar file
 * 
 * @author sdzmitrovich
 * 
 */
public class TestUtil {

	private static final String COMPONENTS_PATH = "WebContent/pages"; //$NON-NLS-1$

	private static final String WEBCONTENT_PATH = "WebContent"; //$NON-NLS-1$

	private static final long MAX_IDLE = 30*60*1000L;


	static void importProjectIntoWorkspace(String path, String projectName) {

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				projectName);

		try {

			IOverwriteQuery overwrite = new IOverwriteQuery() {
				public String queryOverwrite(String pathString) {
					return ALL;
				}
			};

			ImportProvider importProvider = new ImportProvider();

			// need to remove from imported project "svn" files
			List<String> unimportedFiles = new ArrayList<String>();
			unimportedFiles.add(".svn"); //$NON-NLS-1$

			importProvider.setUnimportedFiles(unimportedFiles);

			// create import operation
			ImportOperation importOp = new ImportOperation(project
					.getFullPath(), new File(path), importProvider, overwrite);

			// import files just to project folder ( without old structure )
			importOp.setCreateContainerStructure(false);

			importOp.setContext(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell());

			// run import
			importOp.run(new NullProgressMonitor());

		} catch (InvocationTargetException ite) {
			VPETestPlugin.getDefault().logError(ite.getCause());
		} catch (InterruptedException ie) {
			VPETestPlugin.getDefault().logError(ie);
		}
	}

	/**
	 * 
	 * @return
	 * @throws CoreException
	 */
	public static IResource getComponentPath(String componentPage,
			String projectName) throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				projectName);
		if (project != null) {
			return project.getFolder(COMPONENTS_PATH).findMember(componentPage);

		}

		return null;
	}

	/**
	 * 
	 * @return
	 * @throws CoreException
	 */
	public static IResource getWebContentPath(String componentPage,
			String projectName) throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				projectName);
		if (project != null) {
			return project.getFolder(WEBCONTENT_PATH).findMember(componentPage);
		}

		return null;
	}

	/**
	 * 
	 * @throws CoreException
	 */
	static void removeProject(String projectName) throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				projectName);
		if (project != null) {
			project.delete(IResource.ALWAYS_DELETE_PROJECT_CONTENT,
					new NullProgressMonitor());
		}
	}

	/**
	 * Process UI input but do not return for the specified time interval.
	 * 
	 * @param waitTimeMillis
	 *            the number of milliseconds
	 */
	public static void delay(long waitTimeMillis) {
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
	public static void waitForJobs() {
		while (Job.getJobManager().currentJob() != null)
			delay(100);
	}
	
	public static void waitForIdle() {
		long start = System.currentTimeMillis();
		while (!Job.getJobManager().isIdle()) {
			delay(500);
			if ( (System.currentTimeMillis()-start) > MAX_IDLE ) 
				throw new RuntimeException("A long running task detected"); //$NON-NLS-1$
		}
	}

	/**
	 * find elements by name
	 * 
	 * @param node -
	 *            current node
	 * @param elements -
	 *            list of found elements
	 * @param name -
	 *            name element
	 */
	static public void findElementsByName(nsIDOMNode node,
			List<nsIDOMNode> elements, String name) {

		// get children
		nsIDOMNodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			nsIDOMNode child = children.item(i);

			// if current child is required then add his to list
			if (name.equalsIgnoreCase((child.getNodeName()))) {

				elements.add(child);

			} else {

				findElementsByName(child, elements, name);

			}
		}

	}

	/**
	 * find all elements by name
	 * 
	 * @param node -
	 *            current node
	 * @param elements -
	 *            list of found elements
	 * @param name -
	 *            name element
	 */
	static public void findAllElementsByName(nsIDOMNode node,
			List<nsIDOMNode> elements, String name) {

		try {
			nsIDOMNodeList list = node.getChildNodes();
			if (node.getNodeName().equalsIgnoreCase(name)) {
				elements.add(node);
			}
			for (int i = 0; i < list.getLength(); i++) {
				findAllElementsByName(list.item(i), elements, name);
			}
		} catch (XPCOMException e) {
			// Ignore
			return;
		}
	}

	static public List<ImportBean> createImportBeanList(String projectName,
			String resourcePath) {
		List<ImportBean> projectToImport = new ArrayList<ImportBean>();
		projectToImport.add(createImportBean(projectName, resourcePath));
		return projectToImport;
	}

	static public ImportBean createImportBean(String projectName,
			String resourcePath) {
		ImportBean importBean = new ImportBean();
		importBean.setImportProjectName(projectName);
		importBean.setImportProjectPath(resourcePath);
		return importBean;
	}
	/**
	 * Utility function which is used to calculate offcet in document by line number and character position
	 * 
	 * @param textViewer
	 * @param lineIndex
	 * @param linePosition
	 * @return offcet in document
	 * @throws IllegalArgumentException
	 */
	public static final int getLinePositionOffcet(ITextViewer textViewer, int lineIndex, int linePosition) {
		
		int resultOffcet = 0;
		
		if(textViewer==null) {
				
				throw new IllegalArgumentException("Text viewer shouldn't be a null"); //$NON-NLS-1$
		}	
		//lineIndex-1 becose calculating of line begibns in eclipse from one, but should be form zero
		resultOffcet=textViewer.getTextWidget().getOffsetAtLine(lineIndex-1);
		//here we get's tabs length
		//for more example you can see code org.eclipse.ui.texteditor.AbstractTextEditor@getCursorPosition() and class $PositionLabelValue
		int tabWidth = textViewer.getTextWidget().getTabs();
		int characterOffset=0;
		String currentString = textViewer.getTextWidget().getLine(lineIndex-1);
		int pos=1;
		for (int i= 0; (i < currentString.length())&&(pos<linePosition); i++) {
			if ('\t' == currentString.charAt(i)) {
				
				characterOffset += (tabWidth == 0 ? 0 : 1);
				pos+=tabWidth;
			}else{
				pos++;
				characterOffset++;
			}
		}
		resultOffcet+=characterOffset;
		if(textViewer.getTextWidget().getLineAtOffset(resultOffcet)!=(lineIndex-1)) {
				
				throw new IllegalArgumentException("Incorrect character position in line"); //$NON-NLS-1$
		}
		return resultOffcet;
	}
}
