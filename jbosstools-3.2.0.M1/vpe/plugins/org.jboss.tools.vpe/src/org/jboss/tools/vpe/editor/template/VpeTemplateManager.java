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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.custom.CustomTLDReference;
import org.jboss.tools.vpe.editor.template.textformating.TextFormatingData;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.SourceDomUtil;
import org.jboss.tools.vpe.editor.util.XmlUtil;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VpeTemplateManager {
	
	private static final String EMPTY_VPE_TEMPLATES_AUTO 
			= "templates/empty-vpe-templates-auto.xml"; //$NON-NLS-1$
	private static final String VPE_TEMPLATES_AUTO 
			= "templates/vpe-templates-auto.xml"; //$NON-NLS-1$
	static final String TEMPLATES_FOLDER = File.separator + "templates" + File.separator; //$NON-NLS-1$
	public static final String VPE_PREFIX = "vpe:"; //$NON-NLS-1$
	public static final String TAG_TEMPLATES = VPE_PREFIX + "templates"; //$NON-NLS-1$
	
	static final String TAG_LIST = VPE_PREFIX + "list"; //$NON-NLS-1$
	static final String ATTR_LIST_ORDERED = "ordered"; //$NON-NLS-1$
	static final String[] ATTR_LIST_PROPERTIES = {
		"ordered", //$NON-NLS-1$
		"style", //$NON-NLS-1$
		"class", //$NON-NLS-1$
		"title", //$NON-NLS-1$
		"lang", //$NON-NLS-1$
		"dir" //$NON-NLS-1$
	};

	static final String TAG_LABELED_FORM = VPE_PREFIX + "labeledForm"; //$NON-NLS-1$
	static final String ATTR_LABELED_FORM_LABEL = "labelName"; //$NON-NLS-1$
	static final String ATTR_LABELED_FORM_DEFAULT_LABEL = "label"; //$NON-NLS-1$
	static final String[] ATTR_LABELED_FORM_PROPERTIES = {
		"style", //$NON-NLS-1$
		"class", //$NON-NLS-1$
		"width", //$NON-NLS-1$
		"border", //$NON-NLS-1$
		"frame", //$NON-NLS-1$
		"rules", //$NON-NLS-1$
		"cellspacing", //$NON-NLS-1$
		"cellpadding", //$NON-NLS-1$
		"bgcolor", //$NON-NLS-1$
		"title" //$NON-NLS-1$
	};
	
	static final String TAG_TEMPLATES_LIST = VPE_PREFIX + "templates-list"; //$NON-NLS-1$
	static final String TAG_TEMPLATE_TAGLIB = VPE_PREFIX + "template-taglib"; //$NON-NLS-1$
	static final String TAG_TAG = VPE_PREFIX + "tag"; //$NON-NLS-1$
	static final String TAG_IF = VPE_PREFIX + "if"; //$NON-NLS-1$
	static final String TAG_TEMPLATE = VPE_PREFIX + "template"; //$NON-NLS-1$
	static final String TAG_COPY = VPE_PREFIX + "copy"; //$NON-NLS-1$
	static final String TAG_GRID = VPE_PREFIX + "grid"; //$NON-NLS-1$
	static final String TAG_PANELGRID = VPE_PREFIX + "panelgrid"; //$NON-NLS-1$
	static final String TAG_ELEMENT = VPE_PREFIX + "element"; //$NON-NLS-1$
	static final String TAG_ATTRIBUTE = VPE_PREFIX + "attribute"; //$NON-NLS-1$
	static final String TAG_VALUE = VPE_PREFIX + "value"; //$NON-NLS-1$
	static final String TAG_XMLNS = VPE_PREFIX + "xmlns"; //$NON-NLS-1$
	static final String TAG_ANY = VPE_PREFIX + "any"; //$NON-NLS-1$
	static final String TAG_TAGLIB = VPE_PREFIX + "taglib"; //$NON-NLS-1$
	static final String TAG_LINK = VPE_PREFIX + "link"; //$NON-NLS-1$
	static final String TAG_LOAD_BUNDLE = VPE_PREFIX + "load-bundle"; //$NON-NLS-1$
	static final String TAG_A = VPE_PREFIX + "a"; //$NON-NLS-1$
	static final String TAG_DATATABLE = VPE_PREFIX + "datatable"; //$NON-NLS-1$
	static final String TAG_DATATABLE_COLUMN = VPE_PREFIX + "column"; //$NON-NLS-1$
	static final String TAG_COMMENT = VPE_PREFIX + "comment"; //$NON-NLS-1$
	static final String TAG_STYLE = VPE_PREFIX + "style"; //$NON-NLS-1$
	static final String TAG_JSPROOT = VPE_PREFIX + "jsproot"; //$NON-NLS-1$
	static final String TAG_RESIZE = VPE_PREFIX + "resize"; //$NON-NLS-1$
	static final String TAG_DND = VPE_PREFIX + "dnd"; //$NON-NLS-1$
	static final String TAG_FACET = VPE_PREFIX + "facet"; //$NON-NLS-1$
	static final String TAG_MY_FACES_PAGE_LAYOUT = VPE_PREFIX + "panellayout"; //$NON-NLS-1$	
	
	public static final String TAG_TEXT_FORMATING = VPE_PREFIX + "textFormatting"; //$NON-NLS-1$
	public static final String TAG_FORMAT = VPE_PREFIX + "format"; //$NON-NLS-1$
	public static final String TAG_FORMAT_ATTRIBUTE = VPE_PREFIX + "formatAttribute"; //$NON-NLS-1$

	public static final String ATTR_FORMAT_TYPE = "type"; //$NON-NLS-1$
	public static final String ATTR_FORMAT_ADD_CHILDREN = "addChildren"; //$NON-NLS-1$
	public static final String ATTR_FORMAT_ADD_PARENT = "addParent"; //$NON-NLS-1$
	public static final String ATTR_FORMAT_ADD_CHILDREN_HANDLER = "addChildrenHandler"; //$NON-NLS-1$
	public static final String ATTR_FORMAT_HANDLER = "handler"; //$NON-NLS-1$
	public static final String ATTR_FORMAT_SET_DEFAULT = "setDefault"; //$NON-NLS-1$
	public static final String ATTR_FORMAT_ATTRIBUTE_TYPE = "type"; //$NON-NLS-1$
	public static final String ATTR_FORMAT_ATTRIBUTE_NAME = "name"; //$NON-NLS-1$
	public static final String ATTR_FORMAT_ATTRIBUTE_VALUE = "value"; //$NON-NLS-1$
	public static final String ATTR_FORMAT_ATTRIBUTE_CASE_SENSITIVE = "caseSensitive"; //$NON-NLS-1$
	public static final String ATTR_FORMAT_ATTRIBUTE_TRUE_VALUE = "true"; //$NON-NLS-1$
	public static final String ATTR_FORMAT_ADD_CHILDREN_ALLOW_VALUE = "allow"; //$NON-NLS-1$
	public static final String ATTR_FORMAT_ADD_CHILDREN_DENY_VALUE = "deny"; //$NON-NLS-1$
	public static final String ATTR_FORMAT_ADD_CHILDREN_ITSELF_VALUE = "itself"; //$NON-NLS-1$
	public static final String ATTR_FORMAT_ATTRIBUTE_TYPE_STYLE_VALUE = "style"; //$NON-NLS-1$

	static final String ATTR_DIRECTIVE_TAGLIB_URI = "uri"; //$NON-NLS-1$
	static final String ATTR_DIRECTIVE_TAGLIB_PREFIX = "prefix"; //$NON-NLS-1$

	static final String ATTR_TAG_NAME = "name"; //$NON-NLS-1$
	static final String ATTR_TAG_CASE_SENSITIVE = "case-sensitive"; //$NON-NLS-1$
	public static final String ATTR_VALUE_YES = "yes"; //$NON-NLS-1$
	static final String ATTR_VALUE_NO = "no"; //$NON-NLS-1$

	static final String ATTR_IF_TEST = "test"; //$NON-NLS-1$

	static final String ATTR_TEMPLATE_CLASS = "class"; //$NON-NLS-1$
	static final String ATTR_TEMPLATE_CHILDREN = "children"; //$NON-NLS-1$
	static final String ATTR_TEMPLATE_MODIFY = "modify"; //$NON-NLS-1$
	
	static final String ATTR_TEMPLATE_HAS_IMAGINARY_BORDER = "hasImaginaryBorder"; //$NON-NLS-1$
	
	static final String ATTR_TEMPLATE_INVISIBLE = "invisible"; //$NON-NLS-1$

	static final String ATTR_COPY_ATTRS = "attrs"; //$NON-NLS-1$

	static final String ATTR_ELEMENT_NAME = "name"; //$NON-NLS-1$

	static final String ATTR_ATTRIBUTE_NAME = "name"; //$NON-NLS-1$
	static final String ATTR_ATTRIBUTE_VALUE = "value"; //$NON-NLS-1$

	static final String ATTR_VALUE_EXPR = "expr"; //$NON-NLS-1$

	static final String ATTR_PANELGRID_TABLE_SIZE = "table-size"; //$NON-NLS-1$
	static final String ATTR_PANELGRID_HEADER_CLASS = "headerClass"; //$NON-NLS-1$
	static final String ATTR_PANELGRID_FOOTER_CLASS = "footerClass"; //$NON-NLS-1$
	static final String ATTR_PANELGRID_ROW_CLASSES = "rowClasses"; //$NON-NLS-1$
	static final String ATTR_PANELGRID_COLUMN_CLASSES = "columnClasses"; //$NON-NLS-1$
	static final String ATTR_PANELGRID_CAPTION_CLASS = "captionClass"; //$NON-NLS-1$
	static final String ATTR_PANELGRID_CAPTION_STYLE = "captionStyle"; //$NON-NLS-1$
	static final String ATTR_PANELGRID_RULES = "rules"; //$NON-NLS-1$
	static final String[] ATTR_PANELGRID_PROPERTIES = {
		"style", //$NON-NLS-1$
		"class", //$NON-NLS-1$
		"width", //$NON-NLS-1$
		"border", //$NON-NLS-1$
		"frame", //$NON-NLS-1$
		"cellspacing", //$NON-NLS-1$
		"cellpadding", //$NON-NLS-1$
		"bgcolor", //$NON-NLS-1$
		"title" //$NON-NLS-1$
	};

	static final String ATTR_GRID_LAYOUT = "layout"; //$NON-NLS-1$
	static final String ATTR_GRID_TABLE_SIZE = "table-size"; //$NON-NLS-1$
	static final String[] ATTR_GRID_PROPERTIES = {
		"style", //$NON-NLS-1$
		"class", //$NON-NLS-1$
		"width", //$NON-NLS-1$
		"border", //$NON-NLS-1$
		"frame", //$NON-NLS-1$
		"cellspacing", //$NON-NLS-1$
		"cellpadding", //$NON-NLS-1$
		"bgcolor", //$NON-NLS-1$
		"title", //$NON-NLS-1$
		"dir" //$NON-NLS-1$
	};
	@Deprecated 
	//used just for conversion old configuration to new configuration
	//should be deleted from some time
	private static final String ATTR_ANY_DISPLAY = "display"; //$NON-NLS-1$
	
	static final String ATTR_ANY_TAG_FOR_DISPLAY="tag-for-display"; //$NON-NLS-1$
	static final String ATTR_ANY_VALUE = "value"; //$NON-NLS-1$
	static final String  ATTR_ANY_STYLE = "style"; //$NON-NLS-1$
	@Deprecated 
	//used just for conversion old configuration to new configuration
	//should be deleted from some time
	static final String ATTR_ANY_BORDER = "border"; //$NON-NLS-1$
	@Deprecated 
	//used just for conversion old configuration to new configuration
	//should be deleted from some time
	static final String ATTR_ANY_VALUE_COLOR = "value-color"; //$NON-NLS-1$
	@Deprecated 
	//used just for conversion old configuration to new configuration
	//should be deleted from some time
	static final String ATTR_ANY_VALUE_BACKGROUND_COLOR = "value-background-color"; //$NON-NLS-1$
	@Deprecated 
	//used just for conversion old configuration to new configuration
	//should be deleted from some time
	static final String ATTR_ANY_BACKGROUND_COLOR = "background-color"; //$NON-NLS-1$
	@Deprecated 
	//used just for conversion old configuration to new configuration
	//should be deleted from some time
	static final String ATTR_ANY_BORDER_COLOR = "border-color"; //$NON-NLS-1$
	
	static final String[] ATTR_ANY_PROPERTIES = {"title"}; //$NON-NLS-1$

	static final String ATTR_DATATABLE_HEADER_CLASS = "headerClass"; //$NON-NLS-1$
	static final String ATTR_DATATABLE_FOOTER_CLASS = "footerClass"; //$NON-NLS-1$
	static final String ATTR_DATATABLE_ROW_CLASSES = "rowClasses"; //$NON-NLS-1$
	static final String ATTR_DATATABLE_COLUMN_CLASSES = "columnClasses"; //$NON-NLS-1$
	static final String[] ATTR_DATATABLE_PROPERTIES = {
		"width", //$NON-NLS-1$
		"height", //$NON-NLS-1$
		"bgcolor", //$NON-NLS-1$
		"border", //$NON-NLS-1$
		"cellpadding", //$NON-NLS-1$
		"cellspacing", //$NON-NLS-1$
		"frame", //$NON-NLS-1$
		"rules", //$NON-NLS-1$
		"class", //$NON-NLS-1$
		"style", //$NON-NLS-1$
		"title", //$NON-NLS-1$
		"dir", //$NON-NLS-1$
		"rowClasses" //$NON-NLS-1$
	};

	public static final String ATTR_LINK_HREF = "href"; //$NON-NLS-1$
	public static final String ATTR_LINK_REL = "rel"; //$NON-NLS-1$
	public static final String ATTR_LINK_EXT = "ext"; //$NON-NLS-1$

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
	//text template name
	private static final String TEXT_TEMPLATE_NAME="#text"; //$NON-NLS-1$
	//comment template name
	private static final String COMMENT_TEMPLATE_NAME="#comment"; //$NON-NLS-1$
	
	private static final String ATTRIBUTE_TEMPLATE_NAME="attribute"; //$NON-NLS-1$
	
	//mareshkau, contains a name of custom template
	private static final String CUSTOM_TEMPLATE_NAME="vpeCustomTemplate"; //$NON-NLS-1$
	
	private static final String JSF2_CUSTOM_TEMPLATE="vpejsf2customTemplate"; //$NON-NLS-1$
	
	/**
	 * added by Max Areshkau, JBIDE-1494
	 * Contains default text formating data
	 */
	private static TextFormatingData defaultTextFormattingData;
	/**
	 * contains default text formating file name
	 */
	private static final String DEFAUL_TEXT_FORMATTING_CONF_FILE_NAME = 
		//File.separator + "resources" + File.separator + 
		"textFormatting.xml"; //$NON-NLS-1$
	/**
	 * Property which indicates that with this tag will be added default formats
	 */
	public static final String ATTR_USE_DEFAULT_FORMATS = "use-default-formats"; //$NON-NLS-1$
	/*
	 * Added by Max Areshkau(mareshkau@exadel.com)
	 */ 
	/**  This property identify namespace which should be used to load some specific class.
	 *  For example in rich:dataTable can be h:column, but rich:dataTable is separate plugin,
	 *  so to render h:column we should load the specific class for h:column from richfaces template
	 */
	private static final String NAMESPACE_IDENTIFIER_ATTRIBUTE = "namespaceIdentifier"; //$NON-NLS-1$

	private static final IPath DEFAULT_AUTO_TEMPLATES_PATH = VpePlugin.getDefault()
			.getStateLocation().append(VPE_TEMPLATES_AUTO);
	
	private VpeTemplateManager() {
		// singleton
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
	
	public VpeTemplate getTemplate(VpePageContext pageContext, Node sourceNode, Set<?> dependencySet) {
		VpeTemplate template = getTemplateImpl(pageContext, sourceNode, dependencySet);
		if (template != null) {
			return template;
		}
		return this.defTemplate;
	}

	private VpeTemplate getTemplateImpl(VpePageContext pageContext, Node sourceNode, Set<?> dependencySet) {
		//Fix for JBIDE-4179, mareshkau
		if((sourceNode instanceof Element) &&
				SourceDomUtil.isRenderedAttrEqFalse(pageContext,(Element) sourceNode)){
			return  VpeRenderingTemplate.getInstance();
		}
		
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

	/**
	 * 
	 * @param pageContext
	 * @param sourceNode
	 * @return name of template
	 */
	public  String getTemplateName(VpePageContext pageContext, Node sourceNode) {
		
		if(sourceNode==null) {
			return null;
		}
		switch (sourceNode.getNodeType()){
		
		case Node.TEXT_NODE:
			
			return TEXT_TEMPLATE_NAME;
			
		case Node.COMMENT_NODE:
			
			return COMMENT_TEMPLATE_NAME;
			
		case Node.ATTRIBUTE_NODE: 
			
			return  ATTRIBUTE_TEMPLATE_NAME;
		case Node.ELEMENT_NODE:
			String sourcePrefix = sourceNode.getPrefix();
			//added by Maksim Areshkau as fix for JBIDE-5352
			if(sourcePrefix==null) {
				sourcePrefix=""; //$NON-NLS-1$
			}
			
			if (((IDOMElement)sourceNode).isJSPTag()
						|| "jsp".equals(sourcePrefix)) { //$NON-NLS-1$
				return sourceNode.getNodeName();
			}
			
			List<TaglibData> taglibs = XmlUtil.getTaglibsForNode(sourceNode,pageContext);
			
			TaglibData sourceNodeTaglib = XmlUtil.getTaglibForPrefix(sourcePrefix, taglibs);		

			if(sourceNodeTaglib == null) {
				return sourceNode.getLocalName();
			}
			
			String sourceNodeUri = sourceNodeTaglib.getUri();

			
			String templateTaglibPrefix = getTemplateTaglibPrefix(sourceNodeUri);

			if(templateTaglibPrefix != null) {
				return templateTaglibPrefix + ":" + sourceNode.getLocalName();  //$NON-NLS-1$
			}
			
			if(sourceNodeUri!=null
					&& CustomTLDReference.isExistInCustomTlds(pageContext,sourceNodeUri)) {
				return VpeTemplateManager.CUSTOM_TEMPLATE_NAME;
			}
			if(sourceNodeUri!=null 
					&&CustomTLDReference.isExistInJsf2CustomComponenets(pageContext,sourceNodeUri,sourceNode.getLocalName()) ) {
				return VpeTemplateManager.JSF2_CUSTOM_TEMPLATE;
			}
			return sourceNode.getNodeName();
		default : 
			return null;
		}
		
	}
	
	public String getTemplateTaglibPrefix(String sourceUri) {
		return (String)templateTaglibs.get(sourceUri);
	}

	private void load() {
		initWithoutWhitespaceContainerSet();
		initPseudoElementContainerSet();
		/*
		 * loads templates configurations files
		 */
		templateFileList.load();
		/*
		 * load templates
		 */
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
			Element root = XMLUtilities.getElement(vpeFilePath.toFile(), null);
			loadTemplates(root,confElement);
	}
	
	private void loadTemplates(Element root, IConfigurationElement confElement) {
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
					} else if (TAG_TEMPLATE_TAGLIB.equals(node.getNodeName())) {
						setTemplateTaglib((Element)node);
					}
				}
			}
		}
	}
	
	private void setTagElement(Element tagElement,IConfigurationElement confElement) {
		String name = tagElement.getAttribute(ATTR_TAG_NAME);
		if (name.length() > 0) {
			boolean caseSensitive = !ATTR_VALUE_NO.equals(tagElement.getAttribute(ATTR_TAG_CASE_SENSITIVE));
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
	
	public void setDefTemplate(VpeTemplate defTemplate) {
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
		
		Element root = loadAutoTemplate(DEFAULT_AUTO_TEMPLATES_PATH);
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
						if (caseSensitive == !ATTR_VALUE_NO.equals(((Element)node).getAttribute(ATTR_TAG_CASE_SENSITIVE))) {
							String name = ((Element)node).getAttribute(ATTR_TAG_NAME);
							if (caseSensitive && name.equals(elementName) || !caseSensitive && name.equalsIgnoreCase(elementName)) {
								tagElement = node;
							}
						}
					} else if (TAG_TEMPLATE_TAGLIB.equals(node.getNodeName())) {
						Node prefixAttr = node.getAttributes().getNamedItem(ATTR_DIRECTIVE_TAGLIB_PREFIX);
						String prefix = prefixAttr != null ? prefixAttr.getNodeValue() : ""; //$NON-NLS-1$
						if (prefix.length() > 0) {
							prefixSet.add(prefix);
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
			IPath path = getAutoTemplates(DEFAULT_AUTO_TEMPLATES_PATH);
			XMLUtilities.serialize(root, path.toOSString());
		} catch(IOException e) {
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

	public List<VpeAnyData> getAnyTemplates() {
		return getAnyTemplates(DEFAULT_AUTO_TEMPLATES_PATH);
	}
	public List<VpeAnyData> getAnyTemplates(IPath path) {
		List<VpeAnyData> anyTemplateList = new ArrayList<VpeAnyData>();
		Map<String,Node> taglibs = new HashMap<String,Node>();

		Element root = loadAutoTemplate(path);
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
						VpeAnyData anyData = new VpeAnyData(attr != null ? attr.getNodeValue() : ""); //$NON-NLS-1$
						attr = ((Element)node).getAttributeNode(ATTR_TAG_CASE_SENSITIVE);
						anyData.setCaseSensitive(ATTR_VALUE_YES.equalsIgnoreCase(attr.getNodeValue()));
						Element templateNode = getChildNode(node, TAG_TEMPLATE);
						if (templateNode != null) {
							attr = templateNode.getAttributeNode(ATTR_TEMPLATE_CHILDREN);
							if (attr != null) {
								anyData.setChildren(ATTR_VALUE_YES.equalsIgnoreCase(attr.getNodeValue()));
							}
							
							attr = templateNode.getAttributeNode(ATTR_TEMPLATE_MODIFY);
							if (attr != null) {
								anyData.setModify(ATTR_VALUE_YES.equalsIgnoreCase(attr.getNodeValue()));
							}

							Element anyNode = getChildNode(templateNode, TAG_ANY);
							if (anyNode != null) {
								attr = anyNode.getAttributeNode(ATTR_ANY_TAG_FOR_DISPLAY);
								if(attr!=null) {
									anyData.setTagForDisplay(attr.getNodeValue());
								}
								
								attr = anyNode.getAttributeNode(ATTR_TEMPLATE_CHILDREN);
								if (attr != null) {
									anyData.setChildren(ATTR_VALUE_YES.equalsIgnoreCase(attr.getNodeValue()));
								}
								//TODO Max Areshkau This code was leave here for versions compatibility BEGIN
								StringBuffer stringBuffer = new StringBuffer();
								Node attrDisplay = anyNode.getAttributeNode(ATTR_ANY_DISPLAY);
								if (attr != null) {
									stringBuffer.append(HTML.ATTR_DISPLAY).append(":") //$NON-NLS-1$
									.append(attrDisplay.getNodeValue()).append(";"); //$NON-NLS-1$
								}
								//-----------END

								attr = anyNode.getAttributeNode(ATTR_ANY_VALUE);
								if (attr != null) {
									anyData.setValue(attr.getNodeValue());
								}
								
								attr = anyNode.getAttributeNode(ATTR_ANY_STYLE);
								if (attr !=null) {
									anyData.setStyle(attr.getNodeValue());
								}

								//TODO Max Areshkau This code was leave here for versions compatibility BEGIN
								Node attrBorder = anyNode.getAttributeNode(ATTR_ANY_BORDER);
								if (attrBorder != null) {
									stringBuffer.append("border-width:").append(attrBorder.getNodeValue()) //$NON-NLS-1$
									.append(";"); //$NON-NLS-1$
								}
								//-----------END
								//TODO Max Areshkau This code was leave here for versions compatibility BEGIN
								Node attrValueColor = anyNode.getAttributeNode(ATTR_ANY_VALUE_COLOR);
								if (attrValueColor  != null) {
									stringBuffer.append("color:").append(attrValueColor.getNodeValue()).append(";");  //$NON-NLS-1$//$NON-NLS-2$
								}
								//-----------END
								//TODO Max Areshkau This code was leave here for versions compatibility BEGIN
								Node attrValueBackgroundColor = anyNode.getAttributeNode(ATTR_ANY_VALUE_BACKGROUND_COLOR);
								if (attrValueBackgroundColor != null) {
									stringBuffer.append("background-color:").append(attrValueBackgroundColor.getNodeValue()).append(";");  //$NON-NLS-1$//$NON-NLS-2$
								}
								//-----------END
								//TODO Max Areshkau This code was leave here for versions compatibility BEGIN
								Node attrBachkgroundColor = anyNode.getAttributeNode(ATTR_ANY_BACKGROUND_COLOR);
								if (attrBachkgroundColor != null) {
									//early for displaying any tag was used <div><span></span></div>
									//and this property was for inner span, now used only one element 
									//and this property duplicates
									stringBuffer.append("background-color:").append(attrBachkgroundColor.getNodeValue()).append(";"); //$NON-NLS-1$ //$NON-NLS-2$
								}
								//-----------END
								//TODO Max Areshkau This code was leave here for versions compatibility BEGIN
								Node attrBorderColor = anyNode.getAttributeNode(ATTR_ANY_BORDER_COLOR);
								if (attrBorderColor  != null) {
									stringBuffer.append("border-color:").append(attrBorderColor.getNodeValue()).append(";"); //$NON-NLS-1$ //$NON-NLS-2$
								}
								if(stringBuffer.toString().length()>0) {
									anyData.setStyle(stringBuffer.toString());
								}
								//-----------END
							}
						}
						
						anyTemplateList.add(anyData);
					} else if (TAG_TEMPLATE_TAGLIB.equals(node.getNodeName())) {
						Node prefixAttr = node.getAttributes().getNamedItem(ATTR_DIRECTIVE_TAGLIB_PREFIX);
						String prefix = prefixAttr != null ? prefixAttr.getNodeValue() : ""; //$NON-NLS-1$
						if (prefix.length() > 0) {
							taglibs.put(prefix, node);
						}
					}
				}
			}
		}

		for (Iterator<VpeAnyData> iter = anyTemplateList.iterator(); iter.hasNext();) {
			VpeAnyData element = iter.next();
			String prefix = element.getPrefix();
			if (taglibs.containsKey(prefix)) {
				Node node = (Node)taglibs.get(prefix);
				Node uriAttr = node.getAttributes().getNamedItem(ATTR_DIRECTIVE_TAGLIB_URI);
				String uri = uriAttr != null ? uriAttr.getNodeValue() : ""; //$NON-NLS-1$
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
	
	public void setAnyTemplates(List<VpeAnyData> templates) {
		setAnyTemplates(templates, DEFAULT_AUTO_TEMPLATES_PATH);
	}
	
	public void setAnyTemplates(List<VpeAnyData> templates, IPath path) {
		if (templates != null) {
			Set<String> prefixSet = new HashSet<String>();
			Element root = XMLUtilities.createDocumentElement(TAG_TEMPLATES);
			Document document = root.getOwnerDocument();
			
			for (Iterator<VpeAnyData> iter = templates.iterator(); iter.hasNext();) {
				VpeAnyData data = iter.next();
				root.appendChild(createNewTagElement(document, data));
				String prefix = data.getPrefix();
				if ((prefix != null) && (prefix.length() > 0) && !prefixSet.contains(prefix)) {
					root = appendTaglib(prefixSet, document, root, data);
					prefixSet.add(prefix);
				}
			}
			
			try {
				// fixed bug [EFWPE-869] - uncomment this line
				XMLUtilities.serialize(root, path.toOSString());
			} catch(IOException e) {
				VpePlugin.reportProblem(e);
			}
		}
	}


	static public Element createNewTagElement(Document document, VpeAnyData data) {
		Element newTagElement = document.createElement(TAG_TAG);
		newTagElement.setAttribute(ATTR_TAG_NAME, data.getName());
		newTagElement.setAttribute(ATTR_TAG_CASE_SENSITIVE, data.isCaseSensitive() ? ATTR_VALUE_YES : ATTR_VALUE_NO);

		Element newTemplateElement = document.createElement(TAG_TEMPLATE);
		newTemplateElement.setAttribute(ATTR_TEMPLATE_CHILDREN, data.isChildren() ? ATTR_VALUE_YES : ATTR_VALUE_NO);
		newTemplateElement.setAttribute(ATTR_TEMPLATE_MODIFY, data.isModify() ? ATTR_VALUE_YES : ATTR_VALUE_NO);
		newTagElement.appendChild(newTemplateElement);

		Element newAnyElement = document.createElement(TAG_ANY);
		
		if(data.getTagForDisplay()!=null&& data.getTagForDisplay().length() > 0)
			newAnyElement.setAttribute(ATTR_ANY_TAG_FOR_DISPLAY, data.getTagForDisplay());
//		if (data.getDisplay() != null && data.getDisplay().length() > 0) 
//			newAnyElement.setAttribute(ATTR_ANY_DISPLAY, data.getDisplay());
		if (data.getValue() != null && data.getValue().length() > 0) 
			newAnyElement.setAttribute(ATTR_ANY_VALUE, data.getValue());
		if(data.getStyle()!=null && data.getStyle().length()>0) 
			newAnyElement.setAttribute(ATTR_ANY_STYLE, data.getStyle());
//		if (data.getBorder() != null && data.getBorder().length() > 0) 
//			newAnyElement.setAttribute(ATTR_ANY_BORDER, data.getBorder());
//		if (data.getValueColor() != null && data.getValueColor().length() > 0) 
//			newAnyElement.setAttribute(ATTR_ANY_VALUE_COLOR, data.getValueColor());
//		if (data.getValueBackgroundColor() != null && data.getValueBackgroundColor().length() > 0) 
//			newAnyElement.setAttribute(ATTR_ANY_VALUE_BACKGROUND_COLOR, data.getValueBackgroundColor());
//		if (data.getBackgroundColor() != null && data.getBackgroundColor().length() > 0)
//			newAnyElement.setAttribute(ATTR_ANY_BACKGROUND_COLOR, data.getBackgroundColor());
//		if (data.getBorderColor() != null && data.getBorderColor().length() > 0)
//			newAnyElement.setAttribute(ATTR_ANY_BORDER_COLOR, data.getBorderColor());
		
		newTemplateElement.appendChild(newAnyElement);

		return newTagElement;
	}

	private Element createNewTaglibElement(Document document, VpeAnyData data) {
		Element newTaglibElement = document.createElement(TAG_TEMPLATE_TAGLIB);
		newTaglibElement.setAttribute(ATTR_DIRECTIVE_TAGLIB_PREFIX, data.getPrefix());
		newTaglibElement.setAttribute(ATTR_DIRECTIVE_TAGLIB_URI, data.getUri());
		return newTaglibElement;
	}

	private Element loadAutoTemplate(IPath path) {
		Element root = XMLUtilities.getElement(path.toFile(), null);
		if (root != null && TAG_TEMPLATES.equals(root.getNodeName())) {
			return root;
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
		newTemplateElement.setAttribute(ATTR_TEMPLATE_CHILDREN, ATTR_VALUE_YES);
		newTemplateElement.setAttribute(ATTR_TEMPLATE_MODIFY, ATTR_VALUE_NO);
		Document document = newTemplateElement.getOwnerDocument();
		Element newAnyElement = document.createElement(TAG_ANY);
		newAnyElement.setAttribute(ATTR_ANY_VALUE, "{name()}"); //$NON-NLS-1$
		newAnyElement.setAttribute("title", "{tagstring()}"); //$NON-NLS-1$ //$NON-NLS-2$
		newTemplateElement.appendChild(newAnyElement);
		return newTemplateElement;
	}
	
	static String[] WITHOUT_WHITESPACE_ELEMENT_NAMES = {
		HTML.TAG_TABLE,
		HTML.TAG_CAPTION,
		HTML.TAG_COL,
		HTML.TAG_COLGROUP,
		HTML.TAG_THEAD,
		HTML.TAG_TBODY,
		HTML.TAG_TFOOT,
		HTML.TAG_TH,
		HTML.TAG_TR,
		HTML.TAG_TD
	};
	
	private void initWithoutWhitespaceContainerSet() {
		for (int i = 0; i < WITHOUT_WHITESPACE_ELEMENT_NAMES.length; i++) {
			withoutWhitespaceContainerSet.add(WITHOUT_WHITESPACE_ELEMENT_NAMES[i]);
		}
	}
	
	private void initPseudoElementContainerSet() {
		withoutPseudoElementContainerSet.add(HTML.TAG_BR);
		withoutPseudoElementContainerSet.add(HTML.TAG_INPUT);
	}
	
	public boolean isWithoutWhitespaceContainer(String name) {
		return withoutWhitespaceContainerSet.contains(name.toLowerCase());
	}
	
	public boolean isWithoutPseudoElementContainer(String name) {
		return withoutPseudoElementContainerSet.contains(name.toLowerCase());
	}
	
	@SuppressWarnings("unchecked")
	private VpeTemplate createTemplate(Element templateElement,IConfigurationElement confElement, boolean caseSensitive) {
		VpeTemplate template = null;
		String templateClassName = templateElement
				.getAttribute(VpeTemplateManager.ATTR_TEMPLATE_CLASS);
		String nameSpaceIdentifyer = templateElement
				.getAttribute(VpeTemplateManager.NAMESPACE_IDENTIFIER_ATTRIBUTE);
		if (templateClassName != null && templateClassName.length() > 0) {
			try {
				Bundle bundle;
				if (nameSpaceIdentifyer == null
						|| nameSpaceIdentifyer.length() == 0) {
					nameSpaceIdentifyer = confElement.getNamespaceIdentifier();
				}
				bundle = Platform.getBundle(nameSpaceIdentifyer);

				Class templateClass = bundle.loadClass(templateClassName);
				template = (VpeTemplate) templateClass.newInstance();
			} catch (ClassNotFoundException e) {
				template = handleTemplateClassLoadException(template,
						templateClassName, nameSpaceIdentifyer, e);
			} catch (IllegalAccessException e) {
				template = handleTemplateClassLoadException(template,
						templateClassName, nameSpaceIdentifyer, e);
			} catch (InstantiationException e) {
				template = handleTemplateClassLoadException(template,
						templateClassName, nameSpaceIdentifyer, e);
			}
		} else {
			template = new VpeHtmlTemplate();
		}
		if (template != null) {
			template.init(templateElement, caseSensitive);
		}
		return template;
	}

	private VpeTemplate handleTemplateClassLoadException(VpeTemplate template,
			String templateClassName, String nameSpaceIdentifyer,
			Exception e) {

			Class templateClass = null;
			try {
				templateClass = Class.forName(templateClassName);
			} catch (ClassNotFoundException e1) {
				handleTemplateLoadException(templateClassName,
						nameSpaceIdentifyer, e1);
			}
			try {
				if(templateClass!=null) {
					template = (VpeTemplate)templateClass.newInstance();
				}
			} catch (InstantiationException e2) {
				handleTemplateLoadException(templateClassName,
						nameSpaceIdentifyer, e2);
			} catch (IllegalAccessException e3) {
				handleTemplateLoadException(templateClassName,
						nameSpaceIdentifyer, e3);
			}

		return template;
	}

	private void handleTemplateLoadException(String templateClassName,
			String nameSpaceIdentifyer, Exception e) {
		String message = e.getMessage();
		if(message==null) {
			message = "Can't get VPE template class: " + templateClassName + ", from bundle:" + nameSpaceIdentifyer; //$NON-NLS-1$ //$NON-NLS-2$
		}
		VpePlugin.getPluginLog().logError(message, e);
	}
	
	private VpeTemplate createDefTemplate() {
		VpeTemplate localDefTemplate = new VpeHtmlTemplate();
		localDefTemplate.init(createDefTemplateElement(), true);
		return localDefTemplate;
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

	/**
	 * Initialize and returns default text formatting data
	 * @return the defaultTextFormatingData
	 */
	public static TextFormatingData getDefaultTextFormattingData() {
		if(defaultTextFormattingData==null) {
			try {
				InputStream is = VpePlugin.getDefault().getBundle().getResource(DEFAUL_TEXT_FORMATTING_CONF_FILE_NAME).openStream();
				Element root = XMLUtilities.getElement(new InputStreamReader(is), null);
				defaultTextFormattingData = new TextFormatingData(root);
			} catch (IOException e) {
				VpePlugin.getPluginLog().logError(e);
			}
		}

		return defaultTextFormattingData;
	}
	
	/**
	 * Returns the user's template file path.
	 * <P>
	 * If the file does not exist in the workspace, it is created
	 * by copying the default user template file to the workspace.
	 * </P>
	 * 
	 * @return user's template file path
	 * @throws IOException
	 * 
	 * @see <a href="https://jira.jboss.org/jira/browse/JBIDE-4131" >
				JBIDE-4131: Change saving of vpe auto templates</a> 
	 */
	public static IPath getAutoTemplates(IPath workspaceTemplatePath) {
		final File workspaceTemplateFile = workspaceTemplatePath.toFile();
		if (!workspaceTemplateFile.exists()) {
			IPath dafaultTemplatePath;
			try {
				dafaultTemplatePath = VpeTemplateFileList
						.getFilePath(EMPTY_VPE_TEMPLATES_AUTO, null);
			final File defaultTemplateFile = dafaultTemplatePath.toFile();
			copy(defaultTemplateFile, workspaceTemplateFile);
			} catch (IOException e) {
				VpePlugin.reportProblem(e);
			}
		}
		return workspaceTemplatePath;
	}
	

	public static IPath getAutoTemplates() {
		return getAutoTemplates(DEFAULT_AUTO_TEMPLATES_PATH);
	}

    /** 
     * Copies {@code src} file to {@code dst} file.
     * If the {@code dst} file does not exist, it is created.
     */
    private static void copy(File src, File dst) throws IOException {
    	// make sure the parent directory of the dst file is exist
    	dst.getParentFile().mkdirs();

        final InputStream in = new FileInputStream(src);
        final OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        final byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
