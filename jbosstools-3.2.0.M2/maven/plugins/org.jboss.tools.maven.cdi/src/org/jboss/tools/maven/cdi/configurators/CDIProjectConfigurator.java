package org.jboss.tools.maven.cdi.configurators;

import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.wst.common.componentcore.ModuleCoreNature;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.jboss.tools.maven.cdi.MavenCDIActivator;
import org.jboss.tools.maven.core.IJBossMavenConstants;
import org.jboss.tools.maven.core.internal.project.facet.MavenFacetInstallDataModelProvider;
import org.jboss.tools.maven.ui.Activator;
import org.jboss.tools.maven.cdi.Messages;
import org.maven.ide.eclipse.project.IMavenProjectFacade;
import org.maven.ide.eclipse.project.MavenProjectChangedEvent;
import org.maven.ide.eclipse.project.configurator.AbstractProjectConfigurator;
import org.maven.ide.eclipse.project.configurator.ProjectConfigurationRequest;

public class CDIProjectConfigurator extends AbstractProjectConfigurator {

	private static final String CDI_API_GROUP_ID = "javax.enterprise"; //$NON-NLS-1$
	private static final String CDI_API_ARTIFACT_ID = "cdi-api"; //$NON-NLS-1$
	
	protected static final IProjectFacet dynamicWebFacet;
	protected static final IProjectFacetVersion dynamicWebVersion;
	
	protected static final IProjectFacet cdiFacet;
	protected static final IProjectFacetVersion cdiVersion;
	
	protected static final IProjectFacet m2Facet;
	protected static final IProjectFacetVersion m2Version;
	
	static {
		dynamicWebFacet = ProjectFacetsManager.getProjectFacet("jst.web"); //$NON-NLS-1$
		dynamicWebVersion = dynamicWebFacet.getVersion("2.5");  //$NON-NLS-1$
		cdiFacet = ProjectFacetsManager.getProjectFacet("jst.cdi"); //$NON-NLS-1$
		cdiVersion = cdiFacet.getVersion("1.0"); //$NON-NLS-1$
		m2Facet = ProjectFacetsManager.getProjectFacet("jboss.m2"); //$NON-NLS-1$
		m2Version = m2Facet.getVersion("1.0"); //$NON-NLS-1$
	}
	
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
		boolean configureCDI = store.getBoolean(Activator.CONFIGURE_CDI);
		if (!configureCDI) {
			return;
		}
		
		String packaging = mavenProject.getPackaging();
	    String cdiVersion = getCDIVersion(mavenProject);
	    if (cdiVersion != null) {
	    	final IFacetedProject fproj = ProjectFacetsManager.create(project);
	    	if (fproj != null && "war".equals(packaging)) { //$NON-NLS-1$
	    		installWarFacets(fproj, cdiVersion, monitor);
	    	}
	    }
	}


	@Override
	protected void mavenProjectChanged(MavenProjectChangedEvent event,
			IProgressMonitor monitor) throws CoreException {
		IMavenProjectFacade facade = event.getMavenProject();
	    if(facade != null) {
	      IProject project = facade.getProject();
	      if(isWTPProject(project)) {
	        MavenProject mavenProject = facade.getMavenProject(monitor);
	        configureInternal(mavenProject, project, monitor);
	      }
	    }
		super.mavenProjectChanged(event, monitor);
	}

	private boolean isWTPProject(IProject project) {
	    return ModuleCoreNature.getModuleCoreNature(project) != null;
	 }
	
	private void installM2Facet(IFacetedProject fproj, IProgressMonitor monitor) throws CoreException {
		if (!fproj.hasProjectFacet(m2Facet)) {
			IDataModel config = (IDataModel) new MavenFacetInstallDataModelProvider().create();
			config.setBooleanProperty(IJBossMavenConstants.MAVEN_PROJECT_EXISTS, true);
			fproj.installProjectFacet(m2Version, config, monitor);
		}
	}

	
	private void installWarFacets(IFacetedProject fproj, String cdiVersion,IProgressMonitor monitor) throws CoreException {
		
		if (!fproj.hasProjectFacet(dynamicWebFacet)) {
			MavenCDIActivator.log(Messages.CDIProjectConfigurator_The_project_does_not_contain_the_Web_Module_facet);
		}
		installCDIFacet(fproj, cdiVersion, monitor);
		installM2Facet(fproj, monitor);
		
	}


	private void installCDIFacet(IFacetedProject fproj, String cdiVersionString, IProgressMonitor monitor)
			throws CoreException {
		if (!fproj.hasProjectFacet(cdiFacet)) {
			if (cdiVersionString.startsWith("1.0")) { //$NON-NLS-1$
				IDataModel model = MavenCDIActivator.getDefault().createCDIDataModel(fproj,cdiVersion);
				fproj.installProjectFacet(cdiVersion, model, monitor);	
			}
		}
	}
	
	private String getCDIVersion(MavenProject mavenProject) {
		String version = null;
		version = Activator.getDefault().getDependencyVersion(mavenProject, CDI_API_GROUP_ID, CDI_API_ARTIFACT_ID);
		
	    return version;
	}

}
