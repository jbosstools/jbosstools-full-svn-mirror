/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.test.editor;

import static org.jboss.tools.vpe.xulrunner.util.XPCOM.queryInterface;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jst.jsp.core.internal.domdocument.ElementImplForJSP;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.base.test.TestUtil;
import org.jboss.tools.vpe.base.test.VpeTest;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.VpeVisualDomBuilder;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.template.VpeTemplate;
import org.jboss.tools.vpe.editor.template.VpeToggableTemplate;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.ui.test.VpeUiTests;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ToggleClassCastTest_Jbide9790 extends VpeTest {

	private final String INITIALIZATION_FAILED = "Initialization failed!"; //$NON-NLS-1$
	private final String FILE_NAME = "facets.jsp"; //$NON-NLS-1$
	
	public ToggleClassCastTest_Jbide9790(String name) {
		super(name);
	}

	public void testCustomTemplate() throws Throwable {
		IFile file = (IFile) TestUtil.getComponentPath(FILE_NAME,
            	VpeUiTests.IMPORT_PROJECT_NAME);
        assertNotNull("Specified file does not exist: file name = " + FILE_NAME  //$NON-NLS-1$
        		+ "; project name = " + VpeUiTests.IMPORT_PROJECT_NAME, file); //$NON-NLS-1$
        /*
         * Open file in the VPE
         */
        IEditorInput input = new FileEditorInput(file);
        assertNotNull(INITIALIZATION_FAILED, input);
        
        JSPMultiPageEditor part = openEditor(input);
        assertNotNull(INITIALIZATION_FAILED, part);
        
        VpeEditorPart vep = (VpeEditorPart) part.getVisualEditor();
        assertNotNull(INITIALIZATION_FAILED, vep);
        
        VpeController vc = TestUtil.getVpeController(part);
        MozillaEditor visualEditor = vep.getVisualEditor();
        
        /*
         * Create custom template
         */
		VpeTemplate template = new TestVPETemplate();
		VpeVisualDomBuilder vvdb = vc.getVisualBuilder();
		
		/*
		 * Create source and visual nodes
		 */
		Element sourceNode = new ElementImplForJSP();
		VpeCreationData creationData = template.create(
				vc.getPageContext(), sourceNode, visualEditor.getDomDocument());
		nsIDOMNode visualNode = creationData.getNode();
		/*
		 * Put visual node to the DOMMapping
		 */
		nsIDOMNode newNode = queryInterface(visualNode, nsIDOMNode.class);
		VpeElementMapping nodeMapping = new VpeElementMapping(
				sourceNode, newNode, template, null, null, null); 
		vc.getDomMapping().mapNodes(nodeMapping);
		/*
		 * Update selected nodes in XulRunnerEditor 
		 */
		XulRunnerEditor xulRunnerEditor = visualEditor.getXulRunnerEditor();
		List<nsIDOMNode> selectedNodes = new ArrayList<nsIDOMNode>();
		selectedNodes.add(newNode);
		xulRunnerEditor.setSelectionRectangle(selectedNodes, 0);
		/*
		 * Call doToggle(..) method.
		 * It should work without exceptions.
		 */
		vvdb.doToggle(newNode);
	}
}

class TestVPETemplate extends VpeAbstractTemplate implements VpeToggableTemplate {
	@Override
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {
		nsIDOMElement div = visualDocument.createElement(HTML.TAG_DIV);
		div.setAttribute(VpeVisualDomBuilder.VPE_USER_TOGGLE_ID, "ID1"); //$NON-NLS-1$
		return new VpeCreationData(div);
	}
	@Override
	public void toggle(VpeVisualDomBuilder builder, Node sourceNode, String toggleId) {}
	@Override
	public void stopToggling(Node sourceNode) {}
}