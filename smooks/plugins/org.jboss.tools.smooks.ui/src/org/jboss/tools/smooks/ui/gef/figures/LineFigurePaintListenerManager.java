/**
 * 
 */
package org.jboss.tools.smooks.ui.gef.figures;

import java.util.HashMap;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.smooks.utils.SmooksExtensionPointConstants;

/**
 * @author Dart
 * 
 */
public class LineFigurePaintListenerManager {
	private static LineFigurePaintListenerManager instance;
	private HashMap<String, IConfigurationElement> processorMap = new HashMap<String, IConfigurationElement>();

	private LineFigurePaintListenerManager() {
		super();
		loadExtensions();
	}

	public synchronized static LineFigurePaintListenerManager getInstance() {
		if (instance == null) {
			instance = new LineFigurePaintListenerManager();
		}
		return instance;
	}

	public ILineFigurePaintListener getPaintListener(String sid, String tid) {
		try {
			IConfigurationElement element = processorMap.get(this.generateKey(
					sid, tid));
			if (element != null) {
				return (ILineFigurePaintListener) element
						.createExecutableExtension(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_CLASS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void loadExtensions() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint ep = registry
				.getExtensionPoint(SmooksExtensionPointConstants.EXTENTION_POINT_LINEPAINT_LISTENER);
		IConfigurationElement[] elements = ep.getConfigurationElements();
		for (int i = 0; i < elements.length; i++) {
			IConfigurationElement element = elements[i];
			if (!element
					.getName()
					.equals(
							SmooksExtensionPointConstants.EXTENTION_POINT_ELEMENT_LINEPAINT_LISTENER))
				continue;
			String sourceId = element
					.getAttribute(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_SOURCEID);
			String targetId = element
					.getAttribute(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_TARGETID);
			if (sourceId != null && targetId != null) {
				processorMap.put(generateKey(sourceId, targetId), element);
			}
		}
	}

	private String generateKey(String sid, String tid) {
		return sid + ":" + tid;
	}
}
