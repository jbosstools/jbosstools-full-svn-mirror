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

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.ui.dnd.ModelTransfer;
import org.jboss.tools.common.model.ui.editors.dnd.DropCommandFactory;
import org.jboss.tools.common.model.ui.editors.dnd.DropData;
import org.jboss.tools.common.model.ui.editors.dnd.DropUtils;
import org.jboss.tools.common.model.ui.editors.dnd.IDropCommand;
import org.jboss.tools.common.model.ui.editors.dnd.context.InnerDragBuffer;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.jsp.jspeditor.dnd.JSPTagProposalFactory;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;
import org.jboss.tools.jst.web.tld.model.TLDUtil;
import org.jboss.tools.vpe.VpeDebug;
import org.jboss.tools.vpe.dnd.DndUtil.DragTransferData;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.VpeSourceInnerDragInfo;
import org.jboss.tools.vpe.editor.VpeSourceInnerDropInfo;
import org.jboss.tools.vpe.editor.VpeVisualCaretInfo;
import org.jboss.tools.vpe.editor.VpeVisualDomBuilder;
import org.jboss.tools.vpe.editor.VpeVisualInnerDragInfo;
import org.jboss.tools.vpe.editor.VpeVisualInnerDropInfo;
import org.jboss.tools.vpe.editor.mozilla.MozillaDropInfo;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.mozilla.listener.MozillaDndListener;
import org.jboss.tools.vpe.editor.selection.VpeSelectionController;
import org.jboss.tools.vpe.editor.template.VpePseudoContentCreator;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.VisualDomUtil;
import org.jboss.tools.vpe.editor.util.VpeDndUtil;
import org.jboss.tools.vpe.xulrunner.XPCOM;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIComponentManager;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMEventTarget;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNSDocument;
import org.mozilla.interfaces.nsIDOMNSUIEvent;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.interfaces.nsIDragService;
import org.mozilla.interfaces.nsIFile;
import org.mozilla.interfaces.nsISelectionController;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsISupportsArray;
import org.mozilla.interfaces.nsISupportsCString;
import org.mozilla.interfaces.nsISupportsString;
import org.mozilla.interfaces.nsITransferable;
import org.mozilla.xpcom.Mozilla;
import org.mozilla.xpcom.XPCOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author Max Areshkau
 * @author Yahor Radtsevich (yradtsevich)
 *
 * Class responsible for Drag&Drop functionality
 */
public class VpeDnD implements MozillaDndListener {
	private static int HUGE_DISTANCE = 999999;
	
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
	private VpeVisualInnerDragInfo innerDragInfo = null;
	private DraggablePattern draggablePattern;

	public VpeDnD(VpeController vpeController, MozillaEditor mozillaEditor) {
		this.vpeController = vpeController;
		draggablePattern = new DraggablePattern(mozillaEditor);
	}

	public void dragGesture(nsIDOMEvent domEvent) {
		nsIDOMElement selectedElement = vpeController.getXulRunnerEditor()
				.getLastSelectedElement();
		// start drag sessionvpe-element
		if (isDraggable(selectedElement)) {
			Point mousePosition = getMousePosition(domEvent);
			draggablePattern.startSession(mousePosition.x, mousePosition.y);
			startDragSession(selectedElement);
			draggablePattern.closeSession();
			domEvent.stopPropagation();
			domEvent.preventDefault();
		}
	}
	
	private Point getMousePosition(nsIDOMEvent domEvent) {
		nsIDOMNSUIEvent nsuiEvent = (nsIDOMNSUIEvent)
				domEvent.queryInterface(nsIDOMNSUIEvent.NS_IDOMNSUIEVENT_IID);
		return new Point(nsuiEvent.getPageX(), nsuiEvent.getPageY());
	}

