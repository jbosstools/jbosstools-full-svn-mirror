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

import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.VpeVisualCaretInfo;
import org.jboss.tools.vpe.editor.mozilla.MozillaDropInfo;
import org.jboss.tools.vpe.editor.selection.VpeSelectionController;
import org.jboss.tools.vpe.xulrunner.XPCOM;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIComponentManager;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMEventTarget;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDragService;
import org.mozilla.interfaces.nsISelectionController;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.interfaces.nsISupportsArray;
import org.mozilla.interfaces.nsISupportsString;
import org.mozilla.interfaces.nsITransferable;
import org.mozilla.xpcom.Mozilla;
import org.mozilla.xpcom.XPCOMException;

/**
 * @author Max Areshkau
 *
 *Class which response for drag and drop functionality
 */
public class VpeDnD {
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


	public VpeDnD(VpeController vpeController) {
		this.vpeController = vpeController;
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
	}

	private void refreshCanDrop(nsIDOMEvent event) {
		boolean canDrop = true;

		nsIDOMMouseEvent mouseEvent = (nsIDOMMouseEvent) event.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
		//in this condition  early was check for xulelement
		if (getDragService().getCurrentSession().isDataFlavorSupported(VpeController.MODEL_FLAVOR)) {
			MozillaDropInfo info;

			if(getDragService().getCurrentSession().getSourceNode()==null){
				//external drag 
				  info = vpeController.canExternalDrop(mouseEvent, VpeController.MODEL_FLAVOR, ""); //$NON-NLS-1$
			} else {
			    //internal drag
				 info = vpeController.canInnerDrop(mouseEvent);
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
	/**
	 * Drop Event handler
	 * @param domEvent
	 * @param vpeController
	 */
	public void dragDrop(nsIDOMEvent domEvent) {
		if(getDragService().getCurrentSession().getSourceDocument()==null) {
			//in this case it's is  external drag
			vpeController.externalDrop((nsIDOMMouseEvent)domEvent.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID), VpeController.MODEL_FLAVOR, ""); //$NON-NLS-1$
		} else {
			// in this case it's is an internal drag
			vpeController.innerDrop((nsIDOMMouseEvent)domEvent.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID));
		}
	}
}
