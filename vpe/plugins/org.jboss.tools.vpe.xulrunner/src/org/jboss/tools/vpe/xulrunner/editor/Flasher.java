package org.jboss.tools.vpe.xulrunner.editor;

import org.eclipse.core.runtime.Platform;
import org.jboss.tools.vpe.xulrunner.util.XPCOM;
import org.mozilla.interfaces.inIFlasher;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.xpcom.Mozilla;

/**
 * Class responsible for drawing borders around visual elements.
 * 
 * @author Yahor Radtsevich (yradtsevich): extracted the code from XulRunnerEditor
 * @author mareshkau
 *
 */
public class Flasher {
	private inIFlasher iFlasher;

	// added by Maksim Areshkau as element for which we
	// have drowed border. When we draw new border,
	// we should remove old one;
	private static final String ELEMENT_BORDER_PATTERN = "border: 2px solid %s !important;";//$NON-NLS-1$
	private static final String PREV_STYLE_ATTR_NAME = "oldstyle";//$NON-NLS-1$
	private DrawOutlineInterface drawOutline;
	public Flasher() {
		nsIServiceManager serviceManager = Mozilla.getInstance()
				.getServiceManager();
		iFlasher = (inIFlasher) serviceManager.getServiceByContractID(
				XPCOM.IN_FLASHER_CONTRACTID, inIFlasher.INIFLASHER_IID);
		iFlasher.setThickness(2);
		if (Platform.OS_MACOSX.equals(Platform.getOS())) {
			drawOutline = new DrawOutlineInterface() {
				private nsIDOMElement lastBorderedElement = null;
				@Override
				public void drawElementOutline(nsIDOMElement domElement) {
					if (this.lastBorderedElement != null
							&& this.lastBorderedElement.getAttribute(XulRunnerEditor.STYLE_ATTR) != null) {
						String style = this.lastBorderedElement
								.getAttribute(PREV_STYLE_ATTR_NAME);
						this.lastBorderedElement.removeAttribute(PREV_STYLE_ATTR_NAME);
						this.lastBorderedElement.setAttribute(XulRunnerEditor.STYLE_ATTR, style);
					}

					// save style for early bordered element
					String oldstyle = domElement.getAttribute(XulRunnerEditor.STYLE_ATTR);
					domElement.setAttribute(XulRunnerEditor.STYLE_ATTR, 
							domElement.getAttribute(XulRunnerEditor.STYLE_ATTR) + ';'
							+ String.format(ELEMENT_BORDER_PATTERN, iFlasher.getColor()));

					this.lastBorderedElement = domElement;
					this.lastBorderedElement.setAttribute(PREV_STYLE_ATTR_NAME,
							oldstyle);
					
				}
			};
		} else {
			drawOutline = new DrawOutlineInterface() {			
				@Override
				public void drawElementOutline(nsIDOMElement domElement) {
					iFlasher.drawElementOutline(domElement);
				}
			};
		}
	}
	
	public void drawElementOutline(nsIDOMElement domElement) {

		drawOutline.drawElementOutline(domElement);
	}

	public void scrollElementIntoView(nsIDOMElement element) {
		iFlasher.scrollElementIntoView(element);
	}

	public Object getColor() {
		return iFlasher.getColor();
	}

	public void setColor(String color) {
		iFlasher.setColor(color);
	}

	public void dispose() {
		iFlasher = null;
	}
}
/**
 * Interface for draw outline functionality
 * @author mareshkau
 *
 */
interface DrawOutlineInterface{
	public void drawElementOutline(nsIDOMElement domElement);
};

