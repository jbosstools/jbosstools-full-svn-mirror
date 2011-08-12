package org.jboss.tools.vpe.editor.template;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.graphics.Point;
import org.jboss.tools.jst.jsp.editor.ITextFormatter;
import org.jboss.tools.jst.jsp.selection.SourceSelection;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeSourceDropInfo;
import org.jboss.tools.vpe.editor.VpeSourceInnerDragInfo;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.NodeData;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementData;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.template.textformating.TextFormatingData;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMText;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VpeTemplateSafeWrapper implements VpeTemplate {

	VpeTemplate delegate;

	public VpeTemplateSafeWrapper(VpeTemplate delegate) {
		super();
		this.delegate = delegate;
	}

	/**
	 * @param templateElement
	 * @param caseSensitive
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#init(org.w3c.dom.Element, boolean)
	 */
	public void init(Element templateElement, boolean caseSensitive) {
		try {
			delegate.init(templateElement, caseSensitive);
		} catch(Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
	}

	/**
	 * @param pageContext
	 * @param sourceNode
	 * @param visualDocument
	 * @return
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#create(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMDocument)
	 */
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {
		return delegate.create(pageContext, sourceNode, visualDocument);
	}

	/**
	 * @param pageContext
	 * @param sourceNode
	 * @param visualDocument
	 * @param data
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#validate(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMDocument, org.jboss.tools.vpe.editor.template.VpeCreationData)
	 */
	public void validate(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument, VpeCreationData data) {
		try {
			delegate.validate(pageContext, sourceNode, visualDocument, data);
		} catch(Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
	}

	/**
	 * @param pageContext
	 * @param sourceDocument
	 * @param sourceNode
	 * @param visualNode
	 * @param data
	 * @param charCode
	 * @param selection
	 * @param formatter
	 * @return
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#nonctrlKeyPressHandler(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Document, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMNode, java.lang.Object, long, org.jboss.tools.jst.jsp.selection.SourceSelection, org.jboss.tools.jst.jsp.editor.ITextFormatter)
	 */
	public boolean nonctrlKeyPressHandler(VpePageContext pageContext,
			Document sourceDocument, Node sourceNode, nsIDOMNode visualNode,
			Object data, long charCode, SourceSelection selection,
			ITextFormatter formatter) {
		try { 
			return delegate.nonctrlKeyPressHandler(pageContext, sourceDocument,
					sourceNode, visualNode, data, charCode, selection, formatter);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return false;
	}

	/**
	 * @param pageContext
	 * @param sourceElement
	 * @param visualDocument
	 * @param visualNode
	 * @param data
	 * @param name
	 * @param value
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#setAttribute(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element, org.mozilla.interfaces.nsIDOMDocument, org.mozilla.interfaces.nsIDOMNode, java.lang.Object, java.lang.String, java.lang.String)
	 */
	public void setAttribute(VpePageContext pageContext, Element sourceElement,
			nsIDOMDocument visualDocument, nsIDOMNode visualNode, Object data,
			String name, String value) {
		try {
			delegate.setAttribute(pageContext, sourceElement, visualDocument,
				visualNode, data, name, value);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
	}

	/**
	 * @param pageContext
	 * @param sourceElement
	 * @param visualDocument
	 * @param visualNode
	 * @param data
	 * @param name
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#removeAttribute(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element, org.mozilla.interfaces.nsIDOMDocument, org.mozilla.interfaces.nsIDOMNode, java.lang.Object, java.lang.String)
	 */
	public void removeAttribute(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMNode visualNode, Object data, String name) {
		try {
			delegate.removeAttribute(pageContext, sourceElement, visualDocument,
				visualNode, data, name);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}

	}

	/**
	 * @param pageContext
	 * @param sourceNode
	 * @param visualNode
	 * @param data
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#beforeRemove(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMNode, java.lang.Object)
	 */
	public void beforeRemove(VpePageContext pageContext, Node sourceNode,
			nsIDOMNode visualNode, Object data) {
		try {
			delegate.beforeRemove(pageContext, sourceNode, visualNode, data);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
	}

	/**
	 * @param pageContext
	 * @param sourceNode
	 * @param visualNode
	 * @param data
	 * @return
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#getNodeForUpdate(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMNode, java.lang.Object)
	 */
	public Node getNodeForUpdate(VpePageContext pageContext, Node sourceNode,
			nsIDOMNode visualNode, Object data) {
		try {
			return delegate.getNodeForUpdate(pageContext, sourceNode, visualNode,
				data);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return null;
	}

	/**
	 * @param pageContext
	 * @param sourceElement
	 * @param visualDocument
	 * @param visualElement
	 * @param data
	 * @param constrains
	 * @param top
	 * @param left
	 * @param width
	 * @param height
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#resize(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element, org.mozilla.interfaces.nsIDOMDocument, org.mozilla.interfaces.nsIDOMElement, java.lang.Object, int, int, int, int, int)
	 */
	public void resize(VpePageContext pageContext, Element sourceElement,
			nsIDOMDocument visualDocument, nsIDOMElement visualElement,
			Object data, int constrains, int top, int left, int width,
			int height) {
		try {
			delegate.resize(pageContext, sourceElement, visualDocument,
				visualElement, data, constrains, top, left, width, height);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
	}

	/**
	 * @param pageContext
	 * @param sourceElement
	 * @return
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#canInnerDrag(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element)
	 */
	public boolean canInnerDrag(VpePageContext pageContext, Element sourceElement) {
		try {
			return delegate.canInnerDrag(pageContext, sourceElement);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return false;
	}

	/**
	 * @param pageContext
	 * @param container
	 * @param sourceDragNode
	 * @return
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#canInnerDrop(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.w3c.dom.Node)
	 */
	public boolean canInnerDrop(VpePageContext pageContext, Node container,
			Node sourceDragNode) {
		try {
			return delegate.canInnerDrop(pageContext, container, sourceDragNode);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return false;
	}

	/**
	 * @param pageContext
	 * @param dragInfo
	 * @param dropInfo
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#innerDrop(org.jboss.tools.vpe.editor.context.VpePageContext, org.jboss.tools.vpe.editor.VpeSourceInnerDragInfo, org.jboss.tools.vpe.editor.VpeSourceDropInfo)
	 */
	public void innerDrop(VpePageContext pageContext,
			VpeSourceInnerDragInfo dragInfo, VpeSourceDropInfo dropInfo) {
		try {
			delegate.innerDrop(pageContext, dragInfo, dropInfo);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
	}

	public static final VpeTagDescription EMPTY_TAG_DESCRIPTION = new VpeTagDescription();
	
	/**
	 * @param pageContext
	 * @param sourceElement
	 * @param visualDocument
	 * @param visualElement
	 * @param data
	 * @return
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#getTagDescription(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element, org.mozilla.interfaces.nsIDOMDocument, org.mozilla.interfaces.nsIDOMElement, java.lang.Object)
	 */
	public VpeTagDescription getTagDescription(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMElement visualElement, Object data) {
		try {
			return delegate.getTagDescription(pageContext, sourceElement,
				visualDocument, visualElement, data);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return EMPTY_TAG_DESCRIPTION;
	}

	/**
	 * @param pageContext
	 * @param sourceElement
	 * @param visualDocument
	 * @param visualNde
	 * @param data
	 * @param name
	 * @param value
	 * @return
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#recreateAtAttrChange(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element, org.mozilla.interfaces.nsIDOMDocument, org.mozilla.interfaces.nsIDOMElement, java.lang.Object, java.lang.String, java.lang.String)
	 */
	public boolean recreateAtAttrChange(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMElement visualNde, Object data, String name, String value) {
		try {
			return delegate.recreateAtAttrChange(pageContext, sourceElement,
				visualDocument, visualNde, data, name, value);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return false;
	}

	/**
	 * @return
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#hasChildren()
	 */
	public boolean hasChildren() {
		try {
			return delegate.hasChildren();
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return false;
	}

	/**
	 * @return
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#isCaseSensitive()
	 */
	public boolean isCaseSensitive() {
		try {
			return delegate.isCaseSensitive();
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return false;
	}

	/**
	 * @return
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#hasImaginaryBorder()
	 */
	public boolean hasImaginaryBorder() {
		try {
			return delegate.hasImaginaryBorder();
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return false;
	}

	/**
	 * @return
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#getTextFormattingData()
	 */
	public TextFormatingData getTextFormattingData() {
		try {
			return delegate.getTextFormattingData();
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return null;
	}

	/**
	 * @return
	 * @deprecated
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#getOutputAttributeNames()
	 */
	public String[] getOutputAttributeNames() {
		try {
			return delegate.getOutputAttributeNames();
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return new String[0];
	}

	/**
	 * @param pageContext
	 * @param sourceElement
	 * @param data
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#refreshBundleValues(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element, java.lang.Object)
	 */
	public void refreshBundleValues(VpePageContext pageContext,
			Element sourceElement, Object data) {
		try {
			delegate.refreshBundleValues(pageContext, sourceElement, data);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
	}

	/**
	 * @param pageContext
	 * @param sourceElement
	 * @param data
	 * @deprecated
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#setSourceAttributeValue(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element, java.lang.Object)
	 */
	public void setSourceAttributeValue(VpePageContext pageContext,
			Element sourceElement, Object data) {
		try {
			delegate.setSourceAttributeValue(pageContext, sourceElement, data);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
	}

	/**
	 * @param pageContext
	 * @param sourceElement
	 * @param data
	 * @return
	 * @deprecated
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#getOutputTextNode(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element, java.lang.Object)
	 */
	public nsIDOMText getOutputTextNode(VpePageContext pageContext,
			Element sourceElement, Object data) {
		try {
			return delegate.getOutputTextNode(pageContext, sourceElement, data);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return null;
	}

	/**
	 * @param pageContext
	 * @param sourceElement
	 * @param offset
	 * @param length
	 * @param data
	 * @deprecated
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#setSourceAttributeSelection(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element, int, int, java.lang.Object)
	 */
	public void setSourceAttributeSelection(VpePageContext pageContext,
			Element sourceElement, int offset, int length, Object data) {
		try {
			delegate.setSourceAttributeSelection(pageContext, sourceElement,
				offset, length, data);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
	}

	/**
	 * @return
	 * @deprecated
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#isOutputAttributes()
	 */
	public boolean isOutputAttributes() {
		try {
			return delegate.isOutputAttributes();
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return false;
	}

	/**
	 * @return
	 * @deprecated
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#getType()
	 */
	public int getType() {
		try {
			return delegate.getType();
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return VpeHtmlTemplate.TYPE_ANY;
	}

	/**
	 * @return
	 * @deprecated
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#getAnyData()
	 */
	public VpeAnyData getAnyData() {
		try {
			return delegate.getAnyData();
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return null;
	}

	/**
	 * @param pageContext
	 * @param sourceContainer
	 * @param visualContainer
	 * @param visualDocument
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#setPseudoContent(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMNode, org.mozilla.interfaces.nsIDOMDocument)
	 */
	public void setPseudoContent(VpePageContext pageContext,
			Node sourceContainer, nsIDOMNode visualContainer,
			nsIDOMDocument visualDocument) {
		try {
			delegate.setPseudoContent(pageContext, sourceContainer,
				visualContainer, visualDocument);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
	}

	/**
	 * @return
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#containsText()
	 */
	public boolean containsText() {
		try {
			return delegate.containsText();
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return false;
	}

	/**
	 * @return
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#canModify()
	 */
	public boolean canModify() {
		try {
			return delegate.canModify();
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return false;
	}

	/**
	 * @param modify
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#setModify(boolean)
	 */
	public void setModify(boolean modify) {
		try {
			delegate.setModify(modify);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
	}

	/**
	 * @param node
	 * @param elementData
	 * @param domMapping
	 * @return
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#getNodeData(org.mozilla.interfaces.nsIDOMNode, org.jboss.tools.vpe.editor.mapping.VpeElementData, org.jboss.tools.vpe.editor.mapping.VpeDomMapping)
	 */
	public NodeData getNodeData(nsIDOMNode node, VpeElementData elementData,
			VpeDomMapping domMapping) {
		try {
			return delegate.getNodeData(node, elementData, domMapping);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return null;
	}

	/**
	 * @param elementMapping
	 * @param selectionRange
	 * @param domMapping
	 * @return
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#getVisualNodeBySourcePosition(org.jboss.tools.vpe.editor.mapping.VpeElementMapping, org.eclipse.swt.graphics.Point, org.jboss.tools.vpe.editor.mapping.VpeDomMapping)
	 */
	public nsIDOMNode getVisualNodeBySourcePosition(
			VpeElementMapping elementMapping, Point selectionRange,
			VpeDomMapping domMapping) {
		try {
			return delegate.getVisualNodeBySourcePosition(elementMapping,
				selectionRange, domMapping);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return null;
	}

	/**
	 * @return
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#isInvisible()
	 */
	public boolean isInvisible() {
		try {
			return delegate.isInvisible();
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return false;
	}

	/**
	 * @param pageContext
	 * @param sourceNode
	 * @param domNode
	 * @return
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#getSourceRegionForOpenOn(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node, org.mozilla.interfaces.nsIDOMNode)
	 */
	public IRegion getSourceRegionForOpenOn(VpePageContext pageContext,
			Node sourceNode, nsIDOMNode domNode) {
		try {
			return delegate.getSourceRegionForOpenOn(pageContext, sourceNode,
				domNode);
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logError(ex);
		}
		return null;
	};
    
	public double getPriority() {
		return delegate.getPriority();
	}
	public void setPriority(double priority) {
		delegate.setPriority(priority);
	}
}
