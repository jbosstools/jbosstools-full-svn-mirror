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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.jboss.tools.vpe.xulrunner.BrowserPlugin;
import org.jboss.tools.vpe.xulrunner.XulRunnerException;
import org.jboss.tools.vpe.xulrunner.browser.XulRunnerBrowser;
import org.jboss.tools.vpe.xulrunner.util.XPCOM;
import org.mozilla.interfaces.nsIBaseWindow;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsITooltipListener;
import org.mozilla.xpcom.XPCOMException;

/**
 * @author Sergey Vasilyev (svasilyev@exadel.com)
 * 
 */
public class XulRunnerEditor extends XulRunnerBrowser {

	private VisualPaintListener paintListener;

	/** IVpeResizeListener */
	private IVpeResizeListener resizeListener;
	
	private List<IVpeSelectionListener> selectionListeners = new ArrayList<IVpeSelectionListener>();

	/** IXulRunnerVpeResizer */
	private IXulRunnerVpeResizer xulRunnerVpeResizer;

	
	/**
	 * color which used for highlight elements which user can see, blue color
	 */
	private static final String FLASHER_VISUAL_ELEMENT_COLOR = "#0000ff"; //$NON-NLS-1$

	/**
	 * color which used for highlight parent elements for elements which user,
	 * red color can't see.
	 */
	private static final String FLASHER_HIDDEN_ELEMENT_COLOR = "#ff0000"; //$NON-NLS-1$

	/**
	 * Contains name of attribute for inIFLasher drawing
	 */
	public static String VPE_INVISIBLE_ELEMENT = "vpeInvisibleElement"; //$NON-NLS-1$

	public static final String TRANS_FLAVOR_kHTMLMime = "text/html"; //$NON-NLS-1$
	public static final String TRANS_FLAVOR_kURLDataMime = "text/x-moz-url-data"; //$NON-NLS-1$
	public static final String TRANS_FLAVOR_kFileMime = "application/x-moz-file"; //$NON-NLS-1$
	public static final String TRANS_FLAVOR_kURLMime = "text/x-moz-url"; //$NON-NLS-1$
	public static final String TRANS_FLAVOR_kUnicodeMime = "text/unicode"; //$NON-NLS-1$
	public static final String TRANS_FLAVOR_kNativeHTMLMime = "application/x-moz-nativehtml"; //$NON-NLS-1$

	/**
	 * xpcom flasher component which used to draw lines
	 */
	private Flasher flasher;

	/**
	 * RegExp for find expression 'display : none' in style string
	 */
	private static final Pattern PATTERN = Pattern
			.compile(
					".*\\s*(display)\\s*:\\s*(none)\\s*;.*", Pattern.CASE_INSENSITIVE + Pattern.DOTALL); //$NON-NLS-1$

	/**
	 * Contains attribute name for style
	 */
	public static final String STYLE_ATTR = "style"; //$NON-NLS-1$

	// private nsIDOMElement lastSelectedElement;
	private List<nsIDOMNode> selectedNodes;
	private int lastResizerConstrains;

	private Listener eventListenet = new Listener() {

		public void handleEvent(Event event) {
			Display.getCurrent().asyncExec(new Thread() {
				@Override
				public void run() {
					/*
					 * https://jira.jboss.org/jira/browse/JBIDE-3917 Resizer
					 * should be updated together with selection rectangle.
					 * Otherwise after window maximizing/restoring resizer shows
					 * old position.
					 */
					if (getBrowser() != null && !getBrowser().isDisposed()) {
						showResizer();
						redrawSelectionRectangle();
					}
				}
			});
		}
	};

