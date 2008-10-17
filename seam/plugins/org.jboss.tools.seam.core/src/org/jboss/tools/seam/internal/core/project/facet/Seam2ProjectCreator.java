 /*******************************************************************************
  * Copyright (c) 2008 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.seam.internal.core.project.facet;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.tools.ant.types.FilterSet;
import org.apache.tools.ant.types.FilterSetCollection;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.jboss.tools.seam.core.SeamCorePlugin;

/**
 * @author Alexey Kazakov
 * This class helps New Seam Wizard Page to create EJB, EAR and test projects for seam 2.* WAR project.
 */
public class Seam2ProjectCreator extends SeamProjectCreator {

	// test/*.jar are duplicated here since the filtering seem to be assymetric when matching 
	private static AntCopyUtils.FileSet JBOSS_TEST_LIB_FILESET = new AntCopyUtils.FileSet()
	    .include("testng\\.jar") //$NON-NLS-1$
		.include("test/hibernate-all\\.jar") //$NON-NLS-1$
		.include("hibernate-all\\.jar") //$NON-NLS-1$
		.include("test/jboss-embedded-all.jar") //$NON-NLS-1$
		.include("jboss-embedded-all.jar") //$NON-NLS-1$
		.include("test/jboss-embedded-api.jar") //$NON-NLS-1$
		.include("jboss-embedded-api.jar") //$NON-NLS-1$
		.include("test/jboss-deployers.jar") //$NON-NLS-1$		
		.include("jboss-deployers.jar") //$NON-NLS-1$
		.include("test/thirdparty-all\\.jar") //$NON-NLS-1$
		.include("thirdparty-all\\.jar") //$NON-NLS-1$
		.include("core.jar") //$NON-NLS-1$
		.exclude(".*/CVS") //$NON-NLS-1$
		.exclude(".*/\\.svn"); //$NON-NLS-1$

	/**
	 * @param model Seam facet data model
	 * @param seamWebProject Seam web project
	 */
	public Seam2ProjectCreator(IDataModel model, IProject seamWebProject) {
		super(model, seamWebProject);
		viewFilterSetCollection.addFilterSet(SeamFacetFilterSetFactory.createHibernateDialectFilterSet(model));
		droolsLibFolder = new File(seamHomePath, Seam2FacetInstallDelegate.DROOLS_LIB_SEAM_RELATED_PATH);
	}

