package org.jboss.tools.portlet.core.internal;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.jboss.ide.eclipse.as.classpath.core.jee.AbstractClasspathContainer;

public abstract class BasePortletClasspathContainer extends
		AbstractClasspathContainer {

	protected static final String PORTLET_FOLDER = "portlet"; //$NON-NLS-1$
	public final static String SUFFIX = PORTLET_FOLDER;//$NON-NLS-1$
	public final static String PREFIX = "org.jboss.tools.portlet.core"; //$NON-NLS-1$

	public BasePortletClasspathContainer(IJavaProject project, IPath path,
			String description, String suffix) {
		super(path, description, suffix,  project);
	}

	public IJavaProject getProject() {
		return javaProject;
	}
}
