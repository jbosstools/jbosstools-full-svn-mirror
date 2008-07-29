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


package org.jboss.tools.vpe.test;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.ui.test.TestUtil;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;


/**
 * Test case for testing <a
 * href="https://jira.jboss.org/jira/browse/JBIDE-2582"> JBIDE-2582 </a> issue
 * 
 * @author Evgenij Stherbin
 */
public class JBIDE2582Test extends CommonJBIDE2010Test {
    
    /** The Constant DIR_TEST_PAGE_NAME_3. */
    protected static final String PAGE_1 = "JBIDE/2582/page1.xhtml";

    /**
     * The Constructor.
     * 
     * @param name the name
     */
    public JBIDE2582Test(String name) {
        super(name);

    }

    /**
     * Test rs substitution.
     * 
     * @throws Throwable the throwable
     */
    public void testRsSubstitution() throws Throwable {
        final nsIDOMElement rst = performTestForRichFacesComponent(file);

        assertNotNull(rst);

        final List<nsIDOMNode> elements = new ArrayList<nsIDOMNode>();

        TestUtil.findAllElementsByName(rst, elements, HTML.TAG_SPAN);

        assertEquals("Size should be equals 1", 1, elements.size());

        final nsIDOMElement spanOne = (nsIDOMElement) elements.get(0).queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);

        assertEquals("Style attribute should be substituted", "Hello", spanOne.getFirstChild().getNodeValue());

    }

    /**
     * Gets the open page name.
     * 
     * @return the open page name
     */
    protected String getOpenPageName() {
        return PAGE_1;
    }

}
