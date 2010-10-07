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
package org.jboss.tools.vpe.dnd;

import static org.jboss.tools.vpe.xulrunner.util.XPCOM.queryInterface;

import java.text.MessageFormat;
import java.util.EnumSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.eclipse.wst.xml.core.internal.document.NodeContainer;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.XFileObject;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.ui.dnd.ModelTransfer;
import org.jboss.tools.common.model.ui.editors.dnd.DropUtils;
import org.jboss.tools.common.model.ui.editors.dnd.context.IDNDTextEditor;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.tld.model.TLDUtil;
import org.jboss.tools.vpe.VpeDebug;
import org.jboss.tools.vpe.dnd.DndUtil.DragTransferData;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.VpeSourceDropInfo;
import org.jboss.tools.vpe.editor.VpeSourceInnerDragInfo;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.mozilla.listener.MozillaDndListener;
import org.jboss.tools.vpe.editor.mozilla.listener.MozillaSelectionListener;
import org.jboss.tools.vpe.editor.util.SourceDomUtil;
import org.jboss.tools.vpe.editor.util.VisualDomUtil;
import org.jboss.tools.vpe.editor.util.VpeDndUtil;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.xulrunner.editor.IVpeSelectionListener;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerHint;
import org.jboss.tools.vpe.xulrunner.util.XPCOM;
import org.mozilla.interfaces.nsIComponentManager;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNSUIEvent;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMRange;
import org.mozilla.interfaces.nsIDOMText;
import org.mozilla.interfaces.nsIDragService;
import org.mozilla.interfaces.nsIFile;
import org.mozilla.interfaces.nsISelection;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsISupportsArray;
import org.mozilla.interfaces.nsISupportsCString;
import org.mozilla.interfaces.nsISupportsString;
import org.mozilla.interfaces.nsITransferable;
import org.mozilla.xpcom.Mozilla;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * Class responsible for Drag&Drop functionality
 * 
 * @author Max Areshkau
 * @author Yahor Radtsevich (yradtsevich)
 */
// NOTE: the code has been cleaned after SVN revision 21574, many methods
// have been removed. To find the old code refer to older revisions.
public class VpeDnD implements MozillaDndListener, MozillaSelectionListener, IVpeSelectionListener {
	private static final String TAG_TAGLIB = "taglib"; //$NON-NLS-1$

	private nsIServiceManager serviceManager;
	private nsIComponentManager componentManager;
	private nsIDragService dragService;
	private VpeController vpeController;
	private DraggablePattern draggablePattern;
	private DropableArea dropableArea;
	private XulRunnerHint dropHint;
	
	/** Offset of dropHint related to mouse cursor*/
	private static final Point DROP_HINT_OFFSET = new Point(20, -10);

	/** The Constant FLAVORS. */
	private static final String[] FLAVORS =  { 
		ModelTransfer.MODEL,
		DndUtil.kUnicodeMime,
		DndUtil.kHTMLMime,
		DndUtil.kAOLMailMime,
		DndUtil.kPNGImageMime,
		DndUtil.kJPEGImageMime,
		DndUtil.kGIFImageMime,
		DndUtil.kFileMime,
		DndUtil.kURLMime,
		DndUtil.kURLDataMime,
		DndUtil.kURLDescriptionMime,
		DndUtil.kNativeImageMime,
		DndUtil.kNativeHTMLMime,
		DndUtil.kFilePromiseURLMime,
		DndUtil.kFilePromiseMime,
		DndUtil.kFilePromiseDirectoryMime,
		DndUtil.kTextMime
	};
	

	public VpeDnD(VpeController vpeController, MozillaEditor mozillaEditor) {
		this.vpeController = vpeController;
		draggablePattern = new DraggablePattern(mozillaEditor);
	}

	public void dragStart(nsIDOMEvent domEvent) {
		nsIDOMElement selectedElement = getSelectedElement();
		// start drag sessionvpe-element
		if (isTextSelected(getVisualSelection()) || isDraggable(selectedElement)) {
			Point pageCoords = getPageCoords(domEvent);
			draggablePattern.startSession(pageCoords.x, pageCoords.y);
			startDragSession(selectedElement);
			domEvent.stopPropagation();
			domEvent.preventDefault();
		}
	}

