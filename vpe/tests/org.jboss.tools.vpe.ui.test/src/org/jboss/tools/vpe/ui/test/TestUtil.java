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
import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.xpcom.XPCOMException;
import org.w3c.dom.Node;

/**
 * Class for importing project from jar file.
 * 
 * @author sdzmitrovich
 */
public class TestUtil {

	/** The Constant COMPONENTS_PATH. */
	private static final String COMPONENTS_PATH = "WebContent/pages"; //$NON-NLS-1$

	/** The Constant WEBCONTENT_PATH. */
	private static final String WEBCONTENT_PATH = "WebContent"; //$NON-NLS-1$
	
    /** Editor in which we open visual page. */
    protected final static String EDITOR_ID = "org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor"; //$NON-NLS-1$

	/** The Constant MAX_IDLE. */
	public static final long MAX_IDLE = 15*1000L;

	/**
	 * Gets the component path.
	 * 
	 * @param componentPage the component page
	 * @param projectName the project name
	 * 
	 * @return the component path
	 * 
	 * @throws CoreException the core exception
	 * @throws IOException 
	 */
	public static IResource getComponentPath(String componentPage,
			String projectName) throws CoreException, IOException {
		IProject project = ProjectsLoader.getInstance().getProject(projectName);
		if (project != null) {
			return project.getFolder(COMPONENTS_PATH).findMember(componentPage);
		}

		return null;
	}
	
	public static IResource getResource(String path,
			String projectName) throws CoreException, IOException {
		IProject project = ProjectsLoader.getInstance().getProject(projectName);
		if (project != null) {
			return project.findMember(path);
		}

		return null;
	}

	/**
	 * Gets the web content path.
	 * 
	 * @param componentPage the component page
	 * @param projectName the project name
	 * 
	 * @return the web content path
	 * 
	 * @throws CoreException the core exception
	 * @throws IOException 
	 */
	public static IResource getWebContentPath(String componentPage,
			String projectName) throws CoreException, IOException {
		IProject project = ProjectsLoader.getInstance().getProject(projectName);
		if (project != null) {
			return project.getFolder(WEBCONTENT_PATH).findMember(componentPage);
		}

		return null;
	}
	
	
	/**
	 * @param xmlScheme
	 * @param xmlSchemesRoot
	 * @return
	 */
	public static File getXmlTestFile(String xmlTestPath, String xmlTestsRoot) {
		return new File(xmlTestsRoot + File.separator + xmlTestPath);
	}

	/**
	 * Process UI input but do not return for the specified time interval.
	 * 
	 * @param waitTimeMillis the number of milliseconds
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
		//commented by Maksim Areshkau
		//because this method wait only for jobs which has been runned in current thread,
		//and don't wait for others. It can cause https://jira.jboss.org/jira/browse/JBIDE-5820 
		//https://jira.jboss.org/jira/browse/JBIDE-5821 
//		while (Job.getJobManager().currentJob() != null)
//				delay(100);	
		waitForIdle();
	}
	
	/**
	 * Wait for idle.
	 */
	public static void waitForIdle(long maxIdle) {
		long start = System.currentTimeMillis();
		while (!Job.getJobManager().isIdle()) {
			delay(500);
			if ( (System.currentTimeMillis()-start) > maxIdle ) 
				throw new RuntimeException("A long running task detected"); //$NON-NLS-1$
		}
	}
	
	public static void waitForIdle() {
		waitForIdle(MAX_IDLE);
	}

	/**
	 * find elements by name.
	 * 
	 * @param node -
	 * current node
	 * @param name -
	 * name element
	 * @param elements -
	 * list of found elements
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
	 * find all elements by name.
	 * 
	 * @param node -
	 * current node
	 * @param name -
	 * name element
	 * @param elements -
	 * list of found elements
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

	/**
	 * Utility function which returns node mapping by source position(line and position in line).
	 * 
	 * @param linePosition the line position
	 * @param lineIndex the line index
	 * @param itextViewer the itext viewer
	 * 
	 * @return node for specified src position
	 */
	@SuppressWarnings("restriction")
    public static Node getNodeMappingBySourcePosition(ITextViewer itextViewer, int lineIndex, int linePosition) {
		int offset = getLinePositionOffcet(itextViewer, lineIndex, linePosition);
		IndexedRegion treeNode = ContentAssistUtils.getNodeAt(itextViewer, offset);
		return (Node) treeNode;
	}
	
	/**
	 * Utility function which is used to calculate offcet in document by line number and character position.
	 * 
	 * @param linePosition the line position
	 * @param textViewer the text viewer
	 * @param lineIndex the line index
	 * 
	 * @return offcet in document
	 * 
	 * @throws IllegalArgumentException 	 */
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
	
    /**
     * Gets visual page editor controller.
     * 
     * @param part the part
     * 
     * @return {@link VpeController}
     */
    public static VpeController getVpeController(JSPMultiPageEditor part) {

        VpeEditorPart visualEditor = (VpeEditorPart) part.getVisualEditor();
        while(visualEditor.getController()==null) {
			if (!Display.getCurrent().readAndDispatch()) {
				Display.getCurrent().sleep();
				}
        }
        return visualEditor.getController();
    }

    /**
     * get xulrunner source page.
     * 
     * @param part - JSPMultiPageEditor
     * 
     * @return nsIDOMDocument
     */
    public static nsIDOMDocument getVpeVisualDocument(JSPMultiPageEditor part) {


        VpeController vpeController = TestUtil.getVpeController(part);

        // get xulRunner editor
        XulRunnerEditor xulRunnerEditor = vpeController.getXulRunnerEditor();

        // get dom document
        nsIDOMDocument document = xulRunnerEditor.getDOMDocument();

        return document;
    }

    /**
     * Perform test for rich faces component.
     * 
     * @param componentPage the component page
     * 
     * @return the ns IDOM element
     * 
     * @throws Throwable the throwable
     */
    public static nsIDOMElement performTestForRichFacesComponent(IFile componentPage) throws Throwable {
        nsIDOMElement rst = null;
        TestUtil.waitForJobs();

        // IFile file = (IFile)
        // TestUtil.getComponentPath(componentPage,getImportProjectName());
        IEditorInput input = new FileEditorInput(componentPage);

        TestUtil.waitForJobs();
        //
        JSPMultiPageEditor editor = (JSPMultiPageEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(
                input, EDITOR_ID, true);

        // get dom document
        nsIDOMDocument document = getVpeVisualDocument(editor);
        rst = document.getDocumentElement();
        // check that element is not null
        Assert.assertNotNull(rst);
        return rst;
    }
    
    /**
     * Fail.
     * 
     * @param t the t
     */
    public static void fail(Throwable t){
        Assert.fail("Test case was fail "+t.getMessage()+":"+t);
    }
}
