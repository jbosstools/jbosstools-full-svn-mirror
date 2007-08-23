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
import org.mozilla.interfaces.nsIComponentManager;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMNSHTMLElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDragService;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.xpcom.Mozilla;
import org.mozilla.xpcom.XPCOMException;

/**
 * @author Max Areshkau
 *
 *Class which response for drag and drop functionality
 */
public class VpeDnD {
	
	private static final String CID_DRAGSERVICE="@mozilla.org/widget/dragservice;1";
	private static final String CID_SUPPORTSARRAY = "@mozilla.org/supports-array;1";
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
			dragService = (nsIDragService) serviceManager
			.getServiceByContractID(CID_DRAGSERVICE,
					nsIDragService.NS_IDRAGSERVICE_IID);
		}
		return dragService;
	}

}
