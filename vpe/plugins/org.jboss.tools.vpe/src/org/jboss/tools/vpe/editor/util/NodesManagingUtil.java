package org.jboss.tools.vpe.editor.util;

import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author S.Dzmitrovich
 * 
 */
public class NodesManagingUtil {

	/**
	 * name of "view" tag
	 */
	private static final String VIEW_TAGNAME = "view"; //$NON-NLS-1$

	/**
	 * name of "locale" attribute
	 */
	private static final String LOCALE_ATTRNAME = "locale"; //$NON-NLS-1$

	/**
	 * 
	 * @param domMapping
	 * @param node
	 * @return
	 */
	public static VpeNodeMapping getNodeMapping(VpeDomMapping domMapping,
			Node node) {

		return domMapping.getNearNodeMappingAtSourceNode(node);

	}

	/**
	 * 
	 * @param domMapping
	 * @param node
	 * @return
	 */
	public static VpeNodeMapping getNodeMapping(VpeDomMapping domMapping,
			nsIDOMNode node) {

		return domMapping.getNearNodeMappingAtVisualNode(node);

	}

	/**
	 * 
	 * @param sourceEditor
	 * @return
	 */
	public static IStructuredModel getStructuredModel(
			StructuredTextEditor sourceEditor) {

		IDocument document = sourceEditor.getTextViewer().getDocument();

		return StructuredModelManager.getModelManager()
				.getExistingModelForEdit(document);
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	public static int getNodeLength(Node node) {

		if (node instanceof IDOMAttr) {
			return ((IDOMAttr) node).getValueSource().length();
		} else if (node instanceof IndexedRegion) {
			return ((IndexedRegion) node).getEndOffset()
					- ((IndexedRegion) node).getStartOffset();
		}
		return 0;
	}

	/**
	 * get start offset of node
	 * 
	 * @param node
	 * @return
	 */
	public static int getStartOffsetNode(Node node) {

		if (node instanceof IDOMAttr) {
			return ((IDOMAttr) node).getValueRegionStartOffset() + 1;
		} else if (node instanceof IndexedRegion) {
			return ((IndexedRegion) node).getStartOffset();
		}
		return 0;
	}

	/**
	 * get end offset of node
	 * 
	 * @param node
	 * @return
	 */
	public static int getEndOffsetNode(Node node) {

		if (node instanceof IndexedRegion) {
			return ((IndexedRegion) node).getEndOffset();
		}
		return 0;
	}

	/**
	 * 
	 * @param pageContext
	 * @param sourceElement
	 * @return
	 */
	public static String getPageLocale(VpePageContext pageContext,
			Node sourceNode) {

		while (sourceNode != null) {

			if (VIEW_TAGNAME.equals(sourceNode.getLocalName())) {
				break;
			}
			sourceNode = sourceNode.getParentNode();
		}

		if ((sourceNode == null) || !(sourceNode instanceof Element)
				|| !(((Element) sourceNode).hasAttribute(LOCALE_ATTRNAME)))
			return null;

		String locale = ((Element) sourceNode).getAttribute(LOCALE_ATTRNAME);

		return locale;

	}

}
