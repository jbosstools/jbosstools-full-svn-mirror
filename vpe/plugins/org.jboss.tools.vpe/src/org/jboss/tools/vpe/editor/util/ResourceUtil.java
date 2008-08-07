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

package org.jboss.tools.vpe.editor.util;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.jboss.tools.vpe.editor.bundle.BundleMap;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

/**
 * @author Evgenij Stherbin
 *
 */
@SuppressWarnings("restriction")
public class ResourceUtil {
    /**
     * Gets the bundle value.
     * 
     * @param value the value
     * @param offfset      *
     * param pageContext the page context
     * @param pageContext the page context
     * @param offset the offset
     * 
     * @return the bundle value
     */
    public static String getBundleValue(VpePageContext pageContext, String value, int offset) {
        BundleMap bundle = pageContext.getBundle();
        return bundle.getBundleValue(value, offset);

    }

    /**
     * get bundle.
     * 
     * @param pageContext the page context
     * @param attr the attr
     * 
     * @return the bundle value
     */
    public static String getBundleValue(VpePageContext pageContext, Attr attr) {
        return getBundleValue(pageContext, attr.getNodeValue(), ((IDOMAttr) attr).getValueRegionStartOffset());
    }
    
    

    /**
     * get bundle.
     * 
     * @param pageContext the page context
     * @param attr the attr
     * 
     * @return the bundle value
     */
    public static String getBundleValue(VpePageContext pageContext, Node attr) {
        if(attr instanceof IDOMAttr){
            return getBundleValue(pageContext, attr.getNodeValue(), ((IDOMAttr) attr).getValueRegionStartOffset());
        }else{
            return getBundleValue(pageContext,attr.getNodeValue(),0);
        }
    }

}
