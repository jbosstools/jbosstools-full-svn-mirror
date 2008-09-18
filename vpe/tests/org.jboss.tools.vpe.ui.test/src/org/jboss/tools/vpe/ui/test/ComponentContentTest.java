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
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.mozilla.interfaces.nsIDOMElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

		VpeController controller = getVpeController((JSPMultiPageEditor) editor);

		// get xml test file
		File xmlTestFile = TestUtil.getComponentPath(
				elementPagePath + XML_FILE_EXTENSION, getTestProjectName())
				.getLocation().toFile();

		// get document
		Document xmlTestDocument = TestDomUtil.getDocument(xmlTestFile);
		assertNotNull(xmlTestDocument);

		List<String> ids = TestDomUtil.getTestIds(xmlTestDocument);

		for (String id : ids) {

			assertEquals(true, compareElements(controller, xmlTestDocument, id,
					id));
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

}
