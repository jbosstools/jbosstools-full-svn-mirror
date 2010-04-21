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

import java.util.EnumSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.XFileObject;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.ui.editors.dnd.DropUtils;
import org.jboss.tools.common.model.ui.editors.dnd.context.IDNDTextEditor;
import org.jboss.tools.common.model.ui.editors.dnd.context.InnerDragBuffer;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.tld.model.TLDUtil;
import org.jboss.tools.vpe.VpeDebug;
import org.jboss.tools.vpe.dnd.DndUtil.DragTransferData;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.VpeSourceDropInfo;
import org.jboss.tools.vpe.editor.VpeSourceInnerDragInfo;
import org.jboss.tools.vpe.editor.VpeVisualDomBuilder;
import org.jboss.tools.vpe.editor.VpeVisualInnerDragInfo;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.mozilla.listener.MozillaDndListener;
import org.jboss.tools.vpe.editor.util.VpeDndUtil;
import org.jboss.tools.vpe.xulrunner.XPCOM;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIComponentManager;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNSUIEvent;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDragService;
import org.mozilla.interfaces.nsIFile;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsISupportsArray;
import org.mozilla.interfaces.nsISupportsCString;
import org.mozilla.interfaces.nsISupportsString;
import org.mozilla.interfaces.nsITransferable;
import org.mozilla.xpcom.Mozilla;
import org.w3c.dom.Document;
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
public class VpeDnD implements MozillaDndListener {
	private static final String TAG_TAGLIB = "taglib"; //$NON-NLS-1$

	/*
     * Default transfer data
     */
	private static final String VPE_ELEMENT = ""; //$NON-NLS-1$

	/**
	 *  service manager */
	private nsIServiceManager serviceManager;

	/**
	 * component manager
	 */
	private nsIComponentManager componentManager;

	/**
	 * drag service
	 */
	private nsIDragService dragService;

	private VpeController vpeController;

	private VpeSourceInnerDragInfo sourceInnerDragInfo = null;
	private DraggablePattern draggablePattern;
	private DropableArea dropableArea;

	public VpeDnD(VpeController vpeController, MozillaEditor mozillaEditor) {
		this.vpeController = vpeController;
		draggablePattern = new DraggablePattern(mozillaEditor);
	}

	public void dragGesture(nsIDOMEvent domEvent) {
		nsIDOMElement selectedElement = vpeController.getXulRunnerEditor()
				.getLastSelectedElement();
		// start drag sessionvpe-element
		if (isDraggable(selectedElement)) {
			Point pageCoords = getPageCoords(domEvent);
			draggablePattern.startSession(pageCoords.x, pageCoords.y);
			startDragSession(selectedElement);

			draggablePattern.closeSession();
			domEvent.stopPropagation();
			domEvent.preventDefault();
		}
	}
	
