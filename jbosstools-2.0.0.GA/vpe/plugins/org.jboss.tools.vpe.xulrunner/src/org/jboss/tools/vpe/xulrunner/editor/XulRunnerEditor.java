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

import java.util.regex.Pattern;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.jboss.tools.vpe.xulrunner.BrowserPlugin;
import org.jboss.tools.vpe.xulrunner.XPCOM;
import org.jboss.tools.vpe.xulrunner.XulRunnerException;
import org.jboss.tools.vpe.xulrunner.browser.XulRunnerBrowser;
import org.mozilla.interfaces.inIFlasher;
import org.mozilla.interfaces.nsIBaseWindow;
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
import org.mozilla.interfaces.nsISelection;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsITransferable;
import org.mozilla.xpcom.Mozilla;
import org.mozilla.xpcom.XPCOMException;

/**
 * @author Sergey Vasilyev (svasilyev@exadel.com)
 *
 */
public class XulRunnerEditor extends XulRunnerBrowser {
	/** IVpeResizeListener */
	private IVpeResizeListener resizeListener;

	/** IXulRunnerVpeResizer */
	private IXulRunnerVpeResizer xulRunnerVpeResizer;
	
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
	private nsIDocShell docShell = null;
	
	/**
	 * RegExp for find expression 'display : none' in style string
	 */
	private static final  Pattern  PATTERN = Pattern.compile(".*\\s*(display)\\s*:\\s*(none)\\s*;.*",Pattern.CASE_INSENSITIVE+Pattern.DOTALL);

	/**
	 * Contains attribute name for style
	 */
	private static final String STYLE_ATTR="style";

	private nsIDOMElement lastSelectedElement;
	private int lastResizerConstrains;
	/**
	 *  Scroll selection into view flag
	 */
	private boolean scrollRegtangleFlag = false;

	/**
	 * @param parent
	 * @throws XulRunnerException
	 */
	public XulRunnerEditor(Composite parent) throws XulRunnerException {
		super(parent);
		
		Listener eventListenet = new Listener() {

			public void handleEvent(Event event) {
				Display.getCurrent().asyncExec(new Thread(){
					public void run(){
						showSelectionRectangle();
					}
				});
			}};
//			addListener(SWT.Activate, eventListenet);
			addListener(SWT.Paint, eventListenet);
			//Commented by Max Areshkau (bug on Mac OS X10.4 
			//when switch from visual to preview selection rectangle doen't disappear
//			addListener(SWT.Resize, eventListenet);
			addListener(SWT.Show, eventListenet);
			addListener(SWT.FocusIn, eventListenet);
			//Commented by Max Areshkau (bug on Mac OS X10.4 
			//when switch from visual to preview selection rectangle doen't disappear
//			addListener(SWT.FocusOut, eventListenet);
			addListener(SWT.Selection, eventListenet);
			addListener(SWT.Paint, eventListenet);
		
		resizeListener = new IVpeResizeListener() {
			public void onEndResizing(int usedResizeMarkerHandle, int top,
					int left, int width, int height,
					nsIDOMElement resizedDomElement) {
				endResizing(usedResizeMarkerHandle, top, left, width, height, resizedDomElement);
			}

			public nsISupports queryInterface(String uuid) {
				return null;
			}
		};

		
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
	
	public void onLoadWindow() {
	    if (getIXulRunnerVpeResizer() != null) {
	    	getIXulRunnerVpeResizer().init(getDOMDocument());
	    	getIXulRunnerVpeResizer().addResizeListener(resizeListener);
	    }

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
		if (docShell == null) {
			nsIInterfaceRequestor interfaceRequestor = (nsIInterfaceRequestor) getWebBrowser().queryInterface(nsIInterfaceRequestor.NS_IINTERFACEREQUESTOR_IID);
			docShell = (nsIDocShell) interfaceRequestor.getInterface(nsIDocShell.NS_IDOCSHELL_IID);
		}
		
		return docShell;
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

		nsIDOMWindow domWindow = getWebBrowser().getContentDOMWindow();
		nsISelection selection = domWindow.getSelection();
		return selection;
	}
	
	/**
	 * Function created to restore functionality of MozillaBrowser
	 * @return
	 */
	public nsIDOMElement getLastSelectedElement() {

		return lastSelectedElement;
	}
	/**
	 * Function created to restore functionality of MozillaBrowser
	 * @return
	 */
	private void setLastSelectedElement(nsIDOMElement lastSelectedElement) {
		this.lastSelectedElement = lastSelectedElement;
	}

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
			
			scrollRegtangleFlag = scroll && element != null;
			
			if(checkVisability(getLastSelectedElement())){
				
					if((getLastSelectedElement().getAttribute(VPEFLASHERCOLORATTRIBUTE)==null)||
					(!getLastSelectedElement().getAttribute(VPEFLASHERCOLORATTRIBUTE).equals(flasherHiddentElementColor))) {
				
						getIFlasher().setColor(flasherVisialElementColor);
					} else{
						getIFlasher().setColor(flasherHiddentElementColor);
					}
					
					getIFlasher().repaintElement(getLastSelectedElement());

			}else {
				
				getIFlasher().setColor(flasherHiddentElementColor);
				nsIDOMElement domElement = findVisbleParentElement(getLastSelectedElement());
				
				if(domElement!=null) {
			
					getIFlasher().repaintElement(domElement);
				}
				
			}

		} else if (element != null) {
			
			if (scroll) {
				scrollToElement(element);
				scrollRegtangleFlag = true;
			}
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

		if (xulRunnerVpeResizer != null) {
			if (element != null && resizerConstrains != 0) {
				xulRunnerVpeResizer.show(element, resizerConstrains);
			} else {
				xulRunnerVpeResizer.hide();
			}
		}

		setLastSelectedElement(element);
		lastResizerConstrains = resizerConstrains;
	}
	

