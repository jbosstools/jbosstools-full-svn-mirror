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
import org.jboss.tools.vpe.editor.VpeVisualInnerDragInfo;
import org.jboss.tools.vpe.editor.VpeVisualInnerDropInfo;
import org.jboss.tools.vpe.editor.mozilla.MozillaDropInfo;
import org.jboss.tools.vpe.editor.mozilla.listener.MozillaDndListener;
import org.jboss.tools.vpe.editor.selection.VpeSelectionController;
import org.jboss.tools.vpe.editor.util.VisualDomUtil;
import org.jboss.tools.vpe.editor.util.VpeDndUtil;
import org.jboss.tools.vpe.xulrunner.XPCOM;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIComponentManager;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMEventTarget;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNode;
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

	public VpeDnD(VpeController vpeController) {
		this.vpeController = vpeController;
	}

	public void dragGesture(nsIDOMEvent domEvent) {
		nsIDOMMouseEvent mouseEvent = (nsIDOMMouseEvent) domEvent
				.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
		boolean canDragFlag = canInnerDrag(mouseEvent);
		// start drag sessionvpe-element
		if (canDragFlag) {
			startDragSession(domEvent);
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
		refreshCanDrop(event);
		vpeController.onRefresh();
	}

	/**
	 * Drop Event handler
	 * @param domEvent
	 * @param vpeController
	 */
	public void dragDrop(nsIDOMEvent domEvent) {
		if(getDragService().getCurrentSession().getSourceDocument()==null) {
			//in this case it's is  external drag
			externalDrop((nsIDOMMouseEvent)domEvent.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID), VpeController.MODEL_FLAVOR, ""); //$NON-NLS-1$
		} else {
			// in this case it's is an internal drag
			innerDrop((nsIDOMMouseEvent)domEvent.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID));
		}
		vpeController.onRefresh();
	}

	/**
	 * Starts drag session
	 * @param dragetElement
	 */
	public void startDragSession(nsIDOMEvent  domEvent) {
		nsISupportsArray transArray = (nsISupportsArray) getComponentManager()
		.createInstanceByContractID(XPCOM.NS_SUPPORTSARRAY_CONTRACTID, null,
				nsISupportsArray.NS_ISUPPORTSARRAY_IID);
		transArray.appendElement(createTransferable());
		getDragService().invokeDragSession((nsIDOMNode) domEvent.getTarget().queryInterface(nsIDOMNode.NS_IDOMNODE_IID), transArray, null,
				nsIDragService.DRAGDROP_ACTION_MOVE
						| nsIDragService.DRAGDROP_ACTION_COPY
						| nsIDragService.DRAGDROP_ACTION_LINK);

		domEvent.stopPropagation();
		domEvent.preventDefault();
		
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

	/**
	 * @return the componentManager
	 */
	public nsIComponentManager getComponentManager() {
		
		if(componentManager==null) {
			
			componentManager = Mozilla.getInstance()
			.getComponentManager();
		}
		return componentManager;
	}

	/**
	 * @return the serviceManager
	 */
	public nsIServiceManager getServiceManager() {
		
		if(serviceManager==null) {
			serviceManager = Mozilla.getInstance()
			.getServiceManager();
		}
		return serviceManager;
	}

	/**
	 * @return the dragService
	 */
	public nsIDragService getDragService() {
		
		if(dragService==null) {
			dragService = (nsIDragService) getServiceManager()
			.getServiceByContractID(XPCOM.NS_DRAGSERVICE_CONTRACTID,
					nsIDragService.NS_IDRAGSERVICE_IID);
		}
		return dragService;
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

	public void externalDropAny(final String flavor, final String data, final Point range,
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
	
	public boolean canInnerDrag(nsIDOMMouseEvent event) {
		vpeController.onHideTooltip();
	
		if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
			System.out.print("<<<<<< canInnerDrag"); //$NON-NLS-1$
		}
		if (innerDragInfo != null) {
			innerDragInfo.release();
			innerDragInfo = null;
		}
		boolean canDrag = false;
		VpeVisualInnerDragInfo dragInfo = vpeController.getSelectionBuilder()
				.getInnerDragInfo(event);
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
	
	public MozillaDropInfo canInnerDrop(nsIDOMMouseEvent event) {
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
			VpeVisualInnerDropInfo visualDropInfo = vpeController.getSelectionBuilder()
					.getInnerDropInfo(event);
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
	
	public void innerDrop(nsIDOMMouseEvent event) {
		vpeController.onHideTooltip();
	
		if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
			System.out.print("<<<<<< innerDrop"); //$NON-NLS-1$
		}
		if (innerDragInfo != null) {
			VpeVisualInnerDropInfo visualDropInfo = vpeController.getSelectionBuilder()
					.getInnerDropInfo(event);
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
	
	public void externalDrop(nsIDOMMouseEvent mouseEvent, String flavor, String data) {
		vpeController.onHideTooltip();
	
		VpeVisualInnerDropInfo visualDropInfo = vpeController.getSelectionBuilder()
				.getInnerDropInfo(mouseEvent);
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
	
	public MozillaDropInfo canExternalDrop(nsIDOMMouseEvent mouseEvent, String flavor, String data) {
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
				VpeVisualInnerDropInfo visualDropInfo = vpeController.getSelectionBuilder()
						.getInnerDropInfo(mouseEvent);
				caretParent = visualDropInfo.getDropContainer();
				caretOffset = visualDropInfo.getDropOffset();
			} else {
				String tagname = vpeController.getTagName(object);
				if (tagname.indexOf("taglib") >= 0)tagname = "taglib"; //$NON-NLS-1$ //$NON-NLS-2$
				Node sourceDragNode = ((Document) vpeController.getModel().getAdapter(
						Document.class)).createElement(tagname);
				VpeVisualInnerDropInfo visualDropInfo = vpeController.getSelectionBuilder()
						.getInnerDropInfo(mouseEvent);
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
			VpeVisualInnerDropInfo visualDropInfo = vpeController.getSelectionBuilder()
					.getInnerDropInfo(mouseEvent);
			caretParent = visualDropInfo.getDropContainer();
			caretOffset = visualDropInfo.getDropOffset();
			canDrop = true;
	
		}
		if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
			System.out
					.println("  canDrop: " + canDrop + (canDrop ? "  container: " + caretParent.getNodeName() + "  offset: " + caretOffset : "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		return new MozillaDropInfo(canDrop, caretParent, caretOffset);
	
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
