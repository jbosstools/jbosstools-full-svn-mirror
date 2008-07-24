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


import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.vpe.editor.util.ElService;

/**
 * <p>
 * Test case for testing service {@link ElService}
 * <p>
 * See <a href="http://jira.jboss.com/jira/browse/JBIDE-2010">JBIDE-2010</a> issue
 * 
 */
public class ElPreferencesTestCase extends CommonJBIDE2010Test {


    /**
     * The Constructor.
     * 
     * @param name the name
     */
    public ElPreferencesTestCase(String name) {
        super(name);

    }

    /**
     * Test replace attribute value.
     * 
     * @throws CoreException the core exception
     */
    public void testReplaceAttributeValue() throws CoreException {
        String string1 = "#{beanA.property1}/images/smalle.gif";
        String replacedValue = ElService.getInstance().replaceEl(file, string1);

        assertEquals("Should be equals " + elValuesMap.get(KEY_1) + "/images/smalle.gif", replacedValue, elValuesMap.get(KEY_1)
                + "/images/smalle.gif");

    }

    /**
     * Test replace attribute value2.
     * 
     * @throws CoreException the core exception
     */
    public void testReplaceAttributeValue2() throws CoreException {
        String string1 = "#{beanA.property1}/images/#{beanA.property2}/path2/#{facesContext.requestPath}/smalle.gif";

        final String replacedValue = ElService.getInstance().replaceEl(file, string1);
        final String check = elValuesMap.get(KEY_1) + "/images/" + elValuesMap.get(KEY_2) + "/path2/" + elValuesMap.get(KEY_3)
                + "/smalle.gif";
        assertEquals("Should be equals " + check, check, replacedValue);

    }

    /**
     * Test replace not in set.
     */
    public void testReplaceNotInSet() {
        String string1 = "#{requestScope}/smalle.gif";

        assertEquals("Should be equals", string1, ElService.getInstance().replaceEl(file, string1));
    }
}
