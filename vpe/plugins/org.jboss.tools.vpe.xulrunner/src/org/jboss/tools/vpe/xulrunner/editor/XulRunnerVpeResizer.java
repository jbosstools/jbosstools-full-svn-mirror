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

import static org.jboss.tools.vpe.xulrunner.util.XPCOM.queryInterface;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.jboss.tools.vpe.xulrunner.util.XulRunnerVpeUtils;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMEventListener;
import org.mozilla.interfaces.nsIDOMEventTarget;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNode;

/**
 * IXulRunnerVpeResizer implementation
 * @author A. Yukhovich
 */
public class XulRunnerVpeResizer implements IXulRunnerVpeResizer {
	
	private static String RESIZING_INFO_FORMAT = "%s x %s"; //$NON-NLS-1$
	private static Point RESIZING_INFO_OFFSET = new Point(20, 20);

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
	
	private XulRunnerHint resizingInfo;
	
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
		
		nsIDOMElement bodyElement = XulRunnerVpeUtils.getRootElement(domDocument);
		
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
		
		resizingShadow = createShadow(bodyElement);
		XulRunnerVpeUtils.setElementPosition(resizingShadow, elementBounds.x, elementBounds.y);
		
		resizingInfo = new XulRunnerHint(domDocument);
	}

	
	/* (non-Javadoc)
	 * @see org.jboss.vpe.mozilla.resizer.IVpeResizer#hide()
	 */
	public void hide() {
		if (resizingInfo != null) {
			resizingInfo.dispose();
			resizingInfo = null;
		}

		nsIDOMElement bodyElement = XulRunnerVpeUtils.getRootElement(domDocument);
		if ( bodyElement == null ) {
			return;
		}

		nsIDOMNode parentNode = queryInterface(bodyElement, nsIDOMNode.class);

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
			resizingShadow = null;
		}
		
		markerBottom = null;
		markerTop = null;
		markerLeft = null;
		markerRight = null;
		markerBottomRight = null;
		markerBottomLeft = null;
		markerTopRight = null;
		markerTopLeft = null;
		
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
			nsIDOMMouseEvent mouseEvent = queryInterface(event, nsIDOMMouseEvent.class);
			int clientX, clientY;

			clientX = mouseEvent.getClientX();
			clientY = mouseEvent.getClientY();

			int newX = getNewResizingX(clientX, clientY);
			int newY = getNewResizingY(clientX, clientY);
			int newWidth  = getNewResizingWidth(clientX, clientY);
			int newHeight = getNewResizingHeight(clientX, clientY);
			
			Rectangle shadowBounds = new Rectangle(newX, newY, newWidth, newHeight);
			
			XulRunnerVpeUtils.setElementBounds(resizingShadow, shadowBounds);
			redrawResizingInfo(shadowBounds);
		}
	}
	
	private void redrawResizingInfo(Rectangle bounds) {
		Point position;
		switch (usedResizeMarker) {
		case RESIZER_MARKER_TOPLEFT:
			position = new Point(bounds.x, bounds.y);
			break;
		case RESIZER_MARKER_TOP:
			position = new Point(bounds.x + bounds.width / 2,
					bounds.y);
			break;
		case RESIZER_MARKER_TOPRIGHT:
			position = new Point(bounds.x + bounds.width,
					bounds.y);
			break;
		case RESIZER_MARKER_LEFT:
			position = new Point(bounds.x,
					bounds.y + bounds.height / 2);
			break;
		case RESIZER_MARKER_RIGHT:
			position = new Point(bounds.x + bounds.width,
					bounds.y + bounds.height / 2);
			break;
		case RESIZER_MARKER_BOTTOMLEFT:
			position = new Point(bounds.x,
					bounds.y + bounds.height);
			break;
		case RESIZER_MARKER_BOTTOM:
			position = new Point(bounds.x + bounds.width / 2,
					bounds.y + bounds.height);
			break;
		case RESIZER_MARKER_BOTTOMRIGHT:
			position = new Point(bounds.x + bounds.width,
					bounds.y + bounds.height);
			break;
		default:
			position = new Point(0, 0);
			break;
		}

		position.x += RESIZING_INFO_OFFSET.x;
		position.y += RESIZING_INFO_OFFSET.y;
		
		resizingInfo.setHint(
				String.format(RESIZING_INFO_FORMAT, bounds.width, bounds.height));
		resizingInfo.setPosition(position);
		resizingInfo.redraw();
	}


	/* (non-Javadoc)
	 * @see org.jboss.vpe.mozilla.resizer.IVpeResizer#mouseUp(int, int, org.mozilla.interfaces.nsIDOMElement)
	 */
	public void mouseUp(int aX, int aY, nsIDOMElement target) {
		if (isResizing) {
			isResizing = false;
			
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
	 * 
	 * @param parentNode
	 * @param originalObject
	 * @return
	 */
	private nsIDOMElement createShadow(nsIDOMNode parentNode) {
		return XulRunnerVpeUtils.createAnonymousElement(domDocument,
				XulRunnerConstants.HTML_TAG_SPAN,
				parentNode, XulRunnerConstants.VPE_CLASS_NAME_MOZ_RESIZING_SHADOW,
				true);
	}
	
	private nsIDOMElement createResizingInfo(nsIDOMNode parentNode) {
		return XulRunnerVpeUtils.createAnonymousElement(domDocument, 
				XulRunnerConstants.HTML_TAG_SPAN,
				parentNode, XulRunnerConstants.VPE_CLASS_NAME_MOZ_RESIZING_INFO,
				true);
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
		XulRunnerVpeUtils.setElementBounds(resizingShadow, elementBounds);
		
		redrawResizingInfo(elementBounds);
		

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
		
		nsIDOMEventTarget eventTarget = queryInterface(domDocument, nsIDOMEventTarget.class);
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
	 * Create a new resizer element
	 * @param resizerMarkerString
	 * @param parentNode
	 * @return a new resizer element
	 */
	private nsIDOMElement createResizer(String resizerMarkerString, nsIDOMNode parentNode) {
		nsIDOMElement aNewResizer = XulRunnerVpeUtils.createAnonymousElement(
				domDocument, XulRunnerConstants.HTML_TAG_SPAN, parentNode,
				XulRunnerConstants.VPE_CLASSNAME_MOZ_RESIZER, false);
		
		nsIDOMEventTarget evtTarget = queryInterface(aNewResizer, nsIDOMEventTarget.class);
		
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
			XulRunnerVpeUtils.setElementPosition(
					markerTopLeft, left-resizerWidth-2, top-resizerHeight-2);
		}

		if (markerTop != null) {
			XulRunnerVpeUtils.setElementPosition(
					markerTop, left+width/2-rw, top-resizerHeight-2);
		}

		if (markerTopRight != null) {
			XulRunnerVpeUtils.setElementPosition(
					markerTopRight, left+width, top-resizerHeight-2);
		}

		if (markerLeft != null) {
			XulRunnerVpeUtils.setElementPosition(
					markerLeft, left-resizerWidth-2, top+height/2-rh);
		}
		
		if (markerRight != null) {
			XulRunnerVpeUtils.setElementPosition(
					markerRight, left+width, top+height/2-rh);
		}
		
		if (markerBottomLeft != null) {
			XulRunnerVpeUtils.setElementPosition(
					markerBottomLeft, left-resizerWidth-2, top+height);
		}

		if (markerBottom != null) {
			XulRunnerVpeUtils.setElementPosition(
					markerBottom, left+width/2-rw, top+height);
		}

		if (markerBottomRight != null) {
			XulRunnerVpeUtils.setElementPosition(
					markerBottomRight, left+width, top+height);
		}
		
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
	 * 
	 * @param aClientX
	 * @param aClientY
	 */
	private void endResizing(int aClientX, int aClientY) {
		if (resizingShadow == null) {
			return;
		}
		resizingShadow.setAttribute(XulRunnerConstants.HTML_ATTR_CLASS, XulRunnerConstants.HTML_VALUE_HIDDEN);
		if (resizingInfo == null) {
			return;
		}
		resizingInfo.dispose();

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
