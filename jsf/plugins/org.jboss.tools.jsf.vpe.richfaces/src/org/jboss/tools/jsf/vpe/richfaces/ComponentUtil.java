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


package org.jboss.tools.jsf.vpe.richfaces;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.eclipse.wst.xml.core.internal.document.NodeListImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.jboss.tools.jsf.vpe.richfaces.template.util.RichFaces;
import org.jboss.tools.vpe.editor.bundle.BundleMap;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.css.ELReferenceList;
import org.jboss.tools.vpe.editor.util.ElService;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.xpcom.XPCOMException;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;


/**
 * The Class ComponentUtil.
 */
public class ComponentUtil {


    /** The Constant EMPTY_SELECT_ITEM_VALUE. */
    private static final String EMPTY_SELECT_ITEM_VALUE = "<f:selectItem/>";

    /** The Constant EMPTY_SELECT_ITEMS_VALUE. */
    private static final String EMPTY_SELECT_ITEMS_VALUE = "<f:selectItems/>";

    /** The Constant SELECT_ITEMS. */
    private static final String SELECT_ITEMS = "selectItems";

    /** The Constant PX_SUFFIX. */
    public static final String PX_SUFFIX = "px";

    /** The Constant SELECT_ITEM. */
    private static final String SELECT_ITEM = "selectItem";

