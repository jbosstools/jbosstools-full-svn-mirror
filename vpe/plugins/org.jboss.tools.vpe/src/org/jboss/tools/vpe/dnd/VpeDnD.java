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

import org.eclipse.swt.graphics.Rectangle;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.mozilla.EditorDomEventListener;
import org.jboss.tools.vpe.editor.mozilla.MozillaDropInfo;
import org.mozilla.interfaces.nsIComponentManager;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNSHTMLElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDragService;
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
	
	private static final String CID_DRAGSERVICE = "@mozilla.org/widget/dragservice;1";
	private static final String CID_TRANSFERABLE = "@mozilla.org/widget/transferable;1";
	private static final String CID_SUPPORTSSTRING = "@mozilla.org/supports-string;1";

	private static final String CID_SUPPORTSARRAY = "@mozilla.org/supports-array;1";
	
	private static final String kVpeModelFlavor = "vpe/model";
	private static final String kVpeModelTransport ="vpe/model";
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

	public Rectangle getBounds(nsIDOMNode visualNode) {
		try {
			
		nsIDOMNSHTMLElement domNSHTMLElement = (nsIDOMNSHTMLElement) visualNode.queryInterface(nsIDOMNSHTMLElement.NS_IDOMNSHTMLELEMENT_IID);
		return new Rectangle(domNSHTMLElement.getOffsetLeft(), domNSHTMLElement.getOffsetTop(),domNSHTMLElement.getOffsetWidth(),domNSHTMLElement.getOffsetHeight());
		
		} catch(XPCOMException xpcomException) {
			
			//TODO Max Areshkau 
			//If node not not implement nsIDOMNSHTMLElement, may be check best take a parent node 
			return new Rectangle(0, 0, 0,0);
		}
	}
	
	/**
	 * Starts drag session
	 * @param dragetElement
	 */
	public void startDragSession(nsIDOMEvent  domEvent) {
		//TODO Max Areshkau 
		nsISupportsArray transArray = (nsISupportsArray) getComponentManager()
		.createInstanceByContractID(CID_SUPPORTSARRAY, null,
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
		
		nsITransferable iTransferable = (nsITransferable) componentManager
						.createInstanceByContractID(CID_TRANSFERABLE, null,
								nsITransferable.NS_ITRANSFERABLE_IID);
		nsISupportsString transferData = (nsISupportsString) componentManager
		.createInstanceByContractID(CID_SUPPORTSSTRING, null,
				nsISupportsString.NS_ISUPPORTSSTRING_IID);
		String data="vpe-element";
		transferData.setData(data);
		iTransferable.setTransferData("text/plain", transferData, data.length());
		iTransferable.setTransferData("text/unicode", transferData,data.length()*2);
		iTransferable.setTransferData("text/html", transferData, data.length()*2);
		iTransferable.setTransferData("text/xml", transferData, data.length()*2);
		iTransferable.setTransferData("text/rtf", transferData, data.length()*2);
		iTransferable.setTransferData("text/enriched", transferData, data.length()*2);
		iTransferable.setTransferData("text/richtext", transferData, data.length()*2);
		iTransferable.setTransferData("text/t140", transferData, data.length()*2);
		
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
			.getServiceByContractID(CID_DRAGSERVICE,
					nsIDragService.NS_IDRAGSERVICE_IID);
		}
		return dragService;
	}
	/**
	 * Calls when drag over event ocure
	 * @param event
	 */
	public void dragOver(nsIDOMEvent event, EditorDomEventListener editorDomEventListener) {
		boolean canDrop = false;
		
		nsIDOMMouseEvent mouseEvent = (nsIDOMMouseEvent) event.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
		//in this condition  early was check for xulelement
		if (editorDomEventListener != null) {
			if (getDragService().getCurrentSession().isDataFlavorSupported(VpeController.MODEL_FLAVOR)) {
				MozillaDropInfo info = editorDomEventListener.canExternalDrop(mouseEvent, VpeController.MODEL_FLAVOR, "");
				if (info != null) {
					canDrop = info.canDrop();
				}
			}
		}
		//sets possability to drop current element here
		System.out.println("["+canDrop+"]");
		getDragService().getCurrentSession().setCanDrop(canDrop);
		mouseEvent.preventDefault();
		mouseEvent.stopPropagation();
		
	}

}
