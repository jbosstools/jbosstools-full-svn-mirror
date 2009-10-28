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
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
import org.mozilla.interfaces.nsISelectionListener;
import org.mozilla.interfaces.nsISelectionPrivate;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsITooltipListener;
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
	 * color which used for highlight elements which user can see, blue color
	 */
	public static final String flasherVisialElementColor = "#0000ff"; //$NON-NLS-1$
	
	/**
	 * color which used for highlight parent elements for elements which user, red color
	 * can't see. 
	 */
	public static final String flasherHiddentElementColor = "#ff0000"; //$NON-NLS-1$
	
	//added by Maksim Areshkau as element for which we 
	//have drowed border. When we draw new border,
	//we should remove old one;
	private nsIDOMElement lastBorderedElement;
	private static final String INVISIBLE_ELEMENT_BORDER = "border: 2px solid red;";//$NON-NLS-1$
	private static final String VISIBLE_ELEMENT_BORDER = "border: 2px solid blue;";//$NON-NLS-1$
	private static final String PREV_STYLE_ATTR_NAME = "oldstyle";//$NON-NLS-1$
	
	/**
	 * Contains name of attribute for inIFLasher drawing
	 */
	public static String VPEFLASHERCOLORATTRIBUTE="vpeFlasherColorAttribute"; //$NON-NLS-1$

	public static final String TRANS_FLAVOR_kHTMLMime = "text/html"; //$NON-NLS-1$
	public static final String TRANS_FLAVOR_kURLDataMime = "text/x-moz-url-data"; //$NON-NLS-1$
	public static final String TRANS_FLAVOR_kFileMime = "application/x-moz-file"; //$NON-NLS-1$
	public static final String TRANS_FLAVOR_kURLMime = "text/x-moz-url"; //$NON-NLS-1$
	public static final String TRANS_FLAVOR_kUnicodeMime = "text/unicode"; //$NON-NLS-1$
	public static final String TRANS_FLAVOR_kNativeHTMLMime = "application/x-moz-nativehtml"; //$NON-NLS-1$
	
	/**
	 * xpcom flasher component which used to  draw lines
	 */
	private inIFlasher iFlasher;
	private nsIDocShell docShell = null;
	
	/**
	 * RegExp for find expression 'display : none' in style string
	 */
	private static final  Pattern  PATTERN = Pattern.compile(".*\\s*(display)\\s*:\\s*(none)\\s*;.*",Pattern.CASE_INSENSITIVE+Pattern.DOTALL); //$NON-NLS-1$

	/**
	 * Contains attribute name for style
	 */
	private static final String STYLE_ATTR="style"; //$NON-NLS-1$

//	private nsIDOMElement lastSelectedElement;
	private nsIDOMNode lastSelectedNode;
	private int lastResizerConstrains;
	/**
	 *  Scroll selection into view flag
	 */
	private boolean scrollRegtangleFlag = false;

	private nsISelectionListener selectionListener;

	private Listener eventListenet = new Listener() {

		public void handleEvent(Event event) {
			Display.getCurrent().asyncExec(new Thread(){
				@Override
				public void run(){
				    /*
				     * https://jira.jboss.org/jira/browse/JBIDE-3917
				     * Resizer should be updated together with selection rectangle.
				     * Otherwise after window maximizing/restoring resizer shows old position.
				     */
					if(getBrowser()!=null && !getBrowser().isDisposed()) {
					    showResizer();
					    showSelectionRectangle();
					}
				}
			});
		}};
	/**
	 * @param parent
	 * @throws XulRunnerException
	 */
	public XulRunnerEditor(Composite parent) throws XulRunnerException {
		super(parent);
		getBrowser().addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				//TODO Max Areshkau this caused en error when we close editor under Mac OS
//				getWebBrowser().removeWebBrowserListener(XulRunnerEditor.this, nsIWebProgressListener.NS_IWEBPROGRESSLISTENER_IID);
				getWebBrowser().removeWebBrowserListener(XulRunnerEditor.this, nsITooltipListener.NS_ITOOLTIPLISTENER_IID);
				removeSelectionListener();
				if (resizeListener != null)
					getIXulRunnerVpeResizer().removeResizeListener(resizeListener);
				xulRunnerVpeResizer.dispose();
				xulRunnerVpeResizer = null;
				resizeListener = null;
				if (eventListenet != null) {
					removeListener(SWT.Paint, eventListenet);
					removeListener(SWT.Show, eventListenet);
					removeListener(SWT.FocusIn, eventListenet);
					removeListener(SWT.Selection, eventListenet);
					removeListener(SWT.Paint, eventListenet);
					eventListenet = null;
				}
				getBrowser().removeDisposeListener(this);
				onDispose();
			}
			
		});
		
