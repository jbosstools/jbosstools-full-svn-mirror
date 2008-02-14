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
package org.jboss.tools.vpe.editor.template;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jst.jsp.core.internal.contentmodel.TaglibController;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TLDCMDocumentManager;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TaglibTracker;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.provisional.TLDDocument;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.util.XmlUtil;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VpeTemplateManager {
	
	static final String TEMPLATES_FILE_LIST_NAME = "vpe-templates-list.xml";
	static final String AUTO_TEMPLATES_FILE_NAME = "templates/vpe-templates-auto.xml";
	static final String TEMPLATES_FOLDER = File.separator + "templates" + File.separator;
	public static final String VPE_PREFIX = "vpe:";
	//gavs
	static final String TAG_List = VPE_PREFIX + "list";
	static final String ATTR_LIST_ORDERED = "ordered";
	static final String[] ATTR_LIST_PROPERTIES = {"ordered", "style","class","title","lang","dir"};
	//gavs

	//bela_n
	static final String TAG_LABELED_FORM = VPE_PREFIX + "labeledForm";
	static final String ATTR_LABELED_FORM_LABEL = "labelName";
	static final String ATTR_LABELED_FORM_DEFAULT_LABEL = "label";
	static final String[] ATTR_LABELED_FORM_PROPERTIES = {"style","class","width","border","frame","rules","cellspacing","cellpadding","bgcolor","title"};
	//bela_n
	
	static final String TAG_TEMPLATES_LIST = VPE_PREFIX + "templates-list";
	static final String TAG_TEMPLATES = VPE_PREFIX + "templates";
	static final String TAG_TEMPLATE_TAGLIB = VPE_PREFIX + "template-taglib";
	static final String TAG_TAG = VPE_PREFIX + "tag";
	static final String TAG_IF = VPE_PREFIX + "if";
	static final String TAG_TEMPLATE = VPE_PREFIX + "template";
	static final String TAG_COPY = VPE_PREFIX + "copy";
	static final String TAG_GRID = VPE_PREFIX + "grid";
	static final String TAG_PANELGRID = VPE_PREFIX + "panelgrid";
	static final String TAG_ELEMENT = VPE_PREFIX + "element";
	static final String TAG_ATTRIBUTE = VPE_PREFIX + "attribute";
	static final String TAG_VALUE = VPE_PREFIX + "value";
	static final String TAG_XMLNS = VPE_PREFIX + "xmlns";
	static final String TAG_ANY = VPE_PREFIX + "any";
	static final String TAG_TAGLIB = VPE_PREFIX + "taglib";
	static final String TAG_LINK = VPE_PREFIX + "link";
	static final String TAG_LOAD_BUNDLE = VPE_PREFIX + "load-bundle";
	static final String TAG_DATATABLE = VPE_PREFIX + "datatable";
	static final String TAG_DATATABLE_COLUMN = VPE_PREFIX + "column";
	static final String TAG_COMMENT = VPE_PREFIX + "comment";
	static final String TAG_STYLE = VPE_PREFIX + "style";
	static final String TAG_JSPROOT = VPE_PREFIX + "jsproot";
	static final String TAG_RESIZE = VPE_PREFIX + "resize";
	static final String TAG_DND = VPE_PREFIX + "dnd";
	static final String TAG_FACET = VPE_PREFIX + "facet";
	static final String TAG_MY_FACES_PAGE_LAYOUT = VPE_PREFIX + "panellayout";	
	
	public static final String TAG_TEXT_FORMATING = VPE_PREFIX + "textFormating";
	public static final String TAG_FORMAT = VPE_PREFIX + "format";
	public static final String TAG_FORMAT_ATTRIBUTE = VPE_PREFIX + "formatAttribute";

	public static final String ATTR_FORMAT_TYPE = "type";
	public static final String ATTR_FORMAT_ADD_CHILDREN = "addChildren";
	public static final String ATTR_FORMAT_ADD_PARENT = "addParent";
	public static final String ATTR_FORMAT_ADD_CHILDREN_HANDLER = "addChildrenHandler";
	public static final String ATTR_FORMAT_HANDLER = "handler";
	public static final String ATTR_FORMAT_SET_DEFAULT = "setDefault";
	public static final String ATTR_FORMAT_ATTRIBUTE_TYPE = "type";
	public static final String ATTR_FORMAT_ATTRIBUTE_NAME = "name";
	public static final String ATTR_FORMAT_ATTRIBUTE_VALUE = "value";
	public static final String ATTR_FORMAT_ATTRIBUTE_CASE_SENSITIVE = "caseSensitive";
	public static final String ATTR_FORMAT_ATTRIBUTE_TRUE_VALUE = "true";
	public static final String ATTR_FORMAT_ADD_CHILDREN_ALLOW_VALUE = "allow";
	public static final String ATTR_FORMAT_ADD_CHILDREN_DENY_VALUE = "deny";
	public static final String ATTR_FORMAT_ADD_CHILDREN_ITSELF_VALUE = "itself";
	public static final String ATTR_FORMAT_ATTRIBUTE_TYPE_STYLE_VALUE = "style";

	static final String ATTR_DIRECTIVE_TAGLIB_URI = "uri";
	static final String ATTR_DIRECTIVE_TAGLIB_PREFIX = "prefix";

	static final String ATTR_TAG_NAME = "name";
	static final String ATTR_TAG_CASE_SENSITIVE = "case-sensitive";

	static final String ATTR_IF_TEST = "test";

	static final String ATTR_TEMPLATE_CLASS = "class";
	static final String ATTR_TEMPLATE_CHILDREN = "children";
	static final String ATTR_TEMPLATE_MODIFY = "modify";
	
	/** ATTR_TEMPLATE_HAVE_VISUAL_PREVIEW */
	static final String ATTR_TEMPLATE_HAVE_VISUAL_PREVIEW = "haveVisualPreview";

	static final String ATTR_COPY_ATTRS = "attrs";

	static final String ATTR_ELEMENT_NAME = "name";

	static final String ATTR_ATTRIBUTE_NAME = "name";
	static final String ATTR_ATTRIBUTE_VALUE = "value";

	static final String ATTR_VALUE_EXPR = "expr";

	static final String ATTR_PANELGRID_TABLE_SIZE = "table-size";
	static final String ATTR_PANELGRID_HEADER_CLASS = "headerClass";
	static final String ATTR_PANELGRID_FOOTER_CLASS = "footerClass";
	static final String ATTR_PANELGRID_ROW_CLASSES = "rowClasses";
	static final String ATTR_PANELGRID_COLUMN_CLASSES = "columnClasses";
	static final String[] ATTR_PANELGRID_PROPERTIES = {"style","class","width","border","frame","rules","cellspacing","cellpadding","bgcolor","title"};

	static final String ATTR_GRID_LAYOUT = "layout";
	static final String ATTR_GRID_TABLE_SIZE = "table-size";
	static final String[] ATTR_GRID_PROPERTIES = {"style","class","width","border","frame","rules","cellspacing","cellpadding","bgcolor","title"};

	static final String ATTR_ANY_DISPLAY = "display";
	static final String ATTR_ANY_ICON = "icon";
	static final String ATTR_ANY_VALUE = "value";
	static final String ATTR_ANY_BORDER = "border";
	static final String ATTR_ANY_VALUE_COLOR = "value-color";
	static final String ATTR_ANY_VALUE_BACKGROUND_COLOR = "value-background-color";
	static final String ATTR_ANY_BACKGROUND_COLOR = "background-color";
	static final String ATTR_ANY_BORDER_COLOR = "border-color";
	static final String[] ATTR_ANY_PROPERTIES = {"title"};

	static final String ATTR_DATATABLE_HEADER_CLASS = "headerClass";
	static final String ATTR_DATATABLE_FOOTER_CLASS = "footerClass";
	static final String ATTR_DATATABLE_ROW_CLASSES = "rowClasses";
	static final String ATTR_DATATABLE_COLUMN_CLASSES = "columnClasses";
	static final String[] ATTR_DATATABLE_PROPERTIES = {"width", "bgcolor","border","cellpadding","cellspacing","frame","rules","class","style","title"};

	public static final String ATTR_LINK_HREF = "href";
	public static final String ATTR_LINK_REL = "rel";
	public static final String ATTR_LINK_EXT = "ext";

	private static VpeTemplateManager instance = null;
	private static Object monitor = new Object();

	/**
	 * Contains Mapping from URI and namespace
	 */
	private Map<String,String> templateTaglibs = new HashMap<String,String>();
	
	private Map<String,VpeTemplateSet> caseSensitiveTags = new HashMap<String,VpeTemplateSet>();
	private Map<String,VpeTemplateSet> ignoreSensitiveTags = new HashMap<String,VpeTemplateSet>();
	private VpeTemplate defTemplate;
	private VpeTemplateListener[] templateListeners = new VpeTemplateListener[0];
	private VpeTemplateFileList templateFileList = new VpeTemplateFileList();
	private Set<String> withoutWhitespaceContainerSet = new HashSet<String>();
	private Set<String> withoutPseudoElementContainerSet = new HashSet<String>();
	
	/**
	 * Added by Max Areshkau(mareshkau@exadel.com)
	 *  This property identify namespace which should be used to load some specific class.
	 *  For example in rich:dataTable can be h:column, but rich:dataTable is separate plugin,
	 *  so to render h:column we should load the specific class for h:column from richfaces template
	 */
	private static final String NAMESPACE_IDENTIFIER_ATTRIBUTE="namespaceIdentifier";
	

	private VpeTemplateManager() {
	}

	public static final VpeTemplateManager getInstance() {
		if (instance != null) {
			return instance;
		} else {
			synchronized (monitor) {
				if (instance == null) {
					VpeTemplateManager inst = new VpeTemplateManager();
					inst.load();
					instance = inst;
				}
			}
			return instance;
		}
	}
	
	public VpeTemplate getTemplate(VpePageContext pageContext, Node sourceNode, Set dependencySet) {
		VpeTemplate template = getTemplateImpl(pageContext, sourceNode, dependencySet);
		if (template != null) {
			return template;
		} else {
			return defTemplate;
		}
	}

	private VpeTemplate getTemplateImpl(VpePageContext pageContext, Node sourceNode, Set dependencySet) {
		String name = getTemplateName(pageContext, sourceNode);
		if (name == null) {
			return null;
		}
		VpeTemplateSet set = caseSensitiveTags.get(name);
		if (set != null) {
			return set.getTemplate(pageContext, sourceNode, dependencySet);
		}
		set = ignoreSensitiveTags.get(name.toLowerCase());
		if (set != null) {
			return set.getTemplate(pageContext, sourceNode, dependencySet);
		}
		return null;
	}

	private String getTemplateName(VpePageContext pageContext, Node sourceNode) {
		
		String sourcePrefix = sourceNode.getPrefix();
		
		if (sourcePrefix == null || ((ElementImpl)sourceNode).isJSPTag() || "jsp".equals(sourcePrefix)) {
			
			return sourceNode.getNodeName();
		}
		
		List<TaglibData> taglibs = XmlUtil.getTaglibsForNode(sourceNode,pageContext.getSourceBuilder().getStructuredTextViewer().getDocument());
		
		TaglibData sourceNodeTaglib = XmlUtil.getTaglibForPrefix(sourcePrefix, taglibs);		

		if(sourceNodeTaglib == null) {
			
			return null;
		}
		
		String sourceNodeUri = sourceNodeTaglib.getUri();

		
		String templateTaglibPrefix = getTemplateTaglibPrefix(sourceNodeUri);

		if(templateTaglibPrefix != null) {
			
			return templateTaglibPrefix+":"+sourceNode.getLocalName();
		}

		return null;
	}
	
	public String getTemplateTaglibPrefix(String sourceUri) {
		return (String)templateTaglibs.get(sourceUri);
	}

	private void load() {
		initWithoutWhitespaceContainerSet();
		initPseudoElementContainerSet();
		templateFileList.load();
		loadImpl();
	}

	private void loadImpl() {
		VpeTemplateFile autoTemplateFile = templateFileList.getAutoTemplateFile();
		if (autoTemplateFile != null) {
			loadTemplates(autoTemplateFile.getPath(),autoTemplateFile.getConfigurableElement());
		}
		VpeTemplateFile[] templateFiles = templateFileList.getTemplateFiles();
		for (int i = 0; i < templateFiles.length; i++) {
			loadTemplates(templateFiles[i].getPath(),templateFiles[i].getConfigurableElement());
		}
		if (defTemplate == null) {
			defTemplate = createDefTemplate();
		}
	}

	private void loadTemplates(IPath vpeFilePath, IConfigurationElement confElement) {
		try {
			Element root = XMLUtilities.getElement(vpeFilePath.toFile(), null);
			loadTemplates(root,confElement);
		} catch (Exception e) {
			VpePlugin.reportProblem(e);
		}
	}
	
	private void loadTemplates(Element root, IConfigurationElement confElement) throws Exception {
		if (root == null || !TAG_TEMPLATES.equals(root.getNodeName())) {
			return;
		}
		NodeList children = root.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				Node node = children.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					if (TAG_TAG.equals(node.getNodeName())) {
						setTagElement((Element)node, confElement);
					} else if (TAG_TEMPLATE.equals(node.getNodeName())) {
						setDefTemplate(createTemplate((Element)node,confElement, true));
					}  
					else if (TAG_TEMPLATE_TAGLIB.equals(node.getNodeName())) {
						setTemplateTaglib((Element)node);
					}
				}
			}
		}
	}
	
	private void setTagElement(Element tagElement,IConfigurationElement confElement) {
		String name = tagElement.getAttribute(ATTR_TAG_NAME);
		if (name.length() > 0) {
			boolean caseSensitive = !"no".equals(tagElement.getAttribute(ATTR_TAG_CASE_SENSITIVE));
			Map<String,VpeTemplateSet> tags;
			if (caseSensitive) {
				tags = caseSensitiveTags;
			} else {
				name = name.toLowerCase();
				tags = ignoreSensitiveTags;
			}
			VpeTemplateSet set = (VpeTemplateSet) tags.get(name);
			if (set == null) {
				set = new VpeTemplateSet();
				tags.put(name, set);
			}
			addChildren(tagElement, set, confElement, caseSensitive);
		}
	}
	
	private void addChildren(Element element, VpeTemplateSet set, IConfigurationElement confElement,boolean caseSensitive) {
		NodeList children = element.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				Node node = children.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					if (TAG_IF.equals(node.getNodeName())) {
						addIfElement((Element)node, set, confElement,caseSensitive);
					} else if (TAG_TEMPLATE.equals(node.getNodeName())) {
						set.setDefTemplate(createTemplate((Element)node, confElement, caseSensitive));
					}
				}
			}
		}
	}
	
	private void addIfElement(Element ifElement, VpeTemplateSet parentSet, IConfigurationElement confElement,boolean caseSensitive) {
		String test = ifElement.getAttribute(ATTR_IF_TEST);
		VpeTemplateConditionSet set = new VpeTemplateConditionSet(test, caseSensitive);
		parentSet.addChild(set);
		addChildren(ifElement, set, confElement,caseSensitive);
	}
	
	private void setDefTemplate(VpeTemplate defTemplate) {
		if (this.defTemplate == null) {
			this.defTemplate = defTemplate;
		}
	}
	
	/**
	 * Register templates taglibs from templates files
	 * @param templateTaglibElement
	 */
	private void setTemplateTaglib(Element templateTaglibElement) {
		String uri = templateTaglibElement.getAttribute(ATTR_DIRECTIVE_TAGLIB_URI);
		String pefix = templateTaglibElement.getAttribute(ATTR_DIRECTIVE_TAGLIB_PREFIX);
		if (uri.length() > 0 && pefix.length() > 0) {
			if (!templateTaglibs.containsKey(uri)) {
				templateTaglibs.put(uri, pefix);
			}
		}
	}
	
	public void setAnyTemplate(VpeAnyData data) {
		String elementName = data.getName();
		boolean caseSensitive = data.isCaseSensitive();
		Element root = loadAutoTemplate();
		if (root == null) {
			root = XMLUtilities.createDocumentElement(TAG_TEMPLATES);
		}
		Set<String> prefixSet = new HashSet<String>();
		Node tagElement = null;
		NodeList children = root.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = len - 1; i >= 0; i--) {
				Node node = children.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					if (TAG_TAG.equals(node.getNodeName())) {
						if (caseSensitive == !"no".equals(((Element)node).getAttribute(ATTR_TAG_CASE_SENSITIVE))) {
							String name = ((Element)node).getAttribute(ATTR_TAG_NAME);
							if (caseSensitive && name.equals(elementName) || !caseSensitive && name.equalsIgnoreCase(elementName)) {
								tagElement = node;
							}
						}
					} else if (TAG_TEMPLATE_TAGLIB.equals(node.getNodeName())) {
						Node prefixAttr = node.getAttributes().getNamedItem(ATTR_DIRECTIVE_TAGLIB_PREFIX);
						String prefix = prefixAttr != null ? prefixAttr.getNodeValue() : "";
						if (prefix.length() > 0) {
							prefixSet.add(prefix);
;
						}
					} else {
						root.removeChild(node);
					}
				}
			}
		}

		Document document = root.getOwnerDocument();
		Element newTagElement = createNewTagElement(document, data);

		if (tagElement != null) {
			root.replaceChild(newTagElement, tagElement);
		} else {
			root.appendChild(newTagElement);
		}

		root = appendTaglib(prefixSet, document, root, data);

		try {
			IPath path = VpeTemplateFileList.getFilePath(AUTO_TEMPLATES_FILE_NAME,null);
			XMLUtilities.serialize(root, path.toOSString());
		} catch(Exception e) {
			VpePlugin.reportProblem(e);
		}
		
		reload();
	}
	
	private Element appendTaglib(Set<?> prefixSet, Document document, Element root, VpeAnyData data) {
		if (data.getPrefix() != null && data.getUri() != null &&
				data.getPrefix().length() > 0 && data.getUri().length() > 0 &&
				!prefixSet.contains(data.getPrefix())) {
			Element node = createNewTaglibElement(document, data);
			Node firstNode = null;
			if (root.hasChildNodes()) {
				NodeList childs = root.getChildNodes();
				for (int i = 0; i < childs.getLength(); i++) {
					Node item = childs.item(i);
					if (item.getNodeType() == Node.ELEMENT_NODE) {
						firstNode = item;
						break;
					}
				}
			}

			if (firstNode != null) {
				root.insertBefore(node, firstNode);
			} else {
				root.appendChild(node);
			}
		}
		return root;
	}

	public List getAnyTemplates() {
		List<VpeAnyData> anyTemplateList = new ArrayList<VpeAnyData>();
		Map<String,Node> taglibs = new HashMap<String,Node>();

		Element root = loadAutoTemplate();
		if (root == null) {
			root = XMLUtilities.createDocumentElement(TAG_TEMPLATES);
		}
//		Node tagElement = null;
		NodeList children = root.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = len - 1; i >= 0; i--) {
				Node node = children.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					if (TAG_TAG.equals(node.getNodeName())) {
						Node attr = ((Element)node).getAttributeNode(ATTR_TAG_NAME);
						VpeAnyData anyData = new VpeAnyData(attr != null ? attr.getNodeValue() : "");
						attr = ((Element)node).getAttributeNode(ATTR_TAG_CASE_SENSITIVE);
						anyData.setCaseSensitive("yes".equalsIgnoreCase(attr.getNodeValue()));
						Element templateNode = getChildNode(node, TAG_TEMPLATE);
						if (templateNode != null) {
							attr = ((Element)templateNode).getAttributeNode(ATTR_TEMPLATE_CHILDREN);
							if (attr != null) anyData.setChildren("yes".equalsIgnoreCase(attr.getNodeValue()));
							attr = ((Element)templateNode).getAttributeNode(ATTR_TEMPLATE_MODIFY);
							if (attr != null) anyData.setModify("yes".equalsIgnoreCase(attr.getNodeValue()));

							Element anyNode = getChildNode(templateNode, TAG_ANY);
							if (anyNode != null) {
								attr = ((Element)anyNode).getAttributeNode(ATTR_TEMPLATE_CHILDREN);
								if (attr != null) anyData.setChildren("yes".equalsIgnoreCase(attr.getNodeValue()));
								
								attr = ((Element)anyNode).getAttributeNode(ATTR_ANY_DISPLAY);
								if (attr != null) anyData.setDisplay(attr.getNodeValue());

								attr = ((Element)anyNode).getAttributeNode(ATTR_ANY_ICON);
								if (attr != null) anyData.setShowIcon("yes".equalsIgnoreCase(attr.getNodeValue()));

								attr = ((Element)anyNode).getAttributeNode(ATTR_ANY_VALUE);
								if (attr != null) anyData.setValue(attr.getNodeValue());

								attr = ((Element)anyNode).getAttributeNode(ATTR_ANY_BORDER);
								if (attr != null) anyData.setBorder(attr.getNodeValue());

								attr = ((Element)anyNode).getAttributeNode(ATTR_ANY_VALUE_COLOR);
								if (attr != null) anyData.setValueColor(attr.getNodeValue());

								attr = ((Element)anyNode).getAttributeNode(ATTR_ANY_VALUE_BACKGROUND_COLOR);
								if (attr != null) anyData.setValueBackgroundColor(attr.getNodeValue());

								attr = ((Element)anyNode).getAttributeNode(ATTR_ANY_BACKGROUND_COLOR);
								if (attr != null) anyData.setBackgroundColor(attr.getNodeValue());

								attr = ((Element)anyNode).getAttributeNode(ATTR_ANY_BORDER_COLOR);
								if (attr != null) anyData.setBorderColor(attr.getNodeValue());
							}
						}
						anyTemplateList.add(anyData);
					} else if (TAG_TEMPLATE_TAGLIB.equals(node.getNodeName())) {
						Node prefixAttr = node.getAttributes().getNamedItem(ATTR_DIRECTIVE_TAGLIB_PREFIX);
						String prefix = prefixAttr != null ? prefixAttr.getNodeValue() : "";
						if (prefix.length() > 0) {
							taglibs.put(prefix, node);
;
						}
					}
				}
			}
		}

		for (Iterator iter = anyTemplateList.iterator(); iter.hasNext();) {
			VpeAnyData element = (VpeAnyData) iter.next();
			String prefix = element.getPrefix();
			if (taglibs.containsKey(prefix)) {
				Node node = (Node)taglibs.get(prefix);
				Node uriAttr = node.getAttributes().getNamedItem(ATTR_DIRECTIVE_TAGLIB_URI);
				String uri = uriAttr != null ? uriAttr.getNodeValue() : "";
				element.setUri(uri);
			}
		}

		return anyTemplateList;
	}
	
	private Element getChildNode(Node node, String childName) {
		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				Node item = children.item(i);
				if (item.getNodeType() == Node.ELEMENT_NODE) {
					if (childName.equals(item.getNodeName())) {
						return (Element)item;
					}
				}
			}
		}
		return null;
	}
	

	public void setAnyTemplates(List templates) {
		if (templates != null) {
			Set<String> prefixSet = new HashSet<String>();
			Element root = XMLUtilities.createDocumentElement(TAG_TEMPLATES);
			Document document = root.getOwnerDocument();

			for (Iterator iter = templates.iterator(); iter.hasNext();) {
				VpeAnyData data = (VpeAnyData) iter.next();
				root.appendChild(createNewTagElement(document, data));
				String prefix = data.getPrefix();
				if (prefix != null && prefix.length() > 0 && !prefixSet.contains(prefix)) {
					root = appendTaglib(prefixSet, document, root, data);
					prefixSet.add(prefix);
				}
			}

			try {
				IPath path = VpeTemplateFileList.getFilePath(AUTO_TEMPLATES_FILE_NAME, null);
				// fixed bug [EFWPE-869] - uncomment this line
				XMLUtilities.serialize(root, path.toOSString());
			} catch(Exception e) {
				VpePlugin.reportProblem(e);
			}
		}
	}

	private Element createNewTagElement(Document document, VpeAnyData data) {
		Element newTagElement = document.createElement(TAG_TAG);
		newTagElement.setAttribute(ATTR_TAG_NAME, data.getName());
		newTagElement.setAttribute(ATTR_TAG_CASE_SENSITIVE, data.isCaseSensitive() ? "yes" : "no");

		Element newTemplateElement = document.createElement(TAG_TEMPLATE);
		newTemplateElement.setAttribute(ATTR_TEMPLATE_CHILDREN, data.isChildren() ? "yes" : "no");
		newTemplateElement.setAttribute(ATTR_TEMPLATE_MODIFY, data.isModify() ? "yes" : "no");
		newTagElement.appendChild(newTemplateElement);

		Element newAnyElement = document.createElement(TAG_ANY);
		if (data.getDisplay() != null && data.getDisplay().length() > 0) 
			newAnyElement.setAttribute(ATTR_ANY_DISPLAY, data.getDisplay());
		if (data.getValue() != null && data.getValue().length() > 0) 
			newAnyElement.setAttribute(ATTR_ANY_VALUE, data.getValue());
		if (data.getBorder() != null && data.getBorder().length() > 0) 
			newAnyElement.setAttribute(ATTR_ANY_BORDER, data.getBorder());
		if (data.getValueColor() != null && data.getValueColor().length() > 0) 
			newAnyElement.setAttribute(ATTR_ANY_VALUE_COLOR, data.getValueColor());
		if (data.getValueBackgroundColor() != null && data.getValueBackgroundColor().length() > 0) 
			newAnyElement.setAttribute(ATTR_ANY_VALUE_BACKGROUND_COLOR, data.getValueBackgroundColor());
		if (data.getBackgroundColor() != null && data.getBackgroundColor().length() > 0)
			newAnyElement.setAttribute(ATTR_ANY_BACKGROUND_COLOR, data.getBackgroundColor());
		if (data.getBorderColor() != null && data.getBorderColor().length() > 0)
			newAnyElement.setAttribute(ATTR_ANY_BORDER_COLOR, data.getBorderColor());
		
		newAnyElement.setAttribute(ATTR_ANY_ICON, data.isShowIcon() ? "yes" : "no");

		newTemplateElement.appendChild(newAnyElement);

		return newTagElement;
	}

	private Element createNewTaglibElement(Document document, VpeAnyData data) {
		Element newTaglibElement = document.createElement(TAG_TEMPLATE_TAGLIB);
		newTaglibElement.setAttribute(ATTR_DIRECTIVE_TAGLIB_PREFIX, data.getPrefix());
		newTaglibElement.setAttribute(ATTR_DIRECTIVE_TAGLIB_URI, data.getUri());
		return newTaglibElement;
	}

	private Element loadAutoTemplate() {
		try {
			IPath path = VpeTemplateFileList.getFilePath(AUTO_TEMPLATES_FILE_NAME, null);
			Element root = XMLUtilities.getElement(path.toFile(), null);
			if (root != null && TAG_TEMPLATES.equals(root.getNodeName())) {
				return root;
			}
		} catch (Exception e) {
			VpePlugin.reportProblem(e);
		}
		return null;
	}

	public void reload() {
		synchronized (monitor) {
			templateFileList.load();
			if (templateFileList.isChanged()) {
				caseSensitiveTags.clear();
				ignoreSensitiveTags.clear();
				defTemplate = null;
				loadImpl();
				fireTemplateReloaded();
			}
		}
	}

	public void addTemplateListener(VpeTemplateListener listener) {
		if (listener != null) {
			VpeTemplateListener[] newTemplateListeners = new VpeTemplateListener[templateListeners.length + 1];
			System.arraycopy(templateListeners, 0, newTemplateListeners, 0, templateListeners.length);
			templateListeners = newTemplateListeners;
			templateListeners[templateListeners.length - 1] = listener;
		}
	}
	
	public void removeTemplateListener(VpeTemplateListener listener) {
		if (listener == null || templateListeners.length == 0) return;
		int index = -1;
		for (int i = 0; i < templateListeners.length; i++) {
			if (listener == templateListeners[i]){
				index = i;
				break;
			}
		}
		if (index == -1) return;
		if (templateListeners.length == 1) {
			templateListeners = new VpeTemplateListener[0];
			return;
		}
		VpeTemplateListener[] newTemplateListeners = new VpeTemplateListener[templateListeners.length - 1];
		System.arraycopy(templateListeners, 0, newTemplateListeners, 0, index);
		System.arraycopy(templateListeners, index + 1, newTemplateListeners, index, templateListeners.length - index - 1);
		templateListeners = newTemplateListeners;
	}
	
	private void fireTemplateReloaded() {
		for (int i = 0; i < templateListeners.length; i++) {
			templateListeners[i].templateReloaded();
		}
	}
	
	private Element createDefTemplateElement() {
		Element newTemplateElement = XMLUtilities.createDocumentElement(TAG_TEMPLATE);
		newTemplateElement.setAttribute(ATTR_TEMPLATE_CHILDREN, "yes");
		newTemplateElement.setAttribute(ATTR_TEMPLATE_MODIFY, "no");
		Document document = newTemplateElement.getOwnerDocument();
		Element newAnyElement = document.createElement(TAG_ANY);
		newAnyElement.setAttribute(ATTR_ANY_VALUE, "{name()}");
		newAnyElement.setAttribute("title", "{tagstring()}");
		newTemplateElement.appendChild(newAnyElement);
		return newTemplateElement;
	}
	
	static String[] WITHOUT_WHITESPACE_ELEMENT_NAMES = {
		"table", "caption", "col", "colgroup", "thead", "tbody", "tfoot", "th", "tr", "td"
	};
	private void initWithoutWhitespaceContainerSet() {
		for (int i = 0; i < WITHOUT_WHITESPACE_ELEMENT_NAMES.length; i++) {
			withoutWhitespaceContainerSet.add(WITHOUT_WHITESPACE_ELEMENT_NAMES[i]);
		}
	}
	
	private void initPseudoElementContainerSet() {
		withoutPseudoElementContainerSet.add("br");
		withoutPseudoElementContainerSet.add("input");
	}
	
	public boolean isWithoutWhitespaceContainer(String name) {
		return withoutWhitespaceContainerSet.contains(name.toLowerCase());
	}
	
	public boolean isWithoutPseudoElementContainer(String name) {
		return withoutPseudoElementContainerSet.contains(name.toLowerCase());
	}
	
	private VpeTemplate createTemplate(Element templateElement,IConfigurationElement confElement, boolean caseSensitive) {
		VpeTemplate template = null;
		String templateClassName = templateElement.getAttribute(VpeTemplateManager.ATTR_TEMPLATE_CLASS);
		String nameSpaceIdentifyer = templateElement.getAttribute(VpeTemplateManager.NAMESPACE_IDENTIFIER_ATTRIBUTE);
		if (templateClassName != null && templateClassName.length() > 0) {
			try {
				Bundle bundle;
				if(nameSpaceIdentifyer==null||nameSpaceIdentifyer.length()==0) {
				
					bundle = Platform.getBundle(confElement.getNamespaceIdentifier());
				} else {
					
					bundle = Platform.getBundle(nameSpaceIdentifyer);
				}
				Class templateClass = bundle.loadClass(templateClassName);
				template = (VpeTemplate)templateClass.newInstance();
			} catch (Exception e) {
				try {
					Class templateClass = Class.forName(templateClassName);
					template = (VpeTemplate)templateClass.newInstance();
				} catch (Exception e2) { 
					String message = e.getMessage();
					if(message==null) {
						message = "Can't get VPE template class: " + templateClassName;
					}
					VpePlugin.getPluginLog().logWarning(message, e);
					return null;
				}
			}
		} else {
			template = new VpeHtmlTemplate();
		}
		template.init(templateElement, caseSensitive);
		return template;
	}
	
	private VpeTemplate createDefTemplate() {
		VpeTemplate defTemplate = new VpeHtmlTemplate();
		defTemplate.init(createDefTemplateElement(), true);
		return defTemplate;
	}

	/**
	 * @return the defTemplate
	 */
	public VpeTemplate getDefTemplate() {
		if(defTemplate==null) {
			defTemplate=createDefTemplate();
		}
		return defTemplate;
	}
}
