package org.jboss.tools.maven.seam.preferences;


import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.jboss.tools.maven.seam.MavenSeamActivator;

public class SeamConfiguratorPreferencesInitializer extends
		AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences node = new DefaultScope().getNode(MavenSeamActivator.PLUGIN_ID);
		
		node.putBoolean(
				MavenSeamActivator.CONFIGURE_SEAM,
				MavenSeamActivator.CONFIGURE_SEAM_VALUE);
		node.putBoolean(
				MavenSeamActivator.CONFIGURE_SEAM_RUNTIME,
				MavenSeamActivator.CONFIGURE_SEAM_RUNTIME_VALUE);
		node.putBoolean(
				MavenSeamActivator.CONFIGURE_SEAM_ARTIFACTS,
				MavenSeamActivator.CONFIGURE_SEAM_ARTIFACTS_VALUE);
		node.putBoolean(
				MavenSeamActivator.CONFIGURE_JSF,
				MavenSeamActivator.CONFIGURE_JSF_VALUE);
		node.putBoolean(
				MavenSeamActivator.CONFIGURE_PORTLET,
				MavenSeamActivator.CONFIGURE_PORTLET_VALUE);
		node.putBoolean(
				MavenSeamActivator.CONFIGURE_JSFPORTLET,
				MavenSeamActivator.CONFIGURE_JSFPORTLET_VALUE);
		node.putBoolean(
				MavenSeamActivator.CONFIGURE_SEAMPORTLET,
				MavenSeamActivator.CONFIGURE_SEAMPORTLET_VALUE);
		node.putBoolean(
				MavenSeamActivator.CONFIGURE_CDI,
				MavenSeamActivator.CONFIGURE_CDI_VALUE);
	}

}