	private nsIDOMElement getSelectedElement() {
		return vpeController.getXulRunnerEditor().getLastSelectedElement();
	}
	
	/**
	 * Called when drag over event occurs
	 * @param event
	 */
	public void dragOver(nsIDOMEvent event) {
		final nsIDOMMouseEvent mouseEvent = queryInterface(event, nsIDOMMouseEvent.class);
		
		final XulRunnerEditor editor = vpeController.getXulRunnerEditor();
		new ScrollingSupport(editor).scroll(mouseEvent);
		
		final DropResolver dropResolver;
		if (isInnerDragSession()) {
			if (isTextSelected(getVisualSelection())) {
				dropResolver = getDropResolverForNode(getSourceNode(getVisualSelection().getFocusNode()));
			} else {
				dropResolver = getDropResolverForInternalDrop();
			}

			Point mousePosition = getPageCoords(event);
			draggablePattern.moveTo(mousePosition.x, mousePosition.y);
		} else {
			dropResolver = getDropResolverForExternalDrop();
		}
		highlightDropTargets(dropResolver, mouseEvent);
		refreshCanDrop(event);
		vpeController.onRefresh();
	}
	
	/**
	 * Drop Event handler
	 * @param domEvent
	 * @param vpeController
	 */
	public void dragDrop(nsIDOMEvent domEvent) {
		if(isInnerDragSession()) {
			// in this case it's is an internal drag
			draggablePattern.closeSession();
			innerDrop(queryInterface(domEvent, nsIDOMMouseEvent.class));
		} else {
			//in this case it's is  external drag
			externalDrop(queryInterface(domEvent, nsIDOMMouseEvent.class));
		}
		disposeDropableArea();
		disposeDropHint();
		vpeController.onRefresh();
	}

	public void dragExit(nsIDOMEvent domEvent) {
		nsIDOMNode eventTargetNode = queryInterface(domEvent.getTarget(), nsIDOMNode.class);
		
		if (dropableArea != null) {
			nsIDOMNode dropTargetNode = dropableArea.getNode();
			
			boolean targetNodeIsTemporary = false;
			if (eventTargetNode.getNodeType() == nsIDOMNode.ELEMENT_NODE) {
				nsIDOMElement eventTargetElement 
						= queryInterface(eventTargetNode, nsIDOMElement.class);
				targetNodeIsTemporary = DndUtil.isTemporaryDndElement(eventTargetElement);
			}
			boolean eventTargetIsAscedantOfDropTarget = VisualDomUtil.isAscendant(eventTargetNode, dropTargetNode);
			
			// ignore events which are fired by the reason
			// of drawing the dropable area
			if (targetNodeIsTemporary || eventTargetIsAscedantOfDropTarget) {
				disposeDropableArea();
				disposeDropHint();
			}
		}
	}
	
	public void dragEnd(nsIDOMEvent domEvent) {
		disposeDropableArea();
		disposeDropHint();
		draggablePattern.closeSession();
	}

	public void selectionChanged() {
		refreshDraggablePattern();
	}

	private void refreshDraggablePattern() {
		nsISelection selection = getVisualSelection();
		if (isTextSelected(selection)) {
			nsIDOMRange range = selection.getRangeAt(0);
			nsIDOMText textContainer = queryInterface(
					range.getStartContainer(), nsIDOMText.class);
			
			draggablePattern.showDragIcon(new DraggableTextSelection(
					textContainer, range.getStartOffset(), range.getEndOffset()));
		} else {
			nsIDOMElement selectedElement = getSelectedElement();

			if (isDraggable(selectedElement)) {
				draggablePattern.showDragIcon(new DraggableElement(selectedElement));
			} else {
				draggablePattern.hideDragIcon();
			}
		}
	}
	
	private nsISelection getVisualSelection() {
		return vpeController.getXulRunnerEditor().getWebBrowser()
				.getContentDOMWindow().getSelection();
	}

