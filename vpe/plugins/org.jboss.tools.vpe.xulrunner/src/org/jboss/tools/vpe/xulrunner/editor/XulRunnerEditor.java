/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.vpe.xulrunner.editor;

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.vpe.xulrunner.XPCOM;
import org.jboss.tools.vpe.xulrunner.XulRunnerException;
import org.jboss.tools.vpe.xulrunner.browser.XulRunnerBrowser;
import org.mozilla.interfaces.nsIClipboardDragDropHookList;
import org.mozilla.interfaces.nsIComponentManager;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMDocumentRange;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMRange;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsIDocShell;
import org.mozilla.interfaces.nsIDragService;
import org.mozilla.interfaces.nsIDragSession;
import org.mozilla.interfaces.nsIInterfaceRequestor;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsITransferable;

/**
 * @author Sergey Vasilyev (svasilyev@exadel.com)
 *
 */
public class XulRunnerEditor extends XulRunnerBrowser {
	/**
	 * color which used for highlight elements which user can see
	 */
	public static final String flasherVisialElementColor = "blue";
	
	/**
	 * color which used for highlight parent elements for elements which user 
	 * can't see. 
	 */
	public static final String flasherHiddentElementColor = "red";
	
	/**
	 * Contains name of attribute for inIFLasher drawing
	 */
	public static String VPEFLASHERCOLORATTRIBUTE="vpeFlasherColorAttribute";

	public static final String TRANS_FLAVOR_kHTMLMime = "text/html";
	public static final String TRANS_FLAVOR_kURLDataMime = "text/x-moz-url-data";
	public static final String TRANS_FLAVOR_kFileMime = "application/x-moz-file";
	public static final String TRANS_FLAVOR_kURLMime = "text/x-moz-url";
	public static final String TRANS_FLAVOR_kUnicodeMime = "text/unicode";
	public static final String TRANS_FLAVOR_kNativeHTMLMime = "application/x-moz-nativehtml";
	
	/**
	 * @param parent
	 * @throws XulRunnerException
	 */
	public XulRunnerEditor(Composite parent) throws XulRunnerException {
		super(parent);
	}

	public boolean isMozillaDragFlavor() {
		
		nsIDragSession dragSession = getCurrentDragSession();
		if (dragSession != null) {
			nsITransferable transferable = createTransferable();
			if (transferable != null) {
//				transferable.flavorsTransferableCanImport();

				transferable.addDataFlavor(TRANS_FLAVOR_kURLDataMime);
				transferable.addDataFlavor(TRANS_FLAVOR_kFileMime);
				transferable.addDataFlavor(TRANS_FLAVOR_kURLMime);
				transferable.addDataFlavor(TRANS_FLAVOR_kUnicodeMime);
				dragSession.getData(transferable, 0);

//				transferable.flavorsTransferableCanImport();
				String[] flavors = new String[] {null};
				nsISupports[] data = new nsISupports[] {null};
				long[] length = new long[] {0};
				transferable.getAnyTransferData(flavors, data, length);
				
				return length[0] > 0;
			}
		}
		return false;
	}
	
	public void onElementResize(nsIDOMElement element, int handle, int top, int left, int width, int height) {
	}
	
	public nsIDragSession getCurrentDragSession() {
		nsIServiceManager serviceManager = getServiceManager();
		nsIDragService dragService = (nsIDragService) serviceManager.getServiceByContractID(XPCOM.NS_DRAGSERVICE_CONTRACTID, nsIDragService.NS_IDRAGSERVICE_IID);
		
		return dragService.getCurrentSession();
	}
	
	public nsIClipboardDragDropHookList getClipboardDragDropHookList() {
		nsIDocShell docShell = getDocShell();
		
		if (docShell != null) {
			nsIClipboardDragDropHookList hookList = (nsIClipboardDragDropHookList) docShell.queryInterface(nsIClipboardDragDropHookList.NS_ICLIPBOARDDRAGDROPHOOKLIST_IID);
			return hookList;
		}
		return null;
	}
	
	public nsIDocShell getDocShell() {
		nsIInterfaceRequestor interfaceRequestor = (nsIInterfaceRequestor) getWebBrowser().queryInterface(nsIInterfaceRequestor.NS_IINTERFACEREQUESTOR_IID);
		return (nsIDocShell) interfaceRequestor.getInterface(nsIDocShell.NS_IDOCSHELL_IID);
	}
	
	public nsIDOMDocument getDOMDocument() {
		nsIDOMWindow domWindow = getWebBrowser().getContentDOMWindow();
		return domWindow.getDocument();
	}
	
	public nsIDOMDocumentRange getDOMDocumentRange() {
		return (nsIDOMDocumentRange) getDOMDocument().queryInterface(nsIDOMDocumentRange.NS_IDOMDOCUMENTRANGE_IID);
	}
	
	public nsIDOMRange createDOMRange() {
		return getDOMDocumentRange().createRange();
	}
	
	public void showDragCaret(nsIDOMNode node, long offcet) {
		// TODO Sergey Vasilyev figure out with caret
		System.out.println("Show drag caret for " + node.getNodeName() + ":" + offcet);
	}
	
	public void hideDragCaret() {
		// TODO Sergey Vasilyev figure out with caret
		System.out.println("Hide drag caret");
	}
	
	public nsITransferable createTransferable() {
		nsIComponentManager componentManager = getComponentManager();
		return (nsITransferable) componentManager.createInstanceByContractID(XPCOM.NS_ITRANSFERABLE_CONTRACTID, this, nsITransferable.NS_ITRANSFERABLE_IID);
	}
}
