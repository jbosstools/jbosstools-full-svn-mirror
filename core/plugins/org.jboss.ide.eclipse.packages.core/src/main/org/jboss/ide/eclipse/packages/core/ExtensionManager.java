package org.jboss.ide.eclipse.packages.core;

import java.util.ArrayList;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.jboss.ide.eclipse.packages.core.model.types.AbstractPackageType;
import org.jboss.ide.eclipse.packages.core.model.types.IPackageType;

public class ExtensionManager {
	public static final String PACKAGE_TYPES_EXTENSION_ID = "org.jboss.ide.eclilpse.packages.core.packageTypes";
	
	public static IExtension[] findExtension (String extensionId)
	{
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry.getExtensionPoint(extensionId);
		return extensionPoint.getExtensions();
	}
	
	public static IPackageType[] findPackageTypes ()
	{
		ArrayList packageTypes = new ArrayList();
		IExtension[] extensions = findExtension(PACKAGE_TYPES_EXTENSION_ID);
		
		for (int i = 0; i < extensions.length; i++)
		{
			IConfigurationElement elements[] = extensions[i].getConfigurationElements();
			for (int j = 0; j < elements.length; j++)
			{
				try {
					Class packageTypeClass = Class.forName(elements[j].getAttribute("class"));
					AbstractPackageType packageType = (AbstractPackageType) packageTypeClass.newInstance();
					packageType.setId(elements[j].getAttribute("id"));
					packageType.setLabel(elements[j].getAttribute("label"));
					
					packageTypes.add(packageType);
				} catch (InvalidRegistryObjectException e) {
					Trace.trace(ExtensionManager.class, e);
				} catch (ClassNotFoundException e) {
					Trace.trace(ExtensionManager.class, e);
				} catch (InstantiationException e) {
					Trace.trace(ExtensionManager.class, e);
				} catch (IllegalAccessException e) {
					Trace.trace(ExtensionManager.class, e);
				}
			}
		}
		
		return (IPackageType[]) packageTypes.toArray(new IPackageType[packageTypes.size()]);
	}
}
