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
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public abstract class ComponentContentTest extends VpeTest {

	public ComponentContentTest(String name) {
		super(name);
	}

	/**
	 * 
	 * @param elementPagePath
	 *            - path to test page
	 * @param elementId
	 *            - id of element which will be tested
	 * @param XmlSchemeContent
	 *            - probable content of test element
	 * 
	 * @throws Throwable
	 */
	protected void performContentTestByContent(String elementPagePath,
			String elementId, String xmlTestContent) throws Throwable {
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

		// get element by id
		nsIDOMElement vpeElement = findElementById(
				getVpeController((JSPMultiPageEditor) editor), elementId);
		assertNotNull(vpeElement);

		// get document
		Document modelDocument = TestDomUtil.getDocument(xmlTestContent);
		assertNotNull(modelDocument);

		// get element
		Element modelElement = modelDocument.getDocumentElement();
		assertNotNull(modelElement);

		assertEquals(true, TestDomUtil.compareNodes(vpeElement, modelElement));

		if (getException() != null) {
			throw getException();
		}

	}

	/**
	 * use if xml file contain only one test
	 * 
	 * @param elementPagePath
	 *            - path to test page
	 * @param elementId
	 *            - id of element which will be tested
	 * @param xmlTestPath
	 *            - path to xml file which contains tests of content
	 * @param xmlTestId
	 *            - current id of test in xml file
	 * @throws Throwable
	 */
	protected void performContentTest(String elementPagePath, String elementId,
			String xmlTestPath) throws Throwable {
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

		// get xml test file
		File xmlTestFile = TestUtil.getXmlTestFile(xmlTestPath, getTestsRoot());

		// get document
		Document xmlTestDocument = TestDomUtil.getDocument(xmlTestFile);
		assertNotNull(xmlTestDocument);

		// compare DOMs
		assertEquals(true, compareElements(
				getVpeController((JSPMultiPageEditor) editor), xmlTestDocument,
				elementId));

		if (getException() != null) {
			throw getException();
		}

	}

	/**
	 * 
	 * use if xml file contain several tests
	 * 
	 * @param elementPagePath
	 *            - path to test page
	 * @param elementId
	 *            - id of element which will be tested
	 * @param xmlTestPath
	 *            - path to xml file which contains tests of content
	 * @param xmlTestId
	 *            - current id of test in xml file
	 * @throws Throwable
	 */
	protected void performContentTest(String elementPagePath, String elementId,
			String xmlTestPath, String xmlTestId) throws Throwable {
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

		VpeController controller = getVpeController((JSPMultiPageEditor) editor);

		// get xml test file
		File xmlTestFile = TestUtil.getXmlTestFile(xmlTestPath, getTestsRoot());

		// get document
		Document xmlTestDocument = TestDomUtil.getDocument(xmlTestFile);
		assertNotNull(xmlTestDocument);

		assertEquals(true, compareElements(controller, xmlTestDocument,
				elementId, xmlTestId));

		if (getException() != null) {
			throw getException();
		}

	}

	/**
	 * use if it is necessary to check several elements at the same time
	 * 
	 * @param elementPagePath
	 *            - path to test page
	 * @param xmlTestPath
	 *            - path to xml file which contains tests of content
	 * @param mapId
	 *            - key is id of element which will be tested, value is
	 *            corresponding id of test in xml file
	 * @throws Throwable
	 */
	protected void performContentTest(String elementPagePath,
			String xmlTestPath, Map<String, String> mapId) throws Throwable {
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

		VpeController controller = getVpeController((JSPMultiPageEditor) editor);

		// get xml test file
		File xmlTestFile = TestUtil.getXmlTestFile(xmlTestPath, getTestsRoot());

		// get document
		Document xmlTestDocument = TestDomUtil.getDocument(xmlTestFile);
		assertNotNull(xmlTestDocument);

		for (String elementId : mapId.keySet()) {

			assertEquals(true, compareElements(controller, xmlTestDocument,
					elementId, mapId.get(elementId)));
		}

		if (getException() != null) {
			throw getException();
		}

	}

	protected boolean compareElements(VpeController controller,
			Document xmlTestDocument, String elementId, String xmlTestId) {

		// get element by id
		nsIDOMElement vpeElement = findElementById(controller, elementId);
		assertNotNull(vpeElement);

		// get test element by id - get <test id="..." > element and get his
		// first child
		Element xmlModelElement = TestDomUtil.getFirstChildElement(TestDomUtil
				.getElemenById(xmlTestDocument, xmlTestId));

		assertNotNull(xmlModelElement);

		// compare DOMs
		return TestDomUtil.compareNodes(vpeElement, xmlModelElement);

	}

	protected boolean compareElements(VpeController controller,
			Document xmlTestDocument, String elementId) {

		// get element by id
		nsIDOMElement vpeElement = findElementById(controller, elementId);
		assertNotNull(vpeElement);

		// get test element, get the first <test> tag in file
		Element xmlTestElement = TestDomUtil
				.getFirstChildElement(xmlTestDocument.getDocumentElement());
		assertNotNull(xmlTestElement);

		// model element is the first child of <test> tag
		Element xmlModelElement = TestDomUtil
				.getFirstChildElement(xmlTestElement);

		assertNotNull(xmlModelElement);

		// compare DOMs
		return TestDomUtil.compareNodes(vpeElement, xmlModelElement);

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

		Element sourceElement = getSourceDocument(controller).getElementById(
				elementId);

		VpeElementMapping elementMapping = controller.getDomMapping()
				.getNearElementMapping(sourceElement);

		return elementMapping.getVisualElement();
	}

	/**
	 * 
	 * @return
	 */
	abstract protected String getTestProjectName();

	/**
	 * 
	 * @return
	 */
	abstract protected String getTestsRoot();

}