	/**
	 * Calls when drag over event ocure
	 * @param event
	 */
	public void dragOver(nsIDOMEvent event) {
		final nsIDOMMouseEvent mouseEvent =
			(nsIDOMMouseEvent) event.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
		if (isInnerDragSession()) {
			Point mousePosition = getMousePosition(event);
			draggablePattern.moveTo(mousePosition.x, mousePosition.y);
		}
		final XulRunnerEditor editor = vpeController.getXulRunnerEditor();
		new ScrollingSupport(editor).scroll(mouseEvent);
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
		vpeController.onRefresh();
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
		boolean canDrop = true;

		nsIDOMMouseEvent mouseEvent = (nsIDOMMouseEvent) event.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
		//in this condition  early was check for xulelement
		if (getDragService().getCurrentSession().isDataFlavorSupported(VpeController.MODEL_FLAVOR)) {
			MozillaDropInfo info;

			if(getDragService().getCurrentSession().getSourceNode()==null){
				//external drag 
				  info = canExternalDrop(mouseEvent, VpeController.MODEL_FLAVOR, ""); //$NON-NLS-1$
			} else {
			    //internal drag
				 info = canInnerDrop(mouseEvent);
			}
			if (info != null) {
				canDrop = info.canDrop();
			}
		}
      //sets possability to drop current element here
		//Added by estherbin fix jbide-1046
        VpeSelectionController selectionController = vpeController.getVisualSelectionController();
        final VpeVisualCaretInfo visualCaretInfo = vpeController.getSelectionBuilder().getVisualCaretInfo(event);

        final nsIDOMEventTarget target = event.getTarget();
        final nsIDOMNode targetDomNode = (nsIDOMNode) target.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);
//        final nsIDOMNode selectedVisualNode = controller.getXulRunnerEditor().getLastSelectedNode();
        try {
            if ((targetDomNode.getFirstChild() != null) && (targetDomNode.getFirstChild().getNodeType() == nsIDOMNode.TEXT_NODE)) {
                selectionController.getSelection(nsISelectionController.SELECTION_NORMAL).collapse(targetDomNode.getFirstChild(),
                        visualCaretInfo.getRageOffset());
            } else if ((targetDomNode.getNodeType() != nsIDOMNode.TEXT_NODE)) {
                selectionController.getSelection(nsISelectionController.SELECTION_NORMAL).collapse(targetDomNode, 0);
            }
        } catch (XPCOMException xpcome) {
            event.stopPropagation();
            event.preventDefault();
        }