	private boolean isTextSelected(nsISelection selection) {
		if (selection.getRangeCount() == 0) {
			// nothing selected
			return false;
		}

		nsIDOMRange range = selection.getRangeAt(0);
		nsIDOMNode container = range.getStartContainer();
		if (!container.equals(range.getEndContainer())) {
			// more than one node selected
			return false;
		}
		if (container.getNodeType() != nsIDOMNode.TEXT_NODE) {
			// not text node is selected
			return false;
		}
		if (range.getStartOffset() == range.getEndOffset()) {
			// no text selected
			return false;
		}
		
		return true;
	}

	public boolean isDragIconClicked(nsIDOMMouseEvent mouseEvent) {
		return draggablePattern.isDragIconClicked(mouseEvent);
	}

	private DropResolver getDropResolverForExternalDrop() {
		if (getDragService().getCurrentSession()
				.isDataFlavorSupported(ModelTransfer.MODEL)) {
			XModelObject object = PreferenceModelUtilities.getPreferenceModel()
					.getModelBuffer().source();
			if (object.getFileType() == XFileObject.FILE
					&& !TLDUtil.isTaglib(object)) {
				final IFile f = (IFile) EclipseResourceUtil.getResource(object);
				return getSimpleDropResolver(f != null);
			} else {
				String tagname = vpeController.getTagName(object);
				if (tagname.indexOf(TAG_TAGLIB) >= 0) {
					tagname = TAG_TAGLIB;
				}
				Node dropContainer = ((Document) vpeController.getModel()
						.getAdapter(Document.class)).createElement(tagname);
	
				return getDropResolverForNode(dropContainer);
			}
		} else {
			return getSimpleDropResolver(true);
		}
	}

	private DropResolver getDropResolverForInternalDrop() {
		Node node = DndUtil.getNodeFromDragSession(vpeController.getPageContext());
		if (node != null) {
				return getDropResolverForNode(node);
		} else {
			return getSimpleDropResolver(false);
		}
	}

	private DropResolver getSimpleDropResolver(final boolean canDrop) {
		return new DropResolver() {
			public boolean canDrop(Node node) {
				return canDrop;
			}
		};
	}

	private DropResolver getDropResolverForNode(final Node draggedNode) {
		return new DropResolver() {
			public boolean canDrop(Node container) {
				VpeNodeMapping nodeMapping = vpeController.getDomMapping()
						.getNodeMapping(container);
	
				boolean canDrop = false;
				if (nodeMapping != null && nodeMapping instanceof VpeElementMapping) {
					canDrop = ((VpeElementMapping) nodeMapping).getTemplate()
							.canInnerDrop(vpeController.getPageContext(),
									container, draggedNode);
				}
		
				return canDrop;
			}
		};
	}

