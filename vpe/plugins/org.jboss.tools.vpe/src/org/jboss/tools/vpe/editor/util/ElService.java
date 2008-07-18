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

package org.jboss.tools.vpe.editor.util;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.css.ELReferenceList;
import org.jboss.tools.vpe.editor.css.ResourceReference;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


/**
 * The {@link IELService} implementation.
 * 
 * @author Eugeny Stherbin
 */
public final class ElService implements IELService {

    /** The Constant INSTANCE. */
    private static final ElService INSTANCE = new ElService();

    /**
     * Checks if is available.
     * 
     * @param resourceFile the resource file
     * 
     * @return true, if is available
     */
    public boolean isAvailable(IFile resourceFile) {
        boolean rst = false;
        final ResourceReference[] references = ELReferenceList.getInstance().getAllResources(resourceFile);

        if ((references != null) && (references.length > 0)) {
            rst = true;
        }
        return rst;
    }
    

    /**
     * Gets the singleton instance.
     * 
     * @return the singleton instance
     */
    public static IELService getInstance() {
        return INSTANCE;
    }

    /**
     * Replace el.
     * 
     * @param resourceFile the resource file
     * @param resourceString the resource string
     * 
     * @return the string
     * 
     * @see IELService#getReplacedElValue(IFile, String)
     */
    public String replaceEl(IFile resourceFile, String resourceString) {
     //   Assert.isNotNull(resourceString);
        if(resourceString == null){
            return "";
        }
        Assert.isNotNull(resourceFile);
        String rst = resourceString;
    
        final ResourceReference[] references = ELReferenceList.getInstance().getAllResources(resourceFile);

        if ((references != null) && (references.length > 0)) {
            rst = replace(resourceString, references);
        }
        return rst;
    }

    /**
     * Replace.
     * 
     * @param resourceString the resource string
     * @param references the references
     * 
     * @return the string
     */
    private String replace(String resourceString, ResourceReference[] references) {
        String result = resourceString;

        for (ResourceReference rf : references) {
            if (resourceString.contains(rf.getLocation())) {
                result = result.replace(rf.getLocation(),rf.getProperties());
            }
        }
        return result;
    }


    /**
     * Checks if is cloneable node.
     * 
     * @param sourceNode the source node
     * @param pageContext the page context
     * 
     * @return true, if is cloneable node
     */
    public boolean isCloneableNode(VpePageContext pageContext,Element sourceNode) {
        boolean rst = false;
        final IFile file = pageContext.getVisualBuilder().getCurrentIncludeInfo().getFile();
        
        if(this.isAvailable(file) && this.isAvailableForNode(sourceNode,file)){
            rst = true;
        }
        return rst;
    }


    /**
     * Checks if is available for node.
     * 
     * @param resourceFile the resource file
     * @param sourceNode the source node
     * 
     * @return true, if is available for node
     */
    private boolean isAvailableForNode(Element sourceNode, IFile resourceFile) {
        boolean rst = false;
        final NamedNodeMap nodeMap = sourceNode.getAttributes();
        final ResourceReference[] references = ELReferenceList.getInstance().getAllResources(resourceFile);
        String textValue = null;
        if (sourceNode.getFirstChild() != null && sourceNode.getFirstChild().getNodeType() == Node.TEXT_NODE) {
            textValue = sourceNode.getFirstChild().getNodeValue();
        }
        if (textValue != null) {
            if (isInReferenceResourcesList(references, textValue)) {
                return true;
            }
        }
        if ((nodeMap != null) && (nodeMap.getLength() > 0)) {
            for (int i = 0; i < nodeMap.getLength(); i++) {
                if (isInReferenceResourcesList(references, ((Attr) nodeMap.item(i)).getValue())) {
                    rst = true;
                    break;
                }
            }
        }

        return rst;
    }


    /**
     * Checks if is in reference resources list.
     * 
     * @param value the value
     * @param references the references
     * 
     * @return true, if is in reference resources list
     */
    private boolean isInReferenceResourcesList(ResourceReference[] references, String value) {
        boolean rst = false;

        for (ResourceReference ref : references) {
            if (value.contains(ref.getLocation())) {
                rst = true;
            }
        }
        return rst;
    }

}
