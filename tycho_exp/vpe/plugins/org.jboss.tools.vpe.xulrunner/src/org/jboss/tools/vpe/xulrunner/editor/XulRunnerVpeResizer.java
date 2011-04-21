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
import org.eclipse.swt.graphics.Rectangle;
import org.mozilla.interfaces.nsIDOMCSSStyleDeclaration;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMElementCSSInlineStyle;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMEventListener;
import org.mozilla.interfaces.nsIDOMEventTarget;
import org.mozilla.interfaces.nsIDOMHTMLDocument;
import org.mozilla.interfaces.nsIDOMHTMLElement;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNode;

/**
 * IXulRunnerVpeResizer implementation
 * @author A. Yukhovich
 */
public class XulRunnerVpeResizer implements IXulRunnerVpeResizer {	
	/** COEFFICIENT_TYPE */
	enum  COEFFICIENT_TYPE { X, Y, WIDTH, HEIGHT };
	

	/** RESIZER_MARKER_STRING_TOPLEFT */
	final static private String RESIZER_MARKER_STRING_TOPLEFT = "nw"; //$NON-NLS-1$
	
	/** RESIZER_MARKER_STRING_TOP */
	final static private String RESIZER_MARKER_STRING_TOP  = "n"; //$NON-NLS-1$
	
	/** RESIZER_MARKER_STRING_TOPRIGHT */	
	final static private String RESIZER_MARKER_STRING_TOPRIGHT = "ne"; //$NON-NLS-1$
	
	/** RESIZER_MARKER_STRING_LEFT */
	final static private String RESIZER_MARKER_STRING_LEFT = "w"; //$NON-NLS-1$
	
	/** RESIZER_MARKER_STRING_BOTTOMLEFT */
	final static private String RESIZER_MARKER_STRING_RIGHT = "e"; //$NON-NLS-1$
	
	/** RESIZER_MARKER_STRING_BOTTOMLEFT */
	final static private String RESIZER_MARKER_STRING_BOTTOMLEFT = "sw"; //$NON-NLS-1$
	
	/** RESIZER_MARKER_STRING_BOTTOM */
	final static private String RESIZER_MARKER_STRING_BOTTOM = "s"; //$NON-NLS-1$
	
	/** RESIZER_MARKER_STRING_BOTTOMRIGHT */
	final static private String RESIZER_MARKER_STRING_BOTTOMRIGHT = "se"; //$NON-NLS-1$

	/** MAX_SIZE */
	final static private int MAX_SIZE = 20000000; 
	
	
	nsIDOMEventListener mouseListener;
	nsIDOMEventListener mouseMotionListener;
	  

	private ArrayList<IVpeResizeListener> objectResizeEventListeners = new ArrayList<IVpeResizeListener>();

	private boolean isResizing;
	private int originalX;
	private int originalY;
	
	/** bit-mask of used resize marker */
	private int usedResizeMarker;
	
	private int  incrementFactorX;
	private int  incrementFactorY;
	private int  incrementFactorWidth;
	private int  incrementFactorHeight;
	

	/** resizingObject */
	private nsIDOMElement resizingObject;
	
	/** resizingShadow */
	private nsIDOMElement resizingShadow;
	
	/** domDocument */
	private nsIDOMDocument domDocument;
	
	private nsIDOMElement activeHandle;

	private Rectangle elementBounds;
	
	/** resizer marker top-left */
	private nsIDOMElement markerTopLeft = null;

	/** resizer marker top */
	private nsIDOMElement markerTop = null;
	
	/** resizer marker top-right */
	private nsIDOMElement markerTopRight = null;
	
	/** resizer marker left */
	private nsIDOMElement markerLeft = null;	
	
	/** resizer marker right */
	private nsIDOMElement markerRight = null;
	
	/** resizer marker bottom */
	private nsIDOMElement markerBottom = null;
	
	/** resizer marker bottom-left */
	private nsIDOMElement markerBottomLeft = null;
	
