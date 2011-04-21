package org.jboss.ide.eclipse.packages.ui;

import java.util.ArrayList;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.jboss.ide.eclipse.packages.ui.actions.NewPackageAction;

public class ExtensionManager {
	public static final String NODE_POPUP_MENUS_EXTENSION_ID = "org.jboss.ide.eclipse.packages.ui.nodePopupMenus";
	public static final String NEW_PACKAGE_ACTIONS_EXTENSION_ID = "org.jboss.ide.eclipse.packages.ui.newPackageActions";
	
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
	
	public static NewPackageAction[] findNewPackageActions ()
	{
		ArrayList contributions = new ArrayList();
		IExtension[] extensions = findExtension(NEW_PACKAGE_ACTIONS_EXTENSION_ID);
		
		for (int i = 0; i < extensions.length; i++)
		{
			IConfigurationElement[] elements = extensions[i].getConfigurationElements();
			for (int j = 0; j < elements.length; j++)
			{
				contributions.add(new NewPackageAction(elements[j]));
			}
		}
		
		return (NewPackageAction[]) contributions.toArray(new NewPackageAction[contributions.size()]);
	}
}
