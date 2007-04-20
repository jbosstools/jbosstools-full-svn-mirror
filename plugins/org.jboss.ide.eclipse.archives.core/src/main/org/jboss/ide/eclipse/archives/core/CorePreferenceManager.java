package org.jboss.ide.eclipse.archives.core;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;

public class CorePreferenceManager extends AbstractPreferenceInitializer {
	public static final String AUTOMATIC_BUILDER_ENABLED = "org.jboss.ide.eclipse.archives.core.automaticBuilderEnabled";
	
	public static boolean isBuilderEnabled(IAdaptable adaptable) {
		QualifiedName name = new QualifiedName(ArchivesCorePlugin.PLUGIN_ID, AUTOMATIC_BUILDER_ENABLED);
		if( adaptable != null ) {
			IResource resource = (IResource)adaptable.getAdapter(IResource.class);
			
			// if the resource is null or the resource has no preference val, use global val
			try {
				if( resource != null && resource.getPersistentProperty(name) != null) {
					return Boolean.parseBoolean(resource.getPersistentProperty(name));
				}
			} catch( CoreException ce ) {}
		}
		return new DefaultScope().getNode(ArchivesCorePlugin.PLUGIN_ID).getBoolean(AUTOMATIC_BUILDER_ENABLED, true);
	}
	
	public static void setBuilderEnabled(IAdaptable adaptable, boolean value) {
		QualifiedName name = new QualifiedName(ArchivesCorePlugin.PLUGIN_ID, AUTOMATIC_BUILDER_ENABLED);
		if( adaptable != null ) {
			IResource resource = (IResource)adaptable.getAdapter(IResource.class);
			
			// if the resource is null or the resource has no preference val, use global val
			try {
				if( resource != null && resource.getPersistentProperty(name) != null) {
					resource.setPersistentProperty(name, new Boolean(value).toString());
					return;
				}
			} catch( CoreException ce ) {}
		}
		new DefaultScope().getNode(ArchivesCorePlugin.PLUGIN_ID).putBoolean(AUTOMATIC_BUILDER_ENABLED, value);
	}
	
	public void initializeDefaultPreferences() {
		new DefaultScope().getNode(ArchivesCorePlugin.PLUGIN_ID).putBoolean(AUTOMATIC_BUILDER_ENABLED, true);
	}
}
