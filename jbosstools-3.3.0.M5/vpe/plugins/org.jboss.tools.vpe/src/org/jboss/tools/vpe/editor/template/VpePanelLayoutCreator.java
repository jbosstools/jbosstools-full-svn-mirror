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

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.wst.sse.core.internal.provisional.StructuredModelManager;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.util.FileUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings("nls")
public class VpePanelLayoutCreator extends VpeAbstractCreator {



	public static final String ATTR_LAYOUT_DEFAULT_VALUE = "classic";
	public static final String EMPTY_ATTR_VALUE = "";

	public static final String CSS_CLASS_HEADER_DEFAULT = "pageHeader";
	public static final String CSS_CLASS_NAVIGATOR_DEFAULT = "pageNavigation";
	public static final String CSS_CLASS_BODY_DEFAULT = "pageBody";
	public static final String CSS_CLASS_FOOTER_DEFAULT = "pageFooter";

	//           layout | header | navigation | body  | footer
	//==================|========|============|=======|========
	// classic (Default)| top    | left       | right | bottom
	// navigationRight  | top    | right      | left  | bottom
	// upsideDown       | bottom | left       | right | top
	public static final String ATTR_PL_LAYOUT = "layout";

	public static final String ATTR_PL_CLASS = "class";
	public static final String ATTR_PL_STYLE = "style";
	// TAGS for layout attribute
	public static final String LAYOUT_CLASSIC_ATTR_VALUE = "classic";
	public static final String LAYOUT_NAVIGATION_RIGHT_ATTR_VALUE = "navigationRight";
	public static final String LAYOUT_UPSIDEDOWN_VALUE = "upsideDown";

	// Attributes wil mapped for body td
	public static final String ATTR_PL_BODY_CLASS = "bodyClass"; // CSS class to be used for the table cell.
	public static final String ATTR_PL_BODY_STYLE = "bodyStyle"; // CSS style to be used for the table cell.

	public static final String[][] MAP_ATTR_TO_BODY = {
		{ATTR_PL_CLASS,ATTR_PL_BODY_CLASS,CSS_CLASS_BODY_DEFAULT},
		{ATTR_PL_STYLE,ATTR_PL_BODY_STYLE,EMPTY_ATTR_VALUE}
	};

	// Attributes wil mapped for footer td
	public static final String ATTR_PL_FOOTER_CLASS = "footerClass"; // CSS class to be used for the table cell.
	public static final String ATTR_PL_FOOTER_STYLE = "footerStyle"; // CSS style to be used for the table cell.

	public static final String[][] MAP_ATTR_TO_FOOTER = {
		{ATTR_PL_CLASS,ATTR_PL_FOOTER_CLASS,CSS_CLASS_FOOTER_DEFAULT},
		{ATTR_PL_STYLE,ATTR_PL_FOOTER_STYLE,EMPTY_ATTR_VALUE}
	};

	// Attributes wil mapped for header td
	public static final String ATTR_PL_HEADER_CLASS = "headerClass"; // CSS class to be used for the table cell.
	public static final String ATTR_PL_HEADER_STYLE = "headerStyle"; // CSS style to be used for the table cell.

	public static final String[][] MAP_ATTR_TO_HEADER = {
		{ATTR_PL_CLASS,ATTR_PL_HEADER_CLASS,CSS_CLASS_HEADER_DEFAULT},
		{ATTR_PL_STYLE,ATTR_PL_HEADER_STYLE,EMPTY_ATTR_VALUE}
	};

	// Attributes wil mapped for navigation td
	public static final String ATTR_PL_NAVIGATION_CLASS = "navigationClass"; // CSS class to be used for the table cell.
	public static final String ATTR_PL_NAVIOGATION = "navigationStyle"; // CSS style to be used for the table cell.

	public static final String[][] MAP_ATTR_TO_NAVIGATION = {
		{ATTR_PL_CLASS,ATTR_PL_NAVIGATION_CLASS,CSS_CLASS_NAVIGATOR_DEFAULT},
		{ATTR_PL_STYLE,ATTR_PL_NAVIOGATION,EMPTY_ATTR_VALUE}
	};

	// Attributes will mapped to <table>
	public static final String ATTR_PL_ID = "id"; // Every component may have an unique id. Automatically created if omitted.
	public static final String ATTR_PL_DIR = "dir"; // No Description
	public static final String ATTR_PL_LANG = "lang"; // No Description
	public static final String ATTR_PL_WIDTH = "width"; // No Description
	public static final String ATTR_PL_STYLECLASS = "styleClass"; // Corresponds to the HTML class attribute.
	public static final String ATTR_PL_ALLIGN = "align"; // No Description
	public static final String ATTR_PL_BGCOLOR = "bgcolor";	// No Description
	public static final String ATTR_PL_BORDER = "border"; // No Description
	public static final String ATTR_PL_CELLPADDING = "cellpadding"; // No Description
	public static final String ATTR_PL_CELLSPACING = "cellspacing"; // No Description