		//sets possability to drop current element here
		getDragService().getCurrentSession().setCanDrop(canDrop);
		mouseEvent.preventDefault();
		mouseEvent.stopPropagation();
	}

	private void externalDropAny(final String flavor, final String data, final Point range,
			Node container) {
		if (flavor == null || flavor.length() == 0)
			return;
		IDropCommand dropCommand = DropCommandFactory.getInstance()
				.getDropCommand(flavor, JSPTagProposalFactory.getInstance());
	
		boolean promptAttributes = JspEditorPlugin.getDefault()
				.getPreferenceStore().getBoolean(
						IVpePreferencesPage.ASK_TAG_ATTRIBUTES_ON_TAG_INSERT);
		dropCommand.getDefaultModel().setPromptForTagAttributesRequired(
				promptAttributes);
		DropData dropData = new DropData(flavor, data,
				vpeController.getSourceEditor().getEditorInput(),
				(ISourceViewer) vpeController.getSourceEditor().getAdapter(ISourceViewer.class),
				vpeController.new VpeSelectionProvider(range.x, range.y),
				container);

		/*
		 * https://jira.jboss.org/jira/browse/JBIDE-4982 Setting the value
		 * provider to create tag insert dialog.
		 */
		if (vpeController.getSourceEditor() instanceof JSPTextEditor) {
			dropData.setValueProvider(((JSPTextEditor) vpeController.getSourceEditor())
					.createAttributeDescriptorValueProvider());
		}
	
		dropCommand.execute(dropData);
	}
	
	private boolean isInnerDragSession() {
		return getDragService().getCurrentSession().getSourceDocument() != null;
	}
	
	private boolean isDraggable(nsIDOMElement element) {
		vpeController.onHideTooltip();
	
		if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
			System.out.print("<<<<<< canInnerDrag"); //$NON-NLS-1$
		}
		if (innerDragInfo != null) {
			innerDragInfo.release();
			innerDragInfo = null;
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
				canDrag = vpeController.getVisualBuilder().isTextEditable(dragNode);
				break;
			}
			}
			if (canDrag) {
				VpeSourceInnerDragInfo sourceInnerDragInfo = vpeController.getVisualBuilder()
						.getSourceInnerDragInfo(dragInfo);
				if (sourceInnerDragInfo.getNode() != null) {
					innerDragInfo = dragInfo;
					InnerDragBuffer.object = sourceInnerDragInfo.getNode();
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							InnerDragBuffer.object = null;
						}
					});
				} else {
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
	
	private MozillaDropInfo canInnerDrop(nsIDOMMouseEvent event) {
		vpeController.onHideTooltip();
	
		if (vpeController.getDropWindow().isActive()) {
			if (!event.getAltKey()) {
				vpeController.getDropWindow().close();
			} else {
				return null;
			}
		}
		if (event.getAltKey()) {
			nsIDOMNode visualNode = VisualDomUtil.getTargetNode(event);
			Node sourceNode = vpeController.getDomMapping().getNearSourceNode(visualNode);
			if (sourceNode != null) {
				vpeController.getDropWindow().setActive(true);
				vpeController.getDropWindow().setEventPosition(event.getScreenX(), event
						.getScreenY());
				vpeController.getDropWindow().setInitialTargetNode(sourceNode);
				vpeController.getDropWindow().open();
				event.stopPropagation();
				event.preventDefault();
				return null;
			}
		}
		if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
			System.out.print("<<<<<< canInnerDrop"); //$NON-NLS-1$
		}
		boolean canDrop = false;

		nsIDOMNode caretParent = null;
		long caretOffset = 0;
		if (innerDragInfo != null) {
			VpeVisualInnerDropInfo visualDropInfo = getInnerDropInfo(event);
			if (visualDropInfo.getDropContainer() != null) {
				if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
					System.out.print("  x: " //$NON-NLS-1$
							+ visualDropInfo.getMouseX()
							+ "  y: " //$NON-NLS-1$
							+ visualDropInfo.getMouseY()
							+ "  container: " //$NON-NLS-1$
							+ visualDropInfo.getDropContainer().getNodeName()
							+ "(" //$NON-NLS-1$
							+ visualDropInfo.getDropContainer()
							+ ")  parent: " //$NON-NLS-1$
							+ visualDropInfo.getDropContainer().getParentNode()
									.getNodeName()
							+ "(" //$NON-NLS-1$
							+ visualDropInfo.getDropContainer().getParentNode()
							+ ")  offset: " //$NON-NLS-1$
							+ visualDropInfo.getDropOffset());
				}
				VpeSourceInnerDragInfo sourceInnerDragInfo = vpeController.getVisualBuilder()
						.getSourceInnerDragInfo(innerDragInfo);
				VpeSourceInnerDropInfo sourceDropInfo = vpeController.getVisualBuilder()
						.getSourceInnerDropInfo(sourceInnerDragInfo.getNode(),
								visualDropInfo, true);
				canDrop = sourceDropInfo.canDrop();
				if (canDrop) {
					VpeVisualInnerDropInfo newVisualDropInfo = vpeController.getVisualBuilder()
							.getInnerDropInfo(sourceDropInfo.getContainer(),
									sourceDropInfo.getOffset());
					if (newVisualDropInfo != null) {
						vpeController.getVisualBuilder().correctVisualDropPosition(
								newVisualDropInfo, visualDropInfo);
						caretParent = newVisualDropInfo.getDropContainer();
						caretOffset = newVisualDropInfo.getDropOffset();
					}
				}
			}
			visualDropInfo.release();
		}
		if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
			System.out.println("  canDrop: " + canDrop); //$NON-NLS-1$
		}
		return new MozillaDropInfo(canDrop, caretParent, caretOffset);
	}
	
	private void innerDrop(nsIDOMMouseEvent event) {
		vpeController.onHideTooltip();
	
		if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
			System.out.print("<<<<<< innerDrop"); //$NON-NLS-1$
		}
		if (innerDragInfo != null) {
			VpeVisualInnerDropInfo visualDropInfo = getInnerDropInfo(event);
			if (visualDropInfo.getDropContainer() != null) {
				if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
					System.out
							.print("  container: " + visualDropInfo.getDropContainer().getNodeName() + //$NON-NLS-1$
									"(" + visualDropInfo.getDropContainer() //$NON-NLS-1$
									+ ")" + //$NON-NLS-1$
									"  offset: " //$NON-NLS-1$
									+ visualDropInfo.getDropOffset());
				}
	
				VpeSourceInnerDragInfo sourceInnerDragInfo = vpeController.getVisualBuilder()
						.getSourceInnerDragInfo(innerDragInfo);
				VpeSourceInnerDropInfo sourceDropInfo = vpeController.getVisualBuilder()
						.getSourceInnerDropInfo(sourceInnerDragInfo.getNode(),
								visualDropInfo, true);
				if (sourceDropInfo.canDrop()) {
					VpeVisualInnerDropInfo newVisualDropInfo = vpeController.getVisualBuilder()
							.getInnerDropInfo(sourceDropInfo.getContainer(),
									sourceDropInfo.getOffset());
					if (newVisualDropInfo != null) {
						vpeController.getVisualBuilder().correctVisualDropPosition(
								newVisualDropInfo, visualDropInfo);
						sourceDropInfo.setTop(visualDropInfo.getMouseY());
						sourceDropInfo.setLeft(visualDropInfo.getMouseX());
						vpeController.getVisualBuilder().innerDrop(sourceInnerDragInfo,
								sourceDropInfo);
						if (innerDragInfo != null) {
							innerDragInfo.release();
							innerDragInfo = null;
						}
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
	
		VpeVisualInnerDropInfo visualDropInfo = getInnerDropInfo(mouseEvent);
		Point range = vpeController.getSelectionBuilder().getSourceSelectionRangeAtVisualNode(
				visualDropInfo.getDropContainer(), (int) visualDropInfo
						.getDropOffset());
	
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
		// sourceDropInfo = vpeController.getVisualBuilder().getSourceInnerDropInfo(
		// sourceDragNode, visualDropInfo, true);
		// range = vpeController.getSelectionBuilder().getSourceSelectionRange(
		// sourceDropInfo.getContainer(), sourceDropInfo
		// .getOffset());
		// }
		// }
	
		if (visualDropInfo.getDropContainer() != null && data != null) {
			if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
				System.out
						.println("  drop!  container: " + visualDropInfo.getDropContainer().getNodeName()); //$NON-NLS-1$
			}
			externalDropAny(aFlavor, data, range, null);
	
			// TypedEvent tEvent = new TypedEvent(mouseEvent);
			// tEvent.data = data;
			// dropContext.setFlavor(aFlavor);
			// dropContext.setMimeData(data);
			// DnDUtil.fireDnDEvent(dropContext, textEditor, tEvent);
		}
	}
	
	private MozillaDropInfo canExternalDrop(nsIDOMMouseEvent mouseEvent, String flavor, String data) {
		InnerDragBuffer.object = null;
		vpeController.onHideTooltip();
	
		if (vpeController.getDropWindow().isActive()) {
			if (!mouseEvent.getAltKey()) {
				vpeController.getDropWindow().close();
			} else {
				return new MozillaDropInfo(false, null, 0);
			}
		}
		if (mouseEvent.getAltKey()) {
			nsIDOMEvent event = (nsIDOMEvent) mouseEvent
					.queryInterface(nsIDOMEvent.NS_IDOMEVENT_IID);
			nsIDOMNode visualNode = (nsIDOMNode) event.getTarget()
					.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);
			Node sourceNode = vpeController.getDomMapping().getNearSourceNode(visualNode);
			if (sourceNode != null) {
				if (ModelTransfer.MODEL.equals(flavor)) {
					// XModelObject object =
					// PreferenceModelUtilities.getPreferenceModel().
					// getModelBuffer().source();
					// InnerDragBuffer.object = object;
				} else {
					vpeController.getDropWindow().setFlavor(flavor);
				}
				vpeController.getDropWindow().setActive(true);
				vpeController.getDropWindow().setEventPosition(mouseEvent.getScreenX(), mouseEvent
						.getScreenY());
				vpeController.getDropWindow().setInitialTargetNode(sourceNode);
				vpeController.getDropWindow().open();
				mouseEvent.stopPropagation();
				mouseEvent.preventDefault();
				return new MozillaDropInfo(false, null, 0);
			}
		}
		boolean canDrop = false;
		nsIDOMNode caretParent = null;
		long caretOffset = 0;
	
		if (VpeController.MODEL_FLAVOR.equals(flavor)) {
			XModelObject object = PreferenceModelUtilities.getPreferenceModel()
					.getModelBuffer().source();
			if (object.getFileType() == XModelObject.FILE
					&& !TLDUtil.isTaglib(object)) {
				IFile f = (IFile) EclipseResourceUtil.getResource(object);
				canDrop = f != null;
				VpeVisualInnerDropInfo visualDropInfo = getInnerDropInfo(mouseEvent);
				caretParent = visualDropInfo.getDropContainer();
				caretOffset = visualDropInfo.getDropOffset();
			} else {
				String tagname = vpeController.getTagName(object);
				if (tagname.indexOf("taglib") >= 0)tagname = "taglib"; //$NON-NLS-1$ //$NON-NLS-2$
				Node sourceDragNode = ((Document) vpeController.getModel().getAdapter(
						Document.class)).createElement(tagname);
				VpeVisualInnerDropInfo visualDropInfo = getInnerDropInfo(mouseEvent);
				if (visualDropInfo.getDropContainer() != null) {
					VpeSourceInnerDropInfo sourceDropInfo = vpeController.getVisualBuilder()
							.getSourceInnerDropInfo(sourceDragNode,
									visualDropInfo, true);
					canDrop = sourceDropInfo.canDrop();
					if (canDrop) {
						VpeVisualInnerDropInfo newVisualDropInfo = vpeController.getVisualBuilder()
								.getInnerDropInfo(
										sourceDropInfo.getContainer(),
										sourceDropInfo.getOffset());
						if (newVisualDropInfo != null) {
							vpeController.getVisualBuilder().correctVisualDropPosition(
									newVisualDropInfo, visualDropInfo);
							caretParent = newVisualDropInfo.getDropContainer();
							caretOffset = newVisualDropInfo.getDropOffset();
						}
					}
				}
				visualDropInfo.release();
			}
		} else if (XulRunnerEditor.TRANS_FLAVOR_kFileMime.equals(flavor)
				|| XulRunnerEditor.TRANS_FLAVOR_kURLMime.equals(flavor)) {
			VpeVisualInnerDropInfo visualDropInfo = getInnerDropInfo(mouseEvent);
			caretParent = visualDropInfo.getDropContainer();
			caretOffset = visualDropInfo.getDropOffset();
			canDrop = true;
	
		}
		if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
			System.out.println("  canDrop: " + canDrop			//$NON-NLS-1$ 
							+ (canDrop ?
										"  container: "			//$NON-NLS-1$
										+ caretParent.getNodeName() 
										+ "  offset: "			//$NON-NLS-1$
										+ caretOffset
									   : ""));					//$NON-NLS-1$
		}
		return new MozillaDropInfo(canDrop, caretParent, caretOffset);
	
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

		// fix of JBIDE-4998