	/** resizer marker bottom-right */
	private nsIDOMElement markerBottomRight = null;
	
	
	/**
	 * 
	 * @param domDocument
	 */
	public void init(nsIDOMDocument domDocument)
	{
		
		this.domDocument = domDocument;
		
		mouseListener = new VpeResizerMouseListener(this);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.vpe.mozilla.resizer.IVpeResizer#show(org.mozilla.interfaces.nsIDOMElement,  int)
	 */
	public void show(nsIDOMElement domElement, int resizers) {
				
		if ( resizingObject != null ) {
			hide();
		}
		
		resizingObject = domElement;

		elementBounds = XulRunnerVpeUtils.getElementBounds(domElement);
		
		if ((elementBounds.width <= 0) || 
			(elementBounds.width > MAX_SIZE) || 
			(elementBounds.height <= 0) || 
			(elementBounds.height > MAX_SIZE)) return;
		
		nsIDOMElement bodyElement = getRootElement();
		
		if (bodyElement == null ) return;
		
		if ((resizers & RESIZER_MARKER_TOPLEFT) == RESIZER_MARKER_TOPLEFT) {
			markerTopLeft = createResizer( RESIZER_MARKER_STRING_TOPLEFT, bodyElement);
			if (markerTopLeft == null) {
				return ;
			}
		}

		if ((resizers & RESIZER_MARKER_TOP) == RESIZER_MARKER_TOP) {
			markerTop = createResizer( RESIZER_MARKER_STRING_TOP, bodyElement);
			if (markerTop == null) {
				return ;
			}
		}

		if ((resizers & RESIZER_MARKER_TOPRIGHT) == RESIZER_MARKER_TOPRIGHT) {
			markerTopRight = createResizer(RESIZER_MARKER_STRING_TOPRIGHT, bodyElement);
			if (markerTopRight == null) {
				return ;
			}
		}
		
		if ((resizers & RESIZER_MARKER_LEFT) == RESIZER_MARKER_LEFT) {
			markerLeft = createResizer(RESIZER_MARKER_STRING_LEFT, bodyElement);
			if (markerLeft == null) {
				return ;
			}
		}

		if ((resizers & RESIZER_MARKER_RIGHT) == RESIZER_MARKER_RIGHT) {
			markerRight = createResizer(RESIZER_MARKER_STRING_RIGHT, bodyElement);
			if (markerRight == null) {
				return ;
			}
		}
		
		if ((resizers & RESIZER_MARKER_BOTTOMLEFT) == RESIZER_MARKER_BOTTOMLEFT) {
			markerBottomLeft = createResizer(RESIZER_MARKER_STRING_BOTTOMLEFT, bodyElement);
			if (markerBottomLeft == null) {
				return ;
			}
		}

		if ((resizers & RESIZER_MARKER_BOTTOM) == RESIZER_MARKER_BOTTOM) {
			markerBottom = createResizer(RESIZER_MARKER_STRING_BOTTOM, bodyElement);
			if (markerBottom == null) {
				return ;
			}
		}

		if ((resizers & RESIZER_MARKER_BOTTOMRIGHT) == RESIZER_MARKER_BOTTOMRIGHT) {
			markerBottomRight = createResizer(RESIZER_MARKER_STRING_BOTTOMRIGHT, bodyElement);
			if (markerBottomRight == null) {
				return ;
			}
		}
		
		setAllResizersPosition();
		
		resizingShadow = createShadow(bodyElement, resizingObject);

		setShadowPosition(resizingShadow, elementBounds.x, elementBounds.y);
	}

	
	/* (non-Javadoc)
	 * @see org.jboss.vpe.mozilla.resizer.IVpeResizer#hide()
	 */
	public void hide() {
		nsIDOMElement bodyElement;
		
		bodyElement = getRootElement();
		if ( bodyElement == null ) {
			return;
		}

		nsIDOMNode parentNode = (nsIDOMNode) bodyElement.queryInterface(nsIDOMNode.NS_IDOMNODE_IID);

		if ( parentNode == null) {
			return;
		}
		
		if ( markerTopLeft != null) {
			parentNode.removeChild(markerTopLeft);
		}

		if ( markerTop != null) {
			parentNode.removeChild(markerTop);
		}


		if ( markerTopRight != null) {
			parentNode.removeChild(markerTopRight);
		}

		if ( markerLeft != null) {
			parentNode.removeChild(markerLeft);
		}
		
		if ( markerRight != null) {
			parentNode.removeChild(markerRight);
		}

		if ( markerBottomLeft != null) {
			parentNode.removeChild(markerBottomLeft);
		}

		if ( markerBottom != null) {
			parentNode.removeChild(markerBottom);
		}

		if ( markerBottomRight != null) {
			parentNode.removeChild(markerBottomRight);
		}


		if ( resizingShadow != null ) {
			parentNode.removeChild(resizingShadow);
		}
		
		markerBottom = null;
		markerTop = null;
		markerLeft = null;
		markerRight = null;
		markerBottomRight = null;
		markerBottomLeft = null;
		markerTopRight = null;
		markerTopLeft = null;
		
		resizingShadow = null;
		resizingObject = null;
	}

	
	/* (non-Javadoc)
	 * @see org.jboss.vpe.mozilla.resizer.IVpeResizer#mouseDown(int, int, org.mozilla.interfaces.nsIDOMElement)
	 */
	public void mouseDown(int clientX, int clientY, nsIDOMElement domElement) {
		
		
		if ( domElement != null ) {
			
			boolean isAnonElement = domElement.hasAttribute(XulRunnerConstants.STRING_MOZ_ANONCLASS); 
			
			if (isAnonElement) {
				String anonclass = domElement.getAttribute(XulRunnerConstants.STRING_MOZ_ANONCLASS);

				if ( anonclass != null ) {
					if ( anonclass.equals(XulRunnerConstants.VPE_CLASSNAME_MOZ_RESIZER) ) {
						originalX = clientX;
						originalY = clientY;
						startResizing(domElement);
					} // if
				}  // if
			} // if
		} // if
	}
	
	
	/* (non-Javadoc)
	 * @see org.jboss.vpe.mozilla.resizer.IVpeResizer#mouseMove(org.mozilla.interfaces.nsIDOMEvent)
	 */
	public void mouseMove(nsIDOMEvent event) {
		if (isResizing) {
			nsIDOMMouseEvent mouseEvent = (nsIDOMMouseEvent)event.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
			int clientX, clientY;

			clientX = mouseEvent.getClientX();
			clientY = mouseEvent.getClientY();

			int newX = getNewResizingX(clientX, clientY);
			int newY = getNewResizingY(clientX, clientY);
			int newWidth  = getNewResizingWidth(clientX, clientY);
			int newHeight = getNewResizingHeight(clientX, clientY);
			
			setStylePropertyPixels(resizingShadow, XulRunnerConstants.HTML_ATTR_LEFT, newX);
			setStylePropertyPixels(resizingShadow, XulRunnerConstants.HTML_ATTR_TOP, newY);
			setStylePropertyPixels(resizingShadow, XulRunnerConstants.HTML_ATTR_WIDTH, newWidth);
			setStylePropertyPixels(resizingShadow, XulRunnerConstants.HTML_ATTR_HEIGHT, newHeight);
		} // if
	}


	/* (non-Javadoc)
	 * @see org.jboss.vpe.mozilla.resizer.IVpeResizer#mouseUp(int, int, org.mozilla.interfaces.nsIDOMElement)
	 */
	public void mouseUp(int aX, int aY, nsIDOMElement target) {
		if (isResizing) {
			isResizing = false;
			
			
			hideShadow();
			endResizing(aX, aY);
				
 			nsIDOMEventTarget erP = getDOMEventTarget();
 
 			if (erP != null) { 
 				erP.removeEventListener(XulRunnerConstants.EVENT_NAME_MOUSEMOVE, mouseMotionListener, true); 
 				erP.removeEventListener(XulRunnerConstants.EVENT_NAME_MOUSEUP, mouseListener, true); 
 				mouseMotionListener = null;
 			}
		}
	}
	

	
	/* (non-Javadoc)
	 * @see org.jboss.vpe.mozilla.resizer.IVpeResizer#addResizeListener(org.jboss.vpe.mozilla.resizer.IVpeResizeListener)
	 */
	public void addResizeListener(IVpeResizeListener listener) {
		if ( (objectResizeEventListeners.size() != 0 ) &&
				( objectResizeEventListeners.indexOf(listener) != -1)) {
				return;
			}
			objectResizeEventListeners.add(listener);
	}

	
	/* (non-Javadoc)
	 * @see org.jboss.vpe.mozilla.resizer.IVpeResizer#removeResizeListener(org.jboss.vpe.mozilla.resizer.IVpeResizeListener)
	 */
	public void removeResizeListener(IVpeResizeListener listener) {
		if ( (objectResizeEventListeners.size() == 0 ) ||
				( objectResizeEventListeners.indexOf(listener) == -1)) {
				return;
			}
			objectResizeEventListeners.remove(listener);
	}

	/**
	 * Setting a position of shadow
	 * @param shadowElement
	 * @param aOriginalObjectX
	 * @param aOriginalObjectY
	 */
	private void setShadowPosition(nsIDOMElement shadowElement, int aOriginalObjectX, int aOriginalObjectY)	{
		setAnonymousElementPosition(aOriginalObjectX, aOriginalObjectY, shadowElement);
	}


	/**
	 * create a anonymous dom-element
	 * 
	 * @param aTag
	 *            a tag of dom element
	 * @param aParentNode
	 * @param aAnonClass
	 * @param isCreatedHidden
	 * @return
	 */
	private nsIDOMElement createAnonymousElement(String aTag, nsIDOMNode aParentNode, String aAnonClass, boolean isCreatedHidden) {
		nsIDOMElement returnElement = null;
	
		returnElement = domDocument.createElement(aTag);
		
		// add the "hidden" class if needed
		if (isCreatedHidden) {
			returnElement.setAttribute(XulRunnerConstants.HTML_ATTR_CLASS, XulRunnerConstants.HTML_VALUE_HIDDEN);
		}
		
		// add an _moz_anonclass attribute if needed
		if ( aAnonClass.length() != 0  ) {
			returnElement.setAttribute(XulRunnerConstants.STRING_MOZ_ANONCLASS, aAnonClass);
		}		
		
		aParentNode.appendChild(returnElement);
		
		return returnElement;
	}

	/**
	 * 
	 * @param parentNode
	 * @param originalObject
	 * @return
	 */
	private nsIDOMElement createShadow(nsIDOMNode parentNode,  nsIDOMElement originalObject) {
		nsIDOMElement returnElement = null;
		
		returnElement = createAnonymousElement(XulRunnerConstants.HTML_TAG_SPAN, parentNode, XulRunnerConstants.VPE_CLASS_NAME_MOZ_RESIZING_SHADOW, true );

		return returnElement;
	}
	
	/**
	 * 
	 * @param domElement
	 */
	private void startResizing(nsIDOMElement domElement) {
		isResizing = true;
		
		activeHandle = domElement;
		activeHandle.setAttribute(XulRunnerConstants.STRING_MOZ_ACTIVATED, XulRunnerConstants.HTML_VALUE_TRUE);

		String locationStr = activeHandle.getAttribute(XulRunnerConstants.HTML_ATTR_ANONLOCATION);

		if (locationStr == null ) {
			return;
		}
		
		if (locationStr.equals(RESIZER_MARKER_STRING_TOPLEFT)) {
			usedResizeMarker = RESIZER_MARKER_TOPLEFT;
			setResizeIncrements(1, 1, -1, -1, false);
		}	else if (locationStr.equals(RESIZER_MARKER_STRING_TOP)) {
			usedResizeMarker = RESIZER_MARKER_TOP;
			setResizeIncrements(0, 1, 0, -1, false);
		}	else if (locationStr.equals(RESIZER_MARKER_STRING_TOPRIGHT)) {
			usedResizeMarker = RESIZER_MARKER_TOPRIGHT;
			setResizeIncrements(0, 1, 1, -1, false);
		}	else if (locationStr.equals(RESIZER_MARKER_STRING_LEFT)) {
			usedResizeMarker = RESIZER_MARKER_LEFT;
			setResizeIncrements(1, 0, -1, 0, false);
		}	else if (locationStr.equals(RESIZER_MARKER_STRING_RIGHT)) {
			usedResizeMarker = RESIZER_MARKER_RIGHT;
			setResizeIncrements(0, 0, 1, 0, false);
		}	else if (locationStr.equals(RESIZER_MARKER_STRING_BOTTOMLEFT)) {
			usedResizeMarker = RESIZER_MARKER_BOTTOMLEFT;
			setResizeIncrements(1, 0, -1, 1, false);
		}	else if (locationStr.equals(RESIZER_MARKER_STRING_BOTTOM)) {
			usedResizeMarker = RESIZER_MARKER_BOTTOM;
			setResizeIncrements(0, 0, 0, 1, false);
		}	else if (locationStr.equals(RESIZER_MARKER_STRING_BOTTOMRIGHT)) {
			usedResizeMarker = RESIZER_MARKER_BOTTOMRIGHT;
			setResizeIncrements(0, 0, 1, 1, false);
		}

		// make the shadow appear
		resizingShadow.removeAttribute(XulRunnerConstants.HTML_ATTR_CLASS);

		// position it
		setShadowPosition(resizingShadow, elementBounds.x, elementBounds.y);

		setStylePropertyPixels(resizingShadow, XulRunnerConstants.HTML_ATTR_WIDTH, elementBounds.width );
		setStylePropertyPixels(resizingShadow, XulRunnerConstants.HTML_ATTR_HEIGHT, elementBounds.height );
		
		
		if (mouseMotionListener !=  null) {
			return;
		}
		
		mouseMotionListener = new VpeResizerMouseMotionListener(this);
		if ( mouseMotionListener == null ) {
			return;
		}
		
		nsIDOMEventTarget eventTarget = getDOMEventTarget();  
		
		if (  eventTarget != null ) {
			eventTarget.addEventListener(XulRunnerConstants.EVENT_NAME_MOUSEMOVE, mouseMotionListener, true);
			eventTarget.addEventListener(XulRunnerConstants.EVENT_NAME_MOUSEUP, mouseListener, true);
		}
		
		return;		
	}
	
	/**
	 * getting a nsIDOMEventTarget from nsIDOMDocument
	 * @return nsIDOMEventTarget from nsIDOMDocument
	 */
	private nsIDOMEventTarget getDOMEventTarget() {
		
		nsIDOMEventTarget eventTarget = (nsIDOMEventTarget) domDocument.queryInterface(nsIDOMEventTarget.NS_IDOMEVENTTARGET_IID);
		if (eventTarget == null) {
			throw new RuntimeException("nsIDOMEventTarget is null"); //$NON-NLS-1$
		}
		
		return eventTarget;
	}
	
	
	/**
	 * 
	 * @param aX
	 * @param aY
	 * @return
	 */
	private int getNewResizingX(int aX, int aY)	{
		int resized = elementBounds.x+ getNewResizingIncrement(aX, aY, COEFFICIENT_TYPE.X) * incrementFactorX;
		int max =   elementBounds.x + elementBounds.width;
		return Math.min(resized, max);
	}

	/**
	 * 
	 * @param aX
	 * @param aY
	 * @return
	 */
	private int getNewResizingY(int aX, int aY)	{
		int resized = elementBounds.y + getNewResizingIncrement(aX, aY, COEFFICIENT_TYPE.Y) * incrementFactorY;
		int max =   elementBounds.y + elementBounds.height;
		return Math.min(resized, max);
	}

	/**
	 * 
	 * @param aX
	 * @param aY
	 * @return
	 */
	private int getNewResizingWidth(int aX, int aY)	{
		int resized = elementBounds.width +	getNewResizingIncrement(aX, aY, COEFFICIENT_TYPE.WIDTH) * incrementFactorWidth;
		return Math.max(resized, 1);
	}

	/**
	 * 
	 * @param aX
	 * @param aY
	 * @return
	 */
	private int getNewResizingHeight(int aX, int aY) {
		int resized = elementBounds.height + getNewResizingIncrement(aX, aY, COEFFICIENT_TYPE.HEIGHT) *	incrementFactorHeight;
		return Math.max(resized, 1);
	}

	/**
	 * 
	 * @param aX
	 * @param aY
	 * @param coefficient_type
	 * @return
	 */
	private int getNewResizingIncrement(int aX, int aY, COEFFICIENT_TYPE coefficient_type)	{
		int result = 0;
		
		switch (coefficient_type) {
		case X:
		case WIDTH:
			result = aX - originalX;
			break;
		case Y:
		case HEIGHT:
			result = aY - originalY;
			break;
		}
		return result;
	}

	
	/**
	 * Get root element
	 * 
	 * @return root element
	 */
	private nsIDOMElement getRootElement() {
		
		nsIDOMElement bodyElement = null;
		
		nsIDOMHTMLDocument htmlDocument = (nsIDOMHTMLDocument) domDocument.queryInterface(nsIDOMHTMLDocument.NS_IDOMHTMLDOCUMENT_IID);
		
		if ( htmlDocument != null ) {
			 nsIDOMHTMLElement htmlBody = htmlDocument.getBody();
			 
			 if ( htmlBody != null ) {
				 bodyElement = (nsIDOMElement) htmlBody.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
			 } // if
		} // if
		
		return bodyElement;
	}

	/**
	 * Create a new resizer element
	 * @param resizerMarkerString
	 * @param parentNode
	 * @return a new resizer element
	 */
	private nsIDOMElement createResizer(String resizerMarkerString, nsIDOMNode parentNode) {
		nsIDOMElement aNewResizer = createAnonymousElement(XulRunnerConstants.HTML_TAG_SPAN, parentNode, XulRunnerConstants.VPE_CLASSNAME_MOZ_RESIZER, false );
		
		nsIDOMEventTarget evtTarget = (nsIDOMEventTarget) aNewResizer.queryInterface(nsIDOMEventTarget.NS_IDOMEVENTTARGET_IID);
		
		evtTarget.addEventListener(XulRunnerConstants.EVENT_NAME_MOUSEDOWN, mouseListener, true);

		aNewResizer.setAttribute(XulRunnerConstants.HTML_ATTR_ANONLOCATION, resizerMarkerString);
		
		return aNewResizer;
	}
	

	/**
	 * Set all positions of resizer's markers
	 */
	private void setAllResizersPosition() {
		int left = elementBounds.x;
		int top = elementBounds.y;
		int width = elementBounds.width;
		int height = elementBounds.height;
		
		int resizerWidth = 5;
		int resizerHeight = 5;
		
		int rw = (int)((resizerWidth + 1) / 2);
		int rh = (int)((resizerHeight+ 1) / 2);
		
		if (markerTopLeft != null) {
			setAnonymousElementPosition(left-resizerWidth-2, top-resizerHeight-2, markerTopLeft);
		}

		if (markerTop != null) {
			setAnonymousElementPosition(left+width/2-rw, top-resizerHeight-2, markerTop);
		}

		if (markerTopRight != null) {
			setAnonymousElementPosition(left+width, top-resizerHeight-2, markerTopRight);
		}

		if (markerLeft != null) {
			setAnonymousElementPosition(left-resizerWidth-2, top+height/2-rh, markerLeft);
		}
		
		if (markerRight != null) {
			setAnonymousElementPosition(left+width, top+height/2-rh, markerRight);
		}
		
		if (markerBottomLeft != null) {
			setAnonymousElementPosition(left-resizerWidth-2, top+height, markerBottomLeft);
		}

		if (markerBottom != null) {
			setAnonymousElementPosition(left+width/2-rw, top+height, markerBottom);
		}

		if (markerBottomRight != null) {
			setAnonymousElementPosition(left+width, top+height, markerBottomRight);
		}
		
	}
	
	/**
	 * 
	 * @param left
	 * @param top
	 * @param domElement
	 */
	private void setAnonymousElementPosition(int left, int top,nsIDOMElement domElement)	{
		setStylePropertyPixels(domElement,XulRunnerConstants.HTML_ATTR_LEFT, left);
		setStylePropertyPixels(domElement,XulRunnerConstants.HTML_ATTR_TOP, top);		
	}
	
	/**
	 * 
	 * @param aElement
	 * @param aProperty
	 * @param aValue
	 */
	private void setStylePropertyPixels(nsIDOMElement aElement, String aProperty, int aValue) {
		setStyle(aElement, aProperty, aValue + "px"); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param aX
	 * @param aY
	 * @param aW
	 * @param aH
	 * @param aPreserveRatio
	 */
	private void setResizeIncrements(int aX, int aY, int aW, int aH,  boolean aPreserveRatio) {
		incrementFactorX = aX;
		incrementFactorY = aY;
		incrementFactorWidth = aW;
		incrementFactorHeight = aH;
		// mPreserveRatio = aPreserveRatio;
	}

	
	/**
	 * Set style for nsIDOMElement
	 * @param domElement 
	 * @param cssPropertyName
	 * @param cssPropertyValue
	 */
	private void setStyle(nsIDOMElement domElement, String cssPropertyName, String cssPropertyValue) {
		nsIDOMElementCSSInlineStyle inlineStyles = (nsIDOMElementCSSInlineStyle) domElement.queryInterface(nsIDOMElementCSSInlineStyle.NS_IDOMELEMENTCSSINLINESTYLE_IID);
		
	    if ( inlineStyles == null) {
	    	return;
	    }

	    nsIDOMCSSStyleDeclaration cssDecl = inlineStyles.getStyle();

	    if ( cssDecl == null) {
	    	return;
	    }

	    if (cssPropertyValue.length() == 0 ) {
			// an empty value means we have to remove the property
	    	cssDecl.removeProperty(cssPropertyName);
	    }  else {
			// let's recreate the declaration as it was
	    	String priority = cssDecl.getPropertyPriority(cssPropertyName);
	    	cssDecl.setProperty(cssPropertyName, cssPropertyValue, priority);
	    }
	}


	/**
	 * 
	 * @param aClientX
	 * @param aClientY
	 */
	private void endResizing(int aClientX, int aClientY) {
		if (resizingShadow == null) {
			return;
		}

		if( activeHandle != null) {
			activeHandle.removeAttribute(XulRunnerConstants.STRING_MOZ_ACTIVATED);
			activeHandle = null;
		}

		int left   = getNewResizingX(aClientX, aClientY);
		int top    = getNewResizingY(aClientX, aClientY);
		int width  = getNewResizingWidth(aClientX, aClientY);
		int height = getNewResizingHeight(aClientX, aClientY);
		
		if ( objectResizeEventListeners.size() != 0) {			
			for ( IVpeResizeListener resizeListener : objectResizeEventListeners  ) {
				resizeListener.onEndResizing(usedResizeMarker,top,left,width,height,resizingObject);
			}
		}
		
		usedResizeMarker = 0;
	}
	
	/**
	 * Hide of shadow box
	 */
	private void hideShadow()	{
		if (resizingShadow != null ) {
			resizingShadow.setAttribute(XulRunnerConstants.HTML_ATTR_CLASS, XulRunnerConstants.HTML_VALUE_HIDDEN);
		}		
	}

	public void dispose() {
		if (mouseMotionListener != null) {
			nsIDOMEventTarget erP = getDOMEventTarget();
			 
 			if (erP != null) { 
 				erP.removeEventListener(XulRunnerConstants.EVENT_NAME_MOUSEMOVE, mouseMotionListener, true); 
 				erP.removeEventListener(XulRunnerConstants.EVENT_NAME_MOUSEUP, mouseListener, true); 
 				mouseMotionListener = null;
 			}
		}
	}
}
