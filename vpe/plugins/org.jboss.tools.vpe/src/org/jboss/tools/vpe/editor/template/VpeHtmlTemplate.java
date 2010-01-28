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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.jboss.tools.jst.jsp.editor.ITextFormatter;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeSourceDomBuilder;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.selection.VpeSourceSelection;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.NodesManagingUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMText;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VpeHtmlTemplate extends VpeAbstractTemplate {
	public static final int TYPE_NONE = 0;
	public static final int TYPE_HTML = 1;
	public static final int TYPE_COPY = 2;
	public static final int TYPE_GRID = 3;
	public static final int TYPE_ANY = 4;
	public static final int TYPE_TAGLIB = 5;
	public static final int TYPE_LOAD_BUNDLE = 6;
	public static final int TYPE_DATATABLE = 7;
	public static final int TYPE_DATATABLE_COLUMN = 8;
	public static final int TYPE_COMMENT = 9;
	public static final int TYPE_STYLE = 10;
	public static final int TYPE_LINK = 11;
	//gavs
	public static final int TYPE_LIST = 12;
	public static final int TYPE_JSPROOT = 13;
	public static final int TYPE_LABELED_FORM = 13;
	
	public static final int TYPE_PANELGRID = 14;
	public static final int TYPE_FACET = 15;
	public static final int TYPE_INCLUDE = 16;
	public static final int PANEL_LAYOUT = 17;
	public static final int TYPE_A = 18;

	static final String ATTR_STYLE = "style"; //$NON-NLS-1$
	public static final String ATTR_STYLE_MODIFY_NAME = "-moz-user-modify"; //$NON-NLS-1$
	public static final String ATTR_STYLE_MODIFY_READ_WRITE_VALUE = "read-write"; //$NON-NLS-1$
	public static final String ATTR_STYLE_MODIFY_READ_ONLY_VALUE = "read-only"; //$NON-NLS-1$
	static final String ATTR_CURSOR_POINTER = "cursor:pointer;"; //$NON-NLS-1$

	private int type = TYPE_NONE;
	private VpeCreator creator = null;
	private VpeDependencyMap dependencyMap;
	private boolean dependencyFromBundle;
	@Override
	protected void init(Element templateElement) {
		dependencyMap = new VpeDependencyMap(caseSensitive);
		super.init(templateElement);
		dependencyMap.validate();
		dependencyFromBundle = dependencyMap.contains(VpeExpressionBuilder.SIGNATURE_JSF_VALUE);
	}
	@Override
	protected void initTemplateSection(Element templateSection) {
		if (creator == null) {
			String name = templateSection.getNodeName();
			if (name.startsWith(VpeTemplateManager.VPE_PREFIX)) {
				if (VpeTemplateManager.TAG_COPY.equals(name)) {
					type = TYPE_COPY;
					creator = new VpeCopyCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_ELEMENT.equals(name)) {
					type = TYPE_HTML;
					creator = new VpeElementCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_DATATABLE.equals(name)) {
					type = TYPE_DATATABLE;
					creator = new VpeDataTableCreator(templateSection, dependencyMap, caseSensitive);
				}
				else if (VpeTemplateManager.TAG_A.equals(name)) {
					type = TYPE_A;
					creator = new VpeVisualLinkCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_DATATABLE_COLUMN.equals(name)) {
					type = TYPE_DATATABLE_COLUMN;
					creator = new VpeDataTableColumnCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_FACET.equals(name)) {
					type = TYPE_FACET;
					creator = new VpeFacetCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_LIST.equals(name)) {
					type = TYPE_LIST;
					creator = new VpeListCreator(templateSection, dependencyMap, caseSensitive);
				// gavs	
				}	else if (VpeTemplateManager.TAG_LABELED_FORM.equals(name)) {
					type = TYPE_LABELED_FORM;
					creator = new VpeLabeledFormCreator(templateSection, dependencyMap, caseSensitive);
				// bela_n
				} else if (VpeTemplateManager.TAG_GRID.equals(name)) {
					type = TYPE_GRID;
					creator = new VpeGridCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_PANELGRID.equals(name)) {
					type = TYPE_PANELGRID;
					creator = new VpePanelGridCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_ANY.equals(name)) {
					type = TYPE_ANY;
					creator = new VpeAnyCreator(templateSection, dependencyMap, caseSensitive);
				} 
//				else if (VpeTemplateManager.TAG_TAGLIB.equals(name)) {
//					type = TYPE_TAGLIB;
//					creator = new VpeTaglibCreator(templateSection, dependencyMap);
//				}
				else if (VpeTemplateManager.TAG_LINK.equals(name)) {
					type = TYPE_LINK;
					creator = new VpeLinkCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_LOAD_BUNDLE.equals(name)) {
					type = TYPE_LOAD_BUNDLE;
					creator = new VpeLoadBundleCreator(templateSection, dependencyMap);
				} else if (VpeTemplateManager.TAG_COMMENT.equals(name)) {
					type = TYPE_COMMENT;
					creator = new VpeCommentCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_STYLE.equals(name)) {
					type = TYPE_STYLE;
					creator = new VpeStyleCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_JSPROOT.equals(name)) {
					type = TYPE_JSPROOT;
					creator = new VpeJspRootCreator(templateSection, dependencyMap);
				} else if (VpeTemplateManager.TAG_MY_FACES_PAGE_LAYOUT.equals(name)) {
					type = PANEL_LAYOUT;
					creator = new VpePanelLayoutCreator(templateSection, dependencyMap, caseSensitive);
				}
			} else {
				type = TYPE_HTML;
				creator = new VpeHtmlCreator(templateSection, dependencyMap, caseSensitive);
			}
		}
	}
	
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument) {
		Map<VpeTemplate, ModifyInfo> visualNodeMap = new HashMap<VpeTemplate, ModifyInfo> ();
		VpeCreatorInfo creatorInfo = null;
		if (sourceNode instanceof Element) {
			creatorInfo = createVisualElement(pageContext, (Element)sourceNode, visualDocument, null, visualNodeMap);
		}
		nsIDOMElement newVisualElement = null;
		if (creatorInfo != null) {
			newVisualElement = (nsIDOMElement)creatorInfo.getVisualNode();
		}
		VpeCreationData creationData = new VpeCreationData(newVisualElement);
		if (creatorInfo != null) {
			List<VpeChildrenInfo> childrenInfoList = creatorInfo.getChildrenInfoList();
			if (childrenInfoList != null) {
				for (int i = 0; i < childrenInfoList.size(); i++) {
					creationData.addChildrenInfo((VpeChildrenInfo)childrenInfoList.get(i));
				}
			}
		}
		creationData.setData(visualNodeMap);
		return creationData;
	}
	@Override
	public void validate(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument, VpeCreationData creationdata) {
		if (sourceNode instanceof Element)
			validateVisualElement(pageContext, (Element)sourceNode, visualDocument, null, creationdata.getNode()==null?null:
				(nsIDOMElement)(creationdata.getNode().queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID)), (Map<VpeTemplate,ModifyInfo>)creationdata.getData());
	}
	@Override
	public void setAttribute(VpePageContext pageContext, Element sourceElement, nsIDOMDocument visualDocument, nsIDOMNode visualNode, Object data, String name, String value) {
		setAttribute(pageContext, sourceElement, (Map<VpeTemplate,?>)data, name, value);
	}
	@Override
	public void removeAttribute(VpePageContext pageContext, Element sourceElement, nsIDOMDocument visualDocument, nsIDOMNode visualNode, Object data, String name) {
		removeAttribute(pageContext, sourceElement, (Map<VpeTemplate,?>)data, name);
	}
	@Override
	public void beforeRemove(VpePageContext pageContext, Node sourceNode, nsIDOMNode visualNode, Object data) {
		if (sourceNode instanceof Element)
			removeElement(pageContext, (Element)sourceNode, (Map<VpeTemplate,?>) data);
	}
	@Override
	public boolean hasChildren() {
		// was commented to correct work of non-visual templates which have
		// visual children
		// (for example rich:graphValidator)
		return /* creator == null ? false : */children;
	}
	
	private VpeCreatorInfo createVisualElement(VpePageContext pageContext, Element sourceElement, nsIDOMDocument visualDocument, nsIDOMElement visualParent, Map<VpeTemplate,ModifyInfo> visualNodeMap) {
		if (creator == null) {
			return null;
		}
		VpeCreatorInfo elementInfo =null;
		try {
			elementInfo = creator.create(pageContext, sourceElement, visualDocument, visualParent, visualNodeMap);
		} catch(VpeExpressionException ex) {
			
			VpeExpressionException exception = new VpeExpressionException("Exception on processing node "+sourceElement.toString(), ex); //$NON-NLS-1$
			VpePlugin.reportProblem(exception);
		}
		if (elementInfo != null) {
			nsIDOMElement visualElement = (nsIDOMElement)elementInfo.getVisualNode();
			if (visualElement != null) {
				boolean curModify = getCurrentModify(pageContext, sourceElement, visualNodeMap);
				makeModify(visualElement, curModify);
				if (!HTML.TAG_INPUT.equalsIgnoreCase(visualElement.getNodeName()) && dependencyFromBundle && !curModify) {
					String style = visualElement.getAttribute(ATTR_STYLE);
					visualElement.setAttribute(ATTR_STYLE, style + ATTR_CURSOR_POINTER);
				}
				visualNodeMap.put(this, new ModifyInfo(visualElement, curModify));
			}
		}
		if (dependencyFromBundle) {
			pageContext.addBundleDependency(sourceElement);
		}
		return elementInfo;
	}

	private void validateVisualElement(VpePageContext pageContext, Element sourceElement, nsIDOMDocument visualDocument, nsIDOMElement visualParent, nsIDOMElement visualElement, Map<VpeTemplate,ModifyInfo> visualNodeMap) {
		if (creator != null) {
			creator.validate(pageContext, sourceElement, visualDocument, visualParent, visualElement, visualNodeMap);
		}
	}
	@Override
	public boolean nonctrlKeyPressHandler(VpePageContext pageContext, Document sourceDocument,  Node sourceNode, nsIDOMNode visualNode, Object data, long charCode, VpeSourceSelection selection, ITextFormatter formatter) {
		if (creator != null) {
			boolean done = creator.nonctrlKeyPressHandler(pageContext, sourceDocument,  sourceNode, data, charCode, selection, formatter);
			if (done) {
				return true;
			}
			return super.nonctrlKeyPressHandler(pageContext, sourceDocument,  sourceNode, visualNode, data, charCode, selection, formatter);
		}
		return true;
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * Deprecated
	 */
	@Override
	public void refreshBundleValues(VpePageContext pageContext, Element sourceElement, Object data) {
		refreshBundleValues(pageContext, sourceElement, (Map<VpeTemplate,ModifyInfo>) data);
	}
	
	private void refreshBundleValues(VpePageContext pageContext, Element sourceElement, Map<VpeTemplate,ModifyInfo> visualNodeMap) {
		if (dependencyFromBundle) {
			VpeCreator[] creators = dependencyMap.getCreators(VpeExpressionBuilder.SIGNATURE_JSF_VALUE);
			for (int i = 0; i < creators.length; i++) {
				creators[i].refreshElement(pageContext, sourceElement, visualNodeMap);
			}
			changeModify(pageContext, sourceElement, visualNodeMap);
		}
	}

	/**
	 * 
	 * Deprecated
	 */
	@Override
	public int getType() {
		return type;
	}

	/**
	 * 
	 * Deprecated
	 */
	@Override
	public VpeAnyData getAnyData() {
		VpeAnyData data = null;
		if (getType() == TYPE_ANY && creator != null) {
			data = ((VpeAnyCreator)creator).getAnyData();
			data.setCaseSensitive(caseSensitive);
			data.setChildren(children);
			data.setModify(modify);
		}
		return data;
	}
	
	/**
	 * 
	 * Deprecated
	 */
	public boolean isDependencyFromBundle() {
		return dependencyFromBundle;
	}
	

	private static final String VIEW_TAGNAME = "view"; //$NON-NLS-1$
	private static final String LOCALE_ATTRNAME = "locale"; //$NON-NLS-1$
	private static final String PREFIX_SEPARATOR = ":"; //$NON-NLS-1$

	private String getPageLocale(VpePageContext pageContext, IDOMElement sourceElement) {

			List<TaglibData> taglibs = pageContext.getTagLibs(sourceElement);
			// Find F tracker
			TaglibData fTD = null;
			for (int i = 0; i < taglibs.size(); i++) {
				TaglibData td = (TaglibData)taglibs.get(i);
				if ("http://java.sun.com/jsf/core".equals(td.getUri())) fTD = td; //$NON-NLS-1$
			}
	
			if (fTD == null || fTD.getPrefix() == null || fTD.getPrefix().length() == 0) return null;

			String nodeToFind = fTD.getPrefix() + PREFIX_SEPARATOR + VIEW_TAGNAME; 
			
			IDOMElement el = sourceElement;
			
			IDOMElement jsfCoreViewTag = null;
			while (el != null) {
				if (nodeToFind.equals(el.getNodeName())) {
					jsfCoreViewTag = el;
					break;
				}
				Node parent = el.getParentNode();
				if(parent instanceof IDOMElement) {
					el = (IDOMElement)parent;					
				} else {
					break;
				}
			}
			
			if (jsfCoreViewTag == null || !jsfCoreViewTag.hasAttribute(LOCALE_ATTRNAME)) return null;
			
			String locale = jsfCoreViewTag.getAttribute(LOCALE_ATTRNAME);
			if (locale == null || locale.length() == 0) return null;
			return locale;
	}
	/**
	 * 
	 * Deprecated
	 */
	@Override
	public void setSourceAttributeValue(VpePageContext pageContext, Element sourceElement, Object data) {
		setSourceAttributeValue(pageContext, sourceElement, (Map<?,?>)data);
	}

	private void setSourceAttributeValue(VpePageContext pageContext, Element sourceElement, Map<?,?> visualNodeMap) {
		VpeCreator[] creators = dependencyMap.getCreators(VpeValueCreator.SIGNATURE_VPE_VALUE);
		for (int i = 0; i < creators.length; i++) {
			if (creators[i] instanceof VpeOutputAttributes) {
				((VpeOutputAttributes)creators[i]).setOutputAttributeValue(pageContext, sourceElement, visualNodeMap);
			}
		}
		changeModify(pageContext, sourceElement, visualNodeMap);
	}
	@Override
	public String[] getOutputAttributeNames() {
		VpeCreator[] creators = dependencyMap.getCreators(VpeValueCreator.SIGNATURE_VPE_VALUE);
		for (int i = 0; i < creators.length; i++) {
			if (creators[i] instanceof VpeOutputAttributes) {
				return ((VpeOutputAttributes)creators[i]).getOutputAttributes();
			}
		}
		return null;
	}
	@Override
	public nsIDOMText getOutputTextNode(VpePageContext pageContext, Element sourceElement, Object data) {
		VpeCreator[] creators = dependencyMap.getCreators(VpeValueCreator.SIGNATURE_VPE_VALUE);
		for (int i = 0; i < creators.length; i++) {
			if (creators[i] instanceof VpeOutputAttributes) {
				return ((VpeOutputAttributes)creators[i]).getOutputTextNode(pageContext, sourceElement, (Map<?,?>)data);
			}
		}
		return null;
	}

	/**
	 * 
	 * Deprecated
	 */
	@Override
	public void setSourceAttributeSelection(VpePageContext pageContext, Element sourceElement, int offset, int length, Object data) {
		setSourceAttributeSelection(pageContext, sourceElement, offset, length, (Map) data);
	}
	
	private void setSourceAttributeSelection(VpePageContext pageContext, Element sourceElement, int offset, int length, Map visualNodeMap) {
		VpeCreator[] creators = dependencyMap.getCreators(VpeValueCreator.SIGNATURE_VPE_VALUE);
		boolean setFlag = false;
		for (int i = 0; i < creators.length; i++) {
			if (creators[i] instanceof VpeOutputAttributes) {
				((VpeOutputAttributes)creators[i]).setOutputAttributeSelection(pageContext, sourceElement, offset, length, visualNodeMap);
				setFlag = true;
			}
		}
		if (!setFlag) {
			VpeSourceDomBuilder sourceBuilder = pageContext.getSourceBuilder();
			sourceBuilder.setSelection(sourceElement, 0, 0);
		}
		changeModify(pageContext, sourceElement, visualNodeMap);
	}
	
	static void makeModify(nsIDOMElement visualElement, boolean modify) {
		String s = ATTR_STYLE_MODIFY_NAME + ":" +  //$NON-NLS-1$
				(modify ? ATTR_STYLE_MODIFY_READ_WRITE_VALUE : ATTR_STYLE_MODIFY_READ_ONLY_VALUE);
		String style = visualElement.getAttribute(ATTR_STYLE);
		if (style != null && style.length() > 0) {
			if (style.indexOf(ATTR_STYLE_MODIFY_NAME) >= 0) {
				String[] items =  style.split(";"); //$NON-NLS-1$
				style = ""; //$NON-NLS-1$
				for (int i = 0; i < items.length; i++) {
					String[] item = items[i].split(":"); //$NON-NLS-1$
					if (ATTR_STYLE_MODIFY_NAME.trim().equalsIgnoreCase(item[0].trim())) {
						item[1] = modify ? ATTR_STYLE_MODIFY_READ_WRITE_VALUE : ATTR_STYLE_MODIFY_READ_ONLY_VALUE;
					}
					style += item[0] + ":" + item[1] + ";"; //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else {
				if (!";".equals(style.substring(style.length() - 1)))  style += ";"; //$NON-NLS-1$ //$NON-NLS-2$
				style += s;
			}
		} else {
			style = s;
		}
		visualElement.setAttribute(ATTR_STYLE, style);
	}
	
	private boolean getCurrentModify(VpePageContext pageContext, Element sourceElement, Map visualNodeMap) {
		if (!modify || !dependencyFromBundle) {
			return modify;
		}
		VpeCreator[] creators = dependencyMap.getCreators(VpeValueCreator.SIGNATURE_VPE_VALUE);
		for (int i = 0; i < creators.length; i++) {
			if (creators[i] instanceof VpeOutputAttributes) {
				boolean currModify = ((VpeOutputAttributes)creators[i]).isEditabledAtribute(pageContext, sourceElement, visualNodeMap);
				if (!currModify) {
					return false;
				}
			}
		}
		return true;
	}
	
	void changeModify(VpePageContext pageContext, Element sourceElement, Map visualNodeMap) {
		if (modify && dependencyFromBundle) {
			ModifyInfo info = (ModifyInfo)visualNodeMap.get(this);
			if (info != null) {
				boolean curModify = getCurrentModify(pageContext, sourceElement, visualNodeMap);
				if (curModify != info.modify) {
					info.modify = curModify;
					makeModify(info.visualElement, curModify);
				}
			}
		}
	}
	
	private static class ModifyInfo {
		private nsIDOMElement visualElement;
		private boolean modify;
		
		private ModifyInfo(nsIDOMElement visualElement, boolean modify) {
			this.visualElement = visualElement;
			this.modify = modify;
		}
	}

	/**
	 * 
	 * Deprecated
	 */
	@Override
	public boolean isOutputAttributes() {
		VpeCreator[] creators = dependencyMap.getCreators(VpeValueCreator.SIGNATURE_VPE_VALUE);
		for (int i = 0; i < creators.length; i++) {
			if (creators[i] instanceof VpeOutputAttributes) {
				return true;
			}
		}
		return false;
	}


//////////////////////////////////////////////
	private void removeElement(VpePageContext pageContext, Element sourceElement, Map<VpeTemplate,?> visualNodeMap) {
		if (creator != null) {
			if (dependencyFromBundle) {
				pageContext.removeBundleDependency(sourceElement);
			}
			creator.removeElement(pageContext, sourceElement, visualNodeMap);
		}
	}

	private void setAttribute(VpePageContext pageContext, Element sourceElement, Map<VpeTemplate,?> visualNodeMap, String name, String value) {
		if (creator != null) {
			setAttribute(dependencyMap.getCreators(VpeExpressionBuilder.SIGNATURE_ANY_ATTR), pageContext, sourceElement, visualNodeMap, name, value);
			setAttribute(dependencyMap.getCreators(VpeExpressionBuilder.attrSignature(name, caseSensitive)), pageContext, sourceElement, visualNodeMap, name, value);
			changeModify(pageContext, sourceElement, visualNodeMap);
		}
	}

	private void removeAttribute(VpePageContext pageContext, Element sourceElement, Map<VpeTemplate,?> visualNodeMap, String name) {
		if (creator != null) {
			removeAttribute(dependencyMap.getCreators(VpeExpressionBuilder.SIGNATURE_ANY_ATTR), pageContext, sourceElement, visualNodeMap, name);
			removeAttribute(dependencyMap.getCreators(VpeExpressionBuilder.attrSignature(name, caseSensitive)), pageContext, sourceElement, visualNodeMap, name);
			changeModify(pageContext, sourceElement, visualNodeMap);
		}
	}

	private void setAttribute(VpeCreator[] creators, VpePageContext pageContext, Element sourceElement, Map<VpeTemplate,?> visualNodeMap, String name, String value) {
		for (int i = 0; i < creators.length; i++) {
			creators[i].setAttribute(pageContext, sourceElement, visualNodeMap, name, value);
		}
	}
	
	private void removeAttribute(VpeCreator[] creators, VpePageContext pageContext, Element sourceElement, Map<VpeTemplate,?> visualNodeMap, String name) {
		for (int i = 0; i < creators.length; i++) {
			creators[i].removeAttribute(pageContext, sourceElement, visualNodeMap, name);
		}
	}
	@Override
	public boolean recreateAtAttrChange(VpePageContext pageContext, Element sourceElement, nsIDOMDocument visualDocument, nsIDOMElement visualNode, Object data, String name, String value) {
		if (creator != null) {
			return creator.isRecreateAtAttrChange(pageContext, sourceElement, visualDocument, visualNode, data, name, value);
		}
		return false;
	}
	@Override
	public Node getNodeForUpdate(VpePageContext pageContext, Node sourceNode, nsIDOMNode visualNode, Object data) {
		// TODO Sergey Vasilyev redevelop JSF's facet template
		if (sourceNode.getNodeName().endsWith(":facet")) { //$NON-NLS-1$
			return sourceNode.getParentNode();
		}
		
		if (creator != null) {
			return creator.getNodeForUpdate(pageContext, sourceNode, visualNode, (Map<VpeTemplate,?>)data);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeAbstractTemplate#getSourceRegionForOpenOn(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMNode)
	 */
	/**
	 * @author mareshkau
	 */
	@Override
	public IRegion getSourceRegionForOpenOn(VpePageContext pageContext,
			Node sourceNode, nsIDOMNode domNode) {
		if(sourceNode != null && sourceNode instanceof Element) {
			Element sourceElement = (Element) sourceNode;
		    String templateName = VpeTemplateManager.getInstance()
		    		.getTemplateName(pageContext, sourceElement);

		    Attr file = null;
		    if ("jsp:directive.include".equals(templateName)) { //$NON-NLS-1$
			    file = sourceElement.getAttributeNode("file"); //$NON-NLS-1$
		    } else if ("jsp:include".equals(templateName)) { //$NON-NLS-1$
			    file = sourceElement.getAttributeNode("page"); //$NON-NLS-1$
		    } else if (HTML.TAG_A.equalsIgnoreCase(templateName)) {
		    	file = sourceElement.getAttributeNode(HTML.ATTR_HREF);
		    } else if ("h:outputStylesheet".equals(templateName)//$NON-NLS-1$
		    		|| "h:graphicImage".equals(templateName)) { //$NON-NLS-1$
		    	file = sourceElement.getAttributeNode("name");  //$NON-NLS-1$
		    }

		    if(file != null) {
		    	return new Region(NodesManagingUtil.getStartOffsetNode(file), 0);
		    }
		}
		return null;
	}
}