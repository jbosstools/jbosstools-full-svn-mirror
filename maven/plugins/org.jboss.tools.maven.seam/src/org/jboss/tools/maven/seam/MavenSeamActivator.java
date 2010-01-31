package org.jboss.tools.maven.seam;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.common.project.facet.core.libprov.ILibraryProvider;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryInstallDelegate;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryProviderFramework;
import org.eclipse.jst.jsf.core.internal.project.facet.IJSFFacetInstallDataModelProperties;
import org.eclipse.jst.jsf.core.internal.project.facet.JSFFacetInstallDataModelProvider;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.FacetedProjectFramework;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.jboss.tools.maven.core.IJBossMavenConstants;
import org.jboss.tools.maven.core.MavenCoreActivator;
import org.jboss.tools.seam.core.SeamUtil;
import org.jboss.tools.seam.core.project.facet.SeamRuntime;
import org.jboss.tools.seam.core.project.facet.SeamRuntimeManager;
import org.jboss.tools.seam.core.project.facet.SeamVersion;
import org.jboss.tools.seam.internal.core.project.facet.ISeamFacetDataModelProperties;
import org.jboss.tools.seam.internal.core.project.facet.SeamFacetAbstractInstallDelegate;
import org.maven.ide.eclipse.MavenPlugin;
import org.maven.ide.eclipse.core.IMavenConstants;
import org.maven.ide.eclipse.embedder.MavenModelManager;
import org.maven.ide.eclipse.project.MavenProjectManager;
import org.maven.ide.eclipse.project.ResolverConfiguration;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class MavenSeamActivator extends AbstractUIPlugin {

	private static final String WAR_ARCHIVE_SUFFIX = ".war"; //$NON-NLS-1$
	
	private static final String EJB_ARCHIVE_SUFFIX = ".jar"; //$NON-NLS-1$

	private static final String TEST_SUFFIX = "-test"; //$NON-NLS-1$

	private static final String EJB_SUFFIX = "-ejb"; //$NON-NLS-1$

	private static final String EAR_SUFFIX = "-ear"; //$NON-NLS-1$

	private static final String PARENT_SUFFIX = "-parent"; //$NON-NLS-1$

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.maven.seam"; //$NON-NLS-1$

	public static final String CONFIGURE_SEAM = "configureSeam"; //$NON-NLS-1$

	public static final String CONFIGURE_PORTLET = "configurePortlet"; //$NON-NLS-1$

	public static final boolean CONFIGURE_SEAM_VALUE = true;

	public static final String CONFIGURE_SEAM_RUNTIME = "configureSeamRuntime"; //$NON-NLS-1$
  
	public static final boolean CONFIGURE_SEAM_RUNTIME_VALUE = true;

	public static final String CONFIGURE_SEAM_ARTIFACTS = "configureSeamArtifacts"; //$NON-NLS-1$
	
	public static final boolean CONFIGURE_SEAM_ARTIFACTS_VALUE = true;

	public static final String CONFIGURE_JSF = "configureJSF"; //$NON-NLS-1$
	
	public static final boolean CONFIGURE_JSF_VALUE = true;

	public static final boolean CONFIGURE_PORTLET_VALUE = true;

	public static final String CONFIGURE_JSFPORTLET = "configureJSFPortlet"; //$NON-NLS-1$
	
	public static final boolean CONFIGURE_JSFPORTLET_VALUE = true;

	public static final String CONFIGURE_SEAMPORTLET = "configureSeamPortlet"; //$NON-NLS-1$
	
	public static final boolean CONFIGURE_SEAMPORTLET_VALUE = true;
	
	// The shared instance
	private static MavenSeamActivator plugin;

	private String webProjectName;
	private String artifactId;

	private String parentProjectName;
	private String parentArtifactId;

	private String earProjectName;
	private String earArtifactId;

	private String ejbProjectName;
	private String ejbArtifactId;

	private String testProjectName;
	private String testArtifactId;

	/**
	 * The constructor
	 */
	public MavenSeamActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static MavenSeamActivator getDefault() {
		return plugin;
	}

	
	public void configureSeamProject(IDataModel seamFacetModel,
			IDataModel m2FacetModel) {
		Assert.isNotNull(seamFacetModel);
		Assert.isNotNull(m2FacetModel);
		webProjectName = seamFacetModel.getStringProperty(IFacetDataModelProperties.FACET_PROJECT_NAME);
		artifactId = m2FacetModel.getStringProperty(IJBossMavenConstants.ARTIFACT_ID);
		parentProjectName = webProjectName + PARENT_SUFFIX;
		parentArtifactId = artifactId + PARENT_SUFFIX;
		testProjectName = webProjectName + TEST_SUFFIX;
		testArtifactId = artifactId + TEST_SUFFIX;
		earProjectName = webProjectName + EAR_SUFFIX;
		earArtifactId = artifactId + EAR_SUFFIX;
		ejbProjectName = webProjectName + EJB_SUFFIX;
		ejbArtifactId = artifactId + EJB_SUFFIX;
		configureParentProject(m2FacetModel, seamFacetModel);
		configureWarProject(m2FacetModel, seamFacetModel);
		configureTestProject(m2FacetModel, seamFacetModel);
		if (!SeamFacetAbstractInstallDelegate
				.isWarConfiguration(seamFacetModel)) {
			configureEjbProject(m2FacetModel, seamFacetModel);
			configureEarProject(m2FacetModel, seamFacetModel);
			
		} 
	}

	private void configureTestProject(IDataModel m2FacetModel,
			IDataModel seamFacetModel) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(testProjectName);
		if (project == null || !project.exists()) {
			return;
		}
		IFile pom = project.getFile(IMavenConstants.POM_FILE_NAME);
		IJavaProject javaProject = JavaCore.create(project);
		if (!pom.exists()) {
			Model model = new Model();
			model.setModelVersion(IJBossMavenConstants.MAVEN_MODEL_VERSION);
			model.setGroupId(m2FacetModel
					.getStringProperty(IJBossMavenConstants.GROUP_ID));
			model.setArtifactId(testArtifactId);
			model.setVersion(m2FacetModel
					.getStringProperty(IJBossMavenConstants.VERSION));
			model.setName(m2FacetModel.getStringProperty(IJBossMavenConstants.NAME) + " - test"); //$NON-NLS-1$
			model.setPackaging("jar"); //$NON-NLS-1$
			model.setDescription(m2FacetModel
					.getStringProperty(IJBossMavenConstants.DESCRIPTION));
			
			Parent parent = new Parent();
			parent.setArtifactId(parentArtifactId);
			parent.setGroupId(m2FacetModel.getStringProperty(IJBossMavenConstants.GROUP_ID));
			parent.setVersion(m2FacetModel.getStringProperty(IJBossMavenConstants.VERSION));
			model.setParent(parent);
			
			List dependencies = model.getDependencies();
			
			Dependency dependency = new Dependency();
			dependency.setGroupId("org.jboss.seam.embedded"); //$NON-NLS-1$
			dependency.setArtifactId("hibernate-all"); //$NON-NLS-1$
			dependencies.add(dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("org.jboss.seam.embedded"); //$NON-NLS-1$
			dependency.setArtifactId("jboss-embedded-all"); //$NON-NLS-1$
			dependencies.add(dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("org.jboss.seam.embedded"); //$NON-NLS-1$
			dependency.setArtifactId("thirdparty-all"); //$NON-NLS-1$
			dependencies.add(dependency);
			
			dependency = getSeamDependency();
			dependency.setScope("test"); //$NON-NLS-1$
			dependencies.add(dependency);
			
			dependency = getJSFApi();
			dependency.setScope("test"); //$NON-NLS-1$
			dependencies.add(dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("javax.activation"); //$NON-NLS-1$
			dependency.setArtifactId("activation"); //$NON-NLS-1$
			dependencies.add(dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("org.testng"); //$NON-NLS-1$
			dependency.setArtifactId("testng"); //$NON-NLS-1$
			// FIXME
			dependency.setVersion("${testng.version}"); //$NON-NLS-1$
			dependency.setClassifier("jdk15"); //$NON-NLS-1$
			dependency.setScope("test"); //$NON-NLS-1$
			dependencies.add(dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("org.slf4j"); //$NON-NLS-1$
			dependency.setArtifactId("slf4j-api"); //$NON-NLS-1$
			dependencies.add(dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("org.slf4j"); //$NON-NLS-1$
			dependency.setArtifactId("slf4j-nop"); //$NON-NLS-1$
			dependencies.add(dependency);
			
			Build build = new Build();
			try {
				//build.setFinalName(testProjectName);
				String sourceDirectory = MavenCoreActivator.getSourceDirectory(javaProject);
				if (sourceDirectory != null) {
					build.setSourceDirectory(sourceDirectory);
				}		
				String outputDirectory = MavenCoreActivator.getOutputDirectory(javaProject);	
				build.setOutputDirectory(outputDirectory);
				MavenCoreActivator.addResource(build, project, sourceDirectory);
				model.setBuild(build);
				MavenCoreActivator.createMavenProject(testProjectName, null, model, true);
			} catch (Exception e) {
				MavenSeamActivator.log(e);
			}
			
		}
		
	}

	private void configureEarProject(IDataModel m2FacetModel,
			IDataModel seamFacetModel) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(earProjectName);
		IFile pom = project.getFile(IMavenConstants.POM_FILE_NAME);
		//IJavaProject javaProject = JavaCore.create(project);
		if (!pom.exists()) {
			Model model = new Model();
			model.setModelVersion(IJBossMavenConstants.MAVEN_MODEL_VERSION);
			model.setGroupId(m2FacetModel
					.getStringProperty(IJBossMavenConstants.GROUP_ID));
			model.setArtifactId(earArtifactId);
			model.setVersion(m2FacetModel
					.getStringProperty(IJBossMavenConstants.VERSION));
			model.setName(m2FacetModel.getStringProperty(IJBossMavenConstants.NAME) + " - EAR"); //$NON-NLS-1$
			model.setPackaging("ear"); //$NON-NLS-1$
			model.setDescription(m2FacetModel
					.getStringProperty(IJBossMavenConstants.DESCRIPTION));
			
			Parent parent = new Parent();
			parent.setArtifactId(parentArtifactId);
			parent.setGroupId(m2FacetModel.getStringProperty(IJBossMavenConstants.GROUP_ID));
			parent.setVersion(m2FacetModel.getStringProperty(IJBossMavenConstants.VERSION));
			model.setParent(parent);
			
			List dependencies = model.getDependencies();
			
			Dependency dependency = new Dependency();
			dependency.setGroupId(m2FacetModel.getStringProperty(IJBossMavenConstants.GROUP_ID));
			dependency.setArtifactId(ejbProjectName);
			dependency.setVersion(m2FacetModel.getStringProperty(IJBossMavenConstants.VERSION));
			dependency.setType("ejb"); //$NON-NLS-1$
			dependency.setScope("runtime"); //$NON-NLS-1$
			dependencies.add(dependency);
			
			dependency = new Dependency();
			dependency.setGroupId(m2FacetModel.getStringProperty(IJBossMavenConstants.GROUP_ID));
			dependency.setArtifactId(webProjectName);
			dependency.setVersion(m2FacetModel.getStringProperty(IJBossMavenConstants.VERSION));
			dependency.setType("war"); //$NON-NLS-1$
			dependency.setScope("runtime"); //$NON-NLS-1$
			dependencies.add(dependency);
			
			dependency = getSeamDependency();
			dependency.setVersion("${seam.version}"); //$NON-NLS-1$
			dependency.setType("ejb"); //$NON-NLS-1$
			dependency.setScope("compile"); //$NON-NLS-1$
			List exclusions = dependency.getExclusions();
			Exclusion exclusion = new Exclusion();
			exclusion.setGroupId("javassist"); //$NON-NLS-1$
			exclusion.setArtifactId("javassist"); //$NON-NLS-1$
			exclusions.add(exclusion);
			
			exclusion = new Exclusion();
			exclusion.setGroupId("javax.el"); //$NON-NLS-1$
			exclusion.setArtifactId("el-api"); //$NON-NLS-1$
			exclusions.add(exclusion);
			
			exclusion = new Exclusion();
			exclusion.setGroupId("dom4j"); //$NON-NLS-1$
			exclusion.setArtifactId("dom4j"); //$NON-NLS-1$
			exclusions.add(exclusion);
			
			exclusion = new Exclusion();
			exclusion.setGroupId("xstream"); //$NON-NLS-1$
			exclusion.setArtifactId("xstream"); //$NON-NLS-1$
			exclusions.add(exclusion);
			
			exclusion = new Exclusion();
			exclusion.setGroupId("xpp3"); //$NON-NLS-1$
			exclusion.setArtifactId("xpp3_min"); //$NON-NLS-1$
			exclusions.add(exclusion);
			
			dependencies.add(dependency);
			
			dependency = getRichFacesApi();
			dependency.setType("jar"); //$NON-NLS-1$
			dependency.setScope("compile"); //$NON-NLS-1$
			exclusions = dependency.getExclusions();
			exclusion = new Exclusion();
			exclusion.setGroupId("commons-collections"); //$NON-NLS-1$
			exclusion.setArtifactId("commons-collections"); //$NON-NLS-1$
			exclusions.add(exclusion);
			exclusion = new Exclusion();
			exclusion.setGroupId("commons-logging"); //$NON-NLS-1$
			exclusion.setArtifactId("commons-logging"); //$NON-NLS-1$
			exclusions.add(exclusion);
			
			dependencies.add(dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("org.drools"); //$NON-NLS-1$
			dependency.setArtifactId("drools-compiler"); //$NON-NLS-1$
			dependency.setType("jar"); //$NON-NLS-1$
			dependency.setScope("compile"); //$NON-NLS-1$
			dependencies.add(dependency);
			
			dependency = new Dependency();
			String jbpmGroupId = "org.jbpm"; //$NON-NLS-1$
			// JBoss EAP 5.0 requires org.jbpm.jbpm3
			SeamRuntime seamRuntime = SeamRuntimeManager.getInstance().findRuntimeByName(seamFacetModel.getProperty(ISeamFacetDataModelProperties.SEAM_RUNTIME_NAME).toString());
			if(seamRuntime!=null) {
				SeamVersion seamVersion = seamRuntime.getVersion();
				if (SeamVersion.SEAM_2_2.equals(seamVersion)) {
					String fullVersion = SeamUtil.getSeamVersionFromManifest(seamRuntime);
					if (fullVersion != null && fullVersion.contains("EAP")) { //$NON-NLS-1$
						jbpmGroupId = "org.jbpm.jbpm3"; //$NON-NLS-1$
					}
				}
			}
			dependency.setGroupId(jbpmGroupId);
			dependency.setArtifactId("jbpm-jpdl"); //$NON-NLS-1$
			dependency.setType("jar"); //$NON-NLS-1$
			dependency.setScope("compile"); //$NON-NLS-1$
			dependencies.add(dependency);
			
//			dependency = new Dependency();
//			dependency.setGroupId("org.mvel");
//			if ("org.jbpm.jbpm3".equals(jbpmGroupId)) {
//				dependency.setArtifactId("mvel2");
//			} else {
//				dependency.setArtifactId("mvel14");
//			}
//			dependency.setType("jar");
//			dependency.setScope("compile");
//			dependencies.add(dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("commons-digester"); //$NON-NLS-1$
			dependency.setArtifactId("commons-digester"); //$NON-NLS-1$
			dependencies.add(dependency);
			
			Build build = new Build();
			try {
				build.setFinalName(earProjectName);
				
				String sourceDirectory = MavenCoreActivator.getEarRoot(project);
				if (sourceDirectory != null) {
					build.setSourceDirectory(sourceDirectory);
				}
				build.setOutputDirectory("target/classes"); //$NON-NLS-1$
				MavenCoreActivator.addMavenEarPlugin(build, project, m2FacetModel, true);
				model.setBuild(build);
				MavenCoreActivator.createMavenProject(earProjectName, null, model, true);
				removeWTPContainers(m2FacetModel, project);
				// configureApplicationXml(project, m2FacetModel, null);
				//removeRuntime(project);
				//IProject ejbProject = ResourcesPlugin.getWorkspace().getRoot().getProject(ejbProjectName);
				//removeRuntime(ejbProject);
				//EarFacetRuntimeHandler.updateModuleProjectRuntime(project, ejbProject, null);
				//IProject webProject = ResourcesPlugin.getWorkspace().getRoot().getProject(webProjectName);
				//removeRuntime(webProject);
				//EarFacetRuntimeHandler.updateModuleProjectRuntime(project, webProject, null);
			} catch (Exception e) {
				MavenSeamActivator.log(e);
			}
			
		}
	}
	
	
	private void removeRuntime(IProject project) throws CoreException {
		IFacetedProject facetedProject = ProjectFacetsManager.create( project );
		facetedProject.setRuntime(null, null);
	
	}

	private void configureEjbProject(IDataModel m2FacetModel,
			IDataModel seamFacetModel) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(ejbProjectName);
		IFile pom = project.getFile(IMavenConstants.POM_FILE_NAME);
		IJavaProject javaProject = JavaCore.create(project);
		if (!pom.exists()) {
			Model model = new Model();
			model.setModelVersion(IJBossMavenConstants.MAVEN_MODEL_VERSION);
			model.setGroupId(m2FacetModel
					.getStringProperty(IJBossMavenConstants.GROUP_ID));
			model.setArtifactId(ejbArtifactId);
			model.setVersion(m2FacetModel
					.getStringProperty(IJBossMavenConstants.VERSION));
			model.setName(m2FacetModel.getStringProperty(IJBossMavenConstants.NAME) + " - EJB"); //$NON-NLS-1$
			model.setPackaging("ejb"); //$NON-NLS-1$
			model.setDescription(m2FacetModel
					.getStringProperty(IJBossMavenConstants.DESCRIPTION));
			
			Parent parent = new Parent();
			parent.setArtifactId(parentArtifactId);
			parent.setGroupId(m2FacetModel.getStringProperty(IJBossMavenConstants.GROUP_ID));
			parent.setVersion(m2FacetModel.getStringProperty(IJBossMavenConstants.VERSION));
			model.setParent(parent);
			
			List dependencies = model.getDependencies();
			
			Dependency dependency = getSeamDependency();
			dependency.setScope("provided"); //$NON-NLS-1$
			dependencies.add(dependency);
			dependencies.add(getJSFApi());
			dependencies.add(getRichFacesApi());
			
			dependency = new Dependency();
			dependency.setGroupId("javax.ejb"); //$NON-NLS-1$
			dependency.setArtifactId("ejb-api"); //$NON-NLS-1$
			dependencies.add(dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("javax.annotation"); //$NON-NLS-1$
			dependency.setArtifactId("jsr250-api"); //$NON-NLS-1$
			dependencies.add(dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("javax.persistence"); //$NON-NLS-1$
			dependency.setArtifactId("persistence-api"); //$NON-NLS-1$
			dependencies.add(dependency);
			
			dependencies.add(getHibernateAnnotations());
			dependencies.add(getHibernateCommonAnnotations());
			dependencies.add(getHibernateValidator());
			
			Build build = new Build();
			try {
				// FIXME
				//build.setFinalName(ejbArtifactId);
				String outputDirectory = MavenCoreActivator.getOutputDirectory(javaProject);	
				build.setOutputDirectory(outputDirectory);
				String sourceDirectory = MavenCoreActivator.getSourceDirectory(javaProject);
				if (sourceDirectory != null) {
					build.setSourceDirectory(sourceDirectory);
				}
				MavenCoreActivator.addMavenEjbPlugin(build, project);
				model.setBuild(build);
				MavenCoreActivator.createMavenProject(ejbProjectName, null, model, true);
				removeWTPContainers(m2FacetModel, project);
			} catch (Exception e) {
				MavenSeamActivator.log(e);
			}
			
		}
		
	}

	private void configureWarProject(IDataModel m2FacetModel,IDataModel seamFacetModel) {
		try {
			IProject webProject = ResourcesPlugin.getWorkspace().getRoot().getProject(webProjectName);
			
			IFile pomFile = webProject.getFile(IMavenConstants.POM_FILE_NAME);
			MavenModelManager modelManager = MavenPlugin.getDefault().getMavenModelManager();
			
			String artifactId = parentProjectName;
			String groupId = m2FacetModel.getStringProperty(IJBossMavenConstants.GROUP_ID);
			String version = m2FacetModel.getStringProperty(IJBossMavenConstants.VERSION);
			modelManager.updateProject(pomFile, new ParentAdder(groupId, artifactId, version));
			
			Dependency dependency = getHibernateValidator();
			//dependency.setScope("provided");
			modelManager.addDependency(pomFile,dependency);
			
			dependency = getHibernateAnnotations();
			modelManager.addDependency(pomFile,dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("org.hibernate"); //$NON-NLS-1$
			dependency.setArtifactId("hibernate-entitymanager"); //$NON-NLS-1$
			modelManager.addDependency(pomFile,dependency);
			
			dependency = getSeamDependency();
			if (!SeamFacetAbstractInstallDelegate
					.isWarConfiguration(seamFacetModel)) {
				dependency.setScope("provided"); //$NON-NLS-1$
			}
			modelManager.addDependency(pomFile,dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("org.jboss.seam"); //$NON-NLS-1$
			dependency.setArtifactId("jboss-seam-ui"); //$NON-NLS-1$
			List<Exclusion> exclusions = dependency.getExclusions();
			Exclusion exclusion = new Exclusion();
			exclusion.setGroupId("org.jboss.seam"); //$NON-NLS-1$
			exclusion.setArtifactId("jboss-seam"); //$NON-NLS-1$
			exclusions.add(exclusion);
			modelManager.addDependency(pomFile,dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("org.jboss.seam"); //$NON-NLS-1$
			dependency.setArtifactId("jboss-seam-ioc"); //$NON-NLS-1$
			exclusions = dependency.getExclusions();
			exclusion = new Exclusion();
			exclusion.setGroupId("org.jboss.seam"); //$NON-NLS-1$
			exclusion.setArtifactId("jboss-seam"); //$NON-NLS-1$
			exclusions.add(exclusion);
			modelManager.addDependency(pomFile,dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("org.jboss.seam"); //$NON-NLS-1$
			dependency.setArtifactId("jboss-seam-debug"); //$NON-NLS-1$
			// FIXME
			dependency.setVersion("${seam.version}"); //$NON-NLS-1$
			
			modelManager.addDependency(pomFile,dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("org.jboss.seam"); //$NON-NLS-1$
			dependency.setArtifactId("jboss-seam-mail"); //$NON-NLS-1$
			
			modelManager.addDependency(pomFile,dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("org.jboss.seam"); //$NON-NLS-1$
			dependency.setArtifactId("jboss-seam-pdf"); //$NON-NLS-1$
			
			modelManager.addDependency(pomFile,dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("org.jboss.seam"); //$NON-NLS-1$
			dependency.setArtifactId("jboss-seam-remoting"); //$NON-NLS-1$
			
			modelManager.addDependency(pomFile,dependency);
			
			if (FacetedProjectFramework.hasProjectFacet(webProject, ISeamFacetDataModelProperties.SEAM_FACET_ID, ISeamFacetDataModelProperties.SEAM_FACET_VERSION_21)) {
				dependency = new Dependency();
				dependency.setGroupId("org.jboss.seam"); //$NON-NLS-1$
				dependency.setArtifactId("jboss-seam-excel"); //$NON-NLS-1$
				
				modelManager.addDependency(pomFile,dependency);
			}
			
			dependency = new Dependency();
			dependency.setGroupId("javax.servlet"); //$NON-NLS-1$
			dependency.setArtifactId("servlet-api"); //$NON-NLS-1$
			modelManager.addDependency(pomFile,dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("org.richfaces.ui"); //$NON-NLS-1$
			dependency.setArtifactId("richfaces-ui"); //$NON-NLS-1$
			modelManager.addDependency(pomFile,dependency);
			
			dependency = getRichFacesApi();
			if (!SeamFacetAbstractInstallDelegate
					.isWarConfiguration(seamFacetModel)) {
				dependency.setScope("provided"); //$NON-NLS-1$
			}
			modelManager.addDependency(pomFile,dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("org.richfaces.framework"); //$NON-NLS-1$
			dependency.setArtifactId("richfaces-impl"); //$NON-NLS-1$
			modelManager.addDependency(pomFile,dependency);
			
			dependency = getJSFApi();
			modelManager.addDependency(pomFile,dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("javax.faces"); //$NON-NLS-1$
			dependency.setArtifactId("jsf-impl"); //$NON-NLS-1$
			modelManager.addDependency(pomFile,dependency);
			
			dependency = new Dependency();
			dependency.setGroupId("javax.el"); //$NON-NLS-1$
			dependency.setArtifactId("el-api"); //$NON-NLS-1$
			modelManager.addDependency(pomFile,dependency);
			
			if (SeamFacetAbstractInstallDelegate
					.isWarConfiguration(seamFacetModel)) {
				dependency = new Dependency();
				dependency.setGroupId("org.drools"); //$NON-NLS-1$
				dependency.setArtifactId("drools-compiler"); //$NON-NLS-1$
				dependency.setType("jar"); //$NON-NLS-1$
				dependency.setScope("compile"); //$NON-NLS-1$
				modelManager.addDependency(pomFile,dependency);
				
				dependency = new Dependency();
				dependency.setGroupId("org.jbpm"); //$NON-NLS-1$
				dependency.setArtifactId("jbpm-jpdl"); //$NON-NLS-1$
				dependency.setType("jar"); //$NON-NLS-1$
				dependency.setScope("compile"); //$NON-NLS-1$
				modelManager.addDependency(pomFile,dependency);
				
				dependency = new Dependency();
				dependency.setGroupId("commons-digester"); //$NON-NLS-1$
				dependency.setArtifactId("commons-digester"); //$NON-NLS-1$
				modelManager.addDependency(pomFile,dependency);
			}
			
			// ejb project
			
			if (!SeamFacetAbstractInstallDelegate
					.isWarConfiguration(seamFacetModel)) {
				dependency = new Dependency();
				dependency.setGroupId(m2FacetModel.getStringProperty(IJBossMavenConstants.GROUP_ID));
				dependency.setArtifactId(ejbProjectName);
				dependency.setVersion(m2FacetModel.getStringProperty(IJBossMavenConstants.VERSION));
				dependency.setType("ejb"); //$NON-NLS-1$
				dependency.setScope("provided"); //$NON-NLS-1$
				modelManager.addDependency(pomFile,dependency);
			}
			removeWTPContainers(m2FacetModel, webProject);
		} catch (Exception e) {
			MavenSeamActivator.log(e);
		}
	}

	private void removeWTPContainers(IDataModel m2FacetModel,
			IProject webProject) throws JavaModelException {
		if (m2FacetModel.getBooleanProperty(IJBossMavenConstants.REMOVE_WTP_CLASSPATH_CONTAINERS)) {
			IJavaProject javaProject = JavaCore.create(webProject);
			IClasspathEntry[] entries = javaProject.getRawClasspath();
			List<IClasspathEntry> newEntries = new ArrayList<IClasspathEntry>();
			for (int i = 0; i < entries.length; i++) {
				IClasspathEntry entry = entries[i];
				boolean add = true;
				if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
					// FIXME
					IPath path = entry.getPath();
					if (path != null) {
						String value = path.toString();
						if (value.startsWith("org.eclipse.jst")) { //$NON-NLS-1$
							add = false;
						}
					}
				}
				if (add) {
					newEntries.add(entry);
				}
			}
			javaProject.setRawClasspath(newEntries.toArray(new IClasspathEntry[0]), null);
		}
	}

	private Dependency getHibernateValidator() {
		Dependency dependency = new Dependency();
		dependency.setGroupId("org.hibernate"); //$NON-NLS-1$
		dependency.setArtifactId("hibernate-validator"); //$NON-NLS-1$
		return dependency;
	}

	private Dependency getHibernateAnnotations() {
		Dependency dependency;
		dependency = new Dependency();
		dependency.setGroupId("org.hibernate"); //$NON-NLS-1$
		dependency.setArtifactId("hibernate-annotations"); //$NON-NLS-1$
		return dependency;
	}
	
	private Dependency getHibernateCommonAnnotations() {
		Dependency dependency;
		dependency = new Dependency();
		dependency.setGroupId("org.hibernate"); //$NON-NLS-1$
		dependency.setArtifactId("hibernate-commons-annotations"); //$NON-NLS-1$
		return dependency;
	}

	private Dependency getRichFacesApi() {
		Dependency dependency;
		dependency = new Dependency();
		dependency.setGroupId("org.richfaces.framework"); //$NON-NLS-1$
		dependency.setArtifactId("richfaces-api"); //$NON-NLS-1$
		return dependency;
	}

	private Dependency getJSFApi() {
		Dependency dependency;
		dependency = new Dependency();
		dependency.setGroupId("javax.faces"); //$NON-NLS-1$
		dependency.setArtifactId("jsf-api"); //$NON-NLS-1$
		return dependency;
	}

	private Dependency getSeamDependency() {
		Dependency dependency;
		dependency = new Dependency();
		dependency.setGroupId("org.jboss.seam"); //$NON-NLS-1$
		dependency.setArtifactId("jboss-seam"); //$NON-NLS-1$
		return dependency;
	}

	private void configureParentProject(IDataModel m2FacetModel, IDataModel seamFacetModel) {
		Bundle bundle = getDefault().getBundle();
		URL parentPomEntryURL = bundle.getEntry("/poms/parent-pom.xml"); //$NON-NLS-1$
		InputStream inputStream = null;
		try {
			URL resolvedURL = FileLocator.resolve(parentPomEntryURL);
			MavenModelManager modelManager = MavenPlugin.getDefault().getMavenModelManager();
			inputStream = resolvedURL.openStream();
			Model model = modelManager.readMavenModel(inputStream);
			model.setArtifactId(parentArtifactId);
			model.setGroupId(m2FacetModel.getStringProperty(IJBossMavenConstants.GROUP_ID));
			String projectVersion = m2FacetModel.getStringProperty(IJBossMavenConstants.VERSION);
			String name = m2FacetModel.getStringProperty(IJBossMavenConstants.NAME);
			if (name != null && name.trim().length() > 0) {
				model.setName(name + " - parent"); //$NON-NLS-1$
			}
			String description= m2FacetModel.getStringProperty(IJBossMavenConstants.DESCRIPTION);
			if (description != null && description.trim().length() > 0) {
				model.setDescription(description + " - parent"); //$NON-NLS-1$
			}
			model.setVersion(projectVersion);
			
			Properties properties = model.getProperties();
			properties.put(IJBossMavenConstants.PROJECT_VERSION, projectVersion);
			SeamRuntime seamRuntime = SeamRuntimeManager.getInstance().findRuntimeByName(seamFacetModel.getProperty(ISeamFacetDataModelProperties.SEAM_RUNTIME_NAME).toString());
			if(seamRuntime==null) {
				getDefault().log(Messages.MavenSeamActivator_Cannot_get_seam_runtime + seamFacetModel.getProperty(ISeamFacetDataModelProperties.SEAM_RUNTIME_NAME).toString());
			}
			String seamVersion = m2FacetModel.getStringProperty(IJBossMavenConstants.SEAM_MAVEN_VERSION);
			if (seamVersion != null && seamVersion.trim().length() > 0) {
				properties.put(IJBossMavenConstants.SEAM_VERSION, seamVersion);
			}
			String seamHomePath = seamRuntime.getHomeDir();
			File seamHomeDir = new File(seamHomePath);
			if (seamHomeDir.exists()) {
				//String seamVersion = SeamUtil.getSeamVersionFromManifest(seamRuntime.getHomeDir());
				//properties.put(IJBossMavenConstants.SEAM_VERSION, seamVersion);
				File buildDir = new File(seamHomeDir,"build"); //$NON-NLS-1$
				File rootPom = new File(buildDir,"root.pom.xml"); //$NON-NLS-1$
				if (!rootPom.exists()) {
					MavenSeamActivator.log(NLS.bind(Messages.MavenSeamActivator_The_file_does_not_exist, rootPom.getAbsolutePath()));
				} else {
					try {
						Model rootPomModel = modelManager.readMavenModel(rootPom);
						List<Dependency> seamDependencies = rootPomModel.getDependencyManagement().getDependencies();
						setArtifactVersion("jsf.version", properties, "javax.faces", "jsf-api", seamDependencies); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						String richfacesVersion = setArtifactVersion("richfaces.version", properties, "org.richfaces.framework", "richfaces-impl", seamDependencies); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						if (richfacesVersion == null) {
							Properties seamProperties = rootPomModel.getProperties();
							richfacesVersion = seamProperties.getProperty("version.richfaces"); //$NON-NLS-1$
							if (richfacesVersion != null) {
								properties.put("richfaces.version", richfacesVersion); //$NON-NLS-1$
							}
						}
						setArtifactVersion("hibernate-validator.version", properties, "org.hibernate", "hibernate-validator", seamDependencies); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						setArtifactVersion("hibernate-annotations.version", properties, "org.hibernate", "hibernate-annotations", seamDependencies); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						setArtifactVersion("hibernate-entitymanager.version", properties, "org.hibernate", "hibernate-entitymanager", seamDependencies); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						//setArtifactVersion("testng.version", properties, "org.hibernate", "hibernate-entitymanager", seamDependencies);
						//if (seamVersion != null && "2.2".equals(seamVersion.subSequence(0, 3))) {
						//	properties.put("testng.version", "5.9");
						//}
						setArtifactVersion("jboss.embedded.version", properties, "org.jboss.seam.embedded", "jboss-embedded-api", seamDependencies); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						setArtifactVersion("slf4j.version", properties, "org.slf4j", "slf4j-api", seamDependencies); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						setArtifactVersion("ejb.api.version", properties, "javax.ejb", "ejb-api", seamDependencies); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						setArtifactVersion("jsr250-api.version", properties, "javax.annotation", "jsr250-api", seamDependencies); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						setArtifactVersion("persistence-api.version", properties, "javax.persistence", "persistence-api", seamDependencies); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						setArtifactVersion("servlet.version", properties, "javax.servlet", "servlet-api", seamDependencies); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						setArtifactVersion("javax.el.version", properties, "javax.el", "el-api", seamDependencies); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						String droolsVersion = setArtifactVersion("drools.version", properties, "org.drools", "drools-core", seamDependencies); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						if (droolsVersion == null) {
							Properties seamProperties = rootPomModel.getProperties();
							droolsVersion = seamProperties.getProperty("version.drools"); //$NON-NLS-1$
							if (droolsVersion != null) {
								properties.put("drools.version", droolsVersion); //$NON-NLS-1$
							}
						}
						String jbpmVersion = setArtifactVersion("jbpm.version", properties, "org.jbpm", "jbpm-jpdl", seamDependencies); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						if (jbpmVersion == null) {
							setArtifactVersion("jbpm3.version", properties, "org.jbpm.jbpm3", "jbpm-jpdl", seamDependencies); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						}
						//setArtifactVersion("mvel.version", properties, "org.mvel", "mvel14", seamDependencies);
								        
//				        <javax.activation.version>1.1</javax.activation.version>
//				        <hibernate-commons-annotations.version>3.3.0.ga</hibernate-commons-annotations.version>
//				        <commons.digester.version>1.8</commons.digester.version>
//				        <mvel.version>1.2.21</mvel.version>
						
					} catch (Exception e) {
						getDefault().log(e);
					}
				}
			} else {
				MavenSeamActivator.log(NLS.bind(Messages.MavenSeamActivator_The_folder_does_not_exist, seamHomePath));
			}
			
			List<String> modules = model.getModules();
			modules.add("../" + artifactId); //$NON-NLS-1$
			if (!SeamFacetAbstractInstallDelegate
					.isWarConfiguration(seamFacetModel)) {
				modules.add("../" + ejbArtifactId); //$NON-NLS-1$
				modules.add("../" + earArtifactId); //$NON-NLS-1$
			}
			webProjectName = seamFacetModel.getStringProperty(IFacetDataModelProperties.FACET_PROJECT_NAME);
			
			IProject seamWebProject = ResourcesPlugin.getWorkspace().getRoot().getProject(webProjectName);
			IPath location = seamWebProject.getLocation().removeLastSegments(1);
			location = location.append(parentProjectName);
			MavenCoreActivator.createMavenProject(parentProjectName, null, model, false, location);
			// disable workspace resolution
			MavenProjectManager projectManager = MavenPlugin.getDefault().getMavenProjectManager();
		    IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(parentProjectName);
		    ResolverConfiguration configuration = projectManager.getResolverConfiguration(project);
		    configuration.setResolveWorkspaceProjects(false);
		    projectManager.setResolverConfiguration(project, configuration);
		} catch (Exception e) {
			log(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ignore) {}
			}
		}
	}

	private String setArtifactVersion(String property, Properties properties, String groupId, String artifactId,
			List<Dependency> seamDependencies) {
		for (Dependency dependency:seamDependencies) {
			if (groupId.equals(dependency.getGroupId()) && artifactId.equals(dependency.getArtifactId())) {
				String version = dependency.getVersion();
				if (version != null && !version.startsWith("${")) { //$NON-NLS-1$
					properties.put(property, version);
					return version;
				}
			}
		}
		return null;
	}

	public static void log(Throwable e) {
		IStatus status = new Status(IStatus.ERROR, PLUGIN_ID, e
				.getLocalizedMessage(), e);
		getDefault().getLog().log(status);
	}
	
	public static void log(String message) {
		IStatus status = new Status(IStatus.WARNING, PLUGIN_ID, message, null);
		getDefault().getLog().log(status);
	}
	
	public String getDependencyVersion(MavenProject mavenProject, String gid, String aid) {
		List<Dependency> dependencies = mavenProject.getDependencies();
		for (Dependency dependency:dependencies) {
	    	String groupId = dependency.getGroupId();
    		if (groupId != null && (groupId.equals(gid)) ) {
    			String artifactId = dependency.getArtifactId();
    			if (artifactId != null && artifactId.equals(aid)) {
	    			return dependency.getVersion();
	    		} 
	    	}
	    }
	    return null;
	}
	
	public IDataModel createJSFDataModel(IFacetedProject fproj, IProjectFacetVersion facetVersion) {
		IDataModel config = (IDataModel) new JSFFacetInstallDataModelProvider().create();
		LibraryInstallDelegate libraryDelegate = new LibraryInstallDelegate(fproj, facetVersion);
		ILibraryProvider provider = LibraryProviderFramework.getProvider("jsf-no-op-library-provider"); //$NON-NLS-1$
		libraryDelegate.setLibraryProvider(provider);
		config.setProperty(IJSFFacetInstallDataModelProperties.LIBRARY_PROVIDER_DELEGATE, libraryDelegate);
		config.setProperty(IJSFFacetInstallDataModelProperties.SERVLET_NAME, "");
		config.setProperty(IJSFFacetInstallDataModelProperties.SERVLET_URL_PATTERNS, new String[0]);
		
		return config;
	}
}
