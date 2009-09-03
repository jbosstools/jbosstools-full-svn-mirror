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
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public abstract class ComponentContentTest extends VpeTest {

	public static final String XML_FILE_EXTENSION = ".xml"; //$NON-NLS-1$

	public ComponentContentTest(String name) {
		super(name);
	}

	/**
	 * 
	 * there are several conditions:
	 * 
	 * 1) xml file which contain tests must be named 'name of test page' +
	 * '.xml'
	 * 
	 * Example: test.jsp and test.jsp.xml
	 * 
	 * 2) a tag <test> in xml file and required element in test page must have
	 * the same attribute "id"
	 * 
	 * Example: <tests>... <test id="testId" > ...<tests> - in xml file and
	 * <html>... <x:testElement id="testId" > ... </html> - in test page
	 * 
	 * @param elementPagePath
	 *            - path to test page
	 * @throws Throwable
	 */
	protected void performContentTest(String elementPagePath) throws Throwable {
		setException(null);

		IFile elementPageFile = (IFile) TestUtil.getComponentPath(
				elementPagePath, getTestProjectName());

		IEditorInput input = new FileEditorInput(elementPageFile);

		TestUtil.waitForJobs();

		IEditorPart editor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().openEditor(input,
						EDITOR_ID, true);

		assertNotNull(editor);

		TestUtil.waitForJobs();

		VpeController controller = TestUtil.getVpeController((JSPMultiPageEditor) editor);

		// get xml test file
		File xmlTestFile = TestUtil.getComponentPath(
				elementPagePath + XML_FILE_EXTENSION, getTestProjectName())
				.getLocation().toFile();

		// get document
		Document xmlTestDocument = TestDomUtil.getDocument(xmlTestFile);
		assertNotNull("Can't get test file, possibly file not exists "+xmlTestFile,xmlTestDocument); //$NON-NLS-1$

		List<String> ids = TestDomUtil.getTestIds(xmlTestDocument);

		for (String id : ids) {

			compareElements(controller, xmlTestDocument, id, id);
		}

		if (getException() != null) {
			throw getException();
		}

	}

	/**
	 * 
	 * @param controller
	 * @param xmlTestDocument
	 * @param elementId
	 * @param xmlTestId
	 * @return
	 * @throws ComparisonException
	 */
	protected void compareElements(VpeController controller,
			Document xmlTestDocument, String elementId, String xmlTestId)
			throws ComparisonException {

		// get element by id
		nsIDOMElement vpeElement = findElementById(controller, elementId);
		assertNotNull("Cann't find element with id="+elementId,vpeElement); //$NON-NLS-1$

		// DOMTreeDumper dumper = new DOMTreeDumper(
		// VpeDebug.VISUAL_DUMP_PRINT_HASH);
		// dumper.dumpToStream(System.out, vpeElement);

		// get test element by id - get <test id="..." > element and get his
		// first child
		Element xmlModelElement = TestDomUtil.getFirstChildElement(TestDomUtil
				.getElemenById(xmlTestDocument, xmlTestId));

		assertNotNull(xmlModelElement);

		// compare DOMs
		try {
			TestDomUtil.compareNodes(vpeElement, xmlModelElement);
		} catch (ComparisonException e) {
			fail(e.getMessage());
		}

	}

	/**
	 * test for invisible tags
	 * 
	 * @param elementPagePath
	 *            - path to test page
	 * @param elementId
	 *            - id of element on page
	 * @throws Throwable
	 */
	protected void performInvisibleTagTest(String elementPagePath,
			String elementId) throws Throwable {
		setException(null);

		IFile elementPageFile = (IFile) TestUtil.getComponentPath(
				elementPagePath, getTestProjectName());

		IEditorInput input = new FileEditorInput(elementPageFile);

		TestUtil.waitForJobs();

		IEditorPart editor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().openEditor(input,
						EDITOR_ID, true);

		assertNotNull(editor);

		TestUtil.waitForJobs();

		VpeController controller = TestUtil.getVpeController((JSPMultiPageEditor) editor);

		// find source element and check if it is not null
		Element sourceELement = findSourceElementById(controller, elementId);
		assertNotNull(sourceELement);

		// find visual element and check if it is null
		nsIDOMElement visualElement = findElementById(controller, elementId);
		assertNull(visualElement);

		// set show invisible tag's flag to true
		controller.getVisualBuilder().setShowInvisibleTags(true);
		controller.visualRefresh();

//		TestUtil.waitForIdle();
		//added by Maksim Areshkau, when some non VPE background job 
		//TestUtil.waitForIdle() can be throw exception in time when vpe works normally
		long start = System.currentTimeMillis();
		visualElement = findElementById(controller, elementId);
		while (visualElement==null) {
			TestUtil.delay(500);
			if ( (System.currentTimeMillis()-start) > TestUtil.MAX_IDLE) {
			throw new RuntimeException("A long running task detected"); //$NON-NLS-1$
			}
			visualElement = findElementById(controller, elementId);
		}
		

		// find visual element and check if it is not null
		visualElement = findElementById(controller, elementId);
		assertNotNull(visualElement);

		// generate text for invisible tag
		String modelInvisibleTagText = generateInvisibleTagText(sourceELement
				.getNodeName());

		// generate dom document and get root element
		Element modelElement = TestDomUtil.getDocument(modelInvisibleTagText)
				.getDocumentElement();
		assertNotNull(modelElement);

		TestDomUtil.compareNodes(visualElement, modelElement);

		if (getException() != null) {
			throw getException();
		}

	}

	/**
	 * test for invisible tags which can have visible children
	 * 
	 * @param elementPagePath
	 *            - path to test page
	 * @param elementId
	 *            - id of element on page
	 * @throws Throwable
	 */
	protected void performInvisibleWrapperTagTest(String elementPagePath,
			String elementId) throws Throwable {
		setException(null);

		IFile elementPageFile = (IFile) TestUtil.getComponentPath(
				elementPagePath, getTestProjectName());

		IEditorInput input = new FileEditorInput(elementPageFile);

		TestUtil.waitForJobs();

		IEditorPart editor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().openEditor(input,
						EDITOR_ID, true);

		assertNotNull(editor);

		TestUtil.waitForJobs();

		VpeController controller = TestUtil.getVpeController((JSPMultiPageEditor) editor);

		// find source element and check if it is not null
		Element sourceELement = findSourceElementById(controller, elementId);
		assertNotNull(sourceELement);

		// find visual element and check if it is null
		nsIDOMElement visualElement = findElementById(controller, elementId);
		assertNull(visualElement);

		// check children of non-visual
		NodeList children = sourceELement.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			assertNotNull(findNode(controller, child));
		}

		// set show invisible tag's flag to true
		controller.getVisualBuilder().setShowInvisibleTags(true);
		controller.visualRefresh();

		TestUtil.waitForIdle();

		// find visual element and check if it is not null
		visualElement = findElementById(controller, elementId);
		assertNotNull(visualElement);

		// generate text for invisible tag
		String modelInvisibleTagText = generateInvisibleTagText(sourceELement
				.getNodeName());

		// generate dom document and get root element
		Element modelElement = TestDomUtil.getDocument(modelInvisibleTagText)
				.getDocumentElement();
		assertNotNull(modelElement);

		// compare elements
		TestDomUtil.compareNodes(visualElement, modelElement);

		if (getException() != null) {
			throw getException();
		}

	}

	/**
	 * 
	 * @param tagName
	 * @return
	 */
	private String generateInvisibleTagText(String tagName) {
		return "<span style=\"border: 1px dashed GREY; color: GREY; font-size: 12px;\" >" //$NON-NLS-1$
				+ tagName + "</span>"; //$NON-NLS-1$
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

	/**
	 * find visual element by "id" entered in source part of vpe
	 * 
	 * @param controller
	 * @param elementId
	 * @return
	 */
	protected nsIDOMNode findNode(VpeController controller, Node node) {

		VpeNodeMapping nodeMapping = controller.getDomMapping().getNodeMapping(
				node);

		if (nodeMapping == null)
			return null;

		return nodeMapping.getVisualNode();
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
	 * 
	 * @return
	 */
	abstract protected String getTestProjectName();

}