//		nsISelection selection = visualSelectionController.getSelection(
//				nsISelectionController.SELECTION_NORMAL);
//		nsIDOMNode focusNode = selection.getFocusNode();
//		nsIDOMNode anchorNode = selection.getAnchorNode();
//		//when we select input this function return null
//		//but we select elemnt
//		if(focusNode==null && anchorNode==null) {
//			nsIDOMNode  visualNode =(nsIDOMNode) event.getTarget()
//					.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);
//			//fix of JBIDE-1097
//			if(HTML.TAG_SPAN.equalsIgnoreCase(visualNode.getNodeName())) {
//				if(visualBuilder.getXulRunnerEditor().getLastSelectedElement() != null
//						&& !visualBuilder.getNodeBounds(
//								visualBuilder.getXulRunnerEditor()
//										.getLastSelectedElement())
//								.contains(VisualDomUtil.getMousePoint(event))) {
//					return null;
//				}
//			}
//			int offset = (int) VisualDomUtil.getOffset(visualNode);
//			selection.removeAllRanges();
//			selection.collapse(visualNode.getParentNode(), offset);
//			try {
//				selection.extend(visualNode.getParentNode(), offset + 1);
//			} catch(XPCOMException ex) {
//				//just ignore exception
//				// throws when we trying drag element which already resizing
//				return null;
//			}
//			focusNode = selection.getFocusNode();
//			anchorNode = selection.getAnchorNode();
//		}
//		if (focusNode != null && focusNode.equals(anchorNode)) {
//			int focusOffset = selection.getFocusOffset();
//			int anchorOffset = selection.getAnchorOffset();
//			int offset = Math.min(focusOffset, anchorOffset);
//			int length = Math.max(focusOffset, anchorOffset) - offset;
//
//			int focusNodeType = focusNode.getNodeType();
//			if (focusNodeType == nsIDOMNode.ELEMENT_NODE) {
//				if (length == 1) {
//					nsIDOMNodeList children = focusNode.getChildNodes();
//					nsIDOMNode selectedNode = children.item(
//							Math.min(focusOffset, anchorOffset));
//					if (visualBuilder.getNodeBounds(selectedNode).contains(VisualDomUtil.getMousePoint(event))) {
//						int selectedNodeType = selectedNode.getNodeType();
//						if (selectedNodeType == nsIDOMNode.ELEMENT_NODE) {
//							return new VpeVisualInnerDragInfo((nsIDOMElement)selectedNode.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID));
//						} else if (selectedNodeType == nsIDOMNode.TEXT_NODE) {
//							return new VpeVisualInnerDragInfo(selectedNode, 0, selectedNode.getNodeValue().length());
//						}
//					}
//				}
//			} else if (focusNodeType == nsIDOMNode.TEXT_NODE) {
//				return new VpeVisualInnerDragInfo(focusNode, offset, length);
//			}
//		}
//
//		return null;
	}
	
	private VpeVisualInnerDropInfo getInnerDropInfo(nsIDOMEvent event) {
		nsIDOMNSUIEvent nsuiEvent = (nsIDOMNSUIEvent)
				event.queryInterface(nsIDOMNSUIEvent.NS_IDOMNSUIEVENT_IID);
		nsIDOMNode dropContainer = null;
		int dropOffset = 0;
		int mouseX = nsuiEvent.getPageX();
		int mouseY = nsuiEvent.getPageY();

		nsIDOMDocument document = vpeController.getVisualBuilder()
				.getOriginalTargetNode(event).getOwnerDocument();

		nsIDOMNSDocument nsDocument = (nsIDOMNSDocument) document
				.queryInterface(nsIDOMNSDocument.NS_IDOMNSDOCUMENT_IID);
		nsIDOMNode originalNode = DndUtil.getElementFromPoint(nsDocument, mouseX, mouseY);
//		if (originalNode != null) {
//			if (dropableArea == null) {
//				dropableArea = new DropableArea(document);
//				dropableArea.setDropSpots(EnumSet.allOf(DropSpot.class));
//			}
//			dropableArea.setNode(originalNode);
//			dropableArea.setHighlightedSpot(mouseX, mouseY);
//			dropableArea.setVisible(true);
//			dropableArea.redraw();
//		}

		if (originalNode == null || originalNode.getParentNode() == null ||
				originalNode.getParentNode().getNodeType() == Node.DOCUMENT_NODE) {
			return  new VpeVisualInnerDropInfo(null, 0, mouseX, mouseY);
		}
		if (originalNode.getNodeType() == Node.TEXT_NODE) {
			dropContainer = nsuiEvent.getRangeParent();
			nsIDOMNode containerForPseudoContent = VpePseudoContentCreator
					.getContainerForPseudoContent(dropContainer);
			if (containerForPseudoContent != null) {
				dropContainer = containerForPseudoContent;
				dropOffset = 0;
			} else {
				dropOffset = nsuiEvent.getRangeOffset();
			}
		} else {
		    int closestXDistance = HUGE_DISTANCE;
		    int closestYDistance = HUGE_DISTANCE;
		    boolean inNodeFlag = false;
		    nsIDOMNode closestNode = null;

		    nsIDOMNodeList childen = originalNode.getChildNodes();
			long count = childen.getLength();
			for (long i = 0; i < count; i++) {
				nsIDOMNode child = childen.item(i);
				if (VpeVisualDomBuilder.isPseudoElement(child)
						|| VpeVisualDomBuilder.isAnonElement(child)) {
					continue;
				}
				Rectangle rect = vpeController.getVisualBuilder().getNodeBounds(child);
				int fromTop = mouseY - rect.y;
				int fromBottom = mouseY - rect.y - rect.height;

				int yDistance;
				if (fromTop > 0 && fromBottom < 0) {
					yDistance = 0;
				} else {
					yDistance = Math.min(Math.abs(fromTop), Math.abs(fromBottom));
				}
			      
				if (yDistance <= closestYDistance && rect.width > 0 && rect.height > 0) {
					if (yDistance < closestYDistance) {
						closestXDistance = HUGE_DISTANCE;
					}
					int fromLeft = mouseX - rect.x;
					int fromRight = mouseX - rect.x - rect.width;

					int xDistance;
					if (fromLeft > 0 && fromRight < 0) {
						xDistance = 0;
					} else {
						xDistance = Math.min(Math.abs(fromLeft), Math.abs(fromRight));
					}
					if (xDistance == 0 && yDistance == 0) {
						closestNode = child;
						inNodeFlag = true;
						break;
					}

					if (xDistance < closestXDistance || (xDistance == closestXDistance && rect.x <= mouseX)) {
						closestXDistance = xDistance;
						closestYDistance = yDistance;
						closestNode = child;
					}
				}
			}
			
			if (closestNode == null) {
				closestNode = originalNode;
				inNodeFlag = true;
			}
			if (inNodeFlag) {
				if (closestNode.getNodeType() == Node.TEXT_NODE) {
					dropContainer = nsuiEvent.getRangeParent();
					dropOffset = nsuiEvent.getRangeOffset();
				} else {
					if (HTML.TAG_COLGROUP.equalsIgnoreCase(closestNode.getNodeName())) {
						nsIDOMNode nearChild = getNearChild(closestNode, mouseX, mouseY);
						if (nearChild != null && nearChild.getNodeType() == Node.ELEMENT_NODE) {
							dropContainer = nearChild;
						} else {
							dropContainer = closestNode;
						}
					} else {
						dropContainer = closestNode;
					}
					dropOffset = 0;
				}
			} else {
				dropContainer = closestNode.getParentNode();
				dropOffset = (int)VisualDomUtil.getOffset(closestNode);
				Rectangle rect = vpeController.getVisualBuilder().getNodeBounds(closestNode);
				if (VpeVisualDomBuilder.canInsertAfter(mouseX, mouseY, rect)) {
					dropOffset++;
				}
			}
		}
		VpeVisualInnerDropInfo info = new VpeVisualInnerDropInfo(dropContainer, dropOffset, mouseX, mouseY);
		return info;
	}
	
	private nsIDOMNode getNearChild(nsIDOMNode container, int x, int y) {
	    int closestXDistance = HUGE_DISTANCE;
	    int closestYDistance = HUGE_DISTANCE;
	    nsIDOMNode closestNode = null;

	    nsIDOMNodeList childen = container.getChildNodes();
		long count = childen.getLength();
		for (long i = 0; i < count; i++) {
			nsIDOMNode child = childen.item(i);
			if (VpeVisualDomBuilder.isPseudoElement(child) || VpeVisualDomBuilder.isAnonElement(child)) {
				continue;
			}
			Rectangle rect = vpeController.getVisualBuilder().getNodeBounds(child);
			int fromTop = y - rect.y;
			int fromBottom = y - rect.y - rect.height;

			int yDistance;
			if (fromTop > 0 && fromBottom < 0) {
				yDistance = 0;
			} else {
				yDistance = Math.min(Math.abs(fromTop), Math.abs(fromBottom));
			}
		      
			if (yDistance <= closestYDistance && rect.width > 0 && rect.height > 0) {
				if (yDistance < closestYDistance) {
					closestXDistance = HUGE_DISTANCE;
				}
				int fromLeft = x - rect.x;
				int fromRight = x - rect.x - rect.width;

				int xDistance;
				if (fromLeft > 0 && fromRight < 0) {
					xDistance = 0;
				} else {
					xDistance = Math.min(Math.abs(fromLeft), Math.abs(fromRight));
				}
				if (xDistance == 0 && yDistance == 0) {
					closestNode = child;
					break;
				}
				if (xDistance < closestXDistance || (xDistance == closestXDistance && rect.x <= x)) {
					closestXDistance = xDistance;
					closestYDistance = yDistance;
					closestNode = child;
				}
			}
		}
		return closestNode;
	}
