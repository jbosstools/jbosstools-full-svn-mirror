package org.jboss.tools.jsf.vpe.richfaces.test.jbide;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.jsf.vpe.richfaces.test.RichFacesAllTests;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.ui.test.TestUtil;
import org.jboss.tools.vpe.ui.test.VpeTest;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;

public class JBIDE1579Test extends VpeTest {

	private static final String TEST_PAGE_NAME_2BUTTONS = "JBIDE/1579/JBIDE-1579-2buttons.xhtml";
	private static final String TEST_PAGE_NAME_4BUTTONS = "JBIDE/1579/JBIDE-1579-4buttons.xhtml";

	public JBIDE1579Test(String name) {
		super(name);
	}
	
	public void testJBIDE_1579_2buttons() throws Throwable{
		// wait
		TestUtil.waitForJobs();
		// set exception
		setException(null);

		// get test page path
		IFile file = (IFile) TestUtil.getComponentPath(
				TEST_PAGE_NAME_2BUTTONS, RichFacesAllTests.IMPORT_PROJECT_NAME);
		
		assertNotNull("Could not open specified file " + file.getFullPath(),
				file);

		IEditorInput input = new FileEditorInput(file);

		assertNotNull("Editor input is null", input);
		// open and get editor
		JSPMultiPageEditor part = openEditor(input);

		// get dom document
		nsIDOMDocument document = TestUtil.getVpeVisualDocument(part);
		nsIDOMElement element = document.getDocumentElement();
		
		//check that element is not null
		assertNotNull(element);
		
		// get root node
		nsIDOMNode node = (nsIDOMNode) element
					.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);

		List<nsIDOMNode> elements = new ArrayList<nsIDOMNode>();
		
		// find "img" elements
		TestUtil.findElementsByName(node, elements, HTML.TAG_IMG);
		assertEquals(2, elements.size());
		
	}
	
	public void testJBIDE_1579_4buttons() throws Throwable{
		// wait
		TestUtil.waitForJobs();
		// set exception
		setException(null);

		// get test page path
		IFile file = (IFile) TestUtil.getComponentPath(
				TEST_PAGE_NAME_4BUTTONS, RichFacesAllTests.IMPORT_PROJECT_NAME);
		
		assertNotNull("Could not open specified file " + file.getFullPath(),
				file);

		IEditorInput input = new FileEditorInput(file);

		assertNotNull("Editor input is null", input);
		// open and get editor
		JSPMultiPageEditor part = openEditor(input);

		// get dom document
		nsIDOMDocument document = TestUtil.getVpeVisualDocument(part);
		nsIDOMElement element = document.getDocumentElement();
		
		//check that element is not null
		assertNotNull(element);
		
		// get root node
		nsIDOMNode node = (nsIDOMNode) element
					.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);

		List<nsIDOMNode> elements = new ArrayList<nsIDOMNode>();
		
		// find "img" elements
		TestUtil.findElementsByName(node, elements, HTML.TAG_IMG);
		assertEquals(4, elements.size());
		
	}

}