	/**
	 * @return the iFlasher
	 */
	private inIFlasher getIFlasher() {
		
		if(iFlasher==null) {
			nsIServiceManager serviceManager = Mozilla.getInstance().getServiceManager();
			iFlasher = (inIFlasher) serviceManager.getServiceByContractID(XPCOM.IN_FLASHER_CONTRACTID, inIFlasher.INIFLASHER_IID);
			iFlasher.setThickness(2);
		}
		return iFlasher;
	}
	
	
	private IXulRunnerVpeResizer getIXulRunnerVpeResizer() {
		
		if (xulRunnerVpeResizer==null) {
			xulRunnerVpeResizer = new XulRunnerVpeResizer();
		}
		return xulRunnerVpeResizer;
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
		
		nsIDOMElement domElement;
		try{
			
		domElement = (nsIDOMElement) node.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
		
		} catch(XPCOMException exception) {
		//if we can cast it's is invisible elenebt
			return false;
		}
		
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
		
		nsIDOMElement parentElement;
		
	try {
		
		 parentElement = (nsIDOMElement) element.getParentNode().queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
	} catch (XPCOMException ex) {
		// if parent node isn't nsIDOMElement just return null;
		return null;
	}
		while(parentElement!=null&&!checkVisability(parentElement)) {
			if(checkVisability(parentElement)) {
			
				return parentElement;
			}else {
			
				parentElement=(nsIDOMElement) parentElement.getParentNode() ;
			}
		}
		return parentElement;
	}

	/**
	 * @param usedHandle
	 * @param newTop
	 * @param newLeft
	 * @param newWidth
	 * @param newHeight
	 * @param aResizedObject
	 */
	private void endResizing(int usedHandle, int newTop, int newLeft, int newWidth, int newHeight, nsIDOMElement aResizedObject) {
		onElementResize(aResizedObject, usedHandle, newTop, newLeft, newWidth, newHeight);
	}
	
	/**
	 * 
	 */
	public void showResizer() {
		if (xulRunnerVpeResizer != null && lastSelectedElement != null && lastResizerConstrains != 0) {
			xulRunnerVpeResizer.show(lastSelectedElement, lastResizerConstrains);
		}
	}

	/**
	 * Hide resizer markers
	 */
	public void hideResizer() {
		if(xulRunnerVpeResizer != null) {
			xulRunnerVpeResizer.hide();
		}
	}
	
	public void showSelectionRectangle() {
	
//		((nsIBaseWindow)getWebBrowser().queryInterface(nsIBaseWindow.NS_IBASEWINDOW_IID)).repaint(false);

		if (getIFlasher() != null && getLastSelectedElement() != null) {
			if (scrollRegtangleFlag) {
				scrollRegtangleFlag = false;
					
					scrollToElement(getLastSelectedElement());
			} 
			//checks visability of element
			if(checkVisability(getLastSelectedElement())){
				
				if((getLastSelectedElement().getAttribute(VPEFLASHERCOLORATTRIBUTE)==null)||
							(!getLastSelectedElement().getAttribute(VPEFLASHERCOLORATTRIBUTE).equals(flasherHiddentElementColor))) {
				
				getIFlasher().setColor(flasherVisialElementColor);
				}else{
					
					getIFlasher().setColor(flasherHiddentElementColor);
				}
					
				getIFlasher().drawElementOutline(getLastSelectedElement());
			}else {
				
				getIFlasher().setColor(flasherHiddentElementColor);
				nsIDOMElement domElement = findVisbleParentElement(getLastSelectedElement());
				
				if(domElement!=null) {
					getIFlasher().drawElementOutline(domElement);
				}
			}
		} else if(getIFlasher()!=null&&Platform.getOSArch().equals(Platform.OS_MACOSX)){
			//Max Areshkau (bug on Mac OS X, when we switch to preview from other view, selection rectangle doesn't disappear
			//TODO Max Areshkau (may be exist passability not draw selection on resize event when we switches to other view)
			try {
			((nsIBaseWindow)getWebBrowser().queryInterface(nsIBaseWindow.NS_IBASEWINDOW_IID)).repaint(true);
			} catch(XPCOMException ex) {
				//just ignore its
				BrowserPlugin.getDefault().logInfo("repaint failed", ex);
			}
		}
	}
	/**
	 * Scrools viiew to some elements
	 * @param element -element to which we should scroll 
	 */
	private void scrollToElement(nsIDOMElement element){
		
		getIFlasher().scrollElementIntoView(element);
	}
}