// this method is never used
//	public VpeSourceInnerDropInfo canExternalDropMacro(XModelObject object, Node parentNode, int offset) {
//		String tagname = vpeController.getTagName(object);
//		Node sourceDragNode = ((Document) vpeController.getModel().getAdapter(Document.class))
//				.createElement(tagname);
//		return vpeController.getVisualBuilder().getSourceInnerDropInfo(sourceDragNode, parentNode,
//				offset, false);
//	}
	
// this method is never used
//	public void _dragOver(nsIDOMEvent event) {
//		if (!vpeController.getSwitcher()
//				.startActiveEditor(VpeController.ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
//			return;
//		}
//		try {
//			if (VpeDebug.PRINT_VISUAL_DRAGDROP_EVENT) {
//				System.out.println("<<<<<<<<<<<<<<<<<<<< dragOver"); //$NON-NLS-1$
//			}
//			// browser.computeDropPosition(event);
//			boolean canDrop = !vpeController.getXulRunnerEditor().isMozillaDragFlavor();
//			if (canDrop) {
//				Clipboard clipboard = new Clipboard(Display.getCurrent());
//				canDrop = clipboard.getContents(ModelTransfer.getInstance()) != null;
//			}
//			if (canDrop) {
//				canDrop = VpeDndUtil.isDropEnabled((IModelObjectEditorInput)
//						vpeController.getSourceEditor().getEditorInput());
//			}
//			if (canDrop) {
//				VpeVisualCaretInfo caretInfo = vpeController.getSelectionBuilder()
//						.getVisualCaretInfo(event);
//				canDrop = caretInfo.exist();
//				if (canDrop) {
//					caretInfo.showCaret();
//				} else {
//					caretInfo.hideCaret();
//				}
//			}
//			if (!canDrop) {
//				event.stopPropagation();
//				event.preventDefault();
//			}
//		} finally {
//			vpeController.getSwitcher().stopActiveEditor();
//		}
//	}
}
