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
package org.jboss.tools.jsf.vpe.jsf.test.jbide;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.ui.test.TestUtil;
import org.jboss.tools.vpe.ui.test.VpeTest;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Node;

/**
 * @author mareshkau
 * 
 */
public class JBIDE675Test extends VpeTest {

	private static final String IMPORT_PROJECT_NAME = "jsfTest"; //$NON-NLS-1$

	private static final String TEST_PAGE_NAME = "JBIDE/675/testChangeOnUserInputTextNode.xhtml"; //$NON-NLS-1$

	
	
	public JBIDE675Test(String name) {
		super(name);
	}

	/**
	 * Tests Base Input on Source Page
	 * 
	 * @throws Throwable
	 */
	public void testBaseTextInputOnPage() throws Throwable {
		// wait
		TestUtil.waitForJobs();
		// set exception
		setException(null);
		// Tests CA
		// get test page path
		IFile file = (IFile) TestUtil.getComponentPath(TEST_PAGE_NAME,
				IMPORT_PROJECT_NAME);
		assertNotNull("Could not open specified file " + TEST_PAGE_NAME, file); //$NON-NLS-1$

		IEditorInput input = new FileEditorInput(file);

		assertNotNull("Editor input is null", input); //$NON-NLS-1$

		// open and get editor
		JSPMultiPageEditor part = openEditor(input);

		StyledText styledText = part.getSourceEditor().getTextViewer()
				.getTextWidget();

		for (int i = 0; i < 20; i++) {

			styledText.setCaretOffset(339);
			IndexedRegion treeNode = ContentAssistUtils.getNodeAt(part
					.getSourceEditor().getTextViewer(), 339);
			Node node = (Node) treeNode;
			assertNotNull(node);

			VpeController vpeController = getVpeController(part);

			VpeDomMapping domMapping = vpeController.getDomMapping();

			VpeNodeMapping nodeMapping = domMapping.getNodeMapping(node);

			assertNotNull(nodeMapping);

			nsIDOMNode span = nodeMapping.getVisualNode();

			nsIDOMNode textNode = span.getFirstChild();

			assertEquals(textNode.getNodeType(), nsIDOMNode.TEXT_NODE);

			assertNotNull(textNode.getNodeValue());

			assertEquals(textNode.getNodeValue().trim(), node.getNodeValue()
					.trim());

			styledText.insert("t"); //$NON-NLS-1$
			TestUtil.delay(450);
			TestUtil.waitForJobs();
		}
		if(getException()!=null) {
			throw getException();
		}
	}
	/**
	 * Tests tag Input on Source Page
	 * 
	 * @throws Throwable
	 */
	public void testBaseTagInputOnPage() throws Throwable {
		// wait
		TestUtil.waitForJobs();
		// set exception
		setException(null);
		// Tests CA
		// get test page path
		IFile file = (IFile) TestUtil.getComponentPath("JBIDE/675/testUserInputOnTag.xhtml", //$NON-NLS-1$
				IMPORT_PROJECT_NAME);
		assertNotNull("Could not open specified file " + "JBIDE/675/testUserInputOnTag.xhtml", file); //$NON-NLS-1$

		IEditorInput input = new FileEditorInput(file);

		assertNotNull("Editor input is null", input); //$NON-NLS-1$

		// open and get editor
		JSPMultiPageEditor part = openEditor(input);

		StyledText styledText = part.getSourceEditor().getTextViewer()
				.getTextWidget();

		for (int i = 0; i < 20; i++) {

			styledText.setCaretOffset(311);
			IndexedRegion treeNode = ContentAssistUtils.getNodeAt(part
					.getSourceEditor().getTextViewer(), 311);
			Node node = (Node) treeNode;
			assertNotNull(node);

			VpeController vpeController = getVpeController(part);

			VpeDomMapping domMapping = vpeController.getDomMapping();

			VpeNodeMapping nodeMapping = domMapping.getNodeMapping(node);

			assertNotNull(nodeMapping);

			nsIDOMNode div = nodeMapping.getVisualNode();

			nsIDOMNode span = div.getFirstChild();
			
			nsIDOMNode textNode = span.getFirstChild();

			assertEquals(textNode.getNodeType(), nsIDOMNode.TEXT_NODE);

			assertNotNull(textNode.getNodeValue());
			assertNotNull(node.getNodeName());
			assertEquals(textNode.getNodeValue().trim(), node.getNodeName()
					.trim());

			styledText.insert("t"); //$NON-NLS-1$
			TestUtil.delay(450);
			TestUtil.waitForJobs();
		}
		if(getException()!=null) {
			throw getException();
		}
	}

