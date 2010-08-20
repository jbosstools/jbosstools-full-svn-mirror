package org.jboss.tools.vpe.editor.mozilla.listener;

import java.util.EventListener;

import org.mozilla.interfaces.nsIDOMEvent;

/**
 * Listener for scrolling events,
 * 
 * @author Yahor Radtsevich (yradtsevich)
 *
 */
public interface MozillaScrollListener extends EventListener {
	void editorScrolled(nsIDOMEvent domEvent);
}