	private void highlightDropTargets(DropResolver dropResolver, nsIDOMMouseEvent event) {
		nsIDOMDocument document = vpeController.getXulRunnerEditor().getDOMDocument();

		Point clientCoords = getClientCoords(event);

		nsIDOMNode originalVisualNode = DndUtil.getElementFromPoint(document,
				clientCoords.x, clientCoords.y);
		if (originalVisualNode == null) {
			return;
		}

		Node originalSourceNode = getSourceNode(originalVisualNode);
		
		if (originalSourceNode != null
				&& originalSourceNode.getNodeType() == Node.TEXT_NODE) {
			originalSourceNode = originalSourceNode.getParentNode();
		}

		final Node highlightedNode;
		final EnumSet<DropTarget> dropTargets;
		if (dropResolver.canDrop(originalSourceNode)) {
			highlightedNode = originalSourceNode;

			Node originalSourceNodeParent = originalSourceNode.getParentNode();
			if (originalSourceNodeParent != null
					&& dropResolver.canDrop(originalSourceNodeParent)) {
				dropTargets = EnumSet.of(DropTarget.BEFORE, DropTarget.AFTER,
						DropTarget.BEGIN, DropTarget.END);
			} else {
				dropTargets = EnumSet.of(DropTarget.BEGIN, DropTarget.END);
			}
		} else {
			Node sourceNode = null;
			Node sourceNodeParent = originalSourceNode;
			boolean nodeFound = false;
			while (sourceNodeParent != null && !nodeFound) {
				sourceNode = sourceNodeParent;
				sourceNodeParent = sourceNode.getParentNode();
				nodeFound = dropResolver.canDrop(sourceNodeParent);
			}
			
			if (nodeFound) {
				highlightedNode = sourceNode;
				dropTargets = EnumSet.of(DropTarget.BEFORE, DropTarget.AFTER);
			} else {
				highlightedNode = null;
				dropTargets = EnumSet.noneOf(DropTarget.class);
			}
		}

		Point mouseCoords = getPageCoords(event);
		
		if (highlightedNode != null) {
			if (dropableArea == null) {
				dropableArea = new DropableArea(document);
			}

			dropableArea.setDropTargets(dropTargets);
			dropableArea.setNode(
					vpeController.getDomMapping().getNearVisualNode(highlightedNode));
			dropableArea.setHighlightedDropTarget(mouseCoords.x, mouseCoords.y);
			dropableArea.setVisible(true);
			dropableArea.redraw();
		} else {
			disposeDropableArea();
			disposeDropHint();
		}
		
		if (highlightedNode != null && dropableArea.getHighlightedDropTarget() != null) {
			if (dropHint == null) {
				dropHint = new XulRunnerHint(document);
			}
			String dropHintValue = null;
			switch (dropableArea.getHighlightedDropTarget()) {
			case BEFORE:
				dropHintValue = MessageFormat.format(
						VpeUIMessages.VpeDnD_PLACE_BEFORE_INSIDE,
						highlightedNode.getNodeName(),
						highlightedNode.getParentNode().getNodeName());
				break;
			case AFTER:
				dropHintValue = MessageFormat.format(
						VpeUIMessages.VpeDnD_PLACE_AFTER_INSIDE,
						highlightedNode.getNodeName(),
						highlightedNode.getParentNode().getNodeName());
				break;
			case BEGIN:
				dropHintValue = MessageFormat.format(
						VpeUIMessages.VpeDnD_PLACE_AT_THE_BEGINNING_OF,
						highlightedNode.getNodeName());
				break;
			case END:
				dropHintValue = MessageFormat.format(
						VpeUIMessages.VpeDnD_PLACE_AT_THE_END_OF,
						highlightedNode.getNodeName());
				break;
			}

			dropHint.setHint(dropHintValue);
			dropHint.setPosition(new Point(mouseCoords.x + DROP_HINT_OFFSET.x,
					mouseCoords.y + DROP_HINT_OFFSET.y));
			dropHint.redraw();
		} else {
			disposeDropHint();
		}
	}

	private Point getClientCoords(nsIDOMEvent event) {
		final nsIDOMMouseEvent mouseEvent =
			queryInterface(event, nsIDOMMouseEvent.class);
		return new Point(mouseEvent.getClientX(), mouseEvent.getClientY());
	}
	
	private void disposeDropableArea() {
		if (dropableArea != null) {
			dropableArea.dispose();
			dropableArea = null;
		}
	}
	
	private void disposeDropHint() {
		if (dropHint != null) {
			dropHint.dispose();
			dropHint = null;
		}
	}

	/**
	 * Starts drag session
	 * @param dragetElement
	 */
	private void startDragSession(nsIDOMElement element) {
		nsISupportsArray transArray = (nsISupportsArray) getComponentManager()
				.createInstanceByContractID(XPCOM.NS_SUPPORTSARRAY_CONTRACTID, null,
						nsISupportsArray.NS_ISUPPORTSARRAY_IID);
		transArray.appendElement(createTransferable(getSourceNode(element)));
		getDragService().invokeDragSession(element, transArray, null,
				nsIDragService.DRAGDROP_ACTION_MOVE
						| nsIDragService.DRAGDROP_ACTION_COPY
						| nsIDragService.DRAGDROP_ACTION_LINK);
	}

