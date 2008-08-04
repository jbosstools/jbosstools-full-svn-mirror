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


import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.jboss.tools.common.meta.action.impl.handlers.ReplaceSignificanceMessageImpl;
import org.jboss.tools.vpe.editor.bundle.BundleMap;
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
    private static final IELService INSTANCE = new ElService();

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
    public static synchronized IELService getInstance() {
        return INSTANCE;
    }

    /**
     * The Constructor.
     */
    private ElService() {
        super();
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
    //@Deprecated
    public String replaceEl(IFile resourceFile, String resourceString) {
     //   Assert.isNotNull(resourceString);
        if ((resourceString == null) || (resourceFile == null)) {
            return ""; //$NON-NLS-1$
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
    public boolean isCloneableNode(VpePageContext pageContext,Node sourceNode) {
        boolean rst = false;
        final IFile file = pageContext.getVisualBuilder().getCurrentIncludeInfo().getFile();
        
        if (((this.isAvailable(file) && this.isAvailableForNode(sourceNode, file))) || isInResourcesBundle(pageContext, sourceNode)){
            rst = true;
        }
        return rst;
    }

    /**
     * 
     * @param pageContext
     * @param sourceNode
     * @return
     */
    public boolean isInResourcesBundle(VpePageContext pageContext, Node sourceNode) {
        boolean rst = findInResourcesBundle(pageContext, sourceNode);

//        if (!rst && (sourceNode.getChildNodes() != null) && (sourceNode.getChildNodes().getLength() > 0)) {
//            for (int i = 0; i < sourceNode.getChildNodes().getLength(); i++) {
//                final Node node = sourceNode.getChildNodes().item(i);
//                
//                if (node.getNodeType() == Node.ELEMENT_NODE) {
//                    if((rst = findInResourcesBundle(pageContext, node))){
//                        break;
//                    }
//                    
//
//                }
//
//            }
//        }

        return rst;
    }


    /**
     * @param pageContext
     * @param sourceNode
     * @return
     */
    private boolean findInResourcesBundle(VpePageContext pageContext, Node sourceNode) {
        boolean rst = false;
      
        BundleMap bundleMap = pageContext.getBundle();
        if (bundleMap != null) {
            String textValue = null;
            
            if (sourceNode.getNodeType() == Node.TEXT_NODE) {
                textValue = sourceNode.getNodeValue();

                if ((textValue != null) && isContainsEl(textValue)) {
                    final String newValue = bundleMap.getBundleValue(textValue, 0);

                    if (!textValue.equals(newValue)) {
                        rst = true;
                    }
                }
            }
//            }else if ((sourceNode.getFirstChild() != null) && (sourceNode.getFirstChild().getNodeType() == Node.TEXT_NODE)) {
//                textValue = sourceNode.getFirstChild().getNodeValue();
//            }

            if (!rst) {
                final NamedNodeMap nodeMap = sourceNode.getAttributes();

                if (nodeMap != null && nodeMap.getLength() > 0) {
                    for (int i = 0; i < nodeMap.getLength(); i++) {
                        final Attr attr = (Attr) nodeMap.item(i);
                        final String value = attr.getValue();

                        if (value != null && isContainsEl(value)) {
                            final String value2 = bundleMap.getBundleValue(value, 0);

                            if (!value2.equals(value)) {
                                rst = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return rst;
    }


    /**
     * @param value
     * @return
     */
    //TODO E Sherbin It's shouldn't bee here
    private boolean isContainsEl(final String value) {
        return (value.contains("#{") || value.contains("${"));  //$NON-NLS-1$//$NON-NLS-2$
    }


    /**
     * Checks if is available for node.
     * 
     * @param resourceFile the resource file
     * @param sourceNode the source node
     * 
     * @return true, if is available for node
     */
    private boolean isAvailableForNode(Node sourceNode, IFile resourceFile) {
        boolean rst = findForNode(sourceNode, resourceFile);

//        if (!rst && (sourceNode.getChildNodes() != null) && (sourceNode.getChildNodes().getLength() > 0)) {
//            for (int i = 0; i < sourceNode.getChildNodes().getLength(); i++) {
//                final Node node = sourceNode.getChildNodes().item(i);
//
//                if (node.getNodeType() == Node.ELEMENT_NODE) {
//                    if((rst = findForNode((Element) node, resourceFile))){
//                        break;
//                    }
//                }
//
//            }
//        }

        return rst;
    }


    /**
     * @param sourceNode
     * @param resourceFile
     * @return
     */
    private boolean findForNode(Node sourceNode, IFile resourceFile) {
        boolean rst = false;
        final NamedNodeMap nodeMap = sourceNode.getAttributes();
        final ResourceReference[] references = ELReferenceList.getInstance().getAllResources(resourceFile);
        String textValue = null;
        
        if (sourceNode.getNodeType() == Node.TEXT_NODE/*
                                                       * sourceNode.getFirstChild
                                                       * () != null &&
                                                       * sourceNode
                                                       * .getFirstChild
                                                       * ().getNodeType() ==
                                                       * Node.TEXT_NODE
                                                       */) {
            textValue = sourceNode.getNodeValue();
            if (textValue != null) {
                if (isInReferenceResourcesList(references, textValue)) {
                    return true;
                }
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


    /**
     * Reverse replace.
     * 
     * @param resourceFile the resource file
     * @param replacedString the replaced string
     * 
     * @return the string
     */
    public String reverseReplace(IFile resourceFile, String replacedString) {
        String str = replacedString;

        final ResourceReference[] references = ELReferenceList.getInstance().getAllResources(resourceFile);

        if ((str != null) && (references != null) && (references.length > 0)) {
            for (ResourceReference rf : references) {
                if (replacedString.contains(rf.getProperties())) {
                    str = str.replace(rf.getProperties(), rf.getLocation());
                }
            }
        }
        return str;
    }


    public String replaceElAndResources(VpePageContext pageContext, Node attributeNode) {
        final IFile file = pageContext.getVisualBuilder().getCurrentIncludeInfo().getFile();
        
        String attribuString = null;
        if (attributeNode instanceof Attr) {
            attribuString = ((Attr) attributeNode).getValue();
        } else {
            attribuString = attributeNode.getNodeValue();
        }
        String rst  = attribuString;
        
        rst = ResourceUtil.getBundleValue(pageContext, attributeNode);
        rst = replaceEl(file, rst);

        return rst;
    }
    
//    private String replaceResourceBundle(VpePageContext context,Attr attributeNode){
//        
//    }
    
    

}