	@Override
	protected void createTestProject() {
		File testProjectDir = new File(seamWebProject.getLocation().removeLastSegments(1).toFile(), testProjectName); //$NON-NLS-1$
		testProjectDir.mkdir();

		IVirtualComponent component = ComponentCore.createComponent(seamWebProject);
		IVirtualFolder webRootVirtFolder = component.getRootFolder().getFolder(new Path("/")); //$NON-NLS-1$

		File testLibDir = new File(testProjectDir,"lib"); //$NON-NLS-1$
		File embededEjbDir = new File(testProjectDir,"bootstrap"); //$NON-NLS-1$
		File testSrcDir = new File(testProjectDir,"test-src"); //$NON-NLS-1$
		String seamGenResFolder = seamRuntime.getResourceTemplatesDir();
		File dataSourceFile = new File(seamGenResFolder, "datasource-ds.xml");
		File seamPropertiesFile = new File(seamGenResFolder, "seam.properties");
		//File jbossBeansFile = new File(seamGenResFolder ,"META-INF/jboss-beans.xml"); //$NON-NLS-1$
		FilterSet filterSet = new FilterSet();
		filterSet.addFilter("projectName", seamWebProject.getName()); //$NON-NLS-1$
		filterSet.addFilter("runtimeName", WtpUtils.getServerRuntimeName(seamWebProject)); //$NON-NLS-1$
		filterSet.addFilter("webRootFolder",webRootVirtFolder.getUnderlyingFolder().getFullPath().removeFirstSegments(1).toString()); //$NON-NLS-1$

		FilterSet jdbcFilterSet = SeamFacetFilterSetFactory.createJdbcFilterSet(model);
		// TODO: why are these filters not shared!?
		filterSet.addConfiguredFilterSet(SeamFacetFilterSetFactory.createHibernateDialectFilterSet(model));

		AntCopyUtils.FileSet includeLibs = new AntCopyUtils.FileSet(getJBossTestLibFileset()).dir(new File(seamRuntime.getHomeDir(),"lib")); //$NON-NLS-1$
		AntCopyUtils.FileSet secondSetincludeLibs = new AntCopyUtils.FileSet(getJBossTestLibFileset()).dir(new File(seamRuntime.getHomeDir(),"lib/test")); //$NON-NLS-1$

		File[] firstlibs = includeLibs.getDir().listFiles(new AntCopyUtils.FileSetFileFilter(includeLibs));
		File[] secondLibs = secondSetincludeLibs.getDir().listFiles(new AntCopyUtils.FileSetFileFilter(secondSetincludeLibs));
		Set<String> allLibs = new HashSet<String>(); // HACK: needed to be unique because some jboss-*.jars are duplicated
		for(File f : firstlibs) {
			allLibs.add(f.getName());
		}
		for(File f : secondLibs) {
			allLibs.add(f.getName());
		}

		StringBuffer testLibraries = new StringBuffer();

		for (String file : allLibs) {
			testLibraries.append("\t<classpathentry kind=\"lib\" path=\"lib/" + file + "\"/>\n"); //$NON-NLS-1$ //$NON-NLS-2$
		}			

		StringBuffer requiredProjects = new StringBuffer();
		requiredProjects.append(
				"\t<classpathentry combineaccessrules=\"false\" kind=\"src\" path=\"/" + seamWebProject.getName() + "\"/>"); //$NON-NLS-1$ //$NON-NLS-2$
		if(!SeamFacetAbstractInstallDelegate.isWarConfiguration(model)) {
			requiredProjects.append("\n\t<classpathentry combineaccessrules=\"false\" kind=\"src\" path=\"/" + ejbProjectName + "\"/>"); //$NON-NLS-1$ //$NON-NLS-2$
		} 
		filterSet.addFilter("testLibraries",testLibraries.toString()); //$NON-NLS-1$
		filterSet.addFilter("requiredProjects",requiredProjects.toString()); //$NON-NLS-1$
		File testTemplateDir = null;
		try {
			testTemplateDir = new File(SeamFacetInstallDataModelProvider.getTemplatesFolder(), "test-seam2"); //$NON-NLS-1$
		} catch (IOException e) {
			SeamCorePlugin.getPluginLog().logError(e);
			return;
		}
		AntCopyUtils.FileSet excludeCvsSvn 
				 = new AntCopyUtils.FileSet(SeamFacetAbstractInstallDelegate.CVS_SVN).dir(testTemplateDir);

		AntCopyUtils.copyFilesAndFolders(
				testTemplateDir,
				testProjectDir,
				new AntCopyUtils.FileSetFileFilter(excludeCvsSvn),
				new FilterSetCollection(filterSet), true);

		excludeCvsSvn.dir(new File(seamRuntime.getHomeDir(), "bootstrap")); //$NON-NLS-1$
		AntCopyUtils.copyFilesAndFolders(
				new File(seamRuntime.getHomeDir(), "bootstrap"), //$NON-NLS-1$
				embededEjbDir,
				new AntCopyUtils.FileSetFileFilter(excludeCvsSvn), new FilterSetCollection(), true);

//			AntCopyUtils.copyFileToFile(
//					persistenceFile,
//					new File(testProjectDir,"test-src/META-INF/persistence.xml"), //$NON-NLS-1$
//					new FilterSetCollection(filterSet), true);

		FilterSetCollection f = new FilterSetCollection();
		f.addFilterSet(filterSet);
		f.addFilterSet(jdbcFilterSet);

		AntCopyUtils.copyFileToFile(
				dataSourceFile,
				new File(testProjectDir, "test-src/META-INF/"+seamWebProject.getName() + "-test-ds.xml"), //$NON-NLS-1$
				f, true);

		AntCopyUtils.copyFileToFolder(
				seamPropertiesFile,
				testSrcDir, //$NON-NLS-1$
				new FilterSetCollection(filterSet), true);

		AntCopyUtils.copyFiles(
				new File(seamRuntime.getHomeDir(), "lib"), //$NON-NLS-1$
				testLibDir,
				new AntCopyUtils.FileSetFileFilter(includeLibs));

		//seam2 has a lib/test
		AntCopyUtils.copyFiles(
				new File(seamRuntime.getHomeDir(), "lib/test"), //$NON-NLS-1$
				testLibDir,
				new AntCopyUtils.FileSetFileFilter(includeLibs));

		SeamFacetAbstractInstallDelegate.createComponentsProperties(testSrcDir, "", true); //$NON-NLS-1$
	}

	@Override
	protected void createEjbProject() {
		super.createEjbProject();
		// Copy security.drl to source folder
		AntCopyUtils.copyFileToFolder(new File(seamGenResFolder, "security.drl"), new File(ejbProjectFolder, "ejbModule/"), true); //$NON-NLS-1$ //$NON-NLS-2$
	}

	protected AntCopyUtils.FileSet getJBossTestLibFileset() {
		return JBOSS_TEST_LIB_FILESET;
	}

	protected AntCopyUtils.FileSet getJbossEarContent() {
		return Seam2FacetInstallDelegate.JBOSS_EAR_CONTENT;
	}
}