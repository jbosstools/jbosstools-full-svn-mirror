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

package org.jboss.tools.jsf.vpe.jsf.test.jbide;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.jsf.vpe.jsf.test.CommonJBIDE2010Test;
import org.jboss.tools.jst.web.el.GlobalELReferenceList;
import org.jboss.tools.jst.web.rreferences.ResourceReference;
import org.jboss.tools.vpe.editor.util.ElService;

/**
 * Test case for testing global El expression substitution
 * @author Evgenij Stherbin
 *
 */
public class JBIDE2594Test extends CommonJBIDE2010Test {
    
    /**
     * 
     */
    protected static final String KEY_6 = "#{global.value1}"; //$NON-NLS-1$
    protected Map<String, String> globalElMap = new HashMap<String, String>();

    /**
     * @param name
     */
    public JBIDE2594Test(String name) {
        super(name);
    }
    
    
    
    
    public void testReplaceGlobalElVariable(){
        String replaceString= KEY_6+"images/test.gif"; //$NON-NLS-1$
        String replacedString = ElService.getInstance().replaceEl(file, replaceString);
        
        assertEquals("Should be equals " + globalElMap.get(KEY_6) + "images/test.gif", replacedString, globalElMap.get(KEY_6) //$NON-NLS-1$ //$NON-NLS-2$
                + "images/test.gif"); //$NON-NLS-1$
    }

    /**
     * Test replace attribute value.
     * 
     * @throws CoreException the core exception
     */
    public void testOverrideLocalVariable() throws CoreException {
        String string1 = KEY_1+"/images/smalle.gif"; //$NON-NLS-1$
        String replacedValue = ElService.getInstance().replaceEl(file, string1);

        assertEquals("Should be equals " + globalElMap.get(KEY_1) + "/images/smalle.gif", replacedValue, globalElMap.get(KEY_1) //$NON-NLS-1$ //$NON-NLS-2$
                + "/images/smalle.gif"); //$NON-NLS-1$

    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        IPath path = Platform.getLocation();
        
        assertNotNull("Path can't be null",path); //$NON-NLS-1$
        
        globalElMap.put(KEY_1, "/override/global/value/"); //$NON-NLS-1$
        globalElMap.put(KEY_6, "/global/value1/"); //$NON-NLS-1$
        
        ResourceReference[] entries = new ResourceReference[globalElMap.size()];
        int i = 0;
        for (Entry<String, String> string : globalElMap.entrySet()) {

            entries[i] = new ResourceReference(string.getKey(), ResourceReference.GLOBAL_SCOPE);
            entries[i].setProperties(string.getValue());
            i++;
        }
        setGlobalValues(entries, path);
        
        
    }


    protected void setGlobalValues(ResourceReference[] entries,IPath path) {
        GlobalELReferenceList.getInstance().setAllResources(path, entries);
    }



    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
