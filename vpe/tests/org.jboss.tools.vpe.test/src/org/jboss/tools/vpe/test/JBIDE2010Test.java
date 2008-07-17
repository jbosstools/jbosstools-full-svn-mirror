/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/


package org.jboss.tools.vpe.test;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.jsf.vpe.richfaces.test.RichFacesComponentTest;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.ui.test.TestUtil;
import org.jboss.tools.vpe.xulrunner.browser.util.DOMTreeDumper;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;


/**
 * Test case for testing https://jira.jboss.com:8443/jira/browse/JBIDE-2010
 * issue.
 * 
 * @author Eugeny Stherbin
 */
public class JBIDE2010Test extends CommonJBIDE2010Test {

    /**
     * The Constructor.
     * 
     * @param name the name
     */
    public JBIDE2010Test(String name) {
        super(name);
    }

    /**
     * Test el template simple.
     * 
     * @throws CoreException the core exception
     * @throws Throwable the throwable
     */
    public void testElTemplateSimple() throws CoreException, Throwable {
        final nsIDOMElement rst = performTestForRichFacesComponent((IFile) TestUtil.getComponentPath(DIR_TEST_PAGE_NAME_2,
                IMPORT_PROJECT_NAME));

        List<nsIDOMNode> elements = new ArrayList<nsIDOMNode>();

        // find "td" elements

        TestUtil.findAllElementsByName(rst, elements, HTML.TAG_SPAN);

        assertEquals("Count of divs should be equals 1", 1, elements.size());
        final nsIDOMElement spanOne = (nsIDOMElement) elements.get(0).queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);

        assertEquals("Style attribute should be substituted", VALUE_4, spanOne.getFirstChild().getNodeValue());
        int i = 0;
        DOMTreeDumper dumper = new DOMTreeDumper();
        dumper.dumpToStream(System.out, rst);
        // for(nsIDOMNode n:elements){
        // final nsIDOMElement d = (nsIDOMElement)
        // elements.get(i++).queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
        // System.out.println("JBIDE2010Test.testElTemplateSimple()-"+d.
        // getAttribute("style"));
        // }

    }

}