	/**
	 * Creates transferable object to start drag session
	 * 
	 * @return transferable object
	 */
	private nsITransferable createTransferable(Node node) {
		
		nsITransferable iTransferable = (nsITransferable) getComponentManager()
						.createInstanceByContractID(XPCOM.NS_TRANSFERABLE_CONTRACTID, null,
								nsITransferable.NS_ITRANSFERABLE_IID);
		
		String nodeSource = ((NodeContainer)node).getSource();
		nsISupportsString nodeSourceData = createNsISupportsString(nodeSource);
		int nodeSourceDataLength = nodeSource.length() * 2;
		iTransferable.setTransferData(ModelTransfer.MODEL, nodeSourceData, nodeSourceDataLength);
		iTransferable.setTransferData("text/html", nodeSourceData, nodeSourceDataLength); //$NON-NLS-1$
		iTransferable.setTransferData("text/unicode", nodeSourceData, nodeSourceDataLength); //$NON-NLS-1$
		
		String xPath = SourceDomUtil.getXPath(node);
		nsISupportsString xPathData = createNsISupportsString(xPath);
		iTransferable.setTransferData(DndUtil.VPE_XPATH_FLAVOR, xPathData, xPath.length() * 2);
		
		return iTransferable;
	}

	private nsISupportsString createNsISupportsString(String data) {
		nsISupportsString xulString = (nsISupportsString) getComponentManager()
				.createInstanceByContractID(XPCOM.NS_SUPPORTSSTRING_CONTRACTID, null,
				nsISupportsString.NS_ISUPPORTSSTRING_IID);
		xulString.setData(data);
		
		return xulString;
	}

	private void refreshCanDrop(nsIDOMEvent event) {

		nsIDOMMouseEvent mouseEvent = queryInterface(event, nsIDOMMouseEvent.class);
		getDragService().getCurrentSession().setCanDrop(dropableArea != null
				&& dropableArea.getHighlightedDropTarget() != null);
		mouseEvent.preventDefault();
		mouseEvent.stopPropagation();


	}

	private void dropAny(final String flavor, final String data) {
		VpeSourceDropInfo dropInfo = getDropInfo();
		Point range = getSourceSelectionRange(dropInfo.getContainer(), dropInfo.getOffset());

		if (dropInfo.getContainer() != null && data != null) {
			StructuredTextEditor sourceEditor = vpeController.getSourceEditor();
			if (flavor == null || flavor.length() == 0 
					|| !(sourceEditor instanceof IDNDTextEditor)) {
				return;
			}
			
			sourceEditor.setHighlightRange(range.x, 0, true);
			((IDNDTextEditor) sourceEditor).runDropCommand(flavor, data);
		}
	}
	
	private boolean isInnerDragSession() {
		return getDragService().getCurrentSession().getSourceDocument() != null;
	}
	
	private boolean isDraggable(nsIDOMElement element) {
		vpeController.onHideTooltip();
	
		if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
			System.out.print("<<<<<< canInnerDrag"); //$NON-NLS-1$
		}

		boolean canDrag = false;

