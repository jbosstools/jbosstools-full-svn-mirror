/*******************************************************************************
* Copyright (c) 2007-2008 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributor:
*     Red Hat, Inc. - initial API and implementation
******************************************************************************/
package org.jboss.tools.jsf.vpe.jsf.test.jbide;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.jsf.vpe.jsf.test.JsfAllTests;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.rreferences.AbsoluteFolderReferenceList;
import org.jboss.tools.vpe.rreferences.ResourceReference;
import org.jboss.tools.vpe.ui.test.TestUtil;
import org.jboss.tools.vpe.ui.test.VpeTest;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;

/**
 * @author mareshkau
 * 
 * JUnit for JBIDE-924
 */
public class JBIDE924Test extends VpeTest{

	public JBIDE924Test(String name) {
		super(name);
	}
	
	public void testJBIDE924() throws Throwable {
		
        // get test page path
		setException(null);
        IFile file = (IFile) TestUtil.getComponentPath("JBIDE/924/pages/inputname.xhtml", //$NON-NLS-1$
        		JsfAllTests.IMPORT_PROJECT_NAME);
        assertNotNull("Could not open specified file " + file.getFullPath(), //$NON-NLS-1$
                file);

        IEditorInput input = new FileEditorInput(file);

        assertNotNull("Editor input is null", input); //$NON-NLS-1$

        // open and get editor
        JSPMultiPageEditor part = openEditor(input);
        
        VpeController vpeController = getVpeController(part);
    	nsIDOMDocument document = getVpeVisualDocument(part);
    	nsIDOMElement element = document.getDocumentElement();
    	List<nsIDOMNode> elements = new ArrayList<nsIDOMNode>();
    	TestUtil.findAllElementsByName(element, elements, HTML.TAG_TABLE);
    	 assertTrue("Faceletes loaded, but shouldn't",elements.size()==0); //$NON-NLS-1$
        ResourceReference[] resourceReference = new ResourceReference[1];
        resourceReference[0] = new ResourceReference(file.getParent().getParent().getLocation().toString() ,ResourceReference.FILE_SCOPE);
        AbsoluteFolderReferenceList.getInstance().setAllResources(file, resourceReference );
        TestUtil.delay(10000);
        vpeController.visualRefresh();
        TestUtil.delay(10000);
        document = getVpeVisualDocument(part);
        element = document.getDocumentElement();
    	 elements = new ArrayList<nsIDOMNode>();
    	TestUtil.findAllElementsByName(element, elements, HTML.TAG_TABLE);
    		
        assertTrue("Faceletes wasn't loaded from specified path",elements.size()>=1); //$NON-NLS-1$
        if(getException()!=null) {     	
        	
        	throw getException();
        }
	}

}
