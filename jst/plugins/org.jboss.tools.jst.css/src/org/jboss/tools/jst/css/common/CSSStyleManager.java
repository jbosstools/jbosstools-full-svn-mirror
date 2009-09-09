/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.css.common;

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.wst.css.core.internal.provisional.adapters.IStyleSheetAdapter;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSDocument;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSModel;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSNode;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNamedNodeMap;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.modelquery.ModelQueryUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.ElementCSSInlineStyle;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class CSSStyleManager {

	public static final String STYLE_TAG_NAME = "style"; //$NON-NLS-1$

	public static final String STYLE_ATTRIBUTE_NAME = "style"; //$NON-NLS-1$

	/**
	 * 
	 * @param selection
	 * @return
	 */
	public StyleContainer recognizeCSSStyle(ISelection selection) {

		StyleContainer container = null;
		if (selection instanceof IStructuredSelection) {

			Object selectedObject = ((IStructuredSelection) selection)
					.getFirstElement();

			// if selected object is node in css file
			if (selectedObject instanceof ICSSNode) {

				CSSStyleRule styleRule = getStyleRule((ICSSNode) selectedObject);

				if (styleRule != null)
					container = new CSSStyleRuleContainer(styleRule);

			} else if ((selectedObject instanceof IDOMElement)
					|| (selectedObject instanceof IDOMAttr)) {

				Element selectedElement = null;

				if (selectedObject instanceof Attr)
					selectedElement = ((Attr) selectedObject).getOwnerElement();
				else
					selectedElement = (Element) selectedObject;

				if (isSuitableElement(selectedElement)) {

					container = new StyleAttribyteContainer(selectedElement);

				}
			} else if (selectedObject instanceof Text) {

				Text styleText = (Text) selectedObject;

				Node parentNode = styleText.getParentNode();

				if ((parentNode != null)
						&& STYLE_TAG_NAME.equalsIgnoreCase(parentNode
								.getNodeName())) {

					int offset = getRelationalOffset(styleText,
							((ITextSelection) selection).getOffset());

					CSSStyleSheet sheet = getSheet(parentNode);

					ICSSNode node = getNode(sheet, offset);

					CSSStyleRule styleRule = getStyleRule(node);

					if (styleRule != null) {
						container = new CSSStyleRuleContainer(styleRule);
					}
				}
			}
		}
		return container;
	}

	/**
	 * 
	 * @param styleContainer
	 * @return
	 */
	private CSSStyleSheet getSheet(Node styleContainer) {

		if (styleContainer instanceof INodeNotifier) {

			INodeNotifier notifier = (INodeNotifier) styleContainer;

			IStyleSheetAdapter adapter = (IStyleSheetAdapter) notifier
					.getAdapterFor(IStyleSheetAdapter.class);

			if (adapter != null) {

				return (CSSStyleSheet) adapter.getSheet();
			}

		}
		return null;
	}

	/**
	 * 
	 * @param sheet
	 * @param offset
	 * @return
	 */
	private ICSSNode getNode(CSSStyleSheet sheet, int offset) {

		ICSSModel model = ((ICSSDocument) sheet).getModel();

		if (model != null)

			return (ICSSNode) model.getIndexedRegion(offset);

		return null;

	}

	/**
	 * 
	 * @param element
	 * @return
	 */
	private boolean isSuitableElement(Element element) {

		if (element instanceof ElementCSSInlineStyle
				&& isAttributeAvailable(element, STYLE_TAG_NAME)) {
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	private CSSStyleRule getStyleRule(ICSSNode node) {

		while (node != null) {

			if (node instanceof CSSStyleRule)
				return (CSSStyleRule) node;

			node = node.getParentNode();
		}

		return null;
	}

	/**
	 * 
	 * @param selection
	 * @param styleText
	 * @return
	 */
	private int getRelationalOffset(Node basicNode, int absoluteOffset) {

		return absoluteOffset - ((IndexedRegion) basicNode).getStartOffset();
	}

	
	/**
	 * @param element
	 * @param attrName
	 * @return
	 */
	private static boolean isAttributeAvailable(Element element, String attrName) {
		ModelQuery modelQuery = ModelQueryUtil.getModelQuery(element
				.getOwnerDocument());
		if (modelQuery != null) {
			CMElementDeclaration decl = modelQuery
					.getCMElementDeclaration(element);
			if (decl != null) {
				CMNamedNodeMap map = decl.getAttributes();
				if ((CMAttributeDeclaration) map.getNamedItem(attrName) != null) {
					return true;
				}
			}
		}

		return false;
	}
}