//			addListener(SWT.Activate, eventListenet);
			addListener(SWT.Paint, eventListenet);
			/*
			 * https://jira.jboss.org/jira/browse/JBIDE-3917
			 * Resizer and selection rectangle should be updated
			 * after eclipse window resizing. 
			 * Need to test on Mac OS. 
			 */
			//Commented by Max Areshkau (bug on Mac OS X10.4 
			//when switch from visual to preview selection rectangle doen't disappear
			addListener(SWT.Resize, eventListenet);
			
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
	
	/**
	 * Removes resizer listener
	 */
	public void removeResizerListener() {
		if (resizeListener != null)
			getIXulRunnerVpeResizer().removeResizeListener(resizeListener);
	}
	/**
	 * Add Resizer Listener
	 */
	public void addResizerListener() {
	    if (getIXulRunnerVpeResizer() != null) {
	    	getIXulRunnerVpeResizer().init(getDOMDocument());
	    	getIXulRunnerVpeResizer().addResizeListener(resizeListener);
	    }
	}
	
	public void onLoadWindow() {
		addResizerListener();
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
		System.out.println("Show drag caret for " + node.getNodeName() + ":" + offcet); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void hideDragCaret() {
		// TODO Sergey Vasilyev figure out with caret
		System.out.println("Hide drag caret"); //$NON-NLS-1$
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

		return getElement(lastSelectedNode);
	}
	
	public nsIDOMNode getLastSelectedNode() {
		return lastSelectedNode;
	}

	// /**
	// * Function created to restore functionality of MozillaBrowser
	// * @return
	// */
	// private void setLastSelectedElement(nsIDOMElement lastSelectedElement) {
	// this.lastSelectedElement = lastSelectedElement;
	// }
	
	private void setLastSelectedNode(nsIDOMNode lastSelectedNode) {
		this.lastSelectedNode = lastSelectedNode;
	}

	/**
	 * Draws rectangle around  the element.
	 * @param element
	 * @param resizerConstrains
	 * @param scroll
	 */
	public void setSelectionRectangle(nsIDOMNode node, int resizerConstrains, boolean scroll) {
	    	if (getIFlasher() == null) {
			
			return;
		}
		
		nsIDOMElement element = getElement(node);
		
		if (getLastSelectedElement() != null) {
			
			scrollRegtangleFlag = scroll && node != null;
			
			try {
				((nsIBaseWindow) getWebBrowser().queryInterface(
						nsIBaseWindow.NS_IBASEWINDOW_IID)).repaint(true);
			} catch (XPCOMException ex) {
				// just ignore its
				BrowserPlugin.getDefault().logInfo("repaint failed", ex); //$NON-NLS-1$
			}
			if(checkVisability(getLastSelectedElement())){
				
					if((getLastSelectedElement().getAttribute(VPEFLASHERCOLORATTRIBUTE)==null)||
					(!getLastSelectedElement().getAttribute(VPEFLASHERCOLORATTRIBUTE).equals(flasherHiddentElementColor))) {
				
						getIFlasher().setColor(flasherVisialElementColor);
					} else{
						getIFlasher().setColor(flasherHiddentElementColor);
					}
					
					drawElementOutline(getLastSelectedElement());

			}else {
				
				getIFlasher().setColor(flasherHiddentElementColor);
				nsIDOMElement domElement = findVisbleParentElement(getLastSelectedElement());
				
				if(domElement!=null) {
			
					drawElementOutline(domElement);
				}
				
			}
//			try {
//				((nsIBaseWindow) getWebBrowser().queryInterface(
//						nsIBaseWindow.NS_IBASEWINDOW_IID)).repaint(true);
//			} catch (XPCOMException ex) {
//				// just ignore its
//				BrowserPlugin.getDefault().logInfo("repaint failed", ex); //$NON-NLS-1$
//			}

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
				drawElementOutline(element);
			}else {
				
				getIFlasher().setColor(flasherHiddentElementColor);
				nsIDOMElement domElement = findVisbleParentElement(element);
				
				if(domElement!=null) {
					
				drawElementOutline(domElement);
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

//		setLastSelectedElement(element);
		setLastSelectedNode(node);
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
		
		 parentElement = (nsIDOMElement) element.getParentNode()
					.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
	} catch (XPCOMException ex) {
		// if parent node isn't nsIDOMElement just return null;
		return null;
	}
		while(parentElement!=null&&!checkVisability(parentElement)) {
			if(checkVisability(parentElement)) {
			
				return parentElement;
			}else {
				
				 parentElement = (nsIDOMElement) parentElement.getParentNode()
						.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
				
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
		if (xulRunnerVpeResizer != null && getLastSelectedElement() != null && lastResizerConstrains != 0) {
			xulRunnerVpeResizer.show(getLastSelectedElement(), lastResizerConstrains);
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
					
				drawElementOutline(getLastSelectedElement());
			}else {
				
				getIFlasher().setColor(flasherHiddentElementColor);
				nsIDOMElement domElement = findVisbleParentElement(getLastSelectedElement());
				
				if(domElement!=null) {
					drawElementOutline(domElement);
				}
			}
		} 
//		else if(getIFlasher()!=null&&Platform.OS_MACOSX.equals(Platform.getOS())){
//			//Max Areshkau (bug on Mac OS X, when we switch to preview from other view, selection rectangle doesn't disappear
//			//TODO Max Areshkau (may be exist passability not draw selection on resize event when we switches to other view)
//			try {
//			((nsIBaseWindow)getWebBrowser().queryInterface(nsIBaseWindow.NS_IBASEWINDOW_IID)).repaint(true);
//			} catch(XPCOMException ex) {
//				//just ignore its
//				BrowserPlugin.getDefault().logInfo("repaint failed", ex); //$NON-NLS-1$
//			}
//		}
	}
	/**
	 * Scrools viiew to some elements
	 * @param element -element to which we should scroll 
	 */
	private void scrollToElement(nsIDOMElement element){
		
		getIFlasher().scrollElementIntoView(element);
	}

	/**
	 * Adds selection listener
	 * @param selectionListener
	 */
	public void addSelectionListener (
			nsISelectionListener selectionListener) {
		nsISelection selection = getSelection();
		nsISelectionPrivate selectionPrivate = (nsISelectionPrivate) selection.queryInterface(nsISelectionPrivate.NS_ISELECTIONPRIVATE_IID);
		selectionPrivate.addSelectionListener(selectionListener);
		this.selectionListener = selectionListener;
	}
	/**
	 * Removes selection listener
	 */
	public void removeSelectionListener() {
		if (this.selectionListener != null) {
			nsISelection selection = getSelection();
			nsISelectionPrivate selectionPrivate = (nsISelectionPrivate) selection.queryInterface(nsISelectionPrivate.NS_ISELECTIONPRIVATE_IID);
			try {
			selectionPrivate.removeSelectionListener(this.selectionListener);
			} catch (XPCOMException xpcomException) {
	        	//this exception throws when progress listener already has been deleted, 
	        	//so just ignore if error code NS_ERROR_FAILURE
				//mareshkau fix for jbide-3155
	        	if(xpcomException.errorcode!=XulRunnerBrowser.NS_ERROR_FAILURE) {
	        		throw xpcomException;
	        	}
			}
		}
		this.selectionListener=null;
	}
	/**
	 * get nsIDomElement from nsIDomNode
	 * 
	 * if node is nsIDomElement - return it 
	 * 
	 * if node is text node - return it's
	 * 
	 * parent else return null
	 * 
	 * @param node
	 * @return
	 */
	private nsIDOMElement getElement(nsIDOMNode node) {

		if (node != null) {

			if (node.getNodeType() == nsIDOMNode.ELEMENT_NODE) {
				return (nsIDOMElement) node
						.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
			} else if (node.getNodeType() == nsIDOMNode.TEXT_NODE) {

				return (nsIDOMElement) node.getParentNode().queryInterface(
						nsIDOMElement.NS_IDOMELEMENT_IID);

			}

		}

		return null;
	}
	
	/**
	 * Decorator
	 * @author mareshkau
	 * @param domElement arround which border will be shown 
	 * 
	 */
	private void drawElementOutline(nsIDOMElement domElement) {
		//fix for JBIDE-3969
//		if(Platform.OS_MACOSX.equals(Platform.getOS())&&hasSelectInParenNodes(domElement.getParentNode())) {
//			return;
//		}
				//restore style for previously bordered element
		if(this.lastBorderedElement!=null && this.lastBorderedElement.getAttribute(STYLE_ATTR)!=null) {
			String style = this.lastBorderedElement.getAttribute(PREV_STYLE_ATTR_NAME); 
			this.lastBorderedElement.removeAttribute(PREV_STYLE_ATTR_NAME);
			this.lastBorderedElement.setAttribute(STYLE_ATTR, style);
		}
		
		//save style for early bordered element
		String oldstyle = domElement.getAttribute(STYLE_ATTR);
		if(flasherHiddentElementColor.equals(getIFlasher().getColor())) {
				domElement.setAttribute(STYLE_ATTR,domElement.getAttribute(STYLE_ATTR)+';'+XulRunnerEditor.INVISIBLE_ELEMENT_BORDER);
		}else {
			domElement.setAttribute(STYLE_ATTR,domElement.getAttribute(STYLE_ATTR)+';'+ XulRunnerEditor.VISIBLE_ELEMENT_BORDER);
		}
		this.lastBorderedElement = domElement;
		this.lastBorderedElement.setAttribute(PREV_STYLE_ATTR_NAME, oldstyle);
		//under osx function drawElementOutline not works
		if(!Platform.OS_MACOSX.equals(Platform.getOS())){
			getIFlasher().drawElementOutline(domElement);
		}
	}
	/**
	 * Checks if node has select in parent node, if has it's cause crash 
	 * on OSX and xulrunner 1.8.1.3
	 * @param domElement
	 * @return
	 */
//	private boolean hasSelectInParenNodes(nsIDOMNode domNode){
//		if(domNode==null) {
//			return false;
//		}else if("select".equalsIgnoreCase(domNode.getNodeName())){ //$NON-NLS-1$
//			return true;
//		} else {
//			return hasSelectInParenNodes(domNode.getParentNode());
//		}
//	}
}


