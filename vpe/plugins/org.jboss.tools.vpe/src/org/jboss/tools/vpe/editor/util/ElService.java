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


import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.common.el.core.ELReferenceList;
import org.jboss.tools.common.el.core.GlobalELReferenceList;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.vpe.editor.bundle.BundleMap;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.w3c.dom.Attr;
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

     public static final String DOLLAR_PREFIX = "${"; //$NON-NLS-1$

    private static final String SUFFIX = "}"; //$NON-NLS-1$

    public static final String SHARP_PREFIX = "#{"; //$NON-NLS-1$

    /**
     * Checks if is available.
     *
     * @param resourceFile the resource file
     *
     * @return true, if is available
     */
    public boolean isAvailable(IFile resourceFile) {
        boolean rst = false;
        final ResourceReference[] references = getAllResources(resourceFile);

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
     * @see IELService#replaceEl(IFile, String)
     */
    public String replaceEl(IFile resourceFile, String resourceString) {
    	
     //   Assert.isNotNull(resourceString);
        if ((resourceString == null) || (resourceFile == null)) {
            return ""; //$NON-NLS-1$
        }
        Assert.isNotNull(resourceFile);
        String rst = resourceString;
        final ResourceReference[] references = getAllResources(resourceFile);


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

		// yradtsevich: JBIDE-3576: EL expression overriding.
		// Values with higher precedence are replaced in the first place.
		ResourceReference[] sortedReferences = sortReferencesByScope(references);

		for (ResourceReference rf : sortedReferences) {
			final String dollarEl = DOLLAR_PREFIX + rf.getLocation() + SUFFIX;
			final String sharpEl = SHARP_PREFIX + rf.getLocation() + SUFFIX;

			if (resourceString.contains(dollarEl)) {
				result = result.replace(dollarEl, rf.getProperties());
			}
			if (resourceString.contains(sharpEl)) {
				result = result.replace(sharpEl, rf.getProperties());
			}
		}

		return result;
	}

	/**
	 * Creates a copy of {@code references} array and sorts its elements by
	 * scope value.
	 *
	 * References with the lowest scope value ({@link ResourceReference#FILE_SCOPE})
	 * become the first in the array and so on.
	 *
	 * @param references array to be sorted
	 * @return sorted copy of {@code references}
	 * @author yradtsevich
	 */
	private ResourceReference[] sortReferencesByScope(ResourceReference[] references) {
		ResourceReference[] sortedReferences = references.clone();

        Arrays.sort(sortedReferences, new Comparator<ResourceReference>() {
			public int compare(ResourceReference r1, ResourceReference r2) {
				return r1.getScope() - r2.getScope();
			}
        });

		return sortedReferences;
	}

    /**
     * Checks if is cloneable node.
     *
     * @param sourceNode the source node
     * @param pageContext the page context
     *
     * @return true, if is cloneable node
     */
    public boolean isELNode(VpePageContext pageContext,Node sourceNode) {
        boolean rst = false;
        if(isInCustomElementsAttributes(pageContext,sourceNode)){
        	return true;
        }
        // fix for JBIDE-3030
        if((pageContext.getVisualBuilder().getCurrentIncludeInfo())==null 
        		|| !(pageContext.getVisualBuilder().getCurrentIncludeInfo().getStorage() instanceof IFile)) {
        	return rst;
        }
        final IFile file = (IFile) pageContext.getVisualBuilder().getCurrentIncludeInfo().getStorage();

        if (((this.isAvailable(file) && this.isAvailableForNode(sourceNode, file))) 
        		|| isInResourcesBundle(pageContext, sourceNode)){
            rst = true;
        }else if(Jsf2ResourceUtil.isContainJSFExternalContextPath(sourceNode)){
        	rst = true;
        }if(Jsf2ResourceUtil.isContainJSF2ResourceAttributes(sourceNode)) {
            //added by Maksim Areshkau, see JBIDE-4812
        	rst = true;
        }
        return rst;
    }
    /**
     * Checks is node exist in source custom element attribute map and if so,
     * then retrun true
     * @param pageContext
     * @param sourceNode
     * @return
     */

    private static boolean isInCustomElementsAttributes(VpePageContext pageContext,Node sourceNode){
    	
    	String textValue = null;
    	if(sourceNode.getNodeType() == Node.TEXT_NODE) {
            textValue = sourceNode.getNodeValue();
            if (textValue != null) {
            	for(String key : pageContext.getCustomElementsAttributes().keySet()){
            		if(equalsExppression(textValue,key)){
            			return true;
            		}
            	}
            }
    	}else if(sourceNode.getNodeType() == Node.ELEMENT_NODE) {
			NamedNodeMap attributesMap = sourceNode.getAttributes();
			for(int i=0;i<attributesMap.getLength();i++) {
				Attr attr = (Attr) attributesMap.item(i);
				textValue =attr.getValue();
					if(textValue!=null) {
		            	for(String key : pageContext.getCustomElementsAttributes().keySet()){
		            		if(equalsExppression(textValue,key)){
		            			return true;
		            		}
		            	}
					}
			}
    	}
		return false;
    }
    /**
     *
     * @param pageContext
     * @param sourceNode
     * @return
     */
    public boolean isInResourcesBundle(VpePageContext pageContext, Node sourceNode) {
        boolean rst = findInResourcesBundle(pageContext, sourceNode);
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

                if ((textValue != null) && TextUtil.isContainsEl(textValue)) {
                    final String newValue = bundleMap.getBundleValue(textValue);

                    if (!textValue.equals(newValue)) {
                        rst = true;
                    }
                }
            }

            if (!rst) {
                final NamedNodeMap nodeMap = sourceNode.getAttributes();

                if (nodeMap != null && nodeMap.getLength() > 0) {
                    for (int i = 0; i < nodeMap.getLength(); i++) {
                        final Attr attr = (Attr) nodeMap.item(i);
                        final String value = attr.getValue();

                        if (value != null && TextUtil.isContainsEl(value)) {
                            final String value2 = bundleMap.getBundleValue(value);

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
     * Checks if is available for node.
     *
     * @param resourceFile the resource file
     * @param sourceNode the source node
     *
     * @return true, if is available for node
     */
    private boolean isAvailableForNode(Node sourceNode, IFile resourceFile) {
        boolean rst = findForNode(sourceNode, resourceFile);
        return rst;
    }


    /**
     * @param sourceNode
     * @param resourceFile
     * @return
     */
    private boolean findForNode(Node sourceNode, IFile resourceFile) {
        boolean rst = false;
        final ResourceReference[] references = getAllResources(resourceFile);
        String textValue = null;

        if (sourceNode.getNodeType() == Node.TEXT_NODE) {
            textValue = sourceNode.getNodeValue();
            if (textValue != null) {
                if (isInReferenceResourcesList(references, textValue)) {
                    return true;
                }
            }
        }
        final NamedNodeMap nodeMap = sourceNode.getAttributes();
        if ((nodeMap != null) && (nodeMap.getLength() > 0)) {
            for (int i = 0; i < nodeMap.getLength(); i++) {
                if (isInReferenceResourcesList(references, ((Attr) nodeMap.item(i)).getValue())) {
                    return true;

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

        	//FIXED FOR JBIDE-3149 by sdzmitrovich
			if (equalsExppression(value, ref.getLocation())) {
				return true;
			}
        }
        return rst;
    }

    protected ResourceReference[] getAllResources(IFile resourceFile) {
        ResourceReference[] rst = null;
        final IPath workspacePath = Platform.getLocation();

        final ResourceReference[] gResources = GlobalELReferenceList.getInstance().getAllResources(workspacePath);
        final ResourceReference[] elResources = ELReferenceList.getInstance().getAllResources(resourceFile);

        int size = (gResources == null ? 0 : gResources.length);
        size += (elResources == null ? 0 : elResources.length);
        rst = new ResourceReference[size];

        if ((gResources != null) && (gResources.length > 0)) {
        	System.arraycopy(gResources, 0,  rst,0, gResources.length);
        }
        if ((elResources != null) && (elResources.length > 0)) {
        	System.arraycopy(elResources, 0, rst,  gResources==null?0:gResources.length, elResources.length);
        }

        return rst;

    }

    public String replaceElAndResources(VpePageContext pageContext, String value) {


        String rst  = value;

        rst = ResourceUtil.getBundleValue(pageContext, value);
        //replace custom attributes
        rst = replaceCustomAttributes(pageContext,rst);
        
        if(Jsf2ResourceUtil.isExternalContextPathString(value)){
        	rst =  Jsf2ResourceUtil.processExternalContextPath(value);
        }
        
        if(Jsf2ResourceUtil.isJSF2ResourceString(rst)){
        	rst = Jsf2ResourceUtil.processCustomJSFAttributes(pageContext, rst);
        }
        //fix for JBIDE-3030
        if((pageContext.getVisualBuilder().getCurrentIncludeInfo()==null)
        		|| !(pageContext.getVisualBuilder().getCurrentIncludeInfo().getStorage() instanceof IFile)) {
        	return rst;
        }
        final IFile file = (IFile) pageContext.getVisualBuilder().getCurrentIncludeInfo().getStorage();
        rst = replaceEl(file, rst);
        
        return rst;
    }
    
    private String replaceCustomAttributes(VpePageContext pageContext, String value){
    	String result = value;
		for (String el : pageContext.getCustomElementsAttributes().keySet()) {
			final String dollarEl = DOLLAR_PREFIX + el + SUFFIX;
			final String sharpEl = SHARP_PREFIX + el + SUFFIX;

			if (result.contains(dollarEl)) {
				result = result.replace(dollarEl, pageContext.getCustomElementsAttributes().get(el));
			}
			if (result.contains(sharpEl)) {
				result = result.replace(sharpEl,pageContext.getCustomElementsAttributes().get(el));
			}
		}
    	return result;
    }

    private static boolean equalsExppression(String value, String expression) {

		final String dollarEl = String.valueOf(DOLLAR_PREFIX) + expression
				+ String.valueOf(SUFFIX);
		final String sharpEl = String.valueOf(SHARP_PREFIX) + expression
				+ String.valueOf(SUFFIX);

		if (value.contains(dollarEl) || value.contains(sharpEl)) {
			return true;
		}
		return false;
	}

}
