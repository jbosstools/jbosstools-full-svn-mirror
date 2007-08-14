package org.jboss.tools.vpe.xulrunner.editor;

import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMEventListener;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.xpcom.Mozilla;

/**
 * @author A. Yukhovich
 */
public class VpeResizerMouseMotionListener implements nsIDOMEventListener {
	/** vpeRezizer */
	private IVpeResizer vpeResizer; 
	
	/**
	 * Default constructor
	 * @param vpeResizer a IVpeResizer object
	 */
	public VpeResizerMouseMotionListener(IVpeResizer vpeResizer) {
		this.vpeResizer = vpeResizer;
	}
	
	/**
	 * mouse move
	 * @param event a nsIDOMEvent object
	 */
	public void mouseMove(nsIDOMEvent event) {
		nsIDOMMouseEvent mouseEvent = (nsIDOMMouseEvent) event.queryInterface(nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
		
		if ( mouseEvent == null ) {
			return;
		}
			
		vpeResizer.mouseMove(mouseEvent);	
	}
	
	public void dragMove(nsIDOMEvent event) {
		
	}

	public void handleEvent(nsIDOMEvent event) {
		if ("mousemove".equals(event.getType())) {
			mouseMove(event);
		}
	}

	public nsISupports queryInterface(String aIID) {
		if (aIID.equals(nsIDOMEventListener.NS_IDOMEVENTLISTENER_IID)) {
			return this;
		} // if

		return Mozilla.queryInterface(this, aIID);
	}

}