	/**
	 * @param parent
	 * @throws XulRunnerException
	 */
	public XulRunnerEditor(Composite parent) throws XulRunnerException {
		super(parent);
		getBrowser().addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				// TODO Max Areshkau this caused en error when we close editor
				// under Mac OS
				// getWebBrowser().removeWebBrowserListener(XulRunnerEditor.this,
				// nsIWebProgressListener.NS_IWEBPROGRESSLISTENER_IID);
				if (paintListener != null) {
					getBrowser().getParent().removePaintListener(paintListener);
					paintListener = null;
				}
				getWebBrowser().removeWebBrowserListener(XulRunnerEditor.this,
						nsITooltipListener.NS_ITOOLTIPLISTENER_IID);
				removeProgressListener(XulRunnerEditor.this);

				if (resizeListener != null) {
					getIXulRunnerVpeResizer().removeResizeListener(resizeListener);
				}
				xulRunnerVpeResizer.dispose();
				xulRunnerVpeResizer = null;
				resizeListener = null;
				if (eventListenet != null) {
					removeListener(SWT.Paint, eventListenet);
					removeListener(SWT.Show, eventListenet);
					removeListener(SWT.FocusIn, eventListenet);
					removeListener(SWT.Selection, eventListenet);
					removeListener(SWT.Resize, eventListenet);
					eventListenet = null;
				}
				getBrowser().removeDisposeListener(this);
				onDispose();
			}

		});
		// Part of fix https://jira.jboss.org/jira/browse/JBIDE-4022
		paintListener = new VisualPaintListener();
		getBrowser().getParent().addPaintListener(paintListener);
		// addListener(SWT.Activate, eventListenet);
		// addListener(SWT.Paint, eventListenet);
		/*
		 * https://jira.jboss.org/jira/browse/JBIDE-3917 Resizer and selection
		 * rectangle should be updated after eclipse window resizing. Need to
		 * test on Mac OS.
		 */
		// Commented by Max Areshkau (bug on Mac OS X10.4
		// when switch from visual to preview selection rectangle doen't
		// disappear
		addListener(SWT.Resize, eventListenet);
		addListener(SWT.Show, eventListenet);
		addListener(SWT.FocusIn, eventListenet);
		// Commented by Max Areshkau (bug on Mac OS X10.4
		// when switch from visual to preview selection rectangle doen't
		// disappear
		// addListener(SWT.FocusOut, eventListenet);
		addListener(SWT.Selection, eventListenet);
		addListener(SWT.Paint, eventListenet);

		resizeListener = new IVpeResizeListener() {
			public void onEndResizing(int usedResizeMarkerHandle, int top,
					int left, int width, int height,
					nsIDOMElement resizedDomElement) {
				endResizing(usedResizeMarkerHandle, top, left, width, height,
						resizedDomElement);
			}

			public nsISupports queryInterface(String uuid) {
				return null;
			}
		};

	}

	public void onElementResize(nsIDOMElement element, int handle, int top,
			int left, int width, int height) {
	}

	/**
	 * Removes resize listener
	 */
	public void removeResizeListener() {
		if (resizeListener != null)
			getIXulRunnerVpeResizer().removeResizeListener(resizeListener);
	}

	/**
	 * Add Resize Listener
	 */
	public void addResizeListener() {
		if (getIXulRunnerVpeResizer() != null) {
			getIXulRunnerVpeResizer().init(getDOMDocument());
			getIXulRunnerVpeResizer().addResizeListener(resizeListener);
		}
	}
	
	public void addSelectionListener(IVpeSelectionListener selectionListener) {
		selectionListeners.add(selectionListener);
	}
	
	public void removeSelectionListener(IVpeSelectionListener selectionListener) {
		selectionListeners.remove(selectionListener);
	}
	
	private void fireSelectionListeners() {
		for (IVpeSelectionListener listener : selectionListeners) {
			listener.selectionChanged();
		}
	}
	
	public void onLoadWindow() {
		addResizeListener();
	}

	public nsIDOMDocument getDOMDocument() {
		nsIDOMWindow domWindow = getWebBrowser().getContentDOMWindow();
		return domWindow.getDocument();
	}