	/**
	 * Calls when drag over event ocure
	 * @param event
	 */
	public void dragOver(nsIDOMEvent event) {
		final nsIDOMMouseEvent mouseEvent =
			(nsIDOMMouseEvent) event.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
		
		final XulRunnerEditor editor = vpeController.getXulRunnerEditor();
		new ScrollingSupport(editor).scroll(mouseEvent);
		
		final DropResolver dropResolver;
		if (isInnerDragSession()) {
			dropResolver = getDropResolverForInternalDrop();

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
			innerDrop((nsIDOMMouseEvent)domEvent.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID));
		} else {
			//in this case it's is  external drag
			externalDrop((nsIDOMMouseEvent)domEvent.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID), VpeController.MODEL_FLAVOR, ""); //$NON-NLS-1$
		}
		disposeDropableArea();
		vpeController.onRefresh();
	}
	
	public void dragExit(nsIDOMEvent domEvent) {
//		disposeDropableArea();
		nsIDOMNode visualNode = (nsIDOMNode) domEvent.getTarget()
				.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);
		if (visualNode.getNodeType()==nsIDOMNode.DOCUMENT_NODE) {
			disposeDropableArea();
		}
	}
	
	public void selectionChanged() {
		nsIDOMElement selectedElement = vpeController.getXulRunnerEditor()
				.getLastSelectedElement();
		if (isDraggable(selectedElement)) {
			draggablePattern.showDragIcon(selectedElement);
		} else {
			draggablePattern.hideDragIcon();
		}
	}

	public boolean isDragIconClicked(nsIDOMMouseEvent mouseEvent) {
		return draggablePattern.isDragIconClicked(mouseEvent);
	}

	private DropResolver getDropResolverForExternalDrop() {
		if (getDragService().getCurrentSession()
				.isDataFlavorSupported(VpeController.MODEL_FLAVOR)) {
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
		return getDropResolverForNode(sourceInnerDragInfo.getNode());
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
		nsIDOMDocument document = vpeController.getVisualBuilder()
				.getOriginalTargetNode(event).getOwnerDocument();

		Point clientCoords = getClientCoords(event);

		nsIDOMNode originalVisualNode = DndUtil.getElementFromPoint(document,
				clientCoords.x, clientCoords.y);
		if (originalVisualNode == null) {
			return;
		}
		
		if (dropableArea == null) {
			dropableArea = new DropableArea(document);
		}
		Node originalSourceNode = vpeController.getDomMapping()
				.getNearSourceNode(originalVisualNode);
		if (originalSourceNode.getNodeType() == Node.TEXT_NODE) {
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
			Node sourceNode;
			Node sourceNodeParent = originalSourceNode;
			boolean nodeFound = false;
			do {
				sourceNode = sourceNodeParent;
				sourceNodeParent = sourceNode.getParentNode();
				nodeFound = dropResolver.canDrop(sourceNodeParent);
			} while (sourceNodeParent != null && !nodeFound);
			
			if (nodeFound) {
				highlightedNode = sourceNode;
				dropTargets = EnumSet.of(DropTarget.BEFORE, DropTarget.AFTER);
			} else {
				highlightedNode = null;
				dropTargets = EnumSet.noneOf(DropTarget.class);
			}
		}

		dropableArea.setDropTargets(dropTargets);
		dropableArea.setNode(
				vpeController.getDomMapping().getNearVisualNode(highlightedNode));
		Point mouseCoords = getPageCoords(event);
		dropableArea.setHighlightedDropTarget(mouseCoords.x, mouseCoords.y);
		dropableArea.setVisible(true);
		dropableArea.redraw();
	}

	private Point getClientCoords(nsIDOMEvent event) {
		final nsIDOMMouseEvent mouseEvent =
			(nsIDOMMouseEvent) event.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
		return new Point(mouseEvent.getClientX(), mouseEvent.getClientY());
	}
	
	private void disposeDropableArea() {
		if (dropableArea != null) {
			dropableArea.dispose();
			dropableArea = null;
		}
	}

	/**
	 * Starts drag session
	 * @param dragetElement
	 */
	private void startDragSession(nsIDOMNode  node) {
		nsISupportsArray transArray = (nsISupportsArray) getComponentManager()
				.createInstanceByContractID(XPCOM.NS_SUPPORTSARRAY_CONTRACTID, null,
						nsISupportsArray.NS_ISUPPORTSARRAY_IID);
		transArray.appendElement(createTransferable());
		getDragService().invokeDragSession(node, transArray, null,
				nsIDragService.DRAGDROP_ACTION_MOVE
						| nsIDragService.DRAGDROP_ACTION_COPY
						| nsIDragService.DRAGDROP_ACTION_LINK);
	}

	/**
	 * Creates transferable object to start drag session
	 * 
	 * @return transferable object
	 */
	private nsITransferable createTransferable() {
		
		nsITransferable iTransferable = (nsITransferable) getComponentManager()
						.createInstanceByContractID(XPCOM.NS_TRANSFERABLE_CONTRACTID, null,
								nsITransferable.NS_ITRANSFERABLE_IID);
		nsISupportsString transferData = (nsISupportsString) getComponentManager()
		.createInstanceByContractID(XPCOM.NS_SUPPORTSSTRING_CONTRACTID, null,
				nsISupportsString.NS_ISUPPORTSSTRING_IID);
		String data=VPE_ELEMENT; 
		transferData.setData(data);
		iTransferable.setTransferData(VpeController.MODEL_FLAVOR, transferData, data.length());
		iTransferable.setTransferData("text/plain", transferData, data.length()); //$NON-NLS-1$
		iTransferable.setTransferData("text/unicode", transferData,data.length()*2); //$NON-NLS-1$
		iTransferable.setTransferData("text/html", transferData, data.length()*2); //$NON-NLS-1$
		iTransferable.setTransferData("text/xml", transferData, data.length()*2); //$NON-NLS-1$
		iTransferable.setTransferData("text/rtf", transferData, data.length()*2); //$NON-NLS-1$
		iTransferable.setTransferData("text/enriched", transferData, data.length()*2); //$NON-NLS-1$
		iTransferable.setTransferData("text/richtext", transferData, data.length()*2); //$NON-NLS-1$
		iTransferable.setTransferData("text/t140", transferData, data.length()*2); //$NON-NLS-1$
		
		return iTransferable;
	}

	private void refreshCanDrop(nsIDOMEvent event) {

		nsIDOMMouseEvent mouseEvent = (nsIDOMMouseEvent) event.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
		getDragService().getCurrentSession().setCanDrop(dropableArea != null
				&& dropableArea.getHighlightedDropTarget() != null);
		mouseEvent.preventDefault();
		mouseEvent.stopPropagation();

//		boolean canDrop = true;
//		//in this condition  early was check for xulelement
//		if (getDragService().getCurrentSession().isDataFlavorSupported(VpeController.MODEL_FLAVOR)) {
//			MozillaDropInfo info;
//
//			if(isInnerDragSession()){
//				info = canInnerDrop(mouseEvent);
//			} else {
//				info = canExternalDrop(mouseEvent, VpeController.MODEL_FLAVOR, ""); //$NON-NLS-1$
//			}
//			if (info != null) {
//				canDrop = info.canDrop();
//			}
//		}
//      //sets possability to drop current element here
//		//Added by estherbin fix jbide-1046
//        VpeSelectionController selectionController = vpeController.getVisualSelectionController();
//        final VpeVisualCaretInfo visualCaretInfo = vpeController.getSelectionBuilder().getVisualCaretInfo(event);
//
//        final nsIDOMEventTarget target = event.getTarget();
//        final nsIDOMNode targetDomNode = (nsIDOMNode) target.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);
////        final nsIDOMNode selectedVisualNode = controller.getXulRunnerEditor().getLastSelectedNode();
//        try {
//            if ((targetDomNode.getFirstChild() != null) && (targetDomNode.getFirstChild().getNodeType() == nsIDOMNode.TEXT_NODE)) {
//                selectionController.getSelection(nsISelectionController.SELECTION_NORMAL).collapse(targetDomNode.getFirstChild(),
//                        visualCaretInfo.getRageOffset());
//            } else if ((targetDomNode.getNodeType() != nsIDOMNode.TEXT_NODE)) {
//                selectionController.getSelection(nsISelectionController.SELECTION_NORMAL).collapse(targetDomNode, 0);
//            }
//        } catch (XPCOMException xpcome) {
//            event.stopPropagation();
//            event.preventDefault();
//        }
	}

	private void externalDropAny(final String flavor, final String data,
			final Point range) {
		StructuredTextEditor sourceEditor = vpeController.getSourceEditor();
		if (flavor == null || flavor.length() == 0 
				|| !(sourceEditor instanceof IDNDTextEditor)) {
			return;
		}
		
			//vpeController.getSourceEditor().getTextViewer().getTextWidget().setCaretOffset(range.x);
		sourceEditor.setHighlightRange(range.x, range.y, true);
		((IDNDTextEditor) sourceEditor).runDropCommand(flavor, data);
//		IDropCommand dropCommand = DropCommandFactory.getInstance()
//				.getDropCommand(flavor, JSPTagProposalFactory.getInstance());
//	
//		boolean promptAttributes = JspEditorPlugin.getDefault()
//				.getPreferenceStore().getBoolean(
//						IVpePreferencesPage.ASK_TAG_ATTRIBUTES_ON_TAG_INSERT);
//		dropCommand.getDefaultModel().setPromptForTagAttributesRequired(
//				promptAttributes);
//		DropData dropData = new DropData(flavor, data,
//				vpeController.getSourceEditor().getEditorInput(),
//				(ISourceViewer) vpeController.getSourceEditor().getAdapter(ISourceViewer.class),
//				vpeController.new VpeSelectionProvider(range.x, range.y),
//				container);
//
//		/*
//		 * https://jira.jboss.org/jira/browse/JBIDE-4982 Setting the value
//		 * provider to create tag insert dialog.
//		 */
//		if (vpeController.getSourceEditor() instanceof JSPTextEditor) {
//			dropData.setValueProvider(((JSPTextEditor) vpeController.getSourceEditor())
//					.createAttributeDescriptorValueProvider());
//		}
//	
//		dropCommand.execute(dropData);
	}
	
	private boolean isInnerDragSession() {
		return getDragService().getCurrentSession().getSourceDocument() != null;
	}
	
	private boolean isDraggable(nsIDOMElement element) {
		vpeController.onHideTooltip();
	
		if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
			System.out.print("<<<<<< canInnerDrag"); //$NON-NLS-1$
		}
		if (sourceInnerDragInfo != null) {
			sourceInnerDragInfo = null;
		}
		boolean canDrag = false;
		VpeVisualInnerDragInfo dragInfo = getInnerDragInfo(element);
		if (dragInfo != null) {
			nsIDOMNode dragNode = dragInfo.getNode();
			if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
				System.out
						.print(" dragNode: " + dragNode.getNodeName() + "(" + dragNode + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			switch (dragNode.getNodeType()) {
			case nsIDOMNode.ELEMENT_NODE: {
				canDrag = vpeController.getVisualBuilder().canInnerDrag((nsIDOMElement) dragNode
						.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID));
				break;
			}
			case nsIDOMNode.TEXT_NODE: {
				vpeController.getVisualBuilder();
				canDrag = VpeVisualDomBuilder.isTextEditable(dragNode);
				break;
			}
			}
			if (canDrag) {
				sourceInnerDragInfo = vpeController.getVisualBuilder()
						.getSourceInnerDragInfo(dragInfo);
				if (sourceInnerDragInfo.getNode() != null) {
					InnerDragBuffer.object = sourceInnerDragInfo.getNode();
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							InnerDragBuffer.object = null;
						}
					});
				} else {
					sourceInnerDragInfo = null;
					canDrag = false;
				}
			}
			if (!canDrag) {
				dragInfo.release();
			}
		}
		if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
			System.out.println("  canDrag: " + canDrag); //$NON-NLS-1$
		}
		return canDrag;
	}
	
