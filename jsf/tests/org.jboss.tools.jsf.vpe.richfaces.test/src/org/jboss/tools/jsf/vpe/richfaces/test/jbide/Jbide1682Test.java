/**
 * 
 */
package org.jboss.tools.jsf.vpe.richfaces.test.jbide;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.ui.test.TestUtil;
import org.jboss.tools.vpe.ui.test.VpeTest;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;

/**
 * @author dmaliarevich
 *
 */
public class Jbide1682Test extends VpeTest {
	public static final String IMPORT_PROJECT_NAME = "richFacesTest";

	public static final String TEST_PAGE_WITH_POPUP = "JBIDE/1682/JBIDE-1682-with-popup.xhtml";
	public static final String TEST_PAGE_WITHOUT_POPUP = "JBIDE/1682/JBIDE-1682-without-popup.xhtml";

	public Jbide1682Test(String name) {
		super(name);
	}
	
	public void testJBIDE_1682_With_Popup() throws Throwable {
		// wait
		TestUtil.waitForJobs();
		// set exception
		setException(null);

		// get test page path
		IFile file = (IFile) TestUtil.getComponentPath(
				TEST_PAGE_WITH_POPUP, IMPORT_PROJECT_NAME);
		
		assertNotNull("Could not open specified file " + file.getFullPath(),
				file);

		IEditorInput input = new FileEditorInput(file);

		assertNotNull("Editor input is null", input);
		// open and get editor
		JSPMultiPageEditor part = openEditor(input);

		// get dom document
		nsIDOMDocument document = getVpeVisualDocument(part);
		nsIDOMElement element = document.getDocumentElement();
		
		//check that element is not null
		assertNotNull(element);
		
		// get root node
		nsIDOMNode node = (nsIDOMNode) element
					.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);

		List<nsIDOMNode> elements = new ArrayList<nsIDOMNode>();
		
		// find "img" elements
		TestUtil.findElementsByName(node, elements, HTML.TAG_IMG);
		assertEquals(1, elements.size());

		// find "input" elements
		elements.clear();
		TestUtil.findElementsByName(node, elements, HTML.TAG_INPUT);
		assertEquals(1, elements.size());
		
	}
	
	public void testJBIDE_1682_Without_Popup() throws Throwable {
		// wait
		TestUtil.waitForJobs();
		// set exception
		setException(null);
		
		// get test page path
		IFile file = (IFile) TestUtil.getComponentPath(
				TEST_PAGE_WITHOUT_POPUP, IMPORT_PROJECT_NAME);
		
		assertNotNull("Could not open specified file " + file.getFullPath(),
				file);
		
		IEditorInput input = new FileEditorInput(file);
		
		assertNotNull("Editor input is null", input);
		// open and get editor
		JSPMultiPageEditor part = openEditor(input);
		
		// get dom document
		nsIDOMDocument document = getVpeVisualDocument(part);
		nsIDOMElement element = document.getDocumentElement();
		
		//check that element is not null
		assertNotNull(element);
		
		// get root node
		nsIDOMNode node = (nsIDOMNode) element
		.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);
		
		List<nsIDOMNode> elements = new ArrayList<nsIDOMNode>();
	
		// find "table" elements
		TestUtil.findElementsByName(node, elements, HTML.TAG_TABLE);
		assertEquals(1, elements.size());
		
	}
	
}
