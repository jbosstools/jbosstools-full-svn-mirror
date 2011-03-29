package org.jboss.tools.maven.hibernate.configurators;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.hibernate.eclipse.console.properties.HibernatePropertiesConstants;
import org.hibernate.eclipse.console.utils.ProjectUtils;
import org.jboss.tools.maven.ui.Activator;
import org.maven.ide.eclipse.project.IMavenProjectFacade;
import org.maven.ide.eclipse.project.MavenProjectChangedEvent;
import org.maven.ide.eclipse.project.configurator.AbstractProjectConfigurator;
import org.maven.ide.eclipse.project.configurator.ProjectConfigurationRequest;
import org.osgi.service.prefs.Preferences;

public class HibernateProjectConfigurator extends AbstractProjectConfigurator {

	private static final String HIBERNATE_GROUP_ID = "org.hibernate"; //$NON-NLS-1$
	private static final String HIBERNATE_ARTIFACT_ID_PREFIX = "hibernate"; //$NON-NLS-1$
	
	@Override
	public void configure(ProjectConfigurationRequest request,
			IProgressMonitor monitor) throws CoreException {
		MavenProject mavenProject = request.getMavenProject();
		IProject project = request.getProject();
		configureInternal(mavenProject,project, monitor);
	}
	
	private void configureInternal(MavenProject mavenProject,IProject project,
			IProgressMonitor monitor) throws CoreException {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		boolean configureHibernate = store.getBoolean(Activator.CONFIGURE_HIBERNATE);
		if (!configureHibernate) {
			return;
		}
		
		if (isHibernateProject(mavenProject)) {
			IScopeContext scope = new ProjectScope(project);
			Preferences node = scope.getNode(HibernatePropertiesConstants.HIBERNATE_CONSOLE_NODE);
			if (node != null) {
				boolean enabled = node.getBoolean(HibernatePropertiesConstants.HIBERNATE3_ENABLED, false);
				if (enabled) {
					return;
				}
			}
			ProjectUtils.toggleHibernateOnProject(project, true, ""); //$NON-NLS-1$
		}
	}


	@Override
	public void mavenProjectChanged(MavenProjectChangedEvent event,
			IProgressMonitor monitor) throws CoreException {
		IMavenProjectFacade facade = event.getMavenProject();
	    if(facade != null) {
	      IProject project = facade.getProject();
	      MavenProject mavenProject = facade.getMavenProject(monitor);
	      configureInternal(mavenProject, project, monitor);
	    }
		super.mavenProjectChanged(event, monitor);
	}

	private boolean isHibernateProject(MavenProject mavenProject) {
		List<Artifact> artifacts = new ArrayList<Artifact>();
		ArtifactFilter filter = new org.apache.maven.artifact.resolver.filter.ScopeArtifactFilter(
				Artifact.SCOPE_TEST);
		for (Artifact artifact : mavenProject.getArtifacts()) {
			if (filter.include(artifact)) {
				artifacts.add(artifact);
			}
		}
        for (Artifact artifact:artifacts) {
	    	String groupId = artifact.getGroupId();
    		if (HIBERNATE_GROUP_ID.equals(groupId)) {
    			String artifactId = artifact.getArtifactId();
    			if (artifactId != null && artifactId.startsWith(HIBERNATE_ARTIFACT_ID_PREFIX)) {
	    			return true;
	    		} 
	    	}
	    }
	    return false;
	}
}