	public void testInsertTagOnPage() throws Throwable {
		// wait
		TestUtil.waitForJobs();
		// set exception
		setException(null);
		// Tests CA
		// get test page path
		IFile file = (IFile) TestUtil.getComponentPath("JBIDE/675/testInsertTag.xhtml", //$NON-NLS-1$
				IMPORT_PROJECT_NAME);
		assertNotNull("Could not open specified file " + "JBIDE/675/testInsertTag.xhtml", file); //$NON-NLS-1$ //$NON-NLS-2$

		IEditorInput input = new FileEditorInput(file);

		assertNotNull("Editor input is null", input); //$NON-NLS-1$

		// open and get editor
		JSPMultiPageEditor part = openEditor(input);

		StyledText styledText = part.getSourceEditor().getTextViewer()
				.getTextWidget();

			styledText.setCaretOffset(285);
			styledText.insert("<test></test>"); //$NON-NLS-1$
			TestUtil.delay(450);
			TestUtil.waitForJobs();
			IndexedRegion treeNode = ContentAssistUtils.getNodeAt(part
					.getSourceEditor().getTextViewer(), 290);
			Node node = (Node) treeNode;
			assertNotNull(node);

			VpeController vpeController = getVpeController(part);

			VpeDomMapping domMapping = vpeController.getDomMapping();

			VpeNodeMapping nodeMapping = domMapping.getNodeMapping(node);

			assertNotNull(nodeMapping);

			nsIDOMNode div = nodeMapping.getVisualNode();

			nsIDOMNode span = div.getFirstChild();
			
			nsIDOMNode textNode = span.getFirstChild();

			assertEquals(textNode.getNodeType(), nsIDOMNode.TEXT_NODE);

			assertNotNull(textNode.getNodeValue());
			assertNotNull(node.getNodeName());
			assertEquals(textNode.getNodeValue().trim(), node.getNodeName()
					.trim());
			if(getException()!=null) {
				throw getException();
			}
	}

	public void testClosePageWhenBackgroundJobIsRun() throws Throwable {
		
		TestUtil.waitForJobs();
		
		// wait
		TestUtil.waitForJobs();
		// set exception
		setException(null);
		// Tests CA
		// get test page path
		IFile file = (IFile) TestUtil.getComponentPath("JBIDE/675/employee.xhtml", //$NON-NLS-1$
				IMPORT_PROJECT_NAME);
		assertNotNull("Could not open specified file " + "JBIDE/675/employee.xhtml", file); //$NON-NLS-1$ //$NON-NLS-2$

		IEditorInput input = new FileEditorInput(file);

		assertNotNull("Editor input is null", input); //$NON-NLS-1$

		// open and get editor
		final JSPMultiPageEditor part = openEditor(input);

		StyledText styledText = part.getSourceEditor().getTextViewer()
				.getTextWidget();
		styledText.setCaretOffset(951);
		styledText.insert("<a"); //$NON-NLS-1$
		styledText.setCaretOffset(953);
		for(int i=0;i<50;i++) {		
			styledText.insert(""+i); //$NON-NLS-1$
		}
		Job job = new UIJob("Close editor Job"){ //$NON-NLS-1$

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				
				 part.close(false);
				 part.dispose();
				return Status.OK_STATUS;
			}};
			job.setPriority(Job.SHORT);
			job.schedule(900);
		TestUtil.delay(450);
		if(getException()!=null) {
			throw getException();
		}
	}
		/**
		 * test Visual Editor Refresh method
		 * @throws Throwable
		 */
	public  void testVisualEditorRefreshAdnCloseWhenUIJobIsRunning() throws Throwable {
		TestUtil.waitForJobs();
		
		// wait
		TestUtil.waitForJobs();
		// set exception
		setException(null);
		// Tests CA
		// get test page path
		IFile file = (IFile) TestUtil.getComponentPath("JBIDE/675/employee.xhtml", //$NON-NLS-1$
				IMPORT_PROJECT_NAME);
		assertNotNull("Could not open specified file " + "JBIDE/675/employee.xhtml", file); //$NON-NLS-1$ //$NON-NLS-2$

		IEditorInput input = new FileEditorInput(file);

		assertNotNull("Editor input is null", input); //$NON-NLS-1$

		// open and get editor
		final JSPMultiPageEditor part = openEditor(input);

		StyledText styledText = part.getSourceEditor().getTextViewer()
				.getTextWidget();
		styledText.setCaretOffset(951);
		styledText.insert("<a"); //$NON-NLS-1$
		styledText.setCaretOffset(953);
		for(int i=0;i<100;i++) {		
			styledText.insert(""+i); //$NON-NLS-1$
			TestUtil.delay(30);
		}
		Job job = new UIJob("Close editor Job"){ //$NON-NLS-1$

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				
				 part.getVisualEditor().getController().visualRefresh();
				 part.close(false);
				 part.dispose();
				return Status.OK_STATUS;
			}};
			job.setPriority(Job.SHORT);
			job.schedule(900);
		TestUtil.delay(450);
		if(getException()!=null) {
			throw getException();
		}
	}
}
