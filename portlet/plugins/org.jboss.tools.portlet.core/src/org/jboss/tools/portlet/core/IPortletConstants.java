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
package org.jboss.tools.portlet.core;



/**
 * @author snjeza
 * 
 */

public interface IPortletConstants {

	static final String PORTLET_FACET_ID="jboss.portlet"; //$NON-NLS-1$
	
	static final String JSFPORTLET_FACET_ID="jboss.jsfportlet"; //$NON-NLS-1$
	
	static final String SEAMPORTLET_FACET_ID="jboss.seamportlet"; //$NON-NLS-1$
	
	static final String CONFIG_PATH = "WEB-INF/portlet.xml"; //$NON-NLS-1$
	
	static final String PORTLET_FACET_VERSION_10 = "1.0"; //$NON-NLS-1$
	
	static final String PORTLET_FACET_VERSION_20 = "2.0"; //$NON-NLS-1$
	
	static final String JSFPORTLET_FACET_VERSION_10 = "1.0"; //$NON-NLS-1$

	static final String PORTLET_CONTAINER_10_ID = "org.jboss.tools.portlet.core.internal.portletlibrarycontainer.v10"; //$NON-NLS-1$
	
	static final String PORTLET_CONTAINER_20_ID = "org.jboss.tools.portlet.core.internal.portletlibrarycontainer.v20"; //$NON-NLS-1$
	
	static final String JSFPORTLET_CONTAINER_10_ID = "org.jboss.tools.portlet.core.internal.jsfportletlibrarycontainer.v10"; //$NON-NLS-1$

	static final String PORTLET_INSTANCES_FILE = "WEB-INF/portlet-instances.xml"; //$NON-NLS-1$
	
	static final String PORTLET_OBJECT_FILE = "WEB-INF/default-object.xml"; //$NON-NLS-1$
	
	static final String JBOSS_APP_FILE = "WEB-INF/jboss-app.xml"; //$NON-NLS-1$

	static final String DEPLOY_JARS = "DEPLOY_JARS"; //$NON-NLS-1$
	
	static final String PORTLET_BRIDGE_RUNTIME = "PORTLET_BRIDGE_RUNTIME"; //$NON-NLS-1$

	static final String JSF_SECTION = "jsfSection"; //$NON-NLS-1$

	static final String WEB_INF_LIB = "WEB-INF/lib"; //$NON-NLS-1$

	static final String JBOSS_PORTLET_FILE = "WEB-INF/jboss-portlet.xml"; //$NON-NLS-1$
	
}
