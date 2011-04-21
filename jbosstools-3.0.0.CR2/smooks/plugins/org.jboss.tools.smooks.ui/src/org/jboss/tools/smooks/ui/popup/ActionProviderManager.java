/**
 * 
 */
package org.jboss.tools.smooks.ui.popup;

import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.smooks.utils.SmooksExtensionPointConstants;

/**
 * @author Dart
 * 
 */
public class ActionProviderManager {
	private static ActionProviderManager instance;

	private HashMap<String, IConfigurationElement> providerMap = new HashMap<String, IConfigurationElement>();

	private HashMap<String, IViewerActionsProvider> providerInstanceMap = new HashMap<String, IViewerActionsProvider>();

	private ActionProviderManager() {
		registActionProvider();
	}

	/**
	 * 
	 * @param typeID
	 * @return
	 * @throws CoreException
	 */
	public IViewerActionsProvider newActionProvider(String typeID)
			throws CoreException {
		IConfigurationElement element = providerMap.get(typeID);
		if (element == null) {
			return null;
		}
		IViewerActionsProvider provider = (IViewerActionsProvider) element
				.createExecutableExtension(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_CLASS);
		return provider;
	}

	/**
	 * 
	 * @param typeID
	 * @return
	 */
	public IViewerActionsProvider getActionProvider(String typeID) {
		IViewerActionsProvider provider = providerInstanceMap.get(typeID);
		if (provider == null) {
			try {
				provider = newActionProvider(typeID);
				providerInstanceMap.put(typeID, provider);
			} catch (Exception e) {

			}
		}
		return provider;
	}

	private void registActionProvider() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint ep = registry
				.getExtensionPoint(SmooksExtensionPointConstants.EXTENTION_POINT_ACTION_PROVIDER);
		if (ep == null)
			return;
		IConfigurationElement[] elements = ep.getConfigurationElements();
		for (int i = 0; i < elements.length; i++) {
			IConfigurationElement element = elements[i];

			String typeID = element
					.getAttribute(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_TYPE_ID);
			if (typeID == null) {
				continue;
			}
			providerMap.put(typeID, element);
		}
	}

	/**
	 * 
	 * @return
	 */
	public static synchronized ActionProviderManager getInstance() {
		if (instance == null) {
			instance = new ActionProviderManager();
		}
		return instance;
	}

}
