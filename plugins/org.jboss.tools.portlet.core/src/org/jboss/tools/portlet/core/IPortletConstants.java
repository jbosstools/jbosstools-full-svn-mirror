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

import org.eclipse.core.runtime.IPath;

/**
 * @author snjeza
 * 
 */

public interface IPortletConstants {

	static final String PORTLET_FACET_ID="jboss.portlet";
	
	static final String CONFIG_PATH = "WEB-INF/portlet.xml";
	
	static final String PORTLET_FACET_VERSION_10 = "1.0";
	
	static final String PORTLET_FACET_VERSION_20 = "2.0";

	static final String PORTLET_CONTAINER_10_ID = "org.jboss.tools.portlet.core.internal.portletlibrarycontainer.v10";
	
	static final String PORTLET_CONTAINER_20_ID = "org.jboss.tools.portlet.core.internal.portletlibrarycontainer.v20";

	static final String PORTLET_INSTANCES_FILE = "WEB-INF/portlet-instances.xml";
	
}
