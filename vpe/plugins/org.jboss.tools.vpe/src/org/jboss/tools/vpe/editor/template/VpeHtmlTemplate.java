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
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.jboss.tools.jst.jsp.editor.ITextFormatter;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeSourceDomBuilder;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.selection.VpeSourceSelection;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMText;
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

	static final String ATTR_STYLE = "style";
	public static final String ATTR_STYLE_MODIFY_NAME = "-moz-user-modify";
	public static final String ATTR_STYLE_MODIFY_READ_WRITE_VALUE = "read-write";
	public static final String ATTR_STYLE_MODIFY_READ_ONLY_VALUE = "read-only";
	static final String ATTR_CURSOR_POINTER = "cursor:pointer;";

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
				} else if (VpeTemplateManager.TAG_DATATABLE_COLUMN.equals(name)) {
					type = TYPE_DATATABLE_COLUMN;
					creator = new VpeDataTableColumnCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_FACET.equals(name)) {
					type = TYPE_FACET;
					creator = new VpeFacetCreator(templateSection, dependencyMap, caseSensitive);
				} else if (VpeTemplateManager.TAG_List.equals(name)) {
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
		VpeCreatorInfo creatorInfo = createVisualElement(pageContext, (Element)sourceNode, visualDocument, null, visualNodeMap);
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
		validateVisualElement(pageContext, (Element)sourceNode, visualDocument, null, (nsIDOMElement)creationdata.getNode(), (Map<VpeTemplate,ModifyInfo>)creationdata.getData());
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
		removeElement(pageContext, (Element)sourceNode, (Map<VpeTemplate,?>) data);
	}
	@Override
	public boolean isChildren() {
		return creator == null ? false : children;
	}
	
	private VpeCreatorInfo createVisualElement(VpePageContext pageContext, Element sourceElement, nsIDOMDocument visualDocument, nsIDOMElement visualParent, Map<VpeTemplate,ModifyInfo> visualNodeMap) {
		if (creator == null) {
			return null;
		}
		
		VpeCreatorInfo elementInfo = creator.create(pageContext, sourceElement, visualDocument, visualParent, visualNodeMap);
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
	
	/**
	 * 
	 * Deprecated
	 */
	@Override
	public void openBundleEditors(VpePageContext pageContext, Element sourceElement, Object data) {
		openBundleEditors(pageContext, sourceElement, (Map<VpeTemplate,nsIDOMElement>) data);
	}
	
	private void openBundleEditors(VpePageContext pageContext, Element sourceElement, Map<VpeTemplate,nsIDOMElement> visualNodeMap) {
		if (dependencyFromBundle) {
			VpeCreator[] creators = dependencyMap.getCreators(VpeExpressionBuilder.SIGNATURE_JSF_VALUE);
			for (int i = 0; i < creators.length; i++) {
				if (creators[i] instanceof VpeOutputAttributes) {
					String[] outputAttrs = ((VpeOutputAttributes)creators[i]).getOutputAttributes();
					if (outputAttrs != null) {
						for (int j = 0; j < outputAttrs.length; j++) {
							String value = sourceElement.getAttribute(outputAttrs[j]);
							if (value != null) {
								pageContext.getBundle().openBundle(value, getPageLocale(pageContext, (IDOMElement)sourceElement));
							}
						}
					}
				}
			}
		}
	}

	private static final String VIEW_TAGNAME = "view";
	private static final String LOCALE_ATTRNAME = "locale";
	private static final String PREFIX_SEPARATOR = ":";

	private String getPageLocale(VpePageContext pageContext, IDOMElement sourceElement) {
		IStructuredModel model = null;
		try {

			List<TaglibData> taglibs = pageContext.getTagLibs(sourceElement);
			// Find F tracker
			TaglibData fTD = null;
			for (int i = 0; i < taglibs.size(); i++) {
				TaglibData td = (TaglibData)taglibs.get(i);
				if ("http://java.sun.com/jsf/core".equals(td.getUri())) fTD = td;
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
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
			return null;
		} finally {
			if (model != null)	model.releaseFromRead();
		}
	}

	
	/**
	 * 
	 * Deprecated
	 */
	@Override
	public void openIncludeEditor(VpePageContext pageContext, Element sourceElement, Object data) {
		openIncludeEditor(pageContext, sourceElement, (Map<?,?>) data);
	}
	
	private void openIncludeEditor(VpePageContext pageContext, Element sourceElement, Map<?,?> visualNodeMap) {
		if (sourceElement != null) {
		    String file = null;
		    if ("jsp:directive.include".equals(sourceElement.getNodeName())) {
			    file = sourceElement.getAttribute("file");
		    } else if ("jsp:include".equals(sourceElement.getNodeName())) {
			    file = sourceElement.getAttribute("page");
		    }
		    if (file != null) {
			    pageContext.openIncludeFile(file);
		    }
		}
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
	public String[] getOutputAtributeNames() {
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
		String s = ATTR_STYLE_MODIFY_NAME + ":" + 
				(modify ? ATTR_STYLE_MODIFY_READ_WRITE_VALUE : ATTR_STYLE_MODIFY_READ_ONLY_VALUE);
		String style = visualElement.getAttribute(ATTR_STYLE);
		if (style != null && style.length() > 0) {
			if (style.indexOf(ATTR_STYLE_MODIFY_NAME) >= 0) {
				String[] items =  style.split(";");
				style = "";
				for (int i = 0; i < items.length; i++) {
					String[] item = items[i].split(":");
					if (ATTR_STYLE_MODIFY_NAME.trim().equalsIgnoreCase(item[0].trim())) {
						item[1] = modify ? ATTR_STYLE_MODIFY_READ_WRITE_VALUE : ATTR_STYLE_MODIFY_READ_ONLY_VALUE;
					}
					style += item[0] + ":" + item[1] + ";";
				}
			} else {
				if (!";".equals(style.substring(style.length() - 1)))  style += ";";
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
	public boolean isRecreateAtAttrChange(VpePageContext pageContext, Element sourceElement, nsIDOMDocument visualDocument, nsIDOMElement visualNode, Object data, String name, String value) {
		if (creator != null) {
			return creator.isRecreateAtAttrChange(pageContext, sourceElement, visualDocument, visualNode, data, name, value);
		}
		return false;
	}
	@Override
	public Node getNodeForUptate(VpePageContext pageContext, Node sourceNode, nsIDOMNode visualNode, Object data) {
		// TODO Sergey Vasilyev redevelop JSF's facet template
		if (sourceNode.getNodeName().endsWith(":facet")) {
			return sourceNode.getParentNode();
		}
		
		if (creator != null) {
			return creator.getNodeForUptate(pageContext, sourceNode, visualNode, (Map<VpeTemplate,?>)data);
		}
		return null;
	}
}