	public static final String[][] MAP_ATTR_TO_TABLE = {
		{ATTR_PL_ID,ATTR_PL_ID,EMPTY_ATTR_VALUE},
		{ATTR_PL_DIR,ATTR_PL_DIR,EMPTY_ATTR_VALUE},
		{ATTR_PL_LANG,ATTR_PL_LANG,EMPTY_ATTR_VALUE},
		{ATTR_PL_WIDTH,ATTR_PL_WIDTH,EMPTY_ATTR_VALUE},
		{ATTR_PL_STYLE,ATTR_PL_STYLE,EMPTY_ATTR_VALUE},
		{ATTR_PL_STYLECLASS,ATTR_PL_STYLECLASS,EMPTY_ATTR_VALUE},
		{ATTR_PL_ALLIGN, ATTR_PL_ALLIGN,EMPTY_ATTR_VALUE},
		{ATTR_PL_BGCOLOR,ATTR_PL_BGCOLOR,EMPTY_ATTR_VALUE},
		{ATTR_PL_BORDER,ATTR_PL_BORDER,EMPTY_ATTR_VALUE},
		{ATTR_PL_CELLPADDING,ATTR_PL_CELLPADDING,EMPTY_ATTR_VALUE},
		{ATTR_PL_CELLSPACING,ATTR_PL_CELLSPACING,EMPTY_ATTR_VALUE}
	};

	// Not Mapped Attributes
		// event is never mapped
	public static final String ATTR_PL_ONCLICK = "onclick"; // No Description
	public static final String ATTR_PL_ONDBCLICK = "ondblclick"; // No Description
	public static final String ATTR_PL_ONKEYDOWN = "onkeydown"; // No Description
	public static final String ATTR_PL_ONKEYPRESS = "onkeypress"; // No Description
	public static final String ATTR_PL_ONKEYUP = "onkeyup"; // No Description
	public static final String ATTR_PL_ONMOUSEDOWN = "onmousedown"; // No Description
	public static final String ATTR_PL_ONMOUSEMOVE = "onmousemove"; // No Description
	public static final String ATTR_PL_ONMOUSEOUT = "onmouseout"; // No Description
	public static final String ATTR_PL_ONMOUSEOVER = "onmouseover"; // No Description
	public static final String ATTR_PL_ONMOUSEUP = "onmouseup"; // No Description
	public static final String ATTR_PL_RENDERED = "rendered"; // If false, this component will not be rendered.
		// Unknown attributes
	public static final String ATTR_PL_FRAME = "frame"; // No Description
	public static final String ATTR_PL_RULES = "rules"; // No Description
	public static final String ATTR_PL_SUMMARY = "summary"; // No Description
	public static final String ATTR_PL_TITLE = "title"; // No Description

	public static final String ATTR_PL_DATAFLD = "datafld"; // No Description
	public static final String ATTR_PL_DATAFORMATS = "dataformatas"; // No Description
	public static final String ATTR_PL_DATASCR = "datasrc"; // No Description

	public static final String EMPTY_ATTR = null;

	private boolean caseSensitive;

	VpePanelLayoutCreator(Element panelLayout, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		build(panelLayout, dependencyMap);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeAbstractCreator#isRecreateAtAttrChange(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element, org.mozilla.interfaces.nsIDOMDocument, org.mozilla.interfaces.nsIDOMNode, java.lang.Object, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isRecreateAtAttrChange(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMNode visualNode, Object data, String name, String value) {
		Map visualNodeMap = (Map)data;
		if(name.equals(ATTR_PL_LAYOUT)) {
			String layoutName = (String)visualNodeMap.get(ATTR_PL_LAYOUT);
			if(!value.equals(VpePanelLayoutCreator.LAYOUT_NAVIGATION_RIGHT_ATTR_VALUE)
					&& !value.equals(VpePanelLayoutCreator.LAYOUT_UPSIDEDOWN_VALUE)) {
				value = LAYOUT_CLASSIC_ATTR_VALUE;
			}
			return !layoutName.equals(value);
		} return false;
	}

