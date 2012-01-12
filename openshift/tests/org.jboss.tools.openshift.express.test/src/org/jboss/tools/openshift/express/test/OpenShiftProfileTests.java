/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.express.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.tools.openshift.express.internal.ui.wizard.appimport.OpenShiftProfile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Andre Dietisheim
 */
public class OpenShiftProfileTests {

	private static final String PLUGIN_ID = "org.jboss.tools.openshift.express.test";

	private static final String POM_FILENAME = "pom.xml";

	private static final String POM_WITHOUT_OPENSHIFT =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
					+ "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" "
					+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
					+ "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
					+ "	<modelVersion>4.0.0</modelVersion>\n"
					+ "	<groupId>org.jboss.tools.openshift.tests</groupId>\n"
					+ "	<artifactId>org.jboss.tools.openshift.express.test</artifactId>\n"
					+ "	<packaging>eclipse-test-plugin</packaging>\n"
					+ "</project>\n";

	private static final String OPENSHIFT_PROFILE =
			"<!-- When built in OpenShift the 'openshift' profile will be used when invoking mvn. -->\n"
					+ "<!-- Use this profile for any OpenShift specific customization your app will need. -->\n"
					+ "<!-- By default that is to put the resulting archive into the 'deployments' folder. -->\n"
					+ "<!-- http://maven.apache.org/guides/mini/guide-building-for-different-environments.html -->\n"
					+ "<id>openshift</id>\n"
					+ "<build>\n"
					+ "  <finalName>as22</finalName>\n"
					+ "  <plugins>\n"
					+ "    <plugin>\n"
					+ "      <artifactId>maven-war-plugin</artifactId>\n"
					+ "      <version>2.1.1</version>\n"
					+ "      <configuration>\n"
					+ "        <outputDirectory>deployments</outputDirectory>\n"
					+ "        <warName>ROOT</warName>\n"
					+ "      </configuration>\n"
					+ "    </plugin>\n"
					+ "  </plugins>\n"
					+ "</build>\n";

	private static final String POM_WITH_OPENSHIFT =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
					+ "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" "
					+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
					+ "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
					+ "	<modelVersion>4.0.0</modelVersion>\n"
					+ "	<groupId>org.jboss.tools.openshift.tests</groupId>\n"
					+ "	<artifactId>org.jboss.tools.openshift.express.test</artifactId>\n"
					+ "	<packaging>eclipse-test-plugin</packaging>\n"
					+ " <profiles>\n"
					+ "   <profile>\n"

					+ OPENSHIFT_PROFILE

					+ "   </profile>\n"
					+ " </profiles>\n"
					+ "</project>\n";

	private IProject nonOpenShiftProject;
	private IFile pomWithoutOpenShiftProfile;
	private IProject openShiftProject;
	private IFile pomWithOpenShiftProfile;

	@Before
	public void setUp() throws CoreException {
		this.openShiftProject = createTmpProject();
		this.pomWithOpenShiftProfile = createPomFile(POM_WITH_OPENSHIFT, openShiftProject);
		this.nonOpenShiftProject = createTmpProject();
		this.pomWithoutOpenShiftProfile = createPomFile(POM_WITHOUT_OPENSHIFT, nonOpenShiftProject);
	}

	@After
	public void tearDown() throws CoreException {
		deleteProject(openShiftProject);
		deleteProject(nonOpenShiftProject);
	}

	private void deleteProject(final IProject project) throws CoreException {
		if (project == null
				|| !project.isAccessible()) {
			return;
		}
		project.getWorkspace().run(new IWorkspaceRunnable() {
			
			@Override
			public void run(IProgressMonitor monitor) throws CoreException {
				project.close(null);
				project.delete(true, null);
			}
		}, null);
	}

	@Test
	public void canDetectOpenShiftProfileNotPresent() throws CoreException {
		OpenShiftProfile profile = new OpenShiftProfile(pomWithoutOpenShiftProfile, PLUGIN_ID);
		assertFalse(profile.existsInPom());
	}

	@Test
	public void canDetectOpenShiftProfilePresent() throws CoreException {
		OpenShiftProfile profile = new OpenShiftProfile(pomWithOpenShiftProfile, PLUGIN_ID);
		assertTrue(profile.existsInPom());
	}

	@Test
	public void canAddOpenShiftProfile() throws CoreException {
		OpenShiftProfile profile = new OpenShiftProfile(pomWithoutOpenShiftProfile, PLUGIN_ID);
		boolean added = profile.addToPom(nonOpenShiftProject.getName());
		assertTrue(added);
	}

	@Test
	public void pomHasOpenShiftProfileAfterAdd() throws CoreException {
		OpenShiftProfile profile = new OpenShiftProfile(pomWithoutOpenShiftProfile, PLUGIN_ID);
		profile.addToPom(nonOpenShiftProject.getName());
		profile.savePom();
		profile = new OpenShiftProfile(pomWithoutOpenShiftProfile, PLUGIN_ID);
		assertTrue(profile.existsInPom());
	}

	@Test
	public void doesNotAddOpenShiftProfileIfAlreadyPresent() throws CoreException {
		OpenShiftProfile profile = new OpenShiftProfile(pomWithOpenShiftProfile, PLUGIN_ID);
		boolean added = profile.addToPom(openShiftProject.getName());
		assertFalse(added);
	}

	private IFile createPomFile(final String content, final IProject project) throws CoreException {
		final IFile pomFile = project.getFile(POM_FILENAME);
		IWorkspaceRunnable runnable = new IWorkspaceRunnable() {

			@Override
			public void run(IProgressMonitor monitor) throws CoreException {
				pomFile.create(
						new ByteArrayInputStream(content.getBytes())
						, true
						, new NullProgressMonitor());
				pomFile.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
			}
		};
		project.getWorkspace().run(runnable, new NullProgressMonitor());
		return pomFile;
	}

	private IProject createTmpProject() throws CoreException {
		String name = String.valueOf(System.currentTimeMillis());
		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
		IWorkspaceRunnable runnable = new IWorkspaceRunnable() {

			@Override
			public void run(IProgressMonitor monitor) throws CoreException {
				project.create(new NullProgressMonitor());
				project.open(new NullProgressMonitor());
			}
		};
		project.getWorkspace().run(runnable, new NullProgressMonitor());
		return project;
	}
}
