package org.jboss.ide.eclipse.packages.ui;

import java.util.ArrayList;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class ExtensionManager {
	public static final String NODE_POPUP_MENUS_EXTENSION_ID = "org.jboss.ide.eclipse.packages.ui.nodePopupMenus";
	
	public static IExtension[] findExtension (String extensionId)
	{
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry.getExtensionPoint(extensionId);
		return extensionPoint.getExtensions();
	}
	
	public static NodeContribution[] findNodePopupMenuContributions ()
	{
		ArrayList contributions = new ArrayList();
		IExtension[] extensions = findExtension(NODE_POPUP_MENUS_EXTENSION_ID);
		
		for (int i = 0; i < extensions.length; i++)
		{
			IConfigurationElement[] elements = extensions[i].getConfigurationElements();
			for (int j = 0; j < elements.length; j++)
			{
				contributions.add(new NodeContribution(elements[j]));
			}
		}
		
		return (NodeContribution[]) contributions.toArray(new NodeContribution[contributions.size()]);
	}
}