	@Override
	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument, nsIDOMElement visualElement, Map visualNodeMap) {

		VpePanelLayoutElements layoutElements = new VpePanelLayoutElements(sourceNode, pageContext);
		visualNodeMap.put(ATTR_PL_LAYOUT, layoutElements.getLayoutName());
		PanelLayoutTable layout = new PanelLayoutTable(visualDocument,sourceNode);
		visualNodeMap.put(this,layout);

		if(LAYOUT_NAVIGATION_RIGHT_ATTR_VALUE.equals(layoutElements.getLayoutName())) {
			layout.setTop(layoutElements.getHeaderFacet(),MAP_ATTR_TO_HEADER);
			layout.setRight(layoutElements.getNavigationFacet(),MAP_ATTR_TO_NAVIGATION);
			layout.setLeft(layoutElements.getBodyFacet(),MAP_ATTR_TO_BODY);
			layout.setBottom(layoutElements.getFooterFacet(),MAP_ATTR_TO_FOOTER);
		} else if(LAYOUT_UPSIDEDOWN_VALUE.equals(layoutElements.getLayoutName())) {
			layout.setBottom(layoutElements.getHeaderFacet(),MAP_ATTR_TO_HEADER);
			layout.setLeft(layoutElements.getNavigationFacet(),MAP_ATTR_TO_NAVIGATION);
			layout.setRight(layoutElements.getBodyFacet(),MAP_ATTR_TO_BODY);
			layout.setTop(layoutElements.getFooterFacet(),MAP_ATTR_TO_FOOTER);
		} else { // LAYOUT_CLASSIC_ATTR_VALUE
			layout.setTop(layoutElements.getHeaderFacet(),MAP_ATTR_TO_HEADER);
			layout.setLeft(layoutElements.getNavigationFacet(),MAP_ATTR_TO_NAVIGATION);
			layout.setRight(layoutElements.getBodyFacet(),MAP_ATTR_TO_BODY);
			layout.setBottom(layoutElements.getFooterFacet(),MAP_ATTR_TO_FOOTER);
		}
		return layout.getVpeCreatorInfo();
	}

	public static class VpePanelLayoutElements {

		Node layout = null;
		Node header = null;
		Node footer = null;
		Node navigation = null;
		Node body = null;
		VpePageContext pageContext = null;

		public VpePanelLayoutElements(Node source,VpePageContext context) {
			pageContext = context;
			init(source);
			layout = source;

		}

		public Node getLayoutNode() {
			return layout;
		}

		public String getLayoutName() {
			String name =  getAttributeValue(layout,ATTR_PL_LAYOUT, ATTR_LAYOUT_DEFAULT_VALUE);
			if(name.equals(LAYOUT_NAVIGATION_RIGHT_ATTR_VALUE)
					|| name.equals(LAYOUT_UPSIDEDOWN_VALUE)) {
				return name;
			}
			return LAYOUT_CLASSIC_ATTR_VALUE;
		}

		private void init(Node source){
			init(source, null);
		}

		private void init(Node source, Node include) {
			NodeList list = source.getChildNodes();
			int cnt = list != null ? list.getLength() : 0;
			if (cnt > 0) {
				for (int i = 0; i < cnt; i++) {
					Node node = list.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						boolean isFacet = node.getNodeName().indexOf(":facet") > 0;
						boolean isInclude = node.getNodeName().indexOf("jsp:include") >=0 || node.getNodeName().indexOf("jsp:directive.include")>=0;
						Node attrName = node.getAttributes().getNamedItem("name");
						if(isFacet && attrName!=null) {
							String nodeValue = attrName.getNodeValue();
							if("header".equals(nodeValue)) {
								if(include != null) header = include;
								else header = node;
							} else if("navigation".equals(nodeValue)) {
								if(include != null) navigation = include;
								else navigation = node;
							} else if("body".equals(nodeValue)) {
								if(include != null) body = include;
								else body = node;
							} else if("footer".equals(nodeValue)) {
								if(include != null) footer = include;
								else footer = node;
							}
						} else if(isInclude) {
							IDOMModel wtpModel = null;
							Attr page = ((Element)node).getAttributeNode("page");
							if (page == null) {
								page = ((Element)node).getAttributeNode("file");
							}


							if(page != null){
								String pageName = page.getNodeValue();
									IEditorInput input = pageContext.getEditPart().getEditorInput();
									IFile resource = FileUtil.getFile(input, pageName);
									if(resource != null && (resource).exists()){
										try{
											wtpModel = (IDOMModel)StructuredModelManager.getModelManager().getModelForRead(resource);
											if(wtpModel != null){
												init(wtpModel.getDocument(), node);
											}
										} catch(IOException ex) {
											VpePlugin.reportProblem(ex);
										} catch(CoreException ex) {
											VpePlugin.reportProblem(ex);
										} finally {
											if(wtpModel!=null)
												wtpModel.releaseFromRead();
										}
									}

							}
						}
					}
				}
			}
		}

		public Node getHeaderFacet() {
			return header;
		}

		public Node getFooterFacet() {
			return footer;
		}

		public Node getNavigationFacet() {
			return navigation;
		}

		public Node getBodyFacet() {
			return body;
		}

		public String getPanelLayoutAttribute(String nodeName, String defaultValue) {
			return VpePanelLayoutElements.getAttributeValue(this.layout,nodeName,defaultValue);
		}

		// TODO Move to Util Class
		static String getAttributeValue(Node node, String attributeName, String defaultValue) {
			NamedNodeMap attrs = node.getAttributes();
			Node attr = attrs.getNamedItem(attributeName);
			if(attr!=null)  {
				return attr.getNodeValue();
			}
			return defaultValue;
		}
		public static int DEST=0, SOURCE=1, DEFAULT =2;

		static void mapAttributes(nsIDOMElement dest, Node source, String[][] map) {
			for (int i = 0;i<map.length;i++) {
				dest.setAttribute(map[i][DEST],getAttributeValue(source,map[i][SOURCE],map[i][DEFAULT]));
			}
		}
	}

	public class PanelLayoutTable {

		public static final String TABLE = "table";
		public static final String TR = "tr";
		public static final String TD = "td";
		public static final String CLASS_ATTR = "class";
		public static final String STYLE_ATTR = "style";

		Table table;
		Td top;
		Td bottom;
		Td left;
		Td right;

		VpeChildrenInfo topInfo;
		VpeChildrenInfo bottomInfo;
		VpeChildrenInfo rightInfo;
		VpeChildrenInfo leftInfo;

		VpeCreatorInfo creatorInfo;



		public PanelLayoutTable(nsIDOMDocument visualDocument, Node source) {
			table = new Table(visualDocument, source);
			creatorInfo = new VpeCreatorInfo(table.getDomElement());
			Tr tr = table.crateRow();
			top = tr.createCell(2);
			tr = table.crateRow();
			left = tr.createCell();
			right = tr.createCell();
			tr = table.crateRow();
			bottom = tr.createCell(2);
		}

		public void setTop(Node node, String[][] styleMap) {
			if(node==null) return;
			topInfo = new VpeChildrenInfo(top.getDomElement());
			addVpeCreatorInfoChild(topInfo,top,node,styleMap);
		}

		public void setBottom(Node node,String[][] styleMap) {
			if(node==null) return;
			bottomInfo = new VpeChildrenInfo(bottom.getDomElement());
			addVpeCreatorInfoChild(bottomInfo,bottom,node,styleMap);
		}

		public void setLeft(Node node, String[][] styleMap) {
			if(node==null) return;
			leftInfo = new VpeChildrenInfo(left.getDomElement());
			addVpeCreatorInfoChild(leftInfo,left,node,styleMap);
		}

		public void setRight(Node node, String[][] styleMap) {
			if(node==null) return;
			rightInfo = new VpeChildrenInfo(right.getDomElement());
			addVpeCreatorInfoChild(rightInfo,right,node,styleMap);
		}

		public void updateTop(Node node, String[][] styleMap) {
			if(node==null) return;
			VpePanelLayoutElements.mapAttributes(top.getDomElement(),node,styleMap);
		}

		public void updateBottom(Node node,String[][] styleMap) {
			if(node==null) return;
			VpePanelLayoutElements.mapAttributes(bottom.getDomElement(),node,styleMap);
		}

		public void updateLeft(Node node, String[][] styleMap) {
			if(node==null) return;
			VpePanelLayoutElements.mapAttributes(left.getDomElement(),node,styleMap);
		}

		public void updateRight(Node node, String[][] styleMap) {
			if(node==null) return;
			VpePanelLayoutElements.mapAttributes(right.getDomElement(),node,styleMap);
		}

		public void update(Node node, String[][] styleMap) {
			VpePanelLayoutElements.mapAttributes(table.getDomElement(),node,styleMap);
		}

		public VpeCreatorInfo getVpeCreatorInfo() {
			return creatorInfo;
		}

		private void addVpeCreatorInfoChild(VpeChildrenInfo info,Td td, Node source, String[][] styleMap) {
			info.addSourceChild(source);
			VpePanelLayoutElements.mapAttributes(td.getDomElement(),source,styleMap);
			creatorInfo.addChildrenInfo(info);
		}
	}

	public interface ElementWrapper {
		public nsIDOMElement getDomElement();
		public nsIDOMDocument getOwnerDocument();
		public void setAttributeValue(String name, String value);
	}

	public class DefaultNodeWrapper implements ElementWrapper {

		protected nsIDOMElement element;

		public DefaultNodeWrapper(nsIDOMElement element) {
			this.element = element;
		}

		public nsIDOMElement getDomElement() {
			return element;
		}

		public nsIDOMDocument getOwnerDocument() {
			return element.getOwnerDocument();
		}

		public void setAttributeValue(String name, String value) {
			getDomElement().setAttribute(name,value);
		}

	}

	public class Table extends DefaultNodeWrapper {

		public Table(nsIDOMDocument visualDocument,Node source ) {
			super(visualDocument.createElement(PanelLayoutTable.TABLE));
			VpePanelLayoutElements.mapAttributes(
				getDomElement(),
				source,
				MAP_ATTR_TO_TABLE
			);
		}

		public Tr crateRow() {
			nsIDOMElement tr = getOwnerDocument().createElement(PanelLayoutTable.TR);
			getDomElement().appendChild(tr);
			return new Tr(tr);
		}

	}

	public class Tr extends DefaultNodeWrapper {
		public Tr(nsIDOMElement rowNode) {
			super(rowNode);
		}

		public Td createCell() {
			nsIDOMElement tr = getOwnerDocument().createElement(PanelLayoutTable.TD);
			getDomElement().appendChild(tr);
			return new Td(tr);
		}

		public Td createCell(int colspanNumber) {
			Td td = createCell();
			td.setAttributeValue("colspan",""+colspanNumber);
			return td;
		}

	}

	public class Td extends DefaultNodeWrapper {


		public Td(nsIDOMElement cellNode) {
			super(cellNode);
		}
	}

	private void build(Element element, VpeDependencyMap dependencyMap) {
		Set attrs = new HashSet();
		for(int i=0;i<MAP_ATTR_TO_TABLE.length;i++) {
			attrs.add(VpeExpressionBuilder.attrSignature(MAP_ATTR_TO_TABLE[i][0],caseSensitive));
		}
		dependencyMap.setCreator(this, attrs);
	}

	@Override
	public void setAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name, String value) {
		PanelLayoutTable layout = (PanelLayoutTable)visualNodeMap.get(this);
		VpePanelLayoutElements layoutElements = new VpePanelLayoutElements(sourceElement, pageContext);
		layout.update(sourceElement,MAP_ATTR_TO_TABLE);
		if(LAYOUT_NAVIGATION_RIGHT_ATTR_VALUE.equals(layoutElements.getLayoutName())) {
			layout.updateTop(layoutElements.getHeaderFacet(),MAP_ATTR_TO_HEADER);
			layout.updateLeft(layoutElements.getNavigationFacet(),MAP_ATTR_TO_NAVIGATION);
			layout.updateRight(layoutElements.getBodyFacet(),MAP_ATTR_TO_BODY);
			layout.updateBottom(layoutElements.getFooterFacet(),MAP_ATTR_TO_FOOTER);
		} else if(LAYOUT_UPSIDEDOWN_VALUE.equals(layoutElements.getLayoutName())) {
			layout.updateBottom(layoutElements.getHeaderFacet(),MAP_ATTR_TO_HEADER);
			layout.updateLeft(layoutElements.getNavigationFacet(),MAP_ATTR_TO_NAVIGATION);
			layout.updateRight(layoutElements.getBodyFacet(),MAP_ATTR_TO_BODY);
			layout.updateTop(layoutElements.getFooterFacet(),MAP_ATTR_TO_FOOTER);
		} else { // LAYOUT_CLASSIC_ATTR_VALUE
			layout.updateTop(layoutElements.getHeaderFacet(),MAP_ATTR_TO_HEADER);
			layout.updateLeft(layoutElements.getNavigationFacet(),MAP_ATTR_TO_NAVIGATION);
			layout.updateRight(layoutElements.getBodyFacet(),MAP_ATTR_TO_BODY);
			layout.updateBottom(layoutElements.getFooterFacet(),MAP_ATTR_TO_FOOTER);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeAbstractCreator#validate(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element, org.mozilla.interfaces.nsIDOMDocument, org.mozilla.interfaces.nsIDOMElement, org.mozilla.interfaces.nsIDOMElement, java.util.Map)
	 */
	@Override
	public void validate(VpePageContext pageContext, Element sourceElement,
			nsIDOMDocument visualDocument, nsIDOMElement visualParent,
			nsIDOMElement visualElement, Map visualNodeMap) {
	}

	@Override
	public void removeAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name) {
	}
}
