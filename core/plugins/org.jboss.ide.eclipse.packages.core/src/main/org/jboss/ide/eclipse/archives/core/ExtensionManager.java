package org.jboss.ide.eclipse.archives.core;

import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.jboss.ide.eclipse.archives.core.model.types.IArchiveType;

public class ExtensionManager {
	public static final String ARCHIVE_TYPES_EXTENSION_ID = "org.jboss.ide.eclipse.archives.core.packageTypes";
	
	public static IExtension[] findExtension (String extensionId)
	{
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry.getExtensionPoint(extensionId);
		return extensionPoint.getExtensions();
	}
	
	public static IArchiveType[] findPackageTypes ()
	{
		ArrayList archiveTypes = new ArrayList();
		IExtension[] extensions = findExtension(ARCHIVE_TYPES_EXTENSION_ID);
		
		for (int i = 0; i < extensions.length; i++)
		{
			IConfigurationElement elements[] = extensions[i].getConfigurationElements();
			for (int j = 0; j < elements.length; j++)
			{
				try {
					Object executable = elements[j].createExecutableExtension("class");
					archiveTypes.add((IArchiveType)executable);
				} catch (InvalidRegistryObjectException e) {
					Trace.trace(ExtensionManager.class, e);
				} catch( CoreException e ) {
					Trace.trace(ExtensionManager.class, e);
				}
			}
		}
		
		return (IArchiveType[]) archiveTypes.toArray(new IArchiveType[archiveTypes.size()]);
	}
	
	private static Hashtable archiveTypes;
	public static IArchiveType getArchiveType (String packageType) {
		if (archiveTypes == null)
		{
			archiveTypes = new Hashtable();
			IArchiveType[] registeredTypes = ExtensionManager.findPackageTypes();
			for (int i = 0; i < registeredTypes.length; i++)	
			{
				archiveTypes.put(registeredTypes[i].getId(), registeredTypes[i]);
			}
		}
		return (IArchiveType) archiveTypes.get(packageType);
	}

	
}
