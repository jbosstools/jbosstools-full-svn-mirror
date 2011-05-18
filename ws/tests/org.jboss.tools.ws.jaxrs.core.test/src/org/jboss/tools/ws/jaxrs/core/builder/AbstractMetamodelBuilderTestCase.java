/******************************************************************************* 
 * Copyright (c) 2008 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Xavier Coulon - Initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.ws.jaxrs.core.builder;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.jboss.tools.ws.jaxrs.core.AbstractCommonTestCase;
import org.jboss.tools.ws.jaxrs.core.JBossJaxrsCoreTestsPlugin;
import org.jboss.tools.ws.jaxrs.core.WorkbenchTasks;
import org.jboss.tools.ws.jaxrs.core.WorkbenchUtils;
import org.jboss.tools.ws.jaxrs.core.configuration.ProjectNatureUtils;
import org.jboss.tools.ws.jaxrs.core.metamodel.Metamodel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMetamodelBuilderTestCase extends AbstractCommonTestCase {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMetamodelBuilderTestCase.class);

	protected Bundle bundle = JBossJaxrsCoreTestsPlugin.getDefault().getBundle();

	protected Metamodel metamodel;

	@Before
	public void buildMetamodel() throws CoreException, OperationCanceledException, InterruptedException {
		// WARNING : Avoid project with "Dynamic Web project" facet version "2.5" as it triggers NPE (see Bugzilla 317766 at eclipse.org)
		//ProjectFacetUtils.installFacet(project, "jst.jaxrs");
		ProjectNatureUtils.installProjectNature(project, ProjectNatureUtils.JAXRS_NATURE_ID);
		WorkbenchUtils.setAutoBuild(ResourcesPlugin.getWorkspace(), false);
		//project.build(FULL_BUILD, new NullProgressMonitor());
		WorkbenchTasks.buildProject(project, new NullProgressMonitor());
		metamodel = Metamodel.get(project);
	}

	@After
	public void removeNatureAndBuilder() throws CoreException {
		WorkbenchUtils.setAutoBuild(ResourcesPlugin.getWorkspace(),false);
		//ProjectFacetUtils.uninstallFacet(project, "jst.jaxrs");
		ProjectNatureUtils.uninstallProjectNature(project, ProjectNatureUtils.JAXRS_NATURE_ID);
	}

	protected Metamodel buildMetamodel(int mode) throws CoreException {
		LOGGER.info("Building metamodel (mode=" + mode + ")");
		javaProject.getProject().build(mode, new NullProgressMonitor());
		Metamodel metamodel = Metamodel.get(javaProject.getProject());
		Assert.assertNotNull("Metamodel not built", metamodel);
		return metamodel;
	}

}