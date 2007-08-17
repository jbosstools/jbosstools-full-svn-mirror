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
import org.mozilla.interfaces.inIFlasher;
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
import org.mozilla.interfaces.nsIEditingSession;
import org.mozilla.interfaces.nsIInterfaceRequestor;
import org.mozilla.interfaces.nsISelection;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsITransferable;
import org.mozilla.xpcom.Mozilla;
import java.util.regex.Pattern;

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
	 * xpcom flasher component which used to  draw lines
	 */
	private inIFlasher iFlasher;
	
	//flasher contact id
	private static final String FLASHER_CIID="@mozilla.org/inspector/flasher;1";
	
	/**
	 * RegExp for find expression 'display : none' in style string
	 */
	private static final  Pattern  PATTERN = Pattern.compile(".*\\s*(display)\\s*:\\s*(none)\\s*;.*",Pattern.CASE_INSENSITIVE+Pattern.DOTALL);

	/**
	 * Contains attribute name for style
	 */
	private static final String STYLE_ATTR="style";

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
		return (nsITransferable) componentManager.createInstanceByContractID(XPCOM.NS_TRANSFERABLE_CONTRACTID, this, nsITransferable.NS_ITRANSFERABLE_IID);
	}

	/**
	 * Returns selection controller which used in selection functionality
	 * @return
	 */
	public nsISelection getSelection() {

//		try{
		nsIServiceManager serviceManager = Mozilla.getInstance().getServiceManager();
		nsIEditingSession editingSession = (nsIEditingSession) serviceManager.getServiceByContractID
		("@mozilla.org/editor/editingsession;1", nsIEditingSession.NS_IEDITINGSESSION_IID);
		nsIDOMWindow domWindow = getWebBrowser().getContentDOMWindow();
		nsISelection selection = domWindow.getSelection();
		return selection;
//		} catch(XPCOMException exception) {
//			exception.printStackTrace();
//		}
//		return null;
	}
	
	/**
	 * Function created to restore functionality of MozillaBrowser
	 * @return
	 */
	private nsIDOMElement getLastSelectedElement() {
		//TODO Max Areshkau selection functionality
	//return	(nsIDOMElement) getSelection().getAnchorNode().queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
		return null;
	}
	/**
	 * Function created to restore functionality of MozillaBrowser
	 * @return
	 */
	private void setLastSelectedElement(nsIDOMElement lastSelectedElement) {
		//TODO Max Areshkau selection functionality
	}
	/**
	 * 
	 */
	/**
	 * Draws rectangle around  the element.
	 * @param element
	 * @param resizerConstrains
	 * @param scroll
	 */
	public void setSelectionRectangle(nsIDOMElement element, int resizerConstrains, boolean scroll) {
		if (getIFlasher() == null) {
			
			return;
		}
		if (getLastSelectedElement() != null) {
			
			if(checkVisability(getLastSelectedElement())){
				
					if((getLastSelectedElement().getAttribute(VPEFLASHERCOLORATTRIBUTE)==null)||
					(!getLastSelectedElement().getAttribute(VPEFLASHERCOLORATTRIBUTE).equals(flasherHiddentElementColor))) {
				
						getIFlasher().setColor(flasherVisialElementColor);
					} else{
						getIFlasher().setColor(flasherHiddentElementColor);
					}
					
//					getIFlasher().repaintElement(getLastSelectedElement());
					getIFlasher().drawElementOutline(getLastSelectedElement());
			}else {
				
				getIFlasher().setColor(flasherHiddentElementColor);
				nsIDOMElement domElement = findVisbleParentElement(getLastSelectedElement());
				
				if(domElement!=null) {
					getIFlasher().drawElementOutline(getLastSelectedElement());				
//					getIFlasher().repaintElement(domElement);
				}
				
			}

		} else if (element != null) {
			
			if(checkVisability(element)){
				
			
				if((element.getAttribute(VPEFLASHERCOLORATTRIBUTE)==null)||
					(!element.getAttribute(VPEFLASHERCOLORATTRIBUTE).equals(flasherHiddentElementColor))) {
				
				getIFlasher().setColor(flasherVisialElementColor);
				}else {
					getIFlasher().setColor(flasherHiddentElementColor);
				}
				getIFlasher().drawElementOutline(element);
			}else {
				
				getIFlasher().setColor(flasherHiddentElementColor);
				nsIDOMElement domElement = findVisbleParentElement(element);
				
				if(domElement!=null) {
					
					getIFlasher().drawElementOutline(domElement);
				}
			}

			
		}
		//TODO Alexey Yukhovich resized functionality
//		if (resizer != null) {
//			if (element != null && resizerConstrains != 0) {
//				resizer.Show(element, resizerConstrains);
//			} else {
//				resizer.Hide();
//			}
//		}

		setLastSelectedElement(element);
//		lastSelectedElement = element;
//		lastResizerConstrains = resizerConstrains;
	}
	

	/**
	 * @return the iFlasher
	 */
	private inIFlasher getIFlasher() {
		
		if(iFlasher==null) {
			nsIServiceManager serviceManager = Mozilla.getInstance().getServiceManager();
			iFlasher = (inIFlasher) serviceManager.getServiceByContractID(FLASHER_CIID, inIFlasher.INIFLASHER_IID);
			iFlasher.setThickness(2);
		}
		return iFlasher;
	}
	
	/**Function created for checking if user can see element or not.
	 * Element doesn't shows in VPE if it's has 'display:none;' attribute in style.
	 * 
	 * @param node for checking it's visability
	 * @param iFlasher flasher which used for drawning border for elements  adn in 
	 * 				which was setted color in depends of visability of element
	 * 
	 * @return false for hiddent elements and true for visble elements
	 */
	private boolean checkVisability(nsIDOMNode node){
		
		if(!(node instanceof nsIDOMElement)){		
			
			return true;
		}
		nsIDOMElement domElement = (nsIDOMElement) node;
		
		//TODO  add check not inline styles attribute such as styleclass
		String inlineStyle = domElement.getAttribute(STYLE_ATTR);
		
			return inlineStyle==null?true:!PATTERN.matcher(inlineStyle).matches();
	}
	
	/**
	 * Finds visible nearest visible node for hidden node
	 * 
	 * @param element
	 * 
	 * @return nearest visible node or null if can't find
	 */
	private nsIDOMElement  findVisbleParentElement(nsIDOMElement element) {
		
		if(!(element.getParentNode() instanceof nsIDOMElement)) {
			
			return null;
		}
	
		nsIDOMElement parentElement = (nsIDOMElement) element.getParentNode();
		
		while(parentElement!=null&&!checkVisability(parentElement)) {
			if(checkVisability(parentElement)) {
			
				return parentElement;
			}else {
			
				parentElement=(nsIDOMElement) parentElement.getParentNode() ;
			}
		}
		return parentElement;
	}

}
