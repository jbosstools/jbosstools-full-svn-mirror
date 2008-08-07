/*************************************************************************************
 * Copyright (c) 2008 JBoss, a division of Red Hat and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss, a division of Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.portlet.core.internal;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.jboss.ide.eclipse.as.classpath.core.jee.AbstractClasspathContainer;
import org.jboss.ide.eclipse.as.classpath.core.jee.AbstractClasspathContainerInitializer;
import org.jboss.tools.portlet.core.IPortletConstants;

/**
 * @author snjeza
 * 
 */
public class Portlet20LibrariesContainerInitializer extends
		AbstractClasspathContainerInitializer {

	public String getDescription(IPath containerPath, IJavaProject project) {
		return "JBoss Portlet Classpath Container Initializer v2.0";
	}

	@Override
	protected AbstractClasspathContainer createClasspathContainer(IPath path) {
		return new Portlet10ClasspathContainer(path);
	}

	@Override
	protected String getClasspathContainerID() {
		return IPortletConstants.PORTLET_CONTAINER_20_ID;
	}

	private class Portlet10ClasspathContainer extends BasePortletClasspathContainer {

		public final static String DESCRIPTION = "JBoss Portlet Libraries v2.0";

		public Portlet10ClasspathContainer(IPath path) {
			super(path, DESCRIPTION, SUFFIX);
		}

		@Override
		protected String getPortletVersion() {
			return IPortletConstants.PORTLET_FACET_VERSION_20;
		}
		
	}
}