		if (element != null) {
			if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
				System.out.print(" dragNode: " + element.getNodeName() + "(" + element + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			
			canDrag = canInnerDrag(element);
			if (canDrag) {
				Node sourceNode = getSourceNode(element);
				if (sourceNode == null) {
					canDrag = false;
				}
			}
		}
		if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
			System.out.println("  canDrag: " + canDrag); //$NON-NLS-1$
		}
		return canDrag;
	}
	
	private boolean canInnerDrag(nsIDOMElement visualDragElement) {
		VpeNodeMapping domMapping = vpeController.getDomMapping()
				.getNodeMapping(visualDragElement);
		if (domMapping instanceof VpeElementMapping) {
			VpeElementMapping elementMapping = (VpeElementMapping) domMapping;
			if (elementMapping.getSourceNode() instanceof Element) {
				return elementMapping.getTemplate().canInnerDrag(
						vpeController.getPageContext(),
						(Element) elementMapping.getSourceNode(),
						vpeController.getXulRunnerEditor().getDOMDocument(),
						visualDragElement, elementMapping.getData());
			}
		}
		return false;
	}
	
	private Node getSourceNode(nsIDOMNode visualNode) {
		return vpeController.getDomMapping().getNearSourceNode(visualNode);
	}

	private void innerDrop(nsIDOMMouseEvent event) {
		vpeController.onHideTooltip();
	
		if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
			System.out.print("<<<<<< innerDrop"); //$NON-NLS-1$
		}

		if (isTextSelected(getVisualSelection())) {
			// it is inner Drag&Drop of text
			StyledText textWidget = vpeController.getSourceEditor()
					.getTextViewer().getTextWidget();
			String text = textWidget.getSelectionText();
			
			Point selectionRange = textWidget.getSelectionRange();
			textWidget.replaceTextRange(selectionRange.x, selectionRange.y, ""); //$NON-NLS-1$
			
			dropAny(DndUtil.kUnicodeMime, text);
		} else {
			Node node = DndUtil.getNodeFromDragSession(vpeController.getPageContext());
			if (node != null) {
				VpeSourceDropInfo sourceDropInfo = getDropInfo();
				if (sourceDropInfo.getContainer() != null) {
					if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
						System.out
								.print("  container: " + sourceDropInfo.getContainer().getNodeName() + //$NON-NLS-1$
										"(" + sourceDropInfo.getContainer() //$NON-NLS-1$
										+ ")" + //$NON-NLS-1$
										"  offset: " //$NON-NLS-1$
										+ sourceDropInfo.getOffset());
					}
					
					if (sourceDropInfo.canDrop()) {
						VpeDnDHelper dropper = new VpeDnDHelper();
						dropper.setDndData(false, true);
						dropper.drop(vpeController.getPageContext(),
								new VpeSourceInnerDragInfo(node, 0, 0), sourceDropInfo);
	
						// select dropped node, JBIDE-6239
						setSelectedNode(node);
					}
				}
			}
		}
		if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
			System.out.println();
		}
	}

	@SuppressWarnings("restriction")
	private void setSelectedNode(final Node node) {
		IndexedRegion sourceNodeBounds
				= ((IndexedRegion)node);
		
		vpeController.getSourceEditor().getTextViewer()
				.getTextWidget().setSelection(
						sourceNodeBounds.getStartOffset(),
						sourceNodeBounds.getEndOffset());
	}

	private void externalDrop(nsIDOMMouseEvent mouseEvent) {
		vpeController.onHideTooltip();
	
		final DragTransferData dragTransferData = DndUtil.getDragTransferData(FLAVORS);
		final nsISupports aValue = dragTransferData.getValue();
		String data = ""; //$NON-NLS-1$
		String aFlavor = ""; //$NON-NLS-1$
		if (VpeDndUtil.isNsIFileInstance(aValue)) {
			nsIFile aFile = queryInterface(aValue, nsIFile.class);
	
			// because it is external, convert the path to URL
			final String path = aFile.getPath();
			data = path != null ? DropUtils.convertPathToUrl(path) : null;
			aFlavor = DndUtil.kFileMime;
	
		} else if (VpeDndUtil.isNsICStringInstance(aValue)) {
			nsISupportsCString aString = 
					queryInterface(aValue, nsISupportsCString.class);
			data = aString.getData();
			aFlavor = DndUtil.kHTMLMime;
		} else if (VpeDndUtil.isNsIStringInstance(aValue)) {
			nsISupportsString aString = queryInterface(aValue, nsISupportsString.class);
			data = aString.getData();
			if (ModelTransfer.MODEL.equals(dragTransferData.getFlavor())) {
				aFlavor = dragTransferData.getFlavor();
			} else {
				aFlavor = DndUtil.kURLMime;
			}
		}

		dropAny(aFlavor, data);
	}

	@SuppressWarnings("restriction")
	private Point getSourceSelectionRange(Node sourceInitNode, int sourceInitOffset) {
		int offset=0;
		int position=0;
		switch (sourceInitNode.getNodeType()) {
		case Node.TEXT_NODE:
			offset = Math.min(sourceInitOffset, sourceInitNode.getNodeValue().length());
			position = vpeController.getSourceBuilder()
					.getPosition(sourceInitNode, offset, false);
			break;
		case Node.ELEMENT_NODE:
		case Node.DOCUMENT_NODE:
			NodeList children = sourceInitNode.getChildNodes();
			int count = children.getLength();
			if (sourceInitOffset < count) {
				// insert before the child with index sourceInitOffset 
				Node sourceNode = children.item(sourceInitOffset);
				position = ((IndexedRegion)sourceNode).getStartOffset();
			} else if (count > 0) {
				// insert after the last child
				Node sourceNode = children.item(count - 1);
				position = ((IndexedRegion)sourceNode).getEndOffset();
			} else { // if (count == 0)
				// insert as a child
				position = ((IndexedRegion)sourceInitNode).getStartOffset();
				if (sourceInitNode instanceof ElementImpl) {
			 		ElementImpl element = (ElementImpl)sourceInitNode;
			 		if (element.isContainer()) {
			 			position  = element.getStartEndOffset();
			 		}
				}
			}
			break;
		}
		return new Point(position, 0);
	}

	private Point getPageCoords(nsIDOMEvent domEvent) {
		nsIDOMNSUIEvent nsuiEvent = queryInterface(domEvent, nsIDOMNSUIEvent.class);
		return new Point(nsuiEvent.getPageX(), nsuiEvent.getPageY());
	}

	/**
	 * @return the componentManager
	 */
	private nsIComponentManager getComponentManager() {
		
		if(componentManager==null) {
			componentManager = Mozilla.getInstance()
					.getComponentManager();
		}
		return componentManager;
	}

	/**
	 * @return the serviceManager
	 */
	private nsIServiceManager getServiceManager() {
		
		if(serviceManager==null) {
			serviceManager = Mozilla.getInstance()
			.getServiceManager();
		}
		return serviceManager;
	}

	/**
	 * @return the dragService
	 */
	private nsIDragService getDragService() {
		
		if(dragService==null) {
			dragService = (nsIDragService) getServiceManager()
			.getServiceByContractID(XPCOM.NS_DRAGSERVICE_CONTRACTID,
					nsIDragService.NS_IDRAGSERVICE_IID);
		}
		return dragService;
	}
	
	private VpeSourceDropInfo getDropInfo() {
		Node dropContainer = null;
		int dropOffset = 0;
		boolean canDrop = false;
		if (dropableArea != null && dropableArea.getNode() != null
				&& dropableArea.getHighlightedDropTarget() != null) {
			canDrop = true;
			DropTarget dropTarget = dropableArea.getHighlightedDropTarget();
			Node targetNode = getSourceNode(dropableArea.getNode());
			
			if (dropTarget == DropTarget.BEFORE || dropTarget == DropTarget.AFTER) {
				dropContainer = targetNode.getParentNode();
				int offset = 0;
				for (int i = 0; i < dropContainer.getChildNodes().getLength(); i++) {
					if (targetNode.equals(dropContainer.getChildNodes().item(i))) {
						break;
					}
					offset++;
				}
				
				if (dropTarget == DropTarget.BEFORE) {
					dropOffset = offset;
				} else if (dropTarget == DropTarget.AFTER) {
					dropOffset = offset + 1;
				}
			} else if(dropTarget == DropTarget.BEGIN || dropTarget == DropTarget.END) {
				dropContainer = targetNode;
				if (dropTarget == DropTarget.BEGIN) {
					dropOffset = 0;
				} else if (dropTarget == DropTarget.END) {
					dropOffset = dropContainer.getChildNodes().getLength();
				}
			}
		}
		
		return new VpeSourceDropInfo(dropContainer, dropOffset, canDrop);
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.mozilla.listener.MozillaSelectionListener#notifySelectionChanged(org.mozilla.interfaces.nsIDOMDocument, org.mozilla.interfaces.nsISelection, short)
	 */
	public void notifySelectionChanged(nsIDOMDocument domDocument,
			nsISelection selection, short reason) {
		refreshDraggablePattern();
	}

	private interface DropResolver {
		public boolean canDrop(Node node);
	}
}