// this method is never used
//	private MozillaDropInfo canInnerDrop(nsIDOMMouseEvent event) {
//		vpeController.onHideTooltip();
//	
//		if (vpeController.getDropWindow().isActive()) {
//			if (!event.getAltKey()) {
//				vpeController.getDropWindow().close();
//			} else {
//				return null;
//			}
//		}
//		if (event.getAltKey()) {
//			nsIDOMNode visualNode = VisualDomUtil.getTargetNode(event);
//			Node sourceNode = vpeController.getDomMapping().getNearSourceNode(visualNode);
//			if (sourceNode != null) {
//				vpeController.getDropWindow().setActive(true);
//				vpeController.getDropWindow().setEventPosition(event.getScreenX(), event
//						.getScreenY());
//				vpeController.getDropWindow().setInitialTargetNode(sourceNode);
//				vpeController.getDropWindow().open();
//				event.stopPropagation();
//				event.preventDefault();
//				return null;
//			}
//		}
//		if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
//			System.out.print("<<<<<< canInnerDrop"); //$NON-NLS-1$
//		}
//		boolean canDrop = false;
//
//		nsIDOMNode caretParent = null;
//		long caretOffset = 0;
//		if (sourceInnerDragInfo != null) {
//			VpeVisualDropInfo visualDropInfo = getDropInfo(event);
//			if (visualDropInfo.getDropContainer() != null) {
//				if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
//					System.out.print("  container: " //$NON-NLS-1$
//							+ visualDropInfo.getDropContainer().getNodeName()
//							+ "(" //$NON-NLS-1$
//							+ visualDropInfo.getDropContainer()
//							+ ")  parent: " //$NON-NLS-1$
//							+ visualDropInfo.getDropContainer().getParentNode()
//									.getNodeName()
//							+ "(" //$NON-NLS-1$
//							+ visualDropInfo.getDropContainer().getParentNode()
//							+ ")  offset: " //$NON-NLS-1$
//							+ visualDropInfo.getDropOffset());
//				}
//				VpeSourceDropInfo sourceDropInfo
//						= getSourceDropInfo(sourceInnerDragInfo.getNode(),
//								visualDropInfo, true);
//				canDrop = sourceDropInfo.canDrop();
//				if (canDrop) {
//					VpeVisualDropInfo newVisualDropInfo
//							= getDropInfo(sourceDropInfo.getContainer(),
//									sourceDropInfo.getOffset());
//					if (newVisualDropInfo != null) {
//						correctVisualDropPosition(event,
//								newVisualDropInfo, visualDropInfo);
//						caretParent = newVisualDropInfo.getDropContainer();
//						caretOffset = newVisualDropInfo.getDropOffset();
//					}
//				}
//			}
//			visualDropInfo.release();
//		}
//		if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
//			System.out.println("  canDrop: " + canDrop); //$NON-NLS-1$
//		}
//		return new MozillaDropInfo(canDrop, caretParent, caretOffset);
//	}
	
	private void innerDrop(nsIDOMMouseEvent event) {
		vpeController.onHideTooltip();
	
		if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
			System.out.print("<<<<<< innerDrop"); //$NON-NLS-1$
		}
		if (sourceInnerDragInfo != null) {
			VpeSourceDropInfo sourceDropInfo = getDropInfo(event);
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
							sourceInnerDragInfo, sourceDropInfo);

					if (sourceInnerDragInfo != null) {
						sourceInnerDragInfo = null;
					}
				}
			}
		}
		if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
			System.out.println();
		}
	}

	private void externalDrop(nsIDOMMouseEvent mouseEvent, String flavor, String data) {
		vpeController.onHideTooltip();
	
		VpeSourceDropInfo dropInfo = getDropInfo(mouseEvent);
		Point range = getSourceSelectionRange(
				dropInfo.getContainer(), dropInfo.getOffset());
	
		// if (MODEL_FLAVOR.equals(flavor)) {
		// XModelObject object = PreferenceModelUtilities.getPreferenceModel()
		// .getModelBuffer().source();
		// if(object == null)
	
		final DragTransferData dragTransferData = DndUtil.getDragTransferData();
		final nsISupports aValue = dragTransferData.getValue();
	
		String aFlavor = ""; //$NON-NLS-1$
		if (VpeDndUtil.isNsIFileInstance(aValue)) {
			nsIFile aFile = (nsIFile) aValue
					.queryInterface(nsIFile.NS_IFILE_IID);
	
			// because it is external, convert the path to URL
			final String path = aFile.getPath();
			data = path != null ? DropUtils.convertPathToUrl(path) : null;
			aFlavor = DndUtil.kFileMime;
	
		} else if (VpeDndUtil.isNsICStringInstance(aValue)) {
			nsISupportsCString aString = (nsISupportsCString) aValue
					.queryInterface(nsISupportsCString.NS_ISUPPORTSCSTRING_IID);
			data = aString.getData();
			aFlavor = DndUtil.kHTMLMime;
		} else if (VpeDndUtil.isNsIStringInstance(aValue)) {
			nsISupportsString aString = (nsISupportsString) aValue
					.queryInterface(nsISupportsString.NS_ISUPPORTSSTRING_IID);
			data = aString.getData();
			if (VpeController.MODEL_FLAVOR.equals(dragTransferData.getFlavor())) {
				aFlavor = dragTransferData.getFlavor();
			} else {
				aFlavor = DndUtil.kURLMime;
			}
		}
	
		// if (object.getFileType() == XModelObject.FILE
		// && !TLDUtil.isTaglib(object)) {
		//              flavor = "application/x-moz-file"; //$NON-NLS-1$
		// IFile f = (IFile) EclipseResourceUtil.getResource(object);
		// try {
		// data = f.getLocation().toFile().toURL().toString();
		// } catch (Exception e) {
		// VpePlugin.getPluginLog().logError(e);
		// }
		// } else {
		// String tagname = getTagName(object);
		//              if (tagname.indexOf("taglib") >= 0)tagname = "taglib"; //$NON-NLS-1$ //$NON-NLS-2$
		// Node sourceDragNode = ((Document) getModel().getAdapter(
		// Document.class)).createElement(tagname);
		// if (visualDropInfo.getDropContainer() != null) {
		// sourceDropInfo = vpeController.getVisualBuilder().getSourceDropInfo(
		// sourceDragNode, visualDropInfo, true);
		// range = vpeController.getSelectionBuilder().getSourceSelectionRange(
		// sourceDropInfo.getContainer(), sourceDropInfo
		// .getOffset());
		// }
		// }
	
		if (dropInfo.getContainer() != null && data != null) {
			if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
				System.out
						.println("  drop!  container: " + dropInfo.getContainer().getNodeName()); //$NON-NLS-1$
			}

			externalDropAny(aFlavor, data, range);
	
			// TypedEvent tEvent = new TypedEvent(mouseEvent);
			// tEvent.data = data;
			// dropContext.setFlavor(aFlavor);
			// dropContext.setMimeData(data);
			// DnDUtil.fireDnDEvent(dropContext, textEditor, tEvent);
		}
	}

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
		nsIDOMNSUIEvent nsuiEvent = (nsIDOMNSUIEvent)
				domEvent.queryInterface(nsIDOMNSUIEvent.NS_IDOMNSUIEVENT_IID);
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

	private VpeVisualInnerDragInfo getInnerDragInfo(nsIDOMElement element) {
		if (element == null) {
			return null;
		} else {
			return new VpeVisualInnerDragInfo(element);
		}
	}
	
	private VpeSourceDropInfo getDropInfo(nsIDOMEvent event) {
		Node dropContainer = null;
		int dropOffset = 0;
		boolean canDrop = false;
		if (dropableArea != null && dropableArea.getNode() != null
				&& dropableArea.getHighlightedDropTarget() != null) {
			canDrop = true;
			DropTarget dropTarget = dropableArea.getHighlightedDropTarget();
			Node targetNode = vpeController.getDomMapping()
					.getNearSourceNode(dropableArea.getNode());
			
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

	private interface DropResolver {
		public boolean canDrop(Node node);
	}
}