//	/**
//	 * Function created to restore functionality of MozillaBrowser
//	 * 
//	 * @return
//	 */
//	public nsIDOMElement getLastSelectedElement() {
//		return getElementForNode(lastSelectedNode);
//	}

	public List<nsIDOMNode> getSelectedNodes() {
		if(selectedNodes==null){
			selectedNodes=Collections.<nsIDOMNode>emptyList();
		}
		return selectedNodes;
	}

	/**
	 * Draws rectangle around the element.
	 * 
	 * @param element
	 * @param resizerConstrains
	 * @param scroll
	 */
	public void setSelectionRectangle(List<nsIDOMNode> nodes, int resizerConstrains) {
		if (getFlasher() == null) {
			return;
		}
		this.selectedNodes = nodes;

			nsIDOMElement element = getSelectedElement();
			if (element != null) {
				repaint();
				scrollToElement(element);			
			}
			redrawSelectionRectangle();

			if (xulRunnerVpeResizer != null) {
				if (element != null && resizerConstrains != 0) {
					xulRunnerVpeResizer.show(element, resizerConstrains);
				} else {
					xulRunnerVpeResizer.hide();
				}
			}
		

		lastResizerConstrains = resizerConstrains;
		
		fireSelectionListeners();
	}

	/**
	 * Forcible repaints current XULRunner window.
	 * 
	 * If an exception occurs during repaint, it will be logged.
	 */
	private void repaint() {
		try {
			XPCOM.queryInterface(getWebBrowser(), nsIBaseWindow.class).repaint(true);
		} catch (XPCOMException ex) {
			// just ignore it
			BrowserPlugin.getDefault().logInfo("repaint failed", ex); //$NON-NLS-1$
		}
	}

	/**
	 * @return the iFlasher
	 */
	private Flasher getFlasher() {

		if (flasher == null) {
			flasher = new Flasher();
		}
		return flasher;
	}

	private IXulRunnerVpeResizer getIXulRunnerVpeResizer() {

		if (xulRunnerVpeResizer == null) {
			xulRunnerVpeResizer = new XulRunnerVpeResizer();
		}
		return xulRunnerVpeResizer;
	}

	/**
	 * Function created for checking if the user can see element or not. Element
	 * isn't shown in VPE if it has 'display:none;' attribute in style.
	 * 
	 * @param node node to check its visibility
	 * @return {@code false} for hidden elements and {@code true}
	 * for visible elements
	 */
	private boolean isVisible(nsIDOMNode node) {
		nsIDOMElement domElement;
		try {
			domElement = XPCOM.queryInterface(node, nsIDOMElement.class);
		} catch (XPCOMException exception) {
			// if we can cast it's is invisible elenebt
			return false;
		}

		// TODO add check not inline styles attribute such as styleclass
		String inlineStyle = domElement.getAttribute(STYLE_ATTR);

		return inlineStyle == null ? true : !PATTERN.matcher(inlineStyle)
				.matches();
	}

	/**
	 * Finds visible nearest visible node for hidden node
	 * 
	 * @param element
	 * 
	 * @return nearest visible node or null if can't find
	 */
	private nsIDOMElement findVisibleParentElement(nsIDOMElement element) {

		nsIDOMElement parentElement;

		try {
			parentElement = XPCOM.queryInterface(element.getParentNode(), nsIDOMElement.class);
		} catch (XPCOMException ex) {
			// if parent node isn't nsIDOMElement just return null;
			return null;
		}
		
		while (parentElement != null && !isVisible(parentElement)) {
			if (isVisible(parentElement)) {
				return parentElement;
			} else {
				parentElement = XPCOM.queryInterface(parentElement.getParentNode(), nsIDOMElement.class);
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
	private void endResizing(int usedHandle, int newTop, int newLeft,
			int newWidth, int newHeight, nsIDOMElement aResizedObject) {
		onElementResize(aResizedObject, usedHandle, newTop, newLeft, newWidth,
				newHeight);
	}

	/**
	 * 
	 */
	public void showResizer() {
		if (xulRunnerVpeResizer != null && getSelectedElement() != null
				&& lastResizerConstrains != 0) {
			xulRunnerVpeResizer.show(getSelectedElement(),
					lastResizerConstrains);
		}
	}

	/**
	 * Hide resizer markers
	 */
	public void hideResizer() {
		if (xulRunnerVpeResizer != null) {
			xulRunnerVpeResizer.hide();
		}
	}

	public void redrawSelectionRectangle() {
//		nsIDOMElement element = get SelectedElement();
//		if (element != null) {
//			if (isVisible(element)) {
//				if (element.getAttribute(VPE_INVISIBLE_ELEMENT) == null
//						|| (!element.getAttribute(VPE_INVISIBLE_ELEMENT)
//								.equals(Boolean.TRUE.toString()))) {
//					getFlasher().setColor(FLASHER_VISUAL_ELEMENT_COLOR);
//				} else {
//					getFlasher().setColor(FLASHER_HIDDEN_ELEMENT_COLOR);
//				}			
//				drawElementOutline(element);
//			} else {
//				getFlasher().setColor(FLASHER_HIDDEN_ELEMENT_COLOR);
//				nsIDOMElement domElement = findVisibleParentElement(element);
//				if (domElement != null) {
//					drawElementOutline(domElement);
//				}
//			}
//		}
		List<FlasherData> flasherDatas = new ArrayList<FlasherData>();
		for (nsIDOMNode domNode : getSelectedNodes()) {
			flasherDatas.add(prepareFlasherData(domNode));
		}
		drawElementOutline(flasherDatas);
	}
	
	private FlasherData prepareFlasherData(nsIDOMNode domNode){
		nsIDOMElement domElement = getElementForNode(domNode);
		String selectionBorderColor = FLASHER_VISUAL_ELEMENT_COLOR;
		if (domElement != null) {
			if (isVisible(domElement)) {
				if (domElement.getAttribute(VPE_INVISIBLE_ELEMENT) == null
						|| (!domElement.getAttribute(VPE_INVISIBLE_ELEMENT)
								.equals(Boolean.TRUE.toString()))) {
					selectionBorderColor = FLASHER_VISUAL_ELEMENT_COLOR;
				} else {
					selectionBorderColor = FLASHER_HIDDEN_ELEMENT_COLOR;
				}			
			} else {
				selectionBorderColor = FLASHER_HIDDEN_ELEMENT_COLOR;
				domElement = findVisibleParentElement(domElement);
			}
		}
		
		return new FlasherData(selectionBorderColor, domElement);
	}
	
	

	/**
	 * Scrools viiew to some elements
	 * 
	 * @param element
	 *            -element to which we should scroll
	 */
	private void scrollToElement(nsIDOMElement element) {
		getFlasher().scrollElementIntoView(element);
	}

	/**
	 * Returns the element to be selected for the given {@code node}.
	 * 
	 * If node is an element, then returns it as is.
	 * If node is a text node, then returns its parent
	 * Else returns null;
	 */
	private static nsIDOMElement getElementForNode(nsIDOMNode node) {
		if (node != null) {
			if (node.getNodeType() == nsIDOMNode.ELEMENT_NODE) {
				return XPCOM.queryInterface(node, nsIDOMElement.class);
			} else if (node.getNodeType() == nsIDOMNode.TEXT_NODE) {
				return XPCOM.queryInterface(node.getParentNode(), nsIDOMElement.class);
			}
		}

		return null;
	}

	/**
	 * Decorator
	 * 
	 * @author mareshkau
	 * @param domElement
	 *            arround which border will be shown
	 * 
	 */
	private void drawElementOutline(List<FlasherData> flasherData) {
		getFlasher().drawElementOutline(flasherData);
	}

	/**
	 * Checks if node has select in parent node, if has it's cause crash on OSX
	 * and xulrunner 1.8.1.3
	 * 
	 * @param domElement
	 * @return
	 */
	// private boolean hasSelectInParenNodes(nsIDOMNode domNode){
	// if(domNode==null) {
	// return false;
	//		}else if("select".equalsIgnoreCase(domNode.getNodeName())){ //$NON-NLS-1$
	// return true;
	// } else {
	// return hasSelectInParenNodes(domNode.getParentNode());
	// }
	// }

	@Override
	protected void onDispose() {
		selectedNodes = new ArrayList<nsIDOMNode>();
		if (flasher != null) {
			flasher.dispose();
			flasher = null;
		}
		super.onDispose();
	}

	private class VisualPaintListener implements PaintListener {

		public void paintControl(PaintEvent e) {
			redrawSelectionRectangle();
		}

	}
	/**
	 * 
	 * @return selected element if only one element selected in visual part
	 */
	public nsIDOMElement getSelectedElement(){
		nsIDOMElement resizeElement = null;
		if(getSelectedNodes()!=null&&getSelectedNodes().size()>0){
			resizeElement =getElementForNode(getSelectedNodes().get(0));
		}
		return resizeElement;
	}
}