    /**
     * Gets child of Facet element by name. If facet has a few children the
     * method will return first one.
     * 
     * @param sourceElement the source element
     * @param facetName the facet name
     * @param returnTextNode return child text node if facet doesn't have any child elements;
     * 
     * @return the facet
     */
    public static Node getFacet(Element sourceElement, String facetName, boolean returnTextNode) {
        NodeList children = sourceElement.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node instanceof Element && node.getNodeName() != null && node.getNodeName().indexOf(":facet") > 0) { //$NON-NLS-1$
                Element element = (Element) node;
                String name = element.getAttribute("name"); //$NON-NLS-1$
                if (facetName.equals(name)) {
                    NodeList childNodes = element.getChildNodes();
                    Text textNode = null;
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        Node child = childNodes.item(j);
                        if (child instanceof Element) {
                            return child;
                        } else if (child instanceof Text) {
                            textNode = (Text) child;
                        }
                    }
                    if (returnTextNode) {
                        return textNode;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Gets all facets of sourceElement. If facet has a few children the method
     * will return first one.
     * 
     * @param sourceElement the source element
     * @param facetName      *
     * param returnTextNode return child text node if facet doesn't have
     * any child elements;
     * @param returnTextNode the return text node
     * 
     * @return the facets
     */
    public static ArrayList<Node> getFacets(Element sourceElement, boolean returnTextNode) {
        ArrayList<Node> facets = new ArrayList<Node>();
        NodeList children = sourceElement.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node instanceof Element && "f:facet".equals(node.getNodeName())) { //$NON-NLS-1$
                Element element = (Element) node;
                NodeList childNodes = element.getChildNodes();
                Text textNode = null;
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node child = childNodes.item(j);
                    if (child instanceof Element) {
                        facets.add(child);
                        break;
                    } else if (child instanceof Text) {
                        textNode = (Text) child;
                    }
                }
                if (returnTextNode && facets.isEmpty()) {
                    facets.add(textNode);
                }
            }
        }
        return facets;
    }

    /**
     * Gets child of Facet element by name. If facet has a few children the
     * method will return first one.
     * 
     * @param sourceElement the source element
     * @param facetName the facet name
     * 
     * @return the facet
     */
    public static Element getFacet(Element sourceElement, String facetName) {
        return (Element) getFacet(sourceElement, facetName, false);
    }

    /**
     * Returns true if "rendered" attribute of source element does not contain
     * "false" value.
     * 
     * @param sourceElement the source element
     * 
     * @return true, if is rendered
     */
    public static boolean isRendered(Element sourceElement) {
        return !"false" //$NON-NLS-1$
        .equalsIgnoreCase(sourceElement.getAttribute("rendered")); //$NON-NLS-1$
    }

    /**
     * Sets CSS link in visual html document.
     * 
     * @param ext the ext
     * @param pageContext the page context
     * @param cssHref the css href
     */
    public static void setCSSLink(VpePageContext pageContext, String cssHref, String ext) {
        String pluginPath = RichFacesTemplatesActivator.getPluginResourcePath();
        IPath pluginFile = new Path(pluginPath);
        File cssFile = pluginFile.append(cssHref).toFile();
        if (cssFile.exists()) {
            String cssPath = "file:///" + cssFile.getPath(); //$NON-NLS-1$
            pageContext.getVisualBuilder().replaceLinkNodeToHead(cssPath, ext);
        }
    }

    /**
     * Gets the absolute resource path.
     * 
     * @param resourcePathInPlugin the resource path in plugin
     * 
     * @return the absolute resource path
     */
    public static String getAbsoluteResourcePath(String resourcePathInPlugin) {
        String pluginPath = RichFacesTemplatesActivator.getPluginResourcePath();
        IPath pluginFile = new Path(pluginPath);
        File file = pluginFile.append(resourcePathInPlugin).toFile();
        if (file.exists()) {
            return file.getAbsolutePath();
        } else {
            throw new IllegalArgumentException("Can't get path for " //$NON-NLS-1$
                    + resourcePathInPlugin);
        }
    }

    /**
     * Adds image as attribute to IMG tag.
     * 
     * @param img the img
     * @param fileImageName the file image name
     */
    public static void setImg(nsIDOMElement img, String fileImageName) {
        img.setAttribute("src", "file://" //$NON-NLS-1$//$NON-NLS-2$
                + getAbsoluteResourcePath(fileImageName));
    }

    /**
     * Returns all child source elements of component but facets.
     * 
     * @param returnTextNodes return child text nodes and elements or elements only;
     * @param sourceElement the source element
     * 
     * @return the children
     */
    public static List<Node> getChildren(Element sourceElement, boolean returnTextNodes) {
        ArrayList<Node> children = new ArrayList<Node>();
        NodeList nodeList = sourceElement.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node child = nodeList.item(i);
            if ((child instanceof Element || returnTextNodes) && (!child.getNodeName().equals("f:facet"))) { //$NON-NLS-1$
                children.add(child);
            }
        }
        return children;
    }

    /**
     * Returns all child source elements of component but facets.
     * 
     * @param sourceElement the source element
     * 
     * @return the children
     */
    public static List<Node> getChildren(Element sourceElement) {
        return getChildren(sourceElement, false);
    }

    /**
     * Returns all child visual elements of component but facets.
     * 
     * @param returnTextNodes the return text nodes
     * @param visualElement the visual element
     * 
     * @return the children
     * 
     * return returnTextNodes return child text nodes and elements or elements
     * only;
     */
    public static List<nsIDOMNode> getChildren(nsIDOMElement visualElement, boolean returnTextNodes) {
        ArrayList<nsIDOMNode> children = new ArrayList<nsIDOMNode>();
        nsIDOMNodeList nodeList = visualElement.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            nsIDOMNode child = nodeList.item(i);
            if ((child instanceof nsIDOMElement || returnTextNodes) && (!child.getNodeName().equals("f:facet"))) { //$NON-NLS-1$
                children.add(child);
            }
        }
        return children;
    }

    /**
     * Returns all child visual elements of component but facets.
     * 
     * @param visualElement the visual element
     * 
     * @return the children
     */
    public static List<nsIDOMNode> getChildren(nsIDOMElement visualElement) {
        return getChildren(visualElement, false);
    }

    /**
     * Copies all attributes from source node to visual node.
     * 
     * @param visualNode      *
     * param sourceNode the source node
     * @param sourceNode the source node
     * @param visualElement the visual element
     */
    public static void copyAttributes(Node sourceNode, nsIDOMElement visualElement) {
        NamedNodeMap namedNodeMap = sourceNode.getAttributes();
        for (int i = 0; i < namedNodeMap.getLength(); i++) {
            Node attribute = namedNodeMap.item(i);
            // added by Max Areshkau fix for JBIDE-1568
            try {

                visualElement.setAttribute(attribute.getNodeName(), attribute.getNodeValue());
            } catch (XPCOMException ex) {
                // if error-code not equals error for incorrect name throws
                // exception
                // error code is NS_ERROR_DOM_INVALID_CHARACTER_ERR=0x80530005
                if (ex.errorcode != 2152923141L) {
                    throw ex;
                }
                // else we ignore this exception
            }
        }
    }

    /**
     * Copies attributes from source node to visual node.
     * 
     * @param sourceElement the source element
     * @param attributes - list names of attributes which will copy
     * @param visualNode      *
     * param sourceNode * @param visualElement the visual element
     * @param visualElement the visual element
     */
    public static void copyAttributes(Element sourceElement, List<String> attributes, nsIDOMElement visualElement) {

        if (attributes == null)
            return;

        for (String attributeName : attributes) {

            String attributeValue = sourceElement.getAttribute(attributeName);
            if (attributeValue != null)
                visualElement.setAttribute(attributeName, attributeValue);
        }

    }

    /**
     * Returns true if sourceNode is Facet.
     * 
     * @param sourceNode the source node
     * @param facetName the facet name
     * 
     * @return true, if is facet
     */
    public static boolean isFacet(Node sourceNode, String facetName) {
        if (sourceNode != null && sourceNode instanceof Element && sourceNode.getNodeName().equals("f:facet")) { //$NON-NLS-1$
            String name = ((Element) sourceNode).getAttribute("name"); //$NON-NLS-1$
            if (facetName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns Style with default background image for default RichFaces skin.
     * 
     * @return the header backgound img style
     */
    public static String getHeaderBackgoundImgStyle() {
        return getBackgoundImgStyle("common/background.gif"); //$NON-NLS-1$
    }

    /**
     * Returns Style with background image for default RichFaces skin.
     * 
     * @param imagePath the image path
     * 
     * @return the backgound img style
     */
    public static String getBackgoundImgStyle(String imagePath) {
        String imgPath = ComponentUtil.getAbsoluteResourcePath(imagePath);
        String style = "background-image: url(file:///" //$NON-NLS-1$
                + imgPath.replace('\\', '/') + ");"; //$NON-NLS-1$
        return style;
    }

    // public static createStyleClass(String content){
    //
    // }

    /**
     * Returns value of attribute.
     * 
     * @param attributeName the attribute name
     * @param sourceElement the source element
     * 
     * @return the attribute
     */
    public static String getAttribute(Element sourceElement, String attributeName) {
        String attribute = sourceElement.getAttribute(attributeName);
        if (attribute == null) {
            attribute = ""; //$NON-NLS-1$
        }
        return attribute;
    }

    /**
     * Returns value of attribute.
     * 
     * @param attributeName the attribute name
     * @param sourceElement the source element
     * 
     * @return the attribute
     */
    public static String getAttribute(nsIDOMElement sourceElement, String attributeName) {
        String attribute = sourceElement.getAttribute(attributeName);
        if (attribute == null) {
            attribute = ""; //$NON-NLS-1$
        }
        return attribute;
    }

    /**
     * Parameter present.
     * 
     * @param style the style
     * @param name the name
     * 
     * @return true, if parameter present
     */
    public static boolean parameterPresent(String style, String name) {
        if (style != null && style.length() > 0) {
            String[] styles = style.split(";"); //$NON-NLS-1$
            for (int i = 0; i < styles.length; i++) {
                String[] pair = styles[i].split(":"); //$NON-NLS-1$
                if (pair[0].trim().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the style parametr.
     * 
     * @param style the style
     * @param name the name
     * 
     * @return the style parametr
     */
    public static String getStyleParametr(String style, String name) {
        if (style != null && style.length() > 0) {
            String[] styles = style.split(";"); //$NON-NLS-1$
            for (int i = 0; i < styles.length; i++) {
                String[] pair = styles[i].split(":"); //$NON-NLS-1$
                if (pair[0].trim().equals(name)) {
                    return pair[1].trim();
                }
            }
        }
        return null;
    }

    /**
     * Adds the parameter.
     * 
     * @param element the element
     * @param style the style
     * 
     * @return the string
     */
    public static String addParameter(String style, String element) {
        String s = style.trim();
        return style + (s.length() == 0 || s.endsWith(";") ? "" : ";") //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
                + element;
    }

    /**
     * Adds image as attribute to IMG tag from users worcpace.
     * 
     * @param img img element to which set picture
     * @param pageContext Page Context
     * @param undefinedImgName default image when image is undefined
     * @param fileImageName image name
     */
    public static void setImgFromResources(VpePageContext pageContext, nsIDOMElement img, String fileImageName, String undefinedImgName) {
        IEditorInput input = pageContext.getEditPart().getEditorInput();
        IPath inputPath = getInputParentPath(input);
        String path = ElService.getInstance().replaceEl(pageContext.getVisualBuilder().getCurrentIncludeInfo().getFile(), fileImageName);
        File file = new File(inputPath.toOSString() + File.separator + path);
        if (file.exists()) {
            img.setAttribute(HtmlComponentUtil.HTML_ATR_SRC, HtmlComponentUtil.FILE_PROTOCOL + inputPath.toOSString() + File.separator
                    + path);
        } else {
            img.setAttribute(HtmlComponentUtil.HTML_ATR_SRC, undefinedImgName);
        }
    }

    /**
     * Open file.
     * 
     * @param fileName file name
     * @param pageContext Page Context
     * 
     * @return file
     */
    public static File openFile(VpePageContext pageContext, String fileName) {
        IEditorInput input = pageContext.getEditPart().getEditorInput();
        IPath inputPath = getInputParentPath(input);
        File file = new File(inputPath.toOSString() + File.separator + fileName);
        return file;
    }

    /**
     * Returns locale of user input.
     * 
     * @param input the input
     * 
     * @return the input parent path
     */
    public static IPath getInputParentPath(IEditorInput input) {
        IPath inputPath = null;
        if (input instanceof ILocationProvider) {
            inputPath = ((ILocationProvider) input).getPath(input);
        } else if (input instanceof IFileEditorInput) {
            IFile inputFile = ((IFileEditorInput) input).getFile();
            if (inputFile != null) {
                inputPath = inputFile.getLocation();
            }
        }
        if (inputPath != null && !inputPath.isEmpty()) {
            inputPath = inputPath.removeLastSegments(1);
        }
        return inputPath;
    }

    /**
     * Move attributes from sourceNode to html.
     * 
     * @param visualNode the visual node
     * @param sourceNode the source node
     * @param htmlAttrName the html attr name
     * @param defValue the def value
     * @param prefValue the pref value
     * @param attrName the attr name
     */
    public static void correctAttribute(Element sourceNode, nsIDOMElement visualNode, String attrName, String htmlAttrName,
            String prefValue, String defValue) {
        String attrValue = ((Element) sourceNode).getAttribute(attrName);
        if (prefValue != null && prefValue.trim().length() > 0 && attrValue != null) {
            attrValue = prefValue.trim() + " " + attrValue; //$NON-NLS-1$
        }
        if (attrValue != null) {
            visualNode.setAttribute(htmlAttrName, attrValue);
        } else if (defValue != null) {
            visualNode.setAttribute(htmlAttrName, defValue);
        } else
            visualNode.removeAttribute(attrName);
    }

    /**
     * Parses string value retrieved from sourceElement.getAttribure(..) method
     * to its boolean value.
     * <p>
     * <code>false</code> is returned only if it specified explicitly, otherwise
     * <code>true</code> is returned.
     * 
     * @param str the string to parse
     * 
     * @return boolean value from string
     */
    public static boolean string2boolean(String str) {
        if ((str == null) || ("".equals(str))) { //$NON-NLS-1$
            return true;
        } else if (("true".equalsIgnoreCase(str)) //$NON-NLS-1$
                || ("false".equalsIgnoreCase(str))) { //$NON-NLS-1$
            return new Boolean(str).booleanValue();
        }
        return true;
    }

    /**
     * find elements by name.
     * 
     * @param node - current node
     * @param name - name element
     * @param elements - list of found elements
     */
    static public void findElementsByName(Node node, List<Node> elements, String name) {
        // get children
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            // if current child is required then add his to list
            if (name.equalsIgnoreCase((child.getNodeName()))) {
                elements.add(child);
            } else {
                findElementsByName(child, elements, name);
            }
        }
    }

    /**
     * find all elements by name.
     * 
     * @param node - current node
     * @param name - name element
     * @param elements - list of found elements
     */
    static public void findAllElementsByName(nsIDOMNode node, List<nsIDOMNode> elements, String name) {

        try {
            nsIDOMNodeList list = node.getChildNodes();
            if (node.getNodeName().equalsIgnoreCase(name)) {
                elements.add(node);
            }
            for (int i = 0; i < list.getLength(); i++) {
                findAllElementsByName(list.item(i), elements, name);
            }
        } catch (XPCOMException e) {
            // Ignore
            return;
        }
    }

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
     * Parses the width height value.
     * 
     * @param value the value
     * 
     * @return the integer
     */
    public static Integer parseWidthHeightValue(String value) {
        Integer rst = null;

        if ((value == null) || (value.trim().length() == 0)) {
            throw new NumberFormatException("Passed value is empty ");
        }

        if (value.endsWith(PX_SUFFIX)) {
            rst = Integer.parseInt(value.substring(0, value.length() - 2));
        } else {
            rst = Integer.parseInt(value);
        }

        return rst;
    }

    /**
     * Checks if is blank.
     * 
     * @param value the value
     * 
     * @return true, if is blank
     */
    public static boolean isBlank(String value) {
        return ((value == null) || (value.trim().length() == 0));
    }

    /**
     * Checks if is not blank.
     * 
     * @param value the value
     * 
     * @return true, if is not blank
     */
    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    /**
     * Gets the select items.
     * 
     * @param children the children
     * 
     * @return the select items
     */
    public static List<Element> getSelectItems(NodeList children) {
        final List<Element> selectItems = new ArrayList<Element>();

        for (int i = 0; i < children.getLength(); i++) {
            final Node child = children.item(i);

            if ((child instanceof Element) && (child.getNodeName().indexOf(SELECT_ITEM)) > 1) {
                selectItems.add((Element) child);
            }
        }
        return selectItems;
    }
    
    /**
     * Gets the select item value.
     * 
     * @param e the e
     * 
     * @return the select item value
     */
    public static String getSelectItemValue(Element e) {
        String value = e.getAttribute(RichFaces.ATTR_SELECT_ITEM_LABEL);

        if (isBlank(value)) {
            value = e.getAttribute(RichFaces.ATTR_SELECT_ITEM_VALUE);
            if (ComponentUtil.isBlank(value)) {
                if (e.getNodeName().endsWith(SELECT_ITEMS)) {
                    value = e.getAttribute(RichFaces.ATTR_VALUE);

                    if (ComponentUtil.isBlank(value)) {
                        value = EMPTY_SELECT_ITEMS_VALUE;
                    }
                } else if (e.getNodeName().endsWith(SELECT_ITEM)) {
                    value = EMPTY_SELECT_ITEM_VALUE;
                }
            }
        }
        return value;
    }

}