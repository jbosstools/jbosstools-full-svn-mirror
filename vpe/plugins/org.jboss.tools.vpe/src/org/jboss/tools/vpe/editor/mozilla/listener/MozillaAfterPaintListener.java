package org.jboss.tools.vpe.editor.mozilla.listener;

import java.util.EventListener;

import org.mozilla.interfaces.nsIDOMEvent;

/**
 * Listener for MozAfterPaint events.
 * 
 * @author Yahor Radtsevich (yradtsevich)
 *
 */
public interface MozillaAfterPaintListener extends EventListener {
	void afterPaint(nsIDOMEvent domEvent);
}
