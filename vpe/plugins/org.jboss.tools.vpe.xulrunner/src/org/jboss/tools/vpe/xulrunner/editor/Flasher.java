package org.jboss.tools.vpe.xulrunner.editor;

import java.util.List;

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
	private static final boolean IS_OPEN_JDK = (System.getProperty("java.runtime.name")!=null&&System.getProperty("java.runtime.name").contains("OpenJDK")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	//fix for JBIDE-7957
	private static final boolean IS_LINUX=Platform.OS_LINUX.equals(Platform.getOS());
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
		//fix for JBIDE-7295, added by Maksim Areshkau
		if (Platform.OS_MACOSX.equals(Platform.getOS())
				||IS_OPEN_JDK
				||IS_LINUX) {
			drawOutline = new DrawOutlineInterface() {
				private List<FlasherData> previouslyBorderedElements = null;
				public void drawElementOutline(List<FlasherData> flasherData) {
					clearPreviouslySelectedElements();
					// save style for early bordered element
					saveOldStyle(flasherData);
					this.previouslyBorderedElements  = flasherData;					
				}
				private void clearPreviouslySelectedElements(){
					if(previouslyBorderedElements!=null){
						for (FlasherData borderedData : previouslyBorderedElements) {
							nsIDOMElement lastBorderedElement = borderedData.getElement();
							if (lastBorderedElement != null
									&& lastBorderedElement.getAttribute(XulRunnerEditor.STYLE_ATTR) != null) {
								String style = lastBorderedElement
										.getAttribute(PREV_STYLE_ATTR_NAME);
								lastBorderedElement.removeAttribute(PREV_STYLE_ATTR_NAME);
								lastBorderedElement.setAttribute(XulRunnerEditor.STYLE_ATTR, style);
							}
						}
					}
				}
				
				private void saveOldStyle(List<FlasherData> flasherData){
					for (FlasherData flasherData2 : flasherData) {
						nsIDOMElement domElement = flasherData2.getElement();	
						String oldstyle = domElement.getAttribute(XulRunnerEditor.STYLE_ATTR);
						domElement.setAttribute(XulRunnerEditor.STYLE_ATTR, 
								domElement.getAttribute(XulRunnerEditor.STYLE_ATTR) + ';'
								+ String.format(ELEMENT_BORDER_PATTERN, flasherData2.getSelectionColor()));
						domElement.setAttribute(PREV_STYLE_ATTR_NAME, oldstyle);
					}
				}
				
			};
		} else {
			drawOutline = new DrawOutlineInterface() {			
				public void drawElementOutline(List<FlasherData> flasherData) {
					for (FlasherData flasherData2 : flasherData) {
						iFlasher.setColor(flasherData2.getSelectionColor());
						iFlasher.drawElementOutline(flasherData2.getElement());
					}
				}
			};
		}
	}
	
	public void drawElementOutline(List<FlasherData> flasherData) {

		drawOutline.drawElementOutline(flasherData);
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
	public void drawElementOutline(List<FlasherData> flasherData);
};

