/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.shale.model.spring;

import org.jboss.tools.jsf.web.ConfigFilesData;

public interface SpringBeansConstants {
	public String DOC_PUBLICID = "-//SPRING//DTD BEAN//EN";
	public String DOC_EXTDTD = "http://www.springframework.org/dtd/spring-beans.dtd";
	
	public String ENT_FILE_SPRING_BEANS = "FileSpringBeans";
	public String MAP_ENTITY = "SpringBeansMap";
	public String ENTRY_ENTITY = "SpringBeansEntry";
	public String LIST_ENTITY = "SpringBeansList";
	public String SET_ENTITY = "SpringBeansSet";

	public ConfigFilesData REGISTRATION_DATA = new ConfigFilesData(
		"contextConfigLocation",
		new String[0],
		" "
	);
	
}
